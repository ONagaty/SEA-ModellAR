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

import org.eclipse.emf.ecore.EObject;

/**
 * Predicate for the type of an object.
 *
 * @author uidj9791
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class TypePredicate extends AbstractPredicate
{
	/**
	 * @param pattern
	 */
	public TypePredicate(final String pattern)
	{
		super(pattern);
	}

	/**
	 * Constructs a {@link TypePredicate}.
	 *
	 * @param pattern
	 *        the pattern to match the types
	 * @return typePredicate the predicate for the type that contains the specific pattern
	 */
	public static TypePredicate createTypePredicate(final String pattern)
	{
		return new TypePredicate(pattern);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.editor.ui.armodel.search.AbstractPredicate#getContent(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public String getContent(EObject object)
	{
		String content = object.eClass().getName();

		return content;
	}
}
