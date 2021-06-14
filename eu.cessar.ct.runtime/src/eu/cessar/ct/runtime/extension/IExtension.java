/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 10:38:19 AM </copyright>
 */
package eu.cessar.ct.runtime.extension;

/**
 * A particular extension
 * 
 */
public interface IExtension
{

	/**
	 * Get all elements declared by this extension. If the extension does not
	 * declare any elements, an empty array is returned.
	 * 
	 * @return
	 */
	IElement[] getElements();

}
