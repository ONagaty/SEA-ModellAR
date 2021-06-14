/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 16, 2010 5:11:49 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import org.eclipse.emf.ecore.EObject;

/**
 * @author aurel_avramescu
 * 
 *         Interface used for actions that are operating on GModuleConfiguration
 *         objects
 */
public interface ISyncAction
{
	/**
	 * 
	 * @return the action name
	 */
	String getActionName();

	/**
	 * Execute the action
	 */
	void execute();

	EObject getOwner();

}
