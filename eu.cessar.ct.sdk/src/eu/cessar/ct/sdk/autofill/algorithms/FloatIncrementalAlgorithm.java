/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 10, 2013 5:32:15 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill.algorithms;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.sdk.autofill.IFloatAlgorithm;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Algorithm that takes an initial value and an increment
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:35:38 2014 %
 *
 *         %version: 1 %
 */
public class FloatIncrementalAlgorithm implements IFloatAlgorithm
{

	private Double initialValue;
	private Double increment;
	private Double currentValue;

	private boolean initialValueConsumed;

	/**
	 * @param initialValue
	 * @param increment
	 */

	public FloatIncrementalAlgorithm(Double initialValue, Double increment)
	{
		Assert.isNotNull(initialValue, "initialValue"); //$NON-NLS-1$
		Assert.isNotNull(increment, "increment"); //$NON-NLS-1$

		this.initialValue = initialValue;
		this.increment = increment;

		currentValue = initialValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.IFloatAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.gecucparameterdef.GConfigParameter)
	 */
	@Override
	public Double getValue(GContainer container, GReferrable parameterDefinition)
	{
		if (!initialValueConsumed && (currentValue == initialValue))
		{
			initialValueConsumed = true;
		}
		else
		{
			currentValue += increment;
		}

		return currentValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.IAlgorithm#reset()
	 */
	@Override
	public void reset()
	{
		currentValue = initialValue;
	}

}
