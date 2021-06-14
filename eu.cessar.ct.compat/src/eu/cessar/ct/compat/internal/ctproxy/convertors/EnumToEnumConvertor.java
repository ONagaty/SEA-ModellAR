/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 7, 2011 11:16:15 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.convertors;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
@SuppressWarnings("rawtypes")
public class EnumToEnumConvertor implements IDataTypeConvertor<Enum, Enum>
{

	private final Class<? extends Enum> slaveEnumClass;
	private final Class<? extends Enum> masterEnumClass;

	public EnumToEnumConvertor(Class<? extends Enum> slaveEnumClass,
		Class<? extends Enum> masterEnumClass)
	{
		this.slaveEnumClass = slaveEnumClass;
		this.masterEnumClass = masterEnumClass;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getSlaveClass()
	 */
	public Class<? extends Enum> getSlaveClass()
	{
		return slaveEnumClass;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getMasterClass()
	 */
	public Class<? extends Enum> getMasterClass()
	{
		return masterEnumClass;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#convertToMasterValue(java.lang.Object)
	 */
	public Enum convertToMasterValue(Enum slaveValue)
	{
		Enum[] constants = masterEnumClass.getEnumConstants();
		for (Enum enum1: constants)
		{
			if (slaveValue.name().equals(enum1.name()))
			{
				return enum1;
			}
			// toString() may be overridden to return the literal
			if (slaveValue.toString().equals(enum1.toString()))
			{
				return enum1;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#convertToSlaveValue(java.lang.Object)
	 */
	public Enum convertToSlaveValue(Enum masterValue)
	{
		Enum[] constants = slaveEnumClass.getEnumConstants();
		for (Enum enum1: constants)
		{
			if (masterValue.name().equals(enum1.name()))
			{
				return enum1;
			}
			// toString() may be overridden to return the literal
			if (masterValue.toString().equals(enum1.toString()))
			{
				return enum1;
			}
		}
		return null;
	}

}
