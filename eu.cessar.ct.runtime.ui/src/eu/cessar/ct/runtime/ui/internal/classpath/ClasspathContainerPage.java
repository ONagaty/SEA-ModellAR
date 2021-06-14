package eu.cessar.ct.runtime.ui.internal.classpath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.osgi.framework.Bundle;

import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.classpath.BundleWrapper;
import eu.cessar.ct.runtime.utils.BundleUtils;
import eu.cessar.ct.runtime.utils.RequiredPluginsAccessor;

/**
 * ClasspathContainerPage is an implementation of a classpath container page allows the user to create a new or edit an
 * existing classpath container entry.
 * 
 * @author uidu0944
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Thu Apr 18 15:12:13 2013 %
 * 
 *         %version: 5 %
 */
public class ClasspathContainerPage extends WizardPage implements IClasspathContainerPage,
	IClasspathContainerPageExtension
{
	private static String descText = "The list of plugins available for code execution.\nThe grayed out ones are either specific to a particular Cessar CT version\n" //$NON-NLS-1$
		+ "or dependencies of customer defined plugins.\n"; //$NON-NLS-1$
	private PluginModelBaseWrapper[] models;
	private BundleWrapper[] propertyDefinedPlugins;
	private Map<String, PluginModelBaseWrapper> modified = new HashMap<String, PluginModelBaseWrapper>();
	private IJavaProject project;

	// private Label errText;

	/**
	 * Default Constructor - sets title, page name, description
	 */
	public ClasspathContainerPage()
	{
		super("", "Generic libraries, static for a particular\n Cessar version", null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @param pageName
	 */
	protected ClasspathContainerPage(String pageName)
	{
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension#initialize(org.eclipse.jdt.core.IJavaProject,
	 * org.eclipse.jdt.core.IClasspathEntry[])
	 */
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
	public void createControl(final Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));

		Label text = new Label(container, SWT.NONE);
		text.setText(descText);

		Table table = new Table(container, SWT.BORDER | SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_table.heightHint = 280;
		gd_table.widthHint = 400;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);

		// dynamic content
		configItems(table);
		setupTable(table);
		setControl(container);
	}

	/**
	 * @param table
	 */
	private void configItems(Table table)
	{
		String[] cessarBundles = BundleUtils.loadContributedBundles(project, CessarRuntime.CONTAINER_ID_GENERIC);
		for (int i = 0; i < models.length; i++)
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData(models[i]);
			item.setText(models[i].getPluginModelBase().getPluginBase().getId());

			if (cessarBundles != null && cessarBundles.length > 0)
			{
				for (int k = 0; k < cessarBundles.length; k++)
				{
					if (models[i].getPluginModelBase().getPluginBase().getId().equals(cessarBundles[k]))
					{
						item.setChecked(true);
						item.setGrayed(true);
						PluginModelBaseWrapper data = (PluginModelBaseWrapper) item.getData();
						data.setChecked(true);
						data.setReadOnly(true);
						item.setForeground(table.getDisplay().getSystemColor(SWT.COLOR_GRAY));
						item.setData(data);
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
						PluginModelBaseWrapper data = (PluginModelBaseWrapper) item.getData();
						if (!data.isReadOnly()) // if bundle is read only don't
												// bother
						{
							data.setChecked(true);
							item.setData(data);
							item.setChecked(true);
							if (propertyDefinedPlugins[j].isDependency())
							{
								data.setReadOnly(true);
								item.setGrayed(true);
								item.setForeground(table.getDisplay().getSystemColor(SWT.COLOR_GRAY));
							}
							else
							{
								data.setReadOnly(false);
								item.setForeground(table.getDisplay().getSystemColor(SWT.COLOR_BLACK));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param table
	 */
	private void setupTable(Table table)
	{
		table.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				if (event.detail == SWT.CHECK)
				{
					TableItem item = (TableItem) event.item;
					PluginModelBaseWrapper data = (PluginModelBaseWrapper) item.getData();

					event.detail = SWT.NONE;
					event.type = SWT.None;

					if (!data.isReadOnly())
					{
						data.setChanged(true);
						boolean b = data.isChecked();
						data.setChecked(!b);
						data.setChanged(true);
						modified.put(data.getPluginModelBase().getPluginBase().getId(), data);
						item.setChecked(!b);
					}
					else
					{
						item.setChecked(true);
					}
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#finish()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#getSelection()
	 */
	public IClasspathEntry getSelection()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#setSelection(org.eclipse.jdt.core.IClasspathEntry)
	 */
	public void setSelection(IClasspathEntry containerEntry)
	{
		// empty method
	}

	private void loadPropertyDefinedBundles()
	{
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

	}

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

}
