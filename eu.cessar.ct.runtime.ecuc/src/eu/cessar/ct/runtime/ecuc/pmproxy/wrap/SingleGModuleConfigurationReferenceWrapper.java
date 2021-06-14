/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 5:51:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import gautosar.gecucdescription.GModuleConfiguration;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * 
 */
public class SingleGModuleConfigurationReferenceWrapper extends AbstractSingleMasterReferenceWrapper
{

	private final List<GModuleConfiguration> modules;
	private final Object context;

	public SingleGModuleConfigurationReferenceWrapper(IEMFProxyEngine engine, List<GModuleConfiguration> modules,
		Object context)
	{
		super(engine);
		this.modules = modules;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public EObject getValue()
	{
		if (!isSetValue())
		{
			return null;
		}
		GModuleConfiguration module = modules.get(0);
		return getEngine().getSlaveObject(context, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return modules != null && modules.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		// do nothing, cannot set
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		// do nothing, cannot unset
	}

}
