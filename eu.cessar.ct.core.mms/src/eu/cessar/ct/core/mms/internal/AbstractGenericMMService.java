/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 13, 2011 5:57:09 PM </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.artop.aal.common.resource.impl.AutosarXMLResourceImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.base.Function;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.mdl.IModelDependencyLookup2;

/**
 * @author uidt2045
 * 
 */
public abstract class AbstractGenericMMService implements IMetaModelService
{
	private static final String STRING_TRUE = "true"; //$NON-NLS-1$

	protected Map<EcucModuleConfigGroups, Set<String>> groups;
	protected List<String> allAssignedMD;

	private volatile IMMNamespaceMapping mmNamespaceMapping;
	private volatile Map<EStructuralFeature, String> ignorableFeatureToXMLNameMapping;

	/**
	 * Cache the associated ModelDependencyLookup.
	 */
	private volatile IModelDependencyLookup2 modelDependencyLookup;

	/**
	 * 
	 */
	public AbstractGenericMMService()
	{
		groups = new LinkedHashMap<IMetaModelService.EcucModuleConfigGroups, Set<String>>();
		allAssignedMD = new ArrayList<String>();
		fillGroups();
	}

	/**
	 * @param classes
	 * @return
	 */
	protected Collection<EClass> removeAbstract(Collection<EClass> classes)
	{
		Iterator<EClass> iterator = classes.iterator();
		Collection<EClass> result = new ArrayList<EClass>();
		while (iterator.hasNext())
		{
			EClass aux = iterator.next();
			if (!aux.isAbstract())
			{
				result.add(aux);
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	protected abstract IMMNamespaceMapping doGetMMNamespaceMapping();

	/**
	 * @return
	 */
	protected IMMNamespaceMapping getMMNamespaceMapping()
	{
		if (mmNamespaceMapping == null)
		{
			synchronized (this)
			{
				if (mmNamespaceMapping == null)
				{
					mmNamespaceMapping = doGetMMNamespaceMapping();
				}
			}
		}
		return mmNamespaceMapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMetaModelService#getRootPackageURI()
	 */
	public String getRootPackageURI()
	{
		return getMMNamespaceMapping().getRootNamespace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMetaModelService#getPackageLabel(java.lang.String)
	 */
	public String getPackageLabel(String packageURI)
	{
		return getMMNamespaceMapping().getPackageName(packageURI);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMetaModelService#getAllPackages()
	 */
	public Map<String, String> getAllPackages()
	{
		return getMMNamespaceMapping().getAllPackages();
	}

	/**
	 * Initializes the <code>ignorableFeatureToXMLNameMapping</code> if necessary and returns it.
	 * 
	 * @param classifiers
	 *        all classifiers belonging to a particular meta-model version
	 * @return the feature-XML name mapping
	 */
	protected Map<EStructuralFeature, String> getIgnorableXMLNameToFeatureMapping(EList<EClassifier> classifiers)
	{
		if (ignorableFeatureToXMLNameMapping == null)
		{
			synchronized (this)
			{
				if (ignorableFeatureToXMLNameMapping == null)
				{
					ignorableFeatureToXMLNameMapping = doGetIgnorableXMLNameToFeatureMapping(classifiers);
				}
			}
		}
		return ignorableFeatureToXMLNameMapping;

	}

	private static Map<EStructuralFeature, String> doGetIgnorableXMLNameToFeatureMapping(List<EClassifier> classifiers)
	{
		Map<EStructuralFeature, String> map = new HashMap<EStructuralFeature, String>();

		for (EClassifier classifier: classifiers)
		{
			if (classifier instanceof EClass)
			{
				EClass clz = (EClass) classifier;
				EList<EStructuralFeature> structuralFeatures = clz.getEStructuralFeatures();

				for (EStructuralFeature feature: structuralFeatures)
				{
					if (feature.isTransient() || feature.isVolatile())
					{
						continue;
					}

					EAnnotation eAnno = feature.getEAnnotation(MetaModelUtils.ANN_TAGGEDVALUES);
					if (eAnno != null)
					{
						EMap<String, String> details = eAnno.getDetails();
						String isRoleWrapperElement = details.get(MetaModelUtils.XML_ROLE_WRAPPER_ELEMENT);
						String isRoleElement = details.get(MetaModelUtils.XML_ROLE_ELEMENT);
						String isTypeElement = details.get(MetaModelUtils.XML_TYPE_ELEMENT);

						String name = getIgnorableXMLName(details, isRoleWrapperElement, isRoleElement, isTypeElement);

						if (!"".equals(name)) { //$NON-NLS-1$
							map.put(feature, name);
						}
					}
				}
			}
		}

		return map;
	}

	private static String getIgnorableXMLName(EMap<String, String> details, String isRoleWrapperElement,
		String isRoleElement, String isTypeElement)
	{
		String name = ""; //$NON-NLS-1$

		if (STRING_TRUE.equals(isRoleWrapperElement))
		{
			name = details.get(MetaModelUtils.XML_NAME_PLURAL);
		}
		else if (STRING_TRUE.equals(isRoleElement) && STRING_TRUE.equals(isTypeElement))
		{
			name = details.get(MetaModelUtils.XML_NAME);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMetaModelService#discardIgnorableFeatureToXMLNameMapping()
	 */
	public void discardIgnorableFeatureToXMLNameMapping()
	{
		ignorableFeatureToXMLNameMapping = null;
	}

	public Map<EcucModuleConfigGroups, Set<String>> groupedModuleDefinitionMap()
	{
		return groups;
	}

	protected abstract void fillGroups();

	protected void put(String configName, EcucModuleConfigGroups configGroup)
	{
		if (!groups.containsKey(configGroup))
		{
			groups.put(configGroup, new HashSet<String>());
		}
		groups.get(configGroup).add(configName.toLowerCase());
		allAssignedMD.add(configName.toLowerCase());
	}

	public List<String> getAllAssignedMD()
	{
		return allAssignedMD;
	}

	/**
	 * @return the associated ModelDependencyLookup for graph-based search.
	 */
	protected abstract IModelDependencyLookup2 doGetModelDependencyLookup();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMetaModelService#getModelDependencyLookup()
	 */
	@Override
	public IModelDependencyLookup2 getModelDependencyLookup()
	{
		if (null == modelDependencyLookup)
		{
			synchronized (this)
			{
				if (null == modelDependencyLookup)
				{
					modelDependencyLookup = doGetModelDependencyLookup();
				}
			}
		}
		return modelDependencyLookup;
	}

	/**
	 * Copies MM specific features from {@code source} to {@code dest}.
	 * 
	 * These include references to the definition for module configuration objects.
	 * 
	 * @param dest
	 *        the destination {@code EObject}
	 * @param source
	 *        the source {@code EObject}
	 */
	protected abstract void copyMMSpecificFeaturesForSplit(EObject dest, EObject source);

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IMetaModelService#splitEObject(EObject)
	 */
	public EObject splitEObject(EObject eObject)
	{
		EClass currentEClass = eObject.eClass();
		EObject copyObject = EcoreUtil.create(currentEClass);

		copyMMSpecificFeaturesForSplit(copyObject, eObject);
		return copyObject;
	}

	/**
	 * Return a setter {@code Function} for an EStructuralFeature given by name.
	 * 
	 * @param eClazz
	 *        the {@code EClass} that has the feature
	 * @param featureName
	 *        the feature name
	 * @return a setter {@code Function} for the feature's value
	 */
	protected static Function<Object, Object> getSetFunctionForFeature(final EClass eClazz, final String featureName)
	{
		return getSetFunctionForFeature(eClazz.getEStructuralFeature(featureName));
	}

	/**
	 * Return a setter {@code Function} for the feature's value.
	 * 
	 * @param feature
	 *        the {@code EStructuralFeature}
	 * @return setter {@code Function} for the feature's value
	 */
	protected static Function<Object, Object> getSetFunctionForFeature(final EStructuralFeature feature)
	{
		return new Function<Object, Object>()
		{
			public Object apply(Object aPair)
			{
				if (aPair instanceof CessarEObjectObjectPair)
				{
					CessarEObjectObjectPair pair = (CessarEObjectObjectPair) aPair;
					EObject eObject = pair.getEObject();
					Object value = pair.getValue();
					eObject.eSet(feature, value);
					return value;
				}
				return null;
			}
		};
	}

	/**
	 * Return a getter {@code Function} for the feature of {@link eClazz} given by name {@link featureName}.
	 * 
	 * @param eClazz
	 *        the {@code EClass}
	 * @param featureName
	 *        the feature's name
	 * @return a getter {@code Function} for the feature's value
	 */
	protected static Function<Object, Object> getGetFunctionForFeature(final EClass eClazz, final String featureName)
	{
		return getGetFunctionForFeature(eClazz.getEStructuralFeature(featureName));
	}

	/**
	 * Return a getter {@code Function} for {@link feature}.
	 * 
	 * @param feature
	 *        the {@code EStructuralFeature}
	 * @return a getter {@code Function} for the feature's value
	 */
	protected static Function<Object, Object> getGetFunctionForFeature(final EStructuralFeature feature)
	{
		return new Function<Object, Object>()
		{
			public Object apply(Object eObject)
			{
				return eObject instanceof EObject ? ((EObject) eObject).eGet(feature) : null;
			}
		};
	}

	/**
	 * Retrieve the root AUTOSAR object.
	 * 
	 * This became needed because of resources with multiple roots. See <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=418005"
	 * >https://bugs.eclipse.org/bugs/show_bug.cgi?id=418005</a>
	 * 
	 * @param resource
	 *        the resource
	 * @return the root AUTOSAR object or <code>null</code> if not found.
	 */
	protected EObject getRootAUTOSARObject(final Resource resource, EClass rootEClass)
	{
		Object obj = resource.getContents().get(0);
		if (obj instanceof AutosarXMLResourceImpl)
		{
			AutosarXMLResourceImpl res = (AutosarXMLResourceImpl) obj;
			// There could be multiple root objects inside a resource due to
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=418005
			for (EObject child: res.getContents())
			{
				if (child.eClass().equals(rootEClass))
				{
					return child;
				}
			}
		}
		else if (obj instanceof EObject)
		{
			EObject eObj = (EObject) obj;
			if (eObj.eClass().equals(rootEClass))
			{
				return eObj;
			}
		}
		return null;
	}

}
