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
 * Abstract class that shall be inherited by adapter factories contributed
 * through extension points. The class provide a default adapt implementation by
 * returning the given parent adapter.
 * 
 * @Review uidl6458 - 30.03.2012
 */
public abstract class AbstractEMFAdapterFactory implements IEMFAdapterFactory
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory#adapt(eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory, org.eclipse.emf.common.notify.Adapter, java.lang.Object, java.lang.Class)
	 */
	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		return parentAdapter;
	}
}
