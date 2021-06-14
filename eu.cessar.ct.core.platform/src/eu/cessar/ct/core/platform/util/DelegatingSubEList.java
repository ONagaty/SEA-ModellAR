/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 16, 2010 7:02:57 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * A generic EList that act as a filter for a parent list. Only object that are of particular class will be returned to
 * the caller
 */
public class DelegatingSubEList<E> extends AbstractDelegationList<E>
{

	/**
	 *
	 */
	private static final long serialVersionUID = -5394434818465054217L;
	private final EList<? super E> parentList;

	private int[] indexMapping;

	private IModelChangeStampProvider changeProvider;
	private long lastChangeStamp;

	/**
	 * @param delegatedClass
	 * @param parentList
	 */
	public DelegatingSubEList(Class<E> delegatedClass, EList<? super E> parentList,
		IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass);
		this.parentList = parentList;
		this.changeProvider = changeProvider;
		if (changeProvider != null)
		{
			lastChangeStamp = changeProvider.getCurrentChangeStamp();
		}
	}

	/**
	 * Return the parent list from where this list get the data
	 *
	 * @return
	 */
	public EList<E> getParentList()
	{
		return (EList<E>) parentList;
	}

	/**
	 * Return true if indexMapping can be used.
	 *
	 * @return true if the indexMapping can be used
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
				List<Integer> mapping = new ArrayList<Integer>(parentList.size());
				for (int i = 0; i < parentList.size(); i++)
				{
					if (doDelegate(parentList.get(i)))
					{
						mapping.add(i);
					}
				}
				indexMapping = CollectionUtils.toIntArray(mapping);
			}
			lastChangeStamp = changeProvider.getCurrentChangeStamp();
		}
		return true;
	}

	/**
	 * Return the corresponding index from the delegated list where an element is positioned
	 *
	 * @param index
	 *        the index as the user sees
	 * @return an index into the parent list or -1 if there is no such index
	 */
	protected int getDelegatedIndex(int index)
	{
		if (indexMapping != null && indexMapping.length == 0)
		{
			indexMapping = null;
		}
		if (index < 0)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if (useIndexMapping())
		{
			return indexMapping[index];
		}
		else
		{
			int counter = -1;
			for (int i = 0; i < parentList.size(); i++)
			{
				if (doDelegate(parentList.get(i)))
				{
					counter++;
					if (counter == index)
					{
						return i;
					}
				}
			}
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateList()
	 */
	@Override
	protected EList<E> delegateList()
	{
		return (EList<E>) parentList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateAdd(java.lang.Object)
	 */
	@Override
	protected void delegateAdd(E object)
	{
		int index = -1;
		for (int i = delegateList().size() - 1; i >= 0; i--)
		{
			Object obj = delegateList().get(i);
			if (doDelegate(obj))
			{
				index = i + 1;
				delegateList().add(index, object);
				break;
			}
		}
		if (index == -1)
		{
			delegateList().add(object);
			index = delegateList().size() - 1;
		}
		indexMapping = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateAdd(int, java.lang.Object)
	 */
	@Override
	protected void delegateAdd(int index, E object)
	{
		int size = delegateSize();
		if (index == size)
		{
			delegateAdd(object);
		}
		else
		{
			int parentIndex = getDelegatedIndex(index);
			if (parentIndex == -1)
			{
				throw new BasicIndexOutOfBoundsException(index, size);
			}
			delegateList().add(parentIndex, object);
			indexMapping = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateClear()
	 */
	@Override
	protected void delegateClear()
	{
		int parentIndex = delegateList().size() - 1;
		while (parentIndex >= 0)
		{
			Object obj = delegateList().get(parentIndex);
			if (doDelegate(obj))
			{
				delegateList().remove(parentIndex);
			}
			parentIndex--;
		}
		indexMapping = null;
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
		int parentIndex = getDelegatedIndex(index);
		if (parentIndex == -1)
		{
			throw new BasicIndexOutOfBoundsException(index, delegateSize());
		}
		return delegateList().get(parentIndex);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateHashCode()
	 */
	@Override
	protected int delegateHashCode()
	{
		Iterator<E> iterator = iterator();
		int hashCode = 1;
		while (iterator.hasNext())
		{
			E element = iterator.next();
			hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
		}
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateIndexOf(java.lang.Object)
	 */
	@Override
	protected int delegateIndexOf(Object object)
	{
		Iterator<E> iterator = iterator();
		int result = 0;
		while (iterator.hasNext())
		{
			E element = iterator.next();
			if (element == object)
			{
				return result;
			}
			result++;
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
		return delegateSize() == 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateLastIndexOf(java.lang.Object)
	 */
	@Override
	protected int delegateLastIndexOf(Object object)
	{
		Iterator<E> iterator = iterator();
		int counter = 0;
		int result = -1;
		while (iterator.hasNext())
		{

			E element = iterator.next();
			if (element == object)
			{
				result = counter;
			}
			counter++;
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
		int targetParentIndex = getDelegatedIndex(targetIndex);
		int sourceParentIndex = getDelegatedIndex(sourceIndex);

		if (targetParentIndex == -1)
		{
			throw new BasicIndexOutOfBoundsException(targetIndex, delegateSize());
		}
		if (sourceParentIndex == -1)
		{
			throw new BasicIndexOutOfBoundsException(sourceIndex, delegateSize());
		}
		E result = delegateList().move(targetParentIndex, sourceParentIndex);
		indexMapping = null;
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateRemove(int)
	 */
	@Override
	protected E delegateRemove(int index)
	{
		int parentIndex = getDelegatedIndex(index);

		if (parentIndex == -1)
		{
			throw new BasicIndexOutOfBoundsException(index, delegateSize());
		}
		E result = delegateList().remove(parentIndex);
		indexMapping = null;
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateSet(int, java.lang.Object)
	 */
	@Override
	protected E delegateSet(int index, E object)
	{
		int parentIndex = getDelegatedIndex(index);
		if (parentIndex == -1)
		{
			throw new BasicIndexOutOfBoundsException(index, delegateSize());
		}
		E result = delegateList().set(parentIndex, object);
		return result;

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
			return indexMapping.length;
		}
		else
		{
			Iterator<? super E> iterator = delegateList().iterator();
			int result = 0;
			while (iterator.hasNext())
			{

				E element = (E) iterator.next();
				if (doDelegate(element))
				{
					result++;
				}
			}
			return result;
		}
	}

}
