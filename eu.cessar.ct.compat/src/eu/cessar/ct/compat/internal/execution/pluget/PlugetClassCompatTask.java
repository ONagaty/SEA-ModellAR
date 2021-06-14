/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 1, 2010 11:50:27 AM </copyright>
 */
package eu.cessar.ct.compat.internal.execution.pluget;

import org.autosartools.ecuconfig.pdk.core.IMosartPluget;
import org.autosartools.general.core.project.impl.AutosarProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import eu.cessar.ct.compat.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class PlugetClassCompatTask extends CessarTask<String>
{

	private final String[] arguments;
	private final IProject project;

	/**
	 * @param inputObject
	 * @param name
	 */
	public PlugetClassCompatTask(ICessarTaskManagerImpl<String> manager, String name,
		String inputObject, ClassLoader cl, IProject project, String[] arguments)
	{
		super(manager, name, inputObject, cl);
		this.project = project;
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
			IMosartPluget pluget = (IMosartPluget) loadClass.newInstance();
			pluget.run(new AutosarProject(project), new SubProgressMonitor(monitor,
				IProgressMonitor.UNKNOWN), arguments);
			return Status.OK_STATUS;
		}
		catch (Throwable t)
		{
			return CessarPluginActivator.getDefault().createStatus(t);
		}
	}
}
