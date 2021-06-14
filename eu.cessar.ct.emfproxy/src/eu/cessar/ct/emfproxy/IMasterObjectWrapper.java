/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:12:36 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;

import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * @author uidl6458
 * 
 */
public interface IMasterObjectWrapper<T> extends IMasterWrapper
{

	/**
	 * @param clz
	 * @return
	 */
	public boolean isWrapperForClass(Class<?> clz);

	/**
	 * @return
	 */
	public Class<T> getMasterClass();

	/**
	 * @return
	 */
	public List<T> getAllMasterObjects();

	/**
	 * @return
	 */
	public Object getMasterContainer();

	/**
	 * 
	 * @param object
	 * @return
	 */
	public IEMFProxyObject getSlaveContainer(IEMFProxyObject object);

	/**
	 * @param parentWrapper
	 * @param parentFeature
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper, EStructuralFeature parentFeature);

	/**
	 * Returns additional parameters specific for the wrapper.
	 * 
	 * @return
	 */
	public Map<String, Object> getParameters();

	/**
	 * 
	 * @return the editing domain associated with the master objects
	 */
	public EditingDomain getEditingDomain();
}
