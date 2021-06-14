/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Feb 1, 2010 11:50:27 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution.pluget;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.ICessarPluget;

/**
 * @author uidl6458
 *
 */
public class PlugetClassTask extends CessarTask<String>
{

	private final String[] arguments;
	private final IProject project;

	/**
	 * @param manager
	 * @param inputObject
	 * @param name
	 * @param cl
	 * @param project
	 * @param arguments
	 */
	public PlugetClassTask(ICessarTaskManagerImpl<String> manager, String name, String inputObject, ClassLoader cl,
		IProject project, String[] arguments)
	{
		super(manager, name, inputObject, cl);
		this.project = project;
		this.arguments = arguments;
		setRule(project);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.CessarTask#runTask(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runTask(IProgressMonitor monitor) throws CoreException
	{
		try
		{
			Class<?> loadClass = getParentClassLoader().loadClass(getInput());
			ICessarPluget pluget = (ICessarPluget) loadClass.newInstance();
			pluget.run(project, monitor, arguments);
			return Status.OK_STATUS;
		}
		catch (Throwable t) // SUPPRESS CHECKSTYLE see below
		// Catch all exception a pluget might throw at us, we don't want to
		// crash the system because of a poorly written pluget.
		{
			IStatus status = CessarPluginActivator.getDefault().createStatus(t);
			getLogger().log(status);
			return status;
		}
	}
}
