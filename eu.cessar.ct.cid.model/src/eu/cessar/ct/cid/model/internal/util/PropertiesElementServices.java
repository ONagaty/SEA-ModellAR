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

import eu.cessar.ct.cid.model.Property;
import eu.cessar.ct.cid.model.elements.PropertiesElement;
import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Feb 28 18:46:30 2014 %
 * 
 *         %version: 1 %
 */
public final class PropertiesElementServices
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 * Service definition. Do not use directly, not part of API
	 * 
	 */
	@SuppressWarnings("javadoc")
	public static interface Service
	{

		List<Property> getProperties(PropertiesElement element, String name);

	}

	/**
	 * 
	 */
	private PropertiesElementServices()
	{
		// avoid instance;
	}

	/**
	 * @param element
	 * @param name
	 * @return
	 */
	public static List<Property> getProperties(PropertiesElement element, String name)
	{
		return SERVICE.getProperties(element, name);
	}

}
