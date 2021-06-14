/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 03.08.2012 12:57:21 </copyright>
 */
package eu.cessar.ct.runtime.execution;

import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * @author uidl6458
 * 
 */
public interface ICessarTaskManagerImpl<T> extends ICessarTaskManager<T>
{

	public void setUpperManager(ICessarTaskManagerImpl<?> upperManager);

	public void setCachedData(String key, Object data, boolean useSoftReference);

	public Object getCachedData(String key);

	public void removeCachedData(String key);

}
