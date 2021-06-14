/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Jan 25, 2016 3:58:31 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.sphinx.emf.validation.ui.views.ValidationView;

import eu.cessar.req.Requirement;

/**
 * Custom validation view.
 *
 * @author uidj9791
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class CessarValidationView extends ValidationView
{
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.sphinx.emf.validation.ui.views.ValidationView#fillContextMenuAdditions(org.eclipse.jface.action.
	 * IMenuManager)
	 */
	@Requirement(
		reqID = "235548")
	@Override
	protected void fillContextMenuAdditions(IMenuManager manager)
	{
		super.fillContextMenuAdditions(manager);

		deleteAction.setEnabled(true);
		manager.add(deleteAction);

	}
}
