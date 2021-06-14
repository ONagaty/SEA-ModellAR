/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Mar 16, 2015 1:26:22 PM
 *
 * </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;

/**
 * Class used to display the list of files to be overwritten and a check box for user agreement
 *
 * @author uidg4020
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Tue Mar 17 11:44:08 2015 %
 *
 *         %version: 2 %
 */
public class AgreementCompositeOverwrittenFiles
{
	private Label informationLabel;
	private Composite informationComposite;
	private ScrolledComposite scrolledComposite;
	private Button agreementButton;

	/** */
	public AgreementCompositeOverwrittenFiles()
	{
		// nothing to do here
	}

	/**
	 * @param parent
	 * @param numColumns
	 */
	protected void createContent(Composite parent)
	{

		GridLayout layout = new GridLayout();
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scrolledComposite.setLayout(layout);
		scrolledComposite.setLayoutData(data);

		informationComposite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(informationComposite);

		informationComposite.setLayout(layout);

		informationComposite.setLayoutData(data);
		agreementButton = new Button(informationComposite, SWT.CHECK);
		agreementButton.setText(Messages.OverwriteFilesNotification);
		Point computeSize = agreementButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrolledComposite.setMinHeight(computeSize.y);
		informationLabel = new Label(informationComposite, SWT.NONE);

		scrolledComposite.setVisible(false);
	}

	/**
	 * @param listener
	 */
	public void addSelectionListener(SelectionAdapter listener)
	{
		agreementButton.addSelectionListener(listener);
	}

	/**
	 * @return agreement selection
	 */
	public boolean getSelection()
	{
		return agreementButton.getSelection();
	}

	/**
	 *
	 */
	public void resetButtons()
	{
		scrolledComposite.setVisible(false);
		agreementButton.setSelection(false);
		informationLabel.setText(""); //$NON-NLS-1$

	}

	/**
	 * @param resourcesPaths
	 */
	public void updateInfoMessage(List<String> resourcesPaths)
	{

		String instancesPaths = ""; //$NON-NLS-1$
		for (String path: resourcesPaths)
		{
			instancesPaths += "\n" + path; //$NON-NLS-1$
		}

		informationLabel.setText(instancesPaths);
		informationLabel.redraw();
		informationComposite.setSize(informationComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		informationComposite.redraw();
		scrolledComposite.setVisible(true);
		scrolledComposite.redraw();
	}
}
