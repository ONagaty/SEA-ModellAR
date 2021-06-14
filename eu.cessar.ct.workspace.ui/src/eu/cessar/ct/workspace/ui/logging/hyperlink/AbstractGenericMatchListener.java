/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jul 27, 2012 1:43:49 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IPatternMatchListener;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.part.FileEditorInput;

import eu.cessar.ct.workspace.ui.logging.LoggerConsole;
import eu.cessar.req.Requirement;

/**
 * @author uidu0944
 *
 */
@Requirement(
	reqID = "201938")
public abstract class AbstractGenericMatchListener implements IPatternMatchListener
{

	/**
	 * The console this notifier is tracking
	 */
	protected LoggerConsole fConsole;

	/**
	 * @param fConsole
	 *        to add the listener
	 */
	public AbstractGenericMatchListener(LoggerConsole fConsole)
	{
		this.fConsole = fConsole;
	}

	/**
	 * @return active project or null.
	 */
	@SuppressWarnings("static-method")
	protected IProject getProject()
	{
		IFile ifile = null;
		IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench().getWorkbenchWindows();
		if (workbenchWindows != null && workbenchWindows.length > 0)
		{
			IEditorPart activeEditor = workbenchWindows[0].getActivePage().getActiveEditor();
			if (activeEditor != null)
			{
				IEditorInput editorInput = activeEditor.getEditorInput();
				if (editorInput instanceof FileEditorInput)
				{
					ifile = ((FileEditorInput) editorInput).getFile();
				}
			}
		}
		if (ifile != null)
		{
			return ifile.getProject();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return active file or null.
	 */
	@SuppressWarnings("static-method")
	protected IFile getActiveFile()
	{
		IFile ifile = null;
		IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench().getWorkbenchWindows();
		if (workbenchWindows != null && workbenchWindows.length > 0)
		{
			IEditorPart activeEditor = workbenchWindows[0].getActivePage().getActiveEditor();
			if (activeEditor != null)
			{
				IEditorInput editorInput = activeEditor.getEditorInput();
				if (editorInput instanceof FileEditorInput)
				{
					ifile = ((FileEditorInput) editorInput).getFile();
				}
			}
		}
		return ifile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IPatternMatchListenerDelegate#connect(org.eclipse.ui.console.TextConsole)
	 */
	public void connect(TextConsole console)
	{
		// do nothing here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IPatternMatchListenerDelegate#disconnect()
	 */
	public void disconnect()
	{
		// do nothing here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IPatternMatchListenerDelegate#matchFound(org.eclipse.ui.console.PatternMatchEvent)
	 */
	public abstract void matchFound(PatternMatchEvent event);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IPatternMatchListener#getPattern()
	 */
	public String getPattern()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IPatternMatchListener#getCompilerFlags()
	 */
	public int getCompilerFlags()
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IPatternMatchListener#getLineQualifier()
	 */
	public String getLineQualifier()
	{
		return null;
	}

}