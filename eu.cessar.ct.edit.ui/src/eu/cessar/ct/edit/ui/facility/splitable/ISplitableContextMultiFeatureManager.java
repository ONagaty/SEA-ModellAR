/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Oct 23, 2012 9:51:43 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

import java.util.List;

/**
 * Provides the editing strategy for an editor meant to edit more than one feature in the context of a splittable input.
 * 
 * @see ISplitableContextEditingManager
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 26 10:47:09 2012 %
 * 
 *         %version: 1 %
 */
public interface ISplitableContextMultiFeatureManager extends ISplitableContextEditingManager
{
	/**
	 * 
	 * @return a list with editing strategies, one for each feature that is in editor's scope
	 */
	public List<ISplitableContextFeatureEditingStrategy> getStrategies();
}
