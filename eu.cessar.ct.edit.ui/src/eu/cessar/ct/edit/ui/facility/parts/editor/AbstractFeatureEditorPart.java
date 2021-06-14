/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 3, 2010 2:00:59 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractFeatureEditorPart extends AbstractEditorPart
{
	protected AbstractFeatureEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/**
	 * @return
	 */
	protected EStructuralFeature getInputFeature()
	{
		IModelFragmentEditor editor = getEditor();
		Assert.isTrue(editor instanceof IModelFragmentFeatureEditor, "Editor is not an IModelFragmentFeatureEditor"); //$NON-NLS-1$
		return ((IModelFragmentFeatureEditor) editor).getInputFeature();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart#getEditor()
	 */
	@Override
	public IModelFragmentFeatureEditor getEditor()
	{
		return (IModelFragmentFeatureEditor) super.getEditor();
	}

}
