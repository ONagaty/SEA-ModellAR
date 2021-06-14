/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Aug 19, 2011 11:01:27 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.edit.ui.IEditingFacility;
import eu.cessar.ct.edit.ui.facility.EEditorCategory;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorDelegate;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.composition.IEditorComposition;
import eu.cessar.ct.edit.ui.facility.composition.ISimpleComposition;
import eu.cessar.ct.edit.ui.facility.composition.SimpleCategory;
import eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion;
import eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansionProvider;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.internal.facility.ClassDefinitionElement;
import eu.cessar.ct.edit.ui.internal.facility.ClassifierDefinitionElement;
import eu.cessar.ct.edit.ui.internal.facility.ClassifierDefinitionElement.EFeatureType;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;
import eu.cessar.ct.edit.ui.internal.facility.FeatureDefinitionElement;
import eu.cessar.ct.edit.ui.internal.facility.RelationDefinitionElement;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * Provider for compositions of editors for the direct features of an EObject
 * 
 */
public class SimpleCompositionProvider extends AbstractCompositionProvider implements IEditorCompositionProvider
{
	private boolean initialized = false;

	private Map<EFeatureType, ClassifierDefinitionElement> defaultDefinitions;
	private Map<EFeatureType, List<ClassifierDefinitionElement>> classifierDefinitions;

	private List<FeatureDefinitionElement> featureDefinitions;
	private List<ClassDefinitionElement> classDefinitions;
	private List<RelationDefinitionElement> relationDefinitions;

	private Map<String, RelationDefinitionElement> relationDefinitionsMap;

	private static Map<EClass, Map<List<EStructuralFeature>, IModelFragmentEditorProvider>> cache = new HashMap<EClass, Map<List<EStructuralFeature>, IModelFragmentEditorProvider>>();

	/**
	 * Check if initialization is needed and call doInit if necessary
	 */
	private void checkInit()
	{
		if (!initialized)
		{
			synchronized (SimpleCompositionProvider.class)
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
		defaultDefinitions = new HashMap<EFeatureType, ClassifierDefinitionElement>();

		classifierDefinitions = new HashMap<EFeatureType, List<ClassifierDefinitionElement>>();
		classifierDefinitions.put(EFeatureType.ENUM, //
			new ArrayList<ClassifierDefinitionElement>());
		classifierDefinitions.put(EFeatureType.PRIMITIVE, new ArrayList<ClassifierDefinitionElement>());
		classifierDefinitions.put(EFeatureType.REFERENCE, new ArrayList<ClassifierDefinitionElement>());

		featureDefinitions = new ArrayList<FeatureDefinitionElement>();
		classDefinitions = new ArrayList<ClassDefinitionElement>();
		relationDefinitions = new ArrayList<RelationDefinitionElement>();

		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(FacilityConstants.NAMESPACE,
			FacilityConstants.EXTENSION_MODEL_EDITORS);
		IConfigurationElement[] extensions = extensionPoint.getConfigurationElements();
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement extension = extensions[i];
			String defType = extension.getName();
			if (FacilityConstants.ELEM_CLASSIFIER_DEFINITION.equals(defType))
			{
				ClassifierDefinitionElement def = new ClassifierDefinitionElement(extension);
				if (def.isDefault())
				{
					defaultDefinitions.put(def.getFeatureType(), def);
				}
				else
				{
					classifierDefinitions.get(def.getFeatureType()).add(def);
				}
			}
			else if (FacilityConstants.ELEM_FEATURE_DEFINITION.equals(defType))
			{
				featureDefinitions.add(new FeatureDefinitionElement(extension));

			}
			else if (FacilityConstants.ELEM_CLASS_DEFINITION.equals(defType))
			{
				classDefinitions.add(new ClassDefinitionElement(extension));
			}
			else if (FacilityConstants.ELEM_RELATION_DEFINITION.equals(defType))
			{
				relationDefinitions.add(new RelationDefinitionElement(extension));
			}
			else
			{
				CessarPluginActivator.getDefault().logError(Messages.Unknown_configuration_element, extension);
			}
		}

		relationDefinitionsMap = new HashMap<String, RelationDefinitionElement>();
		for (RelationDefinitionElement elem: relationDefinitions)
		{
			List<String> idList = elem.getMasterEditorID();
			for (String id: idList)
			{
				relationDefinitionsMap.put(id, elem);
			}
		}

	}

