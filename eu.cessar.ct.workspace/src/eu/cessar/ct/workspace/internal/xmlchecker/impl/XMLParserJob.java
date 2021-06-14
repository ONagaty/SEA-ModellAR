/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 31.07.2012 17:03:38 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import java.io.IOException;

import javax.xml.parsers.SAXParser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

import eu.cessar.ct.core.platform.xml.StopParsingException;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer;
import eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector;
import eu.cessar.req.Requirement;

/**
 * The parsing of each of the 2 XML files (expected and obtained ones) is performed in such a job. The used SAX parsing
 * handler ( {@link ConsistencyCheckXMLHandler}) delegates the received events to a shared resource between the 2 jobs:
 * {@link IXMLConsistencyInspector} that performs the actual comparison.
 * 
 * @author uidl6870
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#3")
public class XMLParserJob extends Job
{
	private SAXParser parser;
	private IXMLConsistencyInspector checker;

	private IParsingBuffer side;
	private IStatus status;

	/**
	 * @param name
	 * @param parser
	 * @param checker
	 * @param side
	 */
	public XMLParserJob(String name, SAXParser parser, IXMLConsistencyInspector checker, IParsingBuffer side)
	{
		super(name);

		this.parser = parser;
		this.side = side;
		this.checker = checker;

		status = Status.OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		try
		{
			ConsistencyCheckXMLHandler handler = new ConsistencyCheckXMLHandler(checker, side);
			handler.setDocumentLocator(new LocatorImpl());
			parser.parse(side.getFile(), handler);
		}
		catch (StopParsingException e)
		{
			// do nothing, either cancel has been requested, or parsing has
			// been stopped as an inconsistency has been found
		}
		catch (SAXException e)
		{
			checker.setStopParsingException();
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e);
		}
		catch (IOException e)
		{
			checker.setStopParsingException();
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e);
		}

		return Status.OK_STATUS;
	}

	/**
	 * @return the status
	 */
	public IStatus getStatus()
	{
		return status;
	}

}
