/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Sep 2, 2011 10:05:50 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.composition;

import org.eclipse.emf.ecore.EClass;


/**
 * @author uidt2045
 * 
 */
public class SimpleCategory implements ICompositionCategory<EClass>
{
	private String category;
	private EClass eClass;

	/**
	 * @param cat
	 */
	public SimpleCategory(EClass clz, String cat)
	{
		this.eClass = clz;
		this.category = cat;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICompositionCategory#getName()
	 */
	public String getName()
	{
		return category;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICompositionCategory#getNameWithFeature()
	 */
	public String getFullName()
	{
		return category;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICompositionCategory#getInput()
	 */
	public EClass getInput()
	{
		return eClass;
	}

}
