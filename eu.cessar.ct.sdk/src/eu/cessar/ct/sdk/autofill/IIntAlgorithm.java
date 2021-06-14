/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 10, 2013 11:20:18 AM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import java.math.BigInteger;

/**
 * Algorithm dealing with {@link BigInteger}
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:11:34 2014 %
 *
 *         %version: 1 %
 */
public interface IIntAlgorithm extends IAlgorithm<BigInteger>
{
	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.IAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.gecucparameterdef.GConfigParameter)
	 */
	@Override
	public BigInteger getValue(GContainer container, GReferrable parameterDefinition);
}
