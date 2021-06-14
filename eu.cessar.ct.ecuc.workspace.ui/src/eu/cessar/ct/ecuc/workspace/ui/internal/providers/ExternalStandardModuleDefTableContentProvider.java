package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucparameterdef.GModuleDef;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IProject;

/**
 * The Class ExternalStandardModuleDefinitionTableContentProvider.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidr0537 %
 * 
 *         %date_created: Wed Jun 17 13:26:21 2015 %
 * 
 *         %version: 1 %
 */
public class ExternalStandardModuleDefTableContentProvider extends StandardModuleDefinitionTableContentProvider
{
	/** The external standard import file path. */
	private String externalStandardImportFilePath;

	/** The external modules. */
	private Map<String, GModuleDef> externalModules;

	/**
	 * Instantiates a new standard module definition table content provider.
	 * 
	 * @param project
	 *        the project
	 */
	public ExternalStandardModuleDefTableContentProvider(IProject project)
	{
		setProject(project);
		externalModules = new HashMap<String, GModuleDef>();
	}

	/**
	 * Reads the list of {@link GModuleDef} objects available in the Autosar library and compares that with objects
	 * available in the project. Fills in {@link #moduleDefinitions} with the list of {@link GModuleDef} objects in the
	 * project whose UUID matches the UUID of a library {@link GModuleDef}. The returned list is sorted alphabetically
	 * by name.
	 * 
	 * @return true if module definitions were loaded successfully and false otherwise
	 */
	@Override
	protected boolean initStandardModules()
	{
		// Clear all previously stored informations
		moduleDefinitions.clear();

		Set<String> standardModuleUUIDs = new HashSet<String>();
		Map<String, GModuleDef> standardModules = new HashMap<String, GModuleDef>();

		// Get library Module Definitions and add them to the map
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);

		List<GModuleDef> libraryModules = null;

		// Check for external file module definitions
		String extStandardImportFilePath = getExternalStandardImportFilePath();
		libraryModules = EcucMetaModelUtils.getExternalStandardModuleDefs(autosarRelease, extStandardImportFilePath);

		// store the resource for later unloading
		if (libraryModules != null && !libraryModules.isEmpty())
		{
			GModuleDef moduleDef = libraryModules.get(0);
			standardModuleDefResource = moduleDef.eResource();
		}
		else
		{
			CessarPluginActivator.getDefault().logError(
				Messages.ModuleDefinitionInitializationPage_Err_No_ModuleDefName);
			return false;
		}

		// Get all Module Definitions in the project.
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		List<GModuleDef> allModuleDefs = ecucModel.getAllModuleDefs();

		String uuid = null;
		for (GModuleDef module: allModuleDefs)
		{
			uuid = module.gGetUuid();
			standardModuleUUIDs.add(uuid);
			standardModules.put(uuid, module);
		}

		// TODO: Check if this reasoning is correct
		// Overwrite entries in the map based on matching UUIDs
		String id = null;
		externalModules.clear();

		for (GModuleDef module: libraryModules)
		{
			id = module.gGetUuid();

			// Get all entries and decide by user selection.
			if (!standardModuleUUIDs.contains(id))
			{
				standardModules.put(id, module);
				externalModules.put(id, module);
			}
		}

		// Sort the list by module definition's short name
		moduleDefinitions.addAll(standardModules.values());

		Collections.sort(moduleDefinitions, new Comparator<GModuleDef>()
		{
			public int compare(GModuleDef o1, GModuleDef o2)
			{
				return o1.gGetShortName().compareTo(o2.gGetShortName());
			}

		});
		return true;
	}

	/**
	 * Gets the external standard import file path.
	 * 
	 * @return the externalStandardImportFilePath
	 */
	public String getExternalStandardImportFilePath()
	{
		return externalStandardImportFilePath;
	}

	/**
	 * Sets the external standard import file path.
	 * 
	 * @param externalStandardImportFilePath
	 *        the externalStandardImportFilePath to set
	 */
	public void setExternalStandardImportFilePath(String externalStandardImportFilePath)
	{
		this.externalStandardImportFilePath = externalStandardImportFilePath;
	}

	/**
	 * Checks if is duplicate module.
	 * 
	 * @param module
	 *        the module
	 * @return true, if is duplicate module
	 */
	@Override
	public boolean isDuplicateModule(GModuleDef module)
	{
		return (externalModules != null ? externalModules.containsKey(module.gGetUuid()) : false);
	}
}
