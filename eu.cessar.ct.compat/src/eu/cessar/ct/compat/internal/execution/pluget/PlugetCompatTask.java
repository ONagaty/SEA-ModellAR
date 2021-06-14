/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 1, 2010 11:50:27 AM </copyright>
 */
package eu.cessar.ct.compat.internal.execution.pluget;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.jar.JarFile;

import org.autosartools.ecuconfig.pdk.core.IMosartPluget;
import org.autosartools.general.core.project.IAutosarProject;
import org.autosartools.general.core.project.impl.AutosarProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.compat.internal.CessarPluginActivator;
import eu.cessar.ct.core.platform.util.JarUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.runtime.execution.ReversedURLClassLoader;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class PlugetCompatTask extends CessarTask<IFile>
{

	private final String[] arguments;

	/**
	 * @param inputObject
	 * @param name
	 * @param monitor
	 */
	public PlugetCompatTask(ICessarTaskManagerImpl<IFile> manager, String name, IFile inputObject,
		ClassLoader cl, String[] arguments)
	{
		super(manager, name, inputObject, cl);
		this.arguments = arguments;
		setRule(inputObject.getProject());
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.CessarTask#runTask(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runTask(IProgressMonitor monitor) throws CoreException
	{
		JarFile jarFile;
		ILogger logger = LoggerFactory.getLogger();
		try
		{
			logger.info(">>> Executing pluget file: " //$NON-NLS-1$
				+ getInput().getProjectRelativePath().toString());
			IProject project = getInput().getProject();
			jarFile = new JarFile(getInput().getRawLocation().toOSString());
			// create the class loader
			URL url = null;
			try
			{
				url = getInput().getRawLocation().toFile().toURI().toURL();
			}
			catch (MalformedURLException ex)
			{
				IStatus status = CessarPluginActivator.getDefault().createStatus(ex);
				logger.log(status);
				jarFile.close();
				return status;
			}
			ReversedURLClassLoader classLoader = new ReversedURLClassLoader(new URL[] {url},
				getParentClassLoader());
			try
			{
				// locate the plugets within the inputObject
				List<Class<IMosartPluget>> classes = JarUtils.getClasses(jarFile, classLoader,
					IMosartPluget.class);
				// List<CessarTask<IFile>> result = new
				// ArrayList<CessarTask<IFile>>();
				monitor.beginTask("Executing pluget: " + getName(), classes.size()); //$NON-NLS-1$
				IStatus resultStatus = Status.OK_STATUS;
				IAutosarProject autosarProject = new AutosarProject(project);
				for (Class<IMosartPluget> cls: classes)
				{
					if (monitor.isCanceled())
					{
						break;
					}
					monitor.subTask(cls.getCanonicalName());
					if ((cls.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE)) == 0)
					{
						try
						{
							IMosartPluget instance = cls.newInstance();
							instance.run(autosarProject, monitor, arguments);
						}
						catch (Throwable t)
						{
							logger.log(t);
							resultStatus = PlatformUtils.combineStatus(resultStatus,
								CessarPluginActivator.getDefault().createStatus(t));
						}
					}
					monitor.worked(1);
				}
				return resultStatus;
			}
			catch (Throwable t)
			{
				return CessarPluginActivator.getDefault().createStatus(t);
			}
			finally
			{
				classLoader.close();
				jarFile.close();
			}
		}
		catch (IOException e)
		{
			return CessarPluginActivator.getDefault().createStatus(e);
		}
		finally
		{
			monitor.done();
		}
	}
}
