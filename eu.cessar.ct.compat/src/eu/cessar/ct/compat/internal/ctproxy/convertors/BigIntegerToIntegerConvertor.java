/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 7, 2011 9:45:13 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.convertors;

import java.math.BigInteger;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class BigIntegerToIntegerConvertor implements IDataTypeConvertor<BigInteger, Integer>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getSlaveClass()
	 */
	public Class<BigInteger> getSlaveClass()
	{
		return BigInteger.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getMasterClass()
	 */
	public Class<Integer> getMasterClass()
	{
		return Integer.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#convertToMasterValue(java.lang.Object)
	 */
	public Integer convertToMasterValue(BigInteger slaveValue)
	{
		if (slaveValue != null)
		{
			return slaveValue.intValue();
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#convertToSlaveValue(java.lang.Object)
	 */
	public BigInteger convertToSlaveValue(Integer masterValue)
	{
		if (masterValue != null)
		{
			return BigInteger.valueOf(masterValue.longValue());
		}
		else
		{
			return null;
		}
	}

}
