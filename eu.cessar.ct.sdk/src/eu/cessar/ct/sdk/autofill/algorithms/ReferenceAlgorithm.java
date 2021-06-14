/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Mar 7, 2016 3:22:59 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill.algorithms;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.sdk.autofill.IReferenceAlgorithm;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * TODO: Please comment this class
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class ReferenceAlgorithm implements IReferenceAlgorithm
{

	ISEAContainer value;

	/**
	 *
	 */
	public ReferenceAlgorithm(ISEAContainer value)
	{
		Assert.isNotNull(value, "value"); //$NON-NLS-1$
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.autofill.IGenericAlgorithm#reset()
	 */
	@Override
	public void reset()
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.autofill.IReferenceAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.ggenericstructure.ginfrastructure.GReferrable)
	 */
	@Override
	public ISEAContainer getValue(GContainer container, GReferrable parameterDefinition)
	{
		return value;
	}

}
