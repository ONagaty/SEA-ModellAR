/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 19, 2009 2:36:10 PM </copyright>
 */
package eu.cessar.ct.core.platform.xml;

import org.xml.sax.SAXException;

/**
 * An exception indicating that the parsing should stop.
 * 
 * @author uidl6870
 */
public class StopParsingException extends SAXException
{
	/**
	 * All serializable objects should have a stable serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an instance of <code>StopParsingException</code> with a
	 * <code>null</code> detail message.
	 */
	public StopParsingException()
	{
		super((String) null);
	}
}
