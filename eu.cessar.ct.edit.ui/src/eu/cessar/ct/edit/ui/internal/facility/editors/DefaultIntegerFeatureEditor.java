/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 15, 2010 2:19:40 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiIntegerEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleIntegerEditorPart;
import eu.cessar.ct.edit.ui.internal.facility.actions.ChangeSystemIntegerRadixContributionItem;

/**
 * @author uidl6870
 * 
 */
public class DefaultIntegerFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiIntegerEditorPart(this);
		}
		else
		{
			return new SingleIntegerEditorPart(this);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#populateActionPart(eu.cessar.ct.edit.ui.facility.parts.IActionPart)
	 */
	@Override
	public void populateActionPart(IActionPart part)
	{
		super.populateActionPart(part);
		part.getMenuManager().add(new ChangeSystemIntegerRadixContributionItem(this));
	}
}
