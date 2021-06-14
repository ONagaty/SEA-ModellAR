/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jul 17, 2009 10:44:08 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author uidl6870
 * 
 */
public class DateTimeLabelProvider extends LabelProvider
{

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element)
	{
		// TODO Auto-generated method stub
		return super.getImage(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element)
	{
		if (element instanceof XMLGregorianCalendar)
		{
			XMLGregorianCalendar calendar = (XMLGregorianCalendar) element;
			// int editorValue[] = (int[]) element;
			int hour = calendar.getHour();
			int minutes = calendar.getMinute();
			int seconds = calendar.getSecond();

			int month = calendar.getMonth();
			int year = calendar.getYear();
			int day = calendar.getDay();
			return getDateAsString(day, month, year) + " " //$NON-NLS-1$
				+ getTimeAsString(hour, minutes, seconds);

		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	private String getDateAsString(int day, int month, int year)
	{

		return day + " " + month + " " + year; //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * @param hour
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	private String getTimeAsString(int hour, int minutes, int seconds)
	{
		return hour + ":" + minutes + ":" + seconds; //$NON-NLS-1$//$NON-NLS-2$
	}
}
