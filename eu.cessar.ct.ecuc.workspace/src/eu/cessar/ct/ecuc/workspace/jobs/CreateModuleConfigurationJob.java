package eu.cessar.ct.ecuc.workspace.jobs;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucContainerCommand;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucModuleConfigurationCommand;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucReferenceCommand;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucContainerCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucModuleConfigurationCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucReferenceCommandParameter;
import eu.cessar.ct.ecuc.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.internal.Messages;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;

public class CreateModuleConfigurationJob extends AbstractCreateModuleFileJob
{
	protected static final String JOB_NAME = "Create Module Configuration"; //$NON-NLS-1$

	protected GModuleDef moduleDefinition;
	protected String moduleConfigurationName;
	protected boolean createMandatoryElements = false;
	protected boolean createOptionalElements = false;
	protected boolean createElementsBasedOnUpperMultiplicity = false;
	protected int maximumElementsCount = 0;

	public CreateModuleConfigurationJob(IProject project, IFile outputFile, boolean overwriteFile, boolean appendToFile)
	{
		super(JOB_NAME, project, outputFile, overwriteFile, appendToFile);
	}

	@Override
	protected ISchedulingRule getJobRule()
	{
		return MultiRule.combine(ExtendedPlatform.createSaveSchedulingRule(getProject()),
			ExtendedPlatform.createSaveNewSchedulingRule(outputFile));
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor)
	{
		IStatus result = Status.OK_STATUS;

		final Exception[] error = new Exception[1];
		try
		{
			monitor.beginTask(Messages.job_creatingModuleConfiguration, IProgressMonitor.UNKNOWN);

			// Check validity
			assertValid();
			// Obtain/create the root object
			monitor.subTask(Messages.CessarModuleConfigurationJob_GetModelRoot_Task);
			final EObject rootObject = (EObject) getModelRoot();
			Assert.isNotNull(rootObject);
			// Execute all project changes in a write transaction
			WorkspaceTransactionUtil.executeInWriteTransaction(getEditingDomain(), new Runnable()
			{
				public void run()
				{
					try
					{
						// Obtain/create the destination package.
						monitor.subTask(Messages.CessarModuleConfigurationJob_GetDestinationPackage_Task);
						GARPackage arPackage = getDestinationPackage(rootObject);

						// Create the new EcuC module configuration
						monitor.subTask(Messages.CessarModuleConfigurationJob_CreateModuleConf_Task);
						GModuleConfiguration moduleConfiguration = createModuleConfiguration(arPackage,
							moduleDefinition);

						// Add containers and parameters, if required
						if (createMandatoryElements)
						{
							monitor.subTask(Messages.CessarModuleConfigurationJob_AddMandatoryElements_Task);
							addMandatoryElements(moduleConfiguration);
						}

					}
					catch (Exception e)
					{
						error[0] = e;
					}
				}
			}, Messages.job_creatingModuleConfiguration);
			// Save the result
			monitor.subTask(Messages.CessarModuleConfigurationJob_SaveModuleConf_Task);
			persistModel(rootObject, monitor);
			monitor.done();
		}
		catch (Exception e)
		{
			result = CessarPluginActivator.getDefault().createStatus(e);
		}

		if (null != error[0])
		{
			result = CessarPluginActivator.getDefault().createStatus(error[0]);
		}
		return result;
	}

