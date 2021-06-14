/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Mar 3, 2014 10:07:32 AM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.wizards;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import eu.cessar.ct.cid.bindings.PlugetBinding;
import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.cid.model.Property;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.ui.internal.Messages;
import eu.cessar.ct.runtime.ecuc.util.RunPlugetUtils;

/**
 * Wizard page that allows the selection of a pluget from the project or global
 *
 * @author uidg4020
 *
 *         %created_by: uid95856 %
 *
 *         %date_created: Fri Mar 13 13:33:58 2015 %
 *
 *         %version: 8 %
 */
public class PlugetSelectionPage extends WizardPage
{
	private IProject project;
	private TableViewer viewerGlobal;
	private TableViewer viewerProject;
	private boolean hasLocalDetails;
	/**
	 * Selected pluget. Can be an {@link IFile} if the pluget is from project, else is an {@link Artifact} for an global
	 * pluget
	 */
	private IPluget selectedPluget;

	/**
	 * Contract both for plugets that can be referenced in cid file by an artifact and for plugets that are not
	 * referenced in cid file
	 *
	 * @author uidg4020
	 *
	 */
	public static interface IPluget
	{
		/**
		 * Returns the pluget name
		 *
		 * @return pluget name
		 */
		public String getName();

		/**
		 * Returns true if the pluget is
		 *
		 * @return true if the pluget is referenced by an artifact, else returns false
		 */
		public boolean isArtifact();

	}

	/**
	 * Implementation of the {@link IPluget} for the plugets referenced in cid file by an artifact. It holds the
	 * {@link Artifact} binded to the pluget
	 *
	 * @author uidg4020
	 *
	 */
	public class PlugetArtifact implements IPluget
	{
		/**
		 * {@link Artifact} binded to the pluget
		 **/
		private Artifact pluget;

