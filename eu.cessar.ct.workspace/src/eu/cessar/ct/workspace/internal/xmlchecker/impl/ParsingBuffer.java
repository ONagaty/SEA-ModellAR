/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 02.08.2012 11:25:57 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import java.io.File;

import eu.cessar.ct.workspace.internal.xmlchecker.EXMLParsingEvent;
import eu.cessar.ct.workspace.internal.xmlchecker.IElement;
import eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer;

/**
 * Implementation of {@link IParsingBuffer}
 * 
 * @author uidl6870
 * 
 */
public class ParsingBuffer implements IParsingBuffer
{
	private File file;
	private IElement element;
	private EXMLParsingEvent event;
	private String schemaLocation;
	private String item;
	private boolean text;

	private int column;
	private int line;

	/**
	 * @param file
	 */
	public ParsingBuffer(File file)
	{
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#getFile()
	 */
	public File getFile()
	{
		return file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#getLastItem()
	 */
	public String getLastItem()
	{
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#setLastItem(java.lang.String)
	 */
	public void setLastItem(String lastItem)
	{
		item = lastItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#isLastItemText()
	 */
	public boolean isLastItemElementText()
	{
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#setLastItemText(boolean)
	 */
	public void setLastItemElementText(boolean isText)
	{
		text = isText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#getLineNumber()
	 */
	public int getLineNumber()
	{
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#setLineNumber(int)
	 */
	public void setLineNumber(int lineNumber)
	{
		line = lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#getColumnNumber()
	 */
	public int getColumnNumber()
	{
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#setColumnNumber(int)
	 */
	public void setColumnNumber(int columnNumber)
	{
		column = columnNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#setElement(eu.cessar.ct.workspace.internal.xmlchecker
	 * .IElement)
	 */
	public void setElement(IElement element)
	{
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#getElement()
	 */
	public IElement getElement()
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#getEvent()
	 */
	public EXMLParsingEvent getEvent()
	{
		return event;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#setXMLEvent(eu.cessar.ct.workspace.internal.xmlchecker
	 * .EXMLParsingEvent)
	 */
	public void setXMLEvent(EXMLParsingEvent xmlEvent)
	{
		event = xmlEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#getSchemaLocation()
	 */
	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer#setSchemaLocation(java.lang.String)
	 */
	public void setSchemaLocation(String schemaLocation)
	{
		this.schemaLocation = schemaLocation;

	}

}
