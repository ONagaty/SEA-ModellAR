/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uid94246 Oct 16, 2009 12:38:31 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.util.Arrays;

/**
 * Various constants that are well known C and Java keywords
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 18.04.2012
 * 
 */
public final class Keywords
{
	// ANSI C KEYWORDS

	@SuppressWarnings("nls")
	private static final String[] ANSI_C_KEYWORDS = {"auto", "break", "case", "char", "const",
		"continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto",
		"if", "int", "long", "register", "return", "short", "signed", "sizeof", "static", "struct",
		"switch", "typedef", "union", "unsigned", "void", "volatile", "while",};

	@SuppressWarnings("nls")
	private static final String[] ANSI_C_CONSTANTS = {"false", "NULL", "true"};

	// JAVA KEYWORKDS

	@SuppressWarnings("nls")
	private static final String[] JAVA_KEYWORDS = {"abstract", "assert", "break", "case", "catch",
		"class", "const", "continue", "default", "do", "else", "extends", "final", "finally",
		"for", "goto", "if", "implements", "import", "instanceof", "interface", "native", "new",
		"package", "private", "protected", "public", "return", "static", "strictfp", "super",
		"switch", "synchronized", "this", "throw", "throws", "transient", "try", "volatile",
		"while",};

	@SuppressWarnings("nls")
	private static final String[] JAVA_TYPES = {"boolean", "byte", "char", "double", "enum",
		"float", "int", "long", "short", "void",};

	@SuppressWarnings("nls")
	private static final String[] JAVA_CONSTANTS = {"false", "null", "true"};

	private Keywords()
	{
		// do not inherit this class
	}

	public static String[] getJavaConstants()
	{
		return JAVA_CONSTANTS;
	}

	public static String[] getJavaKeywords()
	{
		return JAVA_KEYWORDS;
	}

	public static String[] getJavaTypes()
	{
		return JAVA_TYPES;
	}

	public static String[] getANSICKeywords()
	{
		return ANSI_C_KEYWORDS;
	}

	public static String[] getANSICConstants()
	{
		return ANSI_C_CONSTANTS;
	}

	public static boolean isJavaReservedWord(String word)
	{
		String toFind = word.toLowerCase();
		if (Arrays.binarySearch(JAVA_KEYWORDS, toFind) >= 0)
		{
			return true;
		}
		if (Arrays.binarySearch(JAVA_TYPES, toFind) >= 0)
		{
			return true;
		}
		if (Arrays.binarySearch(JAVA_CONSTANTS, toFind) >= 0)
		{
			return true;
		}
		return false;
	}
}
