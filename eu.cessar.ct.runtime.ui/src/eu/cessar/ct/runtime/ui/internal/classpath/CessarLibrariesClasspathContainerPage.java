/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg3464<br/>
 * Jan 8, 2014 11:40:56 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.internal.classpath;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.classpath.BundleWrapper;
import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ui.internal.Messages;
import eu.cessar.ct.runtime.utils.BundleUtils;
import eu.cessar.ct.runtime.utils.RequiredPluginsAccessor;
import eu.cessar.req.Requirement;

/**
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Mon Feb  3 15:14:03 2014 %
 * 
 *         %version: 4 %
 */
// SUPPRESS CHECKSTYLE Avoid Checkstyle errors
@Requirement(
	reqID = "REQ_RUNTIME#1")
public class CessarLibrariesClasspathContainerPage extends WizardPage implements IClasspathContainerPage,
	IClasspathContainerPageExtension
{
	private static Color colorYellow = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	private static Color colorGray = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	private Text searchText;
	private TableViewer viewer;
	private Map<String, PluginModelBaseWrapper> modified = new HashMap<String, PluginModelBaseWrapper>();
	private IJavaProject project;
	private PluginModelBaseWrapper[] models;
	private PluginFilter filter;
	private BundleWrapper[] propertyDefinedPlugins;
	private PluginNameComparator comparator;

	/**
	 * Default Constructor - sets title, page name, description
	 */
	public CessarLibrariesClasspathContainerPage()
	{
		super("", Messages.genericText, null); //$NON-NLS-1$ 
	}

	/**
	 * @param pageName
	 */
	protected CessarLibrariesClasspathContainerPage(String pageName)
	{
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension#initialize(org.eclipse.jdt.core.IJavaProject,
	 * org.eclipse.jdt.core.IClasspathEntry[])
	 */
	@Override
	public void initialize(IJavaProject aproject, IClasspathEntry[] currentEntries)
	{
		project = aproject;

		loadSystemBundles();

		loadPropertyDefinedBundles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));

		Label text = new Label(container, SWT.NONE);
		text.setText(Messages.descText);

		// Instantiate the label for the textfield
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: "); //$NON-NLS-1$
		// The text field used for filtering the TableViewer
		searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		// Creates the TableViewer
		createViewer(parent);

		// Set the sorter for the table
		comparator = new PluginNameComparator();
		viewer.setComparator(comparator);

		// New to support the search
		searchText.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				filter.setSearchText(searchText.getText());
				viewer.refresh();
			}
		});

		// The filter used for filtering the TableViewer
		filter = new PluginFilter();
		viewer.addFilter(filter);

		setControl(container);
	}

	private void createViewer(Composite parent)
	{

		// Define the TableViewer
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		// Create the columns
		createColumns();
		// Make lines and make header visible
		Table table = viewer.getTable();
		GridLayout gridLayout = new GridLayout(1, false);
		parent.setLayout(gridLayout);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_table.heightHint = 400;
		gd_table.widthHint = 400;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// Set the ContentProvider
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		// Get the content for the Viewer,
		// setInput will call getElements in the ContentProvider
		viewer.setInput(models);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#finish() When the OK button is clicked, this method is
	 * called.It checks all the plugins from the "modified" list. It takes all the plugins that are flagged as changed
	 * and checks them to see if they are checked or not. If a plugin is checked then it is added to the required
	 * plugins for the specific project
	 */
	public boolean finish()
	{
		boolean finish = false;
		for (Map.Entry<String, PluginModelBaseWrapper> entry: modified.entrySet())
		{
			final String key = entry.getKey();
			final PluginModelBaseWrapper value = entry.getValue();
			if (value.isChanged())
			{
				finish = true;
				String s = value.isChecked() ? "1" : "0"; //$NON-NLS-1$//$NON-NLS-2$
				Bundle bundle = Platform.getBundle(key);
				if (bundle != null)
				{
					RequiredPluginsAccessor.setBundle(project.getProject(), key, s);
					CessarRuntime.resetGenericContainer(project);
				}
			}
		}
		return finish;
	}

	// This will create the columns for the table
	private void createColumns()
	{
		String[] titles = {" ", "Plugin Name"}; //$NON-NLS-1$ //$NON-NLS-2$
		int[] bounds = {25, 250};
		// First column is for checked/unchecked depending if the plugin is required or not
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
			 */
			@Override
			public void update(ViewerCell cell)
			{
				Object element = cell.getElement();
				PluginModelBaseWrapper plugin = ((PluginModelBaseWrapper) element);
				String id = plugin.getPluginModelBase().getPluginBase().getId();
				if (!plugin.isReadOnly())
				{
					if (plugin.isChecked())
					{

						cell.setImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.CHECKED));
						modified.put(id, plugin);
					}
					else
					{
						cell.setImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.UNCHECKED));
						modified.put(id, plugin);
					}
				}
				else
				{
					cell.setImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.CHECKED));
				}
			}
		});
		col.setEditingSupport(new CheckedEditingSupport(viewer));

		// Second column is for the plugin name
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new StyledCellLabelProvider()
		{
			@Override
			public void update(ViewerCell cell)
			{
				String search = searchText.getText();
				PluginModelBaseWrapper plugin = (PluginModelBaseWrapper) cell.getElement();
				String cellText = plugin.getPluginModelBase().getPluginBase().getId();
				// The text that will apear in the second column
				cell.setText(cellText);
				if (plugin.isReadOnly())
				{
					cell.setForeground(colorGray);
				}
				if (search != null && search.length() > 0)
				{
					int intRangesCorrectSize[] = StringUtils.getSearchTermOccurrences(search, cellText);
					List<StyleRange> styleRange = new ArrayList<StyleRange>();
					for (int i = 0; i < intRangesCorrectSize.length / 2; i = i + 2)
					{
						int start = intRangesCorrectSize[i];
						int length = intRangesCorrectSize[i + 1];
						StyleRange myStyledRange = new StyleRange(start, length, null, colorYellow);

						styleRange.add(myStyledRange);
					}
					cell.setStyleRanges(styleRange.toArray(new StyleRange[styleRange.size()]));
				}
				else
				{
					cell.setStyleRanges(null);
				}

				super.update(cell);
			}
		});

	}

	/**
	 * @param title
	 *        represents the title of the column
	 * @param bound
	 * @param colNumber
	 *        represents the index of the Column
	 * @return A TableViewer Column
	 */
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber)
	{
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		return viewerColumn;
	}

	/**
	 * @param column
	 * @param index
	 * @return A selectionAdapter for the given column
	 */
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index)
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{ // The comparator is used for sorting the tableViewer
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}

		};
		return selectionAdapter;
	}

	/**
	 * Used to update the viewer from outside
	 */
	public void refresh()
	{
		viewer.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#getSelection()
	 */
	@Override
	public IClasspathEntry getSelection()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#setSelection(org.eclipse.jdt.core.IClasspathEntry)
	 */
	@Override
	public void setSelection(IClasspathEntry containerEntry)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		viewer.getControl().setFocus();
	}

	/**
	 * @param file
	 * @return The Image with the given name from the "icons/" folder, if the folder exists in the plugin's structure
	 */
	private static Image getImage(String file)
	{
		Bundle bundle = FrameworkUtil.getBundle(CessarLibrariesClasspathContainerPage.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null); //$NON-NLS-1$
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();

	}

	/**
	 * This method is used in order to load the plugins specific for the current project,and checks them as fit
	 */
	// SUPPRESS CHECKSTYLE ignore Checkstyle
	private void loadPropertyDefinedBundles()
	{
		final String[] cessarBundles = BundleUtils.loadContributedBundles(project, CessarRuntime.CONTAINER_ID_GENERIC);

		// property defined
		List<BundleWrapper> bundlesPropertyFile;
		String[] sPropertyDefinedPlugins = RequiredPluginsAccessor.getPluginList(project.getProject());
		if (sPropertyDefinedPlugins != null && sPropertyDefinedPlugins.length > 0)
		{
			BundleWrapper[] propDefinedPlugins = new BundleWrapper[sPropertyDefinedPlugins.length];
			for (int k = 0; k < sPropertyDefinedPlugins.length; k++)
			{
				propDefinedPlugins[k] = new BundleWrapper();
				propDefinedPlugins[k].setBundleId(sPropertyDefinedPlugins[k]);
			}
			bundlesPropertyFile = new ArrayList<BundleWrapper>();
			for (int j = 0; j < models.length; j++)
			{
				for (int i = 0; i < propDefinedPlugins.length; i++)
				{
					if (models[j].getPluginModelBase().getPluginBase().getId().equals(
						propDefinedPlugins[i].getBundleId()))
					{
						bundlesPropertyFile.add(propDefinedPlugins[i]);
						BundleSpecification[] requiredBundles = models[j].getPluginModelBase().getBundleDescription().getRequiredBundles();
						for (int k = 0; k < requiredBundles.length; k++)
						{
							BundleSpecification bS = requiredBundles[k];
							BundleWrapper bW = new BundleWrapper();
							bW.setBundleId(bS.getName());
							bW.setDependency(true);
							bundlesPropertyFile.add(bW);
						}
					}
				}
			}
			if (!bundlesPropertyFile.isEmpty())
			{
				propertyDefinedPlugins = bundlesPropertyFile.toArray(new BundleWrapper[0]);
			}
		}
		for (int i = 0; i < models.length; i++)
		{
			if (cessarBundles != null && cessarBundles.length > 0)
			{
				for (int k = 0; k < cessarBundles.length; k++)
				{
					if (models[i].getPluginModelBase().getPluginBase().getId().equals(cessarBundles[k]))
					{
						models[i].setChecked(true);
						models[i].setReadOnly(true);
					}
				}
			}
			if (propertyDefinedPlugins != null && propertyDefinedPlugins.length > 0)
			{
				for (int j = 0; j < propertyDefinedPlugins.length; j++)
				{
					if (propertyDefinedPlugins[j].getBundleId().equals(
						models[i].getPluginModelBase().getPluginBase().getId()))
					{
						if (!models[i].isReadOnly())
						{
							models[i].setChecked(true);
							if (propertyDefinedPlugins[j].isDependency())
							{
								models[i].setReadOnly(true);

							}
							else
							{
								models[i].setReadOnly(false);
							}
						}

					}
				}
			}
		}

	}

	/**
	 * This method is used in order to instantiate all the plugins from the targetPlatform
	 */
	private void loadSystemBundles()
	{
		IPluginModelBase[] externalModels = PluginRegistry.getExternalModels();
		List<PluginModelBaseWrapper> lModels = new ArrayList<PluginModelBaseWrapper>();
		if (externalModels != null && externalModels.length > 0)
		{
			models = new PluginModelBaseWrapper[externalModels.length];
			for (int i = 0; i < externalModels.length; i++)
			{
				PluginModelBaseWrapper pluginModelBaseWrapper = new PluginModelBaseWrapper(externalModels[i]);
				if (!lModels.contains(pluginModelBaseWrapper)
					&& !externalModels[i].getPluginBase().getId().endsWith(".source")) //$NON-NLS-1$
				{
					lModels.add(pluginModelBaseWrapper);
				}
			}
		}
		// remove duplicates - strangely few bundles from
		// PluginRegistry.getExternalModels() have duplicates
		Set<PluginModelBaseWrapper> h = new HashSet<PluginModelBaseWrapper>(lModels);
		lModels.clear();
		lModels.addAll(h);
		models = lModels.toArray(new PluginModelBaseWrapper[0]);
		java.util.Arrays.sort(models, new PluginModelBaseWrapperComparator());

	}

	private class PluginModelBaseWrapperComparator implements Comparator<PluginModelBaseWrapper>
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(PluginModelBaseWrapper entry1, PluginModelBaseWrapper entry2)
		{
			if (entry1 != null && entry2 != null)
			{
				return entry1.getPluginModelBase().getPluginBase().getId().compareTo(
					entry2.getPluginModelBase().getPluginBase().getId());
			}
			return 0;
		}
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}
}
