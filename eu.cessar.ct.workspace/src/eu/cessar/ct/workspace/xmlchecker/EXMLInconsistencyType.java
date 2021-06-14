/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 03.08.2012 11:05:09 </copyright>
 */
package eu.cessar.ct.workspace.xmlchecker;

/**
 * Enumeration for XML loading/saving inconsistency types
 * 
 * @author uidl6870
 * 
 */
public enum EXMLInconsistencyType
{
	/**
	 * 
	 */
	ELEMENT("Element"), //$NON-NLS-1$
	/**
	 * 
	 */
	ELEMENT_TEXT("Element text"), //$NON-NLS-1$
	/**
	 * 
	 */
	ATTRIBUTE_VALUE("Attribute"); //$NON-NLS-1$ 

	private final String inconsistencyType;

	EXMLInconsistencyType(String type)
	{
		inconsistencyType = type;
	}

	/**
	 * @return inconsistency type
	 */
	public String getName()
	{
		return inconsistencyType;
	}

}
