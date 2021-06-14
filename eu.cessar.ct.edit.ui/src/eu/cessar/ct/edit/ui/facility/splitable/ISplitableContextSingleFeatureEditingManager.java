/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Oct 23, 2012 9:51:27 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

/**
 * Provides the editing strategy for an editor meant to edit a single feature in the context of a splittable input.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 26 10:47:10 2012 %
 * 
 *         %version: 1 %
 */
public interface ISplitableContextSingleFeatureEditingManager extends ISplitableContextEditingManager
{

	/**
	 * 
	 * @return the editing strategy used for a particular feature
	 */
	public ISplitableContextFeatureEditingStrategy getStrategy();
}