		/**
		 *
		 * @param pluget
		 */
		public PlugetArtifact(Artifact pluget)
		{
			this.pluget = pluget;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.IPluget#getName()
		 */
		@Override
		public String getName()
		{
			String name = pluget.getName();
			IArtifactBinding binding = pluget.getConcreteBinding();
			PlugetBinding plugetBinding = (PlugetBinding) binding;
			// execute pluget if global
			if (plugetBinding.isProjectPluget())
			{
				IFile plugetFile = plugetBinding.getPlugetFile();
				name = plugetFile.getName();
			}
			return name;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.IPluget#isArtifact()
		 */
		@Override
		public boolean isArtifact()
		{
			return true;
		}

		/**
		 *
		 * @return artifact for this pluget
		 */
		public Artifact getArtifact()
		{
			return pluget;
		}
	}

	/**
	 * Implementation of the {@link IPluget} for the plugets that are not referenced in cid file. It holds the
	 * {@link IFile} pluget.
	 *
	 * @author uidg4020
	 *
	 */
	public class PlugetFile implements IPluget
	{
		/**
		 * {@link IFile} that holds pluget
		 **/
		private IFile pluget;

		/**
		 *
		 * @param pluget
		 */
		public PlugetFile(IFile pluget)
		{
			this.pluget = pluget;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.IPluget#getName()
		 */
		@Override
		public String getName()
		{
			String name = pluget.getName();
			return name;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.IPluget#isArtifact()
		 */
		@Override
		public boolean isArtifact()
		{
			return false;
		}

		/**
		 *
		 * @return file that holds pluget
		 */
		public IFile getPlugetFile()
		{
			return pluget;
		}
	}

	/**
	 * @param pageName
	 * @param project
	 */
	@SuppressWarnings("nls")
	public PlugetSelectionPage(String pageName, IProject project)
	{
		super(pageName);

		this.project = project;
		setTitle("Plugets");
		setDescription("Select a pluget");
		setPageComplete(false);
	}

	private void createColumns(final TableViewer viewer, boolean hasDetails)
	{
		TableViewerColumn col = createTableViewerColumn(Messages.PLUGET_NAME, 200, viewer);
		col.setLabelProvider(new CellLabelProvider()
		{
			@Override
			public void update(ViewerCell cell)
			{
				IPluget element = (IPluget) cell.getElement();
				String title;
				if (element.isArtifact())
				{
					PlugetArtifact artifact = (PlugetArtifact) element;
					title = artifact.getArtifact().getTitle();
				}
				else
				{
					title = element.getName();
					if (title.endsWith(".pluget")) //$NON-NLS-1$
					{
						title = title.substring(0, title.lastIndexOf('.'));
					}
				}

				cell.setText(title);
			}
		});

		if (hasDetails)
		{
			col = createTableViewerColumn(Messages.PLUGET_VERSION_KEY, 100, viewer);
			col.setLabelProvider(new CellLabelProvider()
			{
				@Override
				public void update(ViewerCell cell)
				{
					IPluget element = (IPluget) cell.getElement();
					if (element.isArtifact())
					{
						PlugetArtifact artifact = (PlugetArtifact) cell.getElement();
						List<Property> props = artifact.getArtifact().getProperties(Messages.PLUGET_VERSION_KEY);
						if (props != null && props.size() > 0)
						{
							cell.setText(props.get(0).getValue());
						}
					}
				}
			});

			col = createTableViewerColumn(Messages.PLUGET_DESCRIPTION_KEY, 350, viewer);
			col.setLabelProvider(new CellLabelProvider()
			{
				String desc = ""; //$NON-NLS-1$

				@Override
				public void update(ViewerCell cell)
				{
					IPluget element = (IPluget) cell.getElement();
					if (element.isArtifact())
					{
						PlugetArtifact artifact = (PlugetArtifact) cell.getElement();
						List<Property> props = artifact.getArtifact().getProperties(Messages.PLUGET_DESCRIPTION_KEY);
						if (props != null && props.size() > 0)
						{
							desc = props.get(0).getValue();
						}

						cell.setText(desc);
					}
				}
			});

			col = createTableViewerColumn(Messages.PLUGET_DOMAIN_KEY, 100, viewer);
			col.setLabelProvider(new CellLabelProvider()
			{
				@Override
				public void update(ViewerCell cell)
				{
					IPluget element = (IPluget) cell.getElement();
					if (element.isArtifact())
					{
						PlugetArtifact artifact = (PlugetArtifact) cell.getElement();
						List<Property> props = artifact.getArtifact().getProperties(Messages.PLUGET_DOMAIN_KEY);
						if (props != null && props.size() > 0)
						{
							cell.setText(props.get(0).getValue());
						}
					}
				}
			});
		}
	}

	// CHECKSTYLE:OFF
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{

		List<IPluget> plugetsGlobal = new ArrayList<PlugetSelectionPage.IPluget>();
		List<IPluget> plugetsProject = new ArrayList<PlugetSelectionPage.IPluget>();

		// get artifacts
		if (project != null)
		{
			// get all plugets from project
			List<IPath> existingPlugetPaths = new ArrayList<IPath>();

			List<Artifact> artifactPlugets = RunPlugetUtils.INSTANCE.getArtifactPlugets(project);
			for (Artifact pluget: artifactPlugets)
			{
				PlugetArtifact plugetArtifact = new PlugetArtifact(pluget);
				PlugetBinding pBinding = (PlugetBinding) pluget.getConcreteBinding();
				existingPlugetPaths.add(getArtifactPath(pluget));
				boolean isProjectPluget = pBinding.isProjectPluget();

				if (isProjectPluget)
				{
					hasLocalDetails = true;
					plugetsProject.add(plugetArtifact);
				}
				else if (pluget.getTitle() != null)
				{
					plugetsGlobal.add(plugetArtifact);
				}
			}

			List<IFile> projectPlugets = RunPlugetUtils.INSTANCE.getProjectPlugets(project);
			for (IFile pluget: projectPlugets)
			{
				PlugetFile plugetFile = new PlugetFile(pluget);
				if (!existingPlugetPaths.contains(pluget.getFullPath()))
				{
					plugetsProject.add(plugetFile);
				}
			}

		}

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout(1, false));
		setControl(container);

		GridLayout gridLayout = new GridLayout(1, false);
		container.setLayout(gridLayout);

		Label origLabel = new Label(container, SWT.NONE);
		origLabel.setText(Messages.PLUGET_CESSAR + ":"); //$NON-NLS-1$
		FontData fontData = origLabel.getFont().getFontData()[0];
		Font bfont = new Font(parent.getDisplay(), fontData.getName(), fontData.getHeight(), SWT.BOLD);
		origLabel.setFont(bfont);

		GridData labelData = new GridData();
		labelData.verticalAlignment = SWT.TOP;
		origLabel.setLayoutData(labelData);

		// CONFIGURE GLOBAL PLUGETS TABLE
		viewerGlobal = new TableViewer(container, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		ColumnViewerToolTipSupport.enableFor(viewerGlobal);

		Table table = viewerGlobal.getTable();

		createColumns(viewerGlobal, true);

		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_table.heightHint = 150;
		gd_table.widthHint = 750;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// -------------------------------

		origLabel = new Label(container, SWT.NONE);
		origLabel.setText(Messages.PLUGET_PROJECT + ":"); //$NON-NLS-1$
		origLabel.setFont(bfont);

		origLabel.setLayoutData(labelData);

		// CONFIGURE LOCAL PLUGETS TABLE
		viewerProject = new TableViewer(container, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		Table ptable = viewerProject.getTable();

		createColumns(viewerProject, hasLocalDetails);

		ptable.setLayoutData(gd_table);
		ptable.setHeaderVisible(true);
		ptable.setLinesVisible(true);
		// --------------------------------

		viewerGlobal.setContentProvider(ArrayContentProvider.getInstance());
		viewerProject.setContentProvider(ArrayContentProvider.getInstance());

		viewerGlobal.setInput(plugetsGlobal);
		viewerProject.setInput(plugetsProject);

		viewerGlobal.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) viewerGlobal.getSelection();
				IPluget pluget = PlatformUIUtils.getObjectFromSelection(IPluget.class, selection);
				if (pluget == null)
				{
					return;
				}

				configurePopup(pluget);

				selectedPluget = pluget;
				// can go to next page
				getContainer().updateButtons();

			}
		});

		viewerProject.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) viewerProject.getSelection();
				IPluget pluget = PlatformUIUtils.getObjectFromSelection(IPluget.class, selection);
				if (pluget == null)
				{
					return;
				}

