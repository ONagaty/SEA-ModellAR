/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Oct 24, 2012 12:03:58 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

import org.artop.aal.gautosar.services.splitting.Splitable;

import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 26 10:47:12 2012 %
 * 
 *         %version: 1 %
 */
public class SplitableContextSingleFeatureEditingManager implements ISplitableContextSingleFeatureEditingManager
{
	private final IModelFragmentFeatureEditor editor;
	private ISplitableContextFeatureEditingStrategy strategy;

	public SplitableContextSingleFeatureEditingManager(IModelFragmentFeatureEditor editor)
	{
		this.editor = editor;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextSingleFeatureEditorManager#getStrategy()
	 */
	@Override
	public ISplitableContextFeatureEditingStrategy getStrategy()
	{
		if (strategy == null)
		{
			strategy = new SplitableContextFeatureEditingStrategy();
			strategy.setInput((Splitable) editor.getInput());
			strategy.setFeature(editor.getInputFeature());
		}
		return strategy;
	}

}
