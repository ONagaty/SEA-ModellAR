/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Feb 1, 2010 4:04:38 PM </copyright>
 */
package eu.cessar.ct.jet.core.runtime;

import java.lang.reflect.Constructor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.jet.core.EJetExecutionMode;
import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.sdk.ICodeGenerator;
import eu.cessar.ct.sdk.pm.IPresentationModel;

/**
 * @author uidl6458
 *
 */
public class JetTask extends AbstractJetTask
{

	/**
	 * @param manager
	 * @param family
	 * @param inputObject
	 * @param name
	 * @param input
	 * @param parentClassLoader
	 * @param outputFolder
	 * @param model
	 * @param initStatus
	 */
	public JetTask(ICessarTaskManagerImpl<IFile> manager, String name, IFile input, ClassLoader parentClassLoader,
		IFolder outputFolder, EObject model, IStatus initStatus)
	{
		super(manager, name, input, parentClassLoader, outputFolder, model);
		executionMode = JETCoreUtils.getJetJarExecutionMode(input);
		if (executionMode == EJetExecutionMode.SEQUENTIAL)
		{
			setRule(input.getProject());
		}
		setJetStatus(initStatus);
	}

	/**
	 * Execute the code generator. If successful the output file has to be generated and the result to be stored inside
	 * the executionResult variable
	 *
	 * @param newInstance
	 * @param monitor
	 * @return IStatus
	 */
	@Override
	protected IStatus runCodeGenerator(Class<?> clazz, String outputFileName, IProgressMonitor monitor)
	{
		monitor.beginTask("Executing " + clazz.getName(), 100); //$NON-NLS-1$
		try
		{
			Object newInstance;
			try
			{
				Constructor<?> constructor = clazz.getConstructor(IProject.class, IFolder.class);
				newInstance = constructor.newInstance(getInput().getProject(), outputFolder);
			}
			catch (NoSuchMethodException e)
			{
				// fail back to no constructor method
				newInstance = clazz.newInstance();
			}
			if (newInstance instanceof ICodeGenerator)
			{
				ICodeGenerator generator = (ICodeGenerator) newInstance;

				monitor.worked(50);
				String generatedOutput;
				generator.setOutputFileName(outputFileName);
				try
				{
					generatedOutput = generator.generate((IPresentationModel) model);
					monitor.worked(800);
				}
				catch (Throwable t)
				{
					IStatus status = CessarPluginActivator.getDefault().createStatus(t);
					getLogger().log(status);
					return status;
				}

				outputFileName = generator.getOutputFileName();

				// create generated file
				try
				{
					IFile outputFile = createOutputFile(generatedOutput, outputFileName, new SubProgressMonitor(
						monitor, 100));
					executionResult = outputFile;
					return Status.OK_STATUS;
				}
				catch (Throwable t)
				{
					IStatus status = CessarPluginActivator.getDefault().createStatus(t);
					getLogger().log(status);
					return status;
				}
			}
			else
			{
				IStatus status = CessarPluginActivator.getDefault().createStatus(IStatus.ERROR,
					"The class {0} is not of the type ICodeGenerator. ", //$NON-NLS-1$
					newInstance.getClass().getName());
				getLogger().log(status);
				return status;
			}
		}
		catch (Throwable t)
		{
			IStatus status = CessarPluginActivator.getDefault().createStatus(t);
			getLogger().log(status);
			return status;
		}
		finally
		{
			monitor.done();
		}
	}

	/**
	 * @param initStatus
	 */
	@Override
	public void setJetStatus(IStatus initStatus)
	{
		super.setJetStatus(initStatus);
	}

}
