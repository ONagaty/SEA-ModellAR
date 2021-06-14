/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by Thanatos Nov 4, 2011 8:51:07 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;

/**
 * @author uidl6870
 * 
 */
public class SaveConfirmationDialog extends SelectionDialog
{
	private Button buttonCurrentProj;
	private Button buttonCurrentRes;
	private Button buttonAllProj;

	private final boolean flagSaveCurrentFromCurrent;
	private final boolean flagSaveAllFromCurrentProject;
	private final boolean flagSavelAllFromAllProjects;

	private int chosenOption;
	private final IFile activeIFile;
	private int dirtyFilesFormCurrentProj;
	private int dirtyFilesFromAllProjs;
	private int dirtyProjects;

	/**
	 * @param parentShell
	 */
	public SaveConfirmationDialog(Shell parentShell, boolean flagSaveCurrentFromCurrent,
		boolean flagSaveAllFromCurrentProject, boolean flagSavelAllFromAllProjects,
		IFile activeIFile)
	{
		super(parentShell);
		this.flagSaveCurrentFromCurrent = flagSaveCurrentFromCurrent;
		this.flagSaveAllFromCurrentProject = flagSaveAllFromCurrentProject;
		this.flagSavelAllFromAllProjects = flagSavelAllFromAllProjects;
		this.activeIFile = activeIFile;

		setTitle("Save confirmation dialog"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{

		Composite composite = (Composite) super.createDialogArea(parent);

		// composite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		CessarFormToolkit kit = new CessarFormToolkit(parent.getDisplay());
		Label label = kit.createLabel(composite,
			"There are multiple resources that need to be saved, please select your desired action:"); //$NON-NLS-1$
		label.setBackground(parent.getBackground());

		buttonCurrentRes = new Button(composite, SWT.RADIO);
		buttonCurrentRes.setText(getOptionText(1));
		buttonCurrentRes.setBackground(parent.getBackground());
		buttonCurrentRes.setEnabled(flagSaveCurrentFromCurrent);
		buttonCurrentRes.setSelection(flagSaveCurrentFromCurrent);

		buttonCurrentProj = new Button(composite, SWT.RADIO);
		buttonCurrentProj.setText(getOptionText(2));
		buttonCurrentProj.setBackground(parent.getBackground());
		buttonCurrentProj.setEnabled(flagSaveAllFromCurrentProject);
		buttonCurrentProj.setSelection(!flagSaveCurrentFromCurrent && flagSaveAllFromCurrentProject);

		buttonAllProj = new Button(composite, SWT.RADIO);
		buttonAllProj.setText(getOptionText(3));
		buttonAllProj.setBackground(parent.getBackground());
		buttonAllProj.setEnabled(flagSavelAllFromAllProjects);
		buttonAllProj.setSelection(!flagSaveCurrentFromCurrent && !flagSaveAllFromCurrentProject
			&& flagSavelAllFromAllProjects);

		// initial value
		chosenOption = flagSaveCurrentFromCurrent ? 1 : (flagSaveAllFromCurrentProject ? 2 : 3);

		return composite;
	}

	/**
	 * @return
	 */
	private String getOptionText(int option)
	{
		StringBuffer sb = new StringBuffer();
		switch (option)
		{
			case 1:
				sb.append("Save current dirty resource "); //$NON-NLS-1$
				if (flagSaveCurrentFromCurrent)
				{
					sb.append("("); //$NON-NLS-1$
					sb.append(activeIFile.getName());
					sb.append(")"); //$NON-NLS-1$

				}
				break;
			case 2:
				sb.append("Save all dirty resources from the current project "); //$NON-NLS-1$
				if (flagSaveAllFromCurrentProject)
				{
					sb.append("("); //$NON-NLS-1$
					sb.append(dirtyFilesFormCurrentProj);
					sb.append(" dirty file(s) in "); //$NON-NLS-1$
					sb.append(activeIFile.getProject().getName());
					sb.append(")"); //$NON-NLS-1$
				}
				break;
			case 3:
				sb.append("Save all dirty resources from all projects"); //$NON-NLS-1$
				if (flagSavelAllFromAllProjects && false)
				{
					sb.append("("); //$NON-NLS-1$
					sb.append(dirtyFilesFromAllProjs);
					sb.append(" dirty files in "); //$NON-NLS-1$
					sb.append(dirtyProjects);
					sb.append(" projects"); //$NON-NLS-1$
					sb.append(")"); //$NON-NLS-1$
				}
				break;
		}

		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed()
	{
		chosenOption = buttonCurrentRes.getSelection() ? 1 : (buttonCurrentProj.getSelection() ? 2
			: 3);
		super.okPressed();

	}

	/**
	 * @return
	 */
	public int getChosenOption()
	{
		return chosenOption;
	}

	/**
	 * @param dirtyFilesFromAllProjs
	 */
	public void setdirtyFilesFromAllProjs(int dirtyFilesFromAllProjs)
	{
		this.dirtyFilesFromAllProjs = dirtyFilesFromAllProjs;
	}

	/**
	 * @param dirtyFilesFormCurrentProj
	 */
	public void setDirtyFilesFormCurrentProj(int dirtyFilesFormCurrentProj)
	{
		this.dirtyFilesFormCurrentProj = dirtyFilesFormCurrentProj;
	}

	/**
	 * @param dirtyProjects
	 */
	public void setDirtyProjects(int dirtyProjects)
	{
		this.dirtyProjects = dirtyProjects;
	}

}
