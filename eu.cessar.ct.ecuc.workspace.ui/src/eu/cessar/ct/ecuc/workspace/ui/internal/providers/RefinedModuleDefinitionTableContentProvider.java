package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * The content provider for refined module definitions.
 *
 * @author uidw8762
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Tue Mar 17 18:43:03 2015 %
 *
 *         %version: 2 %
 */
public class RefinedModuleDefinitionTableContentProvider extends AbstractTableContentProvider implements IColorProvider
{

	/**
	 * List of {@link GModuleDef} refined objects in this project.
	 */
	protected List<GModuleDef> moduleDefinitions = new ArrayList<GModuleDef>();

	/** The working project */
	protected IProject project;

	/** The module definition type. */
	private ModuleDefinitionType moduleDefinitionType;
	/**
	 * The module configuration to find refinements for
	 */
	private GModuleConfiguration moduleConfiguration;

	/**
	 * Instantiates a new standard module definition table content provider.
	 *
	 * @param project
	 *        the project
	 * @param configuration
	 * @param moduleConfiguration
	 */
	public RefinedModuleDefinitionTableContentProvider(IProject project, GModuleConfiguration configuration)
	{
		super();
		moduleConfiguration = configuration;
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
	 * Reads the list of {@link GModuleDef} objects available in the project. Fills in {@link #moduleDefinitions} with
	 * the list of {@link GModuleDef} objects in the project. The returned list is sorted alphabetically by name.
	 *
	 * @return true if module definitions were loaded successfully and false otherwise
	 */
	protected boolean initStandardModules()
	{
		// Clear all previously stored informations
		moduleDefinitions.clear();

		Map<String, GModuleDef> refinedModules = new HashMap<String, GModuleDef>();

		// Get all Module Definitions in the project.
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);

		List<GModuleDef> allModuleDefs = ecucModel.getAllModuleDefs();
		if (moduleConfiguration != null)
		{
			GModuleDef standardDefinition = moduleConfiguration.gGetDefinition();
			if (standardDefinition != null)
			{
				for (GModuleDef module: allModuleDefs)
				{
					String id = module.gGetUuid();
					GModuleDef refinedModuleDef = module.gGetRefinedModuleDef();

					if (null != refinedModuleDef && refinedModuleDef == standardDefinition)
					{
						refinedModules.put(id, module);
					}
				}
			}
		}
		// Sort the list by module definition's short name
		moduleDefinitions.addAll(refinedModules.values());
		Collections.sort(moduleDefinitions, new Comparator<GModuleDef>()
			{
			public int compare(GModuleDef o1, GModuleDef o2)
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
	public boolean isDuplicateModule(GModuleDef module)
	{
		return (moduleDefinitions != null ? moduleDefinitions.contains(module.gGetUuid()) : false);
	}
}
