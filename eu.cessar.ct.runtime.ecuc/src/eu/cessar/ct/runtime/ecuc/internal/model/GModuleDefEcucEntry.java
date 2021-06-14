/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 14, 2009 4:48:00 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.model;

import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public class GModuleDefEcucEntry extends EcucEntry
{

	private final GModuleDef moduleDef;

	public GModuleDefEcucEntry(GModuleDef moduleDef)
	{
		this.moduleDef = moduleDef;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.model.EcucEntry#getName()
	 */
	@Override
	public String getName()
	{
		return moduleDef.gGetShortName();
	}

	/**
	 * Return the <code>GModuleDef</code> that this EcucEntrey points to
	 * 
	 * @return the GModuleDef
	 */
	public GModuleDef getModuleDefinition()
	{
		return moduleDef;
	}
}
