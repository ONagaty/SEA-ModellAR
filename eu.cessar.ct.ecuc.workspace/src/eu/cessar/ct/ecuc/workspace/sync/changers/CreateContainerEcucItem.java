package eu.cessar.ct.ecuc.workspace.sync.changers;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public class CreateContainerEcucItem extends CreateEcucItem
{

	private GIdentifiable theOwnerObject;
	private GContainerDef containerDef;
	private String givenName = null;

	public CreateContainerEcucItem(GIdentifiable owner, GContainerDef def)
	{
		this.theOwnerObject = owner;
		this.containerDef = def;
	}

	public CreateContainerEcucItem(GIdentifiable owner, GContainerDef def, String name)
	{
		this(owner, def);
		this.givenName = name;
	}

	@Override
	public boolean canExecute()
	{
		return EcucMetaModelUtils.canCreateContainer(theOwnerObject, containerDef);
	}

	@Override
	public String getActionDescription()
	{
		return SynchronizeConstants.createContainerItemDescr;
	}

	@Override
	protected void create()
	{
		// Initializations
		EList<GContainer> childrenList = null;
		if (theOwnerObject instanceof GModuleConfiguration)
		{
			childrenList = ((GModuleConfiguration) theOwnerObject).gGetContainers();
		}
		else
		{
			childrenList = ((GContainer) theOwnerObject).gGetSubContainers();
		}

		IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(theOwnerObject);
		// create parameters container and initialize its short name and
		// definition
		GContainer newContainer = ecucGenericFactory.createContainer();

		if (givenName == null)
		{
			newContainer.gSetShortName(MetaModelUtils.computeUniqueChildShortName(theOwnerObject,
				containerDef.gGetShortName()));
		}
		else
		{
			newContainer.gSetShortName(givenName);
		}

		childrenList.add(newContainer);
		newContainer.gSetDefinition(containerDef);
		// instantiate the container with its default parameters
		EList<EObject> defContents = containerDef.eContents();
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(containerDef.eClass());
		IEcucMMService ecucMMService = mmService.getEcucMMService();
		for (EObject defC: defContents)
		{
			if (defC instanceof GIdentifiable)
			{
				if (defC instanceof GConfigParameter)
				{
					CreateParameterEcucItem paramCh = new CreateParameterEcucItem(newContainer,
						(GConfigParameter) defC);
					BigInteger lowerMultiplicity = ecucMMService.getLowerMultiplicity(
						(GConfigParameter) defC, BigInteger.ZERO, true);
					for (int i = 0; i < lowerMultiplicity.intValue(); i++)
					{
						paramCh.create();
					}
				}
			}
		}
	}

	/**
	 * @return
	 */
	protected boolean isChoice()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.AbstractModuleChanger#getOwner()
	 */
	@Override
	public EObject getEcucItem()
	{
		return theOwnerObject;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger#copy(java.lang.String)
	 */
	@Override
	public AbstractEcucItemChanger copy()
	{
		return new CreateContainerEcucItem(theOwnerObject, containerDef);
	}
}
