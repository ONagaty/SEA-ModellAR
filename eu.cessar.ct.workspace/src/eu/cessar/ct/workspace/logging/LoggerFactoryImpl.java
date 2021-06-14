package eu.cessar.ct.workspace.logging;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import ch.qos.logback.classic.LoggerContext;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;

/**
 * Implementation of logger factory.
 */
public final class LoggerFactoryImpl extends EventManager implements LoggerFactory.Service
{
	/** the member that makes this class a singleton */
	private static LoggerFactoryImpl loggerFactory = new LoggerFactoryImpl();

	/** a mapping between the logger name and the logger instance. */
	private Map<String, ILogger2> managedLoggers;

	/**
	 * A private constructor for the singleton class
	 */
	private LoggerFactoryImpl()
	{
		managedLoggers = new HashMap<>();
		LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
		context.setPackagingDataEnabled(false);
		// just get the root logger to perform a full initialization of the
		// logging support; we avoid some class cast exceptions in this way
		org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
	}

	/**
	 * The method that allow to obtain the singleton instance
	 *
	 * @return The {@link LoggerFactory} instance
	 */
	public static LoggerFactoryImpl getInstance()
	{
		return loggerFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.logging.LoggerFactory.Service#getLogger()
	 */
	public ILogger getLogger()
	{
		return getLogger(LoggingConstants.DEFAULT_LOGGER_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.logging.LoggerFactory.Service#getLogger(java.lang.String)
	 */
	public ILogger getLogger(String name)
	{
		assureConsoleActive();
		ILogger2 result = managedLoggers.get(name);
		if (result == null)
		{
			result = new LoggerImpl(name);
			managedLoggers.put(name, result);

			notifyLoggerCreated(result);
		}
		notifyLoggerActivated(result);
		return result;
	}

	/**
	 * Make sure that the workspace.ui plugin is running but only if the platform is running
	 */
	private void assureConsoleActive()
	{
		Bundle wbBundle = Platform.getBundle("org.eclipse.ui.workbench"); //$NON-NLS-1$
		if (wbBundle != null && wbBundle.getState() == Bundle.ACTIVE)
		{
			// look for cessar workspace.ui bundle
			Bundle wsBundle = Platform.getBundle("eu.cessar.ct.workspace.ui"); //$NON-NLS-1$
			if (wsBundle != null && wsBundle.getState() == Bundle.ACTIVE)
			{
				try
				{
					wsBundle.start();
				}
				catch (BundleException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}
	}

	/**
	 * @param result
	 */
	private void notifyLoggerActivated(final ILogger2 result)
	{
		for (Object lstObject: getListeners())
		{
			final ILoggerFactoryListener listener = (ILoggerFactoryListener) lstObject;
			SafeRunner.run(new ISafeRunnable()
			{

				public void run() throws Exception
				{
					listener.loggerActivated(result);
				}

				public void handleException(Throwable ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
			});
		}
	}

	/**
	 * @param result
	 */
	private void notifyLoggerCreated(final ILogger2 result)
	{
		for (Object lstObject: getListeners())
		{
			final ILoggerFactoryListener listener = (ILoggerFactoryListener) lstObject;
			SafeRunner.run(new ISafeRunnable()
			{

				public void run() throws Exception
				{
					listener.loggerCreated(result);
				}

				public void handleException(Throwable ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
			});
		}
	}

	/**
	 * @param logger
	 */
	private void notifyLoggerDisposed(final ILogger2 logger)
	{
		for (Object lstObject: getListeners())
		{
			final ILoggerFactoryListener listener = (ILoggerFactoryListener) lstObject;
			SafeRunner.run(new ISafeRunnable()
			{

				public void run() throws Exception
				{
					listener.loggerDisposed(logger);
				}

				public void handleException(Throwable ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
			});
		}
	}

	/**
	 * Dispose specified logger and notify the listeners about it.
	 *
	 * @param loggerName
	 *        the name of the logger to be removed
	 */
	public void removeLogger(String loggerName)
	{
		ILogger2 logger = managedLoggers.remove(loggerName);
		if (logger != null)
		{
			notifyLoggerDisposed(logger);
		}
	}

	/**
	 * The method that allow to add a listener to this {@link LoggerFactoryImpl} instance
	 *
	 * @param listener
	 *        the listener to be registered to this instance
	 */
	public void addLoggerFactoryListener(ILoggerFactoryListener listener)
	{
		addListenerObject(listener);
	}

	/**
	 * The method that allow to add a remove a listener from this {@link LoggerFactoryImpl} instance
	 *
	 * @param listener
	 *        the listener to be unregistered from this instance
	 */
	public void removeLoggerFactoryListener(ILoggerFactoryListener listener)
	{
		removeListenerObject(listener);
	}
}
