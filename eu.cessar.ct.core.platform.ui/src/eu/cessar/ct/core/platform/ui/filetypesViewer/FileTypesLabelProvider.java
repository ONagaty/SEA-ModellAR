/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 1, 2011 1:50:23 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.filetypesViewer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sphinx.emf.saving.SaveIndicatorUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

import eu.cessar.ct.core.platform.ui.viewers.AbstractTableLabelProvider;
import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * Label provider for the file type viewer
 * 
 * @author uidl6870
 * 
 */
public class FileTypesLabelProvider extends AbstractTableLabelProvider
{
	private boolean dirtyFeedback;

	public FileTypesLabelProvider(boolean dirtyFeedback)
	{
		this.dirtyFeedback = dirtyFeedback;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex)
	{

		if (element instanceof IResource)
		{
			IWorkbenchAdapter adapter = (IWorkbenchAdapter) Platform.getAdapterManager().getAdapter(
				element, IWorkbenchAdapter.class.getName());

			if (adapter != null)
			{
				ImageDescriptor imageDescriptor = adapter.getImageDescriptor(element);
				if (imageDescriptor != null)
				{
					Image image = imageDescriptor.createImage();

					Image decoratedImage = PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator().decorateImage(
						image, element);

					return decoratedImage;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex)
	{
		if (!dirtyFeedback)
		{
			// no dirty feedback needed, simply return the name of the resource
			return ((IResource) element).getName();
		}
		else
		{
			if (element instanceof IFile)
			{
				if (isDirty((IFile) element))
				{
					return "*" + ((IResource) element).getName(); //$NON-NLS-1$
				}
				return ((IResource) element).getName();
			}
			else
			{
				boolean containsDirtyResource = containsDirtyResource((IContainer) element);
				return containsDirtyResource ? "*" + ((IResource) element).getName() : ((IResource) element).getName(); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @param element
	 */
	private boolean containsDirtyResource(IContainer container)
	{
		try
		{
			IResource[] members = container.members();

			for (IResource iResource: members)
			{
				if (iResource instanceof IFile)
				{
					boolean dirty = isDirty((IFile) iResource);
					if (dirty)
					{
						return true;
					}
				}
				else
				{
					boolean containsDirtyResource = containsDirtyResource((IContainer) iResource);
					if (containsDirtyResource)
					{
						return true;
					}
				}
			}
		}
		catch (CoreException e)
		{
			return false;
		}
		return false;
	}

	private boolean isDirty(IFile file)
	{
		Resource definedResource = ModelUtils.getDefinedResource(file);
		TransactionalEditingDomain editingDomain = WorkspaceEditingDomainUtil.getEditingDomain(definedResource);
		return SaveIndicatorUtil.isDirty(editingDomain, definedResource);
	}
}
