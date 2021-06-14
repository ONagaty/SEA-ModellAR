/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 31.07.2012 10:30:48 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import eu.cessar.ct.workspace.internal.xmlchecker.EXMLParsingEvent;
import eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer;
import eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector;

/**
 * Handler for parsing XML files that delegates the {@link EXMLParsingEvent} events to the
 * {@link IXMLConsistencyInspector}.
 * 
 * @author uidl6870
 * 
 */
public class ConsistencyCheckXMLHandler extends DefaultHandler
{
	private IXMLConsistencyInspector checker;
	private EXMLParsingEvent lastEvent;

	private boolean textSent;
	private boolean textAvailable;

	private Locator locator;
	private IParsingBuffer side;
	private StringBuffer text;

	/**
	 * @param checker
	 * @param side
	 */
	public ConsistencyCheckXMLHandler(IXMLConsistencyInspector checker, IParsingBuffer side)
	{
		this.checker = checker;
		this.side = side;
		text = new StringBuffer();
	}

/*
 * (non-Javadoc)
 * 
 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
 * org.xml.sax.Attributes)
 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		lastEvent = EXMLParsingEvent.START_ELEMENT;
		sendTextIfAvailable();

		setLocation();
		checker.startElement(side, qName.trim(), attributes);

	}

/*
 * (non-Javadoc)
 * 
 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		lastEvent = EXMLParsingEvent.END_ELEMENT;
		sendTextIfAvailable();

		setLocation();
		checker.endElement(side, qName.trim());
	}

	/**
	 * @throws SAXException
	 * 
	 */
	private void sendTextIfAvailable() throws SAXException
	{
		if (textAvailable && !textSent)
		{
			if (text.toString().trim().isEmpty())
			{
				// empty text, do not send it
			}
			else
			{
				setLocation();

				checker.textInsideElement(side, text.toString().trim());
				text.delete(0, text.length());
			}
			textSent = true;
			textAvailable = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (lastEvent.equals(EXMLParsingEvent.END_ELEMENT))
		{
			// ignore text outside elements
			return;
		}
		textSent = false;
		textAvailable = true;

		text.append(new String(ch, start, length));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException
	{
		checker.endDocument(side);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	public void setDocumentLocator(Locator loc)
	{
		locator = loc;
	}

	private void setLocation()
	{
		side.setLineNumber(locator.getLineNumber());
		side.setColumnNumber(locator.getColumnNumber());
	}

}
