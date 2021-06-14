/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 5, 2010 12:29:31 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.edit.ui.IEditingFacility;
import eu.cessar.ct.edit.ui.facility.EEditorCategory;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.composition.ECompositionType;
import eu.cessar.ct.edit.ui.facility.composition.IEditorComposition;
import eu.cessar.ct.edit.ui.facility.composition.ISimpleComposition;
import eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion;
import eu.cessar.ct.edit.ui.facility.preferences.ICacheEditingPreference;
import eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences;
import eu.cessar.ct.edit.ui.facility.preferences.internal.CacheEditingPreference;
import eu.cessar.ct.edit.ui.facility.preferences.internal.DefaultEditingPreferences;
import eu.cessar.ct.edit.ui.facility.preferences.internal.ProjectEditingPreferences;
import eu.cessar.ct.edit.ui.facility.preferences.internal.WorkspaceEditingPreferences;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.facility.composition.IEditorCompositionProvider;
import eu.cessar.ct.edit.ui.internal.facility.composition.SimpleCompositionProvider;
import eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition;
import eu.cessar.ct.edit.ui.newobjfactories.INewEObjectUIFactory;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidl6458
 *
 */
public class EditingFacilityImpl implements IEditingFacility
{

	private boolean initialized = false;

	private List<CompositionDefinitionElement> compositionDefinitions;

	private SimpleCompositionProvider simpleCompositionProvider;

	private Map<IProject, Map<String, IEditingPreferences>> mapOfProjPref;
	private Map<AutosarReleaseDescriptor, Map<String, IEditingPreferences>> mapOfWorkspacePref;
	private Map<AutosarReleaseDescriptor, Map<String, IEditingPreferences>> mapOfFactoryDefaultPref;
	private Map<IEditingPreferences, ICacheEditingPreference> mapOfCachePref;
	private Map<String, INewEObjectUIFactory> newUIFactories;

	/**
	 *
	 */
	public EditingFacilityImpl()
	{
		// do nothing
	}

	/**
	 * Check if initialization is needed and call doInit if necessary
	 */
	private void checkInit()
	{
		if (!initialized)
		{
			synchronized (EditingFacilityImpl.class)
			{
				if (!initialized)
				{
					try
					{
						doInit();
					}
					finally
					{
						initialized = true;
					}
				}
			}
		}

	}

