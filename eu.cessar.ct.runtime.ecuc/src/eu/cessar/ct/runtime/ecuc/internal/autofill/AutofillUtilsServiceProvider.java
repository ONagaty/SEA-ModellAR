/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Dec 10, 2013 11:30:38 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.autofill;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.AutomaticValuesUtils;
import eu.cessar.ct.sdk.utils.AutomaticValuesUtils.Service;

/**
 * TProvider for the AutomaticValuesUtils service
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 09:35:47 2014 %
 * 
 *         %version: 2 %
 */
public class AutofillUtilsServiceProvider implements IServiceProvider<AutomaticValuesUtils.Service>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#getService(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return SDKAutofillUtilsService.INSTANCE;
	}

}
