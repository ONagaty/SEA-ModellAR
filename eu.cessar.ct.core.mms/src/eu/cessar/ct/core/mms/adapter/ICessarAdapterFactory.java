/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.adapter;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;

/**
 * The interface implemented by the custom CESSAR adapter factory
 */
public interface ICessarAdapterFactory extends AdapterFactory, IChangeNotifier, IDisposable
{
	/**
	 * Return the parent adapter factory or null if there is no such parent
	 * adapter
	 * 
	 * @return
	 */
	public AdapterFactory getParentAdapterFactory();
}
