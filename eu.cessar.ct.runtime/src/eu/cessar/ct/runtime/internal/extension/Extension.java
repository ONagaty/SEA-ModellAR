/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 1:45:16 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.extension;

import java.util.ArrayList;
import java.util.List;

import eu.cessar.ct.runtime.extension.IElement;
import eu.cessar.ct.runtime.extension.IExtension;

/**
 * 
 * 
 */
public class Extension implements IExtension
{
	private List<IElement> elements = new ArrayList<IElement>();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.extension.IExtension#getElements()
	 */
	public IElement[] getElements()
	{
		return elements.toArray(new IElement[elements.size()]);
	}

	public void addElement(IElement element)
	{
		elements.add(element);
	}

}
