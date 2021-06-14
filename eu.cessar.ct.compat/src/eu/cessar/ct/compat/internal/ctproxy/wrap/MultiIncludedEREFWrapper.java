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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class MultiIncludedEREFWrapper extends AbstractIncludedEREFFeatureWrapper<Object> implements
	IMultiMasterFeatureWrapper<Object>
{

	/**
	 * 
	 * @param engine
	 * @param master
	 * @param lvl1MasterFeature
	 * @param lvl1ExpectedType
	 * @param finalMasterFeature
	 */
	public MultiIncludedEREFWrapper(IEMFProxyEngine engine, EObject master,
		EStructuralFeature lvl1MasterFeature, EClass lvl1ExpectedType,
		EStructuralFeature finalMasterFeature)
	{
		super(engine, master, lvl1MasterFeature, lvl1ExpectedType, finalMasterFeature);
	}

	@SuppressWarnings("unchecked")
	private EList<EObject> getMasterList(boolean doCreate)
	{
		if (doCreate && !getMaster().eIsSet(getLvl1MasterFeature()))
		{
			masterReferringObject = EcoreUtil.create(getLvl1ExpectedType());
			getMaster().eSet(getLvl1MasterFeature(), masterReferringObject);
		}
		if (getMaster().eIsSet(getLvl1MasterFeature()))
		{
			return (EList<EObject>) masterReferringObject.eGet(masterReference);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.wrap.AbstractIncludedWrapper#getHashCode()
	 */
	@Override
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
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null || index < 0 || index > masterList.size())
		{
			throw new ArrayIndexOutOfBoundsException();
		}

		EObject masterValue = masterList.get(index);
		if (masterValue != null)
		{
			// have to transform it to EREF
			IEREF eref = getEREFForMasterReferredObject(masterValue);
			initializeEREF(eref, masterValue);
			return eref;
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#isEmpty()
	 */
	public boolean isEmpty()
	{
		EList<EObject> masterList = getMasterList(false);
		return masterList == null || masterList.size() == 0;
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray()
	 */
	public Object[] toArray()
	{
		EList<EObject> masterList = getMasterList(false);
		Class<?> erefClass = CompatibilitySupport.getModelConstants(getEngine().getProject()).getEREFClass();
		if (masterList == null)
		{
			return (Object[]) Array.newInstance(erefClass, 0);
		}

		Object[] masterArray = masterList.toArray();

		// convert to array of EREF
		Object[] result = (Object[]) Array.newInstance(erefClass, masterArray.length);
		for (int i = 0; i < masterArray.length; i++)
		{
			result[i] = getEREFForMasterReferredObject((EObject) masterArray[i]);
			initializeEREF((IEREF) result[i], (EObject) masterArray[i]);
		}
		masterArray = result;

		return masterArray;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray(S[])
	 */
	@SuppressWarnings("unchecked")
	public <S> S[] toArray(S[] result)
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null)
		{
			return result;
		}

		Object[] masterArray = masterList.toArray();

		// convert to array of EREF
		if (result.length < masterArray.length)
		{
			S[] newArray = (S[]) Array.newInstance(result.getClass().getComponentType(),
				masterArray.length);
			result = newArray;
		}
		for (int i = 0; i < masterArray.length; i++)
		{
			result[i] = (S) getEREFForMasterReferredObject((EObject) masterArray[i]);
			initializeEREF((IEREF) result[i], (EObject) masterArray[i]);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object slaveValue)
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null || masterList.size() == 0)
		{
			return -1;
		}

		final EObject masterDest = getMasterReferredObjectForEREF((IEREF) slaveValue);
		if (masterDest != null)
		{
			return masterList.indexOf(masterDest);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object slaveValue)
	{
		EList<EObject> masterList = getMasterList(false);
		if (masterList == null || masterList.size() == 0)
		{
			return -1;
		}

		final EObject masterDest = getMasterReferredObjectForEREF((IEREF) slaveValue);
		if (masterDest != null)
		{
			return masterList.lastIndexOf(masterDest);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	public void clear()
	{
		final EList<EObject> masterList = getMasterList(false);
		if (masterList != null && masterList.size() > 0)
		{
			updateModel(new Runnable()
			{
				public void run()
				{
					masterList.clear();
				}
			});

		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object newValue)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				EObject newMasterDest = null;
				if (newValue instanceof IEREF)
				{
					newMasterDest = getMasterReferredObjectForEREF((IEREF) newValue);
					erefCache.put((IEREF) newValue, newMasterDest);
				}
				EList<EObject> masterList = getMasterList(true);
				masterList.set(index, newMasterDest);
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT,
					masterReferringObject);
				parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
				engine.updateSlave(getContext(), (EMFProxyObjectImpl) newValue, newMasterDest,
					parameters);
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, final Object newValue)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				EObject newMasterDest = null;
				if (newValue instanceof IEREF)
				{
					newMasterDest = getMasterReferredObjectForEREF((IEREF) newValue);
					erefCache.put((IEREF) newValue, newMasterDest);
				}
				EList<EObject> masterList = getMasterList(true);
				masterList.add(index, newMasterDest);
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT,
					masterReferringObject);
				parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
				engine.updateSlave(getContext(), (EMFProxyObjectImpl) newValue, newMasterDest,
					parameters);
			}
		});

	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
