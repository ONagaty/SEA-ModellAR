/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 22, 2010 10:48:32 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

/**
 * An EList capable to hold elements that are "splitable" in the AUTOSAR sense
 * over several parents.
 */
public interface ESplitableList<E> extends EList<E>
{

	/**
	 * @param <E>
	 */
	public static class Unmodifiable<E> extends BasicEList.UnmodifiableEList<E> implements
		ESplitableList<E>
	{

		private static final long serialVersionUID = -8230253398542100610L;

		private boolean splited;
		private List<Object> sources;

		/**
		 * @param data
		 * @param splited
		 */
		public Unmodifiable(Object[] data, boolean splited, List<Object> sources)
		{
			this(data.length, data, splited, sources);
		}

		/**
		 * @param arg0
		 * @param arg1
		 */
		public Unmodifiable(int size, Object[] data, boolean splited, List<Object> sources)
		{
			super(size, data);
			this.splited = splited;
			if (sources == null)
			{
				this.sources = Collections.emptyList();
			}
			else
			{
				this.sources = Collections.unmodifiableList(sources);
			}
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#isSplited()
		 */
		public boolean isSplited()
		{
			return splited;
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#getSources()
		 */
		public List<Object> getSources()
		{
			return sources;
		}

	}

	public static class Basic<E> extends BasicEList<E> implements ESplitableList<E>
	{

		private static final long serialVersionUID = -8230253398542100610L;

		private boolean splited;
		private List<Object> sources;

		/**
		 * @param data
		 * @param splited
		 */
		public Basic(Object[] data, boolean splited, List<Object> sources)
		{
			this(data.length, data, splited, sources);
		}

		/**
		 * @param arg0
		 * @param arg1
		 */
		public Basic(int size, Object[] data, boolean splited, List<Object> sources)
		{
			super(size, data);
			this.splited = splited;
			if (sources == null)
			{
				this.sources = Collections.emptyList();
			}
			else
			{
				this.sources = Collections.unmodifiableList(sources);
			}
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#isSplited()
		 */
		public boolean isSplited()
		{
			return splited;
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#getSources()
		 */
		public List<Object> getSources()
		{
			return sources;
		}

	}

	/**
	 * Return true if the result is split over multiple sources, false
	 * otherwise. A split result is not modifiable.
	 * 
	 * @return
	 */
	public boolean isSplited();

	/**
	 * Return a modifiable list of sources from where the content of this list
	 * is produced. The returned list usually contain the autosar resources that
	 * hold the original content. Does not return null.
	 * 
	 * @return a modifiable list with the sources from where this splited list
	 *         is constructed in no particular order.
	 */
	public List<Object> getSources();
}
