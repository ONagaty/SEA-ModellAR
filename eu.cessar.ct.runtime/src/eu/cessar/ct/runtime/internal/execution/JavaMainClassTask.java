/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 1, 2010 11:50:27 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import java.lang.reflect.Method;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * @author uidl6458
 * 
 */
public class JavaMainClassTask extends CessarTask<String>
{

	private final String[] arguments;

	/**
	 * @param inputObject
	 * @param name
	 */
	public JavaMainClassTask(ICessarTaskManagerImpl<String> manager, String name,
		String inputObject, ClassLoader cl, IProject project, String[] arguments)
	{
		super(manager, name, inputObject, cl);
		this.arguments = arguments;
		setRule(project);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.CessarTask#runTask(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runTask(IProgressMonitor monitor) throws CoreException
	{
		try
		{
			Class<?> loadClass = getParentClassLoader().loadClass(getInput());
			Method method = loadClass.getMethod("main", (new String[] {}).getClass()); //$NON-NLS-1$
			method.invoke(null, new Object[] {arguments});
			return Status.OK_STATUS;
		}
		catch (Throwable t) // SUPPRESS CHECKSTYLE see below
		// Catch all exception a user written class might throw at us, avoid
		// crashing the system because of user code.
		{
			return CessarPluginActivator.getDefault().createStatus(t);
		}
	}
}
