/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:20:14 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**  
 *
 */
public interface IModelFragmentEditorProvider
{

	/**
	 * @return
	 */
	public IModelFragmentEditor createEditor();

	/**
	 * @param other
	 * @return
	 */
	public boolean canMigrateToEClass(EClass other);

	/**
	 * @param other
	 * @return
	 */
	public IModelFragmentEditorProvider migrateToEClass(EClass other);

	/**
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * @return
	 */
	public int getPriority();

	/**
	 * @return
	 */
	public String[] getCategories();

	/**
	 * Return the list of features that this editor is covering
	 * 
	 * @return
	 */
	public List<EStructuralFeature> getEditedFeatures();

	/**
	 * Return true if this provides a meta editor. A meta editor will have it's
	 * features ignored and used anyway if matches
	 * 
	 * @return
	 */
	public boolean isMetaEditor();

}
