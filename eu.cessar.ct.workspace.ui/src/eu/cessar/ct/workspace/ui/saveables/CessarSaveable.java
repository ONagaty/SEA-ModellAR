/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 5, 2011 9:47:36 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.saveables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sphinx.emf.saving.SaveIndicatorUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.workspace.saving.ModelSaveManager;
import org.eclipse.ui.Saveable;

/**
 * A unit of saveability at {@link Resource} level
 * 
 * @author uidl6870
 * 
 */
public class CessarSaveable extends Saveable
{
	private Resource resource;

	private List<Resource.Diagnostic> lastErrors;

	private List<Resource.Diagnostic> lastWarnings;

	/**
	 * Construct a new
	 * 
	 * @param resource
	 */
	public CessarSaveable(Resource resource)
	{
		this.resource = resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.Saveable#getName()
	 */
	@Override
	public String getName()
	{
		return getIFile().getFullPath().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.Saveable#getToolTipText()
	 */
	@Override
	public String getToolTipText()
	{
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.Saveable#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.Saveable#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) throws CoreException
	{
		try
		{
			ModelSaveManager.INSTANCE.saveModel(resource, false, monitor);
		}
		finally
		{
			lastErrors = new ArrayList<Resource.Diagnostic>(resource.getErrors());
			lastWarnings = new ArrayList<Resource.Diagnostic>(resource.getWarnings());
		}
	}

	/**
	 * Return the errors found within the resource after the save has been performed
	 * 
	 * @return the list with errors, never null
	 */
	public List<Resource.Diagnostic> getLastSavingErrors()
	{
		if (lastErrors == null)
		{
			return Collections.emptyList();
		}
		else
		{
			return Collections.unmodifiableList(lastErrors);
		}
	}

	/**
	 * Return the warnings found within the resource after the save has been performed
	 * 
	 * @return the list with warnings, never null
	 */
	public List<Resource.Diagnostic> getLastSavingWarnings()
	{
		if (lastWarnings == null)
		{
			return Collections.emptyList();
		}
		else
		{
			return Collections.unmodifiableList(lastWarnings);
		}
	}

	/**
	 * Clear the last errors and warnings
	 */
	public void clearErrorsAndWarnings()
	{
		lastErrors = null;
		lastWarnings = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.Saveable#isDirty()
	 */
	@Override
	public boolean isDirty()
	{
		TransactionalEditingDomain editingDomain = WorkspaceEditingDomainUtil.getEditingDomain(resource);
		boolean isDirty = SaveIndicatorUtil.isDirty(editingDomain, resource);
		return isDirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.Saveable#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof CessarSaveable)
		{
			return getName().equals(((CessarSaveable) object).getName());
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.Saveable#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return resource.hashCode();
	}

	/**
	 * 
	 * @return the resource that this saveable is responsible for
	 */
	public Resource getResource()
	{
		return resource;
	}

	/**
	 * 
	 * @return the file behind the resource
	 */
	public IFile getIFile()
	{
		IFile file = EcorePlatformUtil.getFile(resource);
		return file;
	}

}