	/**
	 *
	 */
	private void doInit()
	{
		compositionDefinitions = new ArrayList<CompositionDefinitionElement>();

		IExtensionPoint extPoint = Platform.getExtensionRegistry().getExtensionPoint(FacilityConstants.NAMESPACE,
			FacilityConstants.EXTENSION_COMPOSITIONS);
		IConfigurationElement[] elements = extPoint.getConfigurationElements();
		for (IConfigurationElement configElement: elements)
		{
			compositionDefinitions.add(new CompositionDefinitionElement(configElement));
		}

		// initialize also the new UI factories
		extPoint = Platform.getExtensionRegistry().getExtensionPoint(FacilityConstants.NAMESPACE,
			FacilityConstants.EXTENSION_NEWUIFACTORY);
		if (extPoint != null)
		{
			elements = extPoint.getConfigurationElements();
			newUIFactories = new HashMap<String, INewEObjectUIFactory>();
			for (IConfigurationElement configElement: elements)
			{
				try
				{
					INewEObjectUIFactory factory = (INewEObjectUIFactory) configElement.createExecutableExtension(FacilityConstants.ATT_ECLASS);
					newUIFactories.put(configElement.getAttribute(FacilityConstants.ATT_ECLASS), factory);
				}
				// SUPPRESS CHECKSTYLE catch everything
				catch (Throwable e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
				compositionDefinitions.add(new CompositionDefinitionElement(configElement));
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getEditorExpansion(eu.cessar.ct.edit.ui.facility.IModelFragmentEditor)
	 */
	public IModelFragmentEditorExpansion getEditorExpansion(IModelFragmentEditor editor)
	{
		checkInit();

		SimpleCompositionProvider provider = getSimpleCompositionProvider();

		return provider.getEditorExpansion(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getAllSimpleEditorCompositions(org.eclipse.emf.ecore.EObject)
	 */
	public List<ISimpleComposition> getAllSimpleEditorCompositions(EObject object)
	{
		return getSimpleEditorCompositions(object, object.eClass(), object.eClass().getEAllStructuralFeatures(),
			EDITOR_ALL, EEditorCategory.VALUES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getAllSimpleEditorCompositions(org.eclipse.emf.ecore.EClass)
	 */
	public List<ISimpleComposition> getAllSimpleEditorCompositions(EClass eClass)
	{
		return getSimpleEditorCompositions(null, eClass, eClass.getEAllStructuralFeatures(), EDITOR_ALL,
			EEditorCategory.VALUES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getSimpleEditorCompositions(org.eclipse.emf.ecore.EObject,
	 * eu.cessar.ct.edit.ui.EditorCategoryEnum)
	 */
	public ISimpleComposition getSimpleEditorComposition(EObject object, EEditorCategory category)
	{
		List<ISimpleComposition> simpleEditorCompositions = getSimpleEditorCompositions(object, object.eClass(),
			object.eClass().getEAllStructuralFeatures(), EDITOR_ALL, Collections.singletonList(category));

		if (simpleEditorCompositions.size() == 1)
		{
			return simpleEditorCompositions.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getSimpleEditorComposition(org.eclipse.emf.ecore.EClass,
	 * eu.cessar.ct.edit.ui.EditorCategoryEnum)
	 */
	public ISimpleComposition getSimpleEditorComposition(EClass eClass, EEditorCategory category)
	{
		List<ISimpleComposition> simpleEditorCompositions = getSimpleEditorCompositions(null, eClass,
			eClass.getEAllStructuralFeatures(), EDITOR_ALL, Collections.singletonList(category));

		if (simpleEditorCompositions.size() == 1)
		{
			return simpleEditorCompositions.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getSimpleEditorCompositions(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, java.util.List, int, java.util.List)
	 */
	public List<ISimpleComposition> getSimpleEditorCompositions(EObject object, EClass eClass,
		List<EStructuralFeature> features, int editorTypes, List<EEditorCategory> categories)
	{
		checkInit();

		return getSimpleCompositionProvider().getEditorCompositions(object, eClass, features, editorTypes, categories);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getAllEditorCompositions(org.eclipse.emf.ecore.EObject)
	 */
	public List<IEditorComposition> getAllEditorCompositions(EObject object)
	{
		return getEditorCompositions(object, ECompositionType.VALUES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getAllEditorCompositions(org.eclipse.emf.ecore.EClass)
	 */
	public List<IEditorComposition> getAllEditorCompositions(EClass eClass)
	{
		List<IEditorComposition> res = new ArrayList<IEditorComposition>();

		for (ECompositionType type: ECompositionType.VALUES)
		{
			List<IEditorComposition> editorCompositions = getEditorCompositions(eClass, type);
			res.addAll(editorCompositions);
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getEditorCompositions(org.eclipse.emf.ecore.EObject,
	 * eu.cessar.ct.edit.ui.CompositionType[])
	 */
	public List<IEditorComposition> getEditorCompositions(EObject object, ECompositionType type)
	{
		checkInit();

		List<IEditorComposition> res = new ArrayList<IEditorComposition>();

		for (CompositionDefinitionElement el: compositionDefinitions)
		{
			if (type.getName().equals(el.getType()))
			{
				if (matches(el.getInput(), object.eClass()))
				{
					IEditorCompositionProvider compositionProvider = el.createEditorCompositionProvider();
					compositionProvider.setType(ECompositionType.getLiteral(el.getType()));

					res.addAll(compositionProvider.getEditorCompositions(object));
					break;

				}
			}
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getEditorCompositions(org.eclipse.emf.ecore.EClass,
	 * eu.cessar.ct.edit.ui.CompositionType[])
	 */
	public List<IEditorComposition> getEditorCompositions(EClass eClass, ECompositionType type)
	{
		checkInit();

		if (type == ECompositionType.ECUC)
		{
			// cannot determine ECUC composition
			return Collections.emptyList();
		}

		List<IEditorComposition> res = new ArrayList<>();

		for (CompositionDefinitionElement el: compositionDefinitions)
		{
			if (type.getName().equals(el.getType()))
			{
				if (matches(el.getInput(), eClass))
				{
					IEditorCompositionProvider compositionProvider = el.createEditorCompositionProvider();
					compositionProvider.setType(ECompositionType.getLiteral(el.getType()));

					res.addAll(compositionProvider.getEditorCompositions(eClass));
					break;
				}
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getSimpleEditors(org.eclipse.emf.ecore.EObject)
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorProviders(EObject object)
	{
		return getSimpleEditorsProviders(object, object.eClass(), object.eClass().getEAllStructuralFeatures(),
			IEditingFacility.EDITOR_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getSimpleEditors(org.eclipse.emf.ecore.EClass)
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorsProviders(EClass eClass)
	{
		return getSimpleEditorProviders(eClass, eClass.getEAllStructuralFeatures(), EDITOR_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getSimpleEditors(org.eclipse.emf.ecore.EObject, java.util.List, int)
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorsProviders(EObject object, EClass eClass,
		List<EStructuralFeature> features, int editorTypes)
	{
		checkInit();

		return getSimpleCompositionProvider().getEditors(object, eClass, features, editorTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getSimpleEditors(org.eclipse.emf.ecore.EClass, java.util.List, int)
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorProviders(EClass eClass,
		List<EStructuralFeature> features, int editorTypes)
	{
		checkInit();

		return getSimpleCompositionProvider().getEditors(null, eClass, features, editorTypes);
	}

	private SimpleCompositionProvider getSimpleCompositionProvider()
	{
		if (simpleCompositionProvider == null)
		{
			synchronized (EditingFacilityImpl.class)
			{
				if (simpleCompositionProvider == null)
				{
					for (CompositionDefinitionElement el: compositionDefinitions)
					{
						if (el.getType().equals(ECompositionType.SIMPLE.getName()))
						{
							simpleCompositionProvider = (SimpleCompositionProvider) el.createEditorCompositionProvider();
							simpleCompositionProvider.setType(ECompositionType.getLiteral(el.getType()));
							break;
						}
					}
				}
			}
		}

		return simpleCompositionProvider;
	}

	private boolean matches(String pattern, EClass clz)
	{
		if (clz.getName().equals(pattern) || "EObject".equals(pattern)) //$NON-NLS-1$
		{
			return true;
		}
		else
		{
			EList<EClass> superTypes = clz.getEAllSuperTypes();
			for (EClass eClass: superTypes)
			{
				if (eClass.getName().equals(pattern))
				{
					return true;
				}
			}

			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.IEditingFacility#getEcucEditorproviders(gautosar.gecucparameterdef.GParamConfContainerDef)
	 */
	public List<IModelFragmentEditorProvider> getEditorProvidersForDefinition(GParamConfContainerDef containerDef)
	{
		checkInit();

		List<IModelFragmentEditorProvider> editorsForDefinition = getSimpleCompositionProvider().getEditorsForDefinition(
			containerDef);

		return editorsForDefinition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getCategory(java.lang.String)
	 */
	public String getCategoryFromTabId(String tabId)
	{
		int index = tabId.lastIndexOf("."); //$NON-NLS-1$
		if (index > 0 && tabId.length() > index + 1)
		{
			return tabId.substring(index + 1);
		}
		return ""; //$NON-NLS-1$
	}

	private List<IEditorComposition> getEditorCompositions(EObject object, List<ECompositionType> types)
	{
		List<IEditorComposition> res = new ArrayList<>();

		for (ECompositionType compositionType: types)
		{
			res.addAll(getEditorCompositions(object, compositionType));
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getDefaultEditingPreferences(org.artop.aal.common.metamodel.
	 * AutosarReleaseDescriptor)
	 */

	public IEditingPreferences getDefaultEditingPreferences(AutosarReleaseDescriptor autosarRelease)
	{
		return getDefaultEditingPreferences(autosarRelease, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getDefaultEditingPreferences(org.artop.aal.common.metamodel.
	 * AutosarReleaseDescriptor, java.lang.String)
	 */
	@Override
	public IEditingPreferences getDefaultEditingPreferences(AutosarReleaseDescriptor autosarRelease, String namespace)
	{
		if (mapOfFactoryDefaultPref == null)
		{
			mapOfFactoryDefaultPref = new HashMap<>();
		}

		Map<String, IEditingPreferences> map = mapOfFactoryDefaultPref.get(autosarRelease);
		if (map == null)
		{
			map = new HashMap<>();
			mapOfFactoryDefaultPref.put(autosarRelease, map);
		}

		IEditingPreferences pref = map.get(namespace);
		if (pref == null)
		{
			pref = createFactoryDefault(autosarRelease, namespace);
		}

		return pref;
	}

	private IEditingPreferences createFactoryDefault(AutosarReleaseDescriptor autosarRelease, String namespace)
	{
		IEditingPreferences pref = new DefaultEditingPreferences(autosarRelease, namespace);
		mapOfFactoryDefaultPref.get(autosarRelease).put(namespace, pref);

		return pref;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getWorkspaceEditingPreferences(org.artop.aal.common.metamodel.
	 * AutosarReleaseDescriptor)
	 */
	@Deprecated
	public IEditingPreferences getWorkspaceEditingPreferences(AutosarReleaseDescriptor autosarRelease)
	{
		return getWorkspaceEditingPreferences(autosarRelease, FacilityConstants.WORKSPACE_NAMESPACE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getWorkspaceEditingPreferences(org.artop.aal.common.metamodel.
	 * AutosarReleaseDescriptor, java.lang.String)
	 */
	public IEditingPreferences getWorkspaceEditingPreferences(AutosarReleaseDescriptor autosarRelease, String namespace)
	{
		if (mapOfWorkspacePref == null)
		{
			mapOfWorkspacePref = new HashMap<>();
		}

		Map<String, IEditingPreferences> map = mapOfWorkspacePref.get(autosarRelease);
		if (map == null)
		{
			map = new HashMap<>();
			mapOfWorkspacePref.put(autosarRelease, map);
		}

		IEditingPreferences pref = map.get(namespace);
		if (pref == null)
		{
			pref = createWorkspacePref(autosarRelease, namespace);
		}

		return pref;
	}

	/**
	 * @param autosarRelease
	 * @param wsNamespace
	 * @return
	 */
	private IEditingPreferences createWorkspacePref(AutosarReleaseDescriptor autosarRelease, String wsNamespace)
	{
		// obtain the default editing preference
		String defNs = wsNamespace;
		if (wsNamespace.contains("workspace")) //$NON-NLS-1$
		{
			defNs = wsNamespace.replace("workspace", "default"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		IEditingPreferences parent = getDefaultEditingPreferences(autosarRelease, defNs);

		IEditingPreferences pref = new WorkspaceEditingPreferences(parent, autosarRelease, wsNamespace);

		mapOfWorkspacePref.get(autosarRelease).put(wsNamespace, pref);

		return pref;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getProjectEditingPreferences(org.eclipse.core.resources.IProject)
	 */
	@Deprecated
	public IEditingPreferences getProjectEditingPreferences(IProject project)
	{
		// for compatibility reasons, the method automatically provides the editing preferences in the context of the
		// table editor
		return getProjectEditingPreferences(project, FacilityConstants.PROJECT_NAMESPACE,
			FacilityConstants.WORKSPACE_NAMESPACE);
	}

	@Override
	public IEditingPreferences getProjectEditingPreferences(IProject project, String projNamespace, String wsNamespace)
	{
		if (mapOfProjPref == null)
		{
			mapOfProjPref = new HashMap<>();
		}

		Map<String, IEditingPreferences> map = mapOfProjPref.get(project);
		if (map == null)
		{
			map = new HashMap<>();
			mapOfProjPref.put(project, map);
		}

		IEditingPreferences pref = map.get(projNamespace);
		if (pref == null)
		{
			pref = createProjectPref(project, projNamespace, wsNamespace);
		}

		return pref;
	}

	private IEditingPreferences createProjectPref(IProject project, String projNamespace, String wsNamespace)
	{
		IEditingPreferences parent = getWorkspaceEditingPreferences(MetaModelUtils.getAutosarRelease(project),
			wsNamespace);
		IEditingPreferences pref = new ProjectEditingPreferences(parent, project, projNamespace);

		// update cache
		mapOfProjPref.get(project).put(projNamespace, pref);

		return pref;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getCacheEditingPreference(eu.cessar.ct.edit.ui.facility.preferences.
	 * IEditingPreferences)
	 */
	public ICacheEditingPreference getCacheEditingPreference(IEditingPreferences parentPref)
	{
		if (mapOfCachePref == null)
		{
			mapOfCachePref = new HashMap<>();
			return createCachePref(parentPref);
		}
		else
		{
			if (mapOfCachePref.containsKey(parentPref))
			{
				return mapOfCachePref.get(parentPref);
			}
			else
			{
				return createCachePref(parentPref);
			}
		}
	}

	/**
	 * @param parentPref
	 * @return
	 */
	private ICacheEditingPreference createCachePref(IEditingPreferences parentPref)
	{
		ICacheEditingPreference result = new CacheEditingPreference(parentPref);
		mapOfCachePref.put(parentPref, result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditingFacility#getNewEObjectUIFactory(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public INewEObjectComposition getNewEObjectUIFactory(EObject owner, EStructuralFeature feature, EObject children)
	{
		checkInit();
		if (children != null)
		{
			EClass eClass = children.eClass();
			List<EClass> supers = new ArrayList<>(eClass.getEAllSuperTypes());
			supers.add(eClass);
			for (int i = supers.size() - 1; i >= 0; i--)
			{
				eClass = supers.get(i);
				INewEObjectUIFactory factory = newUIFactories.get(eClass.getName());
				if (factory != null)
				{
					INewEObjectComposition composition = factory.accept(owner, feature, children);
					if (composition != null)
					{
						// found it, just return
						return composition;
					}
				}
			}
		}
		return null;
	}

}
