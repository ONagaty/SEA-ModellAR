/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Dec 17, 2010 10:48:15 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class MultiDirectEReferenceWrapper extends AbstractDirectEReferenceWrapper implements
	IMultiMasterFeatureWrapper<Object>
{

	private EList<Object> values;

	/**
	 * @param engine
	 * @param master
	 * @param eReference
	 */
	@SuppressWarnings("unchecked")
	public MultiDirectEReferenceWrapper(IEMFProxyEngine engine, EObject master,
		EReference eReference)
	{
		super(engine, master, eReference);
		values = (EList<Object>) master.eGet(eReference);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public Object get(int index)
	{
		Object masterValue = values.get(index);
		if (masterValue != null)
		{
			return engine.getSlaveObject(getContext(), masterValue);
		}
		return null;
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
		Object[] result = (Object[]) Array.newInstance(
			getWrappedFeature().getEType().getInstanceClass(), array.length);
		for (int i = 0; i < array.length; i++)
		{
			result[i] = engine.getSlaveObject(getContext(), array[i]);
		}
		return result;
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
			S[] newArray = (S[]) Array.newInstance(
				getWrappedFeature().getEType().getInstanceClass(), result.length);
			array = newArray;
		}
		for (int i = 0; i < result.length; i++)
		{
			array[i] = (S) engine.getSlaveObject(getContext(), result[i]);
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object slaveValue)
	{
		if (slaveValue != null)
		{
			Object masterValue = engine.getMasterObjects((EMFProxyObjectImpl) slaveValue).get(0);
			if (masterValue != null)
			{
				return values.indexOf(masterValue);
			}
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
			if (slaveValue != null)
			{
				Object masterValue = engine.getMasterObjects((EMFProxyObjectImpl) slaveValue).get(0);

				if (masterValue == current)
				{
					return i;
				}
				else if (masterValue == null)
				{
					// do nothing, "other" is not null otherwise the previous
					// test
					// will not fail
				}
				else if (masterValue.equals(current))
				{
					return i;
				}
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	@SuppressWarnings("unchecked")
	public void clear()
	{
		List<EObject> children = new ArrayList<EObject>((EList<EObject>) getProxyObject().eGet(
			getWrappedFeature()));
		updateModel(new Runnable()
		{
			public void run()
			{
				values.clear();
			}
		});
		int featureID = InternalEObject.EOPPOSITE_FEATURE_BASE - getWrappedFeature().getFeatureID();
		for (EObject eObject: children)
		{
			((EMFProxyObjectImpl) eObject).eBasicSetContainer(null, featureID, null);
			Object masterValue = engine.getMasterObjects((EMFProxyObjectImpl) eObject).get(0);
			getEngine().updateSlave(getContext(), (EMFProxyObjectImpl) eObject, masterValue);
		}
		getEngine().updateSlaveFeature(this);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object value)
	{
		EMFProxyObjectImpl children = (EMFProxyObjectImpl) value;
		final Object[] masterValue = {engine.getMasterObjects(children).get(0)};
		updateModel(new Runnable()
		{
			public void run()
			{
				values.set(index, masterValue[0]);
			}
		});
		getEngine().updateSlaveFeature(this);
		getEngine().updateSlave(getContext(), children, masterValue[0]);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, final Object value)
	{
		EMFProxyObjectImpl children = (EMFProxyObjectImpl) value;
		final Object[] masterValue = {engine.getMasterObjects(children).get(0)};
		updateModel(new Runnable()
		{
			public void run()
			{
				values.add(index, masterValue[0]);
			}
		});
		getEngine().updateSlaveFeature(this);
		getEngine().updateSlave(getContext(), children, masterValue[0]);
		// ((EMFProxyObjectImpl) value).eBasicSetContainer(getProxyObject(),
		// InternalEObject.EOPPOSITE_FEATURE_BASE -
		// getWrappedFeature().getFeatureID(), null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(final int index)
	{
		EMFProxyObjectImpl currentObject = (EMFProxyObjectImpl) get(index);
		updateModel(new Runnable()
		{
			public void run()
			{
				values.get(index);
				values.remove(index);
			}
		});
		getEngine().updateSlaveFeature(this);
		getEngine().updateSlave(getContext(), currentObject,
			engine.getMasterObjects(currentObject).get(0));

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
		getEngine().updateSlaveFeature(this);
		return result[0];
	}

}
