package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sphinx.emf.util.EObjectUtil;

import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.core.platform.ui.viewers.AbstractTreeContentProvider;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

public class PackagesTreeContentProvider extends AbstractTreeContentProvider
{
	/**
	 * Working project
	 */
	protected IProject project;

	/**
	 * {@link HashMap} in which entries have as key the result of
	 * {@link #getId(Object)} applied to a {@link GARPackage} object, and the
	 * value is its list of children
	 */
	protected HashMap<String, List<GARPackage>> packages = new HashMap<String, List<GARPackage>>();

	public PackagesTreeContentProvider(IProject project)
	{
		setProject(project);
	}

	public void setProject(IProject project)
	{
		if (null != project && project != this.project)
		{
			this.project = project;

			// Load the map of packages (needed to cope with splittable)
			initPackagesMap();
		}
	}

	/**
	 * Return all sub-packages of parentElement, taking <<atSplittable>> into
	 * account.
	 */
	public Object[] getChildren(Object parentElement)
	{
		Assert.isNotNull(parentElement);

		String parentId = parentElement instanceof GARPackage ? ModelUtils.getAbsoluteQualifiedName((EObject) parentElement)
			: parentElement instanceof IProject ? ((IProject) parentElement).getName() : null;
		if (parentId != null && packages.containsKey(parentId))
		{
			// Each list in the map will contain multiple entries for splitted
			// packages - unify them to present only unique names in the tree
			HashMap<String, GARPackage> uniquePackages = new HashMap<String, GARPackage>();
			List<GARPackage> subPackages = packages.get(parentId);
			for (GARPackage subPackage: subPackages)
			{
				String subPackageId = ModelUtils.getAbsoluteQualifiedName(subPackage);
				if (!uniquePackages.containsKey(subPackageId))
				{
					uniquePackages.put(subPackageId, subPackage);
				}
			}
			return uniquePackages.values().toArray();
		}
		return Collections.EMPTY_LIST.toArray();
	}

	/**
	 * Return the one and only top-level element associated with the project.
	 * Initialize map of packages needed to cope with <<atSplittable>>.
	 */
	public Object[] getElements(Object inputElement)
	{
		if (null == project)
		{
			return Collections.EMPTY_LIST.toArray();
		}

		// Return the one and only root object - the project itself
		return new Object[] {project};
	}

	public Object getParent(Object element)
	{
		return ((EObject) element).eContainer();
	}

	public boolean hasChildren(Object element)
	{
		return getChildren(element).length > 0;
	}

	/**
	 * Builds an internal map grouping all packages with the same qualified
	 * name. This map is used to cope with <<atSplittable>>.
	 */
	private void initPackagesMap()
	{
		// Clear all previously stored informations
		packages.clear();

		Collection<Resource> resources = EResourceUtils.getProjectResources(project);
		List<GARPackage> allPackages = EObjectUtil.getAllInstancesOf(resources, GARPackage.class,
			false);
		for (GARPackage arPackage: allPackages)
		{
			EObject parent = arPackage.eContainer();
			String parentId = parent instanceof GAUTOSAR ? project.getName()
				: ModelUtils.getAbsoluteQualifiedName(parent);
			if (!packages.containsKey(parentId))
			{
				packages.put(parentId, new ArrayList<GARPackage>());
			}
			packages.get(parentId).add(arPackage);
		}
	}
}
