/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 19, 2012 12:21:13 PM </copyright>
 */
package eu.cessar.ct.validation.ui.internal.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.validation.preferences.EValidationType;
import eu.cessar.ct.validation.preferences.ValidationPreferencesAccessor;
import eu.cessar.req.Requirement;

/**
 * @author uidt2045
 * 
 */
public class CessarValidationPrefAndPropPage extends AbstractPreferenceAndPropertyPage
{
	private static final String PROPERTY_PAGE_ID = "eu.cessar.ct.validation.ui.CessarValidationPropertyPage"; //$NON-NLS-1$
	private static final String PREFERENCE_PAGE_ID = "eu.cessar.ct.validation.ui.CessarValidationPreferencePage"; //$NON-NLS-1$

	// LIVE VALIDATION group
	private Button liveValidationButton1;
	private Button liveValidationButton2;
	private Button liveValidationButton3;

	// FILTERED VALIDATION
	private Button filteredValidationButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#createPreferenceContent(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createPreferenceContent(Composite composite)
	{
		Composite parent = new Composite(composite, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		parent.setFont(parent.getFont());

		// validation
		liveValidationPreferences(parent);

		filteredValidationPreferences(parent);
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPreferencePageID()
	 */
	@Override
	protected String getPreferencePageID()
	{
		return PREFERENCE_PAGE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPropertyPageID()
	 */
	@Override
	protected String getPropertyPageID()
	{
		return PROPERTY_PAGE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performOk()
	 */
	@Override
	public boolean performOk()
	{

		if (useProjectSettings())
		{
			ValidationPreferencesAccessor.setProjectSpecific(getProject(), true);
			setValidationTypePreference(getProject());
			setFilteredValidationTypePreference(getProject());
		}
		else
		{
			if (getProject() != null)
			{
				ValidationPreferencesAccessor.setProjectSpecific(getProject(), getFUseProjectSettings());
			}
			else
			{
				setValidationTypePreference(null);
				setFilteredValidationTypePreference(null);
			}

		}

		return true;
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performDefaults()
 */
	@Override
	protected void performDefaults()
	{
		setValidationButtons(EValidationType.FULL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#initialFUseProjectSettings()
	 */
	@Override
	protected boolean initialFUseProjectSettings()
	{
		return ValidationPreferencesAccessor.getProjectSpecific(getProject());
	}

	/**
	 * @param scrollableComposite
	 */
	private void liveValidationPreferences(final Composite parent)
	{
		ExpandableComposite liveValidationExpandableComposite = CessarFormToolkit.DEFAULT.createExpandableComposite(
			parent, ExpandableComposite.TWISTIE);
		liveValidationExpandableComposite.setText("Live validation (during editing)"); //$NON-NLS-1$
		liveValidationExpandableComposite.setBackground(parent.getBackground());
		GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		liveValidationExpandableComposite.setLayoutData(gd);
		liveValidationExpandableComposite.setExpanded(true);
		Composite compositeForExpansion = CessarFormToolkit.DEFAULT.createPlainComposite(
			liveValidationExpandableComposite, SWT.None);
		liveValidationExpandableComposite.setClient(compositeForExpansion);
		GridLayout gridLayoutE = new GridLayout(1, false);
		compositeForExpansion.setLayout(gridLayoutE);

		liveValidationExpandableComposite.addExpansionListener(new ExpansionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.ui.forms.events.ExpansionAdapter#expansionStateChanged(org.eclipse.ui.forms.events.ExpansionEvent
			 * )
			 */
			@Override
			public void expansionStateChanged(ExpansionEvent e)
			{
				parent.getParent().pack(true);
			}
		});

		// // Live Validation
		Group liveValidationGroup = new Group(compositeForExpansion, SWT.NONE);
		liveValidationGroup.setText("Live validation"); //$NON-NLS-1$
		liveValidationGroup.setLayout(new GridLayout(1, false));
		liveValidationGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		liveValidationButton1 = new Button(liveValidationGroup, SWT.RADIO);
		liveValidationButton1.setText("Perform full validation of the selected object (slow but accurate)"); //$NON-NLS-1$
		// liveValidationButton1.setEnabled(true);

		liveValidationButton2 = new Button(liveValidationGroup, SWT.RADIO);
		liveValidationButton2.setText("Disable live validation but show messages from last on-demand validation (fast but potentially show obsolete message)"); //$NON-NLS-1$
		// liveValidationButton2.setEnabled(true);

		liveValidationButton3 = new Button(liveValidationGroup, SWT.RADIO);
		liveValidationButton3.setText("Disable live validation (faster)"); //$NON-NLS-1$
		// liveValidationButton3.setEnabled(true);

		EValidationType validationType;
		if (isProjectPreferencePage())
		{
			validationType = ValidationPreferencesAccessor.getValidationType(getProject());
		}
		else
		{
			validationType = ValidationPreferencesAccessor.getValidationType();
		}
		if (validationType != null)
		{
			setValidationButtons(validationType);
		}

	}

	/**
	 * @param scrollableComposite
	 */
	@Requirement(
		reqID = "21576")
	private void filteredValidationPreferences(final Composite parent)
	{
		ExpandableComposite filteredValidationExpandableComposite = CessarFormToolkit.DEFAULT.createExpandableComposite(
			parent, ExpandableComposite.TWISTIE);
		filteredValidationExpandableComposite.setText("Filtered validation"); //$NON-NLS-1$
		filteredValidationExpandableComposite.setBackground(parent.getBackground());
		GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		filteredValidationExpandableComposite.setLayoutData(gd);
		filteredValidationExpandableComposite.setExpanded(true);
		Composite compositeForExpansion = CessarFormToolkit.DEFAULT.createPlainComposite(
			filteredValidationExpandableComposite, SWT.None);
		filteredValidationExpandableComposite.setClient(compositeForExpansion);
		GridLayout gridLayoutE = new GridLayout(1, false);
		compositeForExpansion.setLayout(gridLayoutE);

		filteredValidationExpandableComposite.addExpansionListener(new ExpansionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.ui.forms.events.ExpansionAdapter#expansionStateChanged(org.eclipse.ui.forms.events.ExpansionEvent
			 * )
			 */
			@Override
			public void expansionStateChanged(ExpansionEvent e)
			{
				parent.getParent().pack(true);
			}
		});

		filteredValidationButton = new Button(compositeForExpansion, SWT.CHECK);
		filteredValidationButton.setText("Do not show wizard when performing filtered validation"); //$NON-NLS-1$
		filteredValidationButton.setEnabled(true);

		boolean hideWizard = false;
		if (isProjectPreferencePage())
		{
			hideWizard = ValidationPreferencesAccessor.getHideFilteredValidationWizardFlag(getProject());
		}
		else
		{
			hideWizard = ValidationPreferencesAccessor.getHideFilteredValidationWizardFlag();
		}
		filteredValidationButton.setSelection(hideWizard);

	}

	private void setValidationButtons(EValidationType validationType)
	{
		if (validationType.compareTo(EValidationType.FULL) == 0)
		{
			liveValidationButton1.setSelection(true);
			liveValidationButton2.setSelection(false);
			liveValidationButton3.setSelection(false);
		}
		else
		{
			if (validationType.compareTo(EValidationType.ON_DEMAND) == 0)
			{
				liveValidationButton1.setSelection(false);
				liveValidationButton2.setSelection(true);
				liveValidationButton3.setSelection(false);
			}
			else
			{
				if (validationType.compareTo(EValidationType.NONE) == 0)
				{
					liveValidationButton1.setSelection(false);
					liveValidationButton2.setSelection(false);
					liveValidationButton3.setSelection(true);
				}
			}
		}
	}

	private void setValidationTypePreference(IProject project)
	{
		if (liveValidationButton1.getSelection())
		{
			if (project != null)
			{
				ValidationPreferencesAccessor.setValidationType(project, EValidationType.FULL);
			}
			else
			{
				ValidationPreferencesAccessor.setValidationType(EValidationType.FULL);
			}
		}
		else
		{
			if (liveValidationButton2.getSelection())
			{
				if (project != null)
				{
					ValidationPreferencesAccessor.setValidationType(project, EValidationType.ON_DEMAND);
				}
				else
				{
					ValidationPreferencesAccessor.setValidationType(EValidationType.ON_DEMAND);
				}
			}
			else
			{
				if (liveValidationButton3.getSelection())
				{
					if (project != null)
					{
						ValidationPreferencesAccessor.setValidationType(project, EValidationType.NONE);
					}
					else
					{
						ValidationPreferencesAccessor.setValidationType(EValidationType.NONE);
					}

				}
			}
		}
	}

	private void setFilteredValidationTypePreference(IProject project)
	{
		boolean selection = filteredValidationButton.getSelection();
		if (project != null)
		{
			ValidationPreferencesAccessor.setHideFilteredValidationWizardFlag(project, selection);
		}
		else
		{
			ValidationPreferencesAccessor.setHideFilteredValidationWizardFlag(selection);
		}

	}
}
