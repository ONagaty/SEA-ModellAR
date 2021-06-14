/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458<br/>
 * 16.10.2012 12:07:05
 *
 * </copyright>
 */
package eu.cessar.ct.testutils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.views.log.LogEntry;
import org.eclipse.ui.internal.views.log.LogSession;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import eu.cessar.ct.testutils.internal.log.LogEntryProxy;
import eu.cessar.ct.testutils.internal.log.LogSessionProxy;
import eu.cessar.ct.testutils.log.ILogEntry;
import eu.cessar.ct.testutils.log.ILogSession;

/**
 * Utilities that allow inspection of the eclipse log. Unfortunately, this class use internal Eclipse API.
 *
 * @author uidl6458
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Mon Dec 15 10:41:23 2014 %
 *
 *         %version: 2 %
 */
@SuppressWarnings("restriction")
public final class PDELogUtils
{

	private PDELogUtils()
	{
		// no instance
	}

	/**
	 * Read the Eclipse log.
	 *
	 * @param output
	 *        Will be filled with the log entries
	 * @return the log session, null if there is no log session available
	 */
	public static ILogSession readDefaultLog(List<ILogEntry> output)
	{
		File logFile = Platform.getLogFileLocation().toFile();
		List<Object> logEntries = new ArrayList<Object>();
		// the LogReader class is package private so we can use it only trough Java Reflection
		Bundle bundle = Platform.getBundle("org.eclipse.ui.views.log");
		Assert.assertNotNull("Cannot locate log bundle", bundle);
		IMemento memento = createLogMemento();
		ILogSession result = null;
		if (bundle.getState() != Bundle.ACTIVE)
		{
			try
			{
				bundle.start();
			}
			catch (BundleException e)
			{
				CessarTestCase.fail(e);
			}
		}
		try
		{
			Class<?> logReaderClass = bundle.loadClass("org.eclipse.ui.internal.views.log.LogReader");
			Method method = logReaderClass.getMethod("parseLogFile", File.class, List.class, IMemento.class);
			boolean accessible = method.isAccessible();
			try
			{
				method.setAccessible(true);
				Object logSession = method.invoke(null, logFile, logEntries, memento);
				if (logSession != null)
				{
					result = new LogSessionProxy((LogSession) logSession);
				}
				for (Object logEntry: logEntries)
				{
					output.add(new LogEntryProxy((LogEntry) logEntry));
				}
			}
			finally
			{
				method.setAccessible(accessible);
			}
		}
		catch (Throwable e)
		{
			CessarTestCase.fail(e);
		}
		return result;
	}

	/**
	 * Create a default memento
	 *
	 * @return
	 */
	private static IMemento createLogMemento()
	{
		IMemento memento = XMLMemento.createWriteRoot("LOGVIEW");
		memento.putString("useLimit", "false");
		memento.putString("allSessions", "true");
		memento.putString("info", "true");
		memento.putString("warning", "true");
		memento.putString("error", "true");
		memento.putString("ok", "true");
		return memento;
	}
}
