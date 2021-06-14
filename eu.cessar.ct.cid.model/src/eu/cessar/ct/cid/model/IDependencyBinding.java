/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.02.2014 12:05:03
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model;

/**
 * Concrete Binding contract for a Dependency
 * 
 * @author uidl6458
 * 
 *         %created_by: uidv3687 %
 * 
 *         %date_created: Mon Mar 10 10:58:03 2014 %
 * 
 *         %version: 3 %
 */
public interface IDependencyBinding extends IConcreteBinding
{

	/**
	 * Initialize the binding for the provided dependency
	 * 
	 * @param dependency
	 */
	public void init(Dependency dependency);

	/**
	 * Return the dependency that this binding is bound to
	 * 
	 * @return the bound dependency
	 */
	public Dependency getDependency();
}
