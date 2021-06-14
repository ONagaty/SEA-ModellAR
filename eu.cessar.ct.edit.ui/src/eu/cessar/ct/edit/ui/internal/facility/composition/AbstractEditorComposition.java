/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Aug 23, 2011 6:06:19 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import java.util.ArrayList;
import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.composition.ICompositionCategory;
import eu.cessar.ct.edit.ui.facility.composition.IEditorComposition;

/**
 * @author uidl6870
 * 
 */
public abstract class AbstractEditorComposition<T extends ICompositionCategory> implements
	IEditorComposition<T>
{
	protected List<IModelFragmentEditorProvider> providers = new ArrayList<IModelFragmentEditorProvider>();
	protected IEditorCompositionProvider compositionProvider;
	protected boolean initialized;

	public AbstractEditorComposition(IEditorCompositionProvider compositionProvider)
	{
		this.compositionProvider = compositionProvider;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getEditorProviders()
	 */
	public List<IModelFragmentEditorProvider> getEditorProviders()
	{
		if (!initialized)
		{
			compositionProvider.doComputeEditors(this);
			initialized = true;
		}
		return providers;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#addEditorProvider(eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider)
	 */
	public void addEditorProvider(IModelFragmentEditorProvider provider)
	{
		providers.add(provider);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("\n\n Composition name: "); //$NON-NLS-1$
		sb.append(getCategory().getName());

		sb.append("\nFull name: "); //$NON-NLS-1$
		sb.append(getCategory().getFullName());

		return sb.toString();
	}

}
