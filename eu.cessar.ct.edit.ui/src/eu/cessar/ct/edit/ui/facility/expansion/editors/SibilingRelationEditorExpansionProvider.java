package eu.cessar.ct.edit.ui.facility.expansion.editors;

import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.expansion.AbstractModelFragmentEditorExpansionProvider;
import eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion;

/**
 * @author uidl7321
 * 
 */
public class SibilingRelationEditorExpansionProvider extends
	AbstractModelFragmentEditorExpansionProvider
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.AbstractModelFragmentEditorExpansionProvider#doCreateEditorExpansion(eu.cessar.ct.edit.ui.facility.IModelFragmentEditor, java.util.List)
	 */
	@Override
	protected IModelFragmentEditorExpansion doCreateEditorExpansion(IModelFragmentEditor editor,
		List<String> featureNamesForExpansion)
	{
		return new SibilingRelationEditorExpansion(editor, featureNamesForExpansion);
	}

}
