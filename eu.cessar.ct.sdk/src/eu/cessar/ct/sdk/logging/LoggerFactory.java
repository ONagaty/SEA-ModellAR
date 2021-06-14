package eu.cessar.ct.sdk.logging;

import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * The factory implementation that allow CESSAR-CT users to create a logger.
 * Obtained {@link ILogger} implementation allow to log different kind of
 * information.
 */
public final class LoggerFactory
{

	private LoggerFactory()
	{
		// avoid instance
	}

	/**
	 * Not public API, do not use
	 * 
	 */
	public static interface Service
	{
		public ILogger getLogger();

		public ILogger getLogger(String name);
	}

	private static final Service service = PlatformUtils.getService(Service.class);

	/**
	 * Allow acquisition of a a logger implementation associated with specified
	 * name.
	 * 
	 * @param name
	 *        the name of the logger.
	 * @return An {@link ILogger} instance.
	 */
	public static ILogger getLogger(String name)
	{
		return service.getLogger(name);
	}

	/**
	 * Allow acquisition of a a default logger implementation.
	 * 
	 * @return An {@link ILogger} instance.
	 */
	public static ILogger getLogger()
	{
		return service.getLogger();
	}
}
