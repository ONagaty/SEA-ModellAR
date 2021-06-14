/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 11, 2010 9:31:59 AM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref;

import java.util.List;

/**
 * A Wrapper around a GInstanceReferenceValue(V=GIdentifiable) or a System
 * Instance Reference(V=SystemIRefFeatureWrapper)
 * 
 */
public class Wrapper<V>
{
	private V target;
	private List<V> context;

	public Wrapper(V target, List<V> context)
	{
		this.target = target;
		this.context = context;
	}

	public V getTarget()
	{
		return target;
	}

	public List<V> getContext()
	{
		return context;
	}
}
