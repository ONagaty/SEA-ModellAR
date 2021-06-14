/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4098<br/>
 * 02.06.2014 12:18:30
 *
 * </copyright>
 */
package eu.cessar.ct.jet.core.internal.compiler;

import org.eclipse.emf.codegen.jet.JETCharDataGenerator;

/**
 * Class extending the default EMF JETCharDataGenerator. Added for supporting output formatting of generated code.
 *
 * @author uidg4098
 *
 *         %created_by: uidt5908 %
 *
 *         %date_created: Wed Jun 4 13:34:40 2014 %
 *
 *         %version: 1 %
 */

@SuppressWarnings("hiding")
public class JETCharDataGenerator2 extends JETCharDataGenerator
{
	/**
	 * Hides the default EMF JET prefix for String data generation from the template.
	 */
	protected static final String FUNCTION_CALL_BEGIN = "stringFormatter.append(\"\" + (";
	/**
	 * Hides the default EMF JET suffix for String data generation from the template.
	 */
	protected static final String FUNCTION_CALL_END = "));"; //$NON-NLS-1$

	/**
	 * @param characters
	 */
	public JETCharDataGenerator2(char[] characters)
	{
		super(characters);
	}

	@Override
	public String generate()
	{
		StringBuffer stringBuffer = new StringBuffer(FUNCTION_CALL_BEGIN);
		stringBuffer.append(generateCharData());
		stringBuffer.append(FUNCTION_CALL_END);

		return stringBuffer.toString();
	}
}
