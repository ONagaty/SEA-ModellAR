/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 07.05.2013 17:42:05
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Aug 23 15:58:34 2013 %
 * 
 *         %version: 4 %
 * @param <E>
 * @param <T>
 */
public abstract class AbstractReferencesEList<E, T extends GConfigReferenceValue> extends AbstractAttributesEList<T, E>
{
	/**
	 * @param parent
	 * @param references
	 * @param definition
	 * @param optionsHolder
	 */
	public AbstractReferencesEList(ISEAContainer parent, ESplitableList<T> references, GConfigReference definition,
		ISeaOptions optionsHolder)
	{
		super(parent, references, definition, optionsHolder);
	}

	@Override
	public boolean add(E e)
	{
		checkArgument(e);

		GConfigReference definition = getDefinition();
		boolean passedCheck = getSeaReferencesHandler().checkDestinationType((ISEAContainer) getParent(),
			definition.gGetShortName(), definition, Collections.singletonList(e));
		if (!passedCheck)
		{
			return false;
		}

		return super.add(e);
	}

	/**
	 * @return the proper {@link ISeaReferencesHandler} to be used
	 */
	protected abstract ISeaReferencesHandler<E> getSeaReferencesHandler();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if (c == null || c.size() == 0)
		{
			return false;
		}

		GConfigReference definition = getDefinition();
		boolean passedCheck = getSeaReferencesHandler().checkDestinationType((ISEAContainer) getParent(),
			definition.gGetShortName(), definition, new ArrayList<E>(c));
		if (!passedCheck)
		{
			return false;
		}

		return super.addAll(c);
	}

	@Override
	public void add(int index, E e)
	{
		checkArgument(e);

		GConfigReference definition = getDefinition();
		boolean passedCheck = getSeaReferencesHandler().checkDestinationType((ISEAContainer) getParent(),
			definition.gGetShortName(), definition, Collections.singletonList(e));
		if (!passedCheck)
		{
			return;
		}

		super.add(index, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#size()
	 */
	@Override
	public int size()
	{
		return getReferencesWithValue().size();
	}

	/**
	 * 
	 * @return a view of the list with only those reference values that do have the value set
	 */
	protected List<T> getReferencesWithValue()
	{
		List<T> referencesWithValue = new ArrayList<T>();
		for (T reference: getAttributeValues())
		{
			if (accept(reference))
			{
				referencesWithValue.add(reference);
			}
		}

		return referencesWithValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#getDefinition()
	 */
	@Override
	protected GConfigReference getDefinition()
	{
		return (GConfigReference) super.getDefinition();
	}

}
