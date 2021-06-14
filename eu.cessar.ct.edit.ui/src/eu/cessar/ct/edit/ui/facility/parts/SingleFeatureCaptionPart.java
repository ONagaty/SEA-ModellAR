/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 3, 2010 2:00:12 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6458
 * 
 */
public class SingleFeatureCaptionPart extends AbstractCaptionPart
{
	/**
	 * @param editor
	 */
	public SingleFeatureCaptionPart(IModelFragmentFeatureEditor editor, String caption)
	{
		super(editor, caption);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICaptionPart#getDocumentation()
	 */
	@Override
	public String getDocumentation()
	{
		return super.getDocumentation() + EcoreUtil.getDocumentation(getInputFeature());
	}

	/**
	 * @return
	 */
	private EStructuralFeature getInputFeature()
	{
		IModelFragmentEditor editor = getEditor();
		Assert.isTrue(editor instanceof IModelFragmentFeatureEditor,
			"Editor is not an IModelFragmentFeatureEditor"); //$NON-NLS-1$
		return ((IModelFragmentFeatureEditor) editor).getInputFeature();
	}

}
