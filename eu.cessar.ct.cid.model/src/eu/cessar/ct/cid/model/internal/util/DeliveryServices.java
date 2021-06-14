/**
 * <copyright>
 * 
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 20.01.2014 16:42:24
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model.internal.util;

import java.util.List;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.Delivery;
import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * Services for a delivery
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Mar  4 17:07:26 2014 %
 * 
 *         %version: 4 %
 */
public final class DeliveryServices
{

	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 * Internal Service interface, not part of the public API
	 */
	@SuppressWarnings("javadoc")
	public static interface Service
	{

		public List<Artifact> getArtifacts(Delivery delivery, String type);

		public List<Artifact> getArtifacts(Delivery delivery, String type, String name);

	}

	/**
	 * Private constructor
	 */
	private DeliveryServices()
	{
		// avoid instance
	}

	/**
	 * Return all artifacts from within the delivery that match a particular artifact type
	 * 
	 * @param delivery
	 * @param type
	 * @return the list of artifacts, never null
	 */
	public static List<Artifact> getArtifacts(Delivery delivery, String type)
	{
		return SERVICE.getArtifacts(delivery, type);
	}

	/**
	 * Return all artifacts from within the delivery that match a particular artifact type and name
	 * 
	 * @param delivery
	 * @param type
	 * @param name
	 * @return the list of artifacts, never null
	 */
	public static List<Artifact> getArtifacts(Delivery delivery, String type, String name)
	{
		return SERVICE.getArtifacts(delivery, type, name);
	}

}
