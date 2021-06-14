/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 17.01.2014 17:32:58
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model.internal.util;

import java.util.List;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.Cid;
import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * Class providing services that delegate actions to the utility class implemented by {@link CidUtils}
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Mar  4 17:07:25 2014 %
 * 
 *         %version: 4 %
 */
public final class CidServices
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 * The internal service definition, should not be used directly
	 * 
	 */
	@SuppressWarnings("javadoc")
	public static interface Service
	{
		public List<Artifact> getArtifacts(Cid cid);

		public List<Artifact> getArtifacts(Cid cid, String type);

		public List<Artifact> getArtifacts(Cid cid, String type, String name);
	}

	/**
	 * private c-tor
	 */
	private CidServices()
	{
		// avoid instance
	}

	/**
	 * Return all Artifacts defined under current Cid
	 * 
	 * @param cid
	 * @return all Artifacts defined under current Cid
	 */
	public static List<Artifact> getArtifacts(Cid cid)
	{
		return SERVICE.getArtifacts(cid);
	}

	/**
	 * Return all Artifacts of specified <code>type</code> defined under current Cid
	 * 
	 * @param type
	 *        Artifact type
	 * @param cid
	 * @return all Artifacts of specified type
	 */
	public static List<Artifact> getArtifacts(Cid cid, String type)
	{
		return SERVICE.getArtifacts(cid, type);
	}

	/**
	 * Return all Artifacts of specified <code>type</code> and <code>name</code> defined under current Cid
	 * 
	 * @param cid
	 *        the Cid
	 * @param type
	 *        the artifact type
	 * @param name
	 *        the artifact name
	 * @return the list of artifacts, never null
	 */
	public static List<Artifact> getArtifacts(Cid cid, String type, String name)
	{
		return SERVICE.getArtifacts(cid, type, name);
	}
}
