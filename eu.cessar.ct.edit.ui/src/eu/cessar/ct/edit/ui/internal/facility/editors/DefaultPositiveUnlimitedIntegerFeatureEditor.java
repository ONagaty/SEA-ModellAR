/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Mar 16, 2010 9:15:49 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiPositiveUnlimitedIntegerEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SinglePositiveUnlimitedIntegerEditorPart;

/**
 * @author uidg3464
 *
 */
public class DefaultPositiveUnlimitedIntegerFeatureEditor extends AbstractModelFragmentFeatureEditor
{

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (isMultiValueEditor())
		{

			return new MultiPositiveUnlimitedIntegerEditorPart(this);
		}
		else
		{
			return new SinglePositiveUnlimitedIntegerEditorPart(this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#populateActionPart(eu.cessar.ct.edit.ui.facility
	 * .parts.IActionPart)
	 */
	@Override
	public void populateActionPart(IActionPart part)
	{
		super.populateActionPart(part);
		// This part has to be commented until the fix in Artop will be made
		// part.getMenuManager().add(new ChangeSystemIntegerRadixContributionItem(this));
	}
}
