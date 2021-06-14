/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 13, 2010 11:58:21 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pm;

import eu.cessar.req.Requirement;

/**
 * This interface shall be used instead of IPMElementInfo when adaptors need to be installed for compatibility mode
 */
@Requirement(
	reqID = "REQ_COMPAT#1")
public interface IPMCompatElementInfo<T> extends IPMElementInfo<T>
{
}
