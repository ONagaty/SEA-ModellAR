/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762 Mon Feb 24 15:21:19 2014 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.consistencychecker;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.req.Requirement;

/**
 * ProjectConsistencyReportTableViewerComparator is a comparator for the identified project inconsistencies displayed in
 * the table. {@link ProjectConsistencyReportPage}
 * 
 * @author uidw8762
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 13:40:54 2014 %
 * 
 *         %version: 3 %
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#6")
public class ProjectConsistencyReportTableViewerComparator extends ViewerComparator
{
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	/**
	 * Instantiates a new project consistency report table viewer comparator.
	 */
	public ProjectConsistencyReportTableViewerComparator()
	{
		propertyIndex = 0;
		direction = DESCENDING;
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
		IProjectCheckInconsistency i1 = (IProjectCheckInconsistency) e1;
		IProjectCheckInconsistency i2 = (IProjectCheckInconsistency) e2;
		int rc = 0;

		switch (propertyIndex)
		{
			case 0:
				rc = i1.getSeverity().getName().compareTo(i2.getSeverity().getName());
				break;
			case 1:
				rc = i1.getInconsistencyType().getName().compareTo(i2.getInconsistencyType().getName());
				break;
			case 2:
				rc = i1.getMessage().compareTo(i2.getMessage());
				break;
			case 3:
				rc = i1.getFiles().get(0).getFullPath().toString().compareTo(
					i2.getFiles().get(0).getFullPath().toString());
				break;
			default:
				rc = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING)
		{
			rc = -rc;
		}
		return rc;
	}

	/**
	 * Gets the direction.
	 * 
	 * @return the direction
	 */
	public int getDirection()
	{
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	/**
	 * Sets the column.
	 * 
	 * @param column
	 *        the new column
	 */
	public void setColumn(int column)
	{
		if (column == propertyIndex)
		{
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		}
		else
		{
			// New column; do an ascending sort
			propertyIndex = column;
			direction = DESCENDING;
		}
	}

}
