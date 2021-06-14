/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 May 24, 2012 1:49:48 PM </copyright>
 */
package eu.cessar.ct.jet.core.internal.compiler;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.CodeGenPlugin;
import org.eclipse.emf.codegen.jet.JETException;

/**
 * This class and all those in this package is work derived from contributions of multiple authors as listed below.
 * Credit for all that is good is shared, responsibility for any problems lies solely with the latest authors.
 *
 */
public class CessarJETParser
{

	public interface Action
	{
		void execute() throws JETException;
	}

	public static class DelegatingListener implements CessarJETParseEventListener
	{
		protected CessarJETParseEventListener delegate;
		protected CessarJETParser.Action action;

		public DelegatingListener(CessarJETParseEventListener delegate, CessarJETParser.Action action)
		{
			this.delegate = delegate;
			this.action = action;
		}

		public void doAction() throws JETException
		{
			action.execute();
		}

		public void beginPageProcessing() throws JETException
		{
			delegate.beginPageProcessing();
		}

		public void endPageProcessing() throws JETException
		{
			delegate.endPageProcessing();
		}

		public void handleDirective(String directive, CessarJETMark start, CessarJETMark stop, Map<String, String> attrs)
			throws JETException
		{
			doAction();
			delegate.handleDirective(directive, start, stop, attrs);
		}

		public void handleScriptlet(CessarJETMark start, CessarJETMark stop, Map<String, String> attrs)
			throws JETException
		{
			doAction();
			delegate.handleScriptlet(start, stop, attrs);
		}

		public void handleExpression(CessarJETMark start, CessarJETMark stop, Map<String, String> attrs)
			throws JETException
		{
			doAction();
			delegate.handleExpression(start, stop, attrs);
		}

		public void handleCharData(char[] chars) throws JETException
		{
			delegate.handleCharData(chars);
		}
	}

	/**
	 * The input source we read from...
	 */
	protected CessarJETReader reader;

	/**
	 * The backend that is notified of constructs recognized in the input...
	 */
	protected CessarJETParseEventListener listener;

	/*
	 * Char buffer for HTML data
	 */
	protected CharArrayWriter writer;

	protected List<CessarJETCoreElement> coreElements = new ArrayList<>();

	protected String openDirective = "<%@";
	protected String closeDirective = "%>";

	protected String openScriptlet = "<%";
	protected String closeScriptlet = "%>";

	protected String openExpr = "<%=";
	protected String closeExpr = "%>";

	protected String quotedStartTag = "<\\%";
	protected String quotedEndTag = "%\\>";
	protected String startTag = "<%";
	protected String endTag = "%>";

	public CessarJETParser(CessarJETReader reader, final CessarJETParseEventListener parseEventListener,
		CessarJETCoreElement[] coreElements)
	{
		this.reader = reader;
		listener = new DelegatingListener(parseEventListener, new Action()
		{
			public void execute() throws JETException
			{
				CessarJETParser.this.flushCharData();
			}
		});

		writer = new CharArrayWriter();

		for (int i = 0; i < coreElements.length; i++)
		{
			this.coreElements.add(coreElements[i]);
		}
	}

	public CessarJETReader getReader()
	{
		return reader;
	}

	public void setStartTag(String tag)
	{
		openScriptlet = tag;
		openExpr = tag + "=";
		openDirective = tag + "@";
		quotedStartTag = tag.charAt(0) + "\\" + tag.charAt(1);
		startTag = tag;
		reader.setStartTag(tag);
	}

	public void setEndTag(String tag)
	{
		closeScriptlet = tag;
		closeExpr = tag;
		closeDirective = tag;
		quotedEndTag = tag.charAt(0) + "\\" + tag.charAt(1);
		endTag = tag;
		reader.setEndTag(tag);
	}

	public String getOpenScriptlet()
	{
		return openScriptlet;
	}

	public String getCloseScriptlet()
	{
		return closeScriptlet;
	}

	public String getOpenExpr()
	{
		return openExpr;
	}

	public String getCloseExpr()
	{
		return closeExpr;
	}

	public String getOpenDirective()
	{
		return openDirective;
	}

	public String getCloseDirective()
	{
		return closeDirective;
	}

	public String getQuotedStartTag()
	{
		return quotedStartTag;
	}

	public String getQuotedEndTag()
	{
		return quotedEndTag;
	}

	public String getStartTag()
	{
		return startTag;
	}

	public String getEndTag()
	{
		return endTag;
	}

	public static class Scriptlet implements CessarJETCoreElement
	{
		public boolean accept(CessarJETParseEventListener listener, CessarJETReader reader, CessarJETParser parser)
			throws JETException
		{
			String close, open;

			if (reader.matches(parser.getOpenScriptlet()))
			{
				open = parser.getOpenScriptlet();
				close = parser.getCloseScriptlet();
			}
			else
			{
				return false;
			}

			reader.advance(open.length());

			CessarJETMark start = reader.mark();
			CessarJETMark stop = reader.skipUntil(close);
			if (stop == null)
			{
				throw new JETException(CodeGenPlugin.getPlugin().getString("jet.error.unterminated",
					new Object[] {open, reader.mark().toString()}));
			}
			listener.handleScriptlet(start, stop, null);
			return true;
		}
	}

