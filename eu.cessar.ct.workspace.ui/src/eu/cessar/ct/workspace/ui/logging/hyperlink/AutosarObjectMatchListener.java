/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jul 24, 2012 4:59:02 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
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
	reqID = "201703")
public class AutosarObjectMatchListener extends AbstractGenericMatchListener
{

	/**
	 * Matches AUTOSAR objects.
	 *
	 * @param loggerConsole
	 *        to add the listener
	 */

	public AutosarObjectMatchListener(LoggerConsole loggerConsole)
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
		return "(ar|pm)@(.*):(.*)::\\[(.*)\\]"; //$NON-NLS-1$
	}

	@Override
	public void matchFound(final PatternMatchEvent event)
	{
		try
		{
			String match = getMatchedText(event);

			if (match != null)
			{
				int matchOffset = 0;
				boolean furtherLinks = true;
				while (furtherLinks)
				{
					String qNameFromString = getQNameFromString(match, matchOffset);
					IProject computeProject = computeProject(match, matchOffset);
					if (qNameFromString != null && computeProject != null)
					{
						int endOfLink = match.indexOf("::", matchOffset); //$NON-NLS-1$

						fConsole.addHyperlink(new AutosarHyperLink(qNameFromString, computeProject), event.getOffset()
							+ endOfLink - qNameFromString.length(), qNameFromString.length());
						matchOffset = endOfLink + 2;
					}
					else
					{
						furtherLinks = false;
					}
				}
			}
		}
		catch (org.eclipse.jface.text.BadLocationException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	@SuppressWarnings("static-method")
	private String getQNameFromString(String value, int startIndex)
	{
		int lastIndex = value.indexOf("::", startIndex); //$NON-NLS-1$
		int firstIndex = value.lastIndexOf(':', lastIndex - 1);
		if (firstIndex > -1 && lastIndex > -1)
		{
			return value.substring(firstIndex + 1, lastIndex);
		}
		return null;
	}

	/**
	 * Will search inside a string and will PREFIX@PROJECT_NAME:QNAME::[TYPE] extract the project with the name
	 * PROJECT_NAME <br>
	 * null if no project was found, or the project is not accessible
	 *
	 * @param value
	 * @return
	 */
	@SuppressWarnings("static-method")
	private IProject computeProject(String value, int beginIndex)
	{
		int startIndex = value.indexOf('@', beginIndex);
		int lastIndex = value.indexOf(':', startIndex);
		if (lastIndex > -1 && startIndex > -1)
		{
			String projectName = value.substring(startIndex + 1, lastIndex);
			final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

			final IProject proj = root.getProject(projectName);
			if (proj != null && proj.isAccessible())
			{
				return proj;
			}
		}
		return null;
	}

	@Override
	public int hashCode()
	{
		return getPattern().hashCode() * 73;
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
