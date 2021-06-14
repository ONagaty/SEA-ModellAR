/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Jul 7, 2011 10:25:29 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

/**
 * @author uidl7321
 * 
 */
public class ModelEditorChangeEvent
{
	private IModelFragmentEditor editor;

	public ModelEditorChangeEvent(IModelFragmentEditor editor)
	{
		this.editor = editor;
	}

	/**
	 * Returns the model editor that has changed its content.
	 * 
	 * @return
	 */
	public IModelFragmentEditor getChangedModelEditor()
	{
		return editor;
	}
}
