/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 31.07.2012 16:58:50 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.xml.StopParsingException;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.internal.Messages;
import eu.cessar.ct.workspace.internal.xmlchecker.EXMLParsingEvent;
import eu.cessar.ct.workspace.internal.xmlchecker.IElement;
import eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer;
import eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector;
import eu.cessar.ct.workspace.xmlchecker.EXMLInconsistencyType;
import eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail;
import eu.cessar.ct.workspace.xmlchecker.ILocation;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * Shared resource between the 2 XML parsing handlers that performs an inspection by checking that the XML items
 * received from the original file match the ones from the generated file.
 *
 * @author uidl6870
 *
 */
public class XMLConsistencyInspector implements IXMLConsistencyInspector
{
	private static final String ROOT_ELEMENT = "AUTOSAR"; //$NON-NLS-1$
	private static final String[] ELEMENTS_WITH_IGNORABLE_ATTRS = new String[] {"AUTOSAR"}; //$NON-NLS-1$
	private static final String[] IGNORABLE_ATTRS = new String[] {"UUID"}; //$NON-NLS-1$

	private IParsingBuffer originalBuffer;
	private IParsingBuffer copyBuffer;
	private IParsingBuffer currentBuffer;

	private boolean receivedFromOriginal;
	private boolean receivedFromCopy;

	private IProgressMonitor monitor;

	private List<IXMLCheckerInconsistency> inconsistencies;

	/**
	 * Set on true when the parsing in one of the 2 threads is stopped due to cancellation or occurrence of an
	 * inconsistency, so that the other thread also stops parsing
	 */
	private boolean stopParsing;

	private Set<String> ignorableXMLNames;

	private boolean inCompareIfIgnorable;

	private int currentLineInOriginalFile;

