package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleStringEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

public class SingleFeatureMapEditorPart extends AbstractSingleDatatypeEditorPart<String>
{

	/**
	 * @param editor
	 */
	public SingleFeatureMapEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		String inputData = getInputData();
		if (inputData == null)
		{
			return ""; //$NON-NLS-1$
		}

		return inputData.toString();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		// TODO Auto-generated method stub
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_INTEGER);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<String> createDatatypeEditor()
	{
		return new SingleStringEditor(true);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#doAcceptData(java.lang.Object, java.lang.Object)
	 */
	@Override
	/**
	 *  get from UI, set to model	 
	 *  gets the string data from the UI and sets it as mixed text for the feature map
	 */
	protected boolean doAcceptData(String oldData, final String newData)
	{
		return performChangeWithChecks(new Runnable()
		{
			public void run()
			{
				Object inputData = getInputData();

				if (inputData instanceof FeatureMap)
				{
					FeatureMap map = (FeatureMap) inputData;

					if (newData != null)
					{
						EObjectUtil.setMixedText(map, newData);
					}
					else
					{
						EObjectUtil.setMixedText(map, null);
					}
				}
			}
		}, "Updating data..."); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#refresh()
	 */
	@Override
	/**
	 * get from model, set to UI
	 * gets the mixed text from the feature map and sets it as input data for the editor
	 */
	public void refresh()
	{
		// get from the model, set on editor
		if (datatypeEditor != null)
		{
			Object inputData = getInputData();

			if (inputData instanceof FeatureMap)
			{
				FeatureMap map = (FeatureMap) inputData;
				String data = EObjectUtil.getMixedText(map);

				datatypeEditor.setReadOnly(!getEditor().getEditableStatus().isOK());

				if (data == null || data.trim().length() == 0)
				{
					datatypeEditor.unsetInputData();
				}
				else
				{
					datatypeEditor.setInputData(data);
				}
			}
		}
	}
}
