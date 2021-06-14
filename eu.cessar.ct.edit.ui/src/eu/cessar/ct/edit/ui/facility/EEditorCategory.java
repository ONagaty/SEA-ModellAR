/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Aug 31, 2011 3:30:28 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration for editor categories
 * 
 */
public enum EEditorCategory
{
	@SuppressWarnings("javadoc")
	MAIN("main"), //$NON-NLS-1$
	@SuppressWarnings("javadoc")
	OTHER("other"), //$NON-NLS-1$
	@SuppressWarnings("javadoc")
	HIDDEN("hidden"), //$NON-NLS-1$
	@SuppressWarnings("javadoc")
	REFERENCED("referenced"), //$NON-NLS-1$
	@SuppressWarnings("javadoc")
	CONFIGCLASS("configClass"), //$NON-NLS-1$
	@SuppressWarnings("javadoc")
	VALIDATION("validation"); //$NON-NLS-1$

	private static final EEditorCategory[] VALUES_ARRAY = new EEditorCategory[] {MAIN, OTHER, HIDDEN, REFERENCED,
		CONFIGCLASS, VALIDATION};
	/**
	 * 
	 */
	// CHECKSTYLE:OFF
	public static final List<EEditorCategory> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));
	// CHECKSTYLE:ON

	private final String categoryName;

	EEditorCategory(String category)
	{
		categoryName = category;
	}

	/**
	 * 
	 * @param name
	 * @return the <em><b>EditorCategoryEnum</b></em> literal with the specified name
	 */
	public static EEditorCategory getLiteral(String name)
	{
		for (int i = 0; i < VALUES_ARRAY.length; ++i)
		{
			EEditorCategory result = VALUES_ARRAY[i];
			if (result.getName().equals(name))
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * @return category
	 */
	public String getName()
	{
		return categoryName;
	}
}
