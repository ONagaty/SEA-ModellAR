/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * 07.05.2013 17:29:35
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.List;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.runtime.ecuc.internal.sea.impl.SEAContainerImpl;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * A list used by the Sea API, to operate on simple/symbolic/choice references of a given definition from a particular
 * container.
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Fri Oct 24 15:24:57 2014 %
 *
 *         %version: 7 %
 */
public class SimpleReferencesElist extends AbstractReferencesEList<ISEAContainer, GReferenceValue>
{
	/**
	 * @param parent
	 * @param store
	 * @param references
	 * @param definition
	 */
	public SimpleReferencesElist(ISEAContainer parent, ISeaOptions store, ESplitableList<GReferenceValue> references,
		GConfigReference definition)
	{
		super(parent, references, definition, store);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o)
	{
		ISEAContainer seaContainer = (ISEAContainer) o;

		List<GReferenceValue> referencesWithValue = getReferencesWithValue();
		for (int i = 0; i < referencesWithValue.size(); i++)
		{
			GReferrable value = referencesWithValue.get(i).gGetValue();
			for (GContainer arContainer: seaContainer.arGetContainers())
			{
				if (arContainer == value)
				{
					return i;
				}
			}

		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#get(int)
	 */
	@Override
	public ISEAContainer get(int index)
	{
		List<GReferenceValue> referencesWithValue = getReferencesWithValue();
		GReferenceValue referenceValue = referencesWithValue.get(index);
		GReferrable value = referenceValue.gGetValue();
		Assert.isTrue(value instanceof GContainer);

		return getSEAModel().getContainer((GContainer) value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#setUnique(int, java.lang.Object)
	 */
	@Override
	public ISEAContainer setUnique(int index, ISEAContainer object)
	{
		List<GReferenceValue> referencesWithValue = getReferencesWithValue();
		GReferenceValue old = referencesWithValue.get(index);

		int realIndex = getRealIndex(index);
		if (realIndex != 1)
		{

			GReferenceValue referenceValue = (GReferenceValue) getMMService().getGenericFactory().createReferenceValue(
				getDefinition());
			referenceValue.gSetValue(object.arGetContainers().get(0));
			getAttributeValues().set(realIndex, referenceValue);
		}

		return new SEAContainerImpl(getSEAModel(), (GContainer) old.gGetValue(), getOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#addUnique(int, java.lang.Object)
	 */
	@Override
	public void addUnique(int index, ISEAContainer object)
	{
		int realIndex = getRealIndex(index);
		if (realIndex != 1)
		{
			GReferenceValue referenceValue = (GReferenceValue) getMMService().getGenericFactory().createReferenceValue(
				getDefinition());
			referenceValue.gSetValue(object.arGetContainers().get(0));
			getAttributeValues().add(realIndex, referenceValue);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#doAdd(java.lang.Object)
	 */
	@Override
	protected boolean doAdd(ISEAContainer e)
	{
		GReferenceValue value = (GReferenceValue) getMMService().getGenericFactory().createReferenceValue(
			getDefinition());

		List<GContainer> containers = e.arGetContainers();
		if (containers.size() > 0)
		{
			value.gSetValue(containers.get(0));
		}

		return getAttributeValues().add(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doMove(int, int)
	 */
	@Override
	public ISEAContainer doMove(int newPosition, int oldPosition)
	{
		GReferenceValue toMove = getReferencesWithValue().get(oldPosition);

		int realOldIndex = getRealIndex(oldPosition);
		int realNewIndex = getRealIndex(newPosition);

		getAttributeValues().move(realNewIndex, realOldIndex);

		return new SEAContainerImpl(getSEAModel(), (GContainer) toMove.gGetValue(), getOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#doRemove(int)
	 */
	@Override
	protected Object doRemove(int index)
	{
		GReferrable toRemove = null;
		int realIndex = getRealIndex(index);
		if (realIndex != -1)
		{
			GReferenceValue valueToRemove = getAttributeValues().get(realIndex);
			toRemove = valueToRemove.gGetValue();

			getAttributeValues().remove(realIndex);
		}

		return toRemove;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractReferencesEList#accept(gautosar.gecucdescription.
	 * GConfigReferenceValue)
	 */
	@Override
	protected boolean accept(GReferenceValue refValue)
	{
		GConfigReference def = refValue.gGetDefinition();
		return def != null && !def.eIsProxy() && refValue.gGetValue() != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractReferencesEList#getSeaReferencesHandler()
	 */
	@Override
	protected ISeaReferencesHandler<ISEAContainer> getSeaReferencesHandler()
	{
		return SeaHandlersManager.getSeaSimpleReferencesHandler(getSEAModel(), getOptionsHolder());
	}

}
