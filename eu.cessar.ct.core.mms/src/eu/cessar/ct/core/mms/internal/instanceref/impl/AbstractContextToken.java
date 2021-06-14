/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 08.07.2013 09:34:34
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.internal.instanceref.IContextToken;

/**
 * For each context type in [A, B, C, D, ..., Z ] an instance of this class will be created. Each instance will have a
 * reference to the next context type, the last instance (For Z type will have null as next context type)
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 10:35:12 2013 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractContextToken implements IContextToken
{
	private IContextToken nextToken;
	private String context;
	private int index;
	private IContextType wrapper;

	/**
	 * @param token
	 */
	public AbstractContextToken(String token)
	{
		context = token;
	}

	public void setNext(IContextToken next)
	{
		nextToken = next;
	}

	public IContextToken getNext()
	{
		return nextToken;
	}

	public boolean hasNext()
	{
		return nextToken != null;
	}

	public String getContext()
	{
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.instanceref.impl.IContextToken#getIndex()
	 */
	@Override
	public int getIndex()
	{
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.instanceref.impl.IContextToken#setIndex(int)
	 */
	@Override
	public void setIndex(int index)
	{
		this.index = index;
	}

	@Override
	public void setContextType(IContextType wr)
	{
		wrapper = wr;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.instanceref.impl.IContextToken#getContextTypeWrapper()
	 */
	@Override
	public IContextType getContextType()
	{
		return wrapper;
	}

}
