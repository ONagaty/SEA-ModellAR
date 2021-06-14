/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jun 23, 2010 2:22:57 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.platform.util.DelegatingSubEList;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;

/**
 * 
 */
public abstract class DelegatingWithSourceSubEList<E> extends DelegatingSubEList<E> implements ESplitableList<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 919172454992694544L;
	private List<Object> sources;

	/**
	 * @param delegatedClass
	 * @param parentList
	 * @param source
	 * @param changeProvider
	 */
	public DelegatingWithSourceSubEList(Class<E> delegatedClass, EList<? super E> parentList, Object source,
		IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass, parentList, changeProvider);
		sources = Collections.singletonList(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#getSources()
	 */
	public List<Object> getSources()
	{
		return sources;
	}

}
