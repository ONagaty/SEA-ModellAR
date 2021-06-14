/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 11.10.2012 15:30:14
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextMultiFeatureManager;

/**
 * This type represents an editor for a model fragment comprising 2 or more features
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Oct 16 13:28:24 2012 %
 * 
 *         %version: 1 %
 */
public interface IModelFragmentMultiFeatureEditor extends IModelFragmentEditor
{
	public ISplitableContextMultiFeatureManager getSplitableContextEditingManager();
}
