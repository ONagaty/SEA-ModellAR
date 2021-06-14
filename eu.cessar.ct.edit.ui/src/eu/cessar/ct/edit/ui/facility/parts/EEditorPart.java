/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 02.10.2012 13:17:20
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * Enumeration for an {@link IModelFragmentEditor}' parts
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct  5 11:18:28 2012 %
 * 
 *         %version: 1 %
 */
public enum EEditorPart
{
	/**
	 * Literal object for the part representing the editor's label
	 */
	CAPTION("caption"), //$NON-NLS-1$
	/**
	 * Literal object for the part that gives validation feedback
	 */
	VALIDATION("validation"), //$NON-NLS-1$
	/**
	 * Literal object for the part that holds the actions specific to a particular editor
	 */
	ACTION("action"), //$NON-NLS-1$
	/**
	 * Literal object for the editing area
	 */
	EDITING_AREA("editing area"), //$NON-NLS-1$
	/**
	 * Literal object for the part indicating the resource(s) where the value(s) presented by the editor are stored
	 */
	RESOURCES("resources"); //$NON-NLS-1$

	private final String name;

	/**
	 * @param name
	 */
	private EEditorPart(String name)
	{
		this.name = name;
	}

	private static final EEditorPart[] VALUES_ARRAY = new EEditorPart[] {CAPTION, VALIDATION, ACTION, EDITING_AREA,
		RESOURCES};

	/**
	 * A public read-only list of all the '<em><b>EEditorPart</b></em>' enumerators.
	 */
	public static final List<EEditorPart> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the <em><b>EEditorPart</b></em> literal with the specified name.
	 * 
	 * @param name
	 *        the name of the literal
	 * @return the <em><b>EEditorPart</b></em> literal with the specified name
	 */
	public static EEditorPart getLiteral(String name)
	{
		EEditorPart result = null;
		for (int i = 0; i < VALUES_ARRAY.length; ++i)
		{
			EEditorPart part = VALUES_ARRAY[i];
			if (part.getName().equals(name))
			{
				result = part;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns the name of the current literal
	 * 
	 * @return the name of the literal
	 */
	public String getName()
	{
		return name;
	}

}
