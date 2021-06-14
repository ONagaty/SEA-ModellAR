/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Apr 8, 2010 10:13:14 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.AbstractCaptionPart;

/**
 * @author uidl6870
 * 
 */
public class EditedFeaturesCaptionPart extends AbstractCaptionPart
{
	/**
	 * @param editor
	 * @param caption
	 */
	public EditedFeaturesCaptionPart(IModelFragmentEditor editor, String caption)
	{
		super(editor, caption);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.ICaptionPart#getDocumentation()
	 */
	@Override
	public String getDocumentation()
	{
		String toolTipMessage = super.getDocumentation();
		IModelFragmentEditor editor = getEditor();
		List<EStructuralFeature> editedFeatures = editor.getEditorProvider().getEditedFeatures();
		for (EStructuralFeature feature: editedFeatures)
		{
			String documentation = EcoreUtil.getDocumentation(feature);
			if (documentation != null)
			{
				toolTipMessage += feature.getName() + ": " + documentation + "\n\n"; //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return toolTipMessage;
	}

}
