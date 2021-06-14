/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Aug 20, 2010 2:01:53 PM </copyright>
 */
package eu.cessar.ct.compat.internal.model;

import org.artop.aal.common.resource.AutosarURIFactory;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class CompatModelUtils
{
	public static final String KEY_CACHE_MERGED_MODEL = "merged.model"; //$NON-NLS-1$

	private CompatModelUtils()
	{
		// do nothing
	}

	/**
	 * Return the last segment for the fragment
	 * 
	 * @param fragment
	 * @return
	 */
	public static String getTrailingAbsoluteQualifiedNameSegment(String fragment)
	{
		return AutosarURIFactory.getTrailingAbsoluteQualifiedNameSegment(fragment);
	}
}
