package eu.cessar.ct.workspace.logging;

import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.req.Requirement;

/**
 * A listener interface that allow implementers to get notifications when a logger is created, requested or disposed by
 * the {@link LoggerFactory} class.
 */
public interface ILoggerFactoryListener
{
	/**
	 * Called when a new logger is created by the {@link LoggerFactory}.
	 *
	 * @param logger
	 *        the new logger instance
	 */
	@Requirement(
		reqID = "215909")
	public void loggerCreated(ILogger2 logger);

	/**
	 * Called when an already existing logger is requested from {@link LoggerFactory} (logger is activated).
	 *
	 * @param logger
	 *        the instance of the requested logger
	 */
	public void loggerActivated(ILogger2 logger);

	/**
	 * Called when a logger is destroyed by the {@link LoggerFactory}.
	 *
	 * @param logger
	 *        the instance of the logger about to be destoyed
	 */
	public void loggerDisposed(ILogger2 logger);
}
