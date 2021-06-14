/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Dec 17, 2010 10:48:25 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleDirectEReferenceWrapper extends AbstractDirectEReferenceWrapper implements
	ISingleMasterFeatureWrapper<Object>
{

	/**
	 * @param engine
	 * @param master
	 * @param eReference
	 */
	public SingleDirectEReferenceWrapper(IEMFProxyEngine engine, EObject master,
		EReference eReference)
	{
		super(engine, master, eReference);
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
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public Object getValue()
	{
		Object masterValue = master.eGet(eReference);
		if (masterValue != null)
		{
			return engine.getSlaveObject(IEMFProxyConstants.DEFAULT_CONTEXT, masterValue);
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		if (newValue instanceof EMFProxyObjectImpl)
		{
			final Object masterValue = engine.getMasterObjects((EMFProxyObjectImpl) newValue).get(0);

			updateModel(new Runnable()
			{
				public void run()
				{
					if (masterValue != null)
					{
						master.eSet(eReference, masterValue);
					}
					else
					{
						unsetValue();
					}
				}
			});
		}
		else if (newValue == null)
		{
			updateModel(new Runnable()
			{
				public void run()
				{
					unsetValue();
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return master.eIsSet(eReference);
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
				master.eUnset(eReference);
			}
		});
	}
}
