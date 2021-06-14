/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Nov 24, 2010 4:27:30 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import eu.cessar.ct.core.mms.CompatPreferenceAccessor;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage;
import eu.cessar.ct.core.platform.ui.prefandprop.SelectionButtonField;

/**
 * @author uidt2045
 * 
 */
public class CompatibilityModePrefAndPropPage extends AbstractPreferenceAndPropertyPage
{

	private String prefPage = "eu.cessar.ct.workspace.ui.CompatModePreffPage"; //$NON-NLS-1$
	private String propPage = "eu.cessar.ct.workspace.ui.CompatModePropPage"; //$NON-NLS-1$

	private Group compatGroup;
	private SelectionButtonField postBuildButton;
	private SelectionButtonField multipleConfigButton;
	private SelectionButtonField useRefinement;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible)
	{
		if (isProjectPreferencePage())
		{
			setEnblementOfProjectSetting(isInCompatMode());
			if (isInCompatMode())
			{
				// enableProjectSpecificSettings(isInCompatMode());
				enableProjectSpecificSettings(CompatPreferenceAccessor.isProjectSpecificSettings(getProject()));
			}
			else
			{
				enableProjectSpecificSettings(false);
			}
			super.setVisible(isInCompatMode());
		}
		super.setVisible(visible);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#createPreferenceContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createPreferenceContent(Composite composite)
	{
		Composite parent = new Composite(composite, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		parent.setFont(parent.getFont());

		// CompatGroup settings
		compatGroup = new Group(parent, SWT.NONE);
		compatGroup.setText("Compatibility"); //$NON-NLS-1$

		layout = new GridLayout(3, false);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.horizontalSpan = 3;

		compatGroup.setLayoutData(gridData);
		compatGroup.setLayout(layout);

		postBuildButton = new SelectionButtonField(SWT.CHECK);
		postBuildButton.doFillIntoGrid(compatGroup, 3);
		postBuildButton.setLabelText("PostBuildChangeable flag sets the upperMultiplicity on infinite"); //$NON-NLS-1$

		postBuildButton.setSelection(CompatPreferenceAccessor.isPostBuild(getProject()));

		multipleConfigButton = new SelectionButtonField(SWT.CHECK);
		multipleConfigButton.doFillIntoGrid(compatGroup, 3);
		multipleConfigButton.setLabelText("MultipleConfigurationContainer flag sets the upperMultiplicity on infinite"); //$NON-NLS-1$

		multipleConfigButton.setSelection(CompatPreferenceAccessor.isMultiConfContainter(getProject()));

		useRefinement = new SelectionButtonField(SWT.CHECK);
		useRefinement.doFillIntoGrid(compatGroup, 3);
		useRefinement.setLabelText("Use the refinement concept"); //$NON-NLS-1$

		useRefinement.setSelection(CompatPreferenceAccessor.isUseRefinement(getProject()));

		return parent;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPreferencePageID()
	 */
	@Override
	protected String getPreferencePageID()
	{
		return prefPage;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPropertyPageID()
	 */
	@Override
	protected String getPropertyPageID()
	{
		return propPage;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performOk()
	 */
	@Override
	public boolean performOk()
	{
		IProject project = getProject();
		if (isProjectPreferencePage())
		{
			// if (useProjectSettings())
			// {
			CompatPreferenceAccessor.setProjectSpecificSettings(getProject(),
				getFUseProjectSettings());
			CompatPreferenceAccessor.setPostBuildInProject(project, postBuildButton.isSelected());
			CompatPreferenceAccessor.setMultiConfContainerInProject(project,
				multipleConfigButton.isSelected());
			CompatPreferenceAccessor.setUseRefinementInProject(project, useRefinement.isSelected());
			// }
		}
		else
		{
			if (project != null)
			{
				CompatPreferenceAccessor.setProjectSpecificSettings(project,
					getFUseProjectSettings());
			}
			else
			{
				CompatPreferenceAccessor.setMultiConfContainerInWS(multipleConfigButton.isSelected());
				CompatPreferenceAccessor.setPostBuildInWS(postBuildButton.isSelected());
				CompatPreferenceAccessor.setUseRefinementInWS(useRefinement.isSelected());
			}

		}

		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#initialFUseProjectSettings()
	 */
	@Override
	protected boolean initialFUseProjectSettings()
	{
		if (isInCompatMode())
		{
			return CompatPreferenceAccessor.isProjectSpecificSettings(getProject());
		}
		return false;
	}

	@Override
	public void performDefaults()
	{
		// set project specific to true
		if (isProjectPreferencePage())
		{
			if (isInCompatMode())
			{
				enableProjectSpecificSettings(true);
			}
		}
		postBuildButton.setSelection(true);
		multipleConfigButton.setSelection(false);
		useRefinement.setSelection(false);
	}

	private boolean isInCompatMode()
	{
		if (getProject() != null)
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(getProject());
			if (!compatibilityMode.name().equals(ECompatibilityMode.NONE.name()))
			{
				return true;
			}
		}
		return false;
	}

}
