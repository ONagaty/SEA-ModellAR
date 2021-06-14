/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 19.09.2012 12:36:07 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.xmlchecker;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * Label Provider for the table viewer showing the files for which
 * inconsistencies have been identified and their corresponding schema locations
 * 
 * @author uidl6870
 * 
 */
public class FilesWithInconsistenciesLabelProvider extends LabelProvider implements
	ITableLabelProvider
{

	private final Map<File, List<IXMLCheckerInconsistency>> fileToInconsistenciesMap;

	public FilesWithInconsistenciesLabelProvider(Map<File, List<IXMLCheckerInconsistency>> map)
	{
		this.fileToInconsistenciesMap = map;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex)
	{
		File file = (File) element;

		String text = ""; //$NON-NLS-1$
		switch (columnIndex)
		{
			case 0:
				IFile iFile = ResourceUtils.getIFile(file);
				text = iFile.getFullPath().toString();
				break;

			case 1:
				List<IXMLCheckerInconsistency> list = fileToInconsistenciesMap.get(file);
				IXMLCheckerInconsistency iInconsistency = list.get(0);
				text = iInconsistency.getDetailFromOriginalFile().getSchemaLocation();
				break;
			default:
		}

		return text;
	}

}
