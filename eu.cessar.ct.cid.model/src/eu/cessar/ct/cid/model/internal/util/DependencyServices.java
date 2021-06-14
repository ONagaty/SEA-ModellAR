/**
 * <copyright>
 * 
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 20.01.2014 16:47:39
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model.internal.util;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.cid.model.Dependency;
import eu.cessar.ct.cid.model.IDependencyBinding;
import eu.cessar.ct.cid.model.Property;
import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * Class providing services that delegate actions to the utility class implemented by {@link DependencyUtils}
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Feb 28 12:18:41 2014 %
 * 
 *         %version: 3 %
 */
public final class DependencyServices
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 * The internal service definition, should not be used directly
	 * 
	 */
	@SuppressWarnings("javadoc")
	public static interface Service
	{
		public IStatus evaluateDependency(Dependency dependency);

		public List<Property> getProperties(Dependency dependency, String propertyType);

		public IDependencyBinding getConcreteBinding(Dependency dependency);
	}

	private DependencyServices()
	{
		// avoid instance
	}

	/**
	 * @param dependency
	 * @return dependency evaluation status
	 */
	public static IStatus evaluateDependency(Dependency dependency)
	{
		return null;
	}

	/**
	 * @param dependency
	 * @param propertyType
	 * @return all Properties of the specified <code>propertyType</code> defined under current Dependency
	 */
	public static List<Property> getProperties(Dependency dependency, String propertyType)
	{
		return SERVICE.getProperties(dependency, propertyType);
	}

	/**
	 * 
	 * Return the concrete binding associated with a dependency. If the dependency does not have a concrete binding,
	 * null will be returned
	 * 
	 * @param dependency
	 *        the dependency
	 * @return the concrete dependency binding or null if one cannot be found
	 */
	public static IDependencyBinding getConcreteBinding(Dependency dependency)
	{
		return SERVICE.getConcreteBinding(dependency);
	}
}
