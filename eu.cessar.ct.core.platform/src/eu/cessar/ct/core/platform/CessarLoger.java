/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * @author uidl6458
 * 
 * @Review uidl6458 - 12.04.2012
 */
public class CessarLoger
{
	/**
	 * @param pluginID
	 * @param severity
	 * @param e
	 * @param message
	 * @param args
	 */
	public static void logMessage(Bundle bundle, int severity, Throwable t, String message,
		Object... args)
	{
		if (message != null)
		{
			if (args != null && args.length > 0)
			{
				// format the message
				message = MessageFormat.format(message, args);
			}
		}
		else
		{
			if (t != null)
			{
				message = t.getMessage();
			}
			else
			{
				// just to have a stack trace available otherwise we will end
				// with messages with a hard to pin-point location
				t = new Exception();
			}
		}
		IStatus status = createStatus(severity, bundle.getSymbolicName(), message, t);
		logMessage(bundle, status);
	}

	/**
	 * @param bundle
	 * @param status
	 */
	public static void logMessage(Bundle bundle, IStatus status)
	{
		Platform.getLog(bundle).log(status);
	}

	/**
	 * @param severity
	 * @param pluginID
	 * @param message
	 * @param e
	 * @return
	 */
	public static IStatus createStatus(int severity, String pluginID, String message, Throwable t)
	{
		return new Status(severity, pluginID, message, t);
	}

}
