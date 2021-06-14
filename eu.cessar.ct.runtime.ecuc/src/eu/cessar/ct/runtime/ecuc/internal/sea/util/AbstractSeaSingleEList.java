/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jun 17, 2015 11:54:50 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.MultiESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:38 2015 %
 * 
 *         %version: 1 %
 * @param <E>
 * @param <T>
 */
public abstract class AbstractSeaSingleEList<E, T> extends AbstractSeaEList<E, T>
{
	private ESplitableList<T> list;

	/**
	 * @param list
	 * @param parent
	 * @param optionsHolder
	 */
	public AbstractSeaSingleEList(ESplitableList<T> list, ISEAContainerParent parent, ISeaOptions optionsHolder)
	{
		super(parent, optionsHolder);
		this.list = list;
	}

	/**
	 * @return the backing store list
	 */
	protected ESplitableList<T> getBackingStoreList()
	{
		return list;
	}

	/**
	 * Throws an unchecked exception if the storage list comes from a split parent and no active resource (temporary or
	 * global) is set
	 */
	@Override
	protected void checkSplitAndNoActiveResource()
	{
		if (list.isSplited())
		{
			if (getActiveResource() == null)
			{
				handleNoActiveResource();
			}
		}
	}

	/**
	 * If the backing store list is split, it makes it read-write and updates its active resource
	 */
	@Override
	protected void updateSettingsForSplitList()
	{
		if (list instanceof MultiESplitableList)
		{
			MultiESplitableList<T> multiList = (MultiESplitableList<T>) list;
			multiList.setReadOnly(false);
			multiList.setActiveSource(getActiveResource());
		}
	}

	/**
	 * If the backing store list is split, sets to <code>null</code> its active resource
	 */
	@Override
	protected void resetActiveResourceForSplitList()
	{
		if (list instanceof MultiESplitableList)
		{
			MultiESplitableList<T> multiList = (MultiESplitableList<T>) list;
			multiList.setActiveSource(null);
		}
	}

}
