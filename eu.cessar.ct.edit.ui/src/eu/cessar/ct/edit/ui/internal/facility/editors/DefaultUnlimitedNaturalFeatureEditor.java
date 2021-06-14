/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 16, 2010 9:16:28 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiUnlimitedNaturalEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleUnlimitedNaturalEditorPart;
import eu.cessar.ct.edit.ui.internal.facility.actions.ChangeIntegerRadixContributionItem;

/**
 * @author uidl6870
 * 
 */
public class DefaultUnlimitedNaturalFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{
			return new MultiUnlimitedNaturalEditorPart(this);
		}
		else
		{
			return new SingleUnlimitedNaturalEditorPart(this);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#populateActionPart(eu.cessar.ct.edit.ui.facility.parts.IActionPart)
	 */
	@Override
	public void populateActionPart(IActionPart part)
	{
		super.populateActionPart(part);
		part.getMenuManager().add(new ChangeIntegerRadixContributionItem(this));
	}
}
