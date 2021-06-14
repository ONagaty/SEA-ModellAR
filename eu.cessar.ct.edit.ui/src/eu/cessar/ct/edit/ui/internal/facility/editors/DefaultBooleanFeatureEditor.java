package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiBooleanEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleBooleanEditorPart;

public class DefaultBooleanFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.internal.facility.editors.Abstract4PartSection#getEditorPart()
	 */
	@Override
	public IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiBooleanEditorPart(this);
		}
		else
		{
			return new SingleBooleanEditorPart(this);
		}
	}
}