				if (pluget instanceof PlugetArtifact)
				{
					configurePopup(pluget);
				}

				selectedPluget = pluget;
				// can go to next page
				getContainer().updateButtons();

			}
		});

		ViewerComparator comparator = new ViewerComparator()
		{
			@Override
			public int compare(Viewer viewer, Object e1, Object e2)
			{
				return ((IPluget) e1).getName().compareTo(((IPluget) e2).getName());
			}
		};

		viewerGlobal.setComparator(comparator);
		viewerProject.setComparator(comparator);
		viewerProject.getTable().getColumn(0).pack();
	}

	/**
	 * Return the selected pluget. If the selected pluget is in project, it will be an {@link IFile}, else the pluget is
	 * external and is an {@link Artifact}
	 *
	 *
	 * @return the selected pluget
	 */
	public IPluget getSelectedPluget()
	{
		return selectedPluget;
	}

	/**
	 * @return Title of the pluget selected in the dialog.
	 */
	public String getSelectedPlugetTitle()
	{
		if (selectedPluget.isArtifact())
		{
			return ((PlugetArtifact) selectedPluget).getArtifact().getTitle();
		}
		return selectedPluget.getName().replace(".pluget", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void configurePopup(IPluget pluget)
	{
		PlugetArtifact artifact = (PlugetArtifact) pluget;

		// map containing the user manual and release notes file with the message shown in the UI as key
		Map<String, File> messageToFileMap = new HashMap<>();
		File docuFile = null;
		IArtifactBinding binding = artifact.getArtifact().getConcreteBinding();
		PlugetBinding plugetBinding = (PlugetBinding) binding;
		try
		{
			URL plugetDocLocation = plugetBinding.getPlugetDocLocation(Messages.PLUGET_DOCU_LINK_KEY.toLowerCase());
			if (plugetDocLocation != null)
			{
				URL fileURL = FileLocator.toFileURL(plugetDocLocation);
				docuFile = new File(fileURL.getPath());
				if (docuFile.isFile())
				{
					messageToFileMap.put(Messages.DOCUMENTATION_LINK_TEXT, docuFile);
				}
			}
			plugetDocLocation = plugetBinding.getPlugetDocLocation(Messages.PLUGET_NOTES_LINK_KEY.toLowerCase());
			if (plugetDocLocation != null)
			{
				URL fileURL = FileLocator.toFileURL(plugetDocLocation);
				docuFile = new File(fileURL.getPath());
				if (docuFile.isFile())
				{
					messageToFileMap.put(Messages.RELEASENOTES_LINK_TEXT, docuFile);
				}
			}
		}
		catch (MalformedURLException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (URISyntaxException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		Rectangle rect = new Rectangle(110, 220, 500, 330);
		Map<String, String> content = new LinkedHashMap<String, String>();

		List<Property> pps = artifact.getArtifact().getProperties(Messages.PLUGET_DESCRIPTION_KEY);
		if (pps != null && pps.size() > 0)
		{
			content.put(Messages.PLUGET_EMPTY_KEY, pps.get(0).getValue());
		}
		else
		{
			content.put(Messages.PLUGET_EMPTY_KEY, ""); //$NON-NLS-1$
		}

		pps = artifact.getArtifact().getProperties(Messages.PLUGET_DOMAIN_KEY);

		if (pps != null && pps.size() > 0)
		{
			content.put(Messages.PLUGET_DOMAIN_KEY, pps.get(0).getValue());
		}
		else
		{
			content.put(Messages.PLUGET_DOMAIN_KEY, ""); //$NON-NLS-1$
		}

		pps = artifact.getArtifact().getProperties(Messages.PLUGET_VERSION_KEY);

		if (pps != null && pps.size() > 0)
		{
			content.put(Messages.PLUGET_VERSION_KEY, pps.get(0).getValue());
		}

		pps = artifact.getArtifact().getProperties(Messages.PLUGET_RELEASE_DATE_KEY);

		if (pps != null && pps.size() > 0)
		{
			content.put(Messages.PLUGET_RELEASE_DATE, pps.get(0).getValue());
		}

		MessagePopup popGlobal = new MessagePopup(viewerGlobal.getTable().getShell(), rect, content, messageToFileMap);
		popGlobal.open();
	}

	/**
	 * @param pLUGET_DOCU_LINK_KEY
	 * @param files
	 * @param filesDocIndex
	 * @return
	 */
	private int getPlugetDocFile(String pLUGET_DOCU_LINK_KEY, File[] files, int filesDocIndex)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Get IPath for this artifact
	 *
	 * @param artifact
	 * @return IPath for this artifact
	 */
	private IPath getArtifactPath(Artifact artifact)
	{

		IArtifactBinding binding = artifact.getConcreteBinding();
		PlugetBinding plugetBinding = (PlugetBinding) binding;
		boolean projectPluget = plugetBinding.isProjectPluget();
		if (projectPluget)
		{
			IFile filePluget = plugetBinding.getPlugetFile();
			IPath fullPath = filePluget.getFullPath();
			return fullPath;
		}
		return null;
	}

	/**
	 * @param title
	 *        represents the title of the column
	 * @param bound
	 * @param colNumber
	 *        represents the index of the Column
	 * @return A TableViewer Column
	 */
	private TableViewerColumn createTableViewerColumn(String title, int bound, TableViewer viewer)
	{
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage()
	{
		if (selectedPluget != null)
		{
			return true;

		}
		return false;
	}

}
