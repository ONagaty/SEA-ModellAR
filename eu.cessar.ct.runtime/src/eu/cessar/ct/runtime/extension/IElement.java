/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 10:38:28 AM </copyright>
 */
package eu.cessar.ct.runtime.extension;

/**
 * @author uidl6870
 * 
 */
public interface IElement
{
	/**
	 * Get the name of the element
	 * 
	 * @return the name of the element
	 */
	String getName();

	/**
	 * Get the name of the attributes of this element. If the element has no
	 * attributes, return an empty array
	 * 
	 * @return
	 */
	String[] getAttributesName();

	/**
	 * Get the value of the attribute named <code>attributeName</code>
	 * 
	 * @param attributeName
	 *        the name of the attribute
	 * @return attribute value or <code>null</code> if none
	 */
	String getAttribute(String attributeName);

	/**
	 * Get all the elements that are children of this element.If the element has
	 * no children, an empty array is returned
	 * 
	 * @return child elements
	 */
	IElement[] getChildren();
}
