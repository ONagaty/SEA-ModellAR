/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Jan 9, 2015 11:17:44 AM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

/**
 * Utility class for displaying the description area.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Wed Jan 28 13:55:56 2015 %
 *
 *         %version: 4 %
 */
public final class CessarDescriptionUtils
{
	private static final String BOLD_START = "<b>"; //$NON-NLS-1$
	private static final String BOLD_END = "</b>"; //$NON-NLS-1$

	private CessarDescriptionUtils()
	{

	}

	/**
	 * Formats the style of given text.
	 *
	 * @param text
	 * @param styles
	 *
	 * @return text
	 */
	public static String formatStyle(String text, List<StyleRange> styles)
	{
		int pos = -1;
		int lastPos = 0;

		StringBuffer formatedText = new StringBuffer(text.length());

		while (lastPos < text.length())
		{
			pos = text.indexOf(BOLD_START, lastPos); // known BMP characters

			if (pos < 0)
			{
				break;
			}
			else
			{
				formatedText.append(text.substring(lastPos, pos));

				lastPos = pos + BOLD_START.length();

				pos = text.indexOf(BOLD_END, lastPos); // known BMP characters

				if (pos < 0)
				{
					// implied </b> at end of input
					pos = text.length();
				}

				styles.add(new StyleRange(formatedText.length(), pos - lastPos, null, null, SWT.BOLD));

				formatedText.append(text.substring(lastPos, pos));

				lastPos = Math.min(pos + BOLD_END.length(), text.length());
			}
		}

		formatedText.append(text.substring(lastPos, text.length()));

		return formatedText.toString();
	}
}
