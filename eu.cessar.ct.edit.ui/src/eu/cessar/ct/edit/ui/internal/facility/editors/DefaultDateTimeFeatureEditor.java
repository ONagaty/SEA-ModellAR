/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 15.03.2010 22:56:25 </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiDateTimeEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleDateTimeEditorPart;

/**
 * @author uidl6870
 * 
 */
public class DefaultDateTimeFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiDateTimeEditorPart(this);
		}
		else
		{
			return new SingleDateTimeEditorPart(this);
		}
	}
}
