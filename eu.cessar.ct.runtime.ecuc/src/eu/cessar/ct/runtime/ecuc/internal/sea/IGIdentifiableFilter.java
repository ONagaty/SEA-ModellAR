/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 13, 2013 10:20:29 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea;

import eu.cessar.ct.core.platform.util.IFilter;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Filter for {@link GIdentifiable}(s)
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Aug 21 10:05:16 2013 %
 * 
 *         %version: 1 %
 */
public interface IGIdentifiableFilter extends IFilter<GIdentifiable>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaFilter#isPassingFilter(GIdentifiable)
	 */
	public boolean isPassingFilter(GIdentifiable element);
}
