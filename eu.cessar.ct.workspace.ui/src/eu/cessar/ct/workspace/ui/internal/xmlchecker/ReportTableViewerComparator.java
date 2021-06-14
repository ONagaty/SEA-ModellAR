/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 22.08.2012 11:39:49 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.xmlchecker;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * Viewer comparator for the identified XML loading/saving inconsistencies
 * displayed in the table shown on {@link ReportPage}
 * 
 * @author uidl6870
 * 
 */
public class ReportTableViewerComparator extends ViewerComparator
{
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public ReportTableViewerComparator()
	{
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2)
	{
		IXMLCheckerInconsistency i1 = (IXMLCheckerInconsistency) e1;
		IXMLCheckerInconsistency i2 = (IXMLCheckerInconsistency) e2;
		int rc = 0;
		switch (propertyIndex)
		{
			case 0:
				rc = i1.getDetailFromOriginalFile().getFile().getName().compareTo(
					i2.getDetailFromOriginalFile().getFile().getName());
				break;
			case 1:
				rc = i1.getInconsistencyType().getName().compareTo(
					i2.getInconsistencyType().getName());
				break;
			case 2:
				rc = i1.getSeverity().getName().compareTo(i2.getSeverity().getName());
				break;
			case 3:
				rc = i1.getMessage().compareTo(i2.getMessage());
				break;
			case 4:
				rc = i1.getDetailFromOriginalFile().getItem().compareTo(
					i2.getDetailFromOriginalFile().getItem());
				break;
			case 5:
				rc = i1.getDetailFromGeneratedFile().getItem().compareTo(
					i2.getDetailFromGeneratedFile().getItem());
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

	public int getDirection()
	{
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column)
	{
		if (column == this.propertyIndex)
		{
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		}
		else
		{
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

}
