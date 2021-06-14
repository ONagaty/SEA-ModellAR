/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 2, 2014 3:19:40 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for the constraints that needs to be extended for a specific constraints filter.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Wed Jan 14 16:46:47 2015 %
 *
 *         %version: 2 %
 */
public class CessarConstraintPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
	/**
	 *
	 */
	private CessarConstraintsSelectionBlock constraintsComposite;

	@Override
	protected Control createContents(Composite parent)
	{

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final String filter = getFilter();

		if (filter.isEmpty())
		{
			constraintsComposite = new CessarConstraintsSelectionBlock();
		}
		else
		{
			constraintsComposite = new CessarConstraintsSelectionBlock(new IConstraintFilter()
			{
				public boolean accept(IConstraintDescriptor constraint, EObject target)
				{
					return constraint.getId().contains(filter);
				}
			});
		}
		constraintsComposite.createComposite(composite);

		applyDialogFont(composite);

		return composite;
	}

	@Override
	protected void performDefaults()
	{
		constraintsComposite.performDefaults();
	}

	@Override
	public boolean performOk()
	{
		constraintsComposite.performOk();
		return true;
	}

	@Override
	public void init(IWorkbench workbench)
	{
		// nothing to do
	}

	/**
	 * @return filter or empty string
	 */
	protected String getFilter()
	{
		return ""; //$NON-NLS-1$
	}
}
