package com.continental.license.internal.core;

/**
 * Enum which describes the license permission
 */
public enum Permission
{
	NO, YES;

	@Override
	public String toString()
	{
		String str = null;
		if (this == YES)
		{
			str = "Yes";
		}
		else
		{
			str = "No";
		}
		return str;
	}
}
