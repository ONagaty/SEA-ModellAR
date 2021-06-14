/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Aug 16, 2010 6:01:17 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.platform.ui.dialogs.ContainerSelectionDialog;

/**
 * Wizard element that provides two kinds of browse for a location (project and workspace). Browse operations are
 * enabled on checkbox selection
 *
 * @author uidt2045
 *
 */
@SuppressWarnings("nls")
public class LocationDialogField
{

	private final static String LINK_NAME = "Location";
	private Shell shell;
	private IProject project;
	private Button[] buttons = new Button[1];
	private Text fLocationText;
	private IPath folderPath;
	private Listener fListener = new Listener();
	private Link fLocationLink;

	/**
	 *
	 * @author uid95856
	 *
	 *         %created_by: uid95856 %
	 *
	 *         %date_created: Wed Mar 4 11:35:32 2015 %
	 *
	 *         %version: 5 %
	 */
	public enum ButtonInfo
	{
		/**
		 *
		 */
		FOLDER(0, "Browse...");

		private int index;
		private String label;

		private ButtonInfo(int index, String label)
		{
			this.index = index;
			this.label = label;
		}

		/**
		 * @return label
		 */
		public String getLabel()
		{
			return label;
		}

		/**
		 * @param label
		 */
		public void setLabel(String label)
		{
			this.label = label;
		}

		/**
		 * @param labelParam
		 * @return label
		 */
		public ButtonInfo changeLabel(String labelParam)
		{
			label = labelParam;
			return this;
		}

		/**
		 * @return index
		 */
		public int getIndex()
		{
			return index;
		}

	}

	/**
	 * @param project
	 * @param shell
	 * @param parent
	 * @param enablement
	 * @param buttonInfos
	 */
	public LocationDialogField(IProject project, Shell shell, Composite parent, boolean enablement,
		ButtonInfo[] buttonInfos)
	{
		this.shell = shell;
		this.project = project;

		createText(parent, LINK_NAME, true);

		createButtons(parent, buttonInfos);

		enableBrowseSection(enablement);
	}

	/**
	 * @param shell
	 * @param parent
	 * @param enablement
	 * @param buttonInfos
	 */
	public LocationDialogField(Shell shell, Composite parent, boolean enablement, ButtonInfo[] buttonInfos)
	{
		this.shell = shell;
		project = null;

		createText(parent, LINK_NAME, true);

		createButtons(parent, buttonInfos);

		enableBrowseSection(enablement);
	}

	/**
	 * @param shell
	 * @param parent
	 * @param enablement
	 */
	public LocationDialogField(Shell shell, Composite parent, boolean enablement)
	{
		this.shell = shell;
		project = null;

		createText(parent, LINK_NAME, true);

		enableBrowseSection(enablement);
	}

	/**
	 * @param parent
	 * @param buttonInfos
	 */
	public void createButtons(Composite parent, ButtonInfo[] buttonInfos)
	{
		for (ButtonInfo buttonInfo: buttonInfos)
		{
			buttons[buttonInfo.getIndex()] = createButton(parent, buttonInfo.getLabel());
			buttons[buttonInfo.getIndex()].setLayoutData(createGridData(1));
		}

	}

	/**
	 * @param parent
	 * @param text
	 * @return button
	 */
	protected Button createButton(Composite parent, String text)
	{
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		button.setLayoutData(new GridData());
		button.addSelectionListener(fListener);
		return button;
	}

	class Listener extends SelectionAdapter implements ModifyListener
	{
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			Object source = e.getSource();

			if (buttons[ButtonInfo.FOLDER.getIndex()] != null && source == buttons[ButtonInfo.FOLDER.getIndex()])
			{
				// handleBrowseWorkspace();
				handleBrowseProject();
			}

		}

