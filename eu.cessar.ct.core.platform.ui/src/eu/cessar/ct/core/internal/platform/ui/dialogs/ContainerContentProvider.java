/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 25, 2010 1:46:03 PM </copyright>
 */
package eu.cessar.ct.core.internal.platform.ui.dialogs;

/**
 * @author uidt2045
 * 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;

import eu.cessar.ct.core.platform.ui.viewers.AbstractTreeContentProvider;

/**
 * Provides content for a tree viewer that shows only containers.
 */
public class ContainerContentProvider extends AbstractTreeContentProvider
{
	private boolean showClosedProjects = true;

	/**
	 * Creates a new ContainerContentProvider.
	 */
	public ContainerContentProvider()
	{
	}

	/*
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element)
	{
		if (element instanceof IWorkspace)
		{
			// check if closed projects should be shown
			IProject[] allProjects = ((IWorkspace) element).getRoot().getProjects();
			if (showClosedProjects)
			{
				return allProjects;
			}

			ArrayList<IProject> accessibleProjects = new ArrayList<IProject>();
			for (int i = 0; i < allProjects.length; i++)
			{
				if (allProjects[i].isOpen())
				{
					accessibleProjects.add(allProjects[i]);
				}
			}
			return accessibleProjects.toArray();
		}
		else if (element instanceof IContainer)
		{
			IContainer container = (IContainer) element;
			if (container.isAccessible())
			{
				try
				{
					List<IResource> children = new ArrayList<IResource>();
					IResource[] members = container.members();
					for (int i = 0; i < members.length; i++)
					{
						if (members[i].getType() != IResource.FILE)
						{
							children.add(members[i]);
						}
					}
					return children.toArray();
				}
				catch (CoreException e)
				{
					// this should never happen because we call #isAccessible
					// before invoking #members
				}
			}
		}
		return new Object[0];
	}

	/*
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object element)
	{
		return getChildren(element);
	}

	/*
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element)
	{
		if (element instanceof IResource)
		{
			return ((IResource) element).getParent();
		}
		return null;
	}

	/*
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element)
	{
		return getChildren(element).length > 0;
	}

	/**
	 * Specify whether or not to show closed projects in the tree viewer.
	 * Default is to show closed projects.
	 * 
	 * @param show
	 *        boolean if false, do not show closed projects in the tree
	 */
	public void showClosedProjects(boolean show)
	{
		showClosedProjects = show;
	}

}