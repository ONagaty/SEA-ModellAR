/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 14, 2010 4:03:10 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.math.BigInteger;

/**
 * Utility class dealing with {@link BigInteger}
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 12.04.2012
 * 
 */
@SuppressWarnings("javadoc")
public final class BigIntegerUtils
{

	public static final BigInteger MAX_SIGNED_INT = BigInteger.valueOf(Integer.MAX_VALUE);
	public static final BigInteger MIN_SIGNED_INT = BigInteger.valueOf(Integer.MIN_VALUE);

	public static final BigInteger MAX_UNSIGNED_INT = BigInteger.valueOf(0xFFFFFFFFL);
	public static final BigInteger MIN_UNSIGNED_INT = BigInteger.ZERO;

	public static final BigInteger MAX_SIGNED_LONG = BigInteger.valueOf(Long.MAX_VALUE);
	public static final BigInteger MIN_SIGNED_LONG = BigInteger.valueOf(Long.MIN_VALUE);

	public static final BigInteger MAX_UNSIGNED_LONG = new BigInteger("FFFFFFFFFFFFFFFF", 16); //$NON-NLS-1$
	public static final BigInteger MIN_UNSIGNED_LONG = BigInteger.ZERO;

	private BigIntegerUtils()
	{
		// never call constructor
	}

	/**
	 * Return the integer value stored into the <code>value</code> restricted to the boundary specified by
	 * {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}
	 * 
	 * @param value
	 * @return the integer value
	 */
	public static int getRestrictedToInt(BigInteger value)
	{
		if (MAX_SIGNED_INT.compareTo(value) < 0)
		{
			return Integer.MAX_VALUE;
		}
		else if (MIN_SIGNED_INT.compareTo(value) > 0)
		{
			return Integer.MIN_VALUE;
		}
		else
		{
			return value.intValue();
		}
	}

	/**
	 * Return the long value stored into the <code>value</code> restricted to the boundary specified by
	 * {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE}
	 * 
	 * @param value
	 * @return the long value
	 */
	public static long getRestrictedToLong(BigInteger value)
	{
		if (MAX_SIGNED_LONG.compareTo(value) < 0)
		{
			return Long.MAX_VALUE;
		}
		else if (MIN_SIGNED_LONG.compareTo(value) > 0)
		{
			return Long.MIN_VALUE;
		}
		else
		{
			return value.longValue();
		}
	}
}
