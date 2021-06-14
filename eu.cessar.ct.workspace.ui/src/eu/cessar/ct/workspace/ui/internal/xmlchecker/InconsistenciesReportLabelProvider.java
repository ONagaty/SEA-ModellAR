/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 11.09.2012 08:57:02 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.xmlchecker;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;
import eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail;
import eu.cessar.ct.workspace.xmlchecker.ILocation;

/**
 * Label Provider for the table viewer showing the inconsistencies
 * 
 * @author uidl6870
 * 
 */
public class InconsistenciesReportLabelProvider extends LabelProvider implements
	ITableLabelProvider
{
	private static final String MISSING = "missing"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex)
	{
		IXMLCheckerInconsistency i = (IXMLCheckerInconsistency) element;

		String text = ""; //$NON-NLS-1$
		switch (columnIndex)
		{
			case 0:
				text = i.getDetailFromOriginalFile().getFile().getName();
				break;

			case 1:
				text = i.getInconsistencyType().getName();
				break;

			case 2:

				text = i.getSeverity().getName();
				break;
			case 3:

				text = i.getMessage();
				break;

			case 4:
				text = getText(i.getDetailFromOriginalFile());
				break;
			case 5:
				text = getText(i.getDetailFromGeneratedFile());
				break;
			default:
		}
		return text;
	}

	public String getText(IInconsistencyDetail detail)
	{
		String value = detail.getItem();

		String text = ("".equals(value)) ? MISSING : value; //$NON-NLS-1$ 
		text += getLocationAsText(detail.getLocation());

		return text;
	}

	private String getLocationAsText(ILocation location)
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append(" (line: "); //$NON-NLS-1$
		buffer.append(location.getLineNumber());
		buffer.append(" ,column: "); //$NON-NLS-1$
		buffer.append(location.getColumnNumber());
		buffer.append(")"); //$NON-NLS-1$

		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
