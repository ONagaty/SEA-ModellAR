package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiLongEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleLongEditorPart;
import eu.cessar.ct.edit.ui.internal.facility.actions.ChangeSystemIntegerRadixContributionItem;

/**
 * @author uidl6343
 * 
 */
public class DefaultLongFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiLongEditorPart(this);
		}
		else
		{
			return new SingleLongEditorPart(this);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#populateActionPart(eu.cessar.ct.edit.ui.facility.parts.IActionPart)
	 */
	@Override
	public void populateActionPart(IActionPart part)
	{
		super.populateActionPart(part);
		part.getMenuManager().add(new ChangeSystemIntegerRadixContributionItem(this));
	}

}