/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 25.05.2012 16:12:10 </copyright>
 */
package eu.cessar.ct.core.platform.util;

/**
 * Utility class dealing with {@link Double}
 * 
 * @author uidl6870
 * 
 */
public final class DoubleUtils
{

	@SuppressWarnings("javadoc")
	public static final String POSITIVE_INFINITY_AS_STRING = "INF"; //$NON-NLS-1$

	@SuppressWarnings("javadoc")
	public static final String NEGATIVE_INFINITY_AS_STRING = "-INF"; //$NON-NLS-1$

	private DoubleUtils()
	{
		// never create this kind of object
	}

	/**
	 * Returns a string representation of the given Double <code>value</code>. The only difference towards the
	 * {@link Double#toString()} implementation, is that for the special values: <li>{@link Double#NEGATIVE_INFINITY}</li>
	 * <li>{@link Double#POSITIVE_INFINITY}</li> <br>
	 * <code>-INF</code> and respectively <code>INF</code> is returned.
	 * 
	 * @param value
	 *        Double value
	 * @return a string representation of the given Double
	 */
	public static String convertToString(Double value)
	{
		if (value == Double.NEGATIVE_INFINITY)
		{
			return NEGATIVE_INFINITY_AS_STRING;
		}
		else if (value == Double.POSITIVE_INFINITY)
		{
			return POSITIVE_INFINITY_AS_STRING;
		}
		else
		{
			return String.valueOf(value);
		}
	}

	/**
	 * Returns a {@link Double} corresponding to the given string representation. <b>-INF</b> and <b>INF</b> are also
	 * accepted in order to obtain {@link Double#NEGATIVE_INFINITY}, respectively {@link Double#POSITIVE_INFINITY}
	 * 
	 * 
	 * @param string
	 *        string representation to be converted to a {@link Double}
	 * @return a {@link Double}
	 * @throws NumberFormatException
	 */
	public static Double convertFromString(String string) throws NumberFormatException
	{
		if (string == null || "".equals(string)) //$NON-NLS-1$
		{
			return null;
		}
		else if (string.equalsIgnoreCase(NEGATIVE_INFINITY_AS_STRING))
		{
			return Double.NEGATIVE_INFINITY;
		}
		else if (string.equalsIgnoreCase(POSITIVE_INFINITY_AS_STRING))
		{
			return Double.POSITIVE_INFINITY;
		}
		else
		{
			return new Double(string);
		}
	}
}
