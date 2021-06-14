/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 5:51:55 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * 
 */
public class MultiGModuleConfigurationReferenceWrapper extends
	AbstractMultiMasterFeatureWrapper<EObject> implements IMasterReferenceWrapper
{

	private final List<List<GModuleConfiguration>> multiModules;
	private final Object context;

	public MultiGModuleConfigurationReferenceWrapper(IEMFProxyEngine engine,
		List<List<GModuleConfiguration>> multiModules, Object context)
	{
		super(engine);
		this.multiModules = multiModules;
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper#haveLiveValues()
	 */
	@Override
	public boolean haveLiveValues()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<EObject> getFeatureClass()
	{
		return EObject.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#getWrappedList()
	 */
	@Override
	protected List<?> getWrappedList()
	{
		return multiModules;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public EObject get(int index)
	{
		// TODO Auto-generated method stub
		List<GModuleConfiguration> list = multiModules.get(index);
		GModuleConfiguration pack = list.get(0);
		return getEngine().getSlaveObject(context, pack);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(int index)
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
	 */
	public Object move(int targetIndex, int sourceIndex)
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(int index, Object value)
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(int index, Object value)
	{
		throw new UnsupportedOperationException();
	}

}
