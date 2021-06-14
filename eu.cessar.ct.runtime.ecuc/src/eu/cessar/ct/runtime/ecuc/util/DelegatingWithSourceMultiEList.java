/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jun 23, 2010 2:22:57 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.platform.util.DelegatingMultiEList;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.runtime.ecuc.internal.Messages;

/**
 * @param <E>
 *
 */
public abstract class DelegatingWithSourceMultiEList<E> extends DelegatingMultiEList<E> implements
	MultiESplitableList<E>
{
	private static final long serialVersionUID = 919172454992694544L;
	private List<Object> sources;

	private Object activeSource;

	// /** read only status */
	private boolean readOnly = true;

	/**
	 * @param delegatedClass
	 * @param changeProvider
	 */
	public DelegatingWithSourceMultiEList(Class<E> delegatedClass, IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass, changeProvider);
	}

	/**
	 * @param delegatedClass
	 * @param parentLists
	 * @param changeProvider
	 */
	public DelegatingWithSourceMultiEList(Class<E> delegatedClass, List<EList<? super E>> parentLists,
		IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass, parentLists, changeProvider);
	}

	/**
	 * @param parentList
	 * @param source
	 */
	public void addParentList(EList<? super E> parentList, Object source)
	{
		getParentELists().add(parentList);
		getSources().add(source);
	}

	/**
	 * @param parentList
	 * @param newSources
	 */
	public void addParentList(List<EList<? super E>> parentList, List<Object> newSources)
	{
		getParentELists().addAll(parentList);
		getSources().addAll(newSources);
	}

	/**
	 * Returns the source corresponding to the given <code>parentList</code>.
	 *
	 * @param parentList
	 *        the list for which to search the source
	 * @return the corresponding source
	 */
	public Object getSource(EList<? super E> parentList)
	{
		List<EList<? super E>> parentELists = getParentELists();
		for (int i = 0; i < parentELists.size(); i++)
		{
			if (parentELists.get(i) == (parentList))
			{
				return sources.get(i);
			}
		}
		return null;
	}

	/**
	 * Returns the parent list corresponding to the given <code>sources</code>.
	 *
	 * @param source
	 *        the source for which to search the parent list
	 * @return the corresponding parent list
	 */
	public EList<? super E> getParentList(Object source)
	{
		for (int i = 0; i < sources.size(); i++)
		{
			if (source.equals(sources.get(i)))
			{
				return getParentELists().get(i);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#getSources()
	 */
	public List<Object> getSources()
	{
		if (sources == null)
		{
			sources = new ArrayList<Object>();
		}

		return sources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.util.MultiESplittableList#setActiveSource(java.lang.Object)
	 */
	public void setActiveSource(Object newActiveSource)
	{
		if (newActiveSource != null && !sources.contains(newActiveSource))
		{
			throw new IllegalArgumentException(NLS.bind(Messages.Invalid_activeSource, new Object[] {activeSource,
				sources}));
		}

		activeSource = newActiveSource;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.util.MultiESplittableList#isReadOnly()
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.util.MultiESplittableList#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.util.MultiESplittableList#getActiveSource()
	 */
	public Object getActiveSource()
	{
		return activeSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.DelegatingMultiEList#delegateAdd(java.lang.Object)
	 */
	@Override
	protected void delegateAdd(E object)
	{
		if (getActiveSource() == null)
		{
		
			// add at the end of the last list
			List<EList<? super E>> parentELists = getParentELists();
			if (parentELists.size() > 0)
			{
				EList<? super E> lastParentEList = parentELists.get(parentELists.size() - 1);
				lastParentEList.add(lastParentEList.size(), object);
			}
		
			return;

		}

		List<EList<? super E>> parentELists = getParentELists();
		for (EList<? super E> parentList: parentELists)
		{
			Object source = getSource(parentList);
			if (source == getActiveSource())
			{
				parentList.add(object);
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.DelegatingMultiEList#delegateAdd(int, java.lang.Object)
	 */
	@Override
	protected void delegateAdd(int index, E object)
	{
	

		if (index < 0 || index > delegateSize())
		{
			throw new BasicIndexOutOfBoundsException(index, delegateSize());
		}

		eu.cessar.ct.core.platform.util.DelegatingMultiEList<E>.ParentIndex parentIndex = getDelegatedIndex(index);
		if (parentIndex != null)
		{
			parentIndex.list.add(parentIndex.index, object);
		}
		else
		{
			List<EList<? super E>> parentELists = getParentELists();
			if (parentELists.size() > 0)
			{
				// add at the end of the last list
				EList<? super E> lastParentEList = parentELists.get(parentELists.size() - 1);
				lastParentEList.add(lastParentEList.size(), object);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.DelegatingMultiEList#delegateSet(int, java.lang.Object)
	 */
	@Override
	protected E delegateSet(int index, E object)
	{
		

		eu.cessar.ct.core.platform.util.DelegatingMultiEList<E>.ParentIndex parentIndex = getDelegatedIndex(index);
		if (parentIndex == null)
		{
			throw new BasicIndexOutOfBoundsException(index, delegateSize());
		}

		// old return (E) parentIndex.list.set(parentIndex.index, object);
		// parentIndex.list.remove(parentIndex.index + 1);
		parentIndex.list.clear();

		boolean ok = parentIndex.list.add(object);
		return ok ? object : null;
		// parentIndex.list.addAll(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.DelegatingMultiEList#delegateMove(int, int)
	 */
	@Override
	protected E delegateMove(int targetIndex, int sourceIndex)
	{
		// TODO: implement
		return super.delegateMove(targetIndex, sourceIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.DelegatingMultiEList#delegateRemove(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected E delegateRemove(int index)
	{
	

		eu.cessar.ct.core.platform.util.DelegatingMultiEList<E>.ParentIndex parentIndex = getDelegatedIndex(index);
		if (parentIndex == null)
		{
			throw new BasicIndexOutOfBoundsException(index, delegateSize());
		}

		return (E) parentIndex.list.remove(parentIndex.index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.DelegatingMultiEList#delegateClear()
	 */
	@Override
	protected void delegateClear()
	{

		

		List<EList<? super E>> parentELists = getParentELists();
		for (EList<? super E> parentList: parentELists)
		{
			Iterator<? super E> iterator = parentList.iterator();

			while (iterator.hasNext())
			{
				Object next = iterator.next();

				if (isDelegatedObject(next))
				{
					iterator.remove();
				}
			}
		}

	}

}
