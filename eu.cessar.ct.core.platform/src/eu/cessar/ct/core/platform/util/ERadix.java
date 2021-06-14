/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Mar 25, 2010 11:13:01 AM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.util.regex.Pattern;

/**
 * Enumeration for an integer radix.
 * 
 * @Review uidl6458 - 12.04.2012
 */
public enum ERadix
{

	DECIMAL("Decimal", "-?[0-9]*", 10), // //$NON-NLS-1$ //$NON-NLS-2$
	HEXADECIMAL("Hexadecimal", "[0-9a-fA-F]*", 16), // //$NON-NLS-1$ //$NON-NLS-2$
	BINARY("Binary", "[0-1]*", 2), //$NON-NLS-1$ //$NON-NLS-2$
	OCTAL("Octal", "[0-7]*", 8), ; //$NON-NLS-1$ //$NON-NLS-2$

	private final String literal;
	private final Pattern pattern;
	private final int radixNumber;

	/**
	 * 
	 * @param literal
	 * @param regExp
	 * @param base
	 */
	ERadix(String literal, String regExp, int base)
	{
		this.literal = literal;
		this.pattern = Pattern.compile(regExp);
		this.radixNumber = base;
	}

	/**
	 * Returns the corresponding Pattern object.
	 * 
	 * @return the Pattern object
	 */
	public Pattern getPattern()
	{
		return pattern;
	}

	/**
	 * Returns the corresponding radix as a number.
	 * 
	 * @return
	 */
	public int getRadixNumber()
	{
		return radixNumber;
	}

	/**
	 * Returns the <code>ERadix</code> with the given literal.
	 * 
	 * @param literal
	 *        the given literal
	 * @return the corresponding <code>ERadix</code>
	 */
	public static ERadix getERadixByLiteral(String literal)
	{
		for (ERadix eRadix: ERadix.values())
		{
			if (eRadix.getLiteral().equals(literal))
			{
				return eRadix;
			}
		}
		return ERadix.DECIMAL;
	}

	public static ERadix getERadixByRadixNumber(int radixNumber)
	{
		for (ERadix eRadix: ERadix.values())
		{
			if (eRadix.getRadixNumber() == radixNumber)
			{
				return eRadix;
			}
		}
		return ERadix.DECIMAL;
	}

	/**
	 * Returns the corresponding literal.
	 * 
	 * @return
	 */
	public String getLiteral()
	{
		return literal;
	}

	/**
	 * Returns an array with the literals corresponding to the enumeration
	 * values.
	 * 
	 * @return the array of literals
	 */
	public static String[] stringValues()
	{
		ERadix[] values = values();
		String[] result = new String[values.length];
		for (int i = 0; i < values.length; i++)
		{
			result[i] = values[i].getLiteral();
		}
		return result;
	}

}