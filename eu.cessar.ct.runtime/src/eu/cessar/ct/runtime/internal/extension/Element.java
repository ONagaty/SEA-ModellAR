/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 1:12:42 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.cessar.ct.runtime.extension.IElement;

/**
 * @author uidl6870
 * 
 */
public class Element implements IElement
{
	private String name;

	private Map<String, String> attrMap = new HashMap<String, String>();

	private List<IElement> children = new ArrayList<IElement>();

	public Element(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.extension.IElement#getName()
	 */
	public String getName()
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.extension.IElement#getAttributesName()
	 */
	public String[] getAttributesName()
	{
		Set<String> keySet = attrMap.keySet();
		if (keySet != null)
		{

			return keySet.toArray(new String[keySet.size()]);
		}
		return new String[0];
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.extension.IElement#getAttribute(java.lang.String)
	 */
	public String getAttribute(String attributeName)
	{
		return attrMap.get(attributeName);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.extension.IElement#getChildren()
	 */
	public IElement[] getChildren()
	{
		return children.toArray(new IElement[children.size()]);
	}

	public void addAttribute(String name, String value)
	{

		attrMap.put(name, value);
	}

	public void addChild(IElement child)
	{
		children.add(child);
	}

}
