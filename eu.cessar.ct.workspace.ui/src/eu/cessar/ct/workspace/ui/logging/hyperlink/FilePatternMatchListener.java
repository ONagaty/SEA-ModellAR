/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jul 24, 2012 3:43:10 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import java.io.File;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.IPatternMatchListener;
import org.eclipse.ui.console.PatternMatchEvent;

import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.logging.LoggerConsole;
import eu.cessar.req.Requirement;

/**
 * @author uidu0944
 *
 */
@Requirement(
	reqID = "201709")
public class FilePatternMatchListener extends AbstractGenericMatchListener
{

	/**
	 * Matches .*jet files.
	 *
	 * @param loggerConsole
	 *        to add the listener
	 */
	public FilePatternMatchListener(LoggerConsole loggerConsole)
	{
		super(loggerConsole);
	}

	@Override
	public int getCompilerFlags()
	{
		return Pattern.CASE_INSENSITIVE;
	}

	@Override
	public String getLineQualifier()
	{
		return null;
	}

	@Override
	public String getPattern()
	{
		return "\"(.*?)jet\""; //$NON-NLS-1$
	}

	@Override
	public void matchFound(final PatternMatchEvent event)
	{
		try
		{
			String match = getMatchedText(event);
			// +2 to match path/path/filename and filename
			String fileLocation = match.substring(match.lastIndexOf(File.separatorChar) + 2, match.length() - 1);
			fConsole.addHyperlink(new FileHyperLink(fileLocation), event.getOffset(), event.getLength());
		}
		catch (BadLocationException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	@Override
	public int hashCode()
	{
		return getPattern().hashCode() * 71;
	}

	@Override
	public boolean equals(final Object o)
	{
		boolean result = false;
		if (o instanceof IPatternMatchListener)
		{
			IPatternMatchListener other = (IPatternMatchListener) o;
			result = getPattern().equals(other.getPattern());
		}
		return result;
	}

	private String getMatchedText(PatternMatchEvent event)
	{
		int eventOffset = event.getOffset();
		int eventLength = event.getLength();
		IDocument document = fConsole.getDocument();
		String matchedText = null;
		try
		{
			matchedText = document.get(eventOffset, eventLength);
		}
		catch (BadLocationException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return matchedText;
	}
}
