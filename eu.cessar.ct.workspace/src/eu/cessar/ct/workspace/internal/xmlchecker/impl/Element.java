/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 06.08.2012 14:05:19 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import org.xml.sax.Attributes;

import eu.cessar.ct.workspace.internal.xmlchecker.IElement;

/**
 * Implementation of an {@link IElement}
 * 
 * @author uidl6870
 * 
 */
public class Element implements IElement
{
	private String value;
	private String attrName;
	private String name;
	private final Attributes attributes;

	/**
	 * @param attributes
	 */
	public Element(Attributes attributes)
	{
		this.attributes = attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IElement#getName()
	 */
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IElement#setName(java.lang.String)
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IElement#getAttributeValue()
	 */
	public String getAttributeValue()
	{
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IElement#setAttributeValue(java.lang.String)
	 */
	public void setAttributeValue(String val)
	{
		value = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IElement#getAttributeName()
	 */
	public String getAttributeName()
	{
		return attrName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IElement#setAttributeName(java.lang.String)
	 */
	public void setAttributeName(String attributeName)
	{
		attrName = attributeName;
	}

	public Attributes getAttributes()
	{
		return attributes;
	}

}