	/**
	 * Checks all assumptions. Throws exception if assumptions not validated.
	 * 
	 * @throws CoreException
	 */
	@Override
	public void assertValid() throws CoreException
	{
		try
		{
			IProject project = getProject();

			// Must have a valid EcuC project
			Assert.isTrue(null != project, Messages.CessarModuleConfigurationJob_NoProject);
			Assert.isTrue(IEcucCore.INSTANCE.isValidEcucProject(project),
				Messages.CessarModuleConfigurationJob_InvalidProject);

			// Must have a module definition
			Assert.isTrue(null != moduleDefinition,
				NLS.bind(Messages.CessarModuleConfigurationJob_NoModuleDefinition, project.getName()));

			// Must have remaining multiplicity for the given Module Definition
			Assert.isTrue(
				hasRemainingMultiplicity(moduleDefinition),
				NLS.bind(Messages.CessarModuleConfigurationJob_NoRemainingMulitplicity,
					MetaModelUtils.getAbsoluteQualifiedName(moduleDefinition)));

			// Must have a module module configuration name
			Assert.isTrue(null != moduleConfigurationName && 0 != moduleConfigurationName.length(),
				Messages.CessarModuleConfigurationJob_NoModuleConfigurationName);

			// Must have a destination package
			Assert.isTrue(
				null != destinationPackageQName && 0 != destinationPackageQName.length(),
				NLS.bind(Messages.CessarModuleConfigurationJob_NoDestinationPackage, destinationPackageQName,
					project.getName()));

			// Must have an output file
			Assert.isTrue(null != outputFile, Messages.CessarModuleConfigurationJob_NoFile);
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Creates a new EcuC Module Configuration in the given package, based on the given Module Definition
	 * 
	 * @param arPackage
	 *        Package in which the new EcuC Module Configuration
	 * @param moduleDefinition
	 *        EcuC Module Definition template for the new Module Configuration
	 * @return Newly created EcuC Module Configuration
	 * @throws CoreException
	 */
	protected GModuleConfiguration createModuleConfiguration(GARPackage arPackage, final GModuleDef moduleDefinition)
		throws CoreException
	{
		try
		{
			// Execute command to create new Module Configuration and install it
			// into parent package's elements.
			final NewEcucModuleConfigurationCommand command = new NewEcucModuleConfigurationCommand(
				new NewEcucModuleConfigurationCommandParameter(arPackage, moduleDefinition, moduleConfigurationName),
				null);
			Assert.isTrue(command.canExecute());
			command.execute();

			// Check result
			Object commandResult = command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			return (GModuleConfiguration) commandResult;
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Creates and adds instances for all required containers and parameters (for the latter default values are
	 * required).
	 * 
	 * @param moduleConfiguration
	 *        EcuC Module Configuration to be completed
	 * @param mandatoryElementsCount
	 *        One of the MandatoryElementsCount options: if NONE, no elements are created, otherwise the method will
	 *        automatically create as many containers as indicated (a number equal with either lower or upper
	 *        multiplicity as set in the definition)
	 * 
	 * 
	 * @throws CoreException
	 */
	protected void addMandatoryElements(GModuleConfiguration moduleConfiguration) throws OperationCanceledException,
		CoreException
	{
		try
		{
			GModuleDef moduleDefinition = moduleConfiguration.gGetDefinition();
			for (final GContainerDef containerDef: moduleDefinition.gGetContainers())
			{
				long containersCount = getNumberOfInstancesToCreate(containerDef);
				for (long index = 0; index < containersCount; ++index)
				{
					createContainer(moduleConfiguration, containerDef);
				}
			}
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Create a Choice container as child of the given parent (either {@link GModuleConfiguration} or {@link GContainer}
	 * ) based on the given {@link GChoiceContainerDef}.
	 * 
	 * Depending on parent's multiplicity, a number of instances are created of the first available choice. If needed
	 * (versions 3.x and 4.0) and intermediate container is created for choices, based on the given
	 * {@link GChoiceContainerDef}.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param containerDef
	 *        definition of the new container
	 * @return newly created container
	 * @throws CoreException
	 */
	protected GContainer createChoiceContainer(GIdentifiable parent, final GChoiceContainerDef containerDef)
		throws CoreException
	{
		try
		{
			// Get the choices list
			List<? extends GParamConfContainerDef> choices = (containerDef).gGetChoices();
			if (null == choices || choices.isEmpty())
			{
				// There are no choices available - cannot create anything
				return null;
			}

			// Select the first choice as default, create this choice instead of
			// parent container
			GContainerDef choice = choices.get(0);

			// Execute command to create new Container and install it
			// into parent's containers list.
			final NewEcucContainerCommandParameter cmdParameter = new NewEcucContainerCommandParameter(parent, choice);
			cmdParameter.setChoiceContainerDefinition(containerDef);
			final NewEcucContainerCommand command = new NewEcucContainerCommand(cmdParameter, null);
			Assert.isTrue(command.canExecute());
			command.execute();

			GIdentifiable commandResult = (GIdentifiable) command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			GContainer container = (GContainer) commandResult;

			return container;
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Create a ParamConf container as child of the given parent (either {@link GModuleConfiguration} or
	 * {@link GContainer}) based on the given {@link GParamConfContainerDef}.
	 * 
	 * Sub-containers, parameters and references with lower multiplicity > 0 are automatically added.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param containerDef
	 *        definition of the new container
	 * @return newly created container
	 * @throws CoreException
	 */
	protected GContainer createParamConfContainer(GIdentifiable parent, final GParamConfContainerDef containerDef)
		throws CoreException
	{
		try
		{
			// Execute command to create new Container and install it
			// into parent's containers list.
			final NewEcucContainerCommand command = new NewEcucContainerCommand(new NewEcucContainerCommandParameter(
				parent, containerDef), null);
			Assert.isTrue(command.canExecute());
			command.execute();

			GIdentifiable commandResult = (GIdentifiable) command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			GContainer container = (GContainer) commandResult;

			// Recurse sub-containers
			EList<? extends GContainerDef> subContainers = containerDef.gGetSubContainers();
			for (final GContainerDef subContainerDef: subContainers)
			{
				long containersCount = getNumberOfInstancesToCreate(subContainerDef);
				for (long index2 = 0; index2 < containersCount; ++index2)
				{
					createContainer(container, subContainerDef);
				}
			}

			// Create parameters
			EList<? extends GConfigParameter> parameters = containerDef.gGetParameters();
			for (GConfigParameter parameter: parameters)
			{
				// Create parameter will actually only create parameters that
				// have default values.
				long parametersCount = getNumberOfInstancesToCreate(parameter);
				for (long index3 = 0; index3 < parametersCount; ++index3)
				{
					createParameter(container, parameter);
				}
			}

			// TT: Removed reference creation: there's no default value for them
			// Create references
			// EList<? extends GConfigReference> references =
			// containerDef.gGetReferences();
			// for (GConfigReference reference: references)
			// {
			// long referencesCount = getNumberOfInstancesToCreate(reference);
			// for (long index4 = 0; index4 < referencesCount; ++index4)
			// {
			// createReference(container, reference);
			// }
			// }
			return container;
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Create a container as child of the given parent (either {@link GModuleConfiguration} or {@link GContainer}) based
	 * on the given {@link GContainerDef}.
	 * 
	 * The new container can either by ParamConf or Choice, depending on the type of containerDef.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param containerDef
	 *        definition of the new container
	 * @param index
	 *        index of this child within parent's children of its kind
	 * @return newly created container
	 * @throws CoreException
	 */
	protected GContainer createContainer(GIdentifiable parent, final GContainerDef containerDef) throws CoreException
	{
		try
		{
			if (containerDef instanceof GChoiceContainerDef)
			{
				return createChoiceContainer(parent, (GChoiceContainerDef) containerDef);
			}
			else if (containerDef instanceof GParamConfContainerDef)
			{
				return createParamConfContainer(parent, (GParamConfContainerDef) containerDef);
			}
			Assert.isTrue(false,
				NLS.bind(Messages.CessarModuleConfigurationJob_InvalidContainerType, containerDef.gGetShortName()));
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
		return null;
	}

	/**
	 * If the given {@link GConfigParameter} definition has a default value, this method creates a new parameter based
	 * on that definition, and sets its value to definition's defaultValue. Otherwise, null is returned.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param parameter
	 *        definition of the new parameter
	 * @return newly created parameter with value set on definition's defaultValue, or null if no default value is set.
	 */
	@SuppressWarnings("unchecked")
	protected GParameterValue createParameter(GContainer container, GConfigParameter parameter)
	{
		try
		{
			GParameterValue parameterValue = null;

			// Create directly in this case: the command creates a new parameter
			// regardless of whether the parameter has a default value set.
			IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(container);
			if (genericFactory != null)
			{
				parameterValue = genericFactory.createParameterValueWithDefault(parameter);
				if (parameterValue != null)
				{
					parameterValue.gSetDefinition(parameter);
					EList<? extends GParameterValue> parameterValues = container.gGetParameterValues();
					EList tmpList = parameterValues;
					tmpList.add(parameterValue);
				}
			}
			return parameterValue;
		}
		catch (Exception e)
		{
			// Changed to log the error, but not throw - execution should
			// continue with the next mandatory element
			// throw new CoreException(Activator.getDefault().createStatus(e));
			CessarPluginActivator.getDefault().logError(e);
			return null;
		}
	}

	/**
	 * Create a new Reference value child of container, based on the given {@link GConfigReference} definition.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param reference
	 *        definition of the new reference
	 * @return newly created reference
	 */
	protected GConfigReferenceValue createReference(GContainer container, GConfigReference reference)
	{
		try
		{
			final NewEcucReferenceCommand command = new NewEcucReferenceCommand(new NewEcucReferenceCommandParameter(
				container, reference), null);
			Assert.isTrue(command.canExecute());
			command.execute();

			Object commandResult = command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			return (GConfigReferenceValue) commandResult;
		}
		catch (Exception e)
		{
			// Changed to log the error, but not throw - execution should
			// continue with the next mandatory element
			// throw new CoreException(Activator.getDefault().createStatus(e));
			CessarPluginActivator.getDefault().logError(e);
			return null;
		}
	}

	/**
	 * Given an object with multiplicity, this method calculates and returns the number of instances of this object that
	 * have to be created, considering the creation flags set in the job:<br>
	 * 
	 * <ulist><li>if createElementsBasedOnUpperMultiplicity is set, upper multiplicity is returned, but not more than
	 * maximumElementsCount;</li><br>
	 * 
	 * <li>if createElementsBasedOnUpperMultiplicity is not set, but createOptionalElements is set, lower multiplicity
	 * is returned, but not less than 1;</li><br>
	 * 
	 * <li>if createElementsBasedOnUpperMultiplicity is not set, createOptionalElements is not set, but
	 * createMandatoryElements is set, lower multiplicity is returned;</li><br>
	 * 
	 * <li>if none of the above applies, the method returns 0.</li></ulist><br>
	 * <br>
	 * 
	 * @param object
	 *        Object for which the number of instances to be created is queried.
	 * @return Number of instances of this object to create
	 */
	protected long getNumberOfInstancesToCreate(GParamConfMultiplicity object)
	{
		if (createElementsBasedOnUpperMultiplicity)
		{
			BigInteger upperMultiplicity = MultiplicityUtils.getUpperMultiplicity(object);
			if (MultiplicityUtils.isInfinite(upperMultiplicity))
			{
				return maximumElementsCount;
			}
			return Math.min(upperMultiplicity.longValue(), maximumElementsCount);
		}

		BigInteger lowerMultiplicity = MultiplicityUtils.getLowerMultiplicity(object);
		if (createOptionalElements && MultiplicityUtils.isZero(lowerMultiplicity))
		{
			return 1;
		}

		if (createMandatoryElements)
		{
			return lowerMultiplicity.longValue();
		}

		return 0;
	}

	/**
	 * Checks if the given {@link GModuleDef} object has remaining multiplicity:
	 * <ul>
	 * <li>if the upper multiplicity infinite feature is set, returns true;</li>
	 * <li>if the upper multiplicity feature is not set, returns true;</li>
	 * <li>if the upper multiplicity feature is smaller than the number of {@link GModuleConfiguration}s using this
	 * definition, returns true;</li>
	 * <li>otherwise returns false</li>.
	 * </ul>
	 * 
	 * @param moduleDefinition
	 *        {@link GModuleDef} whose upper multiplicity is checked
	 * @return true if the {@link GModuleDef} has remaining multiplicity, false otherwise.
	 */
	protected boolean hasRemainingMultiplicity(GModuleDef moduleDefinition)
	{
		Assert.isNotNull(moduleDefinition);

		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(moduleDefinition).getEcucMMService();

		BigInteger upper = ecucMMService.getUpperMultiplicity(moduleDefinition, BigInteger.ONE, true);

		if (upper.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			return true;
		}
		else
		{
			// Get the number of GModuleConfiguration elements using this module
			// definition
			long usedMultiplicity = 0;
			IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(getProject());
			List<GModuleConfiguration> moduleConfigurations = ecucModel.getAllModuleCfgs();
			for (GModuleConfiguration moduleConfiguration: moduleConfigurations)
			{
				if (moduleConfiguration.gGetDefinition().equals(moduleDefinition))
				{
					++usedMultiplicity;
				}
			}

			long longUpper = upper.longValue();
			return longUpper > usedMultiplicity;
		}
	}

	public void setModuleDefinition(GModuleDef moduleDefinition)
	{
		this.moduleDefinition = moduleDefinition;
	}

	@Override
	public void setDestinationPackageQName(String destinationPackageQName)
	{
		this.destinationPackageQName = destinationPackageQName;
	}

	public void setModuleConfigurationName(String moduleConfigurationName)
	{
		this.moduleConfigurationName = moduleConfigurationName;
	}

	public void setCreateMandatoryElements(boolean createMandatory)
	{
		createMandatoryElements = createMandatory;
		if (!createMandatory)
		{
			createOptionalElements = false;
			createElementsBasedOnUpperMultiplicity = false;
			maximumElementsCount = 0;
		}
	}

	public void setCreateOptionalElements(boolean createOptional)
	{
		createOptionalElements = createOptional && createMandatoryElements;
	}

	public void setCreateBasedOnUpperMultiplicity(boolean createBasedOnUpperMultiplicity, int maxElements)
	{
		createElementsBasedOnUpperMultiplicity = createBasedOnUpperMultiplicity && createMandatoryElements;
		maximumElementsCount = maxElements;
	}
}
