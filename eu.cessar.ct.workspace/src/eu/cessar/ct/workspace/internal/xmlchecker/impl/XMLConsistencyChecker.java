/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 04.09.2012 15:00:27 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import java.io.File;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.xml.sax.SAXException;

import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.xmlchecker.IParsingBuffer;
import eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyChecker;
import eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyInspector;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * It starts 2 parsers: one for each of the received files on 2 different threads, parsers that will notify the same
 * {@link IXMLConsistencyInspector} of the XML elements being parsed. The second is responsible with synchronizing the 2
 * threads and creating {@link IXMLCheckerInconsistency}(s) each time a difference between the 2 files is found
 * 
 * 
 * @author uidl6870
 * 
 */
public class XMLConsistencyChecker implements IXMLConsistencyChecker
{
	private final Set<String> ignorableXMLNames;

	/**
	 * @param ignorableXMLNames
	 */
	public XMLConsistencyChecker(Set<String> ignorableXMLNames)
	{
		this.ignorableXMLNames = ignorableXMLNames;
	}

	public IConsistencyCheckResult<IXMLCheckerInconsistency> compare(File expectedFile, File obtainedFile,
		IProgressMonitor monitor)
	{
		IConsistencyCheckResult<IXMLCheckerInconsistency> report = new XMLConsistencyCheckResult();

		try
		{
			IParsingBuffer left = new ParsingBuffer(expectedFile);
			IParsingBuffer right = new ParsingBuffer(obtainedFile);

			IXMLConsistencyInspector checker = new XMLConsistencyInspector(ignorableXMLNames, left, right, monitor);

			XMLParserJob job1 = createJob(checker, left);
			XMLParserJob job2 = createJob(checker, right);

			job1.schedule();
			job2.schedule();

			try
			{
				job1.join();
				job2.join();

				IStatus result1 = job1.getStatus();
				IStatus result2 = job2.getStatus();

				if (result1.isOK() && result2.isOK())
				{
					report.setStatus(Status.OK_STATUS);
				}
				else
				{
					if (!result1.isOK())
					{
						report.setStatus(result1);
					}
					else
					{
						report.setStatus(result2);
					}
				}

				report.setInconsistencies(checker.getInconsistencies());

			}
			catch (InterruptedException e)
			{
				checker.setStopParsingException();

				report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e));
			}
		}
		catch (ParserConfigurationException e1)
		{
			report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e1.getMessage(), e1));
		}
		catch (SAXException e1)
		{
			report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e1.getMessage(), e1));
		}

		return report;
	}

	private XMLParserJob createJob(IXMLConsistencyInspector checker, IParsingBuffer side)
		throws ParserConfigurationException, SAXException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		XMLParserJob job = new XMLParserJob("", saxParser, checker, side); //$NON-NLS-1$
		job.setUser(true);
		job.setPriority(Job.INTERACTIVE);

		return job;
	}

}
