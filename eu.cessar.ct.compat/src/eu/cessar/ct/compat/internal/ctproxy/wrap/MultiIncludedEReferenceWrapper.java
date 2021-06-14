/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 8, 2011 9:01:05 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class MultiIncludedEReferenceWrapper extends AbstractIncludedWrapper implements
	IMultiMasterFeatureWrapper<Object>
{

	/**
	 * @param engine
	 * @param master
	 * @param lvl1MasterFeature
	 * @param lvl1ExpectedType
	 * @param lvl2MasterFeature
	 * @param lvl2ExpectedType
	 * @param finalMasterFeature
	 */
	public MultiIncludedEReferenceWrapper(IEMFProxyEngine engine, EObject master, EStructuralFeature lvl1MasterFeature,
		EClass lvl1ExpectedType, EStructuralFeature lvl2MasterFeature, EClass lvl2ExpectedType,
		EStructuralFeature finalMasterFeature)
	{
		super(engine, master, lvl1MasterFeature, lvl1ExpectedType, lvl2MasterFeature, lvl2ExpectedType,
			finalMasterFeature);
	}

	/**
	 * Return the list of objects from the master, it could also return null
	 * 
	 * @param doCreate
	 *        if set to true, any missing intermediate elements will be created. Of course, in this case the method
	 *        shall be executed from a transaction
	 * @return
	 */
	private EList<EObject> getMasterList(boolean doCreate)
	{
		if (doCreate && !getMaster().eIsSet(getLvl1MasterFeature()))
		{
			EObject obj = EcoreUtil.create(getLvl1ExpectedType());
			getMaster().eSet(getLvl1MasterFeature(), obj);
		}
		if (getMaster().eIsSet(getLvl1MasterFeature()))
		{
			EObject children = (EObject) getMaster().eGet(getLvl1MasterFeature());
			if (getLvl2MasterFeature() != null)
			{
				if (doCreate && !children.eIsSet(getLvl2MasterFeature()))
				{
					EObject obj = EcoreUtil.create(getLvl2ExpectedType());
					children.eSet(getLvl2MasterFeature(), obj);
				}
				if (children.eIsSet(getLvl2MasterFeature()))
				{
					children = (EObject) children.eGet(getLvl2MasterFeature());
				}
				else
				{
					return null;
				}
			}
			return (EList<EObject>) children.eGet(getFinalMasterFeature());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public Object get(int index)
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null || index < 0 || index > masterList.size())
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		return engine.getSlaveObject(getContext(), masterList.get(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#isEmpty()
	 */
	public boolean isEmpty()
	{
		EList<EObject> masterList = getMasterList(false);
		return masterList == null || masterList.size() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#size()
	 */
	public int size()
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null)
		{
			return 0;
		}
		else
		{
			return masterList.size();
		}
	}

	/**
	 * @return
	 */
	private Class<?> getSlaveClass()
	{
		EClass eReferenceType = ((EReference) getFinalMasterFeature()).getEReferenceType();
		EClass slaveEClass = CompatibilitySupport.getModelConstants(getEngine().getProject()).getSlaveEClass(
			eReferenceType);
		return slaveEClass.getInstanceClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray()
	 */
	public Object[] toArray()
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null)
		{
			return (Object[]) Array.newInstance(getSlaveClass(), 0);
		}
		else
		{
			Object[] result = (Object[]) Array.newInstance(getSlaveClass(), masterList.size());
			for (int i = 0; i < masterList.size(); i++)
			{
				result[i] = getEngine().getCachedSlaveObject(getContext(), masterList.get(i));
			}
			return result;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray(S[])
	 */
	public <S> S[] toArray(S[] array)
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null)
		{
			return array;
		}
		else
		{
			if (array.length < masterList.size())
			{
				// SUPPRESS CHECKSTYLE allow assignment to the "array"
				array = (S[]) Array.newInstance(array.getClass().getComponentType(), masterList.size());
			}
			for (int i = 0; i < masterList.size(); i++)
			{
				array[i] = (S) getEngine().getCachedSlaveObject(getContext(), masterList.get(i));
			}
			return array;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object slaveValue)
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null || masterList.size() == 0)
		{
			return -1;
		}
		EObject masterValue = null;
		if (slaveValue instanceof EMFProxyObjectImpl)
		{
			masterValue = (EObject) engine.getMasterObjects((EMFProxyObjectImpl) slaveValue).get(0);
		}
		return masterList.indexOf(masterValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object slaveValue)
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null || masterList.size() == 0)
		{
			return -1;
		}
		EObject masterValue = null;
		if (slaveValue instanceof EMFProxyObjectImpl)
		{
			masterValue = (EObject) engine.getMasterObjects((EMFProxyObjectImpl) slaveValue).get(0);
		}
		return masterList.lastIndexOf(masterValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	public void clear()
	{
		final EList<EObject> masterList = getMasterList(false);
		if (masterList != null && masterList.size() > 0)
		{
			List<EObject> children = new ArrayList<EObject>((EList<EObject>) getProxyObject().eGet(getWrappedFeature()));
			updateModel(new Runnable()
			{
				public void run()
				{
					masterList.clear();
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object value)
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				EObject masterObject = null;
				if (value instanceof EMFProxyObjectImpl)
				{
					masterObject = (EObject) engine.getMasterObjects((EMFProxyObjectImpl) value).get(0);
				}
				EList<EObject> masterList = getMasterList(true);
				masterList.set(index, masterObject);
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
		updateModel(new Runnable()
		{
			public void run()
			{
				EObject masterObject = null;
				if (value instanceof EMFProxyObjectImpl)
				{
					masterObject = (EObject) engine.getMasterObjects((EMFProxyObjectImpl) value).get(0);
				}
				EList<EObject> masterList = getMasterList(true);
				masterList.add(index, masterObject);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(final int index)
	{
		final EList<EObject> masterList = getMasterList(false);
		if (masterList == null)
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				masterList.remove(index);
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
		final EList<EObject> masterList = getMasterList(false);
		if (masterList == null)
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		final Object[] result = {null};
		updateModel(new Runnable()
		{

			public void run()
			{
				result[0] = masterList.move(targetIndex, sourceIndex);
			}
		});
		return result[0];
	}

}
