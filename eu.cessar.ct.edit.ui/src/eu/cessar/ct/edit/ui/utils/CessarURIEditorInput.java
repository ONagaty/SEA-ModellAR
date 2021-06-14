/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Sep 6, 2011 3:33:44 PM </copyright>
 */
package eu.cessar.ct.edit.ui.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IMemento;

/**
 * An URIEditorInput capable also to adapt to IProject
 *
 * @author uidl6458
 *
 */
public class CessarURIEditorInput extends URIEditorInput
{

	/**
	 * @param memento
	 */
	public CessarURIEditorInput(IMemento memento)
	{
		super(memento);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param uri
	 * @param name
	 */
	public CessarURIEditorInput(URI uri, String name)
	{
		super(uri, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param uri
	 */
	public CessarURIEditorInput(URI uri)
	{
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create a CessarURIEditorInput for a project
	 *
	 * @param project
	 */
	public CessarURIEditorInput(IProject project)
	{
		super(URI.createURI(project.getFullPath().toString()), project.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.ui.URIEditorInput#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter)
	{
		if (EMFPlugin.IS_RESOURCES_BUNDLE_AVAILABLE)
		{
			URI uri = getURI();
			if (adapter == IFile.class)
			{
				return super.getAdapter(adapter);
			}
			else if (adapter == IProject.class)
			{
				String rootRelativePath = uri.path();
				IResource member = ResourcesPlugin.getWorkspace().getRoot().findMember(rootRelativePath);
				if (member instanceof IProject)
				{
					return member;
				}
			}
		}
		return null;
	}

}
