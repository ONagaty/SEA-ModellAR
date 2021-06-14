/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Oct 14, 2010 11:12:43 AM </copyright>
 */
package eu.cessar.ct.validation;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.platform.concurrent.IAsyncWorkExecManager;
import eu.cessar.ct.validation.internal.ValidationManagerImpl;

/**
 * @author uidl6870
 *
 */
public interface IValidationManager extends IAsyncWorkExecManager<EObject, Diagnostic>
{
	/** the singleton */
	public static final IValidationManager INSTANCE = new ValidationManagerImpl();

}
