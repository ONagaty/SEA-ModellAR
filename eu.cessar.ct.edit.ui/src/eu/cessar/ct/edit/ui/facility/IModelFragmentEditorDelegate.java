/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:50:06 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Contributed via registry, assigned to a particular EClass
 * 
 */
public interface IModelFragmentEditorDelegate
{

	/**
	 * Get all editors from a particular category that are defined for the
	 * <code>object</code>.
	 * 
	 * @param object
	 *        the object for which the editors are required, could be null
	 * @param clz
	 *        the concrete eClass of the object, never null
	 * @return list of editor providers, never null
	 */
	public List<IModelFragmentEditorProvider> getEditors(EObject object, EClass clz);
}
