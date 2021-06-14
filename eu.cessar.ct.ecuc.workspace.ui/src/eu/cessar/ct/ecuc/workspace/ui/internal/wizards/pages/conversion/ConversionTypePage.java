package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;

/**
 * The page allows the user to choose between standard to refined or refined to standard conversion.
 */
public class ConversionTypePage extends WizardPage
{

	/** The btn convert standard to refined. */
	private Button btnConvertStandardToRefined;

	/** The btn refine module. */
	private Button btnConvertRefinedToStandard;

	/**
	 * Constructor for ModuleConfigurationFileSelectionPage.
	 *
	 */
	public ConversionTypePage()
	{
		super("ConversionTypeSelectionPage"); //$NON-NLS-1$
	}

	/**
	 * Gets the module definition type.
	 *
	 * @return the module definition type
	 */
	public ModuleDefinitionType getConversionType()
	{
		if (btnConvertStandardToRefined.getSelection())
		{
			return ModuleDefinitionType.STANDARD;
		}

		return ModuleDefinitionType.REFINED;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		setControl(container);

		new Label(container, SWT.NONE);
		btnConvertStandardToRefined = new Button(container, SWT.RADIO);
		GridData gridRadio1 = new GridData();
		gridRadio1.horizontalIndent = 10;
		btnConvertStandardToRefined.setLayoutData(gridRadio1);
		btnConvertStandardToRefined.setData("btnImportStandard"); //$NON-NLS-1$
		btnConvertStandardToRefined.setEnabled(true);
		btnConvertStandardToRefined.setSelection(true);
		btnConvertStandardToRefined.setText(Messages.ConversionFromStandardToRefined_radio);
		btnConvertStandardToRefined.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});

		new Label(container, SWT.NONE);
		btnConvertRefinedToStandard = new Button(container, SWT.RADIO);
		GridData gridRadio2 = new GridData();
		gridRadio2.horizontalIndent = 10;
		btnConvertRefinedToStandard.setLayoutData(gridRadio2);
		btnConvertRefinedToStandard.setData("btnRefineModule"); //$NON-NLS-1$
		btnConvertRefinedToStandard.setEnabled(true);
		btnConvertRefinedToStandard.setText(Messages.ConversionFromRefinedToStandard_radio);
		btnConvertRefinedToStandard.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});

		// set page title and description
		setTitle(Messages.ConfigurationSelectionPage_Title);
		setDescription(Messages.ConfigurationSelectionPage_Description);
	}

}
