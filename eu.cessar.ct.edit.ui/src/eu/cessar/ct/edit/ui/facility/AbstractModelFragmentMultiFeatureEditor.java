/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 11.10.2012 15:42:26
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import org.artop.aal.gautosar.services.splitting.Splitable;

import eu.cessar.ct.edit.ui.facility.parts.IResourcesPart;
import eu.cessar.ct.edit.ui.facility.parts.MultiFeaturesResourcesPart;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextMultiFeatureManager;
import eu.cessar.ct.edit.ui.facility.splitable.SplitableContextMultiFeatureEditingManager;

/**
 * Basic implementation of editors meant to edit more than one feature.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Oct 16 13:28:22 2012 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractModelFragmentMultiFeatureEditor extends AbstractModelEditor implements
	IModelFragmentMultiFeatureEditor
{
	private ISplitableContextMultiFeatureManager manger;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createResourcesPart()
	 */
	@Override
	protected IResourcesPart createResourcesPart()
	{
		return new MultiFeaturesResourcesPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createSplitableContextEditorManager()
	 */
	@Override
	protected ISplitableContextMultiFeatureManager createSplitableContextEditingManager()
	{
		manger = new SplitableContextMultiFeatureEditingManager((Splitable) getInput(),
			getEditorProvider().getEditedFeatures());

		return manger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#getSplitableContextEditorManager()
	 */
	@Override
	public ISplitableContextMultiFeatureManager getSplitableContextEditingManager()
	{
		return manger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isMultiValueEditor()
	 */
	@Override
	public boolean isMultiValueEditor()
	{
		// it edits several features
		return true;
	}

}
