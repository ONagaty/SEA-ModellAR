/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 17, 2010 10:55:40 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiPrimitiveEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SinglePrimitiveEditorPart;

/**
 * @author uidl6870
 * 
 */
public class DefaultPrimitiveFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiPrimitiveEditorPart(this);
		}
		else
		{
			return new SinglePrimitiveEditorPart(this);
		}
	}

}
