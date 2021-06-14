package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.EcoreResourceUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * The Class StandardModuleDefinitionTableContentProvider.
 *
 * @author uidw8762
 *
 *         %created_by: uid95856 %
 *
 *         %date_created: Mon Nov 3 10:03:25 2014 %
 *
 *         %version: 17 %
 */
public class StandardModuleDefinitionTableContentProvider extends AbstractTableContentProvider implements IColorProvider
{

	/**
	 * List of {@link GModuleDef} objects in the Autosar library and this project. When a {@link GModuleDef} object
	 * appears both in the project and in the library, the project object is the one taken.
	 */
	protected List<GModuleDef> moduleDefinitions = new ArrayList<GModuleDef>();

	/** The working project */
	protected IProject project;

	/** The standard module def resource. */
	protected Resource standardModuleDefResource;

	/** The module definition type. */
	private ModuleDefinitionType moduleDefinitionType;

	/**
	 * Instantiates a new standard module definition table content provider.
	 */
	public StandardModuleDefinitionTableContentProvider()
	{
	}

	/**
	 * Instantiates a new standard module definition table content provider.
	 *
	 * @param project
	 *        the project
	 */
	public StandardModuleDefinitionTableContentProvider(IProject project)
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
		return moduleDefinitions.toArray();
	}

	/**
	 * Reads the list of {@link GModuleDef} objects available in the Autosar library and compares that with objects
	 * available in the project. Fills in {@link #moduleDefinitions} with the list of {@link GModuleDef} objects in the
	 * project whose UUID matches the UUID of a library {@link GModuleDef}. The returned list is sorted alphabetically
	 * by name.
	 *
	 * @return true if module definitions were loaded successfully and false otherwise
	 */
	protected boolean initStandardModules()
	{
		// Clear all previously stored informations
		moduleDefinitions.clear();

		Set<String> standardModuleNames = new HashSet<String>();
		Map<String, GModuleDef> standardModules = new HashMap<String, GModuleDef>();

		// Get library Module Definitions and add them to the map
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);

		List<GModuleDef> libraryModules = null;

		libraryModules = EcucMetaModelUtils.getStandardModuleDefs(autosarRelease);

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

		for (GModuleDef module: libraryModules)
		{
			String moduleName = ModelUtils.getAbsoluteQualifiedName(module);
			standardModuleNames.add(moduleName);
			standardModules.put(moduleName, module);
		}
		// Get all Module Definitions in the project.
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		List<GModuleDef> allModuleDefs = ecucModel.getAllModuleDefs();

		// TODO: Check if this reasoning is correct
		// Overwrite entries in the map based on matching UUIDs
		for (GModuleDef module: allModuleDefs)
		{
			String moduleName = ModelUtils.getAbsoluteQualifiedName(module);

			if (standardModuleNames.contains(moduleName))
			{
				standardModules.put(moduleName, module);
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
	 * Unloads the resource that was loaded in order to initialize the Standard Module Definitions (EcucParamDef.arxml).
	 */
	public void unloadStandardModuleDefinitionResource()
	{
		if (standardModuleDefResource != null)
		{
			EcoreResourceUtil.unloadResource(standardModuleDefResource, true);
		}

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
			if (element instanceof IProject)
			{
				return CessarPluginActivator.getDefault().getImage(CessarPluginActivator.ICON_BSW_PROJECT);
			}
			if (element instanceof GARPackage)
			{
				return CessarPluginActivator.getDefault().getImage(CessarPluginActivator.ICON_AR_PACKAGE);
			}
			if (element instanceof GModuleDef)
			{
				return CessarPluginActivator.getDefault().getImage(CessarPluginActivator.ICON_BSW_MODULE);
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
				return ((GModuleDef) element).gGetShortName();

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
		IFile file = EcorePlatformUtil.getFile((EObject) element);
		if (null == file)
		{
			// Module definition not in the project
			return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		}
		else
		{
			// Module definition already in the project.
			return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		}
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
	public boolean isDuplicateModule(GModuleDef module)
	{
		return (moduleDefinitions != null ? moduleDefinitions.contains(module.gGetUuid()) : false);
	}

}
