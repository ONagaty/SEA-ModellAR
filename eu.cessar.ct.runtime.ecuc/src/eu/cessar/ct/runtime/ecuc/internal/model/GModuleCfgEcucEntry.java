/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 14, 2009 4:48:00 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public class GModuleCfgEcucEntry extends EcucEntry
{

	private final List<GModuleConfiguration> modules;

	private final String moduleName;

	public GModuleCfgEcucEntry(GModuleConfiguration moduleCfg)
	{
		modules = new ArrayList<GModuleConfiguration>();
		modules.add(moduleCfg);
		moduleName = moduleCfg.gGetShortName();
	}

	/**
	 * return true if all module configurations have the same definitions
	 * 
	 * @return
	 */
	public boolean sameDefinition()
	{
		if (modules.size() <= 1)
		{
			return true;
		}
		else
		{
			GModuleDef moduleDef = modules.get(0).gGetDefinition();
			if (moduleDef == null)
			{
				return false;
			}
			else
			{
				for (int i = 1; i < modules.size(); i++)
				{
					if (moduleDef != modules.get(i).gGetDefinition())
					{
						return false;
					}
				}
				return true;
			}
		}
	}

	public void addModuleConfiguration(GModuleConfiguration cfg)
	{
		modules.add(cfg);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.model.EcucEntry#getName()
	 */
	@Override
	public String getName()
	{
		return moduleName;
	}

	/**
	 * Return the <code>GModuleConfiguration</code> that this EcucEntry points
	 * to
	 * 
	 * @return the GModuleConfiguration
	 */
	public List<GModuleConfiguration> getAllConfigurations()
	{
		return Collections.unmodifiableList(modules);
	}

	/**
	 * @param def
	 * @return
	 */
	public List<GModuleConfiguration> getConfigurations(GModuleDef def)
	{
		List<GModuleConfiguration> result = new ArrayList<GModuleConfiguration>();
		for (GModuleConfiguration cfg: modules)
		{
			if (cfg.gGetDefinition() == def)
			{
				result.add(cfg);
			}
		}
		return result;
	}
}
