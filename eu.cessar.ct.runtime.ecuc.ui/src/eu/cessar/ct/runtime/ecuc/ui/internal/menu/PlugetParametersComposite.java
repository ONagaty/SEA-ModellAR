/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Mar 3, 2014 4:04:29 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import java.util.EventListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.widgets.ExtendedCCombo;

/**
 * Provides controls for the selection of the variables to be used when running a pluget
 *
 * @author uidg4020
 *
 *         %created_by: uid95856 %
 *
 *         %date_created: Tue Jan 13 15:22:21 2015 %
 *
 *         %version: 5 %
 */
public class PlugetParametersComposite
{
	public final static String BUTTON_NAME = "Variables..."; //$NON-NLS-1$

	private Text text;
	private Button fVariablesButton;
	private ExtendedCCombo historyCombo;
	private String introducedText = ""; //$NON-NLS-1$
	private IPlugetParametersCompositeChangedListener parametersCompositeListener;
	private String history[];
	private Button btnClearHistory;
	private static IProject project;

	/**
	 * Interface to be implemented by classes that receive changed modifications from this composite
	 *
	 * @author uidg4020
	 *
	 */
	public static interface IPlugetParametersCompositeChangedListener extends EventListener
	{
		/**
		 *
		 * The controls that allow to run the pluget are enabled
		 *
		 * @param isValid
		 */
		public void setRunPlugetControlsEnabled(boolean isValid);

		/**
		 * Set the list of parameters that have been used before for the run pluget action
		 *
		 * @param history
		 */
		public void setHistory(String[] history);

		/**
		 * Pass the error code to listener
		 *
		 * @param error
		 */
		public void setErrorCode(String error);

		/**
		 * Used when the "enter" was pressed
		 *
		 * @return true if controls used to run pluget are enabled
		 */
		public void handleControl();

	}

	/**
	 *
	 * @param plugetParametersListener
	 *        - listener to modifications
	 */
	public PlugetParametersComposite(IPlugetParametersCompositeChangedListener plugetParametersListener)
	{
		parametersCompositeListener = plugetParametersListener;
	}

