package eu.cessar.ct.workspace.ui.internal.logging;

/**
 * Constants specific for logging console
 */
public final class ConsolePreferencesConstants
{
	// patterns
	public static final String LOG_PATTERN_MSG_ONLY = "%msg%n"; //$NON-NLS-1$
	public static final String LOG_PATTERN_LEVEL_MSG = "%level - %msg%n"; //$NON-NLS-1$
	public static final String LOG_PATTERN_SHORT_TIME_LEVEL_MSG = "%date{HH:mm:ss} | %-5level - %msg%n"; //$NON-NLS-1$
	public static final String LOG_PATTERN_LONG_TIME_LEVEL_MSG = "%date{YYY/MM/DD-HH:mm:ss} | %-5level - %msg%n"; //$NON-NLS-1$

	// console default colors
	public static final int[] CONSOLE_BKG_COLOR = new int[] {255, 255, 255}; // white
	public static final int[] CONSOLE_INFO_COLOR = new int[] {0, 0, 0}; // black
	public static final int[] CONSOLE_DEBUG_COLOR = new int[] {0, 0, 255}; // blue
	public static final int[] CONSOLE_WARN_COLOR = new int[] {176, 176, 176}; // yellow
	public static final int[] CONSOLE_ERROR_COLOR = new int[] {255, 0, 0}; // red

	// filters
	public static final int FILTER_NONE = 0;
	public static final int FILTER_DEBUG = 1 << 1;
	public static final int FILTER_INFO = 1 << 2;
	public static final int FILTER_WARN = 1 << 3;
	public static final int FILTER_ERROR = 1 << 4;

	private ConsolePreferencesConstants()
	{
	}
}
