/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Sep 1, 2011 6:00:54 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import eu.cessar.ct.edit.ui.facility.composition.ECompositionType;

/**
 * @author uidl6870
 * 
 */
public abstract class AbstractCompositionProvider implements IEditorCompositionProvider
{
	private ECompositionType type;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorCompositionProvider#getType()
	 */
	public ECompositionType getType()
	{
		return type;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorCompositionProvider#setType(eu.cessar.ct.edit.ui.CompositionType)
	 */
	public void setType(ECompositionType type)
	{
		this.type = type;

	}

}
