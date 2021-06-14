/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Apr 14, 2013 11:00:05 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * A list used by the Sea API, to operate on foreign references of a given definition from a particular container. <br>
 * <strong>Note:</strong> not all the methods are implemented!
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 14:53:49 2014 %
 *
 *         %version: 6 %
 */
public class ForeignReferencesElist extends AbstractReferencesEList<GIdentifiable, GReferenceValue>
{
	private final EList<GReferenceValue> references;

	/**
	 * @param parent
	 * @param references
	 *        reference parameters list (backing store)
	 * @param definition
	 *        reference definition
	 * @param optionsHolder
	 *        store for Sea options
	 */
	public ForeignReferencesElist(ISEAContainer parent, ESplitableList<GReferenceValue> references,
		GConfigReference definition, ISeaOptions optionsHolder)
	{
		super(parent, references, definition, optionsHolder);
		this.references = references;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#get(int)
	 */
	@Override
	public GIdentifiable get(int index)
	{
		List<GReferenceValue> referencesWithValue = getReferencesWithValue();
		GReferenceValue referenceValue = referencesWithValue.get(index);
		GReferrable value = referenceValue.gGetValue();

		if (value instanceof GIdentifiable)
		{
			return (GIdentifiable) referenceValue.gGetValue();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AttributesAbstractEList#doAdd(java.lang.Object)
	 */
	@Override
	protected boolean doAdd(GIdentifiable e)
	{
		GReferenceValue value = (GReferenceValue) getMMService().getGenericFactory().createReferenceValue(
			getDefinition());
		value.gSetValue(e);

		return references.add(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doMove(int, int)
	 */
	@Override
	public GIdentifiable doMove(int newPosition, int oldPosition)
	{
		GReferenceValue toMove = getReferencesWithValue().get(oldPosition);

		int realOldIndex = getRealIndex(oldPosition);
		int realNewIndex = getRealIndex(newPosition);

		getAttributeValues().move(realNewIndex, realOldIndex);

		GReferrable value = toMove.gGetValue();

		if (value instanceof GIdentifiable)
		{
			return (GIdentifiable) value;
		}

		return null;
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
			GReferenceValue valueToRemove = getReferencesWithValue().get(realIndex);
			toRemove = valueToRemove.gGetValue();

			getReferencesWithValue().remove(realIndex);
		}

		return toRemove;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AttributesAbstractEList#getDefinition()
	 */
	@Override
	protected GConfigReference getDefinition()
	{
		GCommonConfigurationAttributes def = super.getDefinition();

		return (GConfigReference) def;
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
	protected ISeaReferencesHandler<GIdentifiable> getSeaReferencesHandler()
	{
		return SeaHandlersManager.getSeaForeignReferencesHandler(getSEAModel(), getOptionsHolder());
	}

}
