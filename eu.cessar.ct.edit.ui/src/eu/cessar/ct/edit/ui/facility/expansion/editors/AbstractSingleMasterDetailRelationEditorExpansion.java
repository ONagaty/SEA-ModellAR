package eu.cessar.ct.edit.ui.facility.expansion.editors;

import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.expansion.AbstractSingleModelFragmentEditorExpansion;

/**
 * @author uidl7321
 * 
 */
public abstract class AbstractSingleMasterDetailRelationEditorExpansion extends
	AbstractSingleModelFragmentEditorExpansion
{

	/**
	 * @param masterEditor
	 * @param featureNamesForExpansion
	 */
	public AbstractSingleMasterDetailRelationEditorExpansion(IModelFragmentEditor masterEditor,
		List<String> featureNamesForExpansion)
	{
		super(masterEditor, featureNamesForExpansion);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.AbstractSingleModelFragmentEditorExpansion#canExpand()
	 */
	@Override
	public boolean canExpand()
	{
		IModelFragmentEditor masterEditor = getMasterEditor();
		return masterEditor != null && masterEditor.isValueSet();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.AbstractModelFragmentEditorExpansion#doSetExpanded(boolean)
	 */
	@Override
	protected void doSetExpanded(boolean expanded)
	{
		IModelFragmentEditor masterEditor = getMasterEditor();
		if (masterEditor != null)
		{
			masterEditor.setEnabled(!expanded);
		}
	}

}
