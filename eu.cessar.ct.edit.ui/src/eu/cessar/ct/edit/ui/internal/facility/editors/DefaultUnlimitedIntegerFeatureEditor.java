/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 16, 2010 9:15:49 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiUnlimitedIntegerEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleUnlimitedIntegerEditorPart;
import eu.cessar.ct.edit.ui.internal.facility.actions.ChangeSystemIntegerRadixContributionItem;

/**
 * @author uidl6870
 * 
 */
public class DefaultUnlimitedIntegerFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{

			return new MultiUnlimitedIntegerEditorPart(this);
		}
		else
		{
			return new SingleUnlimitedIntegerEditorPart(this);
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
