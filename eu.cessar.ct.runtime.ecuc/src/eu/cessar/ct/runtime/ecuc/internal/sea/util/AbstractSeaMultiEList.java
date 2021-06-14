/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * May 26, 2015 3:00:57 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.Collection;
import java.util.List;

import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:53 2015 %
 * 
 *         %version: 2 %
 * @param <E>
 * @param <T>
 */
public abstract class AbstractSeaMultiEList<E, T> extends AbstractSeaEList<E, T>
{
	/**
	 * 
	 * @param parent
	 *        SEA wrapper around the parent of the backing store lists
	 * @param optionsHolder
	 *        the used SEA options store
	 */
	public AbstractSeaMultiEList(ISEAContainerParent parent, ISeaOptions optionsHolder)
	{
		super(parent, optionsHolder);

	}

	/**
	 * @return the list of 'live' lists
	 */
	protected abstract List<ESplitableList<GContainer>> getList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#primitiveGet(int)
	 */
	@Override
	protected E primitiveGet(int index)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#setUnique(int, java.lang.Object)
	 */
	@Override
	public E setUnique(int index, E object)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addUnique(java.lang.Object)
	 */
	@Override
	public void addUnique(E object)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addUnique(int, java.lang.Object)
	 */
	@Override
	public void addUnique(int index, E object)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(java.util.Collection)
	 */
	@Override
	public boolean addAllUnique(Collection<? extends E> collection)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(int, java.util.Collection)
	 */
	@Override
	public boolean addAllUnique(int index, Collection<? extends E> collection)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(java.lang.Object[], int, int)
	 */
	@Override
	public boolean addAllUnique(Object[] objects, int start, int end)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(int, java.lang.Object[], int, int)
	 */
	@Override
	public boolean addAllUnique(int index, Object[] objects, int start, int end)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#remove(int)
	 */
	@Override
	public E remove(int index)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#move(int, int)
	 */
	@Override
	public E move(int targetIndex, int sourceIndex)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#basicList()
	 */
	@Override
	protected List<E> basicList()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public E get(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size()
	{
		return getList().size();
	}

}
