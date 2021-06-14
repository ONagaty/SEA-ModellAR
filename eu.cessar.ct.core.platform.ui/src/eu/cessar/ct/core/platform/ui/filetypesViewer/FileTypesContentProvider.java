/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Oct 31, 2011 11:04:36 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.filetypesViewer;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.viewers.AbstractFilteredTreeContentProvider;

/**
 * Content provider for the File types viewer.
 *
 * @author uidl6870
 *
 * @Review uidl6458 - 19.04.2012
 *
 */
public class FileTypesContentProvider extends AbstractFilteredTreeContentProvider
{
	/** */
	protected boolean showHiddenResources;
	private List<String> allowedExtensions;

	/**
	 * @param allowedExtensions
	 */
	public FileTypesContentProvider(List<String> allowedExtensions)
	{
		this.allowedExtensions = allowedExtensions;
	}

	/**
	 * @param showHidden
	 */
	public void showHidden(boolean showHidden)
	{
		showHiddenResources = showHidden;
	}

	public Object getParent(Object element)
	{
		return ((IResource) element).getParent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.core.platform.ui.resourcesNavigator.AbstractFilteredTreeContentProvider#getAllElements(java.lang
	 * .Object)
	 */
	@Override
	public Object[] getAllElements(Object inputElement)
	{
		if (inputElement instanceof IWorkspaceRoot)
		{
			return ((IWorkspaceRoot) inputElement).getProjects();
		}

		if (inputElement instanceof List<?>)
		{
			return ((List<?>) inputElement).toArray();
		}

		if (inputElement instanceof IContainer)
		{
			return getAllChildren(inputElement);
		}

		return new Object[0];
	}

	/**
	 * If element is a container (project or folder), returns all its members, otherwise returns an empty array.
	 */
	@Override
	protected Object[] getAllChildren(Object element)
	{
		if (!(element instanceof IFile))
		{
			try
			{
				return ((IContainer) element).members(showHiddenResources ? IContainer.INCLUDE_HIDDEN : IResource.NONE);
			}
			catch (CoreException e)
			{
				// Ignore
			}
		}
		return Collections.EMPTY_LIST.toArray();
	}

	@Override
	public boolean hasChildren(Object element)
	{
		return !(element instanceof IFile) && super.hasChildren(element);
	}

	/**
	 * Returns true for files of the right extension, false otherwise.
	 *
	 * @see AbstractFilteredTreeContentProvider#isValidElement(Object)
	 */
	@Override
	protected boolean isValidElement(Object element)
	{
		if (((IResource) element).isHidden() && !showHiddenResources)
		{
			// Should not happen!
			// Resources are already filtered in getAllChildren with respect to
			// INCLUDE_HIDDEN
			return false;
		}

		// if folders shall be filtered
		if (allowedExtensions.size() == 1 && allowedExtensions.contains(PlatformUIConstants.FOLDER_FILTER_KEY))
		{
			return handleFolder(element);
		}

		// if all the files shall be displayed and any extension is valid
		if ((allowedExtensions.size() == 1 && (PlatformUIConstants.ANY_FILE_EXTENSION_KEY.equals(allowedExtensions.get(0)) || PlatformUIConstants.EMPTY_STRING.equals(allowedExtensions.get(0)))))
		{
			return handleAnyExtension(element);
		}

		// if given file extensions exist
		if (element instanceof IFile)
		{
			if (null == allowedExtensions || allowedExtensions.isEmpty())
			{
				return true;
			}
			else
			{
				String fileExtension = ((IFile) element).getFileExtension();
				return allowedExtensions.contains(fileExtension);
			}
		}

		return false;
	}

	/**
	 * Returns a hash over element's full path.
	 *
	 * @see AbstractFilteredTreeContentProvider#getId(Object)
	 */
	@Override
	protected Object getId(Object element)
	{
		return ((IResource) element).getFullPath().toString().hashCode();
		// return ((IResource) element).getFullPath().toString().replace(' ',
		// '_');
	}

	/**
	 * Check if the current element is valid, if any file extension shall be accepted
	 *
	 * @param element
	 * @return true if the element is valid and false otherwise
	 */
	private static boolean handleAnyExtension(Object element)
	{
		if (element instanceof IFolder)
		{
			return false;
		}
		if (element instanceof IFile)
		{
			return true;
		}
		return false;
	}

	/**
	 * Check if the current element is valid, if only folders shall be accepted
	 *
	 * @param element
	 * @return true if the element is valid and false otherwise
	 */
	private static boolean handleFolder(Object element)
	{
		if (element instanceof IFolder)
		{
			IFolder folder = (IFolder) element;
			try
			{
				// as long as the current folder has subfolders, element is not valid
				IResource[] members = folder.members();
				for (IResource iResource: members)
				{
					if (iResource instanceof IFolder)
					{
						return false;
					}
				}
				// if no subfolder found anymore, element is considered valid
				return true;
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		return false;

	}

}
