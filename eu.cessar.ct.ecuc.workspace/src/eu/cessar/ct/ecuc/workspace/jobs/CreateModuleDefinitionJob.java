package eu.cessar.ct.ecuc.workspace.jobs;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;

import eu.cessar.ct.core.mms.ecuc.commands.CopyEcucModuleDefinitionCommand;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucModuleDefinitionCommand;
import eu.cessar.ct.core.mms.ecuc.commands.RefinedCopyEcucModuleDefinitionCommand;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucModuleDefinitionCommandParameter;
import eu.cessar.ct.ecuc.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.internal.Messages;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * The Class CreateModuleDefinitionJob creates and execute the scheduled job in a monitored transaction.
 */
public class CreateModuleDefinitionJob extends AbstractCreateModuleFileJob
{

	/** The Constant JOB_NAME. */
	protected static final String JOB_NAME = "Create Module Definition"; //$NON-NLS-1$

	/** The module definition type. */
	protected ModuleDefinitionType moduleDefinitionType;

	/** The module definition. */
	protected GModuleDef moduleDefinition;

	/** The module definition name. */
	protected String moduleDefinitionName;

	/**
	 * Instantiates a new module definition job.
	 *
	 * @param project
	 *        the project
	 * @param outputFile
	 *        the output file
	 * @param overwriteFile
	 *        the overwrite file
	 * @param appendToFile
	 *        the append to file
	 */
	public CreateModuleDefinitionJob(IProject project, IFile outputFile, boolean overwriteFile, boolean appendToFile)
	{
		super(JOB_NAME, project, outputFile, overwriteFile, appendToFile);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.ecuc.workspace.jobs.AbstractCreateModuleFileJob#getJobRule()
	 */
	@Override
	protected ISchedulingRule getJobRule()
	{
		return MultiRule.combine(ExtendedPlatform.createSaveSchedulingRule(getProject()),
			ExtendedPlatform.createSaveNewSchedulingRule(outputFile));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.ecuc.workspace.jobs.AbstractCreateModuleFileJob#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(final IProgressMonitor monitor)
	{
		IStatus result = Status.OK_STATUS;

		final Exception[] error = new CoreException[1];
		try
		{
			monitor.beginTask(Messages.job_creatingModuleDefinition, IProgressMonitor.UNKNOWN);

			// Check validity
			assertValid();
			// Obtain/create the root object
			monitor.subTask(Messages.CessarModuleDefinitionJob_GetModelRoot_Task);
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
						monitor.subTask(Messages.CessarModuleDefinitionJob_GetDestinationPackage_Task);
						GARPackage arPackage = getDestinationPackage(rootObject);

						// Create the new EcuC module configuration
						monitor.subTask(Messages.CessarModuleDefinitionJob_CreateModuleDef_Task);
						createModuleDefinition(arPackage, moduleDefinition);

					}
					catch (Exception e)
					{
						error[0] = e;
					}

				}
			}, Messages.job_creatingModuleDefinition);

