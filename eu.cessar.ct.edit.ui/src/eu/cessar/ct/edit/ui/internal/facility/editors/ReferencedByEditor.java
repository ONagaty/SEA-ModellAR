/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jun 18, 2010 3:11:11 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IValidationPart;
import eu.cessar.ct.edit.ui.facility.parts.NullActionPart;
import eu.cessar.ct.edit.ui.facility.parts.NullCaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.NullValidationPart;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingManager;
import eu.cessar.ct.edit.ui.facility.splitable.NullSplitableContextEditingManager;
import eu.cessar.ct.edit.ui.internal.facility.editors.parts.ReferencedByEditorPart;
import eu.cessar.req.Requirement;

/**
 * @author uidl6458
 * 
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#REFERENCED_BY#1")
public class ReferencedByEditor extends AbstractModelEditor
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createCaptionPart()
	 */
	@Override
	protected ICaptionPart createCaptionPart()
	{
		return new NullCaptionPart(this, "References:"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		return new ReferencedByEditorPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createActionPart()
	 */
	@Override
	protected IActionPart createActionPart()
	{
		return new NullActionPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createValidationPart()
	 */
	@Override
	protected IValidationPart createValidationPart()
	{
		return new NullValidationPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#doInitialize()
	 */
	@Override
	protected void doInitialize()
	{
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getInstanceId()
	 */
	public String getInstanceId()
	{
		return getEditorProvider().getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isMultiValueEditor()
	 */
	public boolean isMultiValueEditor()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#populateActionPart(eu.cessar.ct.edit.ui.facility.parts.IActionPart
	 * )
	 */
	public void populateActionPart(IActionPart dropDownActionPart)
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueMandatory()
	 */
	public boolean isValueMandatory()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueSet()
	 */
	public boolean isValueSet()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createSplitableContextEditorManager()
	 */
	@Override
	protected ISplitableContextEditingManager createSplitableContextEditingManager()
	{
		return new NullSplitableContextEditingManager();
	}

}
