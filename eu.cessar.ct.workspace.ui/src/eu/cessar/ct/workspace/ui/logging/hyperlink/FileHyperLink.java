/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jul 24, 2012 3:50:00 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.ide.IDE;

import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;

/**
 * @author uidu0944
 *
 */
@Requirement(
	reqID = "201721")
public class FileHyperLink extends GenericHyperLink implements IHyperlink
{

	private String fileLocation;

	/**
	 *
	 * @param fileLocation
	 *        the filename or a path ending with the filename relative to the containing project.
	 */
	public FileHyperLink(String fileLocation)
	{
		super();
		this.fileLocation = fileLocation;
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
	public void linkActivated()
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null)
		{
			IWorkbenchPage page = window.getActivePage();
			try
			{
				IResource res = lookupResource(fileLocation);
				if ((res != null) && (res instanceof IFile))
				{
					File file = new File(res.getLocationURI());

					IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());

					if (fileStore != null)
					{
						IDE.openEditor(page, (IFile) res);
					}
				}
			}
			catch (PartInitException ex)
			{
				CessarPluginActivator.getDefault().logError(ex);
			}
		}
	}

}
