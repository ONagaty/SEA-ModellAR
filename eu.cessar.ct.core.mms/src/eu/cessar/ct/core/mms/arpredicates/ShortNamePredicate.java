/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Nov 24, 2015 9:34:28 AM
 *
 * </copyright>
 */
package eu.cessar.ct.core.mms.arpredicates;

import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import org.eclipse.emf.ecore.EObject;

/**
 * Predicate for the shortname of an object.
 *
 * @author uidj9791
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class ShortNamePredicate extends AbstractPredicate
{
	/**
	 * @param pattern
	 *
	 */
	public ShortNamePredicate(String pattern)
	{
		super(pattern);
	}

	/**
	 * Constructs a {@link ShortNamePredicate}.
	 *
	 * @param pattern
	 *        the pattern to match the shortName
	 * @return shortNamePredicate the predicate for the shortName that contains the specific pattern
	 */
	public static ShortNamePredicate createShortNamePredicate(final String pattern)
	{
		return new ShortNamePredicate(pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.editor.ui.armodel.search.AbstractPredicate#getContent()
	 */
	@Override
	public String getContent(EObject object)
	{
		String content = "";

		if (object instanceof GReferrable)
		{
			content = ((GReferrable) object).gGetShortName();
		}

		return content;
	}
}
