/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package org.autosartools.ecuconfig.codegen.core.impl;

import org.autosartools.ecuconfig.codegen.core.JetUtil.Service;

import eu.cessar.ct.core.platform.util.IServiceProvider;

/**
 * 
 * @Review uidl7321 - Apr 12, 2012
 * 
 */
public class JetUtilServiceProvider implements IServiceProvider<Service>
{

	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return JetUtilService.eINSTANCE;
	}

}
