/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Jun 6, 2016 4:46:33 PM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * This is Event class to notify Radix changed
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class RadixChangeEvent
{
	private ERadix radix;

	/**
	 * Default constructor
	 *
	 * @param radix
	 *
	 */
	public RadixChangeEvent(ERadix radix)
	{
		this.radix = radix;
	}

	/**
	 * @return the radix
	 */
	public ERadix getRadix()
	{
		return radix;
	}

	/*
	 * Overrides toString to provide information about new radix
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String string = super.toString();
		return string.substring(0, string.length() - 1) // remove trailing '}'
			+ " New Radix=" + radix.getLiteral() //$NON-NLS-1$
			+ "}"; //$NON-NLS-1$
	}
}
