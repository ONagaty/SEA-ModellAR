/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * May 10, 2016 6:50:58 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill.algorithms;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.sdk.autofill.IInstanceReferenceAlgorithm;
import eu.cessar.ct.sdk.collections.Wrapper;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * This is implementation of IAgorithm for the InstanceReference container
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class InstanceReferenceAlgorithm implements IInstanceReferenceAlgorithm
{

	/**
	 * Instance of wrapper responsible to stotre the target & context values for InstanceReference container.
	 */
	Wrapper<GIdentifiable> value;

	/**
	 * @param value
	 */
	public InstanceReferenceAlgorithm(Wrapper<GIdentifiable> value)
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
	 * Getter method for value
	 *
	 * @see eu.cessar.ct.sdk.autofill.IInstanceReferenceAlgorithm#getValue(gautosar.gecucdescription.GContainer,
	 * gautosar.ggenericstructure.ginfrastructure.GReferrable)
	 */
	@Override
	public Wrapper<GIdentifiable> getValue(GContainer container, GReferrable parameterDefinition)
	{
		return value;
	}

}
