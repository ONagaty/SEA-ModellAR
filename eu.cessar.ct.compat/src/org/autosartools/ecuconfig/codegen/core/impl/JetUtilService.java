/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package org.autosartools.ecuconfig.codegen.core.impl;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.autosartools.ecuconfig.codegen.core.JetUtil;
import org.autosartools.ecuconfig.codegen.core.JetUtil.Service;
import org.autosartools.ecuconfig.codegen.core.delegates.LicenseHandlerDelegate;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.CessarPluginActivator;
import eu.cessar.ct.core.platform.io.MultiplexedOutputStream;
import eu.cessar.ct.core.platform.io.NullOutputStream;
import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.jet.core.runtime.AbstractJetTask;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.workspace.logging.ILogger2;
import eu.cessar.ct.workspace.logging.LoggingConstants;

/**
 * Implementation of the service offered by {@link JetUtil}.
 * 
 * @Review uidl7321 - Apr 12, 2012
 */
public final class JetUtilService implements Service
{

	/**
	 * The singleton member
	 */
	public static final Service eINSTANCE = new JetUtilService();

	private ThreadLocal<PrintStream> stream = new ThreadLocal<PrintStream>();

	private JetUtilService()
	{
		// do nothing
	}

	/**
	 * @return
	 */
	private static RuntimeException getIllegalStateException()
	{
		return new IllegalStateException("Method not called from within a template"); //$NON-NLS-1$		
	}

	/**
	 * Return the current cessar task. If no such task can be located an IllegalStateException is thrown
	 * 
	 * @return
	 */
	private static CessarTask<?> getCurrentTask()
	{
		CessarTask<?> currentTask = CessarTask.getCurrentTask();
		if (currentTask == null)
		{
			throw getIllegalStateException();
		}
		else
		{
			return currentTask;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#printToConsole(java.lang.String)
	 */
	public void printToConsole(String message)
	{
		getConsoleOutput().print(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#printToConsoleLn(java.lang.String)
	 */
	public void printToConsoleLn(String message)
	{
		getConsoleOutput().println(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#getConsoleOutput()
	 */
	public PrintStream getConsoleOutput()
	{
		PrintStream printStream = stream.get();
		if (printStream != null)
		{
			return printStream;
		}
		else
		{
			ILogger2 logger = (ILogger2) LoggerFactory.getLogger();
			List<OutputStream> streams = logger.getCurrentStreams(LoggingConstants.LEVEL_INFO);
			if (streams.size() == 0)
			{
				// the logger is not yet created.
				printStream = new PrintStream(new NullOutputStream());
			}
			else
			{
				MultiplexedOutputStream mout = new MultiplexedOutputStream(streams);
				printStream = new PrintStream(mout);
			}
			stream.set(printStream);
			return printStream;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#getJetOutputFolder()
	 */
	public File getJetOutputFolder()
	{
		CessarTask<?> task = getCurrentTask();
		if (task instanceof AbstractJetTask)
		{
			AbstractJetTask jetTask = (AbstractJetTask) task;
			return jetTask.getOutputFolder().getLocation().toFile();
		}
		else
		{
			throw getIllegalStateException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#getProjectFiles(java.lang.String)
	 */
	public List<?> getProjectFiles(String extension)
	{
		CessarTask<?> task = getCurrentTask();
		IProject project = task.getTaskManager().getProject();
		List<File> result = new ArrayList<File>();
		try
		{
			List<IFile> files = ResourceUtils.getProjectFiles(project, extension);
			for (IFile file: files)
			{
				result.add(file.getLocation().toFile());
			}
		}
		catch (CoreException e)
		{
			// log and ignore
			CessarPluginActivator.getDefault().logError(e);
			e.printStackTrace(getConsoleOutput());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#sortByShortname(org.eclipse.emf.common.util.EList)
	 */
	public EList sortByShortName(EList list)
	{
		EList<EObject> result = new BasicEList<EObject>();
		Iterator<?> iter = list.iterator();

		final String shortNameValue = "shortName"; //$NON-NLS-1$

		while (iter.hasNext())
		{
			EObject obj = (EObject) iter.next();

			String shortName = getParameterValue(obj, shortNameValue);
			if (shortName != null)
			{
				result.add(obj);
			}
		}
		Collections.sort(result, new Comparator<EObject>()
		{
			public int compare(final EObject o1, final EObject o2)
			{
				String sn1 = getParameterValue(o1, shortNameValue);
				String sn2 = getParameterValue(o2, shortNameValue);
				return sn1.compareTo(sn2);
			}
		});
		return result;

	}

	/**
	 * <p>
	 * returns the string representation of the value for the parameter with the specified <code>parameterName</code> in
	 * the container represented by <code>eObject</code>, if there is a parameter known under that name, or
	 * <code>null</code>, if no such parameter is known. It is assumed that the parameter is directly contained in the
	 * parameter container represented by <code>eObject</code>, parameters that are contained in subcontainers of that
	 * container are ignored.
	 * </p>
	 * 
	 * @param eObject
	 *        an object that is assumed to represent a container
	 * @param parameterName
	 *        the name of a parameter
	 * @return the string representation of the value of the specified parameter, or <code>null</code>
	 */
	private static String getParameterValue(final EObject eObject, final String parameterName)
	{
		String result = null;
		if (eObject != null)
		{
			EStructuralFeature sf = eObject.eClass().getEStructuralFeature(parameterName);
			if (sf != null && eObject.eIsSet(sf))
			{
				Object obj = eObject.eGet(sf);
				if (obj instanceof String)
				{
					result = (String) obj;
				}
				else if (obj != null)
				{
					result = obj.toString();
				}
			}
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#checkAndInstallLicense(java.lang.String,
	 *      java.lang.String)
	 * 
	 * @deprecated there is no implementation yet, marked as such to avoid usage
	 */
	@Deprecated
	public void checkAndInstallLicense(String productName, String moduleName)
	{
		// not implemented
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#getCurrentProjectEcuExtracts()
	 */
	public List<?> getCurrentProjectEcuExtracts()
	{
		return getProjectFiles("ecuextract"); //$NON-NLS-1$
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#getLicenseHandler()
	 * @deprecated there is no implementation yet, marked as such to avoid usage
	 */
	@Deprecated
	public LicenseHandlerDelegate getLicenseHandler()
	{
		return LicenseHandlerDelegate.getInstance();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.ecuconfig.codegen.core.JetUtil.Service#isUIMode()
	 * @deprecated there is no implementation yet, marked as such to avoid usage
	 */
	@Deprecated
	public boolean isUIMode()
	{
		return false;
	}

}
