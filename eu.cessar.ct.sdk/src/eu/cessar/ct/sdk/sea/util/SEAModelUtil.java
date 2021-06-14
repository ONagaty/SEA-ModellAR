/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 13:54:24
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import java.util.Collections;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.req.Requirement;

/**
 * Main class for receiving the root of the <b>Simple ECUC API Model</b>.
 * 
 * @since 2013A
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:18 2015 %
 * 
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_API#SEA#1")
public final class SEAModelUtil
{
	// just for the sake of code coverage
	static
	{
		new SEAModelUtil();
	}
	/**
	 * The key for setting a custom error handler
	 */
	public static final String ERROR_HANDLER = "errorHandler"; //$NON-NLS-1$
	/**
	 * The key to be used for specifying whether the modifications are to be persisted automatically (default:
	 * <code>false</code>)
	 */
	public static final String AUTO_SAVE = "autoSave"; //$NON-NLS-1$

	/**
	 * The key to be used for specifying whether the write operations will be done outside a write transaction (default:
	 * <code>true</code>)
	 */
	public static final String READ_ONLY = "readOnly"; //$NON-NLS-1$

	/**
	 * The key to be used in the splittable context. If enabled, the fragment where a parameter is set will be reused
	 * when changing the value of that parameter (default: <code>false</code>)
	 */
	public static final String REUSE_FRAGMENT = "reuseFragment"; //$NON-NLS-1$

	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 * The Service part of the public SDK, do not use it directly, not part of the public API
	 * 
	 * @author uidl6458
	 * 
	 *         %created_by: uidl6870 %
	 * 
	 *         %date_created: Wed Jun 17 18:43:18 2015 %
	 * 
	 *         %version: 2 %
	 */
	public static interface Service
	{
		@SuppressWarnings("javadoc")
		public ISEAModel getSEAModel(IProject project, Map<String, Object> params);

	}

	/*
	 * No instance allowed
	 */
	private SEAModelUtil()
	{
	}

	/**
	 * Returns the root of the <b>Simple ECUC API Model</b> associated with the provided project, that will use the
	 * default options . <br>
	 * <br>
	 * NOTE: If the passed project is not available or does not have CESSAR nature, an unchecked exception will be
	 * thrown!
	 * 
	 * @param project
	 *        the project for which to obtain the SEA model root
	 * @return the SEA model root
	 */
	public static ISEAModel getSEAModel(IProject project)
	{
		return getSEAModel(project, Collections.EMPTY_MAP);
	}

	/**
	 * Returns the root of the <b>Simple ECUC API Model</b> associated with the provided project, that will use the
	 * specified options. <br/>
	 * <br>
	 * NOTE: If the passed project is not available or does not have CESSAR nature, an unchecked exception will be
	 * thrown!
	 * 
	 * <p>
	 * <b>Usage</b>:
	 * 
	 * <pre>
	 * Map&lt;String, Object&gt; options = new HashMap&lt;String, Object&gt;();
	 * 
	 * options.put(SEAModelUtil.READ_ONLY, false); // modifications will be done in a
	 * // write transaction
	 * options.put(SEAModelUtil.AUTO_SAVE, true); // modifications will be persisted automatically
	 * options.put(SEAModelUtil.ERROR_HANDLER, new CustomSEAErrorHandler()); // specifies a custom error handling
	 * 
	 * ISEAModel seaModel = SEAModelUtil.getSEAModel(project, options); // ask for a SEA model that will use the given options
	 * </pre>
	 * 
	 * @param project
	 *        the project for which to obtain the SEA model root
	 * @param params
	 *        map with additional options to be passed to the SEA model
	 * @return the SEA model root
	 */
	public static ISEAModel getSEAModel(IProject project, Map<String, Object> params)
	{

		return SERVICE.getSEAModel(project, params);
	}

}
