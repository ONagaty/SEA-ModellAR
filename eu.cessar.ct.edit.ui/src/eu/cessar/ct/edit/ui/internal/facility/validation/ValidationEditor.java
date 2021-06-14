package eu.cessar.ct.edit.ui.internal.facility.validation;

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
import eu.cessar.req.Requirement;

/**
 * 
 * Editor for the Validation tab of Properties view
 * 
 * @author uidv3687
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed Sep 17 14:46:13 2014 %
 * 
 *         %version: 1 %
 */

@Requirement(
	reqID = "REQ_EDIT_PROP#VALIDATION#1")
public class ValidationEditor extends AbstractModelEditor
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getInstanceId()
	 */
	@Override
	public String getInstanceId()
	{
		return getEditorProvider().getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isMultiValueEditor()
	 */
	@Override
	public boolean isMultiValueEditor()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueSet()
	 */
	@Override
	public boolean isValueSet()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueMandatory()
	 */
	@Override
	public boolean isValueMandatory()
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
	@Override
	public void populateActionPart(IActionPart dropDownActionPart)
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createCaptionPart()
	 */
	@Override
	protected ICaptionPart createCaptionPart()
	{
		return new NullCaptionPart(this, ""); //$NON-NLS-1$
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

	@Override
	protected IActionPart createActionPart()
	{
		return new NullActionPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		return new ValidationEditorPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createSplitableContextEditingManager()
	 */
	@Override
	protected ISplitableContextEditingManager createSplitableContextEditingManager()
	{
		return new NullSplitableContextEditingManager();
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

}
