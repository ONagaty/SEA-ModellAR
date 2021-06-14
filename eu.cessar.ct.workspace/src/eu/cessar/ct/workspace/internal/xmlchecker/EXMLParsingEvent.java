/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 07.08.2012 15:34:06 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker;

import eu.cessar.ct.workspace.internal.xmlchecker.impl.ConsistencyCheckXMLHandler;

/**
 * Enumeration for events signaled by {@link ConsistencyCheckXMLHandler}
 * 
 * @author uidl6870
 * 
 */
public enum EXMLParsingEvent
{

	/**
	 * 
	 */
	START_ELEMENT("Start Element"), //$NON-NLS-1$ 
	/**
	 * 
	 */
	END_ELEMENT("End Element"), //$NON-NLS-1$ 
	/**
	 * 
	 */
	TEXT_INSIDE_ELEMENT("Text inside element"); //$NON-NLS-1$ 

	private final String event;

	EXMLParsingEvent(String event)
	{
		this.event = event;
	}

	/**
	 * @return event type
	 */
	public String getName()
	{
		return event;
	}
}
