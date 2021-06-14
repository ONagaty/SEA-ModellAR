/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 14:52:43
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea;

import eu.cessar.req.Requirement;

/**
 * Common interface of all SEA objects
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Aug 26 17:31:18 2013 %
 * 
 *         %version: 1 %
 */
@Requirement(
	reqID = "REQ_API#SEA#2")
public interface ISEAObject
{
	/**
	 * Returns the root of the <b>Simple ECUC API Model</b> this object belongs to
	 * 
	 * @return the associated SEA model root
	 */
	public ISEAModel getSEAModel();

}
