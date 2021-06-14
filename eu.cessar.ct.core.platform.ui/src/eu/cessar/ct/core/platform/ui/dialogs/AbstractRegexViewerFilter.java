/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu4748<br/>
 * 23.06.2015 15:39:23
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.dialogs;

import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ViewerFilter;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 * A generic implementation for a RegEx based {@linkplain ViewerFilter}. Supports case sensitivity.
 *
 * @author uidu4748
 *
 *         %created_by: uidu4748 %
 *
 *         %date_created: Thu Jul  2 10:20:42 2015 %
 *
 *         %version: 1 %
 */
public abstract class AbstractRegexViewerFilter extends ViewerFilter
{
	private String searchString = ""; //$NON-NLS-1$
	private boolean isCaseSensitive;

	/**
	 *
	 * @return the String for which is currently searched.
	 */
	public String getSearchString()
	{
		return searchString;
	}

	/**
	 * Sets the string that will be searched.
	 *
	 * @param searchString
	 *        the searchString to set.
	 */
	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}

	/**
	 * @return whether the search is case sensitive.
	 */
	public boolean isCaseSensitive()
	{
		return isCaseSensitive;
	}

	/**
	 * Case sensitive toggle
	 *
	 * @param value
	 *        - the new value.
	 */
	public void setCaseSensitive(boolean value)
	{
		isCaseSensitive = value;
	}

	/**
	 * Check if a text matches the given expression.
	 *
	 * @param text
	 * @param match
	 * @return whether the text matched under the given circumstances (case sensitivity, search criteria).
	 */
	protected boolean matchText(String text, String match)
	{
		String filter = StringUtils.escapeRegexForSearch(match);
		Pattern pattern = Pattern.compile(filter);
		String textSearch = text;

		if (!isCaseSensitive)
		{
			textSearch = text.toLowerCase();
			pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
		}

		return pattern.matcher(textSearch).lookingAt();
	}
}
