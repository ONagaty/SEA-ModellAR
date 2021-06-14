/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 17.12.2012 17:00:13
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.util.DelegatingEList;

/**
 * Super class for all delegation list
 * 
 * @param <E>
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Dec 18 17:39:31 2012 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractDelegationList<E> extends DelegatingEList<E>
{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -5085231257563323793L;
	private Class<E> delegatedClass;
	private Set<IFilter<E>> filters;

	/**
	 * @param delegatedClass
	 * @param parentLists
	 */
	public AbstractDelegationList(Class<E> delegatedClass)
	{
		this.delegatedClass = delegatedClass;
	}

	/**
	 * Add a new filter that will be used to probe for delegation. If the object pass the filter it will be delegated.
	 * 
	 * @param filter
	 */
	public void addFilter(IFilter<E> filter)
	{
		if (filters == null)
		{
			filters = new HashSet<IFilter<E>>();
		}
		filters.add(filter);
	}

	/**
	 * Remove a filter
	 * 
	 * @param filter
	 * @return true if the filter has been removed, false otherwise
	 */
	public boolean removeFilter(IFilter<E> filter)
	{
		if (filters != null)
		{
			return filters.remove(filter);
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return the class that will be checked for delegation
	 */
	protected Class<E> getDelegatedClass()
	{
		return delegatedClass;
	}

	/**
	 * Return true if the element need to be delegated, false otherwise
	 * 
	 * @param parentElement
	 * @return true if the element need to be delegated
	 */
	protected final boolean doDelegate(final Object parentElement)
	{
		final boolean result[] = {isDelegatedObject(parentElement)};
		if (result[0] && filters != null)
		{
			// check also the filters
			for (final IFilter<E> filter: filters)
			{
				SafeRunner.run(new SafeRunnable()
				{

					@SuppressWarnings("unchecked")
					@Override
					public void run() throws Exception
					{
						result[0] = filter.isPassingFilter((E) parentElement);
					}
				});
				if (!result[0])
				{
					// stop right now, it has been filtered
					break;
				}
			}
		}

		return result[0];
	}

	/**
	 * Return true if the <code>parentElement</code> is a delegated element
	 * 
	 * @param parentElement
	 * @return true if the element have to be delegated, false otherwise
	 */
	protected boolean isDelegatedObject(Object parentElement)
	{
		return parentElement != null && delegatedClass.isInstance(parentElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateContainsAll(java.util.Collection)
	 */
	@Override
	protected boolean delegateContainsAll(Collection<?> collection)
	{
		Iterator<?> it = collection.iterator();
		while (it.hasNext())
		{
			if (!delegateContains(it.next()))
			{
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateEquals(java.lang.Object)
	 */
	@Override
	protected boolean delegateEquals(Object object)
	{
		if (object == this)
		{
			return true;
		}
		if (object instanceof List<?>)
		{
			List<?> lst = (List<?>) object;
			Iterator<E> it = iterator();
			Iterator<?> otherIt = lst.iterator();
			while (it.hasNext())
			{
				Object obj = it.next();
				if (otherIt.hasNext())
				{
					Object otherObj = otherIt.next();
					if (obj != otherObj)
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			return !otherIt.hasNext();
		}
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateToString()
	 */
	@Override
	protected String delegateToString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("["); //$NON-NLS-1$
		Iterator<E> it = iterator();
		while (it.hasNext())
		{
			stringBuffer.append(String.valueOf(it.next()));
			if (it.hasNext())
			{
				stringBuffer.append(", "); //$NON-NLS-1$
			}
		}
		stringBuffer.append("]"); //$NON-NLS-1$
		return stringBuffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateToArray()
	 */
	@Override
	protected Object[] delegateToArray()
	{
		int size = delegateSize();
		return delegateToArray((E[]) Array.newInstance(delegatedClass, size), size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.DelegatingEList#delegateToArray(T[])
	 */
	@Override
	protected <T> T[] delegateToArray(T[] array)
	{
		return delegateToArray(array, delegateSize());
	}

	/**
	 * @param newInstance
	 * @param size
	 * @return
	 */
	private <T> T[] delegateToArray(T[] array, int cachedSize)
	{
		if (array.length < cachedSize)
		{
			T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), cachedSize);
			// SUPPRESS CHECKSTYLE allow assignment
			array = newArray;
		}
		int index = 0;
		Iterator<E> it = iterator();
		while (it.hasNext())
		{
			array[index++] = (T) it.next();
		}
		return array;
	}
}