	public IModelFragmentEditorExpansion getEditorExpansion(IModelFragmentEditor editor)
	{
		String id = editor.getTypeId();
		RelationDefinitionElement elem = relationDefinitionsMap.get(id);
		if (elem != null)
		{
			IModelFragmentEditorExpansionProvider extensionProvider = elem.getExpansionProvider();
			if (extensionProvider != null)
			{
				IModelFragmentEditorExpansion expansion = extensionProvider.createExpansion(editor,
					elem.getProperties());
				return expansion;
			}
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditorCompositionProvider#getEditorCompositions(org.eclipse.emf.ecore.EObject)
	 */
	public List<ISimpleComposition> getEditorCompositions(EObject object)
	{
		return getEditorCompositions(object, object.eClass(), object.eClass().getEAllStructuralFeatures(),
			IEditingFacility.EDITOR_ALL, EEditorCategory.VALUES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditorCompositionProvider#getEditorCompositions(org.eclipse.emf.ecore.EClass)
	 */
	public List<ISimpleComposition> getEditorCompositions(EClass clz)
	{
		return getEditorCompositions(null, clz, clz.getEAllStructuralFeatures(), IEditingFacility.EDITOR_ALL,
			EEditorCategory.VALUES);
	}

	public List<ISimpleComposition> getEditorCompositions(EObject object, List<EStructuralFeature> features,
		int editorTypes)
	{
		return getEditorCompositions(object, object.eClass(), features, editorTypes, EEditorCategory.VALUES);
	}

	public List<ISimpleComposition> getEditorCompositions(EClass eClass, List<EStructuralFeature> features,
		int editorTypes)
	{
		return getEditorCompositions(null, eClass, features, editorTypes, EEditorCategory.VALUES);
	}

	public List<IModelFragmentEditorProvider> getEditors(EObject object, EClass clz, List<EStructuralFeature> features,
		int editorTypes)
	{
		checkInit();

		// lookup the MMService in this moment to improve the performance
		IMetaModelService service = lookupMMService(object, clz);
		List<EStructuralFeature> disposableFeatureList = new ArrayList<EStructuralFeature>(features);

		List<IModelFragmentEditorProvider> providers = new ArrayList<IModelFragmentEditorProvider>();
		if ((editorTypes & IEditingFacility.EDITOR_CLASS) != 0)
		{
			providers.addAll(lookupClassEditorProvider(service, object, clz, disposableFeatureList));
		}
		if ((editorTypes & IEditingFacility.EDITOR_FEATURE) != 0)
		{
			providers.addAll(lookupFeatureEditorProvider(service, object, clz, clz, disposableFeatureList));
		}
		if ((editorTypes & IEditingFacility.EDITOR_CLASSIFIER) != 0)
		{
			providers.addAll(lookupClassifierEditorProviders(service, object, clz, disposableFeatureList));
		}

		Collections.sort(providers, new EditorOrderComparator());

		return providers;
	}

	public List<ISimpleComposition> getEditorCompositions(EObject object, EClass clz,
		List<EStructuralFeature> features, int editorTypes, List<EEditorCategory> acceptedCategories)
	{
		List<IModelFragmentEditorProvider> providers = getEditors(object, clz, features, editorTypes);
		List<ISimpleComposition> compositionsByCategory = groupByCategory(clz, providers, acceptedCategories);

		return compositionsByCategory;
	}

	/**
	 * Groups the editor providers into {@link ISimpleEditorComposition} according to the supported categories.
	 * 
	 * @param clz
	 * 
	 * @param providers
	 * @param acceptedCategories
	 * @return
	 */
	private List<ISimpleComposition> groupByCategory(EClass clz, List<IModelFragmentEditorProvider> providers,
		List<EEditorCategory> acceptedCategories)
	{
		List<ISimpleComposition> result = new ArrayList<ISimpleComposition>();

		for (IModelFragmentEditorProvider provider: providers)
		{
			String[] categories = provider.getCategories();
			for (String cat: categories)
			{

				boolean accepted = false;
				for (EEditorCategory accCat: acceptedCategories)
				{
					if (accCat.getName().endsWith(cat))
					{
						accepted = true;
						break;
					}
				}

				if (!accepted)
				{
					continue;
				}

				ISimpleComposition composition = null;
				for (ISimpleComposition compo: result)
				{
					if (compo.getCategory().getName().equals(cat))
					{
						composition = compo;
						break;
					}
				}

				if (composition == null)
				{
					// for each category create a simple composition
					composition = new SimpleEditorComposition(this);
					composition.setCategory(new SimpleCategory(clz, cat));

					result.add(composition);
				}

				// could happen if the received list contains duplicates
				if (result.contains(provider))
				{
					continue;
				}
				composition.addEditorProvider(provider);
			}
		}

		return result;
	}

	private List<IModelFragmentEditorProvider> lookupClassEditorProvider(IMetaModelService mmService, EObject obj,
		EClass rootEClass, List<EStructuralFeature> features)
	{
		List<IModelFragmentEditorProvider> result = new ArrayList<IModelFragmentEditorProvider>();

		EList<EClass> superTypes = new BasicEList<EClass>(rootEClass.getEAllSuperTypes());
		superTypes.add(rootEClass);
		for (int i = superTypes.size() - 1; i >= 0; i--)
		{
			List<IModelFragmentEditorProvider> superEditors = lookupClassEditorProvider(mmService, obj, rootEClass,
				superTypes.get(i), features);
			result.addAll(superEditors);
			if (features.isEmpty())
			{
				break;
			}
		}
		return result;
	}

	/**
	 * @param obj
	 * @param clz
	 * @param features
	 * @return
	 */
	private List<IModelFragmentEditorProvider> lookupClassEditorProvider(IMetaModelService mmService, EObject obj,
		EClass rootEClass, EClass clz, List<EStructuralFeature> features)
	{
		if (features.isEmpty())
		{
			return Collections.emptyList();
		}
		List<IModelFragmentEditorProvider> result = new ArrayList<IModelFragmentEditorProvider>();

		// result.addAll(checkCache(clz, features));

		// if (features.isEmpty())
		// {
		// return result;
		// }

		for (ClassDefinitionElement elem: classDefinitions)
		{
			if (elem.isSelected(mmService, obj, clz))
			{
				IModelFragmentEditorDelegate delegate = elem.getDelegate();
				if (delegate == null)
				{
					continue;
				}

				List<IModelFragmentEditorProvider> editors = delegate.getEditors(obj, rootEClass);

				for (IModelFragmentEditorProvider provider: editors)
				{
					List<EStructuralFeature> editedFeatures = provider.getEditedFeatures();
					if (provider.isMetaEditor() || !Collections.disjoint(features, editedFeatures))
					{
						result.add(provider);
						features.removeAll(editedFeatures);

						if (elem.isCacheable() && !provider.isMetaEditor())
						{ // SUPPRESS CHECKSTYLE check in future
							// addToCache(clz, editedFeatures, provider);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param obj
	 * @param clz
	 * @param features
	 * @return
	 */
	private List<IModelFragmentEditorProvider> lookupFeatureEditorProvider(IMetaModelService mmService, EObject obj,
		EClass rootEClass, EClass clz, List<EStructuralFeature> features)
	{
		if (features.isEmpty())
		{
			return Collections.emptyList();
		}
		List<IModelFragmentEditorProvider> result = new ArrayList<IModelFragmentEditorProvider>();

		collectFromCache(checkCache(clz, features), rootEClass, result);

		collectFromFeatureDefinitions(mmService, obj, clz, features, result);

		// recursive call for super classes
		if (!features.isEmpty())
		{
			EList<EClass> superTypes = clz.getESuperTypes();
			for (EClass superClz: superTypes)
			{
				List<IModelFragmentEditorProvider> superEditors = lookupFeatureEditorProvider(mmService, obj,
					rootEClass, superClz, features);
				result.addAll(superEditors);
				if (features.isEmpty())
				{
					break;
				}
			}
		}
		return result;
	}

	private void collectFromCache(List<IModelFragmentEditorProvider> cacheList, EClass rootEClass,
		List<IModelFragmentEditorProvider> result)
	{
		for (IModelFragmentEditorProvider provider: cacheList)
		{
			if (provider.canMigrateToEClass(rootEClass))
			{
				result.add(provider.migrateToEClass(rootEClass));
			}
			else
			{
				result.add(provider);
			}
		}
	}

	private void collectFromFeatureDefinitions(IMetaModelService mmService, EObject obj, EClass clz,
		List<EStructuralFeature> features, List<IModelFragmentEditorProvider> result)
	{
		Iterator<EStructuralFeature> it = features.iterator();
		while (it.hasNext())
		{
			boolean found = false;
			EStructuralFeature feature = it.next();
			for (FeatureDefinitionElement elem: featureDefinitions)
			{
				if (elem.isSelected(mmService, obj, clz, feature))
				{
					IModelFragmentEditorProvider[] providers = elem.getEditorProviders(mmService, obj, clz, feature);

					result.addAll(Arrays.asList(providers));

					for (IModelFragmentEditorProvider provider: providers)
					{
						if (elem.isCacheable() && !provider.isMetaEditor())
						{
							addToCache(clz, provider.getEditedFeatures(), provider);
						}
					}
					found = true;
					break;
				}
			}
			if (found)
			{
				it.remove();
			}
		}
	}

	/**
	 * verify if the cache contains the needed editors
	 * 
	 * @param clz
	 * @param features
	 * @return
	 */
	private List<IModelFragmentEditorProvider> checkCache(EClass clz, List<EStructuralFeature> features)
	{
		List<IModelFragmentEditorProvider> result = new ArrayList<IModelFragmentEditorProvider>();

		if (cache.containsKey(clz))
		{
			Map<List<EStructuralFeature>, IModelFragmentEditorProvider> cacheValues = cache.get(clz);
			Set<List<EStructuralFeature>> keySet = cacheValues.keySet();
			for (List<EStructuralFeature> cachedFeatures: keySet)
			{
				if (features.containsAll(cachedFeatures))
				{
					features.removeAll(cachedFeatures);
					result.add(cacheValues.get(cachedFeatures));
				}
			}
		}
		return result;
	}

	/**
	 * @param clz
	 * @param featureList
	 * @param asList
	 */
	private void addToCache(EClass clz, List<EStructuralFeature> featureList,
		IModelFragmentEditorProvider editorProvider)
	{
		Map<List<EStructuralFeature>, IModelFragmentEditorProvider> cacheValue;
		if (cache.containsKey(clz))
		{
			// the class allready exists in the cache
			cacheValue = cache.get(clz);
		}
		else
		{
			cacheValue = new HashMap<List<EStructuralFeature>, IModelFragmentEditorProvider>();
			cache.put(clz, cacheValue);
		}
		cacheValue.put(featureList, editorProvider);
	}

	/**
	 * @param obj
	 * @param clz
	 * @param features
	 * @return
	 */
	private List<IModelFragmentEditorProvider> lookupClassifierEditorProviders(IMetaModelService mmService,
		EObject obj, EClass clz, List<EStructuralFeature> features)
	{
		if (features.isEmpty())
		{
			return Collections.emptyList();
		}
		List<IModelFragmentEditorProvider> result = new ArrayList<IModelFragmentEditorProvider>();
		for (EStructuralFeature feature: features)
		{
			EFeatureType fType = EFeatureType.getFeatureType(feature);
			IModelFragmentEditorProvider[] editorProviders = null;
			for (ClassifierDefinitionElement elem: classifierDefinitions.get(fType))
			{
				if (elem.isSelected(mmService, obj, clz, feature))
				{
					editorProviders = elem.getEditorProviders(mmService, obj, clz, feature);
					break;
				}
			}
			if (editorProviders == null)
			{
				editorProviders = defaultDefinitions.get(fType).getEditorProviders(mmService, obj, clz, feature);
			}
			result.addAll(Arrays.asList(editorProviders));
			for (IModelFragmentEditorProvider provider: editorProviders)
			{
				if (!provider.isMetaEditor())
				{

					List<EStructuralFeature> editedFeatures = provider.getEditedFeatures();
					if (editedFeatures != null)
					{
						// wrong
						if (editedFeatures.size() == 1)
						{
							EStructuralFeature feat = editedFeatures.get(0);
							addToCache(clz, editedFeatures, provider);
						}
						else
						{
							// should never happen but just to be sure
							addToCache(clz, editedFeatures, provider);
						}
					}
				}
			}
		}
		features.clear();
		return result;
	}

	/**
	 * @param containerDef
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getEditorsForDefinition(GParamConfContainerDef containerDef)
	{

		for (ClassDefinitionElement el: classDefinitions)
		{
			if (el.getDelegate() instanceof IEcucDelegate)
			{
				return ((IEcucDelegate) el.getDelegate()).getEditorsForDefinition(containerDef);
			}
		}

		return Collections.emptyList();
	}

	/**
	 * Search for a suitable MMService, could return null
	 * 
	 * @param object
	 * @param clz
	 * @return
	 */
	private IMetaModelService lookupMMService(EObject object, EClass clz)
	{
		IMetaModelService mmService = null;
		if (MetaModelUtils.isModelClass(clz))
		{
			mmService = MMSRegistry.INSTANCE.getMMService(clz);
		}
		else
		{
			if (object != null)
			{
				mmService = MMSRegistry.INSTANCE.getMMService(object.eClass());
			}
		}
		return mmService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.internal.facility.composition.IEditorCompositionProvider#doComputeEditors(eu.cessar.ct.edit
	 * .ui.facility.composition.IEditorComposition)
	 */
	public void doComputeEditors(IEditorComposition<?> composition)
	{
		// nothing to be done for this type of provider, as it populates the
		// composition immediately after instantiation
	}

}
