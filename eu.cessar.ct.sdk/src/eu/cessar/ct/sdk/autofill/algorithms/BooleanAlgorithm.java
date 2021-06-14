/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 12, 2013 11:16:43 AM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill.algorithms;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.sdk.autofill.IBooleanAlgorithm;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:35:38 2014 %
 *
 *         %version: 1 %
 */
public class BooleanAlgorithm implements IBooleanAlgorithm
{
	private Boolean value;

	/**
	 * @param value
	 */
	public BooleanAlgorithm(Boolean value)
	{
		Assert.isNotNull(value, "value"); //$NON-NLS-1$
		this.value = value;
	}

	/**
	 * @param containerGContainer
	 * @return the value
	 */
	@Override
	public Boolean getValue(GContainer container, GReferrable parameterDefinition)
	{
		return value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.IGenericAlgorithm#reset()
	 */
	@Override
	public void reset()
	{
		// do nothing
	}

}
