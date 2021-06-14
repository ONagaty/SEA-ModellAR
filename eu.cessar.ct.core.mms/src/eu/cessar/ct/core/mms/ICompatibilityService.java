/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 21, 2010 5:05:14 PM </copyright>
 */
package eu.cessar.ct.core.mms;

/**
 * Compatibility related services. Only MM 2.1 and 3x have implementation. Can
 * be obtained using the {@link IMetaModelService#getCompatibilityService()}
 * method
 * 
 * @Review uidl6458 - 29.03.2012
 */
public interface ICompatibilityService
{

	/**
	 * Return the name of the task used to generate plugets from .pluget file in
	 * compatibility mode for the corresponding metamodel
	 * 
	 * @return the name of the task
	 */
	public String getPlugetTaskTypeName();

	/**
	 * Return the name of the task used to generate plugets from classes in
	 * compatibility mode for the corresponding metamodel
	 * 
	 * @return the name of the task
	 */
	public String getPlugetClassTaskTypeName();

}
