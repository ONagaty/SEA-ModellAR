/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Nov 28, 2011 10:56:50 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.events;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidu0944
 * 
 */
public interface IDataRefreshListener
{
	/**
	 * @param selection
	 */
	void refreshDataOnSelectionChanged(EObject selection);
}