			// Save the result
			monitor.subTask(Messages.CessarModuleDefinitionJob_SaveModuleDef_Task);
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
	 *         the core exception
	 */
	@Override
	public void assertValid() throws CoreException
	{
		try
		{
			IProject currentProject = getProject();

			// Must have a valid EcuC project
			Assert.isTrue(null != currentProject, Messages.CessarModuleDefinitionJob_NoProject);
			Assert.isTrue(IEcucCore.INSTANCE.isValidEcucProject(currentProject),
				Messages.CessarModuleDefinitionJob_InvalidProject);

			// Must have a module definition to work with, if not creating pure
			// vendor
			Assert.isTrue(null != moduleDefinition || (ModuleDefinitionType.PURE_VENDOR == moduleDefinitionType),
				NLS.bind(Messages.CessarModuleDefinitionJob_NoModuleDefinition, currentProject.getName()));

			// If creating a standard module, the module must not already
			// be in the project

			if (ModuleDefinitionType.STANDARD == moduleDefinitionType)
			{
				Assert.isTrue(false == moduleAlreadyPresent(moduleDefinition), NLS.bind(
					Messages.CessarModuleDefinitionJob_StandardModulePresent, moduleDefinition.gGetShortName()));
			}
			if (ModuleDefinitionType.EXTERNAL_STANDARD == moduleDefinitionType)
			{
				Assert.isTrue(false == moduleAlreadyPresent(moduleDefinition), NLS.bind(
					Messages.CessarModuleDefinitionJob_StandardModulePresent, moduleDefinition.gGetShortName()));
			}

			// Must have a module module definition name
			Assert.isTrue(null != moduleDefinitionName && 0 != moduleDefinitionName.length(),
				Messages.CessarModuleDefinitionJob_NoModuleDefinitionName);

			// Must have a destination package
			Assert.isTrue(null != destinationPackageQName && 0 != destinationPackageQName.length(),
				NLS.bind(Messages.CessarModuleDefinitionJob_NoDestinationPackage, destinationPackageQName,
					currentProject.getName()));

			// This requirement is off:
			/*
			 * // If importing a standard module, the destination package must be // "AUTOSAR" if
			 * (ModuleDefinitionType.STANDARD == moduleDefinitionType) Assert.isTrue(
			 * "/AUTOSAR".equals(MetaModelUtils.normalizeQualifiedName(destinationPackageQName)),
			 * NLS.bind(Messages.CessarModuleDefinitionJob_NotAutosarPackage, destinationPackageQName));
			 */

			// Must have an output file
			Assert.isTrue(null != outputFile, Messages.CessarModuleDefinitionJob_NoFile);
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Creates the module definition.
	 *
	 * @param arPackage
	 *        the ar package
	 * @param currentModuleDefinition
	 *        the module definition
	 * @return the g module def
	 * @throws CoreException
	 *         the core exception
	 */
	public GModuleDef createModuleDefinition(GARPackage arPackage, final GModuleDef currentModuleDefinition)
		throws CoreException
	{
		try
		{
			// Execute command to create new Module Definition and install it
			// into parent package's elements.
			switch (moduleDefinitionType)
			{
				case EXTERNAL_STANDARD:
					// It creates the same internal resource as in STANDARD case.

				case STANDARD:
					final CopyEcucModuleDefinitionCommand copyCommand = new CopyEcucModuleDefinitionCommand(
						new NewEcucModuleDefinitionCommandParameter(arPackage, currentModuleDefinition,
							moduleDefinitionName),
						null);
					Assert.isTrue(copyCommand.canExecute());
					copyCommand.execute();

					// Check result
					Object copyCommandResult = copyCommand.getResult().iterator().next();
					Assert.isNotNull(copyCommandResult);
					return (GModuleDef) copyCommandResult;

				case REFINED:
					final RefinedCopyEcucModuleDefinitionCommand refCopyCommand = new RefinedCopyEcucModuleDefinitionCommand(
						new NewEcucModuleDefinitionCommandParameter(arPackage, currentModuleDefinition,
							moduleDefinitionName),
						null);
					Assert.isTrue(refCopyCommand.canExecute());
					refCopyCommand.execute();

					// Check result
					Object refCopyCommandResult = refCopyCommand.getResult().iterator().next();
					Assert.isNotNull(refCopyCommandResult);
					return (GModuleDef) refCopyCommandResult;

				case PURE_VENDOR:
					final NewEcucModuleDefinitionCommand createCommand = new NewEcucModuleDefinitionCommand(
						new NewEcucModuleDefinitionCommandParameter(arPackage, currentModuleDefinition,
							moduleDefinitionName),
						null);
					Assert.isTrue(createCommand.canExecute());
					createCommand.execute();

					// Check result
					Object createCommandResult = createCommand.getResult().iterator().next();
					Assert.isNotNull(createCommandResult);
					return (GModuleDef) createCommandResult;

				default:
					return null;
			}
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Try to find the given standard module in the project.
	 *
	 * @param currentModuleDefinition
	 *        the module definition
	 * @return True if module definition found, false otherwise. {@link GModuleDef} to search the project for.
	 */
	protected boolean moduleAlreadyPresent(GModuleDef currentModuleDefinition)
	{
		boolean modulePresent = false;

		// Get all Module Definitions in the project.
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		List<GModuleDef> allModuleDefs = ecucModel.getAllModuleDefs();

		// TODO: Check if this reasoning is correct
		// Look for a module definition with the same UUID as
		// moduleDefinition's.
		for (GModuleDef module: allModuleDefs)
		{
			String moduleQualifiedName = ModelUtils.getAbsoluteQualifiedName(module);
			String currentQualifiedName = ModelUtils.getAbsoluteQualifiedName(currentModuleDefinition);

			if (moduleQualifiedName.equals(currentQualifiedName))
			{
				modulePresent = true;
				break;
			}
		}

		return modulePresent;
	}

	/**
	 * Sets the module definition type.
	 *
	 * @param moduleDefinitionType
	 *        the new module definition type
	 */
	public void setModuleDefinitionType(ModuleDefinitionType moduleDefinitionType)
	{
		this.moduleDefinitionType = moduleDefinitionType;
	}

	/**
	 * Sets the module definition.
	 *
	 * @param moduleDefinition
	 *        the new module definition
	 */
	public void setModuleDefinition(GModuleDef moduleDefinition)
	{
		this.moduleDefinition = moduleDefinition;
	}

	/**
	 * Sets the module definition name.
	 *
	 * @param moduleDefinitionName
	 *        the new module definition name
	 */
	public void setModuleDefinitionName(String moduleDefinitionName)
	{
		this.moduleDefinitionName = moduleDefinitionName;
	}
}
