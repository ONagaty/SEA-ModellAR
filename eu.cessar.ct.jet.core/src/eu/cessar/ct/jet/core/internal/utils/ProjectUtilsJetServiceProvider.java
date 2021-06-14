/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Mar 4, 2011 2:45:15 PM </copyright>
 */
package eu.cessar.ct.jet.core.internal.utils;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.ProjectUtils;

/**
 * @author uidt2045
 * 
 */
public class ProjectUtilsJetServiceProvider implements IServiceProvider<ProjectUtils.JetService>
{

	public ProjectUtils.JetService getService(Class<ProjectUtils.JetService> serviceClass,
		Object... args)
	{
		return ProjectUtilsJetServiceImpl.eINSTANCE;
	}

}
