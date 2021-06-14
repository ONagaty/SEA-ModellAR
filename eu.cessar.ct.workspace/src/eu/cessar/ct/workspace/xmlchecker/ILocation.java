/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 03.08.2012 13:42:18 </copyright>
 */
package eu.cessar.ct.workspace.xmlchecker;

/**
 * Stores the location (line and column numbers) of an XML item (element, element text or attribute) inside an AUTOSAR
 * file at which an inconsistency has been encountered.
 * 
 * @author uidl6870
 * 
 */
public interface ILocation
{
	/**
	 * @return the line number
	 */
	public int getLineNumber();

	/**
	 * @return the column number
	 */
	public int getColumnNumber();
}
