/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.02.2014 18:50:26
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model.internal.util;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.cid.model.Dependency;
import eu.cessar.ct.cid.model.elements.DependantElement;
import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Feb 28 18:46:28 2014 %
 * 
 *         %version: 1 %
 */
public final class DependantElementServices
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 * Service definition. Do not use directly, not part of API
	 * 
	 */
	@SuppressWarnings("javadoc")
	public static interface Service
	{

		public IStatus evaluateDependencies(DependantElement element);

		public IStatus evaluateDependencies(DependantElement element, String type);

		public List<Dependency> getDependencies(DependantElement element, String type);

	}

	/**
	 * 
	 */
	private DependantElementServices()
	{
		// avoid instance;
	}

	/**
	 * @param element
	 * @param type
	 * @return
	 */
	public static List<Dependency> getDependencies(DependantElement element, String type)
	{
		return SERVICE.getDependencies(element, type);
	}

	/**
	 * Evaluate all dependencies defined by the artifact plus those inherited from Delivery and not overrided.
	 * 
	 * @param artifact
	 * @return evaluation status
	 */
	public static IStatus evaluateDependencies(DependantElement element)
	{
		return SERVICE.evaluateDependencies(element);
	}

	/**
	 * Evaluate all dependencies of type <code>dependencyType</code>defined by the element plus those inherited from
	 * Delivery and not overrided.
	 * 
	 * @param element
	 * @param type
	 * @return evaluation status
	 */
	public static IStatus evaluateDependencies(DependantElement element, String type)
	{
		return SERVICE.evaluateDependencies(element, type);
	}
}
