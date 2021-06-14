/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Mar 7, 2016 3:15:53 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

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
public interface IReferenceAlgorithm extends IAlgorithm<ISEAContainer>
{
	@Override
	public ISEAContainer getValue(GContainer container, GReferrable parameterDefinition);
}
