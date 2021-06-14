/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 02.04.2013 09:39:57
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.sea.util.SEAModelUtil;
import eu.cessar.ct.sdk.sea.util.SEAModelUtil.Service;

/**
 * 
 * Provider for the SEAModelUtil service
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jun  3 09:19:12 2013 %
 * 
 *         %version: 2 %
 */
public class SEAModelUtilServiceProvider implements IServiceProvider<SEAModelUtil.Service>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#getService(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return SDKSeaModelUtilService.INSTANCE;
	}

}
