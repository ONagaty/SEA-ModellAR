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

import eu.cessar.ct.compat.internal.ctproxy.CTProxyUtils;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleEREFValueFeatureWrapper extends AbstractMasterFeatureWrapper<String> implements
	ISingleMasterFeatureWrapper<String>
{
	private EObject masterReferredObject;
	private EObject masterReferringObject;
	private EReference masterReference;

	/**
	 * @param engine
	 * @param masterReference
	 * @param masterReferringObject
	 * @param master2
	 */
	public SingleEREFValueFeatureWrapper(IEMFProxyEngine engine, EObject masterReferredObject,
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
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public String getValue()
	{
		if (masterReferredObject != null)
		{
			return MetaModelUtils.getAbsoluteQualifiedName(masterReferredObject);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void setValue(final Object newValue)
	{
		if (newValue != null)
		{

			// check to see if the newValue is the same with the one
			// already set
			if (masterReferredObject == null
				|| !MetaModelUtils.getAbsoluteQualifiedName(masterReferredObject).equals(newValue))
			{
				final EObject newMasterReferredObject = CTProxyUtils.getRealOrProxyObject(
					engine.getProject(), (String) newValue, ((IEREF) getProxyObject()).getDest(),
					(masterReference != null) ? masterReference.getEReferenceType() : null);
				if (masterReferringObject != null && masterReference != null)
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
				else
				{
					masterReferredObject = newMasterReferredObject;
				}
			}

		}
		else
		{
			unsetValue();
		}

		// engine.updateSlaveFeature(this);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return masterReferredObject != null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	@SuppressWarnings("unchecked")
	public void unsetValue()
	{
		// set the new value
		if (masterReference.isMany())
		{
			final EList<EObject> masterReferredObjects = (EList<EObject>) masterReferringObject.eGet(masterReference);
			if (masterReferredObject != null)
			{
				// remove old value with the new one
				final int index = masterReferredObjects.indexOf(masterReferredObject);
				updateModel(new Runnable()
				{
					public void run()
					{
						masterReferredObjects.remove(index);
					}
				});

			}

		}
		else
		{
			updateModel(new Runnable()
			{
				public void run()
				{
					masterReferringObject.eUnset(masterReference);
				}
			});
		}
		masterReferredObject = null;

		// engine.updateSlaveFeature(this);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		throw new UnsupportedOperationException();
	}

}
