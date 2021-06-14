/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 29, 2010 11:39:03 AM </copyright>
 */
package eu.cessar.ct.compat.internal.sdk;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * Contains utilities related to the compatility layer.
 * 
 * @author uidl6458
 * @Review uidl7321 - Apr 3, 2012
 */
public final class CompatibilityUtils
{

	public interface Service
	{
		public EObject getPMRoot(IProject project);
	}

	private static Service service = PlatformUtils.getService(Service.class);

	private CompatibilityUtils()
	{
		// do not instantiate
	}

	/**
	 * Return the root of the presentation model of the project. The result is
	 * safe to be cast to <code>ecuc.RootNode</code>
	 * 
	 * @param project
	 *        the given project
	 * @return the root node
	 */
	public static EObject getPMRoot(IProject project)
	{
		return service.getPMRoot(project);
	}

}
