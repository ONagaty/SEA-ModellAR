/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 15, 2015 6:12:38 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.mms.arpredicates;

import org.eclipse.emf.ecore.EObject;

import com.continental_corporation.automotive.powertrain.artop.autosar.predicates.Predicate;

import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * Abstract class for a regex predicate.
 *
 * @author uidj9791
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public abstract class AbstractPredicate implements Predicate
{
	private static final String REGEX_VALUE_ALL = "(.*)"; //$NON-NLS-1$
	private static final String ATTRIBUTE_IDENTIFIER = "@"; //$NON-NLS-1$
	private String pattern;

	/**
	 * @param pattern
	 */
	public AbstractPredicate(String pattern)
	{
		this.pattern = pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.continental_corporation.automotive.powertrain.artop.autosar.predicates.Predicate#evaluate(org.eclipse.emf
	 * .ecore.EObject)
	 */
	@Override
	public boolean evaluate(EObject object)
	{
		String content = null;

		if (!(ModelUtils.getAbsoluteQualifiedName(object).contains(ATTRIBUTE_IDENTIFIER)))
		{
			content = getContent(object);
		}

		pattern = pattern.replaceAll("[^\\w\\s\\*]", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String patternString = REGEX_VALUE_ALL + pattern.replace("*", REGEX_VALUE_ALL) + REGEX_VALUE_ALL; //$NON-NLS-1$

		if ((content != null) && (content.toLowerCase().matches(patternString.toLowerCase())))
		{
			return true;
		}

		return false;
	}

	/**
	 * @param object
	 *        the object to match
	 * @return the content to be matched with the input pattern or <code>null</code> otherwise
	 */
	public abstract String getContent(EObject object);

}
