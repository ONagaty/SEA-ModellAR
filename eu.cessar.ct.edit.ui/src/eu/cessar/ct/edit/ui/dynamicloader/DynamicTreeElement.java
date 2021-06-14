/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4449<br/>
 * Jul 31, 2015 2:40:24 PM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.dynamicloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic tree element, should be extened to support listeneres
 *
 * @author uidg4449
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class DynamicTreeElement
{
	Object wraped;
	Object parent;
	private boolean hasChildren;
	private String imageStr;

	/**
	 * @param wraped
	 * @param parent
	 */
	public DynamicTreeElement(Object wraped, Object parent)
	{
		this.wraped = wraped;
		this.parent = parent;
	}

	/**
	 * @return The real element
	 */
	public Object getWrappedObject()
	{
		return wraped;
	}

	/**
	 * @return return null if no parent else returns the parent
	 */
	public Object getParent()
	{
		if (parent == null)
		{
			return null;
		}
		return parent;
	}

	/**
	 * @return hasChildren
	 */
	public boolean hasChildren()
	{
		return hasChildren;
	}

	/**
	 * @param hsChildren
	 */
	public void setHasChildren(boolean hsChildren)
	{
		hasChildren = hsChildren;
	}

	/**
	 * @param o
	 * @param parent
	 * @param imgStr
	 * @return wraps multiple items
	 */
	public static List<DynamicTreeElement> getAllWrapped(List<Object> o, Object parent, String imgStr)
	{
		ArrayList<DynamicTreeElement> arrayList = new ArrayList<>();
		for (Object toWrap: o)
		{
			DynamicTreeElement e = new DynamicTreeElement(toWrap, parent);
			e.setImage(imgStr);
			arrayList.add(e);
		}
		return arrayList;
	}

	/**
	 * Unwrapts the wraped element or returns the original one
	 *
	 * @param toUnwrap
	 * @return obj
	 */
	public static Object unWrap(Object toUnwrap)
	{
		if (toUnwrap instanceof DynamicTreeElement)
		{
			return ((DynamicTreeElement) toUnwrap).getWrappedObject();
		}
		return toUnwrap;
	}

	/**
	 * @return Image
	 */
	public String getImage()
	{
		return imageStr;
	}

	/**
	 * @param img
	 */
	public void setImage(String img)
	{
		imageStr = img;
	}

}
