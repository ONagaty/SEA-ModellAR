/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 02.08.2012 11:23:20 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker;

import java.io.File;

/**
 * An instance is created for the original file and another for the generated file. During the parsing, the
 * {@link IXMLConsistencyInspector} updates the buffer with the last parsed XML item and the corresponding event (i.e
 * start/end of element, text inside element).
 * 
 * @author uidl6870
 * 
 */
public interface IParsingBuffer
{

	/**
	 * @return the schema location for the file
	 */
	public String getSchemaLocation();

	/**
	 * @param schemaLocation
	 */
	public void setSchemaLocation(String schemaLocation);

	/**
	 * @return the {@link EXMLParsingEvent}
	 */
	public EXMLParsingEvent getEvent();

	/**
	 * @param event
	 */
	public void setXMLEvent(EXMLParsingEvent event);

	/**
	 * @param element
	 */
	public void setElement(IElement element);

	/**
	 * @return the {@link IElement}
	 */
	public IElement getElement();

	/**
	 * Returns the file to be parsed
	 * 
	 * @return the file to be parsed
	 */
	public File getFile();

	/**
	 * Returns the last parsed XML item
	 * 
	 * @return the last parsed XML item
	 */
	public String getLastItem();

	/**
	 * 
	 * @param item
	 */
	public void setLastItem(String item);

	/**
	 * 
	 * @return whether the last parsed item is an element's text
	 */
	public boolean isLastItemElementText();

	/**
	 * @param isText
	 */
	public void setLastItemElementText(boolean isText);

	/**
	 * @return the line number
	 */
	public int getLineNumber();

	/**
	 * @param line
	 */
	public void setLineNumber(int line);

	/**
	 * @return the column number
	 */
	public int getColumnNumber();

	/**
	 * @param column
	 */
	public void setColumnNumber(int column);

}
