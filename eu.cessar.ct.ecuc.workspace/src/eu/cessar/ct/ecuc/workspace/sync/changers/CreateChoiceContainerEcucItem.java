/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jan 26, 2011 2:04:52 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.sync.changers;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

/**
 * @author uidt2045
 * 
 */
public class CreateChoiceContainerEcucItem extends CreateContainerEcucItem
{
	private GIdentifiable theOwnerObject;
	private String containerName;
	private GContainerDef containerDef;

	/**
	 * @param owner
	 */
	public CreateChoiceContainerEcucItem(GIdentifiable owner, GContainerDef def, String name)
	{
		super(owner, def);
		this.theOwnerObject = owner;
		this.containerName = name;
		this.containerDef = def;
	}

	@Override
	public boolean canExecute()
	{
		return canCreateChoice(theOwnerObject);
	}

	/**
	 * Verify if a new choice container instance for the definition of this
	 * command parameter can be created
	 * 
	 * @param owner
	 *        the owner object where the container should be created
	 * @return <code>true</code> if the container instance can be created, or
	 *         <code>false</code> otherwise.
	 */
	private boolean canCreateChoice(GIdentifiable owner)
	{

		if (!(containerDef instanceof GChoiceContainerDef))
		{
			return false;
		}

		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(owner).getEcucMMService();
		EList<? extends GParamConfContainerDef> choicesDef = ((GChoiceContainerDef) containerDef).gGetChoices();
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
				noInstances++;
				// noInstances += subContainerInst.gGetSubContainers().size();
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

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.CreateContainerModuleChanger#isChoice()
	 */
	@Override
	protected boolean isChoice()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.CreateContainerModuleChanger#createContainer(gautosar.ggenericstructure.ginfrastructure.GIdentifiable, gautosar.gecucparameterdef.GContainerDef)
	 */
	@Override
	protected void create()
	{
		// TODO Auto-generated method stub
		GIdentifiable ownerDef = null;
		EList<GContainer> childrenList = null;
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(theOwnerObject).getEcucMMService();
		IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(theOwnerObject);

		// GContainerDef containerDef = cmdParameter.getDefinition();
		if (theOwnerObject instanceof GModuleConfiguration)
		{
			childrenList = ((GModuleConfiguration) theOwnerObject).gGetContainers();
			ownerDef = ((GModuleConfiguration) theOwnerObject).gGetDefinition();
		}
		else
		{// (owner instanceof GContainer)
			childrenList = ((GContainer) theOwnerObject).gGetSubContainers();
			ownerDef = ((GContainer) theOwnerObject).gGetDefinition();
		}

		GContainer newContainer = ecucGenericFactory.createContainer();
		newContainer.gSetShortName(containerName);
		newContainer.gSetDefinition(containerDef);

		// check if is a choice container case, and create the intermediate
		// element if needed
		if (isChoice() && ecucMMService.needChoiceIntermediateContainer())
		{
			// if (ownerDef instanceof GParamConfContainerDef || ownerDef
			// instanceof GModuleDef)
			if (ownerDef instanceof GModuleDef)
			{
				GChoiceContainerDef choiceDef = (GChoiceContainerDef) containerDef;
				GContainer choiceInstance = findChoiceIntermediate(childrenList, choiceDef);
				if (choiceInstance == null)
				{
					// if no instance of choice intermediate was not found,
					// create one, and the container about to be created
					// will be added as child to this instance
					choiceInstance = ecucGenericFactory.createContainer();
					choiceInstance.gSetShortName(choiceDef.gGetShortName());
					choiceInstance.gSetDefinition(choiceDef);
					childrenList.add(choiceInstance);
				}
				childrenList = choiceInstance.gGetSubContainers();
			}
			else
			{
				// the action is executed from the instance of the choice
				// itself, do nothing, the childreList is allready the right one
			}
		}
		childrenList.add(newContainer);
	}

	/**
	 * Find the container corresponding to a choice container definition into
	 * specified list
	 * 
	 * @param childrenList
	 *        the children list where the search is performed
	 * @param containerDef
	 *        the choice container definition whose instance is searched for
	 * @return The Choice container instance if found, or <code>null</code>
	 *         otherwise.
	 */
	private GContainer findChoiceIntermediate(EList<? extends GContainer> childrenList,
		GChoiceContainerDef containerDef)
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

}
