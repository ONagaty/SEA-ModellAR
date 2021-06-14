/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 May 31, 2012 10:42:44 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import org.eclipse.emf.ecore.EClass;

/**
 * @author uidt2045
 * 
 */
public abstract class AbstractEditorProvider implements IModelFragmentEditorProvider
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#canMigrateToEClass(org.eclipse.emf.ecore.EClass)
	 */
	public boolean canMigrateToEClass(EClass other)
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#migrateToEClass(org.eclipse.emf.ecore.EClass)
	 */
	public IModelFragmentEditorProvider migrateToEClass(EClass other)
	{
		return this;
	}

}