	/**
	 * @param ignorableXMLNames
	 * @param original
	 * @param copy
	 * @param monitor
	 */
	public XMLConsistencyInspector(Set<String> ignorableXMLNames, IParsingBuffer original, IParsingBuffer copy,
		IProgressMonitor monitor)
	{
		this.ignorableXMLNames = ignorableXMLNames;
		originalBuffer = original;
		copyBuffer = copy;

		this.monitor = monitor;

		if (monitor == null)
		{
			this.monitor = new NullProgressMonitor();
		}

		inconsistencies = new ArrayList<IXMLCheckerInconsistency>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector#startElement(eu.cessar.ct.workspace.internal
	 * .xmlchecker.IParsingBuffer, java.lang.String, org.xml.sax.Attributes)
	 */
	public synchronized void startElement(IParsingBuffer buffer, String qName, Attributes attributes)
		throws SAXException
	{
		if (monitor.isCanceled())
		{
			handleCancellation();
		}

		if (stopParsing)
		{
			// end execution of current thread
			throw new StopParsingException();
		}

		IElement element = new Element(attributes);
		element.setName(qName);

		buffer.setXMLEvent(EXMLParsingEvent.START_ELEMENT);
		currentBuffer = buffer;
		updateBuffer(buffer, element, qName, false);

		if (inCompareIfIgnorable)
		{
			addInconsistency(EXMLInconsistencyType.ELEMENT);
			handleStopParsing();
		}

		tryToCompareItems(EXMLInconsistencyType.ELEMENT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector#endElement(eu.cessar.ct.workspace.internal
	 * .xmlchecker.IParsingBuffer, java.lang.String)
	 */
	public synchronized void endElement(IParsingBuffer buffer, String qName) throws SAXException
	{
		if (monitor.isCanceled())
		{
			handleCancellation();
		}

		if (stopParsing)
		{
			// end execution of current thread
			throw new StopParsingException();
		}

		IElement element = new Element(null);
		element.setName(qName);

		buffer.setXMLEvent(EXMLParsingEvent.END_ELEMENT);
		currentBuffer = buffer;
		updateBuffer(buffer, element, qName, false);

		if (currentBuffer == originalBuffer && inCompareIfIgnorable)
		{
			inCompareIfIgnorable = false;
			receivedFromOriginal = false;
			originalBuffer.setElement(null);
			originalBuffer.setLastItem(""); //$NON-NLS-1$
			// an ignorable element has ended right after it started
			return;
		}

		tryToCompareItems(EXMLInconsistencyType.ELEMENT);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector#textInsideElement(eu.cessar.ct.workspace.
	 * internal.xmlchecker.IParsingBuffer, java.lang.String)
	 */
	public synchronized void textInsideElement(IParsingBuffer buffer, String text) throws SAXException
	{
		if (monitor.isCanceled())
		{
			handleCancellation();
		}

		if (stopParsing)
		{
			// end execution of current thread
			throw new StopParsingException();
		}

		buffer.setXMLEvent(EXMLParsingEvent.TEXT_INSIDE_ELEMENT);
		currentBuffer = buffer;
		updateBuffer(buffer, null, text, true);

		if (inCompareIfIgnorable)
		{
			addInconsistency(EXMLInconsistencyType.ELEMENT_TEXT);
			handleStopParsing();
		}

		tryToCompareItems(EXMLInconsistencyType.ELEMENT_TEXT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IXMLChecker#endDocument()
	 */
	public synchronized void endDocument(IParsingBuffer side)
	{
		notify();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IXMLChecker#getInconsistency()
	 */
	public List<IXMLCheckerInconsistency> getInconsistencies()
	{
		return inconsistencies;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector#setStopParsingException()
	 */
	public synchronized void setStopParsingException()
	{
		stopParsing = true;
		notify();
	}

	/**
	 * If items available, delegates comparison to {@link XMLConsistencyInspector#compareItems(EXMLInconsistencyType)} ,
	 * otherwise synchronizes the two threads by blocking the current one, so that the other one receives the XML item. <br>
	 * <i>Note</i>: The method is called within one of the two thread in the moment an XML event (start of element, end
	 * of element, text inside element) has been received from the handler. All the 3 caller methods: <li>
	 * {@link XMLConsistencyInspector#startElement(IParsingBuffer, String, Attributes)}</li> <li>
	 * {@link XMLConsistencyInspector#textInsideElement(IParsingBuffer, String)}</li> <li>
	 * {@link XMLConsistencyInspector#textInsideElement(IParsingBuffer, String)}</li> <br>
	 * are marked as <i>synchronized</i>, thus serial access is guaranteed.
	 *
	 * @param type
	 *        the type of inconsistency to check (whether at element, element text or attribute level)
	 * @throws StopParsingException
	 *         exception in case inconsistency has been detected that is incompatible with the continuation of parsing
	 *         and comparison (i.e. at element level)
	 *
	 */
	private void tryToCompareItems(EXMLInconsistencyType type) throws StopParsingException
	{
		if (!itemsAvailable())
		{
			handleWaitForOtherItem();
		}

		if (itemsAvailable())
		{
			/*
			 * Check whether text inside element has been received from only one of the files. If so, verifies if text
			 * is empty (can be ignored), case in which allows the corresponding thread to continue. Otherwise, (text
			 * not empty) adds and inconsistency object to the list and stops parsing.
			 */
			if (originalBuffer.isLastItemElementText() ^ copyBuffer.isLastItemElementText())
			{
				if (originalBuffer.isLastItemElementText())
				{
					handleMissingElementText(originalBuffer);
				}
				else
				{
					handleMissingElementText(copyBuffer);
				}
			}

			compareItems(type);

		}
	}

	/**
	 * Blocks current thread, so that the other one has the chance to obtain its item
	 *
	 * @throws StopParsingException
	 */
	private void handleWaitForOtherItem() throws StopParsingException
	{
		try
		{
			notify();

			// block current thread
			wait();

			// check flag after each wait()
			if (stopParsing)
			{
				throw new StopParsingException();
			}
		}
		catch (InterruptedException e)
		{
			handleStopParsing();
		}
	}

	/**
	 * @param buffer
	 * @param element
	 * @param qName
	 * @param isElementText
	 */
	private void updateBuffer(IParsingBuffer buffer, IElement element, String qName, boolean isElementText)
	{
		if (buffer == originalBuffer)
		{
			receivedFromOriginal = true;
		}
		else
		{
			receivedFromCopy = true;
		}

		buffer.setLastItem(qName);
		buffer.setLastItemElementText(isElementText);
		if (element != null)
		{
			buffer.setElement(element);
		}
	}

	/**
	 * @throws SAXException
	 */
	private void compareAttributes() throws StopParsingException
	{
		if (monitor.isCanceled())
		{
			handleCancellation();
		}

		IElement element1 = originalBuffer.getElement();
		Attributes attributes1 = element1.getAttributes();

		IElement element2 = copyBuffer.getElement();
		Attributes attributes2 = element2.getAttributes();

		if (attributes1 == null || attributes2 == null)
		{
			return;
		}

		Map<String, String> map1 = getAttributesMap(attributes1);
		Map<String, String> map2 = getAttributesMap(attributes2);

		List<String> processedAttrs = new ArrayList<String>();

		boolean dif = false;
		for (String key: map1.keySet())
		{
			if (shouldSkipAttribute(key))
			{
				continue;
			}
			processedAttrs.add(key);

			String value1 = map1.get(key);
			String value2 = map2.get(key);

			if (!value1.equals(value2))
			{
				dif = true;

				originalBuffer.getElement().setAttributeName(key);
				originalBuffer.getElement().setAttributeValue(value1);

				copyBuffer.getElement().setAttributeName(key);
				copyBuffer.getElement().setAttributeValue(value2 == null ? "" : value2); //$NON-NLS-1$

				addInconsistency(EXMLInconsistencyType.ATTRIBUTE_VALUE);
			}
		}

		if (dif || map1.size() != map2.size())
		{
			for (String key: map2.keySet())
			{
				if (shouldSkipAttribute(key))
				{
					continue;
				}
				if (processedAttrs.contains(key))
				{
					continue;
				}
				processedAttrs.add(key);

				String value1 = map1.get(key);
				String value2 = map2.get(key);

				if (!value2.equals(value1))
				{
					originalBuffer.getElement().setAttributeName(key);
					originalBuffer.getElement().setAttributeValue(value1 == null ? "" : value1); //$NON-NLS-1$

					copyBuffer.getElement().setAttributeName(key);
					copyBuffer.getElement().setAttributeValue(value2);

					addInconsistency(EXMLInconsistencyType.ATTRIBUTE_VALUE);
				}
			}
		}
	}

	/**
	 * Returns <code>true</code> if comparison of given <code>attrName</code> should be skipped
	 *
	 * @param key
	 * @return
	 */
	private boolean shouldSkipAttribute(String attrName)
	{
		boolean skip = false;
		for (String attr: IGNORABLE_ATTRS)
		{
			if (attr.equals(attrName))
			{
				skip = true;
				break;
			}
		}
		return skip;
	}

	/**
	 * Returns a mapping between the attributes' name and their value
	 *
	 * @param attributes
	 * @return
	 */
	private Map<String, String> getAttributesMap(Attributes attributes)
	{
		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < attributes.getLength(); i++)
		{
			String attrName = attributes.getQName(i);
			String value = attributes.getValue(i);
			map.put(attrName, value);
		}

		return map;
	}

	/**
	 * Return <code>true</code> if items have been received from both files
	 *
	 * @return
	 */
	private boolean itemsAvailable()
	{
		return receivedFromOriginal && receivedFromCopy;
	}

	private void handleStopParsing() throws StopParsingException
	{
		// set flag indicating that parsing should be ended in the other thread
		stopParsing = true;

		// wake up the other thread
		notify();

		monitor.done();

		// end execution of current thread
		throw new StopParsingException();
	}

	private void handleCancellation() throws StopParsingException
	{
		// set flag indicating that parsing should be ended in the other thread
		stopParsing = true;

		// wake up the other thread
		notify();

		// end execution of current thread
		throw new StopParsingException();
	}

	/**
	 * Compares the last received items, stored in the two buffers.
	 *
	 * @param type
	 *        the type of inconsistency to check
	 * @throws StopParsingException
	 */
	private void compareItems(EXMLInconsistencyType type) throws StopParsingException
	{
		EXMLParsingEvent event = originalBuffer.getEvent();

		if (originalBuffer.getLastItem().equals(copyBuffer.getLastItem()))
		{
			// comparison passed, continue with comparison of attributes if the
			// case
			if (event.equals(EXMLParsingEvent.START_ELEMENT))
			{
				boolean skipComparison = shouldSkipAttributes(currentBuffer.getElement());
				if (!skipComparison)
				{
					compareAttributes();
				}

				if (ROOT_ELEMENT.equals(currentBuffer.getElement().getName()))
				{
					retrieveSchemaLocation();
				}
			}

			// comparison passed for the current items
			reportProgress();
			resetBuffers();
		}
		else
		{
			// items do not match, check whether item from original file is
			// ignorable
			if (event.equals(EXMLParsingEvent.START_ELEMENT))
			{
				if (ignorableXMLNames.contains(originalBuffer.getLastItem()))
				{
					inCompareIfIgnorable = true;
					receivedFromOriginal = false;

					if (currentBuffer == originalBuffer)
					{
						// let it continue
						reportProgress();
						return;
					}
					else
					{
						// block current thread
						handleWaitForOtherItem();
					}
				}
				else
				{
					// item not ignorable
					addInconsistency(type);
					handleStopParsing();
				}
			}
			else if (event.equals(EXMLParsingEvent.TEXT_INSIDE_ELEMENT))
			{
				// try to convert to Integer/Float. E.g: <VALUE>13</VALUE>
				// and <VALUE>13.0</VALUE>
				boolean match = tryToMatchTextElements();

				if (match)
				{
					// comparison passed
					reportProgress();
					resetBuffers();
				}
				else
					// comparison failed
				{
					addInconsistency(type);
					resetBuffers();
				}

			}
			else if (event.equals(EXMLParsingEvent.END_ELEMENT))
			{
				addInconsistency(type);
				handleStopParsing();
			}
		}

	}

	/**
	 * Return <code>true</code> if the comparison of <code>element</code>'s attributes should be skipped
	 *
	 * @param element
	 * @return
	 */
	private boolean shouldSkipAttributes(IElement element)
	{
		boolean skip = false;
		String name = element.getName();
		for (String elem: ELEMENTS_WITH_IGNORABLE_ATTRS)
		{
			if (elem.equals(name))
			{
				skip = true;
				break;
			}
		}
		return skip;
	}

	/**
	 * When the root of the XML document (AUTOSAR) is reached, retrieves the information related to the schema location
	 * and stores it in the two corresponding buffers
	 */
	private void retrieveSchemaLocation()
	{
		IElement element = originalBuffer.getElement();
		Attributes attributes = element.getAttributes();
		if (attributes != null)
		{
			String schemaLocation = attributes.getValue(MetaModelUtils.SCHEMA_LOCATION_ATTR);
			originalBuffer.setSchemaLocation(schemaLocation);

			element = copyBuffer.getElement();
			attributes = element.getAttributes();

			if (attributes != null)
			{
				schemaLocation = attributes.getValue(MetaModelUtils.SCHEMA_LOCATION_ATTR);
				copyBuffer.setSchemaLocation(schemaLocation);
			}
		}
	}

	/**
	 * Called when 'text inside element' items' textual comparison failed, to verify if items correspond to numerical
	 * values which are in fact equal, but have a different textual representation in the XML file. e.g: 7 and 7.0; 3,14
	 * and 3,140
	 *
	 * @return
	 */
	private boolean tryToMatchTextElements()
	{
		boolean match = false;

		String lastItem1 = originalBuffer.getLastItem();
		String lastItem2 = copyBuffer.getLastItem();

		// try to convert to integer
		try
		{
			Integer val1 = Integer.decode(lastItem1);
			Integer val2 = Integer.decode(lastItem2);

			if (val1.equals(val2))
			{
				match = true;
			}
		}
		catch (NumberFormatException e)
		{
			// ignore
		}

		if (!match)
		{
			// try to convert to float
			try
			{
				Float val1 = Float.valueOf(lastItem1);
				Float val2 = Float.valueOf(lastItem2);

				if (val1.equals(val2))
				{
					match = true;
				}
			}
			catch (NumberFormatException e)
			{
				// ignore
			}
		}

		return match;
	}

	/**
	 * Report progress within the thread for parsing the original file
	 */
	private void reportProgress()
	{
		if (currentLineInOriginalFile != originalBuffer.getLineNumber())
		{
			currentLineInOriginalFile = originalBuffer.getLineNumber();

			monitor.worked(1);
		}
	}

	/**
	 * Handles the case when only one of the two items is of 'element text' type. <br>
	 * If empty, ignores it, resets the flag indicating item has been received and either allows parsing to continue or
	 * blocks current thread, depending on the thread with empty element text item. <br>
	 * Otherwise, adds an inconsistency to the list and stops parsing.
	 *
	 * @param bufferWithElementText
	 *        the buffer with the last item representing a text element
	 * @throws StopParsingException
	 */
	private void handleMissingElementText(IParsingBuffer bufferWithElementText) throws StopParsingException
	{
		if (bufferWithElementText.getLastItem().isEmpty())
		{
			// element text is an empty, ignore and request the following
			// item
			if (bufferWithElementText == originalBuffer)
			{
				receivedFromOriginal = false;
			}
			else
			{
				receivedFromCopy = false;
			}

			if (currentBuffer == bufferWithElementText)
			{
				// let the current thread continue
				return;
			}
			else
			{
				handleWaitForOtherItem();
			}
		}
		else
		{
			// found a difference, set empty string in the other buffer
			if (bufferWithElementText == originalBuffer)
			{
				copyBuffer.setLastItem(""); //$NON-NLS-1$
			}
			else
			{
				originalBuffer.setLastItem(""); //$NON-NLS-1$
			}

			addInconsistency(EXMLInconsistencyType.ELEMENT_TEXT);
			handleStopParsing();
		}
	}

	/**
	 * Resets the flags in the two buffers and allows the other thread to continue.
	 */
	private void resetBuffers()
	{
		receivedFromOriginal = false;
		receivedFromCopy = false;

		originalBuffer.setLastItemElementText(false);
		copyBuffer.setLastItemElementText(false);

		originalBuffer.setLastItem(""); //$NON-NLS-1$
		copyBuffer.setLastItem(""); //$NON-NLS-1$

		notify();
	}

	/**
	 * Creates an object representing an identified inconsistency of the given <code>type</code>, populates it with data
	 * from the two buffers and adds it to the list of inconsistencies.
	 *
	 *
	 * @param type
	 *        the type of inconsistency
	 */
	private void addInconsistency(EXMLInconsistencyType type)
	{
		XMLCheckInconsistency inconsistency = new XMLCheckInconsistency();

		inconsistency.setDetailFromOriginalFile(createDetail(originalBuffer, type));
		inconsistency.setDetailFromGeneratedFile(createDetail(copyBuffer, type));

		inconsistency.setInconsistencyType(type);

		ESeverity severity = SeverityAnalyzer.getSeverity(inconsistency);
		inconsistency.setSeverity(severity);

		if (type.equals(EXMLInconsistencyType.ELEMENT))
		{
			inconsistency.setMessage(NLS.bind(Messages.saxException_element_message_detailed, new Object[] {
				originalBuffer.getLastItem(), copyBuffer.getLastItem()}));
		}
		else if (type.equals(EXMLInconsistencyType.ELEMENT_TEXT))
		{
			inconsistency.setMessage(NLS.bind(Messages.saxException_value_message_detailed,
				new Object[] {originalBuffer.getElement().getName()}));
		}
		else if (type.equals(EXMLInconsistencyType.ATTRIBUTE_VALUE))
		{
			inconsistency.setMessage(NLS.bind(Messages.saxException_attribute_message_detailed, new Object[] {
				originalBuffer.getElement().getAttributeName(), originalBuffer.getElement().getName()}));
		}

		inconsistencies.add(inconsistency);
	}

	/**
	 * Creates an object representing additional information of an inconsistency, information related to one of the two
	 * files retrieved from the given <code>buffer</code>
	 *
	 * @param buffer
	 * @param type
	 * @return
	 */
	private IInconsistencyDetail createDetail(IParsingBuffer buffer, EXMLInconsistencyType type)
	{
		IInconsistencyDetail detail = new InconsistencyDetail();

		detail.setFile(buffer.getFile());
		detail.setSchemaLocation(buffer.getSchemaLocation());

		ILocation location = new Location(buffer.getLineNumber(), buffer.getColumnNumber());
		detail.setLocation(location);

		if (type.equals(EXMLInconsistencyType.ELEMENT))
		{
			detail.setItem(buffer.getLastItem());
		}
		else if (type.equals(EXMLInconsistencyType.ELEMENT_TEXT))
		{
			detail.setItem(buffer.getLastItem());
		}
		else if (type.equals(EXMLInconsistencyType.ATTRIBUTE_VALUE))
		{
			detail.setItem(buffer.getElement().getAttributeValue());
		}

		return detail;
	}

}
