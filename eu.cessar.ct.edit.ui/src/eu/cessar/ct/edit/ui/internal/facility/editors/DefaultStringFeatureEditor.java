/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 12, 2010 6:22:51 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiStringEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleStringEditorPart;

/**
 * @author uidl6870
 * 
 */
public class DefaultStringFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiStringEditorPart(this);
		}
		else
		{
			return new SingleStringEditorPart(this);
		}
	}

}
