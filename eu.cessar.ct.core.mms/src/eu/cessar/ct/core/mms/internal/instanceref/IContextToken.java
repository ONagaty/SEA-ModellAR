/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 08.07.2013 09:32:07
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref;

import eu.cessar.ct.core.mms.instanceref.IContextType;

/**
 * Wrapper around String representing the context of an instance reference, having as a suffix a symbol denoting the
 * multiplicity of the context (?, * or +)
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 17:47:08 2013 %
 * 
 *         %version: 1 %
 */
public interface IContextToken
{
	/**
	 * @return the index of the context
	 */
	public int getIndex();

	/**
	 * @param index
	 */
	public void setIndex(int index);

	/**
	 * @return the String
	 */
	public String getContext();

	/**
	 * @return whether another context follows this one
	 */
	public boolean hasNext();

	/**
	 * @param next
	 */
	public void setNext(IContextToken next);

	/**
	 * @return the next context
	 */
	public IContextToken getNext();

	/**
	 * @return context type
	 */
	public IContextType getContextType();

	/**
	 * @param wrapper
	 */
	public void setContextType(IContextType wrapper);

}
