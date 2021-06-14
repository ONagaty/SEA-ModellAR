package com.continental.license.internal.core;

import java.text.DateFormat;
import java.util.Calendar;

public class ExpDate
{
	private static final int BASE_YEAR = 2000;
	private static final int MAX_YEAR = 31;
	private static final int MAX_YEAR_MODULO = 255;

	private int day;
	private int month;
	private int year;
	private int yearModulo;
	private Calendar calendar;

	public ExpDate()
	{
		// empty constructor
	}

	public ExpDate(Calendar calendar)
	{

		this.calendar = calendar;
		if (calendar != null)
			setValues(calendar);
	}

	public ExpDate(int aYear, int aMonth, int aDay, int aYearModulo) throws NumberFormatException
	{
		if ((aYear == 0) && (aMonth == 0) && (aDay == 0) && (aYearModulo == 0))
			return;

		setValues(aDay, aMonth, aYear, aYearModulo);
		setCalendarWithValues(aDay, aMonth, aYear, aYearModulo);
	}

	private void setValues(Calendar aCalendar) throws NumberFormatException
	{

		int iDay = aCalendar.get(Calendar.DAY_OF_MONTH);
		int iMonth = aCalendar.get(Calendar.MONTH);
		int tmpYear = aCalendar.get(Calendar.YEAR);
		int iYear = (tmpYear - BASE_YEAR) % MAX_YEAR;
		int iYearModulo = (tmpYear - BASE_YEAR) / MAX_YEAR;
		setValues(iDay, iMonth, iYear, iYearModulo);
	}

	private void setValues(int aDay, int aMonth, int aYear, int aYearModulo)
		throws NumberFormatException
	{

		if ((aYear < 0) || (aYear > MAX_YEAR))
			throw new NumberFormatException("illegal year");
		if ((aYearModulo < 0) || (aYearModulo > MAX_YEAR_MODULO))
			throw new NumberFormatException("illegal year");
		if ((aMonth < 0) || (aMonth > 11))
			throw new NumberFormatException("illegal month");
		if ((aDay < 1) || (aDay > 31))
			throw new NumberFormatException("illegal day");

		day = aDay;
		month = aMonth;
		year = aYear;
		yearModulo = aYearModulo;
	}

	private void setCalendarWithValues(int aDay, int aMonth, int aYear, int aYearModulo)
	{
		
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, aDay);
		calendar.set(Calendar.MONTH, aMonth);
		calendar.set(Calendar.YEAR, (BASE_YEAR + aYear + (aYearModulo * MAX_YEAR)));
	}

	public boolean isDefined()
	{
		
		return calendar != null;
	}

	public Calendar getCalendar()
	{

		return calendar;
	}

	public int yearModulo()
	{

		return yearModulo;
	}

	public int year()
	{

		return year;
	}

	public int month()
	{

		return month;
	}

	public int day()
	{

		return day;
	}
	
	@Override
	public String toString()
	{
		String str = "-";
		if (calendar != null) {
			str = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
		}
		return str;
	}
	
	/**
	 * Return true if the date is expired.
	 * @return true if the date is expired
	 */
	public boolean expired() {
		
		return Calendar.getInstance().after(calendar);
	}
}
