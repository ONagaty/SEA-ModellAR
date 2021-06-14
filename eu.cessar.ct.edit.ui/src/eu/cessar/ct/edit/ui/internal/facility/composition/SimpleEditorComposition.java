/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Aug 22, 2011 10:00:49 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.composition.ECompositionType;
import eu.cessar.ct.edit.ui.facility.composition.ISimpleComposition;
import eu.cessar.ct.edit.ui.facility.composition.SimpleCategory;

/**
 * @author uidl6870
 * 
 */
public class SimpleEditorComposition extends AbstractEditorComposition<SimpleCategory> implements
	ISimpleComposition
{
	/**
	 * @param compositionProvider
	 */
	public SimpleEditorComposition(IEditorCompositionProvider compositionProvider)
	{
		super(compositionProvider);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getEditorProviders()
	 */
	@Override
	public List<IModelFragmentEditorProvider> getEditorProviders()
	{
		return providers;
	}

	private SimpleCategory category;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getType()
	 */
	public ECompositionType getType()
	{
		return ECompositionType.SIMPLE;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getCategory()
	 */
	public SimpleCategory getCategory()
	{
		return category;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#setCategory(eu.cessar.ct.edit.ui.ICompositionCategory)
	 */
	public void setCategory(SimpleCategory simpleCategory)
	{
		this.category = simpleCategory;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append(super.toString());

		sb.append("\nInput type: "); //$NON-NLS-1$
		sb.append(getCategory().getInput());

		return sb.toString();
	}
}
