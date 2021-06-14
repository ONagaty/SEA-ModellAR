/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Jun 19, 2012 1:47:12 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.preferences;

import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import eu.cessar.ct.runtime.CessarRuntimeUtils;

/**
 * Preference page for debug-related preferences.
 * 
 */
public class DebugPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
	public final static String VARIABLES_BUTTON_NAME = "Variables..."; //$NON-NLS-1$

	private Text vmArgsText;
	private Button variablesButton;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		// main composite
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		addVMArgumentsGroup(composite);

		return composite;
	}

	/**
	 * @param composite
	 */
	private void addVMArgumentsGroup(Composite composite)
	{
		Group group = new Group(composite, SWT.None);
		group.setText("VM arguments for debugging plugets and other user code"); //$NON-NLS-1$
		GridLayout layout = new GridLayout(1, false);
		group.setLayout(layout);
		GridData groupGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		group.setLayoutData(groupGridData);

		// arguments text editor
		vmArgsText = new Text(group, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.heightHint = 100;
		vmArgsText.setLayoutData(gridData);
		vmArgsText.setText(CessarRuntimeUtils.getVMArgs());

		variablesButton = new Button(group, SWT.PUSH);
		variablesButton.setText(VARIABLES_BUTTON_NAME);
		gridData = new GridData();
		variablesButton.setLayoutData(gridData);

		variablesButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Object source = e.getSource();
				if (source == variablesButton)
				{
					handleInsertVariable();
				}
			}

		});

	}

	/**
	 * 
	 */
	private void handleInsertVariable()
	{
		StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
		if (dialog.open() == Window.OK)
		{
			String variableExpression = dialog.getVariableExpression();
			if (variableExpression != null)
			{
				vmArgsText.append(variableExpression);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench)
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk()
	{
		// save the preference
		CessarRuntimeUtils.setVMArgs(vmArgsText.getText());
		return super.performOk();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults()
	{
		vmArgsText.setText(CessarRuntimeUtils.getDefaultVMArgs());
		super.performDefaults();
	}

}
