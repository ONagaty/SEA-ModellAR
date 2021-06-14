/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 20, 2010 11:54:50 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.prefandprop;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.ControlEnableState;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PreferencesUtil;

import eu.cessar.ct.core.internal.platform.ui.prefandprop.LayoutUtil;
import eu.cessar.ct.core.internal.platform.ui.prefandprop.ProjectSelectionDialog;
import eu.cessar.ct.core.platform.PlatformConstants;

/**
 * @author uidt2045
 * 
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractPreferenceAndPropertyPage extends PreferencePage implements IWorkbenchPreferencePage,
	IWorkbenchPropertyPage
{
	private Control fConfigurationBlockControl;
	private ControlEnableState fBlockEnableState;
	private Link fChangeWorkspaceSettings;
	private SelectionButtonField fUseProjectSettings;
	private Composite fParentComposite;

	private IProject fProject; // project or null
	private Map fData; // page data

	public static final String DATA_NO_LINK = "PropertyAndPreferencePage.nolink"; //$NON-NLS-1$

	public AbstractPreferenceAndPropertyPage()
	{
		fBlockEnableState = null;
		fProject = null;
		fData = null;
	}

	protected abstract Control createPreferenceContent(Composite composite);

	protected boolean hasProjectSpecificOptions(IProject project)
	{
		if (project != null && getFUseProjectSettings())
		{
			return true;
		}
		return false;
	}

	protected abstract String getPreferencePageID();

	protected abstract String getPropertyPageID();

	protected boolean supportsProjectSpecificOptions()
	{
		return getPropertyPageID() != null;
	}

	protected boolean offerLink()
	{
		return fData == null || !Boolean.TRUE.equals(fData.get(DATA_NO_LINK));
	}

	@Override
	protected Label createDescriptionLabel(Composite parent)
	{
		fParentComposite = parent;
		if (isProjectPreferencePage())
		{
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setFont(parent.getFont());
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.numColumns = 2;
			composite.setLayout(layout);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			IDialogFieldListener listener = new IDialogFieldListener()
			{
				public void dialogFieldChanged(DialogField field)
				{
					boolean enabled = ((SelectionButtonField) field).isSelected();
					enableProjectSpecificSettings(enabled);

					if (enabled && getData() != null)
					{
						applyData(getData());
					}
				}
			};

			fUseProjectSettings = new SelectionButtonField(SWT.CHECK);

			fUseProjectSettings.setSelection(initialFUseProjectSettings());

			fUseProjectSettings.setDialogFieldListener(listener);
			fUseProjectSettings.setLabelText("Project Specific"); //$NON-NLS-1$
			fUseProjectSettings.doFillIntoGrid(composite, 1);
			LayoutUtil.setHorizontalGrabbing(fUseProjectSettings.getSelectionButton(null));

			if (offerLink())
			{
				fChangeWorkspaceSettings = createLink(composite, "Configure Workspace Settings"); //$NON-NLS-1$
				fChangeWorkspaceSettings.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
			}
			else
			{
				LayoutUtil.setHorizontalSpan(fUseProjectSettings.getSelectionButton(null), 2);
			}

			Label horizontalLine = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			horizontalLine.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
			horizontalLine.setFont(composite.getFont());
		}
		else if (supportsProjectSpecificOptions() && offerLink())
		{
			fChangeWorkspaceSettings = createLink(parent, "Configure Project Specific Settings"); //$NON-NLS-1$
			fChangeWorkspaceSettings.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		}

		return super.createDescriptionLabel(parent);
	}

	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setFont(parent.getFont());

		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);

		fConfigurationBlockControl = createPreferenceContent(composite);
		fConfigurationBlockControl.setLayoutData(data);

		if (isProjectPreferencePage())
		{
			boolean useProjectSettings = hasProjectSpecificOptions(getProject());
			enableProjectSpecificSettings(useProjectSettings);
		}

		Dialog.applyDialogFont(composite);
		return composite;
	}

	private Link createLink(Composite composite, String text)
	{
		Link link = new Link(composite, SWT.NONE);
		link.setFont(composite.getFont());
		link.setText("<A>" + text + "</A>"); //$NON-NLS-1$//$NON-NLS-2$
		link.addSelectionListener(new SelectionListener()
		{
			public void widgetSelected(SelectionEvent e)
			{
				doLinkActivated();
			}

			public void widgetDefaultSelected(SelectionEvent e)
			{
				doLinkActivated();
			}
		});
		return link;
	}

	protected boolean useProjectSettings()
	{
		return isProjectPreferencePage() && fUseProjectSettings != null && fUseProjectSettings.isSelected();
	}

	protected boolean isProjectPreferencePage()
	{
		return fProject != null;
	}

	protected IProject getProject()
	{
		return fProject;
	}

	/**
	 * Handle link activation.
	 * 
	 * @param link
	 *        the link
	 */
	final void doLinkActivated()
	{
		Map data = getData();
		if (data == null)
		{
			data = new HashMap();
		}
		data.put(DATA_NO_LINK, Boolean.TRUE);

		if (isProjectPreferencePage())
		{
			openWorkspacePreferences(data);
		}
		else
		{
			Set<IProject> projectsWithSpecifics = new HashSet<IProject>();
			Collection<IProject> projects = ExtendedPlatform.getProjects(PlatformConstants.CESSAR_NATURE);
			for (IProject project: projects)
			{
				projectsWithSpecifics.add(project);
			}
			ProjectSelectionDialog dialog = new ProjectSelectionDialog(getShell(), projectsWithSpecifics);
			if (dialog.open() == Window.OK)
			{
				IProject res = (IProject) dialog.getFirstResult();
				openProjectProperties(res.getProject(), data);
			}
		}
	}

	protected final void openWorkspacePreferences(Object data)
	{
		String id = getPreferencePageID();
		PreferencesUtil.createPreferenceDialogOn(getShell(), id, new String[] {id}, data).open();
	}

	protected final void openProjectProperties(IProject project, Object data)
	{
		String id = getPropertyPageID();
		if (id != null)
		{
			PreferencesUtil.createPropertyDialogOn(getShell(), project, id, new String[] {id}, data).open();
		}
	}

	protected void enableProjectSpecificSettings(boolean useProjectSpecificSettings)
	{
		fUseProjectSettings.setSelection(useProjectSpecificSettings);
		enablePreferenceContent(useProjectSpecificSettings);
		updateLinkVisibility();
		// doStatusChanged();
	}

	private void updateLinkVisibility()
	{
		if (fChangeWorkspaceSettings == null || fChangeWorkspaceSettings.isDisposed())
		{
			return;
		}

		if (isProjectPreferencePage())
		{
			fChangeWorkspaceSettings.setEnabled(!useProjectSettings());
		}
	}

	protected void enablePreferenceContent(boolean enable)
	{
		if (enable)
		{
			if (fBlockEnableState != null)
			{
				fBlockEnableState.restore();
				fBlockEnableState = null;
			}
		}
		else
		{
			if (fBlockEnableState == null)
			{
				fBlockEnableState = ControlEnableState.disable(fConfigurationBlockControl);
			}
		}
	}

	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults()
	{
		if (useProjectSettings())
		{
			enableProjectSpecificSettings(false);
		}
		super.performDefaults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
	 */
	public IAdaptable getElement()
	{
		return fProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
	 */
	public void setElement(IAdaptable element)
	{
		Object adapter = element.getAdapter(IResource.class);

		if (adapter instanceof IProject)
		{
			fProject = (IProject) adapter;
		}
		else if (adapter instanceof IFile)
		{
			IFile iFile = (IFile) adapter;
			fProject = iFile.getProject();
		}
		else if (adapter instanceof IFolder)
		{
			IFolder iFolder = (IFolder) adapter;
			fProject = iFolder.getProject();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#applyData(java.lang.Object)
	 */
	@Override
	public void applyData(Object data)
	{
		if (data instanceof Map)
		{
			fData = (Map) data;
		}
		if (fChangeWorkspaceSettings != null)
		{
			if (!offerLink())
			{
				fChangeWorkspaceSettings.dispose();
				fParentComposite.layout(true, true);
			}
		}
	}

	protected Map getData()
	{
		return fData;
	}

	@Override
	public abstract boolean performOk();

	protected boolean getFUseProjectSettings()
	{
		return fUseProjectSettings.isSelected();
	}

	protected abstract boolean initialFUseProjectSettings();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench)
	{
		// TODO Auto-generated method stub
	}

	protected void setEnblementOfProjectSetting(boolean enable)
	{
		fUseProjectSettings.setEnabled(enable);
	}

}
