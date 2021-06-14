/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 10, 2013 5:31:47 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Algorithm dealing with {@link Double} values
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:11:33 2014 %
 *
 *         %version: 1 %
 */
public interface IFloatAlgorithm extends IAlgorithm<Double>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.IAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.gecucparameterdef.GConfigParameter)
	 */
	@Override
	public Double getValue(GContainer container, GReferrable parameterDefinition);
}
