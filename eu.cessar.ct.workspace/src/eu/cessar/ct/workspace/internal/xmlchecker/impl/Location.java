/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 06.08.2012 10:38:15 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import eu.cessar.ct.workspace.xmlchecker.ILocation;

/**
 * Implementation of {@link ILocation}
 * 
 * @author uidl6870
 * 
 */
public class Location implements ILocation
{

	private int line;
	private int column;

	/**
	 * @param lineNumber
	 * @param columnNumber
	 */
	public Location(int lineNumber, int columnNumber)
	{
		line = lineNumber;
		column = columnNumber;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.workspace.xmlchecker.ILocation#getLineNumber()
	 */
	public int getLineNumber()
	{
		return line;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.workspace.xmlchecker.ILocation#getColumnNumber()
	 */
	public int getColumnNumber()
	{
		return column;
	}

}
