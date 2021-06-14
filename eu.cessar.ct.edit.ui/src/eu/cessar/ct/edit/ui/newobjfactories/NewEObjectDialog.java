/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 11.04.2013 17:04:16
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.newobjfactories;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple JFace dialog that will use an {@link INewEObjectComposition} as a body
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Apr 16 19:15:00 2013 %
 * 
 *         %version: 1 %
 */
public class NewEObjectDialog extends TitleAreaDialog
{

	private IUIFeedback feedback = new IUIFeedback()
	{

		@Override
		public void setWarningMessage(String message)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void setErrorMessage(String message)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public String getWarningMessage()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getErrorMessage()
		{
			// TODO Auto-generated method stub
			return null;
		}
	};

	private final INewEObjectComposition composition;

	/**
	 * @param parentShell
	 * @param composition
	 */
	public NewEObjectDialog(Shell parentShell, INewEObjectComposition composition)
	{
		super(parentShell);
		this.composition = composition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite cmp = (Composite) super.createDialogArea(parent);
		composition.init(feedback);
		composition.createControls(cmp);
		return cmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TrayDialog#close()
	 */
	@Override
	public boolean close()
	{
		composition.dispose();
		return super.close();
	}
}
