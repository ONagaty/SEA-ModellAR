/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jul 25, 2012 2:32:17 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.console.PatternMatchEvent;

import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.logging.LoggerConsole;
import eu.cessar.req.Requirement;

/**
 * @author uidu0944
 *
 */
@Requirement(
	reqID = "201707")
public class StackTraceMatchListener extends AbstractGenericMatchListener
{
	/**
	 * Matches stack trace output.
	 *
	 * @param console
	 *        to add the listener
	 */
	public StackTraceMatchListener(LoggerConsole console)
	{
		super(console);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.console.IPatternMatchListener#matchFound(org.eclipse.ui.console.PatternMatchEvent)
	 */
	@Override
	public void matchFound(PatternMatchEvent event)
	{
		String match = getMatchedText(event);
		if (isJavaExtension(match))
		{
			try
			{
				fConsole.addHyperlink(new StackTraceHyperLink(getProject(), match), event.getOffset(),
					event.getLength());
			}
			catch (BadLocationException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
	}

	@SuppressWarnings("static-method")
	private boolean isJavaExtension(String match)
	{
		int dotIndex = match.lastIndexOf('.');
		int colonIndex = match.lastIndexOf(':');
		if (dotIndex > 0 && colonIndex > 0 && dotIndex < colonIndex)
		{
			String ext = match.substring(dotIndex, colonIndex);
			return ".java".equals(ext); //$NON-NLS-1$
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.console.IPatternMatchListener#getPattern()
	 */
	@Override
	public String getPattern()
	{
		return "\\(([a-zA-Z0-9_]*\\.((java)|(cjet)|(hjet)):\\d+)\\)"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.console.IPatternMatchListener#getCompilerFlags()
	 */
	@Override
	public int getCompilerFlags()
	{
		return Pattern.DOTALL;
	}

	private String getMatchedText(PatternMatchEvent event)
	{
		String lineText = null;
		int eventOffset = event.getOffset();
		IDocument document = fConsole.getDocument();
		try
		{
			IRegion reg = document.getLineInformation(document.getLineOfOffset(eventOffset));
			lineText = document.get(reg.getOffset(), reg.getLength());
		}
		catch (BadLocationException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return lineText;
	}

}
