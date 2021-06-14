/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Apr 30, 2010 11:22:59 AM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.net.URL;
import java.util.List;

/**
 * @author uidl6458
 * 
 */
public interface IModifiableExecutionLoader extends IExecutionLoader
{
	/**
	 * Return a modifiable list of bundleIDs that will be used to locate classes
	 * 
	 * @return
	 */
	public List<String> getBundleIDs();

	/**
	 * @param resolver
	 */
	public void setBinaryClassResolver(IBinaryClassResolver resolver);

	/**
	 * Return a modifiable list of urls that will be used to locate classes. The
	 * order is relevant!
	 * 
	 * @return
	 */
	public List<URL> getCustomLibraries();

	/**
	 * 
	 */
	public void dispose();

}
