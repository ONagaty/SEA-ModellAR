/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.sdk;

import eu.cessar.ct.compat.internal.sdk.CompatibilityUtils.Service;
import eu.cessar.ct.core.platform.util.IServiceProvider;

/**
 * 
 * @Review uidl7321 - Apr 12, 2012
 * 
 */
public class CompatibilityUtilsServiceProvider implements IServiceProvider<Service>
{

	public CompatibilityUtilsServiceProvider()
	{
		// TODO Auto-generated constructor stub
	}

	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return CompatibilityUtilsService.eINSTANCE;
	}

}
