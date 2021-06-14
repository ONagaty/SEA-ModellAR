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

import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Predicate for the definition of an object.
 *
 * @author uidj9791
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class DefinitionPredicate extends AbstractPredicate
{

	/**
	 * @param pattern
	 */
	public DefinitionPredicate(String pattern)
	{
		super(pattern);
	}

	/**
	 * Constructs a {@link DefinitionShortNamePredicate}.
	 *
	 * @param pattern
	 *        the pattern to match the definition
	 * @return definitionPredicate the predicate for the definition that contains the specific pattern
	 */
	public static DefinitionPredicate createDefinitionPredicate(String pattern)
	{
		return new DefinitionPredicate(pattern);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.editor.ui.armodel.search.AbstractPredicate#getContent(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public String getContent(EObject object)
	{
		String content = null;
		GReferrable definition = null;

		if (object instanceof GContainer)
		{
			definition = ((GContainer) object).gGetDefinition();
		}
		else if (object instanceof GModuleConfiguration)
		{
			definition = ((GModuleConfiguration) object).gGetDefinition();
		}
		else if (object instanceof GConfigReferenceValue)
		{
			definition = ((GConfigReferenceValue) object).gGetDefinition();
		}
		if (object instanceof GParameterValue)
		{
			definition = ((GParameterValue) object).gGetDefinition();
		}

		if (definition != null)
		{
			content = definition.gGetShortName();
		}

		return content;
	}

}