	public static class Expression implements CessarJETCoreElement
	{
		public boolean accept(CessarJETParseEventListener listener, CessarJETReader reader, CessarJETParser parser)
			throws JETException
		{
			String close, open;
			Map<String, String> attrs = null;

			if (reader.matches(parser.getOpenExpr()))
			{
				open = parser.getOpenExpr();
				close = parser.getCloseExpr();
			}
			else
			{
				return false;
			}

			reader.advance(open.length());

			CessarJETMark start = reader.mark();
			CessarJETMark stop = reader.skipUntil(close);
			if (stop == null)
			{
				throw new JETException(CodeGenPlugin.getPlugin().getString("jet.error.unterminated",
					new Object[] {open, reader.mark().toString()}));
			}
			listener.handleExpression(start, stop, attrs);
			return true;
		}
	}

	/**
	 * Quoting in template text. Entities &apos; and &quote;
	 */
	public static class QuoteEscape implements CessarJETCoreElement
	{
		/**
		 * constants for escapes
		 */
		protected static final String APOS = "&apos;";
		protected static final String QUOTE = "&quote;";

		public boolean accept(CessarJETParseEventListener listener, CessarJETReader reader, CessarJETParser parser)
			throws JETException
		{
			try
			{
				if (reader.matches(parser.getQuotedEndTag()))
				{
					reader.advance(parser.getQuotedEndTag().length());
					parser.writer.write(parser.getEndTag());
					parser.flushCharData();
					return true;
				}
				else if (reader.matches(APOS))
				{
					reader.advance(APOS.length());
					parser.writer.write("\'");
					parser.flushCharData();
					return true;
				}
				else if (reader.matches(QUOTE))
				{
					reader.advance(QUOTE.length());
					parser.writer.write("\"");
					parser.flushCharData();
					return true;
				}
			}
			catch (IOException exception)
			{
				throw new JETException(exception);
			}
			return false;
		}
	}

	public static class Directive implements CessarJETCoreElement
	{
		protected Collection<String> directives = new ArrayList<>();

		public boolean accept(CessarJETParseEventListener listener, CessarJETReader reader, CessarJETParser parser)
			throws JETException
		{
			if (reader.matches(parser.getOpenDirective()))
			{
				CessarJETMark start = reader.mark();
				reader.advance(parser.getOpenDirective().length());
				reader.skipSpaces();

				// Check which directive it is.
				//
				String match = null;

				for (String directive: directives)
				{
					if (reader.matches(directive))
					{
						match = directive;
						break;
					}
				}

				if (match == null)
				{
					throw new JETException(CodeGenPlugin.getPlugin().getString("jet.error.bad.directive",
						new Object[] {start.format("jet.mark.file.line.column")}));
					// reader.reset(start);
					// return false;
				}

				reader.advance(match.length());

				// Parse the attr-val pairs.
				//
				Map<String, String> attrs = reader.parseTagAttributes();

				// Match close.
				reader.skipSpaces();
				if (!reader.matches(parser.getCloseDirective()))
				{
					throw new JETException(CodeGenPlugin.getPlugin().getString("jet.error.unterminated",
						new Object[] {parser.getOpenDirective(), reader.mark().toString()}));
				}
				else
				{
					reader.advance(parser.getCloseDirective().length());
				}

				CessarJETMark stop = reader.mark();
				listener.handleDirective(match, start, stop, attrs);
				return true;
			}
			else
			{
				return false;
			}
		}

		public Collection<String> getDirectives()
		{
			return directives;
		}

	}

	protected void flushCharData() throws JETException
	{
		char[] array = writer.toCharArray();
		if (array.length != 0) // Avoid unnecessary out.write("") statements...
		{
			listener.handleCharData(writer.toCharArray());
		}
		writer = new CharArrayWriter();
	}

	public void parse() throws JETException
	{
		parse(null);
	}

	public void parse(String until) throws JETException
	{
		parse(until, null);
	}

	public void parse(String until, Class<?>[] accept) throws JETException
	{
		while (reader.hasMoreInput())
		{
			if (until != null && reader.matches(until))
			{
				return;
			}

			Iterator<CessarJETCoreElement> e = coreElements.iterator();

			if (accept != null)
			{
				List<CessarJETCoreElement> v = new ArrayList<>();
				while (e.hasNext())
				{
					CessarJETCoreElement c = e.next();
					for (int i = 0; i < accept.length; i++)
					{
						if (c.getClass().equals(accept[i]))
						{
							v.add(c);
						}
					}
				}
				e = v.iterator();
			}

			boolean accepted = false;
			while (e.hasNext())
			{
				CessarJETCoreElement c = e.next();
				reader.mark();
				if (c.accept(listener, reader, this))
				{
					accepted = true;
					break;
				}
			}
			if (!accepted)
			{
				String s = reader.nextContent();
				writer.write(s, 0, s.length());
			}
		}
		flushCharData();
	}
}
