/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 13, 2010 5:39:05 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * The emf proxy cache keeps a mapped cache with IEMFProxyObject and master
 * objects.<br/>
 * Rules:<br/>
 * <ul>
 * <li>one IEMFProxyObject could be associated to multiple master object but a
 * master object can be associated to a single IEMFProxyObject</li>
 * <li>the IEMFProxyObject are keep using a week reference so when they are not
 * referred anymore by user code they could be discarded by the garbage
 * collector</li>
 * </ul>
 * 
 */
public interface IEMFProxyCache<S, M>
{

	/**
	 * @author uidl6458
	 * 
	 * @param <S>
	 */
	public static interface ICacheValue<S>
	{
		public S getSlave();

		public Resource getMasterResource();
	}

	/**
	 * @author uidl6458
	 * 
	 * @param <M>
	 */
	public static interface IFilter<S, M>
	{
		/**
		 * @param master
		 * @return
		 */
		public boolean accept(M masterValue, ICacheValue<S> slaveValue);

		/**
		 * @param master
		 * @param slave
		 */
		public void aboutToRemove(M masterValue, ICacheValue<S> slaveValue);
	}

	/**
	 * Clear the cache
	 */
	public void clearCache();

	/**
	 * Clear all master objects from the cache that are accepted by the filter
	 * 
	 * @param filter
	 */
	public void clearCache(IFilter<S, M> filter);

	/**
	 * @param masterObject
	 * @return
	 */
	public S getSlaveObject(M masterObject);

	/**
	 * @param slaveObject
	 * @param masterObjects
	 */
	public void addToCache(S slaveObject, List<? extends M> masterObjects);

	/**
	 * @param masterObjects
	 */
	public void removeMastersFromCache(Collection<? extends M> masterObjects);

}
