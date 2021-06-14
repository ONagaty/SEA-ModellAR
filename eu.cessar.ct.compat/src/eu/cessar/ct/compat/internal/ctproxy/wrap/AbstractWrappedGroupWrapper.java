/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 May 13, 2011 5:09:31 PM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public abstract class AbstractWrappedGroupWrapper<T> extends AbstractMasterFeatureWrapper<T>
{

	private EObject masterWrappedUsage;
	private EStructuralFeature masterWrappedUsageFeature;
	private EMFProxyObjectImpl intermediateSlaveNode;

	/**
	 * @param engine
	 */
	public AbstractWrappedGroupWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl intermediateSlaveNode, EObject masterWrappedUsage,
		EStructuralFeature masterWrappedUsageFeature)
	{
		super(engine);
		this.masterWrappedUsage = masterWrappedUsage;
		this.masterWrappedUsageFeature = masterWrappedUsageFeature;
		this.intermediateSlaveNode = intermediateSlaveNode;
	}

	/**
	 * @return the masterWrappedUsage
	 */
	public EObject getMasterWrappedUsage()
	{
		return masterWrappedUsage;
	}

	/**
	 * @return the masterWrappedUsageFeature
	 */
	public EStructuralFeature getMasterWrappedUsageFeature()
	{
		return masterWrappedUsageFeature;
	}

	/**
	 * @return the intermediateSlaveNode
	 */
	public EMFProxyObjectImpl getIntermediateSlaveNode()
	{
		return intermediateSlaveNode;
	}

}
