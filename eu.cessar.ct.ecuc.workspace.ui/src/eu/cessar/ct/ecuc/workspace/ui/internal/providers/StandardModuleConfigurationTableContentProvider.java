package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import eu.cessar.ct.core.mms.MetaModelUtils;
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

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * The content provider for standard module configurations.
 *
 * @author uidw8762
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Mon Mar 16 20:15:15 2015 %
 *
 *         %version: 1 %
 */
public class StandardModuleConfigurationTableContentProvider extends AbstractTableContentProvider implements
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
	public StandardModuleConfigurationTableContentProvider()
	{
	}

	/**
	 * Instantiates a new standard module definition table content provider.
	 *
	 * @param project
	 *        the project
	 */
	public StandardModuleConfigurationTableContentProvider(IProject project)
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
	 * Reads the list of {@link GModuleDef} objects available in the Autosar library and compares that with objects
	 * available in the project. Fills in {@link #moduleConfigurations} with the list of {@link GModuleDef} objects in
	 * the project whose UUID matches the UUID of a library {@link GModuleDef}. The returned list is sorted
	 * alphabetically by name.
	 *
	 * @return true if module definitions were loaded successfully and false otherwise
	 */
	protected boolean initStandardModules()
	{ //
		// Set<String> standardModuleUUIDs = new HashSet<String>();

		// Get library Module Definitions and add them to the map
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);

		// List<GModuleDef> libraryModules = null;

		// libraryModules = EcucMetaModelUtils.getStandardModuleDefs(autosarRelease);

		// // store the resource for later unloading
		// if (libraryModules != null && !libraryModules.isEmpty())
		// {
		// GModuleDef moduleDef = libraryModules.get(0);
		// standardModuleDefResource = moduleDef.eResource();
		// }
		// else
		// {
		// CessarPluginActivator.getDefault().logError(
		// Messages.ModuleDefinitionInitializationPage_Err_No_ModuleDefName);
		// return false;
		// }

		// for (GModuleDef module: libraryModules)
		// {
		// String uuid = module.gGetUuid();
		// standardModuleUUIDs.add(uuid);
		// // standardModules.put(uuid, module);
		// }

		// Clear all previously stored informations
		moduleConfigurations.clear();

		Map<String, GModuleConfiguration> standardModules = new HashMap<String, GModuleConfiguration>();

		// Get library Module Definitions and add them to the map

		// Get all Module Definitions in the project.
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		List<GModuleConfiguration> allModuleConfigurations = ecucModel.getAllModuleCfgs();

		// Get only configurations based on standard definition
		for (GModuleConfiguration module: allModuleConfigurations)
		{
			GModuleDef definition = module.gGetDefinition();
			GModuleDef refinedModule = definition.gGetRefinedModuleDef();
			if (null == refinedModule)
			{
				String id = module.gGetUuid();

				standardModules.put(id, module);
			}

		}

		// Sort the list by module configuration's short name
		moduleConfigurations.addAll(standardModules.values());
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
				IFile definingFile = EcorePlatformUtil.getFile((EObject) element);
				return (definingFile == null ? "" : definingFile.getFullPath().toString()); //$NON-NLS-1$
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
