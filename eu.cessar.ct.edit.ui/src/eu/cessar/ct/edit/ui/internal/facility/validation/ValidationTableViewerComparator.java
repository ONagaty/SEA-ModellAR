/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidv3687<br/>
 * Feb 22, 2013 9:49:56 AM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.validation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.sphinx.emf.validation.ui.views.ConcreteMarker;
import org.eclipse.sphinx.emf.validation.ui.views.MarkerList;

import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * Sorting comparator used by ValidationEditorPart TableViewer
 *
 * @author uidv3687
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Sep 17 14:46:16 2014 %
 *
 *         %version: 1 %
 */
public class ValidationTableViewerComparator extends ViewerComparator
{
	private EValidationTableColumn column;
	private boolean ascending;

	/**
	 * constructor
	 */
	public ValidationTableViewerComparator()
	{
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2)
	{
		// If none set exit
		if (column == null)
		{
			return 0;
		}

		ConcreteMarker marker1 = null;
		ConcreteMarker marker2 = null;
		try
		{
			marker1 = MarkerList.createMarker((IMarker) e1);
			marker2 = MarkerList.createMarker((IMarker) e2);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		int rc = 0;

		String s1 = column.getText(marker1);
		String s2 = column.getText(marker2);
		rc = s1.compareTo(s2);

		// If descending order, flip the direction
		if (!ascending)
		{
			rc = -rc;
		}
		return rc;
	}

	/**
	 * @return sorting direction
	 */
	public boolean isAscending()
	{
		return ascending;
	}

	/**
	 * @param column
	 *        Sets the column to be sorted
	 * @param ascending
	 *        Sets the order to sort the column ture for ascending, false for descending
	 */
	public void setSortOrder(EValidationTableColumn column, boolean ascending)
	{
		// New column to be sorted after
		this.column = column;
		// Sort order
		this.ascending = ascending;
	}
}
