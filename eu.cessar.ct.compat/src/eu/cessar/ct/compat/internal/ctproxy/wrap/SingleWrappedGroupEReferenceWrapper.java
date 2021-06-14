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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;
import org.eclipse.emf.ecore.util.FeatureMap;

import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleWrappedGroupEReferenceWrapper extends AbstractWrappedGroupWrapper<Object>
	implements IMultiMasterFeatureWrapper<Object>
{

	/**
	 * @param engine
	 * @param intermediateSlaveNode
	 * @param masterWrappedUsageFeature
	 * @param masterWrappedUsage
	 */
	@SuppressWarnings("unchecked")
	public SingleWrappedGroupEReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl intermediateSlaveNode, final EObject masterWrappedUsage,
		final EStructuralFeature masterWrappedUsageFeature)
	{
		super(engine, intermediateSlaveNode, masterWrappedUsage, masterWrappedUsageFeature);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper#getWrappedFeature()
	 */
	@Override
	public EStructuralFeature getWrappedFeature()
	{
		return getMasterWrappedUsageFeature();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<Object> getFeatureClass()
	{
		return Object.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		int hashCode = 1;
		Object[] array = toArray();
		for (Object object: array)
		{
			hashCode = 31 * hashCode + (object == null ? 0 : object.hashCode());
		}
		return hashCode;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public Object get(int index)
	{
		Object masterValue = null;
		if (index == 0)
		{
			masterValue = getMasterWrappedUsage().eGet(getMasterWrappedUsageFeature());
		}

		// find the corresponding slave
		if (masterValue != null)
		{
			final IEMFProxyObject slaveObject = engine.getSlaveObject(
				IEMFProxyConstants.DEFAULT_CONTEXT, masterValue);
			// find the slave type
			final EClass slaveEClass = slaveObject.eClass();
			// search in intermediateNode the corresponding feature for the
			// slave object
			EReference slaveFeatureFromIntermediate = null;
			EList<EReference> eReferences = getIntermediateSlaveNode().eClass().getEReferences();
			for (EReference eReference: eReferences)
			{
				if (eReference.getEReferenceType() == slaveEClass)
				{
					slaveFeatureFromIntermediate = eReference;
					break;
				}
			}

			return new EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry(
				(EReferenceImpl) slaveFeatureFromIntermediate, (EMFProxyObjectImpl) slaveObject);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#isEmpty()
	 */
	public boolean isEmpty()
	{
		return !getMasterWrappedUsage().eIsSet(getMasterWrappedUsageFeature());
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#size()
	 */
	public int size()
	{
		if (getMasterWrappedUsage().eIsSet(getMasterWrappedUsageFeature()))
		{
			return 1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray()
	 */
	public Object[] toArray()
	{
		Object[] array = null;

		if (getMasterWrappedUsage().eIsSet(getMasterWrappedUsageFeature()))
		{
			array = new Object[1];
			array[0] = getMasterWrappedUsage().eGet(getMasterWrappedUsageFeature());
		}
		else
		{
			array = new Object[0];
		}

		// }
		Object[] result = (Object[]) Array.newInstance(
			getMasterWrappedUsageFeature().getEType().getInstanceClass(), array.length);
		for (int i = 0; i < array.length; i++)
		{
			result[i] = engine.getSlaveObject(getContext(), array[i]);
		}
		return result;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray(S[])
	 */
	public <S> S[] toArray(S[] array)
	{
		throw new UnsupportedOperationException();// unreachable code

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object value)
	{
		if (value != null)
		{
			if (value.equals(getMasterWrappedUsage().eGet(getMasterWrappedUsageFeature())))
			{
				return 0;
			}

		}
		return -1;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object slaveValue)
	{
		return indexOf(slaveValue);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	public void clear()
	{
		// doesn't reach in here

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object value)
	{
		if (value != null)
		{
			EMFProxyObjectImpl slaveValue = (EMFProxyObjectImpl) ((FeatureMap.Entry) value).getValue();
			final Object[] masterValue = {engine.getMasterObjects(slaveValue).get(0)};
			updateModel(new Runnable()
			{
				public void run()
				{
					getMasterWrappedUsage().eSet(getMasterWrappedUsageFeature(), masterValue[0]);
				}
			});
		}

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, final Object value)
	{
		if (value != null)
		{
			EMFProxyObjectImpl slaveValue = (EMFProxyObjectImpl) ((FeatureMap.Entry) value).getValue();
			final Object[] masterValue = {engine.getMasterObjects(slaveValue).get(0)};
			updateModel(new Runnable()
			{
				public void run()
				{
					getMasterWrappedUsage().eSet(getMasterWrappedUsageFeature(), masterValue[0]);
				}
			});

		}

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
				getMasterWrappedUsage().eUnset(getMasterWrappedUsageFeature());
			}
		});

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
	 */
	public Object move(final int targetIndex, final int sourceIndex)
	{
		return null;

	}

}
