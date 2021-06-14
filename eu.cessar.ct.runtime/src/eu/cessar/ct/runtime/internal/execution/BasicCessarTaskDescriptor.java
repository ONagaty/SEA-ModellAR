/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 4:04:25 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import eu.cessar.ct.runtime.classpath.ICessarClasspathContributor;
import eu.cessar.ct.runtime.execution.ICessarTaskDescriptor;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.internal.Messages;

/**
 * Implementation of {@link ICessarTaskDescriptor}.
 */
public class BasicCessarTaskDescriptor implements ICessarTaskDescriptor
{
	private String taskID;
	private String taskDescription;
	private ICessarTaskManagerFactory<?> taskManagerFactory;

	private static final String ATTR_ID = "id"; //$NON-NLS-1$
	private static final String ATTR_DESCRIPTION = "description"; //$NON-NLS-1$
	private static final String ATTR_FACTORY = "factory"; //$NON-NLS-1$

	/**
	 * Initialize the descriptor by checking the plugin registry configuration
	 * element. The configuration element should point to the
	 * cessarTask.exsd/task element
	 * 
	 * @param configElement
	 */
	public void initialize(IConfigurationElement configElement)
	{
		taskID = configElement.getAttribute(ATTR_ID);
		taskDescription = configElement.getAttribute(ATTR_DESCRIPTION);
		try
		{
			Object extension = configElement.createExecutableExtension(ATTR_FACTORY);
			if (extension instanceof ICessarTaskManagerFactory<?>)
			{
				taskManagerFactory = (ICessarTaskManagerFactory<?>) extension;
				taskManagerFactory.initialize(this);
			}
			else
			{
				// log error
				CessarPluginActivator.getDefault().logError(Messages.InvalidExtension,
					ICessarClasspathContributor.class, extension.getClass());
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IExecutionScope#getDescription()
	 */
	public String getDescription()
	{
		return taskDescription;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IExecutionScope#getTaskManagerFactory()
	 */
	public ICessarTaskManagerFactory<?> getTaskManagerFactory()
	{
		return taskManagerFactory;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IExecutionScope#getID()
	 */
	public String getID()
	{
		return taskID;
	}
}
