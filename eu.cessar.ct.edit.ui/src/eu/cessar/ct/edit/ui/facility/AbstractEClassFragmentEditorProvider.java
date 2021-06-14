/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 May 31, 2012 10:32:54 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import org.eclipse.emf.ecore.EClass;

/**
 * @author uidt2045
 * 
 */
public abstract class AbstractEClassFragmentEditorProvider extends AbstractEditorProvider
{
	private EClass inputClass;

	/**
	 * 
	 */
	public AbstractEClassFragmentEditorProvider(EClass inputClass)
	{
		this.inputClass = inputClass;
	}

	/**
	 * @return the inputClass
	 */
	public EClass getInputClass()
	{
		return inputClass;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractEditorProvider#canMigrateToEClass(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public boolean canMigrateToEClass(EClass other)
	{
		// for the anotations feature the provider must be recreated every time
		// so the preferences for table viewer will compute an unique instance
		// ID
		// return inputClass != other && inputClass.isSuperTypeOf(other);
		return inputClass.isSuperTypeOf(other);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractEditorProvider#migrateToEClass(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public IModelFragmentEditorProvider migrateToEClass(EClass other)
	{
		if (canMigrateToEClass(other))
		{
			IModelFragmentEditorProvider result = createCopy(other);
			return result;
		}
		else
		{
			return this;
		}
	}

	/**
	 * @param other
	 * @return
	 */
	protected abstract IModelFragmentEditorProvider createCopy(EClass other);

}
