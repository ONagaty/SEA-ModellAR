/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4098<br/>
 * 09.11.2015 16:28:18
 *
 * </copyright>
 */
package eu.cessar.ct.jet.core.internal.compiler;

import org.eclipse.emf.codegen.jet.JETScriptletGenerator;

import eu.cessar.ct.jet.core.JETCoreUtils;

/**
 * TODO: Please comment this class
 *
 * @author uidg4098
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class JETScriptletGenerator2 extends JETScriptletGenerator
{

	/**
	 * @param chars
	 */
	public JETScriptletGenerator2(char[] chars)
	{
		super(chars);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.codegen.jet.JETScriptletGenerator#generate()
	 */
	@Override
	public String generate()
	{
		return JETCoreUtils.convertLineFeed(super.generate());
	}
}
