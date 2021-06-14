package eu.cessar.ct.workspace.logging;

/**
 * Console reporting (logging) constants
 */
@SuppressWarnings("nls")
public final class LoggingConstants
{
	/**
	 * the name of the default logger
	 */
	public static final String DEFAULT_LOGGER_NAME = "CESSAR-CT console";

	/**
	 * default pattern used by Console Reporting to display logged messages
	 */
	public static final String DEFAULT_LOGGING_PATTERN = "%date{HH:mm:ss} | %-5level - %msg%n";

	/**
	 * Represents the Level given for the Debug
	 */
	public static final int LEVEL_DEBUG = 1 << 0;
	/**
	 * Represents the Level given for the Info
	 */
	public static final int LEVEL_INFO = 1 << 1;
	/**
	 * Represents the Level given for the Warning
	 */
	public static final int LEVEL_WARN = 1 << 2;
	/**
	 * Represents the Level given for the Error
	 */
	public static final int LEVEL_ERROR = 1 << 3;

	/**
	 * Represents the Level obtained if either of the EventLevels is occured
	 */
	public static final int LEVEL_ALL = LEVEL_DEBUG | LEVEL_INFO | LEVEL_WARN | LEVEL_ERROR;
}
