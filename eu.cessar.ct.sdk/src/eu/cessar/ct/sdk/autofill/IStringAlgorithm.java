/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 12, 2013 11:13:40 AM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Algorithm dealing with {@link String} values
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:11:35 2014 %
 *
 *         %version: 1 %
 */
public interface IStringAlgorithm extends IAlgorithm<String>
{
	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.IAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.gecucparameterdef.GConfigParameter)
	 */
	@Override
	public String getValue(GContainer container, GReferrable parameterDefinition);
}
