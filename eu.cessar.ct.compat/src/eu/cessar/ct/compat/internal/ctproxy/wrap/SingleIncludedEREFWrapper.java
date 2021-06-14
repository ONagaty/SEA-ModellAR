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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleIncludedEREFWrapper extends AbstractIncludedEREFFeatureWrapper<Object> implements
	ISingleMasterFeatureWrapper<Object>
{

	/**
	 * 
	 * @param engine
	 * @param master
	 * @param lvl1MasterFeature
	 * @param lvl1ExpectedType
	 * @param finalMasterFeature
	 */
	public SingleIncludedEREFWrapper(IEMFProxyEngine engine, EObject master,
		EStructuralFeature lvl1MasterFeature, EClass lvl1ExpectedType,
		EStructuralFeature finalMasterFeature)
	{
		super(engine, master, lvl1MasterFeature, lvl1ExpectedType, finalMasterFeature);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public Object getValue()
	{
		if (masterReferringObject != null)
		{
			if (getLvl1ExpectedType().isInstance(masterReferringObject))
			{
				EObject masterReferredObject = (EObject) masterReferringObject.eGet(masterReference);

				if (masterReferredObject != null)
				{
					// have to transform it to EREF
					IEREF eref = getEREFForMasterReferredObject(masterReferredObject);
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT,
						masterReferringObject);
					parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
					engine.updateSlave(getContext(), (EMFProxyObjectImpl) eref,
						masterReferredObject, parameters);
					initializeEREF(eref, masterReferredObject);

					return eref;

				}
				return null;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(final Object newValue)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				if (!getMaster().eIsSet(getLvl1MasterFeature()) && newValue != null)
				{
					// create in level 1
					masterReferringObject = EcoreUtil.create(getLvl1ExpectedType());
					getMaster().eSet(getLvl1MasterFeature(), masterReferringObject);
				}
				if (getMaster().eIsSet(getLvl1MasterFeature()))
				{
					masterReferringObject = (EObject) getMaster().eGet(getLvl1MasterFeature());
					if (getLvl1ExpectedType().isInstance(masterReferringObject))
					{
						// found the children
						if (newValue == null)
						{
							masterReferringObject.eUnset(getFinalMasterFeature());
						}
						else
						{
							IEREF eRef = (IEREF) newValue;
							final EObject newMasterReferredObject = getMasterReferredObjectForEREF(eRef);
							erefCache.put(eRef, newMasterReferredObject);
							masterReferringObject.eSet(masterReference, newMasterReferredObject);
							Map<String, Object> parameters = new HashMap<String, Object>();
							parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT,
								masterReferringObject);
							parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE,
								masterReference);

							engine.updateSlave(getContext(), (EMFProxyObjectImpl) eRef,
								newMasterReferredObject, parameters);

						}
					}
				}
			}
		});

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		if (masterReferringObject != null)
		{
			return masterReferringObject.eIsSet(masterReference);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		if (masterReferringObject != null)
		{
			if (getLvl1ExpectedType().isInstance(masterReferringObject))
			{
				// found the children
				if (masterReferringObject.eIsSet(masterReference))
				{
					// unset it
					updateModel(new Runnable()
					{
						public void run()
						{
							masterReferringObject.eUnset(masterReference);
						}
					});
				}
			}
		}

	}

}
