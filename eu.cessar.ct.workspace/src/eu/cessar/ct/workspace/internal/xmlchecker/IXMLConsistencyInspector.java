/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 31.07.2012 16:57:25 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import eu.cessar.ct.core.platform.xml.StopParsingException;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * An instance of {@link IXMLConsistencyInspector} is notified as the original XML file and the corresponding copy are
 * being parsed. It is responsible with verifying that the XML items (elements, their content and attributes) from the
 * copy match the ones from the original file. <br>
 * In case that a difference is encountered at element level, the consistency inspector will stop the parsing of both
 * files, by throwing {@link StopParsingException} exceptions and encapsulating the necessary data in a
 * {@link IXMLCheckerInconsistency} entity.
 * 
 * @author uidl6870
 * 
 */
public interface IXMLConsistencyInspector
{

	/**
	 * Notifies the consistency inspector of the start of <code>qName</code> element, in the file corresponding to
	 * <code>side</code> parsing buffer
	 * 
	 * @param side
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @throws SAXException
	 */
	public void startElement(IParsingBuffer side, String qName, Attributes attributes) throws SAXException;

	/**
	 * Notifies the consistency inspector of the end of <code>qName</code> element, in the file corresponding to
	 * <code>side</code> parsing buffer
	 * 
	 * @param side
	 * @param qName
	 * @throws SAXException
	 */
	public void endElement(IParsingBuffer side, String qName) throws SAXException;

	/**
	 * Notifies the consistency inspector of the occurrence of an element's <code>text</code>, in the file corresponding
	 * to <code>side</code> parsing buffer
	 * 
	 * @param side
	 * @param text
	 * @throws SAXException
	 */
	public void textInsideElement(IParsingBuffer side, String text) throws SAXException;

	/**
	 * Notifies the consistency inspector that the end of the file corresponding to <code>side</code> parsing buffer has
	 * been reached
	 * 
	 * @param side
	 */
	public void endDocument(IParsingBuffer side);

	/**
	 * Return the list with inconsistencies. If no inconsistency found, an empty list in returned
	 * 
	 * @return list with found inconsistencies, never <code>null</code>
	 */
	public List<IXMLCheckerInconsistency> getInconsistencies();

	/**
	 * In case that the parsing of on one the files stops because of an error, this flag is set indicating that parsing
	 * of the other file should be ended also
	 */
	public void setStopParsingException();

}
