/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 3:26:59 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various string related utilities
 * 
 * @Review uidl6458 - 18.04.2012
 */
public final class StringUtils
{
	private static final String REGEXP_ESCAPES = ".\\?*+&:{}[]()^$"; //$NON-NLS-1$

	private StringUtils()
	{
		// avoid instance
	}

	/**
	 * Return the argument with the first letter converted to upper case
	 * 
	 * @param value
	 * @return
	 */
	public static String toTitleCase(String value)
	{
		if (value.length() == 0)
		{
			return value;
		}
		else
		{
			char first = Character.toUpperCase(value.charAt(0));
			if (value.length() > 1)
			{
				return first + value.substring(1);
			}
			else
			{
				return String.valueOf(first);
			}
		}
	}

	/**
	 * Concatenate the <code>values</code> using <code>separator</code> as separator
	 * 
	 * @param qName
	 * @param dot
	 * @return
	 */
	public static String concat(String[] values, String separator)
	{
		if (values.length == 0)
		{
			return ""; //$NON-NLS-1$
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length - 1; i++)
		{
			sb.append(values[i]);
			sb.append(separator);
		}
		sb.append(values[values.length - 1]);
		return sb.toString();
	}

	/**
	 * Concatenates the <code>values</code> using '_' as separator.
	 * 
	 * @param separator
	 * @param values
	 * @return
	 */
	public static String concatBy_(String... values)
	{
		return StringUtils.concat(values, "_"); //$NON-NLS-1$
	}

	/**
	 * Split a string in multiple strings by looking at the upper case / lower case variations
	 * 
	 * @param word
	 * @return
	 */
	public static String[] splitCamelCase(String word)
	{
		Pattern pattern = Pattern.compile("([A-Z]|[a-z])[a-z]*"); //$NON-NLS-1$
		List<String> tokens = new ArrayList<String>();
		Matcher matcher = pattern.matcher(word);
		String acronym = ""; //$NON-NLS-1$
		while (matcher.find())
		{
			String found = matcher.group();
			if (found.matches("^[A-Z]$")) //$NON-NLS-1$
			{
				acronym += found;
			}
			else
			{
				if (acronym.length() > 0)
				{
					// we have an acronym to add before we continue
					tokens.add(acronym);
					acronym = ""; //$NON-NLS-1$
				}
				tokens.add(found);
			}
		}
		if (acronym.length() > 0)
		{
			tokens.add(acronym);
		}
		return tokens.toArray(new String[tokens.size()]);
	}

	/**
	 * Return a new string made from the camel splited words found in <code>word</code> separated by space
	 * 
	 * @param word
	 * @return
	 */
	public static String humaniseCamelCase(String word)
	{
		String[] tokens = splitCamelCase(word);
		if (tokens.length > 0)
		{
			StringBuilder buffer = new StringBuilder(word.length() + tokens.length - 1);
			buffer.append(tokens[0]);
			for (int i = 1; i < tokens.length; i++)
			{
				buffer.append(' ');
				buffer.append(tokens[i]);
			}
			return buffer.toString();
		}
		return word;
	}

