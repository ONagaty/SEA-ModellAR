/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 6:21:43 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author uidl6458
 * 
 */
public interface IEMFProxyLogger
{

	public static final int LOG_INFO = 0;

	public static final int LOG_WARNING = 1;

	public static final int LOG_ERROR = 2;

	public void logMultiplicityWarning(EStructuralFeature feature, int found);

}
