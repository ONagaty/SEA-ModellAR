/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Dec 16, 2010 4:59:18 PM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.convertors;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class DateToXMLGregorianCalendarConvertor implements
	IDataTypeConvertor<Date, XMLGregorianCalendar>
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getSlaveClass()
	 */
	public Class<Date> getSlaveClass()
	{
		return Date.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getMasterClass()
	 */
	public Class<XMLGregorianCalendar> getMasterClass()
	{
		return XMLGregorianCalendar.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#convertToMasterValue(java.lang.Object)
	 */
	public XMLGregorianCalendar convertToMasterValue(Date slaveValue)
	{
		if (slaveValue != null)
		{
			Calendar slaveCalendar = Calendar.getInstance();
			slaveCalendar.setTime(slaveValue);

			DatatypeFactory newInstance;
			XMLGregorianCalendar xmlGregorianCalendar = null;
			try
			{
				newInstance = DatatypeFactory.newInstance();
				if (newInstance != null)
				{
					xmlGregorianCalendar = newInstance.newXMLGregorianCalendar(new GregorianCalendar());
					xmlGregorianCalendar.setDay(slaveCalendar.get(Calendar.DAY_OF_MONTH));
					xmlGregorianCalendar.setMonth(slaveCalendar.get(Calendar.MONTH) + 1);
					xmlGregorianCalendar.setYear(slaveCalendar.get(Calendar.YEAR));
					xmlGregorianCalendar.setTime(slaveCalendar.get(Calendar.HOUR_OF_DAY),
						slaveCalendar.get(Calendar.MINUTE), slaveCalendar.get(Calendar.SECOND));
					return xmlGregorianCalendar;
				}
			}
			catch (DatatypeConfigurationException e)
			{
				return null;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.mm21.internal.ctproxy.convertors.IDataTypeConvertor#getSlaveValue(java.lang.Object)
	 */
	public Date convertToSlaveValue(XMLGregorianCalendar masterValue)
	{
		if (masterValue != null)
		{
			Calendar slaveCalendar = Calendar.getInstance();
			slaveCalendar.set(masterValue.getYear(), masterValue.getMonth() - 1,
				masterValue.getDay(), masterValue.getHour(), masterValue.getMinute(),
				masterValue.getSecond());
			return slaveCalendar.getTime();
		}
		return null;
	}

}
