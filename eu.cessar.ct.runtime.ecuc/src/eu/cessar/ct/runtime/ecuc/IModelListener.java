/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 15, 2010 12:12:32 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc;

import org.eclipse.core.resources.IProject;

/**
 * 
 */
public interface IModelListener
{

	public void modelChanged(IProject project);

}
