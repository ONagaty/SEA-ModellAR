package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.SingleFeatureCaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleFeatureMapEditorPart;

public class DefaultFeatureMapFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	/**
	 * will only return a SINGLE editor part
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		return new SingleFeatureMapEditorPart(this);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#createCaptionPart()
	 */
	/**
	 * overrides the standard caption part creation and sets the caption text to
	 * "Text"
	 */
	@Override
	protected ICaptionPart createCaptionPart()
	{
		String caption = "Text"; //$NON-NLS-1$

		return new SingleFeatureCaptionPart(this, caption);
	}

}
