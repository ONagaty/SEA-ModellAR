/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw8762<br/>
 * Dec 5, 2013 3:07:14 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill.algorithms;

import java.math.BigInteger;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.sdk.autofill.IIntAlgorithm;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Algorithm that takes an initial value and an increment
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 14:32:47 2014 %
 *
 *         %version: 2 %
 */
public class IntIncrementalAlgorithm implements IIntAlgorithm
{
	private BigInteger initialValue;
	private BigInteger increment;
	private BigInteger currentValue;

	private boolean initialValueConsumed;

	/**
	 * @param initialValue
	 * @param increment
	 */
	public IntIncrementalAlgorithm(BigInteger initialValue, BigInteger increment)
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
	 * @see eu.cessar.ct.sdk.utils.IIntAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.gecucparameterdef.GConfigParameter)
	 */
	public BigInteger getValue(GContainer container, GReferrable parameterDefinition)
	{
		if (!initialValueConsumed && (currentValue == initialValue))
		{
			initialValueConsumed = true;
		}
		else
		{
			currentValue = currentValue.add(increment);
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
