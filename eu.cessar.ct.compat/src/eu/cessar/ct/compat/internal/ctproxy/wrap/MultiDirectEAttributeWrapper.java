/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import java.lang.reflect.Array;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.compat.internal.ctproxy.convertors.IDataTypeConvertor;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class MultiDirectEAttributeWrapper extends AbstractDirectEAttributeWrapper implements
	IMultiMasterFeatureWrapper<Object>
{

	private EList<Object> values;

	/**
	 * @param engine
	 * @param master
	 * @param masterEAttribute
	 * @param iDataTypeConvertor
	 */
	@SuppressWarnings("unchecked")
	public MultiDirectEAttributeWrapper(IEMFProxyEngine engine, EObject master,
		EAttribute eAttribute, IDataTypeConvertor<Object, Object> convertor)
	{
		super(engine, master, eAttribute, convertor);
		values = (EList<Object>) master.eGet(eAttribute);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	@Override
	public Class<Object> getFeatureClass()
	{
		return Object.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		return values.hashCode();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public Object get(int index)
	{
		Object masterValue = values.get(index);
		if (convertor != null)
		{
			return convertor.convertToSlaveValue(masterValue);
		}

		return masterValue;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#isEmpty()
	 */
	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#size()
	 */
	public int size()
	{
		return values.size();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray()
	 */
	public Object[] toArray()
	{
		Object[] array = values.toArray();
		if (convertor != null)
		{
			Object[] result = (Object[]) Array.newInstance(convertor.getSlaveClass(), array.length);
			for (int i = 0; i < array.length; i++)
			{
				result[i] = convertor.convertToSlaveValue(array[i]);
			}
			array = result;
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray(S[])
	 */
	@SuppressWarnings("unchecked")
	public <S> S[] toArray(S[] array)
	{
		Object[] result = values.toArray();
		if (array.length < result.length)
		{
			S[] newArray = (S[]) Array.newInstance(array.getClass().getComponentType(),
				result.length);
			array = newArray;
		}
		for (int i = 0; i < result.length; i++)
		{
			if (convertor != null)
			{
				array[i] = (S) convertor.convertToSlaveValue(result[i]);
			}
			else
			{
				array[i] = (S) result[i];
			}
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object slaveValue)
	{
		Object masterValue = slaveValue;
		if (convertor != null)
		{
			masterValue = convertor.convertToMasterValue(slaveValue);
		}
		if (masterValue != null)
		{
			return values.indexOf(masterValue);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object slaveValue)
	{
		for (int i = size() - 1; i >= 0; i--)
		{
			Object current = get(i);
			Object masterValue = slaveValue;
			if (convertor != null)
			{
				masterValue = convertor.convertToMasterValue(slaveValue);
			}

			if (masterValue == current)
			{
				return i;
			}
			else if (masterValue == null)
			{
				// do nothing, "other" is not null otherwise the previous test
				// will not fail
			}
			else if (masterValue.equals(current))
			{
				return i;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	public void clear()
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				values.clear();
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object value)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				Object masterValue = value;
				if (convertor != null)
				{
					masterValue = convertor.convertToMasterValue(value);
				}
				values.set(index, masterValue);
			}
		});

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, final Object value)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				Object masterValue = value;
				if (convertor != null)
				{
					masterValue = convertor.convertToMasterValue(value);
				}
				values.add(index, masterValue);
			}
		});

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(final int index)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				values.remove(index);
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
	 */
	public Object move(final int targetIndex, final int sourceIndex)
	{
		final Object[] result = {null};

		updateModel(new Runnable()
		{
			public void run()
			{
				result[0] = values.move(targetIndex, sourceIndex);
			}
		});
		return result[0];
	}

}
