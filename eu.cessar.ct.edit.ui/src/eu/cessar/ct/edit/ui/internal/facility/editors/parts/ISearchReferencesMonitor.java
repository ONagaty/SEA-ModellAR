/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jul 12, 2011 9:06:14 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors.parts;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidu0944
 * 
 */
public interface ISearchReferencesMonitor
{
	/**
	 * @param refs
	 */
	void setReferences(Collection<EObject> refs);

	/**
	 * @return
	 */
	boolean isCanceled();
}
