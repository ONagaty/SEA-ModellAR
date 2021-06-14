/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidv3687<br/>
 * May 16, 2013 11:06:57 AM
 *
 * </copyright>
 */
package eu.cessar.ct.core.security.descriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Class dealing with a metamodel description
 *
 * @author uidv3687
 *
 *         %created_by: uidv3687 %
 *
 *         %date_created: Tue May 28 15:36:09 2013 %
 *
 *         %version: 1 %
 */
public final class MMDescriptor
{

	final Pattern pattern;
	final int numericalID;

	/**
	 * This is intended to map the autosar schema version to autosar release versions. It needs to be updated with the
	 * new version from autosar at each artop/autosar update
	 */
	private static final Map<Integer, String> MAPPED_AUTOSAR_RELEASES = new HashMap<>();
	static
	{
		MAPPED_AUTOSAR_RELEASES.put(444, "4.3.1"); //$NON-NLS-1$
	}

	/**
	 * @param numericalID
	 *        - metamodel id
	 */
	public MMDescriptor(int numericalID)
	{
		pattern = Pattern.compile("^.+" + numericalID + "(\\..+|$)"); //$NON-NLS-1$ //$NON-NLS-2$
		this.numericalID = numericalID;
	}

	/**
	 * Check if the value matches the patern
	 *
	 * @param value
	 * @return true if the value matches
	 */
	public boolean patternMatched(String value)
	{
		return pattern.matcher(value).matches();
	}

	/**
	 * Return true if the version matches the internal version. The provided version can be dot separated
	 *
	 * @param version
	 * @return true if matches
	 */
	public boolean matchesVersion(String version)
	{
		boolean isMapped = false;
		int isInt = 0;
		Set<Entry<Integer, String>> entrySet = MAPPED_AUTOSAR_RELEASES.entrySet();
		for (Entry<Integer, String> entry: entrySet)
		{
			if (version.equals(entry.getValue()))
			{
				isMapped = true;
				isInt = entry.getKey();
			}
		}

		if (isMapped)
		{
			return numericalID == isInt;
		}

		String str = version.replaceAll("\\.", ""); //$NON-NLS-1$//$NON-NLS-2$
		try
		{
			return numericalID == Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

	}

	/**
	 * @return the version stored into this descriptor as a dot separated string
	 */
	public String getVersion()
	{
		String autosarRelease = MAPPED_AUTOSAR_RELEASES.get(numericalID);
		if (autosarRelease == null)
		{
			return (numericalID / 100) + "." + ((numericalID % 100) / 10) + "." //$NON-NLS-1$ //$NON-NLS-2$
				+ ((numericalID % 100) % 10);
		}
		return autosarRelease;
	}
}
