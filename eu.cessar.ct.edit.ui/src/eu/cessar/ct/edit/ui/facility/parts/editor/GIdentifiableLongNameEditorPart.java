package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.artop.aal.common.util.IdentifiableUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleStringEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl7321
 * 
 */
public class GIdentifiableLongNameEditorPart extends AbstractSingleDatatypeEditorPart<String>
{
	/**
	 * @param editor
	 */
	public GIdentifiableLongNameEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#getInputData()
	 */
	@Override
	protected String getInputData()
	{
		return IdentifiableUtil.getLongName(getInputObject(), "EN"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#setNewData(org.eclipse.emf.ecore.
	 * EObject, java.lang.Object)
	 */
	@Override
	protected void setNewData(EObject input, String newValue)
	{
		if (newValue != null)
		{
			IdentifiableUtil.setLongName(input, newValue, "EN"); //$NON-NLS-1$
		}
		else
		{
			getInputObject().eUnset(getInputFeature());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.SingleStringEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<String> createDatatypeEditor()
	{
		return new SingleStringEditor(true);
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
 */
	public Image getImage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		String inputData = getInputData();
		if (inputData == null)
		{
			return ""; //$NON-NLS-1$
		}

		return inputData;
	}
}
