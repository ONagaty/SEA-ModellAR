/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.adapter;

import org.eclipse.emf.common.notify.Adapter;

/**
 * The interface that must be implemented by specific adapter factories
 * contributed through extensions by the AUTOSAR MM Services plugins.
 * 
 * @Review uidl6458 - 30.03.2012
 */
public interface IEMFAdapterFactory
{
	/**
	 * The method that creates the corresponding adapter.
	 * 
	 * @param adapterFactory
	 *        CESSAR specific adapter factory
	 * @param parentAdapter
	 *        default (EMF) adapter factory)
	 * @param target
	 *        object to be adapted
	 * @param type
	 *        the type to which the object must be adapted
	 * @return Adapted object
	 */
	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type);
}
