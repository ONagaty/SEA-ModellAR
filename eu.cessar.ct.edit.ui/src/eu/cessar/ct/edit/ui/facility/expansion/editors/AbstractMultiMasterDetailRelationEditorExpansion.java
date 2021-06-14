package eu.cessar.ct.edit.ui.facility.expansion.editors;

import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.expansion.AbstractMultiModelFragmentEditorExpansion;
import eu.cessar.ct.edit.ui.facility.expansion.EExpansionType;

/**
 * @author uidl7321
 * 
 */
public abstract class AbstractMultiMasterDetailRelationEditorExpansion extends
	AbstractMultiModelFragmentEditorExpansion
{

	/**
	 * @param masterEditor
	 * @param featureNamesForExpansion
	 */
	public AbstractMultiMasterDetailRelationEditorExpansion(IModelFragmentEditor masterEditor,
		List<String> featureNamesForExpansion)
	{
		super(masterEditor, featureNamesForExpansion);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.editor.expansion.IModelFragmentEditorExpansion#canExpand()
	 */
	@Override
	public boolean canExpand()
	{
		IModelFragmentEditor masterEditor = getMasterEditor();
		return masterEditor != null;
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

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#getType()
	 */
	public EExpansionType getType()
	{
		return EExpansionType.MASTER_DETAIL;
	}
}
