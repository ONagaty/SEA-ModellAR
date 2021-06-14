/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 23.07.2012 16:41:38 </copyright>
 */
package eu.cessar.ct.core.platform.ui.filetypesViewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IContentProvider;

/**
 * 
 * Provider for the checked and grayed state of the
 * {@link FileTypesResourceViewer} with check-box style
 * 
 * @author uidl6870
 * 
 */
public class CheckStateProvider implements ICheckStateProvider
{
	private CheckboxTreeViewer checkboxTreeViewer;

	private List<IFile> checkedFiles = new ArrayList<IFile>();

	public CheckStateProvider(CheckboxTreeViewer viewer, List<IFile> initiallyCheckedFiles)
	{
		this.checkboxTreeViewer = viewer;
		checkedFiles.addAll(initiallyCheckedFiles);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICheckStateProvider#isChecked(java.lang.Object)
	 */
	public boolean isChecked(Object element)
	{
		if (element instanceof IFile)
		{
			return checkedFiles.contains(element);
		}

		IContentProvider contentProvider = checkboxTreeViewer.getContentProvider();

		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICheckStateProvider#isGrayed(java.lang.Object)
	 */
	public boolean isGrayed(Object element)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
