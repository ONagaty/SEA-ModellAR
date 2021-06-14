/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 May 25, 2012 9:03:32 AM </copyright>
 */
package eu.cessar.ct.jet.core.internal.compiler;

import java.util.Map;

import org.eclipse.emf.codegen.jet.JETException;

/**
 * The interface for the JET code generation back-end.
 */
public interface CessarJETParseEventListener
{
	void beginPageProcessing() throws JETException;

	void handleDirective(String directive, CessarJETMark start, CessarJETMark stop,
		Map<String, String> attributes) throws JETException;

	void handleExpression(CessarJETMark start, CessarJETMark stop, Map<String, String> attributes)
		throws JETException;

	void handleCharData(char[] chars) throws JETException;

	void endPageProcessing() throws JETException;

	void handleScriptlet(CessarJETMark start, CessarJETMark stop, Map<String, String> attributes)
		throws JETException;
}