/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jul 24, 2012 5:06:47 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.sourcelookup.containers.ClasspathVariableSourceContainer;
import org.eclipse.jdt.launching.sourcelookup.containers.JavaProjectSourceContainer;
import org.eclipse.jdt.launching.sourcelookup.containers.PackageFragmentRootSourceContainer;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.ide.IDE;

import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.req.Requirement;

/**
 * @author uidu0944
 *
 */
@Requirement(
	reqID = "201719")
public class StackTraceHyperLink extends GenericHyperLink implements IHyperlink
{

	private String stackTrace;
	private IProject iProject;
	private String javaFileFullPath;
	private int lineNumber;

	/**
	 * @param iProject
	 * @param stackTrace
	 */
	public StackTraceHyperLink(IProject iProject, String stackTrace)
	{
		super();
		this.stackTrace = stackTrace;
		this.iProject = iProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IHyperlink#linkEntered()
	 */
	public void linkEntered()
	{
		// do nothing here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IHyperlink#linkExited()
	 */
	public void linkExited()
	{
		// do nothing here
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.console.IHyperlink#linkActivated()
	 */
	@Requirement(
		reqID = "201726")
	public void linkActivated()
	{
		boolean isMatch = false;

		parseStackTrace();

		String fileName = javaFileFullPath.replace('.', File.separatorChar) + ".java"; //$NON-NLS-1$

		List<ISourceContainer> containerList = doGetSourceContainers(iProject, fileName);

		for (ISourceContainer iSourceContainer: containerList)
		{
			if (iSourceContainer instanceof JavaProjectSourceContainer)
			{
				isMatch = handleJavaProjectSourceContainer(isMatch, fileName, iSourceContainer);
			}
			else
			{
				if (iSourceContainer instanceof PackageFragmentRootSourceContainer)
				{
					isMatch = handlePackageFragmentRootSourceContainer(isMatch, fileName, iSourceContainer);
				}
			}
		}
		if (!isMatch)
		{
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
				Messages.link_console_stacktrace_unavailable,
				MessageFormat.format(Messages.link_console_cannot_load_source, javaFileFullPath));
		}
	}

	/**
	 * @param isMatch
	 * @param fileName
	 * @param iSourceContainer
	 * @return
	 */
	private boolean handleJavaProjectSourceContainer(boolean isMatchInitValue, String fileName,
		ISourceContainer iSourceContainer)
	{
		boolean isMatch = isMatchInitValue;

		JavaProjectSourceContainer javaSrcContainer = (JavaProjectSourceContainer) iSourceContainer;
		try
		{
			ISourceContainer[] sourceContainers = javaSrcContainer.getSourceContainers();
			for (int i = 0; i < sourceContainers.length; i++)
			{
				Object[] srcEl = sourceContainers[i].findSourceElements(fileName);

				if (srcEl != null && srcEl.length > 0)
				{
					isMatch = true;
					IFile file = (IFile) srcEl[0];
					IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.getRawLocationURI());
					IMarker m = file.createMarker(IMarker.MARKER);
					m.setAttribute(IMarker.LINE_NUMBER, lineNumber);

					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

					IEditorPart openEditorOnFileStore = IDE.openEditorOnFileStore(page, fileStore);
					IDE.gotoMarker(openEditorOnFileStore, m);
				}
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return isMatch;
	}

	/**
	 * @param isMatch
	 * @param fileName
	 * @param iSourceContainer
	 * @return
	 */
	@SuppressWarnings("static-method")
	private boolean handlePackageFragmentRootSourceContainer(boolean isMatchInitValue, String fileName,
		ISourceContainer iSourceContainer)
	{

		boolean isMatch = isMatchInitValue;

		PackageFragmentRootSourceContainer iPackageFragmentRootSourceContainer = (PackageFragmentRootSourceContainer) iSourceContainer;
		try
		{
			Object[] findSourceElements = iPackageFragmentRootSourceContainer.findSourceElements(fileName);
			if (findSourceElements != null && findSourceElements.length > 0)
			{
				isMatch = true;
				for (int i = 0; i < findSourceElements.length; i++)
				{
					IClassFile cF = (IClassFile) findSourceElements[i];

					try
					{
						JavaUI.openInEditor(cF);
					}
					catch (PartInitException e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}
				}
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return isMatch;
	}

	private void parseStackTrace()
	{
		// compute .java path
		int startIndex = stackTrace.indexOf("\tat"); //$NON-NLS-1$
		String s2 = stackTrace.substring(startIndex + 4, stackTrace.indexOf('(', startIndex));
		javaFileFullPath = s2.substring(0, s2.lastIndexOf('.'));

		// compute line number
		String lineNum = stackTrace;
		lineNum = lineNum.substring(lineNum.lastIndexOf(':') + 1, lineNum.lastIndexOf(')'));
		if (lineNum != null)
		{
			try
			{
				lineNumber = Integer.valueOf(lineNum).intValue();
			}
			catch (NumberFormatException nex)
			{
				CessarPluginActivator.getDefault().logError(nex);
			}
		}
	}

	private List<ISourceContainer> doGetSourceContainers(IProject project, String fileName)
	{
		IJavaProject javaProject = null;
		List<ISourceContainer> containers = new ArrayList<>();
		if (project != null && project.isAccessible())
		{
			javaProject = JavaCore.create(project);
		}
		else
		{
			String newFileName = fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1, fileName.length());
			javaProject = lookupProject(newFileName);
		}
		if (javaProject != null)
		{
			containers.add(new JavaProjectSourceContainer(javaProject));
			IClasspathEntry[] rawClasspath = null;
			try
			{
				rawClasspath = javaProject.getRawClasspath();
				for (IClasspathEntry entry: rawClasspath)
				{
					addSourceForDependencies(javaProject, entry, containers);
				}
			}
			catch (JavaModelException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

		}
		return containers;
	}

	private void addSourceForDependencies(IJavaProject javaProject, IClasspathEntry entry,
		List<ISourceContainer> containers) throws JavaModelException
	{
		ISourceContainer sourceContainer = null;

		switch (entry.getEntryKind())
		{
			case IClasspathEntry.CPE_CONTAINER:
				IClasspathContainer container = JavaCore.getClasspathContainer(entry.getPath(), javaProject);
				IClasspathEntry[] classpathEntries = container.getClasspathEntries();
				for (IClasspathEntry iClasspathEntry: classpathEntries)
				{
					addSourceForDependencies(javaProject, iClasspathEntry, containers);
				}
				break;
			case IClasspathEntry.CPE_LIBRARY:
				IPackageFragmentRoot root = javaProject.findPackageFragmentRoot(entry.getPath());
				if (root != null)
				{
					sourceContainer = new PackageFragmentRootSourceContainer(root);
					sourceContainer.getName();
				}
				break;
			case IClasspathEntry.CPE_PROJECT:
				String name = entry.getPath().segment(0);
				IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
				if (p.exists())
				{
					IJavaProject jp = JavaCore.create(p);
					if (jp.exists())
					{
						sourceContainer = new JavaProjectSourceContainer(jp);
					}
				}
				break;
			case IClasspathEntry.CPE_VARIABLE:
				sourceContainer = new ClasspathVariableSourceContainer(entry.getPath());
				break;
			default:
				break;
		}

		if (sourceContainer != null)
		{
			for (ISourceContainer container: containers)
			{
				if (container.getName().equals(sourceContainer.getName()))
				{
					return;
				}
			}
			containers.add(sourceContainer);
		}
	}

}
