package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.GIdentifiableDescriptionEditorPart;

/**
 * @author uidl7321
 *
 */
public class GIdentifiableDescriptionFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.internal.facility.editors.DefaultStringFeatureEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		return new GIdentifiableDescriptionEditorPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#populateActionPart(eu.cessar.ct.edit.ui.facility
	 * .parts.IActionPart)
	 */
	@Override
	public void populateActionPart(IActionPart part)
	{
		super.populateActionPart(part);
	}

}
