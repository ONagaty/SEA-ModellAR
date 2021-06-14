/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 16, 2010 9:14:12 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiDoubleEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleDoubleEditorPart;

/**
 * @author uidl6870
 * 
 */
public class DefaultDoubleFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiDoubleEditorPart(this);
		}
		else
		{
			return new SingleDoubleEditorPart(this);
		}
	}

}
