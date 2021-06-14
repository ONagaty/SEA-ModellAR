package eu.cessar.ct.edit.ui.facility.expansion.editors;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.expansion.AbstractSingleModelFragmentEditorExpansion;
import eu.cessar.ct.edit.ui.facility.expansion.EExpansionType;
import eu.cessar.ct.edit.ui.facility.parts.EEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;

/**
 * @author uidl7321
 * 
 */
public class SibilingRelationEditorExpansion extends AbstractSingleModelFragmentEditorExpansion
{

	private class SibilingRelationEditorLabelProvider extends LabelProvider
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element)
		{
			IModelFragmentEditor masterEditor = getMasterEditor();
			if (masterEditor != null)
			{
				IEditorPart editorPart = (IEditorPart) masterEditor.getPart(EEditorPart.EDITING_AREA);
				if (editorPart != null)
				{
					return editorPart.getText();
				}
			}
			return "null"; //$NON-NLS-1$
		}
	}

	/**
	 * @param masterEditor
	 * @param featureNamesForExpansion
	 */
	public SibilingRelationEditorExpansion(IModelFragmentEditor masterEditor, List<String> featureNamesForExpansion)
	{
		super(masterEditor, featureNamesForExpansion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.expansion.AbstractSingleModelFragmentEditorExpansion#canExpand()
	 */
	@Override
	public boolean canExpand()
	{
		return getExpansionInput() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#getLabelProvider()
	 */
	public ILabelProvider getLabelProvider()
	{
		return new SibilingRelationEditorLabelProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.expansion.AbstractSingleModelFragmentEditorExpansion#doGetExpansionInput()
	 */
	@Override
	protected EObject doGetExpansionInput()
	{
		IModelFragmentEditor masterEditor = getMasterEditor();
		if (masterEditor != null)
		{
			return masterEditor.getInput();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.expansion.AbstractModelFragmentEditorExpansion#doSetExpanded(boolean)
	 */
	@Override
	protected void doSetExpanded(boolean expanded)
	{
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#getType()
	 */
	public EExpansionType getType()
	{
		return EExpansionType.SIBLING;
	}

}
