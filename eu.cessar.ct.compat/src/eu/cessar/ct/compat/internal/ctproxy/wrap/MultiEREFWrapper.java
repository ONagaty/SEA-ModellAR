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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.sdk.pm.PMRuntimeException;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class MultiEREFWrapper extends AbstractEREFFeatureWrapper<Object> implements
	IMultiMasterFeatureWrapper<Object>
{
	private EList<EObject> masterReferredValues;

	/**
	 * @param engine
	 * @param masterFeature
	 * @param masterReferringObject
	 */
	@SuppressWarnings("unchecked")
	public MultiEREFWrapper(IEMFProxyEngine engine, EObject masterReferringObject,
		EReference masterReference)
	{
		super(engine, masterReferringObject, masterReference);
		masterReferredValues = (EList<EObject>) masterReferringObject.eGet(masterReference);
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
		EObject masterValue = masterReferredValues.get(index);
		if (masterValue != null)
		{
			// have to transform it to EREF
			IEREF eref = getEREFForMasterReferredObject(masterValue);
			erefCache.put(eref, masterValue);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT, masterReferringObject);
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
			engine.updateSlave(getContext(), (EMFProxyObjectImpl) eref, masterValue, parameters);
			// initializeEREF(eref, masterValue);
			return eref;

		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#isEmpty()
	 */
	public boolean isEmpty()
	{
		return masterReferredValues.isEmpty();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#size()
	 */
	public int size()
	{
		return masterReferredValues.size();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray()
	 */
	public Object[] toArray()
	{
		Object[] masterArray = masterReferredValues.toArray();

		// convert to array of EREF
		Object[] result = (Object[]) Array.newInstance(
			CompatibilitySupport.getModelConstants(getEngine().getProject()).getEREFClass(),
			masterArray.length);
		for (int i = 0; i < masterArray.length; i++)
		{
			result[i] = getEREFForMasterReferredObject((EObject) masterArray[i]);
			// initializeEREF((IEREF) result[i], (EObject) masterArray[i]);
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
		Object[] masterArray = masterReferredValues.toArray();

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
			// initializeEREF((IEREF) result[i], (EObject) masterArray[i]);
		}
		return result;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object slaveValue)
	{
		if (!(slaveValue instanceof IEREF))
		{
			throw new PMRuntimeException("Expected EREF, got " + slaveValue); //$NON-NLS-1$
		}

		final EObject masterDest = getMasterReferredObjectForEREF((IEREF) slaveValue);
		if (masterDest != null)
		{
			return masterReferredValues.indexOf(masterDest);
		}
		return -1;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object slaveValue)
	{
		if (!(slaveValue instanceof IEREF))
		{
			throw new InternalProxyConfigurationError("Expected EREF, got " + slaveValue); //$NON-NLS-1$
		}

		Object masterDest = getMasterReferredObjectForEREF((IEREF) slaveValue);

		for (int i = size() - 1; i >= 0; i--)
		{
			Object current = masterReferredValues.get(i);

			if (masterDest == current)
			{
				return i;
			}
			else if (masterDest == null)
			{
				// do nothing, "other" is not null otherwise the previous
				// test
				// will not fail
			}
			else if (masterDest.equals(current))
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
				masterReferredValues.clear();
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, Object newValue)
	{
		if (!(newValue instanceof IEREF))
		{
			throw new InternalProxyConfigurationError("Expected EREF, got " + newValue); //$NON-NLS-1$
		}

		final EObject newMasterDest = getMasterReferredObjectForEREF((IEREF) newValue);
		erefCache.put((IEREF) newValue, newMasterDest);

		updateModel(new Runnable()
		{
			public void run()
			{
				masterReferredValues.set(index, newMasterDest);
			}
		});

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT, masterReferringObject);
		parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
		engine.updateSlave(getContext(), (EMFProxyObjectImpl) newValue, newMasterDest, parameters);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, Object newValue)
	{
		if (!(newValue instanceof IEREF))
		{
			throw new InternalProxyConfigurationError("Expected EREF, got " + newValue); //$NON-NLS-1$
		}

		final EObject newMasterDest = getMasterReferredObjectForEREF((IEREF) newValue);
		erefCache.put((IEREF) newValue, newMasterDest);

		updateModel(new Runnable()
		{
			public void run()
			{
				masterReferredValues.add(index, newMasterDest);
			}
		});

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT, masterReferringObject);
		parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
		engine.updateSlave(getContext(), (EMFProxyObjectImpl) newValue, newMasterDest, parameters);
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
				masterReferredValues.remove(index);
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
				result[0] = masterReferredValues.move(targetIndex, sourceIndex);
			}
		});
		return result[0];
	}

}
