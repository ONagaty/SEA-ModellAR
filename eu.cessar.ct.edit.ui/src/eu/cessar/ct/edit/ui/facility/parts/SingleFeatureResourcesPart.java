/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.10.2012 10:01:49
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingStrategy;

/**
 * Concrete implementation of a {@link IResourcesPart} to be used by single feature editors
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Oct 16 13:28:27 2012 %
 * 
 *         %version: 1 %
 */
public class SingleFeatureResourcesPart extends AbstractSingleStrategyResourcesPart
{
	private final IModelFragmentFeatureEditor featureEditor;

	/**
	 * @param editor
	 */
	public SingleFeatureResourcesPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
		featureEditor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractSingleStrategyResourcesPart#getStrategy()
	 */
	@Override
	protected ISplitableContextEditingStrategy getStrategy()
	{
		return featureEditor.getSplitableContextEditingManager().getStrategy();
	}

}
