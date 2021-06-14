/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 4, 2011 5:15:19 PM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.convertors;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class CTProxyConvertorsUtil
{

	private static class CTProxyConvertorEntry<S, M>
	{
		private final Class<S> left;
		private final Class<M> right;

		public CTProxyConvertorEntry(Class<S> left, Class<M> right)
		{
			this.left = left;
			this.right = right;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof CTProxyConvertorEntry)
			{
				CTProxyConvertorEntry<?, ?> other = (CTProxyConvertorEntry<?, ?>) obj;
				if (left == other.left && right == other.right)
				{
					return true;
				}
			}
			return false;
		}
	}

	private static Map<CTProxyConvertorEntry<?, ?>, IDataTypeConvertor<?, ?>> convertors;

	static
	{
		convertors = new HashMap<CTProxyConvertorEntry<?, ?>, IDataTypeConvertor<?, ?>>();
		convertors.put(new CTProxyConvertorEntry<Integer, BigInteger>(Integer.class,
			BigInteger.class), new IntegerToBigIntegerConvertor());
		convertors.put(new CTProxyConvertorEntry<BigInteger, Integer>(BigInteger.class,
			Integer.class), new BigIntegerToIntegerConvertor());
		convertors.put(new CTProxyConvertorEntry<Date, XMLGregorianCalendar>(Date.class,
			XMLGregorianCalendar.class), new DateToXMLGregorianCalendarConvertor());
	}

	/**
	 * @param left
	 * @param right
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <S extends Object, M extends Object> IDataTypeConvertor<S, M> getConvertor(
		Class<S> left, Class<M> right)
	{
		if (left.isEnum() && right.isEnum())
		{
			return (IDataTypeConvertor<S, M>) new EnumToEnumConvertor((Class<? extends Enum>) left,
				(Class<? extends Enum>) right);
		}
		for (CTProxyConvertorEntry<?, ?> entry: convertors.keySet())
		{
			if (entry.left == left && entry.right == right)
			{
				return (IDataTypeConvertor<S, M>) convertors.get(entry);
			}
		}
		return null;
	}
}
