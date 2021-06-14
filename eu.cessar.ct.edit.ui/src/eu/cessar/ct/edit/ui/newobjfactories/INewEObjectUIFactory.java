/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 11.04.2013 08:42:39
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.newobjfactories;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * An IEMFUIFactory is a factory class that creates classes capable to show a user interface for creating a new EObject
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Apr 16 19:14:59 2013 %
 * 
 *         %version: 1 %
 */
public interface INewEObjectUIFactory
{

	/**
	 * Return an {@link INewEObjectComposition} if the factory can show an UI for creation of the newChild, null
	 * otherwise
	 * 
	 * @param owner
	 *        The future owner of the children. This object is already persisted into a resource
	 * @param feature
	 *        The feature of the owner where the new created children will be stored
	 * @param newChildren
	 *        the children that will be stored into the owner. The object is already created but not already stored into
	 *        the owner. The factory <strong>shall not</strong> store it into the owner, this is handled by the caller.
	 * @return an {@link INewEObjectComposition} or null if this factory cannot handle the input
	 */
	public INewEObjectComposition accept(EObject owner, EStructuralFeature feature, EObject newChildren);

}
