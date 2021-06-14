/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jul 27, 2010 5:45:37 PM </copyright>
 */
package eu.cessar.ct.jet.core.runtime;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.commons.io.input.ReaderInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.platform.util.StatusUtil;

import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.jet.core.EJetExecutionMode;
import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.runtime.execution.ReversedURLClassLoader;
import eu.cessar.ct.sdk.logging.ILogger;

/**
 * @author uidl6458
 *
 */
public abstract class AbstractJetTask extends CessarTask<IFile>
{

	protected IFolder outputFolder;
	protected EJetExecutionMode executionMode = EJetExecutionMode.SEQUENTIAL;
	protected EObject model;

	/**
	 * @param name
	 * @param input
	 * @param parentClassLoader
	 */
	public AbstractJetTask(ICessarTaskManagerImpl<IFile> manager, String name, IFile input,
		ClassLoader parentClassLoader, IFolder outputFolder, EObject model)
	{
		super(manager, name, input, parentClassLoader);
		this.outputFolder = outputFolder;
		this.model = model;
	}

	/**
	 * Return the output folder where the code should be generated
	 *
	 * @return
	 */
	public IFolder getOutputFolder()
	{
		return outputFolder;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.resources.WorkspaceJob#runInWorkspace(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runTask(IProgressMonitor monitor) throws CoreException
	{
		// obtain binary name of jet located inside jetJarFile jar
		IFile jetJarFile = getInput();
		ILogger logger = getLogger();
		// 100 : class loading and instantiating
		// 800 : execution
		// 100 : saving
		monitor.beginTask("Executing " + jetJarFile.getProjectRelativePath().toString(), 101); //$NON-NLS-1$
		try
		{
			ResourceUtils.mkDirs(outputFolder, new SubProgressMonitor(monitor, 1));
			long startExecutionTime = System.currentTimeMillis();

			logger.info(">>> Executing jet (" + executionMode.name() + ") : " //$NON-NLS-1$//$NON-NLS-2$
				+ jetJarFile.getFullPath().makeRelative().toString());
			String binaryName = null;
			try
			{
				binaryName = JETCoreUtils.getJetBinaryName(jetJarFile);
			}
			catch (IOException e)
			{
				IStatus status = CessarPluginActivator.getDefault().createStatus(e);
				logger.log(status);
				return status;
			}
			if (binaryName == null)
			{
				IStatus status = CessarPluginActivator.getDefault().createStatus(IStatus.ERROR,
					"Cannot locate a jet class within {0} jar", jetJarFile.getName()); //$NON-NLS-1$
				logger.log(status);
				return status;
			}
			// create the class loader
			URL url = null;
			if (!jetJarFile.isLinked())
			{
				try
				{

					url = jetJarFile.getRawLocation().toFile().toURI().toURL();
				}
				catch (MalformedURLException ex)
				{
					IStatus status = CessarPluginActivator.getDefault().createStatus(ex);
					logger.log(status);
					return status;
				}
			}
			else
			{
				try
				{
					url = jetJarFile.getLocation().toFile().toURI().toURL();
				}
				catch (MalformedURLException e)
				{
					IStatus status = CessarPluginActivator.getDefault().createStatus(e);
					logger.log(status);
					return status;
				}
			}
			URL[] urls = new URL[] {url};
			ReversedURLClassLoader classLoader = new ReversedURLClassLoader(urls, getParentClassLoader());

			monitor.worked(50);
			try
			{
				Class<?> clazz = classLoader.loadClass(binaryName);

				String outputFileName = JETCoreUtils.getJetOutputFileName(jetJarFile.getName());
				if (outputFileName == null)
				{
					throw new CoreException(StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(),
						"Cannot determine the output file name for " + jetJarFile.getName())); //$NON-NLS-1$
				}

				IStatus result = runCodeGenerator(clazz, outputFileName, new SubProgressMonitor(monitor, 800));
				startExecutionTime = System.currentTimeMillis() - startExecutionTime;
				double resultTime = (double) startExecutionTime / 1000;
				String message = ">>> Execution of {0} done in {1,number,#.###} s"; //$NON-NLS-1$
				logger.info(MessageFormat.format(message, jetJarFile.getName(), resultTime));
				return result;
			}
			catch (Throwable t)
			{
				IStatus status = CessarPluginActivator.getDefault().createStatus(t);
				logger.log(status);
				return status;
			}
			finally
			{
				classLoader.close();
				classLoader = null;
			}
		}
		finally
		{
			monitor.done();
		}
	}

	/**
	 * @param clazz
	 * @param outputFileName
	 * @param logger
	 * @param subProgressMonitor
	 * @return
	 */
	protected abstract IStatus runCodeGenerator(Class<?> clazz, String outputFileName, IProgressMonitor monitor);

	/**
	 *
	 */
	protected IFile createOutputFile(final String content, String fileName, IProgressMonitor monitor)
		throws CoreException
	{
		monitor.beginTask("Saving generated file", 100); //$NON-NLS-1$
		try
		{
			if (outputFolder == null)
			{
				throw new CoreException(StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(),
					"The output folder is not set.")); //$NON-NLS-1$
			}
			if (fileName == null)
			{
				throw new CoreException(StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(),
					"The output file is not set.")); //$NON-NLS-1$
			}
			final IFile outFile = outputFolder.getFile(fileName);

			outputFolder.getWorkspace().run(new IWorkspaceRunnable()
			{

				public void run(IProgressMonitor monitor) throws CoreException
				{
					ReaderInputStream ris = new ReaderInputStream(new StringReader(content));
					// ByteArrayInputStream stream = new
					// ByteArrayInputStream(content.getBytes());
					if (outFile.exists())
					{
						if (outFile.isReadOnly())
						{
							// make it writable
							ResourceAttributes resourceAttributes = outFile.getResourceAttributes();
							resourceAttributes.setReadOnly(false);
							outFile.setResourceAttributes(resourceAttributes);
						}
						outFile.setContents(ris, true, true, new SubProgressMonitor(monitor, 90));
					}
					else
					{
						outFile.create(ris, true, new SubProgressMonitor(monitor, 90));
					}
				}
			}, outputFolder, IWorkspace.AVOID_UPDATE, monitor);

			String saveMessage = "Saved " + outFile.getFullPath().makeRelative().toString(); //$NON-NLS-1$
			if (outputFolder.isLinked())
			{
				saveMessage = "Saved " + outFile.getName() + " into workspace folder " //$NON-NLS-1$//$NON-NLS-2$
					+ outputFolder.getFullPath().makeRelative() + ". Location on disk: " //$NON-NLS-1$
					+ outFile.getLocation().toOSString();
			}
			getLogger().info(saveMessage);
			return outFile;
		}
		finally
		{
			monitor.done();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.CessarTask#dispose()
	 */
	@Override
	protected void dispose()
	{
		super.dispose();
		model = null;
	}
}
