/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Mar 19, 2014 3:48:55 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.utils;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * ImportExportUtils - API class for encapsulation of import/export methods of various formats.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 16:16:33 2014 %
 * 
 *         %version: 1 %
 */
public final class ImportExportUtils
{

	private ImportExportUtils()
	{

	}

	/**
	 * Export table content to clipboard.
	 * 
	 * @param table
	 *        the table
	 * @return the formatted string
	 */
	public static String exportTableContentToClipboard(Table table)
	{
		String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
		StringBuilder clipboardContent = new StringBuilder();
		String tableCellSeparator = "\t"; //$NON-NLS-1$

		if (table != null)
		{
			if (table.getColumnCount() > 0)
			{
				for (int i = 0; i < table.getColumnCount(); i++)
				{
					clipboardContent.append(table.getColumn(i).getText());

					if (i < table.getColumnCount() - 1)
					{
						clipboardContent.append(tableCellSeparator);
					}
				}
				clipboardContent.append(newLine);

				for (int i = 0; i < table.getItemCount(); i++)
				{
					TableItem item = table.getItem(i);

					for (int colIndex = 0; colIndex < table.getColumnCount(); colIndex++)
					{
						clipboardContent.append(item.getText(colIndex));
						clipboardContent.append(tableCellSeparator);
					}
					clipboardContent.append(newLine);
				}
			}
		}
		return clipboardContent.toString();
	}

}
