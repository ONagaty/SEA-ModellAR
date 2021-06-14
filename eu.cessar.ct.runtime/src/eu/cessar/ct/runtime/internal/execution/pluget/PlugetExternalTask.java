/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4449<br/>
 * Feb 25, 2014 11:44:27 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.internal.execution.pluget;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.core.platform.util.JarUtils;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.runtime.execution.ReversedURLClassLoader;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.ICessarPluget;

/**
 * Cessar Task to run external pluget
 * 
 * @author uidg4449
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Tue Aug 5 16:52:53 2014 %
 * 
 *         %version: 3 %
 */
public class PlugetExternalTask extends CessarTask<URL>
{

	private final String[] arguments;
	private final IProject project;

	/**
	 * @param manager
	 * @param name
	 * @param input
	 * @param parentClassLoader
	 * @param project
	 * @param arguments
	 */
	public PlugetExternalTask(ICessarTaskManagerImpl<URL> manager, String name, URL input,
		ClassLoader parentClassLoader, IProject project, String[] arguments)
	{
		super(manager, name, input, parentClassLoader);
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
	protected IStatus runTask(IProgressMonitor monitor) throws CoreException
	{
		IStatus status = Status.OK_STATUS;
		JarFile jarFile = null;
		ReversedURLClassLoader classLoader = null;
		try
		{
			try
			{
				URL url = getInput();
				String path = URLDecoder.decode(url.getPath(), "UTF-8"); //$NON-NLS-1$
				jarFile = new JarFile(path);
				classLoader = new ReversedURLClassLoader(new URL[] {url}, getParentClassLoader());
				List<Class<ICessarPluget>> plugetClasses = JarUtils.getClasses(jarFile, classLoader,
					ICessarPluget.class);

				// Runs each pluget class from the jar
				for (Class<ICessarPluget> plugetClass: plugetClasses)
				{
					int modifier = plugetClass.getModifiers();
					if ((modifier & (Modifier.ABSTRACT | Modifier.INTERFACE)) == 0)
					{
						ICessarPluget plugetInstance = plugetClass.newInstance();
						plugetInstance.run(project, monitor, arguments);
					}
				}

				return status;
			}
			catch (Throwable t) // SUPPRESS CHECKSTYLE see below
			// Catch all exception a pluget might throw at us, we don't want to
			// crash the system because of a poorly written pluget.
			{
				status = CessarPluginActivator.getDefault().createStatus(t);
				return status;
			}
			finally
			{
				if (null != classLoader)
				{
					classLoader.close();
				}
				if (null != jarFile)
				{
					jarFile.close();
				}
			}
		}
		catch (IOException e)
		{
			status = CessarPluginActivator.getDefault().createStatus(e);
			return status;
		}
		finally
		{
			monitor.done();
		}
	}
}
