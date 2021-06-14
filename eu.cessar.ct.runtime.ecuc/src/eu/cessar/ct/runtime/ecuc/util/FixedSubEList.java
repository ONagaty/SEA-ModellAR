/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Sep 28, 2010 2:32:43 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;

/**
 * 
 */
public class FixedSubEList<E> extends DelegatingWithSourceSubEList<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7872359026459253543L;
	private E[] objects;

	/**
	 * @param delegatedClass
	 * @param parentList
	 * @param source
	 */
	public FixedSubEList(Class<E> delegatedClass, E[] objects, EList<? super E> parentList, Object source,
		IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass, parentList, source, changeProvider);
		this.objects = objects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#isSplited()
	 */
	public boolean isSplited()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.DelegatingSubEList#isDelegatedObject(java.lang.Object)
	 */
	@Override
	protected boolean isDelegatedObject(Object parentElement)
	{
		for (E obj: objects)
		{
			if (obj == parentElement)
			{
				return true;
			}
		}
		return false;
	}

}
