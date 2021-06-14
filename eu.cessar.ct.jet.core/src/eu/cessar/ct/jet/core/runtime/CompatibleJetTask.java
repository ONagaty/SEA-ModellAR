/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 1, 2010 4:04:38 PM </copyright>
 */
package eu.cessar.ct.jet.core.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.jet.core.EJetExecutionMode;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;

/**
 * @author uidl6458
 * 
 */
public class CompatibleJetTask extends AbstractJetTask
{

	/**
	 * @param family
	 * @param inputObject
	 * @param name
	 */
	public CompatibleJetTask(ICessarTaskManagerImpl<IFile> manager, String name, IFile input,
		ClassLoader parentClassLoader, IFolder outputFolder, EObject model)
	{
		super(manager, name, input, parentClassLoader, outputFolder, model);
		executionMode = EJetExecutionMode.SEQUENTIAL;
		setRule(input.getProject());
	}

	/**
	 * Execute the code generator. If successful the output file has to be
	 * generated and the result to be stored inside the executionResult variable
	 * 
	 * @param newInstance
	 * @param monitor
	 * @return
	 */
	@Override
	protected IStatus runCodeGenerator(Class<?> clazz, String outputFileName,
		IProgressMonitor monitor)
	{
		monitor.beginTask("Executing " + clazz.getName(), 12); //$NON-NLS-1$
		try
		{
			Object newInstance = clazz.newInstance();
			Method setOutputFileName;
			Method getOutputFileName;
			Method generate;
			try
			{
				generate = clazz.getMethod("generate", Object.class); //$NON-NLS-1$
			}
			catch (NoSuchMethodException e)
			{
				// report this, we cannot generate
				IStatus status = CessarPluginActivator.getDefault().createStatus(IStatus.ERROR,
					"The class {0} does not have a generate(Object) method, cannot execute.", //$NON-NLS-1$
					newInstance.getClass().getName());
				getLogger().log(status);
				return status;
			}
			try
			{
				setOutputFileName = clazz.getMethod("setOutputFileName", String.class); //$NON-NLS-1$
				getOutputFileName = clazz.getMethod("getOutputFileName"); //$NON-NLS-1$
			}
			catch (NoSuchMethodException e)
			{
				// ignore, there is no support to set/get the jet output
				// but be sure that both variables are null
				setOutputFileName = null;
				getOutputFileName = null;
			}
			monitor.worked(1);
			String generatedOutput;
			if (setOutputFileName != null)
			{
				setOutputFileName.invoke(newInstance, outputFileName);
			}
			try
			{
				generatedOutput = (String) generate.invoke(newInstance, model);
				monitor.worked(10);
			}
			catch (Throwable t)
			{
				if (t instanceof InvocationTargetException)
				{
					InvocationTargetException ex = (InvocationTargetException) t;
					if (ex.getCause() != null)
					{
						t = ex.getCause();
					}
				}
				IStatus status = CessarPluginActivator.getDefault().createStatus(t);
				getLogger().log(status);
				return status;
			}
			if (getOutputFileName != null)
			{
				outputFileName = (String) getOutputFileName.invoke(newInstance);
			}
			// create generated file
			try
			{
				IFile outputFile = createOutputFile(generatedOutput, outputFileName,
					new SubProgressMonitor(monitor, 1));
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
}
