/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleEREFWrapper extends AbstractEREFFeatureWrapper<Object> implements
	ISingleMasterFeatureWrapper<Object>
{

	/**
	 * @param engine
	 * @param masterReferringObject
	 * @param masterReference
	 */
	public SingleEREFWrapper(IEMFProxyEngine engine, EObject masterReferringObject,
		EReference masterReference)
	{
		super(engine, masterReferringObject, masterReference);
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
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public Object getValue()
	{
		EObject masterReferredObject = (EObject) masterReferringObject.eGet(masterReference);

		if (masterReferredObject != null)
		{
			// have to transform it to EREF
			IEREF eref = getEREFForMasterReferredObject(masterReferredObject);
			erefCache.put(eref, masterReferredObject);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT, masterReferringObject);
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
			engine.updateSlave(getContext(), (EMFProxyObjectImpl) eref, masterReferredObject,
				parameters);
			// initializeEREF(eref, masterReferredObject);

			return eref;

		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		if (newValue != null)
		{
			IEREF eRef = (IEREF) newValue;
			final EObject newMasterReferredObject = getMasterReferredObjectForEREF(eRef);
			erefCache.put(eRef, newMasterReferredObject);

			updateModel(new Runnable()
			{
				public void run()
				{
					masterReferringObject.eSet(masterReference, newMasterReferredObject);
				}
			});

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT, masterReferringObject);
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);

			engine.updateSlave(getContext(), (EMFProxyObjectImpl) eRef, newMasterReferredObject,
				parameters);

		}
		else
		{
			unsetValue();
		}

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return masterReferringObject.eIsSet(masterReference);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				masterReferringObject.eUnset(masterReference);
			}
		});
	}

}
