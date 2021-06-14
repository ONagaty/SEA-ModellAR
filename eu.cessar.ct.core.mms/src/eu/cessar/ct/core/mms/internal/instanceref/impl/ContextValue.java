/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.07.2013 11:27:02
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import java.util.List;

import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.instanceref.IContextValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 17:47:09 2013 %
 * 
 *         %version: 1 %
 */
public class ContextValue implements IContextValue
{
	private IContextType wrapper;
	private List<GIdentifiable> value;

	/**
	 * @param wrapper
	 * @param value
	 */
	public ContextValue(IContextType wrapper, List<GIdentifiable> value)
	{
		this.wrapper = wrapper;
		this.value = value;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IContextValue#getElements()
	 */
	public List<GIdentifiable> getElements()
	{
		return value;

	}

	public IContextType getContextType()
	{
		return wrapper;

	}

}
