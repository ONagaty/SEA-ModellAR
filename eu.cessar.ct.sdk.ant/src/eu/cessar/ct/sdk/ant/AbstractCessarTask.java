/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jul 1, 2010 11:34:50 AM </copyright>
 */
package eu.cessar.ct.sdk.ant;

import org.apache.tools.ant.BuildException;

import eu.cessar.ct.ant.tasks.AbstractTask;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * @author uidl6870
 * 
 */
public abstract class AbstractCessarTask extends AbstractTask
{
	@Override
	public final void execute() throws BuildException
	{
		checkArgs();
		processArgs();

		ILogger logger = null;
		try
		{
			logger = LoggerFactory.getLogger();
			logger.attachStream(System.out);
			doExecute();

		}
		finally
		{
			if (logger != null)
			{
				logger.detachStream(System.out);
			}
		}
	}

	/**
	 * 
	 */
	protected abstract void doExecute();
}
