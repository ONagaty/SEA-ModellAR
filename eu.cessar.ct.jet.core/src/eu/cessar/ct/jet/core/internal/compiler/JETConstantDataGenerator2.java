/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4098<br/>
 * 02.06.2014 12:46:45
 *
 * </copyright>
 */
package eu.cessar.ct.jet.core.internal.compiler;

import org.eclipse.emf.codegen.jet.JETConstantDataGenerator;

import eu.cessar.ct.jet.core.JETCoreUtils;

/**
 * Class extending the default EMF JETConstantDataGenerator. Added for supporting output formatting of generated code.
 *
 * @author uidg4098
 *
 *         %created_by: uidt5908 %
 *
 *         %date_created: Wed Jun 4 13:34:46 2014 %
 *
 *         %version: 1 %
 */

@SuppressWarnings("hiding")
public class JETConstantDataGenerator2 extends JETConstantDataGenerator
{
	/**
	 * Hides the default EMF JET prefix for String constant generation.
	 */
	protected static final String FUNCTION_CALL_BEGIN = "stringFormatter.append("; //$NON-NLS-1$
	/**
	 * Hides the default EMF JET suffix for String constant generation.
	 */
	protected static final String FUNCTION_CALL_END = ");"; //$NON-NLS-1$

	/**
	 * @param characters
	 * @param label
	 */
	public JETConstantDataGenerator2(char[] characters, String label)
	{
		super(characters, label);
	}

	@Override
	public String generate()
	{
		StringBuffer stringBuffer = new StringBuffer(FUNCTION_CALL_BEGIN);
		stringBuffer.append(label);
		stringBuffer.append(FUNCTION_CALL_END);
		return JETCoreUtils.convertLineFeed(stringBuffer.toString());
	}
}
