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
import org.eclipse.core.runtime.Plugin;

import eu.cessar.ct.core.security.license.LicenseApi;

/**
 * All CESSAR activators from non-UI plugin shall extend from this class.
 * 
 * @Warning This class will be active even if <b> no license</b> is found and
 *          therefore all the classes it uses should be public, (not in an
 *          internal package)
 * @Review uidl6458 - 12.04.2012
 */
public abstract class AbstractCessarPlugin extends Plugin implements ICessarPlugin
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#getPluginID()
	 */
	public String getPluginID()
	{
		return getBundle().getSymbolicName();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#getDebugOption(java.lang.String)
	 */
	public String getDebugOption(String debugOption)
	{
		StringBuilder str = new StringBuilder(getPluginID());
		if (!debugOption.startsWith("/")) //$NON-NLS-1$
		{
			str.append('/');
		}
		str.append(debugOption);
		return Platform.getDebugOption(str.toString());
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(int, java.lang.String, java.lang.Object[])
	 */
	public void log(int severity, String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), severity, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(int, java.lang.Exception)
	 */
	public void log(int severity, Throwable t)
	{
		CessarLoger.logMessage(getBundle(), severity, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(int, java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void log(int severity, Throwable t, String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), severity, t, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(org.eclipse.core.runtime.IStatus)
	 */
	public void log(IStatus status)
	{
		CessarLoger.logMessage(getBundle(), status);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logError(java.lang.String, java.lang.Object[])
	 */
	public void logError(String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.ERROR, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logError(java.lang.Exception)
	 */
	public void logError(Throwable t)
	{
		CessarLoger.logMessage(getBundle(), IStatus.ERROR, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logError(java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void logError(Throwable t, String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.ERROR, t, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logInfo(java.lang.String, java.lang.Object[])
	 */
	public void logInfo(String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.INFO, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logInfo(java.lang.Exception)
	 */
	public void logInfo(Throwable t)
	{
		CessarLoger.logMessage(getBundle(), IStatus.INFO, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logInfo(java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void logInfo(Throwable t, String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.INFO, t, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logWarning(java.lang.String, java.lang.Object[])
	 */
	public void logWarning(String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.WARNING, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logWarning(java.lang.Exception)
	 */
	public void logWarning(Throwable t)
	{
		CessarLoger.logMessage(getBundle(), IStatus.WARNING, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logWarning(java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void logWarning(Throwable t, String message, Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.WARNING, t, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#createStatus(java.lang.Throwable)
	 */
	public IStatus createStatus(Throwable t)
	{
		return CessarLoger.createStatus(IStatus.ERROR, getPluginID(), t.getMessage(), t);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#createStatus(int, java.lang.String, java.lang.Object[])
	 */
	public IStatus createStatus(int severity, String message, Object... args)
	{
		if (args != null && args.length > 0)
		{
			// format the message
			message = MessageFormat.format(message, args);
		}
		return CessarLoger.createStatus(severity, getPluginID(), message, null);
	}

	public boolean isLicenseValid()
	{
		return LicenseApi.hasValidLicense();
	}
}
