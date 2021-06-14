/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 May 24, 2012 5:14:40 PM </copyright>
 */
package eu.cessar.ct.jet.core.internal.compiler;

import org.eclipse.emf.codegen.jet.JETException;

/**
 * @author uidu0944
 * 
 */
public interface CessarJETCoreElement
{

	/**
	 * Return true if the input contained the sequence that matched the action
	 * corresponding to this core tag.
	 */
	boolean accept(CessarJETParseEventListener listener, CessarJETReader reader,
		CessarJETParser parser) throws JETException;

}
