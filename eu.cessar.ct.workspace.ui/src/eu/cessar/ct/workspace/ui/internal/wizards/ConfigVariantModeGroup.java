/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Feb 9, 2015 3:50:04 PM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import org.eclipse.sphinx.platform.ui.groups.AbstractGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

import eu.cessar.ct.core.platform.EProjectVariant;

/**
 * Class for creating a wizard group for configuration variant
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Mon Feb 16 15:39:17 2015 %
 *
 *         %version: 2 %
 */
public class ConfigVariantModeGroup extends AbstractGroup
{
	private static final String GROUP_NAME = "Configuration variant mode"; //$NON-NLS-1$
	private Combo cfgVariantCombo;

	/**
	 *
	 */
	public ConfigVariantModeGroup()
	{
		super(GROUP_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.sphinx.platform.ui.groups.AbstractGroup#doCreateContent(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void doCreateContent(Composite parent, int numColumns)
	{
		Font parentFont = parent.getFont();

		parent.setLayout(new GridLayout(numColumns, false));
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite controlsComposite = new Composite(parent, SWT.NONE);
		controlsComposite.setLayout(new GridLayout(2, false));
		controlsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// group controls
		Label cfgVariantLabel = new Label(controlsComposite, SWT.None);
		cfgVariantLabel.setFont(parentFont);
		cfgVariantLabel.setText("Phase"); //$NON-NLS-1$

		cfgVariantCombo = new Combo(controlsComposite, SWT.READ_ONLY);
		cfgVariantCombo.setItems(EProjectVariant.stringValues());
		cfgVariantCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cfgVariantCombo.select(0);
		createConfigVariantInformationMessage(parent);
	}

	/**
	 * Create a label containing information about configuration variant phases.
	 *
	 * @param parent
	 *        - parent composite
	 */
	private static void createConfigVariantInformationMessage(Composite parent)
	{

		Link link = new Link(parent, SWT.NONE);
		String message = Messages.projectVariantInfo;
		link.setText(message + "<a>here</a>"); //$NON-NLS-1$
		link.setSize(400, 100);
		link.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
				helpSystem.displayHelpResource("/eu.cessar.ct.doc/manual/Chapter_3.4.html");//$NON-NLS-1$
			}
		});
	}

	/**
	 * @return the {@link Combo} for the configuration variant
	 */
	public Combo getCfgVariantCombo()
	{
		return cfgVariantCombo;
	}

}
