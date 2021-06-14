/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidv3687<br/>
 * Feb 20, 2013 5:08:27 PM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.validation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.sphinx.emf.validation.ui.views.ConcreteMarker;
import org.eclipse.sphinx.emf.validation.ui.views.MarkerList;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * Label provider for Properties view's validation tab
 *
 * @author uidv3687
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Sep 17 14:46:15 2014 %
 *
 *         %version: 1 %
 */
public class ValidationMarkerLabelProvider extends LabelProvider implements ITableLabelProvider
{

	private EValidationTableColumn[] columnName;

	/**
	 * @param columnName
	 */
	public ValidationMarkerLabelProvider(EValidationTableColumn[] columnName)
	{
		super();
		this.columnName = columnName;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object obj, int columnIndex)
	{
		ConcreteMarker marker = null;
		try
		{
			marker = MarkerList.createMarker((IMarker) obj);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return columnName[columnIndex].getIcon(marker);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex)
	{
		String columnText = ""; //$NON-NLS-1$

		ConcreteMarker marker = null;
		try
		{
			marker = MarkerList.createMarker((IMarker) element);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		columnText = columnName[columnIndex].getText(marker);

		if (columnText == null)
		{
			columnText = ""; //$NON-NLS-1$
		}

		return columnText;
	}

}
