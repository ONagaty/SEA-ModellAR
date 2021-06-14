/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 3, 2010 4:41:16 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import org.eclipse.core.runtime.ISafeRunnable;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;

/**
 * A SafeRunnable is a class that implements {@link ISafeRunnable} by logging
 * the exception to the eclipse logger. Any exception throw in the run method
 * are caught and logged
 * 
 * Usage:
 * 
 * <pre>
 * SafeRunner.run(new SafeRunnable()
 * {
 *     public void run() throws Exception
 *     {
 *        ...
 *     }
 * });
 * </pre>
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 18.04.2012
 */
public abstract class SafeRunnable implements ISafeRunnable
{

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.ISafeRunnable#handleException(java.lang.Throwable)
	 */
	public void handleException(Throwable e)
	{
		CessarPluginActivator.getDefault().logError(e);
	}
}