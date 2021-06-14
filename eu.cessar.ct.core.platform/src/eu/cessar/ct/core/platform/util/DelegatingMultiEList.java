/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 17, 2010 3:16:50 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * A delegating list that get the information from multiple EList's
 *
 * @author uidl6458
 *
 * @Review uidl6458 - 12.04.2012
 *
 */
@SuppressWarnings("unchecked")
public class DelegatingMultiEList<E> extends AbstractDelegationList<E>
{

	private static final long serialVersionUID = 382229621352199410L;
	private List<EList<? super E>> parentLists;
	private IModelChangeStampProvider changeProvider;
	private List<ParentIndex> indexMapping;
	private long lastChangeStamp;

	protected class ParentIndex
	{

		public EList<? super E> list;

		public int index;

		public ParentIndex(EList<? super E> parentList, int parentIndex)
		{
			this.list = parentList;
			this.index = parentIndex;
		}
	}

	/**
	 * @param delegatedClass
	 * @param parentLists
	 */
	public DelegatingMultiEList(Class<E> delegatedClass, IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass);
		this.changeProvider = changeProvider;
		if (changeProvider != null)
		{
			lastChangeStamp = changeProvider.getCurrentChangeStamp();
		}
		this.parentLists = new ArrayList<EList<? super E>>();
	}

	/**
	 * @param delegatedClass
	 * @param parentLists
	 */
	public DelegatingMultiEList(Class<E> delegatedClass, List<EList<? super E>> parentLists,
		IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass);
		this.changeProvider = changeProvider;
		if (changeProvider != null)
		{
			lastChangeStamp = changeProvider.getCurrentChangeStamp();
		}
		this.parentLists = new ArrayList<EList<? super E>>(parentLists);
	}

	/**
	 * @return
	 */
	public List<EList<? super E>> getParentELists()
	{
		return parentLists;
	}

	/**
	 * @return
	 */
	protected boolean useIndexMapping()
	{
		if (changeProvider == null)
		{
			return false;
		}
		if (indexMapping == null || lastChangeStamp != changeProvider.getCurrentChangeStamp())
		{
			synchronized (this)
			{
				List<ParentIndex> mapping = new ArrayList<ParentIndex>();
				for (EList<? super E> list: parentLists)
				{
					for (int i = 0; i < list.size(); i++)
					{
						if (doDelegate(list.get(i)))
						{
							mapping.add(new ParentIndex(list, i));
						}
					}
				}
				indexMapping = mapping;
			}
			lastChangeStamp = changeProvider.getCurrentChangeStamp();
		}
		return true;
	}

	/**
	 * @param index
	 * @return
	 */
	protected ParentIndex getDelegatedIndex(int index)
	{
		if (index < 0)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if (useIndexMapping())
		{
			return indexMapping.get(index);
		}
		int counter = -1;
		for (EList<? super E> list: parentLists)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (doDelegate(list.get(i)))
				{
					counter++;
					if (counter == index)
					{
						return new ParentIndex(list, i);
					}
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateList()
	 */
	@Override
	protected List<E> delegateList()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateAdd(java.lang.Object)
	 */
	@Override
	protected void delegateAdd(E object)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateAdd(int, java.lang.Object)
	 */
	@Override
	protected void delegateAdd(int index, E object)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateClear()
	 */
	@Override
	protected void delegateClear()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateContains(java.lang.Object)
	 */
	@Override
	protected boolean delegateContains(Object object)
	{
		return delegateIndexOf(object) > -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateGet(int)
	 */
	@Override
	protected E delegateGet(int index)
	{
		ParentIndex parentIndex = getDelegatedIndex(index);
		if (parentIndex == null)
		{
			throw new BasicIndexOutOfBoundsException(index, delegateSize());
		}
		return (E) parentIndex.list.get(parentIndex.index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateHashCode()
	 */
	@Override
	protected int delegateHashCode()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateIndexOf(java.lang.Object)
	 */
	@Override
	protected int delegateIndexOf(Object object)
	{
		int result = -1;
		for (EList<? super E> list: parentLists)
		{
			for (int i = 0; i < list.size(); i++)
			{
				Object element = list.get(i);
				if (doDelegate(element))
				{
					result++;
					if (object == element)
					{
						return result;
					}
				}
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateIsEmpty()
	 */
	@Override
	protected boolean delegateIsEmpty()
	{
		if (useIndexMapping())
		{
			return indexMapping.isEmpty();
		}
		else
		{
			for (EList<? super E> list: parentLists)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object element = list.get(i);
					if (doDelegate(element))
					{
						return false;
					}
				}
			}
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateLastIndexOf(java.lang.Object)
	 */
	@Override
	protected int delegateLastIndexOf(Object object)
	{
		int index = -1;
		int result = -1;
		for (EList<? super E> list: parentLists)
		{
			for (int i = 0; i < list.size(); i++)
			{
				Object element = list.get(i);
				if (doDelegate(element))
				{
					index++;
					if (object == element)
					{
						result = index;
					}
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateMove(int, int)
	 */
	@Override
	protected E delegateMove(int targetIndex, int sourceIndex)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateRemove(int)
	 */
	@Override
	protected E delegateRemove(int index)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateSet(int, java.lang.Object)
	 */
	@Override
	protected E delegateSet(int index, E object)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateSize()
	 */
	@Override
	protected int delegateSize()
	{
		if (useIndexMapping())
		{
			return indexMapping.size();
		}
		else
		{
			int result = 0;
			for (EList<? super E> list: parentLists)
			{
				for (int i = 0; i < list.size(); i++)
				{
					if (doDelegate(list.get(i)))
					{
						result++;
					}
				}
			}
			return result;
		}
	}

}
