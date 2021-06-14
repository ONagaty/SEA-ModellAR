/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Sep 2, 2011 10:05:10 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.composition;

import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidt2045
 * 
 */
public class EcucCategory implements ICompositionCategory<GParamConfContainerDef>
{
	private GParamConfContainerDef containerDef;

	/**
	 * @param containerDef
	 */
	public EcucCategory(GParamConfContainerDef containerDef)
	{
		this.containerDef = containerDef;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICompositionCategory#getName()
	 */
	public String getName()
	{
		return containerDef.gGetShortName();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICompositionCategory#getNameWithFeature()
	 */
	public String getFullName()
	{
		// temp
		// ModelUtils.getAbsoluteQualifiedName(containerDef);
		return getName();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICompositionCategory#getInput()
	 */
	public GParamConfContainerDef getInput()
	{
		return containerDef;
	}

}
