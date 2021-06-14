/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jan 28, 2011 2:40:11 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.sync.changers;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public class CreateReferenceEcucItem extends CreateEcucItem
{
	private GIdentifiable owner;
	private GConfigReference definition;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.CreateModuleChanger#create()
	 */
	/**
	 * 
	 */
	public CreateReferenceEcucItem(GIdentifiable owner, GConfigReference def)
	{
		this.owner = owner;
		this.definition = def;
	}

	@Override
	protected void create()
	{
		EList<GConfigReferenceValue> referenceValues = ((GContainer) owner).gGetReferenceValues();
		IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(owner);

		GConfigReferenceValue referenceValue = genericFactory.createReferenceValue(definition);
		referenceValues.add(referenceValue);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.AbstractModuleChanger#canExecute()
	 */
	@Override
	public boolean canExecute()
	{
		if (!(owner instanceof GContainer))
		{
			return false;
		}
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(owner).getEcucMMService();
		EList<? extends GConfigReferenceValue> values = ((GContainer) owner).gGetReferenceValues();
		int instancesCount = 0;

		// count the reference instances
		for (GConfigReferenceValue gReferenceValue: values)
		{
			if (gReferenceValue.gGetDefinition().equals(definition))
			{
				instancesCount++;
			}
		}

		BigInteger upper = ecucMMService.getUpperMultiplicity(definition, BigInteger.ONE, true);

		if (upper.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			return true;
		}
		else
		{
			long longUpper = upper.longValue();
			return longUpper > instancesCount;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.AbstractModuleChanger#getActionDescription()
	 */
	@Override
	public String getActionDescription()
	{
		return SynchronizeConstants.createReferenceItemDescr;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.AbstractModuleChanger#getOwner()
	 */
	@Override
	public EObject getEcucItem()
	{
		return owner;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger#copy(java.lang.String)
	 */
	@Override
	public AbstractEcucItemChanger copy()
	{
		return this;
	}

}
