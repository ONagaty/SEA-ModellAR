/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 27, 2009 10:45:05 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import eu.cessar.ct.core.platform.xml.StopParsingException;
import eu.cessar.ct.runtime.CessarRuntime;

/**
 * @author uidl6870
 * 
 *         Handler for parsing .classpath file
 * 
 */
public class ClasspathHandler extends DefaultHandler
{

	private String output = null;

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException
	{
		if (qName.equals(CessarRuntime.CLASSPATHENTRY))
		{
			String value = attributes.getValue(CessarRuntime.ATTRIBUTE_KIND);
			if (CessarRuntime.KIND_VALUE_OUTPUT.equals(value))
			{
				output = attributes.getValue(CessarRuntime.ATTRIBUTE_PATH);
				throw new StopParsingException();
			}
		}
	}

	public String getOutput()
	{
		return output;
	}
}
