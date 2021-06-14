/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 13, 2013 10:24:08 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea;

import eu.cessar.ct.core.platform.util.StringUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import org.eclipse.core.runtime.Assert;

/**
 * {@link IGIdentifiableFilter} implementation that accepts elements whose shortName match the filter's one.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Aug 21 10:05:15 2013 %
 * 
 *         %version: 1 %
 */
public class GIdentifiableFilterByName implements IGIdentifiableFilter
{
	private final String shortName;
	private final boolean ignoreCase;

	/**
	 * @param shortName
	 *        name to compare against
	 * @param ignoreCase
	 *        case insensitive or not
	 */
	public GIdentifiableFilterByName(String shortName, boolean ignoreCase)
	{
		Assert.isNotNull(shortName);

		this.shortName = shortName;
		this.ignoreCase = ignoreCase;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaFilter#isPassingFilter(gautosar.ggenericstructure.ginfrastructure.
	 * GIdentifiable)
	 */
	public boolean isPassingFilter(GIdentifiable identifiable)
	{
		Assert.isNotNull(identifiable);

		String name = identifiable.gGetShortName();
		return StringUtils.equals(shortName, name, ignoreCase);
	}

}
