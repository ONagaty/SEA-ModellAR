package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.viewers.AbstractFilteredTreeContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ModuleConfigurationWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.config.ModuleConfigurationDefSelectionPage;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * This is a provider used in conjunction with
 * {@link ModuleConfigurationDefSelectionPage} of the
 * {@link ModuleConfigurationWizard}. It provides a tree containing the selected
 * project and all packages having Module Definitions whose upper multiplicity
 * has not been completely used, along with those Module Definitions.
 * 
 * @author tiziana_brinzas
 * 
 */
public class ModuleDefinitionTreeContentProvider extends AbstractFilteredTreeContentProvider
{
	/**
	 * Working project
	 */
	protected IProject project;

	/**
	 * {@link HashMap} in which entries have as key the result of
	 * {@link #getId(Object)} applied to a {@link GModuleDef} object, and the
	 * value is the number of {@link GModuleConfiguration} objects in the
	 * project using it.
	 */
	protected HashMap<Object, Integer> usedMultiplicity = new HashMap<Object, Integer>();

	public ModuleDefinitionTreeContentProvider(IProject project)
	{
		setProject(project);
	}

	public void setProject(IProject project)
	{
		if (null != project && project != this.project)
		{
			this.project = project;

			// Store info on module definition multiplicity
			initMultiplicityInfo();
		}
	}

	/**
	 * Returns the selected project as the single top-level element. Builds
	 * information on the available multiplicity for each Module Definition.
	 * 
	 * @see AbstractFilteredTreeContentProvider#getAllElements(Object)
	 * @see AbstractFilteredTreeContentProvider#getElements(Object)
	 */
	@Override
	public Object[] getAllElements(Object inputElement)
	{
		if (null == project)
		{
			return Collections.EMPTY_LIST.toArray();
		}

		return new Object[] {project};
	}

	/**
	 * Returns all Package and Module Definition children.
	 * 
	 * @see AbstractFilteredTreeContentProvider#getAllChildren(Object)
	 * @see AbstractFilteredTreeContentProvider#getChildren(Object)
	 */
	@Override
	protected Object[] getAllChildren(Object element)
	{
		Assert.isNotNull(element);

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		Assert.isNotNull(ecucModel);

		ArrayList<Object> children = new ArrayList<Object>();
		if (element instanceof GModuleDef)
		{
			return children.toArray();
		}

		// Add sub-packages
		String ownerQName = getQName(element);
		Map<String, List<GARPackage>> subPackages = ecucModel.getArPackagesWithModuleDefs(ownerQName);
		for (String subPackageName: subPackages.keySet())
		{
			children.add(subPackages.get(subPackageName).get(0));
		}

		// Add all module definitions
		children.addAll(ecucModel.getModuleDefsFromPackage(getQName(element)));
		return children.toArray();
	}

	@Override
	public boolean hasChildren(Object element)
	{
		return super.hasChildren(element) && !(element instanceof GModuleDef);
	}

	/**
	 * Returns the hash code of elements qualified name.
	 * <p>
	 * Note: Using the hash code was required because Autosar imposes no
	 * restrictions regarding the presence spacing characters in names; however,
	 * the multiplicity map used by this implementation will trim all such
	 * characters when building its keys.
	 * </p>
	 * 
	 * @see AbstractFilteredTreeContentProvider#getId(Object)
	 */
	@Override
	protected Object getId(Object element)
	{
		return getQName(element).hashCode();
	}

	/**
	 * Returns true for Module Definitions with remaining multiplicity, false
	 * otherwise.
	 * 
	 * @see #hasRemainingMultiplicity(GModuleDef)
	 * @see AbstractFilteredTreeContentProvider#isValidElement(Object)
	 */
	@Override
	protected boolean isValidElement(Object element)
	{
		// An element is valid if it is a ModuleDefinition with remaining
		// multiplicity
		if (element instanceof GModuleDef)
		{
			return hasRemainingMultiplicity((GModuleDef) element);
		}

		// ...or a package with valid ModuleDefinition descendants,
		// in which case returning false will force recursive verifications
		return false;
	}

	/**
	 * Build a map having as keys module definition Ids and as values the number
	 * of module configurations using them as definition.
	 */
	private void initMultiplicityInfo()
	{
		// Clear all previously stored informations
		usedMultiplicity.clear();

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		ecucModel.modelChanged();
		Assert.isNotNull(ecucModel);

		// Store info on module definition multiplicity
		List<GModuleConfiguration> moduleCfgs = ecucModel.getAllModuleCfgs();
		for (GModuleConfiguration moduleCfg: moduleCfgs)
		{
			GModuleDef moduleDefinition = moduleCfg.gGetDefinition();
			Object moduleDefinitionId = getId(moduleDefinition);
			if (usedMultiplicity.containsKey(moduleDefinitionId))
			{
				usedMultiplicity.put(moduleDefinitionId,
					usedMultiplicity.get(moduleDefinitionId) + 1);
			}
			else
			{
				usedMultiplicity.put(moduleDefinitionId, 1);
			}
		}
	}

	/**
	 * Returns true if the given module definition still has unused
	 * multiplicity, i.e:
	 * <ul>
	 * <li>its upper multiplicity attribute is not set, or</>
	 * <li>its upper multiplicity infinite attribute is set, or</>
	 * <li>its upper multiplicity is a number larger than the number of module
	 * configurations using this module definition.</> </>
	 * 
	 * @param moduleName
	 *        Module definition whose remaining multiplicity is queried.
	 * @return True if the module definition has unused multiplicity, false
	 *         otherwise.
	 */
	private boolean hasRemainingMultiplicity(GModuleDef moduleDefinition)
	{
		Object moduleDefinitionId = getId(moduleDefinition);
		if (!usedMultiplicity.containsKey(moduleDefinitionId))
		{
			// No configuration uses this definition yet
			return true;
		}

		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(moduleDefinition).getEcucMMService();

		BigInteger upperMultiplicity = ecucMMService.getUpperMultiplicity(moduleDefinition,
			BigInteger.ONE, true);
		if (null == upperMultiplicity || IEcucMMService.MULTIPLICITY_STAR == upperMultiplicity)
		{
			return true;
		}

		try
		{
			// Compare upper multiplicity with used multiplicity
			Long maxMultiplicity;
			maxMultiplicity = upperMultiplicity.longValue();
			if (usedMultiplicity.get(moduleDefinitionId) < maxMultiplicity)
			{
				return true;
			}
		}
		catch (NumberFormatException e)
		{
			// Not a number - act as if not set
			return true;
		}
		return false;
	}

	public Object getParent(Object element)
	{
		return (element instanceof IProject ? null : ((EObject) element).eContainer());
	}

	/**
	 * Return the absolute qualified name of the given object ("/" for
	 * projects).
	 */
	private String getQName(Object object)
	{
		String qualifiedName = (object instanceof IProject ? MetaModelUtils.QNAME_SEPARATOR_STR
			: MetaModelUtils.getAbsoluteQualifiedName((EObject) object));
		return MetaModelUtils.normalizeQualifiedName(qualifiedName);
	}
}
