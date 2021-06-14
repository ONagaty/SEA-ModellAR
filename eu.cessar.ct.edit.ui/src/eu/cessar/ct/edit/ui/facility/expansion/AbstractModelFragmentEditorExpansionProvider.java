package eu.cessar.ct.edit.ui.facility.expansion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * @author uidl7321
 * 
 */
public abstract class AbstractModelFragmentEditorExpansionProvider implements
	IModelFragmentEditorExpansionProvider
{
	private static final String KEY_CHILD = "child"; //$NON-NLS-1$

/* (non-Javadoc)
 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansionProvider#createExpansion(eu.cessar.ct.edit.ui.facility.IModelFragmentEditor, java.util.Map)
 */
	public IModelFragmentEditorExpansion createExpansion(IModelFragmentEditor editor,
		Map<String, String> properties)
	{
		int i = 0;
		List<String> featureNamesForExpansion = new ArrayList<String>();
		if (properties != null)
		{
			while (properties.containsKey(KEY_CHILD + i))
			{
				featureNamesForExpansion.add(properties.get(KEY_CHILD + i));
				i++;
			}
		}
		return doCreateEditorExpansion(editor, featureNamesForExpansion);
	}

	/**
	 * @param editor
	 * @param featureNamesForExpansion
	 * @return
	 */
	protected abstract IModelFragmentEditorExpansion doCreateEditorExpansion(
		IModelFragmentEditor editor, List<String> featureNamesForExpansion);
}
