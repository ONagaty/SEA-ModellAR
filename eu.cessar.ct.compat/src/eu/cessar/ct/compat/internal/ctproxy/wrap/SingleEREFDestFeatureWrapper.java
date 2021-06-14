/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;

import eu.cessar.ct.compat.internal.ctproxy.CTProxyUtils;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleEREFDestFeatureWrapper extends AbstractMasterFeatureWrapper<String> implements
	ISingleMasterFeatureWrapper<String>
{
	private EObject masterReferredObject;
	private EObject masterReferringObject;
	private EReference masterReference;
	private String dest;

	/**
	 * @param engine
	 * @param master
	 * @param masterReference
	 * @param masterReferringObject
	 */
	public SingleEREFDestFeatureWrapper(IEMFProxyEngine engine, EObject masterReferredObject,
		EObject masterReferringObject, EReference masterReference)
	{
		super(engine);
		this.masterReferredObject = masterReferredObject;
		this.masterReferringObject = masterReferringObject;
		this.masterReference = masterReference;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<String> getFeatureClass()
	{
		return String.class;
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
	public String getValue()
	{
		if (dest != null)
		{
			return dest;
		}
		if (masterReferredObject != null)
		{
			dest = BasicExtendedMetaData.INSTANCE.getName(masterReferredObject.eClass());
			return dest;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object newValue)
	{
		if (dest == null || !dest.equals(newValue))
		{
			dest = (String) newValue;
			// force reset of value, because dest has changed
			IEREF proxyObject = (IEREF) getProxyObject();
			final EObject newMasterReferredObject = CTProxyUtils.getRealOrProxyObject(
				engine.getProject(), proxyObject.getValue(), dest,
				(masterReference != null) ? masterReference.getEReferenceType() : null);
			if (newMasterReferredObject != null && newMasterReferredObject.eIsProxy()
				&& masterReferringObject != null && masterReference != null)
			{
				// set the new value
				if (masterReference.isMany())
				{
					final EList<EObject> masterReferredObjects = (EList<EObject>) masterReferringObject.eGet(masterReference);
					if (masterReferredObject != null)
					{
						// replace old value with the new one
						final int index = masterReferredObjects.indexOf(masterReferredObject);
						updateModel(new Runnable()
						{
							public void run()
							{

								masterReferredObject = newMasterReferredObject;
								masterReferredObjects.set(index, newMasterReferredObject);
							}
						});

					}
					else
					{
						masterReferredObject = newMasterReferredObject;
					}
				}
				else
				{

					updateModel(new Runnable()
					{
						public void run()
						{
							masterReferredObject = newMasterReferredObject;
							masterReferringObject.eSet(masterReference, newMasterReferredObject);
						}
					});
				}
			}

		}

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return (dest != null) || (masterReferredObject != null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		dest = null;

	}

}
