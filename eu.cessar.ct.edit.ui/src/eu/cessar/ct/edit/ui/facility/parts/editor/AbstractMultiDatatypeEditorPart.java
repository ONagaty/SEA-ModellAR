/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 12, 2010 2:56:20 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractMultiDatatypeEditorPart<T> extends AbstractSingleDatatypeEditorPart<List<T>>
{

	/**
	 * @param editor
	 */
	public AbstractMultiDatatypeEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractDatatypeEditorPart#doAcceptData(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected boolean doAcceptData(List<T> oldData, final List<T> newData)
	{
		// perform the change into the model and accept data
		return performChangeWithChecks(new Runnable()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run()
			{
				applyNewData(newData);
			}
		}, "Updating data..."); //$NON-NLS-1$
	}

	@Override
	protected void applyNewData(List<T> newData)
	{
		boolean splitableInput = getEditor().isInputSplitable();
		if (!splitableInput)
		{
			setNewData(getInputObject(), newData);
		}

	}

	@Override
	protected void setNewData(EObject input, List<T> newData)
	{
		EList<T> data = (EList<T>) input.eGet(getInputFeature());
		data.clear();

		if (newData != null && !newData.isEmpty())
		{
			data.addAll(newData);
		}
	}

}
