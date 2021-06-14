/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jan 11, 2012 2:50:21 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.attribute;

/**
 * @author uidt2045
 * 
 */
public enum CacheableTypeEnum
{
	NONE, SIMPLE;

	public static CacheableTypeEnum getEnum(String elem)
	{
		if (elem != null && elem.equalsIgnoreCase(CacheableTypeEnum.SIMPLE.name()))
		{
			return CacheableTypeEnum.SIMPLE;
		}
		return CacheableTypeEnum.NONE;
	}
}
