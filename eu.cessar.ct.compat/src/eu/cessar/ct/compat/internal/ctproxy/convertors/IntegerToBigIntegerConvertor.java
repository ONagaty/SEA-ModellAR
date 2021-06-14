/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.convertors;

import java.math.BigInteger;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class IntegerToBigIntegerConvertor implements IDataTypeConvertor<Integer, BigInteger>
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getSlaveClass()
	 */
	public Class<Integer> getSlaveClass()
	{
		return Integer.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getMasterClass()
	 */
	public Class<BigInteger> getMasterClass()
	{
		return BigInteger.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getMasterValue(java.lang.Object)
	 */
	public BigInteger convertToMasterValue(Integer slaveValue)
	{
		if (slaveValue != null)
		{
			return BigInteger.valueOf(slaveValue.longValue());
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getSlaveValue(java.lang.Object)
	 */
	public Integer convertToSlaveValue(BigInteger masterValue)
	{
		if (masterValue != null)
		{
			return new Integer(masterValue.intValue());
		}
		else
		{
			return null;
		}
	}

}
