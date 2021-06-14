/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 21, 2009 12:11:32 PM </copyright>
 */
package eu.cessar.ct.core.platform;

/**
 * A representation of a project's configuration variant
 * 
 * @Review uidl6458 - 12.04.2012
 * 
 */
public enum EProjectVariant
{
	/**
	 * Development phase replaces OTHER phase
	 */
	DEVELOPMENT,
	/**
	 * Production phase, replaces PRE_COMPILE, LINK_TIME and POST_BUILD phases
	 */
	PRODUCTION;

	/**
	 * @return String values
	 */
	public static String[] stringValues()
	{
		EProjectVariant[] values = values();
		String[] result = new String[values.length];
		for (int i = 0; i < values.length; i++)
		{
			result[i] = values[i].name();
		}
		return result;
	}

	/**
	 * Transforms the String value of the preference 'key' from the project preferences in a enum value and if necessary
	 * transforms the old enum values to new enum values as follows: OTHER becomes DEVELOPMENT, all other phases
	 * PRE_COMPILE, LINK_TIME, POST_BUILD become PRODUCTION.
	 * 
	 * @param projectVariant
	 * @return enum value corresponding to the old String preference value
	 */
	public static EProjectVariant getProjectVariant(String projectVariant)
	{
		String oldValue = "OTHER"; //$NON-NLS-1$
		String newValue = DEVELOPMENT.toString();
		if ((oldValue.equals(projectVariant)) || (newValue.equals(projectVariant)))
		{
			return DEVELOPMENT;
		}
		else
		{
			String production = PRODUCTION.toString();
			String preCompile = "PRE_COMPILE"; //$NON-NLS-1$
			String post_build = "POST_BUILD"; //$NON-NLS-1$
			String linkTime = "LINK_TIME"; //$NON-NLS-1$
			if (production.equals(projectVariant) || preCompile.equals(projectVariant)
				|| linkTime.equals(projectVariant) || post_build.equals(projectVariant))
			{
				return PRODUCTION;
			}
			return DEVELOPMENT;
		}

	}
}
