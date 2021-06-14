/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * May 26, 2013 6:22:40 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.runtime.ecuc.internal.sea.impl.SEAInstanceRefImpl;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAInstanceReference;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * * A list used by the Sea API, to operate on ECUC instance references of a given definition from a particular
 * container.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 14:53:50 2014 %
 * 
 *         %version: 7 %
 */
public class InstanceReferencesEList extends AbstractReferencesEList<ISEAInstanceReference, GInstanceReferenceValue>
{
	/**
	 * @param parent
	 * @param references
	 * @param definition
	 * @param optionsHolder
	 */
	public InstanceReferencesEList(ISEAContainer parent, ESplitableList<GInstanceReferenceValue> references,
		GConfigReference definition, ISeaOptions optionsHolder)
	{
		super(parent, references, definition, optionsHolder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#setUnique(int, java.lang.Object)
	 */
	@Override
	public ISEAInstanceReference setUnique(int index, ISEAInstanceReference object)
	{
		List<GInstanceReferenceValue> referencesWithValue = getReferencesWithValue();
		GInstanceReferenceValue oldValue = referencesWithValue.get(index);

		int realIndex = getRealIndex(index);
		if (realIndex != -1)
		{
			getAttributeValues().set(realIndex, object.getValue());
		}
		return new SEAInstanceRefImpl(getSEAModel(), oldValue, getOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#addUnique(int, java.lang.Object)
	 */
	@Override
	public void addUnique(int index, ISEAInstanceReference object)
	{
		int realIndex = getRealIndex(index);
		if (realIndex != -1)
		{
			getAttributeValues().add(realIndex, object.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o)
	{
		ISEAInstanceReference seaInstanceRef = (ISEAInstanceReference) o;
		GInstanceReferenceValue arValue = seaInstanceRef.getValue();

		List<GInstanceReferenceValue> referencesWithValue = getReferencesWithValue();
		for (int i = 0; i < referencesWithValue.size(); i++)
		{
			if (arValue == referencesWithValue.get(i))
			{
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doMove(int, int)
	 */
	@Override
	public ISEAInstanceReference doMove(int newPosition, int oldPosition)
	{
		GInstanceReferenceValue toMove = getReferencesWithValue().get(oldPosition);

		int realOldIndex = getRealIndex(oldPosition);
		int realNewIndex = getRealIndex(newPosition);

		getAttributeValues().move(realNewIndex, realOldIndex);

		return new SEAInstanceRefImpl(getSEAModel(), toMove, getOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#doRemove(int)
	 */
	@Override
	protected Object doRemove(int index)
	{
		ISEAInstanceReference toRemove = null;
		int realIndex = getRealIndex(index);

		if (realIndex != -1)
		{
			EList<GInstanceReferenceValue> attrValues = getAttributeValues();
			GInstanceReferenceValue valueToRemove = attrValues.get(realIndex);

			attrValues.remove(realIndex);

			toRemove = new SEAInstanceRefImpl(getSEAModel(), valueToRemove, getOptionsHolder());
		}

		return toRemove;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#doAdd(java.lang.Object)
	 */
	@Override
	protected boolean doAdd(ISEAInstanceReference e)
	{
		GInstanceReferenceValue value = e.getValue();

		GIdentifiable target = e.getTarget();
		EList<GIdentifiable> contexts = e.getContexts();

		IEcucMMService ecucMMService = getMMService().getEcucMMService();
		if (target != null)
		{
			ecucMMService.setInstanceRefTarget(value, target);
		}
		if (contexts != null)
		{
			ecucMMService.setInstanceRefContext(value, contexts);
		}

		return getAttributeValues().add(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public ISEAInstanceReference get(int index)
	{
		List<GInstanceReferenceValue> referencesWithValue = getReferencesWithValue();
		GInstanceReferenceValue iReferenceValue = referencesWithValue.get(index);

		return new SEAInstanceRefImpl(getSEAModel(), iReferenceValue, getOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractReferencesEList#accept(gautosar.gecucdescription.
	 * GConfigReferenceValue)
	 */
	@Override
	protected boolean accept(GInstanceReferenceValue refValue)
	{
		GConfigReference def = refValue.gGetDefinition();
		return def != null && !def.eIsProxy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractReferencesEList#getSeaReferencesHandler()
	 */
	@Override
	protected ISeaReferencesHandler<ISEAInstanceReference> getSeaReferencesHandler()
	{
		return SeaHandlersManager.getSeaInstanceReferencesHandler(getSEAModel(), getOptionsHolder());
	}

}
