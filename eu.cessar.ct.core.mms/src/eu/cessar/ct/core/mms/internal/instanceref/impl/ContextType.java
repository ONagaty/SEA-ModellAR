/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jul 13, 2011 10:19:03 AM </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.core.mms.instanceref.IContextType;

/**
 * Such an instance is created for each context of an instance reference.
 * 
 * @author uidl6870
 * 
 */
public class ContextType implements IContextType
{
	private EClass type;
	private boolean allowsInfiniteInstances;
	private final boolean isRoot;

	/**
	 * @param type
	 *        type of the context
	 * @param allowsInfiniteInstances
	 *        whether infinite instances are allowed for configuring context
	 * @param isRoot
	 *        whether this is the first context
	 */
	public ContextType(EClass type, boolean allowsInfiniteInstances, boolean isRoot)
	{
		this.type = type;
		this.allowsInfiniteInstances = allowsInfiniteInstances;
		this.isRoot = isRoot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IContextType#getType()
	 */
	public EClass getType()
	{
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IContextType#allowsInfiniteInstances()
	 */
	public boolean allowsInfiniteInstances()
	{
		return allowsInfiniteInstances;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IContextType#isRoot()
	 */
	@Override
	public boolean isRoot()
	{
		return isRoot;
	}

}
