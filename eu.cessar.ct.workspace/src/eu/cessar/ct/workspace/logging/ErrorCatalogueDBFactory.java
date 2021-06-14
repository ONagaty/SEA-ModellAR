/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr0073<br/>
 * 17.11.2015 09:35:51
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.logging;

import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * The factory implementation that allow CESSAR-CT users to get access to error catalogue database. Obtained
 * {@link IErrorCatalogueDB} implementation allow to log different kind of information.
 *
 * @author uidr0073
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public final class ErrorCatalogueDBFactory
{

	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	private ErrorCatalogueDBFactory()
	{
		// avoid instantiation
	}

	/**
	 * Not public API, do not use
	 *
	 */
	public static interface Service
	{
		/**
		 * @return database to get error catalogue messages
		 */
		public IErrorCatalogueDB getDatabase();
	}

	/**
	 * Allow acquisition of an instance to access error catalogue database.
	 *
	 * @return An {@link IErrorCatalogueDB} instance.
	 */
	public static IErrorCatalogueDB getDatabase()
	{
		return SERVICE.getDatabase();
	}
}
