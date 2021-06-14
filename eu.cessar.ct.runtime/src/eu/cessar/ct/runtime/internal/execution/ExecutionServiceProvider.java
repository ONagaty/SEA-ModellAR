/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 12, 2010 6:06:50 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ExecutionService.Service;

/**
 * @author uidl6458
 * 
 */
public class ExecutionServiceProvider implements IServiceProvider<ExecutionService.Service>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#getService(java.lang.Class)
	 */
	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return ExecutionServiceImpl.eINSTANCE;
	}

}
