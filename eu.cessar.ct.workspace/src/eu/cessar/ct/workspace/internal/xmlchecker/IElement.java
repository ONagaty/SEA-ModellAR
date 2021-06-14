/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 06.08.2012 13:58:32 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker;

import org.xml.sax.Attributes;

/**
 * Data about a parsed XML element as it is being inspected, such as: name, its attributes, the last inspected attribute
 * 
 * @author uidl6870
 * 
 */
public interface IElement
{
	/**
	 * Return then name of the element
	 * 
	 * @return the element name
	 */
	public String getName();

	/**
	 * Sets the name of the element
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Returns the XML attributes of the element
	 * 
	 * @return element's {@link Attributes}
	 */
	public Attributes getAttributes();

	/**
	 * Returns the name of the attribute on which an inconsistency has been found, if the case.
	 * 
	 * @return attribute on which an inconsistency was found or <code>null</code>
	 */
	public String getAttributeName();

	/**
	 * Stores the name of the attribute for which a problem was found.
	 * 
	 * @param attrName
	 */
	public void setAttributeName(String attrName);

	/**
	 * 
	 * @return the value of the attribute on which an inconsistency has been found or <code>null</code>
	 */
	public String getAttributeValue();

	/**
	 * Stores the value of the attribute on which an inconsistency has been found.
	 * 
	 * @param value
	 */
	public void setAttributeValue(String value);

}
