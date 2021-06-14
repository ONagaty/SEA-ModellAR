/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 10, 2013 10:24:44 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * A list used by the Sea API, to operate on parameters of a given definition with the help of parameter value
 * converters. <br>
 * <strong>Note:</strong> not all the methods are implemented!
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 14:53:50 2014 %
 * 
 *         %version: 6 %
 * @param <E>
 *        parameter value's external representation
 * @param <D>
 *        parameter definition
 */
public class ParameterEList<E, D extends GConfigParameter> extends AbstractAttributesEList<GParameterValue, E>
{
	private final D definition;
	private final IParameterValueConvertor<GParameterValue, D, E> convertor;

	/**
	 * @param parent
	 * @param parameters
	 *        parameter values list (backing store)
	 * @param definition
	 *        parameter definition
	 * @param convertor
	 *        parameter value's internal model representation <-> external representation converter
	 * @param optionsHolder
	 *        store for Sea options
	 */
	public ParameterEList(ISEAContainer parent, ESplitableList<GParameterValue> parameters, D definition,
		IParameterValueConvertor<GParameterValue, D, E> convertor, ISeaOptions optionsHolder)
	{
		super(parent, parameters, definition, optionsHolder);
		this.definition = definition;
		this.convertor = convertor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractAttributesEList#getAttributeValues()
	 */
	@Override
	public EList<GParameterValue> getAttributeValues()
	{
		return super.getAttributeValues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#size()
	 */
	@Override
	public int size()
	{
		return getParametersWithValue().size();
	}

	/**
	 * @param paramValue
	 * @return whether the given <code>paramValue</code> has the value set
	 */
	@Override
	protected boolean accept(GParameterValue paramValue)
	{
		return convertor.isSetValue(paramValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		return indexOf(o) != -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#get(int)
	 */
	@Override
	public E get(int index)
	{
		// gather parameters that do have a value
		List<GParameterValue> withValues = getParametersWithValue();
		GParameterValue fromIndex = withValues.get(index);

		return convertor.getValue(fromIndex);
	}

	/**
	 * Returns a view of the list with only those parameters that do have the value set
	 * 
	 * @return
	 */
	private List<GParameterValue> getParametersWithValue()
	{
		List<GParameterValue> parametersWithValue = new ArrayList<GParameterValue>();
		for (GParameterValue parameter: getAttributeValues())
		{
			if (accept(parameter))
			{
				parametersWithValue.add(parameter);
			}
		}

		return parametersWithValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AbstractAttributesEList#doRemove(int)
	 */
	@Override
	protected Object doRemove(int index)
	{
		int size = size();
		if (index >= size)
		{
			throw new BasicIndexOutOfBoundsException(index, size);
		}

		List<GParameterValue> parametersWithValue = getParametersWithValue();
		GParameterValue toRemove = parametersWithValue.get(index);

		E removed = convertor.getValue(toRemove);
		getAttributeValues().remove(toRemove);

		return removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o)
	{
		int index = -1;
		if (o == null)
		{
			return index;
		}

		List<GParameterValue> parametersWithValue = getParametersWithValue();
		Iterator<GParameterValue> iterator = parametersWithValue.iterator();
		while (iterator.hasNext())
		{
			GParameterValue next = iterator.next();
			E value = convertor.getValue(next);

			if (o.equals(value))
			{
				index = parametersWithValue.indexOf(next);
				break;
			}
		}
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(Object o)
	{
		int lastIndex = -1;
		if (o == null)
		{
			return lastIndex;
		}

		List<GParameterValue> parametersWithValue = getParametersWithValue();
		Iterator<GParameterValue> iterator = parametersWithValue.iterator();
		while (iterator.hasNext())
		{
			GParameterValue next = iterator.next();
			E value = convertor.getValue(next);
			if (o.equals(value))
			{
				lastIndex = parametersWithValue.indexOf(next);
			}
		}

		return lastIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AttributesAbstractEList#doAdd(java.lang.Object)
	 */
	@Override
	protected boolean doAdd(E e)
	{
		final GParameterValue newValue = convertor.createValue(definition, e);
		return getAttributeValues().add(newValue);
	}

	@Override
	public void addUnique(int index, E object)
	{
		int realIndex = getRealIndex(index);
		if (realIndex != -1)
		{
			GParameterValue newValue = convertor.createValue(definition, object);
			getAttributeValues().add(realIndex, newValue);
		}
	}

	@Override
	public E setUnique(int index, E e)
	{
		List<GParameterValue> parametersWithValue = getParametersWithValue();

		GParameterValue oldParamvalue = parametersWithValue.get(index);
		E old = convertor.getValue(oldParamvalue);

		int realIndex = getRealIndex(index);
		if (realIndex != -1)
		{
			GParameterValue newValue = convertor.createValue(definition, e);
			getAttributeValues().set(realIndex, newValue);
		}

		return old;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doMove(int, int)
	 */
	@Override
	public E doMove(int newPosition, int oldPosition)
	{
		GParameterValue toMove = getParametersWithValue().get(oldPosition);

		int realOldIndex = getRealIndex(oldPosition);
		int realNewIndex = getRealIndex(newPosition);

		getAttributeValues().move(realNewIndex, realOldIndex);

		return convertor.getValue(toMove);
	}

}