	/**
	 * Convert and XML Class name to a Java class name:
	 * 
	 * <pre>
	 * CLIENT--SERVER--OPERATION =&gt; ClientServerOperation
	 * SW-COMPONENT-PROTOTYPE =&gt; SwComponentPrototype
	 * I--SIGNAL--TO--I--PDU--MAPPING =&gt;ISignalToIPduMapping
	 * TRIGGER =&gt; Trigger
	 * Trigger =&gt; Trigger
	 * </pre>
	 * 
	 * If there is at least one character inside the input that is neither in upper case nor dash, no transformation
	 * occur
	 * 
	 * @param name
	 * @return
	 */
	public static String convertToClassName(String name)
	{
		if (name == null || name.length() == 0)
		{
			return name;
		}
		StringBuilder result = new StringBuilder(name.length());
		boolean wordStart = true;
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);
			if (Character.isUpperCase(c))
			{
				if (wordStart)
				{
					wordStart = false;
					result.append(c);
				}
				else
				{
					result.append(Character.toLowerCase(c));
				}
			}
			else if (c == '-')
			{
				wordStart = true;
			}
			else
			{
				// not a valid name
				return name;
			}
		}
		return result.toString();
	}

	/**
	 * This method converts the characters that are not java identifiers( " ` " , " . ",etc ) into "_" and also converts
	 * the first letter, of the given string, into UpperCase after checking if it can be used as the first character
	 * 
	 * @param segmentName
	 * @return result
	 */
	public static String convertToJavaIdentifier(String segmentName)
	{
		int length = segmentName.length();
		if (length == 0)
		{
			return segmentName;
		}
		StringBuilder result = new StringBuilder(length);
		char charAt = segmentName.charAt(0);
		if (Character.isJavaIdentifierStart(charAt))
		{
			charAt = Character.toUpperCase(charAt);
			result.append(charAt);
		}
		else
		{

			result.append('_');
		}
		for (int i = 1; i < length; i++)
		{
			char c = segmentName.charAt(i);
			if (Character.isJavaIdentifierPart(c))
			{

				result.append(c);
			}
			else
			{

				result.append('_');
			}

		}
		return result.toString();
	}

	/**
	 * Replace characters having special meaning in regular expressions with their escaped equivalents. Do not escape
	 * the * and ? characters
	 * 
	 * @param aRegexFragment
	 * @return
	 */
	public static String escapeRegexForSearch(String aRegexFragment)
	{
		final StringBuilder result = new StringBuilder(aRegexFragment.length());

		final StringCharacterIterator iterator = new StringCharacterIterator(aRegexFragment);
		char character = iterator.current();
		while (character != CharacterIterator.DONE)
		{

			if (character == '?')
			{
				result.append('.');
			}
			else if (character == '*')
			{
				result.append(".*"); //$NON-NLS-1$
			}
			else
			{
				result.append(escapeRegexChar(character));
			}

			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * Replace characters having special meaning in regular expressions with their escaped equivalents.
	 * 
	 * @param character
	 * @return
	 */
	public static String escapeRegexChar(char character)
	{
		if (REGEXP_ESCAPES.indexOf(character) > -1)
		{
			return "\\" + character; //$NON-NLS-1$
		}
		else
		{
			return "" + character; //$NON-NLS-1$
		}
	}

	/**
	 * Return true if both strings are <code>null</code> or equals in the sense of {@link String#equals(Object)}
	 * 
	 * @param left
	 * @param right
	 * @return {@code true} if the given objects are equivalent, {@code false} otherwise
	 */
	public static boolean equals(String left, String right)
	{
		return equals(left, right, false);
	}

	/**
	 * Return true if both strings are <code>null</code> or equals in the sense of {@link String#equals(Object)} or
	 * {@link String#equalsIgnoreCase(String)}, depending on the specified flag.
	 * 
	 * @param str1
	 * @param str2
	 * @param ignoreCase
	 *        whether equivalent ignoring case
	 * @return {@code true} if the objects are equivalent; {@code false} otherwise
	 */
	public static boolean equals(String str1, String str2, boolean ignoreCase)
	{
		if (str1 == null)
		{
			return (str2 == null);
		}

		if (ignoreCase)
		{
			return str1.equalsIgnoreCase(str2);
		}
		else
		{
			return str1.equals(str2);
		}
	}

	/**
	 * 
	 * Checks if the given <code>str</code> string ends with the specified suffix (optionally case insensitive).
	 * 
	 * @param str
	 *        the string to check, may be null
	 * @param suffix
	 *        the suffix to find, may be null
	 * @param ignoreCase
	 *        indicates whether the compare should ignore case (case insensitive) or not.
	 * @return <code>true</code> if the <code><str</code> string ends with the given suffix or if both strings are
	 *         <code>null</code>, <code>false</code> otherwise
	 */
	public static boolean endsWith(String str, String suffix, boolean ignoreCase)
	{
		if (str == null || suffix == null)
		{
			return (str == null && suffix == null);
		}
		if (suffix.length() > str.length())
		{
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
	}

	/**
	 * Searches "searchTerm" in "content" and returns an array of int pairs (index, length) for each occurrence. The
	 * search is case-sensitive. The consecutive occurrences are merged together.
	 * 
	 * <pre>
	 * 	Examples:
	 * 	 content = "123123x123"
	 * 	 searchTerm = "1"
	 * 	 --> [0, 1, 3, 1, 7, 1]
	 * 	 content = "123123x123"
	 * 	 searchTerm = "123"
	 * 	 --> [0, 6, 7, 3]
	 * </pre>
	 * 
	 * @param searchTerm
	 *        can be null or empty. int[0] is returned in this case!
	 * @param content
	 *        a not-null string (can be empty!)
	 * @return an array of int pairs (index, length)
	 */
	public static int[] getSearchTermOccurrences(final String searchTerm, final String content)
	{
		if (searchTerm == null || searchTerm.length() == 0)
		{
			return new int[0];
		}
		if (content == null)
		{
			throw new IllegalArgumentException("content is null"); //$NON-NLS-1$
		}
		final List<Integer> list = new ArrayList<Integer>();
		int searchTermLength = searchTerm.length();
		int index;
		int fromIndex = 0;
		int lastIndex = -1;
		int lastLength = 0;
		while (true)
		{
			index = content.indexOf(searchTerm, fromIndex);
			if (index == -1)
			{
				// no occurrence of "searchTerm" in "content" starting from
				// index "fromIndex"
				if (lastIndex != -1)
				{
					// but there was a previous occurrence
					list.add(lastIndex);
					list.add(lastLength);
				}
				break;
			}
			if (lastIndex == -1)
			{
				// the first occurrence of "searchTerm" in "content"
				lastIndex = index;
				lastLength = searchTermLength;
			}
			else
			{
				if (lastIndex + lastLength == index)
				{
					// the current occurrence is right after the previous
					// occurrence
					lastLength += searchTermLength;
				}
				else
				{
					// there is at least one character between the current
					// occurrence and the previous one
					list.add(lastIndex);
					list.add(lastLength);
					lastIndex = index;
					lastLength = searchTermLength;
				}
			}
			fromIndex = index + searchTermLength;
		}
		final int[] result = CollectionUtils.toIntArray(list);
		return result;
	}
}