		public void modifyText(ModifyEvent e)
		{
			// fTab.scheduleUpdateJob();
		}
	}

	private static GridData createGridData(int colSpan)
	{
		GridData gd = new GridData();
		gd.horizontalSpan = colSpan;
		gd.horizontalAlignment = GridData.GRAB_HORIZONTAL;
		return gd;
	}

	/**
	 * @param parent
	 * @param text
	 */
	public void createText(Composite parent, String text, boolean editable)
	{
		fLocationLink = new Link(parent, SWT.NONE);
		fLocationLink.setText("<a>" + text + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		fLocationLink.setLayoutData(createGridData(1));

		fLocationText = new Text(parent, SWT.SINGLE | SWT.BORDER);

		GridData gd = createGridData(2);
		gd.widthHint = 300;
		fLocationText.setLayoutData(gd);
		fLocationText.addModifyListener(fListener);
		fLocationText.setEditable(editable);

		fLocationLink.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				try
				{
					File f = getFile(getLocationText());

					if (f != null && f.exists())
					{
						Program.launch(f.getCanonicalPath());
					}
					else
					{
						MessageDialog.openWarning(shell, "Open Directory",
							"The specified directory could not be found.");
					}
				}
				catch (Exception ex)// SUPPRESS CHECKSTYLE OK
				{
					// please change this in the future if additional actions
					// must be taken in case of exception
					CessarPluginActivator.getDefault().logError(ex);
				}
			}

		});
	}

	/**
	 *
	 * @param relativeLocation
	 * @return
	 */
	private File getFile(String relativeLocation)
	{
		File locationFile = null;
		String absoluteLocation = null;

		// try to see if this is a workspace location
		try
		{
			absoluteLocation = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(
				relativeLocation, false);

			if (absoluteLocation != null)
			{

				String[] absoluteLocationParts = absoluteLocation.split("\\\\");

				// if size is 2 then it contains only the project
				if (absoluteLocationParts.length == 2)
				{
					IProject projectPath = ResourcesPlugin.getWorkspace().getRoot().getProject(absoluteLocationParts[1]);

					if (projectPath != null)
					{
						return (projectPath.getLocation().makeAbsolute().toFile());
					}
				}

				// if bigger than one it contains a folder path
				if (absoluteLocationParts.length > 1)
				{
					Path path = new Path(absoluteLocation);
					IFolder iFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);

					if (iFolder.exists())
					{
						return iFolder.getRawLocation().makeAbsolute().toFile();
					}
				}
				locationFile = new File(absoluteLocation);

				if (!locationFile.exists())
				{
					// try to see if this is a project location
					if (project != null)
					{
						absoluteLocation = resolveProjectRelativeLocation(relativeLocation);
						locationFile = new File(absoluteLocation);
					}

					if (!locationFile.exists())
					{
						// try to see if this a system absolute path
						locationFile = new File(relativeLocation);
					}
				}
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return locationFile;

	}

	/**
	 *
	 * @param relativeLocation
	 * @return
	 */
	private String resolveProjectRelativeLocation(String relativeLocation)
	{
		String absoluteLocation = null;

		String correctedRelativeLocation = relativeLocation.replace('\\', File.separatorChar);
		correctedRelativeLocation = correctedRelativeLocation.replace('/', File.separatorChar);
		if (correctedRelativeLocation.startsWith(File.separator))
		{
			correctedRelativeLocation = correctedRelativeLocation.substring(File.separator.length());
		}
		if (correctedRelativeLocation.endsWith(File.separator))
		{
			correctedRelativeLocation = correctedRelativeLocation.substring(0, correctedRelativeLocation.length()
				- File.separator.length());
		}

		absoluteLocation = project.getLocation().toOSString() + File.separator + correctedRelativeLocation;

		return absoluteLocation;
	}

	/**
	 *
	 */
	private void handleBrowseWorkspace()
	{
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(shell, ResourcesPlugin.getWorkspace().getRoot(),
			true, "PATH");

		if (dialog.open() == Window.OK)
		{
			Object[] result = dialog.getResult();
			if (result.length == 0)
			{
				return;
			}
			folderPath = (IPath) result[0];

			fLocationText.setText(/* "${resource_path:/" + */folderPath.makeRelative().toString() /* + "}" */);
		}
	}

	/**
	 *
	 */
	private void handleBrowseProject()
	{
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(shell, project, true, "Project Path:");

		if (dialog.open() == Window.OK)
		{
			Object[] result = dialog.getResult();
			if (result.length == 0)
			{
				return;
			}
			folderPath = (IPath) result[0];
			fLocationText.setText(folderPath.makeRelative().removeFirstSegments(1).toString());

		}
	}

	/**
	 * @param enabled
	 */
	public void enableBrowseSection(boolean enabled)
	{
		fLocationLink.setEnabled(enabled);
		fLocationText.setEnabled(enabled);
		for (Button button: buttons)
		{
			if (button != null)
			{
				button.setEnabled(enabled);
			}
		}
	}

	/**
	 * @return location
	 */
	public String getLocationToSave()
	{
		String location = fLocationText.getText().trim();
		return location;
	}

	private String getLocationText()
	{
		return fLocationText.getText().trim();
	}

	/**
	 * Set the location that shall be displayed into the UI
	 *
	 * @param location
	 */
	public void setLocation(String location)
	{
		folderPath = new Path(location);
		fLocationText.setText(location);
	}

}
