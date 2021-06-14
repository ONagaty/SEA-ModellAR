package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.viewers.AbstractTreeContentProvider;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucparameterdef.GModuleDef;

public class RefinedModuleDefinitionTreeContentProvider extends AbstractTreeContentProvider
{

	protected IProject project;
	protected IEcucModel ecucModel;

	/**
	 * 
	 */
	protected List<GModuleDef> baseModules = new ArrayList<GModuleDef>();
	protected HashMap<Object, List<GModuleDef>> refinedModules = new HashMap<Object, List<GModuleDef>>();

	public RefinedModuleDefinitionTreeContentProvider(IProject project)
	{
		setProject(project);
	}

	public void setProject(IProject project)
	{
		if (null != project && project != this.project)
		{
			this.project = project;
			initRefinedModules();
		}
	}

	public Object[] getElements(Object inputElement)
	{
		if (null == project)
		{
			return Collections.EMPTY_LIST.toArray();
		}

		return baseModules.toArray();
	}

	public Object[] getChildren(Object parentElement)
	{
		Assert.isTrue(parentElement instanceof GModuleDef);

		Object moduleId = getId(parentElement);
		if (refinedModules.containsKey(moduleId))
		{
			return refinedModules.get(moduleId).toArray();
		}

		return Collections.EMPTY_LIST.toArray();
	}

	/**
	 * Builds a map of {@link GModuleDef} in which the value is the Id (@see
	 * {@link #getId(Object)}) of a Module Definition and the value is a list
	 * with all Module Definitions that refine that one.
	 * 
	 * While building this map, all {@link GModuleDef} objects that have no
	 * refinement module at all are added to {@link #baseModules}.
	 */
	public void initRefinedModules()
	{
		// Clear any previously stored informations
		baseModules.clear();
		refinedModules.clear();

		// Note: Working with IEcucModel.getDescendedModuleDefs(GModuleDef)
		// means we have to traverse the model tree for each module definition.
		// With a different approach, a single browse through the Module
		// Definition list (meaning only one traversal) can provide all required
		// data.

		// Get all Module Definitions in the project.
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		List<GModuleDef> allModuleDefs = ecucModel.getAllModuleDefs();

		// Build up the refinements map and fill in the list of base modules
		// along the way
		for (GModuleDef module: allModuleDefs)
		{
			GModuleDef refinedModule = module.gGetRefinedModuleDef();
			if (null == refinedModule)
			{
				baseModules.add(module);
			}
			else
			{
				List<GModuleDef> moduleRefinements = new ArrayList<GModuleDef>();

				Object moduleId = getId(refinedModule);
				if (refinedModules.containsKey(moduleId))
				{
					moduleRefinements = refinedModules.get(moduleId);
				}
				moduleRefinements.add(module);
				refinedModules.put(moduleId, moduleRefinements);
			}
		}
	}

	public Object getParent(Object element)
	{
		Assert.isTrue(element instanceof GModuleDef);
		GModuleDef moduleDefinition = (GModuleDef) element;
		return moduleDefinition.gGetRefinedModuleDef();
	}

	public boolean hasChildren(Object element)
	{
		return getChildren(element).length > 0;
	}

	/**
	 * Returns the hash code of elements qualified name, to be used as key in
	 * the internal map.
	 */
	protected Object getId(Object element)
	{
		return MetaModelUtils.getAbsoluteQualifiedName((EObject) element).hashCode();
	}

}
