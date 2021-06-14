/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.02.2014 12:04:12
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Mon Mar  3 11:56:14 2014 %
 * 
 *         %version: 2 %
 */
public interface IConcreteBinding
{

	/**
	 * Return the type of the CID element that this binding can bound to
	 * 
	 * @return the binding type, never null
	 */
	public String getBindingType();

}
