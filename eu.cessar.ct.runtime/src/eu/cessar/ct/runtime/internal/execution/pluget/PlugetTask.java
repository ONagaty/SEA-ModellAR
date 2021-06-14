/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Feb 1, 2010 11:50:27 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution.pluget;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.core.platform.util.JarUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.runtime.execution.ReversedURLClassLoader;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.ICessarPluget;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * @author uidl6458
 *
 */
public class PlugetTask extends CessarTask<IFile>
{

	private static final String ACTIVE_AUTOSAR_VERSION_4X = "cessar.ActiveAutosarMetamodel.4x"; //$NON-NLS-1$
	private static final String VERSION_PROPERTY = "Specification-Version"; //$NON-NLS-1$
	private final String[] arguments;

	/**
	 * @param manager
	 * @param inputObject
	 * @param name
	 * @param cl
	 * @param arguments
	 * @param monitor
	 */
	public PlugetTask(ICessarTaskManagerImpl<IFile> manager, String name, IFile inputObject, ClassLoader cl,
		String[] arguments)
	{
		super(manager, name, inputObject, cl);
		this.arguments = arguments;
		setRule(inputObject.getProject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.CessarTask#runTask(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runTask(IProgressMonitor monitor) throws CoreException
	{
		IStatus status = null;

		JarFile jarFile;
		ILogger logger = LoggerFactory.getLogger();

		try
		{
			logger.info(">>> Executing pluget file: " //$NON-NLS-1$
				+ getInput().getProjectRelativePath().toString());
			IProject project = getInput().getProject();
			jarFile = new JarFile(getInput().getRawLocation().toOSString());
			Manifest manifest = jarFile.getManifest();
			logAdditionalInformation(manifest, logger);

			// create the class loader
			URL url = null;
			try
			{
				url = getInput().getRawLocation().toFile().toURI().toURL();
			}
			catch (MalformedURLException ex)
			{
				status = CessarPluginActivator.getDefault().createStatus(ex);
				logger.log(status);
				jarFile.close();
				return status;
			}

			ReversedURLClassLoader classLoader = new ReversedURLClassLoader(new URL[] {url}, getParentClassLoader());
			try
			{
				status = runPluget(monitor, jarFile, logger, project, classLoader);
			}
			catch (Throwable t) // SUPPRESS CHECKSTYLE see below
			// Catch all exception a pluget might throw at us, we don't want to
			// crash the system because of a poorly written pluget
			{
				status = CessarPluginActivator.getDefault().createStatus(t);
			}
			finally
			{
				classLoader.close();
				jarFile.close();
			}
			return status;
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

	@SuppressWarnings("static-method")
	private void logAdditionalInformation(Manifest manifest, ILogger logger)
	{
		String currentMM4x = System.getProperty(ACTIVE_AUTOSAR_VERSION_4X);
		String version = null;

		if (manifest != null && manifest.getMainAttributes() != null)
		{
			Attributes attributes = manifest.getMainAttributes();
			version = attributes.getValue(VERSION_PROPERTY);
		}

		if (version != null && !version.isEmpty())
		{
			logger.info("Pluget version: " + version); //$NON-NLS-1$
		}
		else
		{
			logger.warn("Missing version for pluget!!!"); //$NON-NLS-1$
		}

		if (currentMM4x != null && !currentMM4x.isEmpty())
		{
			logger.info("Used Meta-Model: " + currentMM4x); //$NON-NLS-1$
		}
		else
		{
			logger.warn("Missing Meta-Model!!!!"); //$NON-NLS-1$
		}

	}

	/**
	 * Runs the pluget packed inside the specified jarFile from within the given project, using the provided
	 * classLoader. The progressMonitor will monitor the progress of the activity.
	 *
	 * @param monitor
	 *        the {@link IProgressMonitor} to be used
	 * @param jarFile
	 *        the {@link JarFile} that contains the pluget
	 * @param logger
	 *        the {@link ILogger} used to log errors
	 * @param project
	 *        the {@link IProject} that contains the pluget jar file
	 * @param classLoader
	 *        the {@link ReversedURLClassLoader} to be used for loading the pluget class
	 * @return the {@link IStatus} of running the pluget
	 * @throws Exception
	 */
	private IStatus runPluget(IProgressMonitor monitor, JarFile jarFile, ILogger logger, IProject project,
		ReversedURLClassLoader classLoader) throws Exception
	{
		List<Class<ICessarPluget>> classes = JarUtils.getClasses(jarFile, classLoader, ICessarPluget.class);
		monitor.beginTask("Executing pluget: " + getName(), classes.size()); //$NON-NLS-1$
		IStatus resultStatus = Status.OK_STATUS;
		for (Class<ICessarPluget> cls: classes)
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
					ICessarPluget instance = cls.newInstance();
					instance.run(project, monitor, arguments);
				}
				catch (Throwable t) // SUPPRESS CHECKSTYLE see below
				// Catch all exception a pluget might throw at us, we don't want
				// to crash the system because of a poorly written pluget.
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

}
