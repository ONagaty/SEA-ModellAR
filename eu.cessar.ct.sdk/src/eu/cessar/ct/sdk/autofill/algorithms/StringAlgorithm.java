/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 12, 2013 11:14:24 AM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill.algorithms;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.sdk.autofill.IStringAlgorithm;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:35:39 2014 %
 *
 *         %version: 1 %
 */
public class StringAlgorithm implements IStringAlgorithm
{

	private String value;

	/**
	 * @param value
	 */
	public StringAlgorithm(String value)
	{
		Assert.isNotNull(value, "value"); //$NON-NLS-1$
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.IStringAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.gecucparameterdef.GConfigParameter)
	 */
	@Override
	public String getValue(GContainer container, GReferrable parameterDefinition)
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
