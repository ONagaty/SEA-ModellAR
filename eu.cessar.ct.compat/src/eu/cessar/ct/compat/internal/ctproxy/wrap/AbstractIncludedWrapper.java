/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 8, 2011 9:38:29 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class AbstractIncludedWrapper extends AbstractMasterFeatureWrapper<Object>
{

	private final EObject master;
	private final EStructuralFeature lvl1MasterFeature;
	private final EClass lvl1ExpectedType;
	private final EStructuralFeature lvl2MasterFeature;
	private final EClass lvl2ExpectedType;
	private final EStructuralFeature finalMasterFeature;

	/**
	 * @param engine
	 */
	public AbstractIncludedWrapper(IEMFProxyEngine engine, EObject master,
		EStructuralFeature lvl1MasterFeature, EClass lvl1ExpectedType,
		EStructuralFeature lvl2MasterFeature, EClass lvl2ExpectedType,
		EStructuralFeature finalMasterFeature)
	{
		super(engine);
		this.master = master;
		this.lvl1MasterFeature = lvl1MasterFeature;
		this.lvl1ExpectedType = lvl1ExpectedType;
		this.lvl2MasterFeature = lvl2MasterFeature;
		this.lvl2ExpectedType = lvl2ExpectedType;
		this.finalMasterFeature = finalMasterFeature;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return IEMFProxyConstants.DEFAULT_CONTEXT;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<Object> getFeatureClass()
	{
		return Object.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		return 0;
	}

	/**
	 * @return the master
	 */
	protected EObject getMaster()
	{
		return master;
	}

	/**
	 * @return the lvl1MasterFeature
	 */
	protected EStructuralFeature getLvl1MasterFeature()
	{
		return lvl1MasterFeature;
	}

	/**
	 * @return the lvl1ExpectedType
	 */
	protected EClass getLvl1ExpectedType()
	{
		return lvl1ExpectedType;
	}

	/**
	 * @return the lvl2MasterFeature
	 */
	protected EStructuralFeature getLvl2MasterFeature()
	{
		return lvl2MasterFeature;
	}

	/**
	 * @return the lvl2ExpectedType
	 */
	protected EClass getLvl2ExpectedType()
	{
		return lvl2ExpectedType;
	}

	/**
	 * @return the finalMasterFeature
	 */
	protected EStructuralFeature getFinalMasterFeature()
	{
		return finalMasterFeature;
	}

}
