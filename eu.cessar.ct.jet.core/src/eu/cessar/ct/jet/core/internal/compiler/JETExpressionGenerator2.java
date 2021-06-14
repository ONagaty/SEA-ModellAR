/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4098<br/>
 * 02.06.2014 12:13:44
 *
 * </copyright>
 */
package eu.cessar.ct.jet.core.internal.compiler;


/**
 * Class replace the default EMF JETExpressionGenerator. Added for supporting output formatting of generated code.
 * Generator to deal with JSP expressions: <%= ... %> stuff.
 *
 * @author uidg4098
 *
 *         %created_by: uidt5908 %
 *
 *         %date_created: Wed Jun 4 13:34:47 2014 %
 *
 *         %version: 1 %
 */
public class JETExpressionGenerator2 extends JETScriptletGenerator2
{
	/**
	 * Hides the default EMF JET prefix for expression generation.
	 */
	protected static final String FUNCTION_CALL_BEGIN = "stringFormatter.append(\"\" + (";
	/**
	 * Hides the default EMF JET suffix for expression generation.
	 */
	protected static final String FUNCTION_CALL_END = "));"; //$NON-NLS-1$

	/**
	 * @param chars
	 *        content of the expression.
	 */
	public JETExpressionGenerator2(char[] chars)
	{
		super(chars);
	}

	@Override
	public String generate()
	{
		StringBuffer stringBuffer = new StringBuffer(FUNCTION_CALL_BEGIN);
		stringBuffer.append(super.generate());
		stringBuffer.append(FUNCTION_CALL_END);

		return stringBuffer.toString();
	}
}
