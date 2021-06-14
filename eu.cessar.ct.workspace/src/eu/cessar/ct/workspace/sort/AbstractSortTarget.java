/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 8, 2011 3:57:09 PM </copyright>
 */
package eu.cessar.ct.workspace.sort;

import java.util.List;


/**
 * @author uidt2045
 * 
 */
public abstract class AbstractSortTarget implements ISortTarget
{
	private Object image;
	private String label;
	protected List<ISortCriterion> sortCriterion;

	/**
	 * @param image
	 */
	protected void setImage(Object image)
	{
		this.image = image;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortTarget#getImage()
	 */
	public Object getImage()
	{
		return image;
	}

	/**
	 * @param label
	 */
	protected void setLabel(String label)
	{
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortTarget#getLabel()
	 */
	public String getLabel()
	{
		return label;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortTarget#getAvailableSortCriteria()
	 */
	public List<ISortCriterion> getAvailableSortCriteria()
	{
		if (sortCriterion == null)
		{
			sortCriterion = doGetSortCriteria();
		}
		return sortCriterion;
	}

	/**
	 * @return
	 */
	protected abstract List<ISortCriterion> doGetSortCriteria();
}
