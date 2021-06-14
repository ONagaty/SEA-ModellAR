package com.continental.license.internal;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends Plugin
{

	// The plug-in ID
	public static final String PLUGIN_ID = "com.continental.license";

	// The shared instance
	private static CessarPluginActivator plugin;

	/**
	 * The constructor
	 */
	public CessarPluginActivator()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CessarPluginActivator getDefault()
	{
		return plugin;
	}
	
	/**
	 * @return
	 */
	private static ILog getBundleLog()
	{
		return Platform.getLog(Platform.getBundle(PLUGIN_ID));
	}

	public static void log(final Throwable thr)
	{
		String defaultMsg = "No details available.";
		String msg = thr.getMessage() == null ? defaultMsg : thr.getMessage();
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, 0, msg, thr);
		log(status);
	}

	public static void logWarning(final Throwable thr)
	{
		String defaultMsg = "No details available.";
		String msg = thr.getMessage() == null ? defaultMsg : thr.getMessage();
		IStatus status = new Status(IStatus.WARNING, PLUGIN_ID, 0, msg, thr);
		log(status);
	}

	public static void log(final IStatus status)
	{
		try {
			getBundleLog().log(status);
		} catch (Throwable t)
		{
			System.err.println("Exception occurred while logging information");
			t.printStackTrace();
			if(status != null && status.getException() != null){
				status.getException().printStackTrace();
			}
		}
	}
}
