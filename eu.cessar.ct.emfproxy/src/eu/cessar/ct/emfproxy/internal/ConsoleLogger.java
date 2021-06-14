/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 6:21:59 PM </copyright>
 */
package eu.cessar.ct.emfproxy.internal;

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyLogger;

/**
 * @author uidl6458
 * 
 */
public class ConsoleLogger implements IEMFProxyLogger
{

	public static final String[] LOG_STRINGS = {"INFO", "WARNING", "ERROR"};

	public void logMultiplicityWarning(EStructuralFeature feature, int found)
	{
		logWarning("multiplicity: " + feature);
	}

	public void logWarning(String message, Object... args)
	{
		message = MessageFormat.format(message, args);
		log(LOG_WARNING, message);
	}

	public void log(int type, String message)
	{
		// System.err.println(LOG_STRINGS[type] + ":" + message);
	}

}
