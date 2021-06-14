/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 8, 2010 1:21:43 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author uidl6458
 * 
 */
public class DelegatedEditorProvider extends AbstractEditorProvider
{

	private final IModelFragmentEditorProvider delegate;
	private final String[] categories;
	private final int priority;

	public DelegatedEditorProvider(IModelFragmentEditorProvider delegate)
	{
		this(delegate, delegate.getCategories(), delegate.getPriority());
	}

	public DelegatedEditorProvider(IModelFragmentEditorProvider delegate, String[] categories)
	{
		this(delegate, categories, delegate.getPriority());
	}

	public DelegatedEditorProvider(IModelFragmentEditorProvider delegate, String[] categories,
		int priority)
	{
		this.delegate = delegate;
		this.categories = categories;
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#createEditor()
	 */
	public IModelFragmentEditor createEditor()
	{
		return delegate.createEditor();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getId()
	 */
	public String getId()
	{
		return delegate.getId();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getCategories()
	 */
	public String[] getCategories()
	{
		return categories;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getEditedFeatures()
	 */
	public List<EStructuralFeature> getEditedFeatures()
	{
		return delegate.getEditedFeatures();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getPriority()
	 */
	public int getPriority()
	{
		// TODO Auto-generated method stub
		return priority;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#isMetaEditor()
	 */
	public boolean isMetaEditor()
	{
		return delegate.isMetaEditor();
	}

}
