/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 12, 2010 6:15:42 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.PMUtils.Service;

/**
 * 
 */
public class PMUtilsServiceProvider implements IServiceProvider<PMUtils.Service>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#getService(java.lang.Class)
	 */
	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return PMUtilsService.eINSTANCE;
	}

}
