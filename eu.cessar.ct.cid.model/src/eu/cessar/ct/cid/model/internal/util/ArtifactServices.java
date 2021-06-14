/**
 * <copyright>
 * 
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 20.01.2014 16:45:29
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model.internal.util;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * Class providing services that delegate actions to the utility class implemented by {@link ArtifactUtils}
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Feb 28 18:47:06 2014 %
 * 
 *         %version: 5 %
 */
public final class ArtifactServices
{

	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 * The internal service definition, should not be used directly
	 * 
	 */
	@SuppressWarnings("javadoc")
	public static interface Service
	{
		public IArtifactBinding getConcreteBinding(Artifact artifact);
	}

	private ArtifactServices()
	{
	}

	/**
	 * 
	 * Return the concrete binding associated with an artifact. If the artifact does not have a concrete binding, null
	 * will be returned
	 * 
	 * @param artifact
	 *        the artifact
	 * @return the concrete artifact binding or null if one cannot be found
	 */
	public static IArtifactBinding getConcreteBinding(Artifact artifact)
	{
		return SERVICE.getConcreteBinding(artifact);
	}

}
