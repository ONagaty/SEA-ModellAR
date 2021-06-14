/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Apr 9, 2014 1:12:00 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.sphinx.emf.validation.ui.util.ExtendedDiagnosticDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.ct.validation.ui.internal.actions.AbstractValidateAutosarContentAction;

/**
 * Diagnostic dialog extracted out of {@link AbstractValidateAutosarContentAction}.
 *
 * @author uidu2337
 *
 *         %created_by: uidu8153 %
 *
 *         %date_created: Wed Apr 29 07:07:54 2015 %
 *
 *         %version: RAUTOSAR~8 %
 */
public class CessarDiagnosticDialog extends ExtendedDiagnosticDialog
{
	private List<Integer> nrOfStatus;
	private Text elements;

	private CLabel labelEMFConstraintsStatus;
	private Link validationEMFlink;
	private boolean areEMFIntrinsicConstraintsPrefEnabled;

	private Image diagnosticImage;

	/**
	 * @param parentShell
	 * @param dialogTitle
	 * @param message
	 * @param diagnostics
	 * @param severityMask
	 * @param nrOfStatus
	 */
	public CessarDiagnosticDialog(Shell parentShell, String dialogTitle, String message, List<Diagnostic> diagnostics,
		int severityMask, List<Integer> nrOfStatus)
	{
		super(parentShell, dialogTitle, message, diagnostics, severityMask);
		this.nrOfStatus = nrOfStatus;
		areEMFIntrinsicConstraintsPrefEnabled = false;

		// update the image to display, depending on the most severe problem found (error, warning, info)
		if (nrOfStatus.get(0) != 0)
		{
			diagnosticImage = getErrorImage();
		}

		else if (nrOfStatus.get(1) != 0)
		{
			diagnosticImage = getWarningImage();

		}

		else
		{
			diagnosticImage = getInfoImage();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.sphinx.emf.validation.ui.util.ExtendedDiagnosticDialog#getImage()
	 */
	@Override
	protected Image getImage()
	{
		return diagnosticImage;
	}

	/**
	 * Update validation status label.
	 *
	 * @param composite
	 *        the composite
	 */
	private void updateEMFValidationStatus(final Composite parentComposite)
	{
		Composite composite = new Composite(parentComposite, SWT.None);
		composite.setLayout(new GridLayout());
		composite.setData(new GridData(SWT.FILL, SWT.FILL, true, true));

		areEMFIntrinsicConstraintsPrefEnabled = ValidationUtilsCommon.areEMFIntrinsicConstraintsEnabled();

		if (labelEMFConstraintsStatus == null)
		{
			labelEMFConstraintsStatus = new CLabel(composite, SWT.NONE);
		}

		labelEMFConstraintsStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		labelEMFConstraintsStatus.setImage(getWarningImage());

		labelEMFConstraintsStatus.setText(areEMFIntrinsicConstraintsPrefEnabled ? Messages.EMFIntrinsicConstraintsStatusEnabled
			: Messages.EMFIntrinsicConstraintsStatusDisabled);

		if (validationEMFlink == null)
		{
			validationEMFlink = new Link(composite, SWT.NONE);
		}

		validationEMFlink.setFont(composite.getFont());
		validationEMFlink.setText("<A>" + Messages.AUTOSARValidationPreferences + "</A>"); //$NON-NLS-1$//$NON-NLS-2$
		validationEMFlink.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{

				final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

				final PreferenceDialog validationPrefDialog = PreferencesUtil.createPreferenceDialogOn(
					activeWorkbenchWindow.getShell(), "org.artop.aal.validation.ui.preferences.validation", //$NON-NLS-1$
					new String[] {"org.artop.aal.validation.ui.preferences.validation"}, null); //$NON-NLS-1$

				boolean oldEMFIntrinsicConstraintsPrefEnabled = ValidationUtilsCommon.areEMFIntrinsicConstraintsEnabled();

				int windowResult = validationPrefDialog.open();

				if (windowResult == Window.OK)
				{
					areEMFIntrinsicConstraintsPrefEnabled = ValidationUtilsCommon.areEMFIntrinsicConstraintsEnabled();
					// When the EMF intrinsic value is changed from false to true, the validation message dialog to be
					// displayed.
					if ((oldEMFIntrinsicConstraintsPrefEnabled != areEMFIntrinsicConstraintsPrefEnabled)
						&& areEMFIntrinsicConstraintsPrefEnabled)
					{

						MessageDialog.openInformation(Display.getDefault().getActiveShell(), Messages.ValidationTitle,
							Messages.ValidationMessage);
					}

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// ...
			}
		});
	}

/*
 * (non-Javadoc)
 *
 * @see
 * org.eclipse.sphinx.emf.validation.ui.util.ExtendedDiagnosticDialog#createDialogArea(org.eclipse.swt.widgets.Composite
 * )
 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		final Control dlgArea = super.createDialogArea(parent);
		if (dlgArea instanceof Composite)
		{
			((Composite) dlgArea).setLayout(new GridLayout(2, false));

			Label labelValidationStatus = new Label((Composite) dlgArea, SWT.NATIVE);
			labelValidationStatus.setText("Number of errors/warnings: " + nrOfStatus.get(0) + ":" //$NON-NLS-1$ //$NON-NLS-2$
				+ nrOfStatus.get(1));
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			gridData.horizontalSpan = 2;
			labelValidationStatus.setLayoutData(gridData);

			// Set/update EMF intrinsic constraints status
			updateEMFValidationStatus((Composite) dlgArea);
		}

		return dlgArea;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IconAndMessageDialog#createMessageArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createMessageArea(Composite composite)
	{
		Control messageArea = super.createMessageArea(composite);
		if (messageArea instanceof Composite)
		{
			elements = new Text((Composite) messageArea, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

			GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
			data.horizontalSpan = 2;
			data.heightHint = 100;
			elements.setLayoutData(data);
			elements.setBackground(messageArea.getBackground());
		}
		return messageArea;
	}

	/**
	 * Populate elements.
	 *
	 * @param elementsList
	 *        the elements list
	 */
	public void populateElements(String elementsList)
	{
		elements.setText(elementsList);
	}

}
