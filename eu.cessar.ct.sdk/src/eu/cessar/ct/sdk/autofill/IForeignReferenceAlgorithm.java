/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Mar 9, 2016 11:50:16 AM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
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
public interface IForeignReferenceAlgorithm extends IAlgorithm<GIdentifiable>
{
	@Override
	public GIdentifiable getValue(GContainer container, GReferrable parameterDefinition);
}
