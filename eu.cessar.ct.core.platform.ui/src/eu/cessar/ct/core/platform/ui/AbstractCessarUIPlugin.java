/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import eu.cessar.ct.core.platform.CessarLoger;
import eu.cessar.ct.core.security.license.LicenseApi;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @Warning This class will be active even if <b> no license</b> is found and
 *          therefore all the classes it uses should be public, (not in an
 *          internal package)
 * 
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractCessarUIPlugin extends AbstractUIPlugin implements ICessarUIPlugin
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
	 * @see eu.cessar.ct.core.platform.ui.ICessarUIPlugin#getImage(java.lang.String)
	 */
	public Image getImage(String key)
	{
		return getImageRegistry().get(key);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(int, java.lang.String, java.lang.Object[])
	 */
	public void log(final int severity, final String message, final Object... args)
	{
		CessarLoger.logMessage(getBundle(), severity, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(int, java.lang.Exception)
	 */
	public void log(final int severity, final Throwable t)
	{
		CessarLoger.logMessage(getBundle(), severity, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(int, java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void log(final int severity, final Throwable t, final String message,
		final Object... args)
	{
		CessarLoger.logMessage(getBundle(), severity, t, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#log(org.eclipse.core.runtime.IStatus)
	 */
	public void log(final IStatus status)
	{
		CessarLoger.logMessage(getBundle(), status);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logError(java.lang.String, java.lang.Object[])
	 */
	public void logError(final String message, final Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.ERROR, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logError(java.lang.Exception)
	 */
	public void logError(final Throwable t)
	{
		CessarLoger.logMessage(getBundle(), IStatus.ERROR, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logError(java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void logError(final Throwable t, final String message, final Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.ERROR, t, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logInfo(java.lang.String, java.lang.Object[])
	 */
	public void logInfo(final String message, final Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.INFO, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logInfo(java.lang.Exception)
	 */
	public void logInfo(final Throwable t)
	{
		CessarLoger.logMessage(getBundle(), IStatus.INFO, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logInfo(java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void logInfo(final Throwable t, final String message, final Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.INFO, t, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logWarning(java.lang.String, java.lang.Object[])
	 */
	public void logWarning(final String message, final Object... args)
	{
		CessarLoger.logMessage(getBundle(), IStatus.WARNING, null, message, args);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logWarning(java.lang.Exception)
	 */
	public void logWarning(final Throwable t)
	{
		CessarLoger.logMessage(getBundle(), IStatus.WARNING, t, null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#logWarning(java.lang.Exception, java.lang.String, java.lang.Object[])
	 */
	public void logWarning(final Throwable t, final String message, final Object... args)
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

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ICessarPlugin#isLicenseValid()
	 */
	public boolean isLicenseValid()
	{
		return LicenseApi.hasValidLicense();
	}

}
