/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Mar 6, 2014 2:07:44 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.consistencychecker;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail;
import eu.cessar.ct.workspace.xmlchecker.ILocation;
import eu.cessar.req.Requirement;

/**
 * ProjectInconsistenciesReportLabelProvider
 * 
 * @author uidw8762
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Tue Jun 23 10:50:31 2015 %
 * 
 *         %version: RAUTOSAR~4 %
 */
@Requirement(
	reqID = "REQ_CHECK#6")
public class ProjectInconsistenciesReportLabelProvider extends LabelProvider implements ITableLabelProvider
{
	private static final String MISSING = "missing"; //$NON-NLS-1$

/*
 * (non-Javadoc)
 * 
 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
 */
	public String getColumnText(Object element, int columnIndex)
	{
		IProjectCheckInconsistency inconsistency = (IProjectCheckInconsistency) element;

		String text = ""; //$NON-NLS-1$
		switch (columnIndex)
		{
			case 0:
				text = inconsistency.getSeverity().getName();
				break;

			case 1:
				text = inconsistency.getInconsistencyType().getName();
				break;

			case 2:

				text = inconsistency.getMessage();
				break;
			case 3:
				text = ""; //$NON-NLS-1$
				StringBuilder mergedFileList = new StringBuilder();
				if (!inconsistency.getFiles().isEmpty())
				{
					for (IFile file: inconsistency.getFiles())
					{
						mergedFileList.append(file.getProjectRelativePath().toString());
						mergedFileList.append("; "); //$NON-NLS-1$
					}
					text = mergedFileList.toString().substring(0, mergedFileList.toString().length() - 2);
				}
				break;

			default:
		}
		return text;
	}

	/**
	 * Gets the text.
	 * 
	 * @param detail
	 *        the detail
	 * @return the text
	 */
	public static String getText(IInconsistencyDetail detail)
	{
		String value = detail.getItem();

		String text = ("".equals(value)) ? MISSING : value; //$NON-NLS-1$ 
		text += getLocationAsText(detail.getLocation());

		return text;
	}

	/**
	 * Gets the location as text.
	 * 
	 * @param location
	 *        the location
	 * @return the location as text
	 */
	private static String getLocationAsText(ILocation location)
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append(" (line: "); //$NON-NLS-1$
		buffer.append(location.getLineNumber());
		buffer.append(" ,column: "); //$NON-NLS-1$
		buffer.append(location.getColumnNumber());
		buffer.append(")"); //$NON-NLS-1$

		return buffer.toString();
	}

/*
 * (non-Javadoc)
 * 
 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
 */
	public Image getColumnImage(Object element, int columnIndex)
	{
		return null;
	}

}