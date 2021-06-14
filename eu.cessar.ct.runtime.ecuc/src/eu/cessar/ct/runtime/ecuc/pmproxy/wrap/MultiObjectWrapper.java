/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 1:49:05 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;

/**
 * 
 */
public abstract class MultiObjectWrapper<T extends EObject> extends AbstractPMObjectWrapper<T>
	implements INonsplitedSingleEAttributeAccessorProvider,
	INonsplitedSingleEAttributeAccessListener

{

	private List<T> masterObject;

	/**
	 * @param masterObject
	 */
	public MultiObjectWrapper(IEMFProxyEngine engine, Class<T> masterClass, List<T> masterObject)
	{
		super(engine, masterClass);
		this.masterObject = masterObject;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterObject()
	 */
	public List<T> getMasterObject()
	{
		// TODO Auto-generated method stub
		return masterObject;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getAllMasterObjects()
	 */
	public List<T> getAllMasterObjects()
	{
		return masterObject;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessorProvider#getAccessor(java.lang.String)
	 */
	public INonsplitedSingleEAttributeAccessor getAccessor(String featureName)
	{
		return new NonsplitedSingleEAttributeAccessor(masterObject,
			(EAttribute) getMasterEClass().getEStructuralFeature(featureName), this);
	}

	public abstract EClass getMasterEClass();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessListener#notifySingleAttrGet(org.eclipse.emf.ecore.EAttribute, java.lang.Object)
	 */
	public void notifySingleAttrGet(EAttribute attr, Object result)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessListener#notifySingleAttrIsSet(org.eclipse.emf.ecore.EAttribute, boolean)
	 */
	public void notifySingleAttrIsSet(EAttribute attr, boolean result)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessListener#preNotifySingleAttrSet(org.eclipse.emf.ecore.EAttribute, java.lang.Object)
	 */
	public void preNotifySingleAttrSet(EAttribute attr, Object newValue)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessListener#notifySingleAttrSet(org.eclipse.emf.ecore.EAttribute, java.lang.Object)
	 */
	public void postNotifySingleAttrSet(EAttribute attr, Object newValue)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessListener#preNotifySingleAttrUnset(org.eclipse.emf.ecore.EAttribute)
	 */
	public void preNotifySingleAttrUnset(EAttribute attr)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessListener#notifySingleAttrUnset(org.eclipse.emf.ecore.EAttribute)
	 */
	public void postNotifySingleAttrUnset(EAttribute attr)
	{
		// do nothing
	}
}