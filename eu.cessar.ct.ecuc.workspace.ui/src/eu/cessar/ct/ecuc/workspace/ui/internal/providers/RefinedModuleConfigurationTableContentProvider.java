package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * The content provider for refined module configurations.
 *
 * @author uidw8762
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Tue Mar 17 18:42:45 2015 %
 *
 *         %version: 2 %
 */
public class RefinedModuleConfigurationTableContentProvider extends AbstractTableContentProvider implements
	IColorProvider
{

	/**
	 * List of {@link GModuleConfiguration} objects in this project.
	 */
	protected List<GModuleConfiguration> moduleConfigurations = new ArrayList<GModuleConfiguration>();

	/** The working project */
	protected IProject project;

	/** The module definition type. */
	private ModuleDefinitionType moduleDefinitionType;

	/**
	 * Instantiates a new standard module definition table content provider.
	 */
	public RefinedModuleConfigurationTableContentProvider()
	{
	}

	/**
	 * Instantiates a new standard module definition table content provider.
	 *
	 * @param project
	 *        the project
	 */
	public RefinedModuleConfigurationTableContentProvider(IProject project)
	{
	setProject(project);
	}

	/**
	 * Sets the project.
	 *
	 * @param project
	 *        the new project
	 */
	public void setProject(IProject project)
	{
		if (null != project)// && project != this.project)
		{
			this.project = project;
			initStandardModules();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.providers.AbstractTableContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement)
	{
		if (null == project)
		{
			return Collections.EMPTY_LIST.toArray();
		}
		return moduleConfigurations.toArray();
	}

	/**
	 * Reads the list of refined {@link GModuleConfiguration} objects available in project. Fills in
	 * {@link #moduleConfigurations} with the list of {@link GModuleConfiguration} objects in the project. The returned
	 * list is sorted alphabetically by name.
	 *
	 * @return true if module configurations were loaded successfully and false otherwise
	 */
	protected boolean initStandardModules()
	{
		// Clear all previously stored informations
		moduleConfigurations.clear();

		Map<String, GModuleConfiguration> refinedModules = new HashMap<String, GModuleConfiguration>();
		Map<String, GModuleDef> refinedModulesDef = new HashMap<String, GModuleDef>();
		Map<String, GModuleDef> standardModulesDef = new HashMap<String, GModuleDef>();

		// Get all Module Configurations in the project.
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		List<GModuleConfiguration> allModuleConfigurations = ecucModel.getAllModuleCfgs();

		// Get only configurations based on standard definition
		for (GModuleConfiguration module: allModuleConfigurations)
		{
			GModuleDef definition = module.gGetDefinition();
			GModuleDef refinedModule = definition.gGetRefinedModuleDef();
			if (null != refinedModule)
			{
				String id = module.gGetUuid();

				refinedModules.put(id, module);
				refinedModulesDef.put(id, definition);
				standardModulesDef.put(id, refinedModule);
			}

		}

		// Sort the list by module configuration's short name
		moduleConfigurations.addAll(refinedModules.values());
		Collections.sort(moduleConfigurations, new Comparator<GModuleConfiguration>()
		{
			public int compare(GModuleConfiguration o1, GModuleConfiguration o2)
			{
				return o1.gGetShortName().compareTo(o2.gGetShortName());
			}

		});
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.ecuc.workspace.ui.internal.providers.AbstractTableContentProvider#getColumnImage(java.lang.Object,
	 * int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex)
	{
		if (element == null)
		{
			return null;
		}

		if (columnIndex == 0)
		{
			if (element instanceof GModuleConfiguration)
			{
				return CessarPluginActivator.getDefault().getImage(CessarPluginActivator.ICON_ECUC_FILE);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.ecuc.workspace.ui.internal.providers.AbstractTableContentProvider#getColumnText(java.lang.Object,
	 * int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex)
	{
		if (null == element)
		{
			return null;
		}
		switch (columnIndex)
		{
			case 0:
				return ((GModuleConfiguration) element).gGetShortName();

			case 1:
			{
				GModuleDef currentDefinition = ((GModuleConfiguration) element).gGetDefinition();

				return currentDefinition.gGetShortName();
			}
			case 2:
			{
				GModuleDef currentDefinition = ((GModuleConfiguration) element).gGetDefinition();
				GModuleDef refinedModuleDef = currentDefinition.gGetRefinedModuleDef();
				return refinedModuleDef.gGetShortName();
			}
			default:
				return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	public Color getBackground(Object element)
	{
		return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	public Color getForeground(Object element)
	{
		return null;
	}

	/**
	 * Gets the module definition type.
	 *
	 * @return the moduleDefinitionType
	 */
	public ModuleDefinitionType getModuleDefinitionType()
	{
		return moduleDefinitionType;
	}

	/**
	 * Sets the module definition type.
	 *
	 * @param moduleDefinitionType
	 *        the moduleDefinitionType to set
	 */
	public void setModuleDefinitionType(ModuleDefinitionType moduleDefinitionType)
	{
		this.moduleDefinitionType = moduleDefinitionType;
	}

	/**
	 * Checks if is duplicate module.
	 *
	 * @param module
	 *        the module
	 * @param isExternal
	 *        the is external
	 * @return true, if is duplicate module
	 */
	public boolean isDuplicateModule(GModuleConfiguration module)
	{
		return (moduleConfigurations != null ? moduleConfigurations.contains(module.gGetUuid()) : false);
	}

}
