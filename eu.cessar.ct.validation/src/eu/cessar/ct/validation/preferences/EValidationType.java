/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jun 20, 2012 3:28:10 PM </copyright>
 */
package eu.cessar.ct.validation.preferences;

/**
 * @author uidt2045
 * 
 */
public enum EValidationType
{
	FULL("FULL"), ON_DEMAND("ON_DEMAND"), NONE("NONE"); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$

	private String name;

	/**
	 * 
	 */
	private EValidationType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public static EValidationType getEValidationTypeFromName(String name)
	{
		if (name == null)
		{
			return null;
		}
		if (name.equals(EValidationType.FULL.getName()))
		{
			return EValidationType.FULL;
		}
		if (name.equals(EValidationType.NONE.getName()))
		{
			return EValidationType.NONE;
		}
		if (name.equals(EValidationType.ON_DEMAND.getName()))
		{
			return EValidationType.ON_DEMAND;
		}
		return null;
	}

}
