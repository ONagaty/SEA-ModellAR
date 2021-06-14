package eu.cessar.ct.core.mms.ecuc.commands;

import java.math.BigInteger;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucContainerCommandParameter;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.EProjectVariant;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Generic implementation of the command that create an ECUC Container, based on the data passed through the command
 * parameter.
 */
public class NewEcucContainerCommand extends AbstractCessarCommand
{
	private NewEcucContainerCommandParameter cmdParameter;

	/**
	 * Default command constructor
	 * 
	 * @param parameter
	 *        an instance of container command parameter
	 * @param image
	 *        an image object to be displayed as command icon
	 */
	public NewEcucContainerCommand(NewEcucContainerCommandParameter parameter, Object image)
	{
		super(image);
		Assert.isNotNull(parameter);
		cmdParameter = parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
	 */
	@Override
	public boolean canExecute()
	{
		if (!(cmdParameter.getOwner() instanceof GIdentifiable))
		{
			return false;
		}

		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();

		// if the owner has a choice definition, the container about to be
		// created should be considered one of the choices
		GIdentifiable intermediateOwner = owner;
		if (owner instanceof GContainer)
		{
			GContainerDef containerDef = ((GContainer) owner).gGetDefinition();
			if (containerDef instanceof GChoiceContainerDef)
			{
				cmdParameter.setChoiceContainerDefinition((GChoiceContainerDef) containerDef);

				IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(cmdParameter.getEOwner());
				if (mmService.getAutosarReleaseOrdinal() == 4)
				{
					intermediateOwner = (GIdentifiable) owner.eContainer();
				}
			}
		}

		if (cmdParameter.isChoice())
		{

			return canCreateChoice(intermediateOwner);
		}
		else
		{
			return EcucMetaModelUtils.canCreateContainer(owner, cmdParameter.getDefinition());
		}
	}

	/**
	 * Verify if a new choice container instance for the definition of this command parameter can be created
	 * 
	 * @param owner
	 *        the owner object where the container should be created
	 * @return <code>true</code> if the container instance can be created, or <code>false</code> otherwise.
	 */
	private boolean canCreateChoice(GIdentifiable owner)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(owner).getEcucMMService();
		GChoiceContainerDef containerDef = cmdParameter.getChoiceContainerDefinition();
		// if the project is in production phase, creation of new container is allowed only if their definition has
		// postBuildChangeable attribute set to true
		// or if its parent is created in production phase
		boolean canBeCreatedIfInProduction = EcucMetaModelUtils.canContainerBeCreatedIfInProduction(containerDef, owner);
		if (!canBeCreatedIfInProduction)
		{
			return false;
		}

		EList<? extends GParamConfContainerDef> choicesDef = containerDef.gGetChoices();
		int noInstances = 0;

		EList<? extends GContainer> subContainers = null;

		if (owner instanceof GModuleConfiguration)
		{
			subContainers = ((GModuleConfiguration) owner).gGetContainers();
		}
		else if (owner instanceof GContainer)
		{
			subContainers = ((GContainer) owner).gGetSubContainers();
		}

		// count the number of choice containers, with and without the existence
		// of intermediate choice instance
		for (GContainer subContainerInst: subContainers)
		{
			GContainerDef currentDef = subContainerInst.gGetDefinition();
			if (currentDef.equals(containerDef))
			{
				// intermediate choice container instance found, but don't count
				// noInstances++;
				noInstances += subContainerInst.gGetSubContainers().size();
			}
			else
			{
				for (GContainerDef choice: choicesDef)
				{
					if (currentDef.equals(choice))
					{
						noInstances++;
					}
				}
			}
		}

		// verify how the upper multiplicity of the choice definition is
		// compared with the counted instances
		BigInteger upper = ecucMMService.getUpperMultiplicity(containerDef, BigInteger.ONE, true);

		if (upper.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			return true;
		}
		else
		{
			long longUpper = upper.longValue();
			return longUpper > noInstances;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getText()
	 */
	@Override
	public String getText()
	{
		return cmdParameter.getDefinition().gGetShortName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.command.AbstractCommand#undo()
	 */
	@Override
	public void undo()
	{
		if (result != null)
		{
			int size = result.size();
			if (size != 0)
			{
				EObject eObject = result.get(size - 1);
				GChoiceContainerDef choiceContainerDefinition = cmdParameter.getChoiceContainerDefinition();
				if (choiceContainerDefinition == null)
				{
					removeElementInTransaction(eObject);
				}
				else
				{
					EObject eContainer = eObject.eContainer();
					removeElementInTransaction(eContainer);
				}
				result.clear();

			}
		}
	}

	/**
	 * Create a transaction to handle the element creation
	 * 
	 * @param eContainer
	 * @param eObject
	 */
	@SuppressWarnings("static-method")
	public void removeElementInTransaction(final EObject eObject)
	{
		Runnable r = new Runnable()
		{

			@Override
			public void run()
			{
				EcoreUtil.remove(eObject);

			}
		};

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(eObject);

		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, r, "Undo Element"); //$NON-NLS-1$
		}
		catch (OperationCanceledException e)
		{
			eu.cessar.ct.core.mms.internal.CessarPluginActivator.getDefault().logError(e);
		}
		catch (ExecutionException e)
		{
			eu.cessar.ct.core.mms.internal.CessarPluginActivator.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	// SUPRESS CHECKSTYLLE not needed
	@Override
	public void execute()
	{
		// Initializations
		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();
		GIdentifiable ownerDef = null;
		EList<GContainer> childrenList = null;
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(owner);
		IEcucMMService ecucMMService = mmService.getEcucMMService();

		IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(owner);
		GContainerDef containerDef = cmdParameter.getDefinition();
		if (owner instanceof GModuleConfiguration)
		{
			childrenList = ((GModuleConfiguration) owner).gGetContainers();
			ownerDef = ((GModuleConfiguration) owner).gGetDefinition();
		}
		else
		{ // (owner instanceof GContainer)
			childrenList = ((GContainer) owner).gGetSubContainers();
			ownerDef = ((GContainer) owner).gGetDefinition();
		}
		// get project phase
		IProject project = MetaModelUtils.getProject(owner);
		EProjectVariant projectVariant = EProjectVariant.DEVELOPMENT;
		if (project != null)
		{
			projectVariant = CESSARPreferencesAccessor.getProjectVariant(project);
		}

		// check if is a choice container case, and create the intermediate
		// element if needed
		if (cmdParameter.isChoice() && ecucMMService.needChoiceIntermediateContainer())
		{
			if (ownerDef instanceof GParamConfContainerDef || ownerDef instanceof GModuleDef)
			{
				GChoiceContainerDef choiceDef = cmdParameter.getChoiceContainerDefinition();
				GContainer choiceInstance = null;
				if (mmService.getAutosarReleaseOrdinal() != 4)
				{
					choiceInstance = findChoiceIntermediate(childrenList, choiceDef);
				}

				if (choiceInstance == null)
				{
					// if no instance of choice intermediate was not found,
					// create one, and the container about to be created
					// will be added as child to this instance
					choiceInstance = ecucGenericFactory.createContainer();
					choiceInstance.gSetShortName(MetaModelUtils.computeUniqueChildShortName(owner,
						choiceDef.gGetShortName()));
					choiceInstance.gSetDefinition(choiceDef);
					childrenList.add(choiceInstance);
					// set the project phase for the created container
					EcucMetaModelUtils.setContainerCreationPhase(choiceInstance, projectVariant);
				}
				childrenList = choiceInstance.gGetSubContainers();
			}
			else
			{ // CHECKSTYLE IGNORE check FOR NEXT 3 LINES
				// the action is executed from the instance of the choice
				// itself, do nothing, the childreList is already the right one
			}
		}
		// create parameters container and initialize its short name and
		// definition
		GContainer newContainer = ecucGenericFactory.createContainer();
		newContainer.gSetShortName(cmdParameter.getShortName());
		newContainer.gSetDefinition(containerDef);
		// set the project phase for the created container
		EcucMetaModelUtils.setContainerCreationPhase(newContainer, projectVariant);
		childrenList.add(newContainer);
		result.add(newContainer);
	}

	/**
	 * Find the container corresponding to a choice container definition into specified list
	 * 
	 * @param childrenList
	 *        the children list where the search is performed
	 * @param containerDef
	 *        the choice container definition whose instance is searched for
	 * @return The Choice container instance if found, or <code>null</code> otherwise.
	 */
	private GContainer findChoiceIntermediate(EList<? extends GContainer> childrenList, GChoiceContainerDef containerDef)
	{
		for (GContainer gContainer: childrenList)
		{
			if (gContainer.gGetDefinition().equals(containerDef))
			{
				return gContainer;
			}
		}
		return null;
	}

	/**
	 * @param runnable
	 *        the runnable to execute
	 * @param label
	 *        label of the operation
	 * @return <code>true</code> if the change has been performed, <code>false</code> otherwise //
	 */
	// protected boolean performChangeWithChecks(Runnable runnable, String label)
	// {
	// try
	// {
	// WorkspaceTransactionUtil.executeInWriteTransaction(getEditingDomain(), runnable, label);
	// return true;
	// }
	// catch (OperationCanceledException e)
	// {
	// CessarPluginActivator.getDefault().logError(e);
	// return false;
	// }
	// catch (ExecutionException e)
	// {
	// CessarPluginActivator.getDefault().logError(e);
	// return false;
	// }
	// }
}
