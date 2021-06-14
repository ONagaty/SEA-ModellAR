/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 06.09.2013 11:40:27
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.domain;

import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Sep  6 16:58:30 2013 %
 * 
 *         %version: 1 %
 */
public interface IUpdatableModelChangeStampProvider extends IModelChangeStampProvider
{

	/**
	 * 
	 */
	public void modelChanged();

}
