/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 02.03.2012 13:36:08 </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import java.util.Collections;
import java.util.Map;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractMMNamespaceMapping implements IMMNamespaceMapping
{

	protected volatile Map<String, String> namespacePackageMap;

	private final String rootURI;

	protected AbstractMMNamespaceMapping(String rootURI)
	{
		this.rootURI = rootURI;
	}

	/**
	 * 
	 */
	protected abstract Map<String, String> buildNamespaceMap();

	/**
	 * 
	 */
	private void checkInit()
	{
		if (namespacePackageMap == null)
		{
			synchronized (this)
			{
				if (namespacePackageMap == null)
				{
					namespacePackageMap = buildNamespaceMap();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMMNamespaceMapping#getPackageName(java.lang.String)
	 */
	public String getPackageName(String namespace)
	{
		checkInit();
		return namespacePackageMap.get(namespace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMMNamespaceMapping#getRootNamespace()
	 */
	public String getRootNamespace()
	{
		return rootURI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.IMMNamespaceMapping#getAllPackages()
	 */
	public Map<String, String> getAllPackages()
	{
		checkInit();
		return Collections.unmodifiableMap(namespacePackageMap);
	}

}
