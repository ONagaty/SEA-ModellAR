/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Aug 11, 2010 10:36:04 AM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.preferences;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage;
import eu.cessar.ct.core.platform.ui.prefandprop.DialogField;
import eu.cessar.ct.core.platform.ui.prefandprop.IDialogFieldListener;
import eu.cessar.ct.core.platform.ui.prefandprop.SelectionButtonField;
import eu.cessar.ct.core.platform.ui.widgets.LocationDialogField;
import eu.cessar.ct.runtime.CodegenPreferencesAccessor;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;

/**
 * @author uidt2045
 *
 */
public class CodeGenerationPrefAndPropPage extends AbstractPreferenceAndPropertyPage
{

	private String prefPage = "eu.cessar.ct.workspace.ui.CodeGenerationPrefPage"; //$NON-NLS-1$
	private String propPage = "eu.cessar.ct.workspace.ui.CodeGenerationPropPage"; //$NON-NLS-1$

	// dump
	private Group dumpGroup;
	private SelectionButtonField setDump;
	private LocationDialogField dumpLocation;

	// private DirectoryFieldEditor dumpFieldEditor;
	// private StringFieldEditor dumpFieldEditor;

	// output
	private Group outputGroup;
	private SelectionButtonField setOutput;
	private LocationDialogField outputLocation;

	// external changes option
	private Group externalChangesGroup;
	private SelectionButtonField setExternalChangesOption;
	private IFolder dumpFolder;

	// private DirectoryFieldEditor outputFieldEditor;
	// private StringFieldEditor outputFieldEditor;

	// error
	// private BooleanFieldEditor setError;

	// JVM param
	// private Group jvmGroup;

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

		Composite parent = new Composite(composite, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		parent.setFont(parent.getFont());

		// DumpGroup settings
		createDumpGroup(parent);

		// OutputGroup
		createOutputGroup(parent);

		// External changes option
		createExternalChangesGroup(parent);

		return parent;
	}

	/**
	 * @param parent
	 */
	@Requirement(
		reqID = "227389")
	private void createExternalChangesGroup(Composite parent)
	{
		GridLayout layout;
		externalChangesGroup = new Group(parent, SWT.NONE);
		externalChangesGroup.setText("Automatically reload external modified autosar configuration files"); //$NON-NLS-1$

		layout = new GridLayout(3, false);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.horizontalSpan = 3;

		externalChangesGroup.setLayoutData(gridData);
		externalChangesGroup.setLayout(layout);

		setExternalChangesOption = new SelectionButtonField(SWT.CHECK);
		setExternalChangesOption.doFillIntoGrid(externalChangesGroup, 3);
		setExternalChangesOption.setLabelText("Take into account the changes made on autosar configuration files outside Cessar-CT by reloading the model before code generation"); //$NON-NLS-1$

		setExternalChangesOption.setSelection(CodegenPreferencesAccessor.isExternalChangesOption(getProject()));
	}

	/**
	 * @param parent
	 */
	private void createDumpGroup(Composite parent)
	{
		GridLayout layout;
		dumpGroup = new Group(parent, SWT.NONE);
		dumpGroup.setText("Dump Folders"); //$NON-NLS-1$

		layout = new GridLayout(3, false);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.horizontalSpan = 3;

		dumpGroup.setLayoutData(gridData);
		dumpGroup.setLayout(layout);

		setDump = new SelectionButtonField(SWT.CHECK);
		setDump.doFillIntoGrid(dumpGroup, 3);
		setDump.setLabelText("Dump Jet Folder"); //$NON-NLS-1$

		IProject project = getProject();
		setDump.setSelection(CodegenPreferencesAccessor.isDumpingJetSource(project));
		try
		{
			if (project != null)
			{
				IPath folder = CodegenPreferencesAccessor.getResolvedDumpJetSourceFolder(project);
				String folderFullPath = folder.toString();
				int index = folderFullPath.indexOf(project.getName());
				if (index != -1)
				{
					String folderRelativePath = folderFullPath.substring(index + 1 + project.getName().length());
					dumpFolder = project.getFolder(folderRelativePath);
				}
				else
				{
					dumpFolder = project.getFolder(".src"); //$NON-NLS-1$
				}
			}

		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);

		}
		if (isProjectPreferencePage())
		{
			dumpLocation = new LocationDialogField(project, getShell(), dumpGroup, setDump.isSelected(),
				new LocationDialogField.ButtonInfo[] {LocationDialogField.ButtonInfo.FOLDER.changeLabel("Browse...")}); //$NON-NLS-1$
			dumpLocation.setLocation(CodegenPreferencesAccessor.getDumpJetSourceFolder(project));
		}
		else
		{
			dumpLocation = new LocationDialogField(getShell(), dumpGroup, setDump.isSelected());
			dumpLocation.setLocation(CodegenPreferencesAccessor.getDumpJetSourceFolder(null));
		}

