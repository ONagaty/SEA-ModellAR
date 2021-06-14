/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 13, 2010 5:50:02 PM </copyright>
 */
package eu.cessar.ct.emfproxy.internal;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.emfproxy.IEMFProxyCache;

/**
 * @author uidl6458
 * 
 */
public class EMFProxyCache<S, M> implements IEMFProxyCache<S, M>
{

	public class CacheValue<S> implements ICacheValue<S>
	{
		private S slave;
		private Resource masterResource;

		public CacheValue(S slave, Resource masterResource)
		{
			this.slave = slave;
			this.masterResource = masterResource;
		}

		public S getSlave()
		{
			return slave;
		}

		public Resource getMasterResource()
		{
			return masterResource;
		}
	}

	/**
	 * Key = slave
	 * 
	 * value = masters
	 */
	private Map<M, CacheValue<S>> cache = new IdentityHashMap<M, CacheValue<S>>();

	/**
	 * 
	 */
	public EMFProxyCache()
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IEMFProxyCache#addToCache(java.lang.Object, java.util.List)
	 */
	public synchronized void addToCache(S slaveObject, List<? extends M> masterObjects)
	{
		for (M m: masterObjects)
		{
			CacheValue<S> cacheValue;
			if (m instanceof EObject)
			{
				cacheValue = new CacheValue<S>(slaveObject, ((EObject) m).eResource());
			}
			else
			{
				cacheValue = new CacheValue<S>(slaveObject, null);
			}
			CacheValue<S> prev = cache.put(m, cacheValue);
			if (prev != null)
			{
				// System.err.println("bp here");
			}
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IEMFProxyCache#clearCache()
	 */
	public synchronized void clearCache()
	{
		cache.clear();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IEMFProxyCache#clearCache(eu.cessar.ct.emfproxy.IEMFProxyCache.IFilter)
	 */
	public synchronized void clearCache(IFilter<S, M> filter)
	{
		Iterator<Entry<M, CacheValue<S>>> iterator = cache.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<M, CacheValue<S>> next = iterator.next();
			if (filter.accept(next.getKey(), next.getValue()))
			{
				filter.aboutToRemove(next.getKey(), next.getValue());
				iterator.remove();
			}
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IEMFProxyCache#getSlaveObject(java.lang.Object)
	 */
	public synchronized S getSlaveObject(M masterObject)
	{
		CacheValue<S> cacheValue = cache.get(masterObject);
		return cacheValue == null ? null : cacheValue.slave;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IEMFProxyCache#removeMastersFromCache(java.util.Collection)
	 */
	public void removeMastersFromCache(Collection<? extends M> masterObjects)
	{
		for (M master: masterObjects)
		{
			cache.remove(master);
		}
	}
}
