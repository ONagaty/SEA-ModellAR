/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * May 10, 2016 6:45:32 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import eu.cessar.ct.sdk.collections.Wrapper;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * An interface to define InstanceReferenceAlgorithm, Responsible to provide value of type Wrapper<GIdentifiable>
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public interface IInstanceReferenceAlgorithm extends IAlgorithm<Wrapper<GIdentifiable>>
{
	@Override
	public Wrapper<GIdentifiable> getValue(GContainer container, GReferrable parameterDefinition);
}
