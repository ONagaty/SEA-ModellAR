package eu.cessar.ct.workspace.ui.internal.logging;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * The class that implements console preferences dialog.
 */
public class ConsolePreferencesDialog extends Dialog
{
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ConsolePreferencesDialog(Shell parentShell)
	{
		super(parentShell);
		setShellStyle(SWT.TITLE);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@SuppressWarnings("nls")
	@Override
	protected Control createDialogArea(final Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		createGeneralGroup(parent, composite);

		createFilterMessagesGroup(composite);

		createColorsGroup(composite);

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, true));
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));

		Button btnRestoreDefaults = new Button(composite_2, SWT.NONE);
		btnRestoreDefaults.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRestoreDefaults.setText("Restore Defaults");

		Button btnApply = new Button(composite_2, SWT.NONE);
		btnApply.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnApply.setBounds(0, 0, 105, 32);
		btnApply.setText("Apply");

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		return container;
	}

	/**
	 * @param parent
	 * @param composite
	 */
	@SuppressWarnings("nls")
	private void createGeneralGroup(final Composite parent, Composite composite)
	{
		Group grpGeneral = new Group(composite, SWT.NONE);
		grpGeneral.setText("General");
		grpGeneral.setLayout(new GridLayout(4, false));
		grpGeneral.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblAutomaticallySaveConsole = new Label(grpGeneral, SWT.NONE);
		lblAutomaticallySaveConsole.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false,
			2, 1));
		lblAutomaticallySaveConsole.setBounds(0, 0, 85, 22);
		lblAutomaticallySaveConsole.setText("Automatically save console to file :");

		Label lblDconsolelog = new Label(grpGeneral, SWT.BORDER);
		lblDconsolelog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblDconsolelog.setText("");

		Button button_5 = new Button(grpGeneral, SWT.NONE);
		button_5.setText("...");
		button_5.addSelectionListener(new SelectionAdapter()
		{
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@SuppressWarnings("nls")
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog fd = new FileDialog(parent.getShell(), SWT.SAVE);
				fd.setText("Save console content to file");
				String[] filterExt = {"*.txt", "*.log", "*.*"};
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
			}
		});

		Label lblFormat = new Label(grpGeneral, SWT.NONE);
		lblFormat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFormat.setBounds(0, 0, 85, 22);
		lblFormat.setText("Message format :");

		Combo combo = new Combo(grpGeneral, SWT.NONE);
		combo.setItems(new String[] {"%msg%n", "%level - %msg%n", "%d{HH:mm:ss} | %msg%n",
			"%d{HH:mm:ss} | %level - %msg%n", "%d{HH:mm:ss} | %-5level - %msg%n",
			"%date{HH:mm:ss.SSS} - %msg%n", "%date{dd MMM yyyy; HH:mm:ss.SSS} | %level - %msg%n",
			"%date{yyy/MM/dd; HH:mm:ss} | %-5level - %msg%n"});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
	}

	/**
	 * @param composite
	 */
	@SuppressWarnings("nls")
	private void createFilterMessagesGroup(Composite composite)
	{
		Group grpFilterMessages = new Group(composite, SWT.NONE);
		grpFilterMessages.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpFilterMessages.setLayout(new GridLayout(1, false));
		grpFilterMessages.setText("Filters");

		Button btnShowDebugMessages = new Button(grpFilterMessages, SWT.CHECK);
		btnShowDebugMessages.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnShowDebugMessages.setText("Show DEBUG messages");

		Button btnShowInfoMessages = new Button(grpFilterMessages, SWT.CHECK);
		btnShowInfoMessages.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnShowInfoMessages.setText("Show INFO messages");

		Button btnShowWarningMessages = new Button(grpFilterMessages, SWT.CHECK);
		btnShowWarningMessages.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnShowWarningMessages.setText("Show WARNING messages");

		Button btnShowErrorMessages = new Button(grpFilterMessages, SWT.CHECK);
		btnShowErrorMessages.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnShowErrorMessages.setText("Show ERROR messages");
	}

	/**
	 * @param composite
	 */
	@SuppressWarnings("nls")
	private void createColorsGroup(Composite composite)
	{
		Group grpColors = new Group(composite, SWT.NONE);
		grpColors.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpColors.setLayout(new GridLayout(2, false));
		grpColors.setText("Colors");

		Label lblBackgroundColor = new Label(grpColors, SWT.NONE);
		lblBackgroundColor.setText("Background color");

		Button button = new Button(grpColors, SWT.NONE);
		button.setText("                    ");

		Label lblInfoTextColor = new Label(grpColors, SWT.NONE);
		lblInfoTextColor.setText("INFO text color");

		Button button_1 = new Button(grpColors, SWT.NONE);
		button_1.setText("                    ");

		Label lblWarningTextColor = new Label(grpColors, SWT.NONE);
		lblWarningTextColor.setText("WARNING text color");

		Button button_2 = new Button(grpColors, SWT.NONE);
		button_2.setText("                    ");

		Label lblErrorTextColor = new Label(grpColors, SWT.NONE);
		lblErrorTextColor.setText("ERROR text color");

		Button button_3 = new Button(grpColors, SWT.NONE);
		button_3.setText("                    ");

		Label lblDebugTextColor = new Label(grpColors, SWT.NONE);
		lblDebugTextColor.setText("DEBUG text color");

		Button button_4 = new Button(grpColors, SWT.NONE);
		button_4.setText("                    ");
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(680, 480);
	}
}
