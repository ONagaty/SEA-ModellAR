/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 3:06:44 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterAttributeWrapper;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * 
 */
public class MultiGParameterValueWrapper<S extends GParameterValue, D extends GConfigParameter, T> extends
	AbstractMultiMasterFeatureWrapper<T> implements IMasterAttributeWrapper<T>
{
	private final EList<S> parameters;
	private final IParameterValueConvertor<S, D, T> convertor;
	private final D definition;
	private final boolean missingContainer;

	public MultiGParameterValueWrapper(IEMFProxyEngine engine, EList<S> parameters, D definition,
		IParameterValueConvertor<S, D, T> convertor, boolean missingContainer)
	{
		super(engine);
		this.parameters = parameters;
		this.definition = definition;
		this.convertor = convertor;
		this.missingContainer = missingContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<T> getFeatureClass()
	{
		return convertor.getValueClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#getWrappedList()
	 */
	@Override
	protected List<?> getWrappedList()
	{
		if (missingContainer)
		{
			return Collections.emptyList();
		}
		else
		{
			return parameters;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public T get(int index)
	{
		if (missingContainer)
		{
			throw new IndexOutOfBoundsException("index=" + index + ";size=0"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return convertor.getValue(parameters.get(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	@Override
	public void clear()
	{
		if (missingContainer)
		{
			throw new PMRuntimeException("Cannot change values within a missing container"); //$NON-NLS-1$
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				SplitPMUtils.clearValues(parameters, definition);
			}
		});
		super.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(final int index)
	{
		if (missingContainer)
		{
			throw new PMRuntimeException("Cannot change values within a missing container"); //$NON-NLS-1$
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				SplitPMUtils.remove(index, parameters, definition);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
	 */
	public Object move(final int targetIndex, final int sourceIndex)
	{
		if (missingContainer)
		{
			throw new PMRuntimeException("Cannot change values within a missing container"); //$NON-NLS-1$
		}
		final Object[] result = {null};
		updateModel(new Runnable()
		{

			public void run()
			{
				if (parameters instanceof DelegatingWithSourceMultiEList)
				{
					T t = get(sourceIndex);
					remove(sourceIndex);
					add(targetIndex, t);
					result[0] = t;
				}
				else
				{
					result[0] = parameters.move(targetIndex, sourceIndex);
				}
			}
		});
		return result[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object value)
	{
		if (missingContainer)
		{
			throw new PMRuntimeException("Cannot change values within a missing container"); //$NON-NLS-1$
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				SplitPMUtils.set(index, value, parameters, definition, convertor);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, final Object value)
	{
		if (missingContainer)
		{
			throw new PMRuntimeException("Cannot change values within a missing container"); //$NON-NLS-1$
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				S paramValue = convertor.createValue(definition, (T) value);
				SplitPMUtils.add(index, paramValue, parameters, definition);
			}
		});
	}
}