		setDump.setDialogFieldListener(new IDialogFieldListener()
		{
			public void dialogFieldChanged(DialogField field)
			{
				dumpLocation.enableBrowseSection(setDump.isSelected());
			}
		});
	}

	/**
	 * @param parent
	 */
	private void createOutputGroup(Composite parent)
	{
		GridLayout layout;
		outputGroup = new Group(parent, SWT.NONE);
		outputGroup.setText("Output"); //$NON-NLS-1$

		layout = new GridLayout(3, false);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData.horizontalSpan = 3;

		outputGroup.setLayoutData(gridData);
		outputGroup.setLayout(layout);

		setOutput = new SelectionButtonField(SWT.CHECK);
		setOutput.doFillIntoGrid(outputGroup, 3);
		setOutput.setLabelText("Output"); //$NON-NLS-1$

		setOutput.setSelection(CodegenPreferencesAccessor.isUsingCustomOutputFolder(getProject()));

		if (isProjectPreferencePage())
		{
			outputLocation = new LocationDialogField(getProject(), getShell(), outputGroup, setOutput.isSelected(),
				new LocationDialogField.ButtonInfo[] {LocationDialogField.ButtonInfo.FOLDER.changeLabel("Browse...")}); //$NON-NLS-1$

			outputLocation.setLocation(CodegenPreferencesAccessor.getCustomOutputFolder(getProject()));
		}
		else
		{
			outputLocation = new LocationDialogField(getShell(), outputGroup, setOutput.isSelected());
			outputLocation.setLocation(CodegenPreferencesAccessor.getCustomOutputFolder(null));
		}

		setOutput.setDialogFieldListener(new IDialogFieldListener()
		{
			public void dialogFieldChanged(DialogField field)
			{
				outputLocation.enableBrowseSection(setOutput.isSelected());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.CessarPreferenceAndPropertyPage#getPreferencePageID()
	 */
	@Override
	protected String getPreferencePageID()
	{
		return prefPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.CessarPreferenceAndPropertyPage#getPropertyPageID()
	 */
	@Override
	protected String getPropertyPageID()
	{
		return propPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performOk()
	 */
	@SuppressWarnings({})
	@Override
	public boolean performOk()
	{
		IProject project = getProject();
		IJavaProject javaProject = null;
		IClasspathEntry[] oldEntries = null;
		IClasspathEntry[] newEntries = null;
		IClasspathEntry[] entriesToBeSet = null;
		try
		{
			if (project != null)
			{
				javaProject = JavaCore.create(project);
				oldEntries = javaProject.getRawClasspath();
				newEntries = new IClasspathEntry[oldEntries.length];
				entriesToBeSet = new IClasspathEntry[oldEntries.length];

				for (int i = 0; i < oldEntries.length; i++)
				{

					if (!oldEntries[i].getPath().equals(dumpFolder.getFullPath()))
					{
						newEntries[i] = oldEntries[i];
					}

				}
			}
		}
		catch (JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		if (useProjectSettings())
		{
			CodegenPreferencesAccessor.setProjectSpecificSettings(project, getFUseProjectSettings());
			CodegenPreferencesAccessor.setDumpJetSourceInProject(project, setDump.isSelected());
			CodegenPreferencesAccessor.setDumpJetSourceFolderInProject(project, dumpLocation.getLocationToSave());
			CodegenPreferencesAccessor.setUsingCustomOutputFolderInProject(project, setOutput.isSelected());
			CodegenPreferencesAccessor.setCustomOutputFolderInProject(project, outputLocation.getLocationToSave());
			CodegenPreferencesAccessor.setExternalChangesOptionInProject(project, setExternalChangesOption.isSelected());
		}
		else
		{
			if (project != null)
			{
				CodegenPreferencesAccessor.setProjectSpecificSettings(project, getFUseProjectSettings());
			}
			else
			{
				CodegenPreferencesAccessor.setDumpJetSourceInWS(setDump.isSelected());
				CodegenPreferencesAccessor.setDumpJetSourceFolderInWS(dumpLocation.getLocationToSave());
				CodegenPreferencesAccessor.setUsingCustomOutputFolderInWS(setOutput.isSelected());
				CodegenPreferencesAccessor.setCustomOutputFolderInWS(outputLocation.getLocationToSave());
				CodegenPreferencesAccessor.setExternalChangesOptionInWS(setExternalChangesOption.isSelected());
			}
		}

		try
		{
			String dumpJetSourceFolder = CodegenPreferencesAccessor.getDumpJetSourceFolder(project);
			if (project != null)
			{
				IFolder folder = project.getFolder(dumpJetSourceFolder);
				boolean sourceFolderIsAdded = false;
				for (IClasspathEntry iClasspathEntry: oldEntries)
				{
					if (iClasspathEntry.getPath().equals(folder.getFullPath()))
					{
						sourceFolderIsAdded = true;
					}
				}
				if (!sourceFolderIsAdded)
				{
					IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(folder);
					for (int i = 0; i < newEntries.length; i++)
					{
						if (newEntries[i] == null)
						{
							entriesToBeSet[i] = JavaCore.newSourceEntry(packageRoot.getPath(), new Path[] {});
						}
						else
						{
							entriesToBeSet[i] = newEntries[i];
						}
					}
					javaProject.setRawClasspath(entriesToBeSet, null);
				}
			}
		}
		catch (JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return true;
	}

	@Override
	public void performDefaults()
	{
		setDump.setSelection(false);
		setOutput.setSelection(false);
		setExternalChangesOption.setSelection(false);
		super.performDefaults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#defaultFUseProjectSettings()
	 */
	@Override
	protected boolean initialFUseProjectSettings()
	{
		return CodegenPreferencesAccessor.isProjectSpecificSettings(getProject());
	}

}
