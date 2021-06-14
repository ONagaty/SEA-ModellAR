/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Mar 24, 2014 2:06:04 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.emf;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain.EditingDomainProvider;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Identical with {@link EditingDomainProvider}, except the declared method starts with 'e' instead of 'get'.
 * 
 */
public interface IAltEditingDomainProvider
{

	@SuppressWarnings("javadoc")
	public EditingDomain eGetEditingDomain();
}