	/**
	 *
	 * @param control
	 * @param fparent
	 *        - creates widgets for pluget parameters and handle their actions on the parent composite
	 * @return composite with parameters widgets on
	 */
	public Composite createParametersArea(Composite parent)
	{
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		control.setLayout(layout);
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		GridData labelGrid = new GridData();
		labelGrid.horizontalSpan = 1;

		Label param = new Label(control, SWT.NONE);
		param.setText("Parameters:"); //$NON-NLS-1$
		param.setLayoutData(labelGrid);
		param.setAlignment(SWT.LEFT);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = 300;
		gd.heightHint = 100;

		text = new Text(control, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		text.setLayoutData(gd);
		text.setEditable(true);
		text.setEnabled(true);
		text.setFocus();

		KeyAdapter keyListener = createKeyListener();
		text.addKeyListener(keyListener);

		ModifyListener modifyListener = createModifyListener();
		text.addModifyListener(modifyListener);

		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = SWT.RIGHT;
		fVariablesButton = new Button(control, SWT.PUSH);
		fVariablesButton.setText(BUTTON_NAME);
		fVariablesButton.setLayoutData(gd);
		Shell shell = control.getShell();
		SelectionAdapter variablesButtonListener = createVariablesButtonListener(shell);
		fVariablesButton.addSelectionListener(variablesButtonListener);

		labelGrid = new GridData();
		param = new Label(control, SWT.NONE);
		param.setText("History:"); //$NON-NLS-1$
		param.setLayoutData(labelGrid);

		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		historyCombo = new ExtendedCCombo(control, SWT.BORDER);
		historyCombo.setLayoutData(gd);
		historyCombo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		SelectionAdapter comboListener = createComboListener();
		historyCombo.addSelectionListener(comboListener);

		setHistory(history);

		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = SWT.RIGHT;
		new Label(control, SWT.NONE);
		btnClearHistory = new Button(control, SWT.FLAT);
		btnClearHistory.setText("Clear history"); //$NON-NLS-1$
		btnClearHistory.setLayoutData(gd);
		btnClearHistory.addSelectionListener(clearHistoryButtonListener());

		control.setTabList(new Control[] {text, fVariablesButton, historyCombo});
		return control;

	}

	/**
	 * Listener for clearHistoryButton
	 */
	private SelectionAdapter clearHistoryButtonListener()
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				HistoryParametersUtils.clearParametersHistory(project);
				historyCombo.removeAll();
			}
		};

		return selectionAdapter;
	}

	/**
	 * Sets the history on the proper combo if there is history information and returns true, else returns false
	 *
	 * @param parametersHistory
	 *        -the history to be set
	 * @return true if history is set, false if there is no history to set
	 */
	public boolean setHistory(String[] parametersHistory)
	{
		if (parametersHistory != null)
		{
			history = parametersHistory;
			// this is in case the method is called before the controls are created
			if (historyCombo == null)
			{
				return true;
			}

			historyCombo.setItems(history);
			// set the combo and the text to the first parameters in history

			if (history.length > 0)
			{
				historyCombo.select(0);
				text.setText(historyCombo.getText());
				text.setSelection(0, text.getText().length());
			}
			return true;
		}
		return false;
	}

	/**
	 * Creates the variable selection dialog and handles the variable selection
	 *
	 * @param parent
	 */
	private void handleInsertVariable(Shell parent)
	{
		StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(parent);
		if (dialog.open() == Window.OK)
		{
			text.insert(dialog.getVariableExpression() + " "); //$NON-NLS-1$
		}

	}

	/**
	 * Validates the text and returns true if the text is valid, else returns false and send an error code to the
	 * parameters composite listener
	 *
	 * @param txt
	 * @return
	 */
	private boolean validateText(String txt)
	{
		try
		{
			VariablesPlugin.getDefault().getStringVariableManager().validateStringVariables(txt);
		}
		catch (CoreException e)
		{
			parametersCompositeListener.setErrorCode("Error:" + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	/**
	 * Creates a listener for text modifications
	 *
	 * @return listener for the text modifications
	 */
	private ModifyListener createModifyListener()
	{
		ModifyListener modifyListener = new ModifyListener()
		{

			public void modifyText(ModifyEvent e)
			{
				introducedText = text.getText();
				parametersCompositeListener.setErrorCode(null);
				boolean isValid = validateText(introducedText);
				parametersCompositeListener.setRunPlugetControlsEnabled(isValid);

			}
		};
		return modifyListener;
	}

	/**
	 *
	 * @return listener for the variables button
	 */
	private SelectionAdapter createVariablesButtonListener(final Shell shell)
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Object source = e.getSource();
				if (source == fVariablesButton)
				{

					handleInsertVariable(shell);
				}
			}
		};
		return selectionAdapter;
	}

	/**
	 * Creates key listener for the text
	 *
	 * @return
	 */
	private KeyAdapter createKeyListener()
	{
		KeyAdapter keyListener = new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.character == SWT.TAB)
				{
					text.traverse(SWT.TRAVERSE_TAB_NEXT);
					// getButtonBar().setFocus();
					// fVariablesButton.setFocus();
					e.doit = false;
				}
				else if (e.character == SWT.CR)
				{
					parametersCompositeListener.handleControl();
					e.doit = false;
				}
			}
		};
		return keyListener;
	}

	/**
	 *
	 * @return listener for the history combo
	 */
	private SelectionAdapter createComboListener()
	{
		SelectionAdapter adapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Object source = e.getSource();
				if (source == historyCombo)
				{
					text.setText(historyCombo.getItem(historyCombo.getSelectionIndex()));
				}
			}
		};
		return adapter;
	}

	/**
	 *
	 * @return the parsed parameters
	 */
	public String getIntroducedText()
	{
		return introducedText;

	}

	/**
	 * @param project
	 */
	public void setProject(IProject project)
	{
		PlugetParametersComposite.project = project;
	}
}
