/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jun 16, 2010 10:34:48 AM </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.artop.aal.common.metamodel.AutosarMetaModelVersionData;
import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.gautosar.services.splitting.Splitables;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;
import org.eclipse.sphinx.emf.metamodel.MetaModelDescriptorRegistry;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.osgi.framework.Version;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.SafeUsageCrossReferencer;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.utils.IModelDependencyLookup;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.ModelUtils.Service;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * This class represent the implementation of the ModelUtils class from the SDK
 *
 */
public final class SDKModelUtils implements Service
{

	/**
	 * @author uidu3379
	 *
	 */
	private final class UpdateModelRunnable implements Runnable
	{
		private final String[] splitedPath;
		private final EObject secondBase;
		private final IGenericFactory genericFactory;
		private final int a;
		private final List<GARPackage> result;

		private UpdateModelRunnable(String[] splitedPath, EObject secondBase, IGenericFactory genericFactory, int a,
			List<GARPackage> result)
		{
			this.splitedPath = splitedPath;
			this.secondBase = secondBase;
			this.genericFactory = genericFactory;
			this.a = a;
			this.result = result;
		}

		public void run()
		{
			List<GARPackage> list = baseCheck(secondBase);
			GARPackage createARPackage = null;
			for (int i = a - 1; i < splitedPath.length; i++)
			{
				createARPackage = genericFactory.createARPackage();
				createARPackage.gSetShortName(splitedPath[i]);
				list.add(createARPackage);
				list = baseCheck(createARPackage);
			}
			result.add(createARPackage);
		}

		private List<GARPackage> baseCheck(@SuppressWarnings("hiding") final EObject secondBase)
		{
			List<GARPackage> list;
			if (secondBase instanceof GAUTOSAR)
			{
				list = ((GAUTOSAR) secondBase).gGetArPackages();
			}
			else
			{
				list = ((GARPackage) secondBase).gGetSubPackages();
			}
			return list;
		}
	}

	/**
	 * SDKModelUtils instance
	 */
	public static final SDKModelUtils eINSTANCE = new SDKModelUtils();

	private SDKModelUtils()
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getAbsoluteQualifiedName(org.eclipse.emf.ecore.EObject)
	 */
	public String getAbsoluteQualifiedName(EObject object)
	{
		return MetaModelUtils.getAbsoluteQualifiedName(object);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getDirtyResources(org.eclipse.core.resources.IProject)
	 */
	public Collection<Resource> getDirtyResources(IProject project)
	{
		return EResourceUtils.getDirtyResources(project);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#saveResources(java.util.Collection,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void saveResources(Collection<Resource> resources, IProgressMonitor monitor)
	{
		try
		{
			EResourceUtils.saveResources(resources, monitor, true);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectWithQualifiedName(org.eclipse.core.resources.IProject,
	 * java.lang.String)
	 */
	public EObject getEObjectWithQualifiedName(IProject project, String qualifiedName)
	{
		List<EObject> found = getEObjectsWithQualifiedName(project, qualifiedName);
		if (found.size() != 1)
		{
			return null;
		}
		else
		{
			return found.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectWithQualifiedName(org.eclipse.core.resources.IProject,
	 * java.lang.String, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#4")
	@Override
	public GIdentifiable getEObjectWithQualifiedName(IProject project, String qualifiedName, boolean merged)
	{
		if (!merged)
		{
			return (GIdentifiable) getEObjectWithQualifiedName(project, qualifiedName);
		}

		List<EObject> foundMerged = getMergedInstances(getEObjectsWithQualifiedName(project, qualifiedName));

		List<GIdentifiable> mergedGIdentifiables = getGIdentifiableList(foundMerged);

		if (mergedGIdentifiables.size() != 1)
		{
			return null;
		}
		else
		{

			return mergedGIdentifiables.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectsWithQualifiedName(org.eclipse.core.resources.IProject,
	 * java.lang.String)
	 */
	public List<EObject> getEObjectsWithQualifiedName(IProject project, String qualifiedName)
	{
		return EObjectLookupUtils.getEObjectsWithQName(project, qualifiedName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectWithQualifiedName(org.eclipse.core.resources.IProject,
	 * java.lang.String, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#4")
	@Override
	public List<GIdentifiable> getEObjectsWithQualifiedName(IProject project, String qualifiedName, boolean merged)
	{
		List<EObject> eObjectList = new ArrayList<EObject>();
		if (!merged)
		{
			eObjectList = getEObjectsWithQualifiedName(project, qualifiedName);
			return getGIdentifiableList(eObjectList);
		}

		eObjectList = getMergedInstances(getEObjectsWithQualifiedName(project, qualifiedName));
		return getGIdentifiableList(eObjectList);
	}

	/**
	 * @param eObjectList
	 * @return list of {@link GIdentifiable} objects
	 */
	private List<GIdentifiable> getGIdentifiableList(List<EObject> eObjectList)
	{
		List<GIdentifiable> gIdentList = new ArrayList<GIdentifiable>();
		for (EObject obj: eObjectList)
		{
			if (obj instanceof GIdentifiable)
			{
				gIdentList.add((GIdentifiable) obj);
			}
		}
		return gIdentList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getFilteredList(java.lang.Class,
	 * org.eclipse.emf.common.util.EList)
	 */
	public <T> EList<T> getFilteredList(Class<T> subType, EList<? super T> ownerList)
	{
		return MetaModelUtils.getFilteredList(subType, ownerList, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#createAutosarFile(org.eclipse.core.resources.IFile, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#1")
	public GAUTOSAR createAutosarFile(IFile autosarFile, boolean forceCreate, IProgressMonitor monitor)
	{
		Assert.isNotNull(autosarFile);
		if (monitor == null)
		{
			monitor = new NullProgressMonitor();// SUPPRESS CHECKSTYLE it's ok
		}
		if (autosarFile.exists() && !forceCreate)
		{
			Resource resource = EcorePlatformUtil.getResource(autosarFile);
			return (GAUTOSAR) resource.getContents().get(0);
		}
		else
		{
			IProject project = autosarFile.getProject();
			AutosarReleaseDescriptor release = MetaModelUtils.getAutosarRelease(project);
			Assert.isNotNull(release);

			IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(project);
			GAUTOSAR gautosar = factory.createAUTOSAR();

			TransactionalEditingDomain domain = WorkspaceEditingDomainUtil.getEditingDomain(project, release);

			Map<String, Object> options = new HashMap<String, Object>();
			options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
			// options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
			options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);

			EcorePlatformUtil.saveNewModelResource(domain, autosarFile.getFullPath(), release.getDefaultContentTypeId(),
				gautosar, options, false, monitor);
			PlatformUtils.waitForModelLoading(project, monitor);
			return gautosar;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getAutosarFiles(org.eclipse.core.resources.IProject)
	 */
	public Collection<IFile> getAutosarFiles(IProject project)
	{
		return EResourceUtils.getProjectFiles(project);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getAutosarResources(org.eclipse.core.resources.IProject)
	 */
	public Collection<Resource> getAutosarResources(IProject project)
	{
		return EResourceUtils.getProjectResources(project);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getInstancesOfType(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, boolean)
	 */
	public List<EObject> getInstancesOfType(IProject project, EClass clazz, boolean exactMatch)
	{
		return EObjectLookupUtils.getInstancesOfType(project, clazz, exactMatch);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getInstancesOfType(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#4")
	public List<EObject> getInstancesOfType(IProject project, EClass clazz, boolean exactMatch, boolean merged)
	{
		if (!merged)
		{
			return EObjectLookupUtils.getInstancesOfType(project, clazz, exactMatch);
		}

		return getMergedInstances(EObjectLookupUtils.getInstancesOfType(project, clazz, exactMatch));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getInstancesOfType(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#3")
	public List<EObject> getInstancesOfType(EObject context, EClass clazz, boolean exactMatch)
	{
		if (!isMerged(context))
		{
			List<EObject> instances = EObjectLookupUtils.getInstancesOfType(context, clazz, exactMatch);
			return instances;
		}
		else
		{
			List<EObject> mergedInstances = new ArrayList<EObject>();

			if (context instanceof GARObject)
			{
				// obtain the fragments of the merged object
				Collection<GARObject> fragments = SplitUtils.getConcreteInstances((GARObject) context);
				for (GARObject fragment: fragments)
				{
					// perform the search in each fragment
					List<EObject> instances = EObjectLookupUtils.getInstancesOfType(fragment, clazz, exactMatch);
					for (EObject eObject: instances)
					{
						// get the merged instance corresponding to the found object
						GARObject mergedInstance = SplitUtils.getMergedInstance((GARObject) eObject);

						// add it to result if not already present
						if (!mergedInstances.contains(mergedInstance))
						{
							mergedInstances.add(mergedInstance);
						}
					}
				}
			}
			return mergedInstances;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getDefiningFile(org.eclipse.emf.ecore.EObject)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#2")
	public IFile getDefiningFile(EObject eObject)
	{

		if (isMerged(eObject))
		{
			return getDefiningFile(SplitUtils.getActiveResource((GARObject) eObject));
		}

		return EcorePlatformUtil.getFile(eObject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getDefiningFile(org.eclipse.emf.ecore.resource.Resource)
	 */
	public IFile getDefiningFile(Resource resource)
	{
		return EcorePlatformUtil.getFile(resource);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectWithQualifiedName(org.eclipse.emf.ecore.resource.Resource,
	 * java.lang.String)
	 */
	public GIdentifiable getEObjectWithQualifiedName(Resource resource, String qualifiedName)
	{
		List<GIdentifiable> objectsWithQName = getEObjectsWithQualifiedName(resource, qualifiedName);
		if (objectsWithQName.size() != 1)
		{
			return null;
		}
		else
		{
			return objectsWithQName.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectWithQualifiedName(org.eclipse.emf.ecore.EObject,
	 * java.lang.String)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#3")
	@Override
	public GIdentifiable getEObjectWithQualifiedName(EObject current, String qualifiedName)
	{
		List<GIdentifiable> foundInstances = getEObjectsWithQualifiedName(current, qualifiedName);
		if (foundInstances.size() != 1)
		{
			return null;
		}
		return foundInstances.get(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectsWithQualifiedName(org.eclipse.emf.ecore.resource.Resource,
	 * java.lang.String)
	 */
	public List<GIdentifiable> getEObjectsWithQualifiedName(Resource resource, String qualifiedName)
	{
		List<EObject> list = EObjectLookupUtils.getEObjectsWithQName(resource, qualifiedName);

		List<GIdentifiable> result = new ArrayList<GIdentifiable>();
		for (int i = 0; i < list.size(); i++)
		{
			result.add((GIdentifiable) list.get(i));
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getEObjectsWithQualifiedName(org.eclipse.emf.ecore.EObject,
	 * java.lang.String)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#3")
	@Override
	public List<GIdentifiable> getEObjectsWithQualifiedName(EObject current, String qualifiedName)
	{
		if (isMerged(current))
		{
			List<EObject> found = getEObjectsWithQualifiedName(EcorePlatformUtil.getFile(current).getProject(),
				qualifiedName);

			List<GIdentifiable> mergedList = new ArrayList<GIdentifiable>();
			for (EObject notMerged: found)
			{
				GIdentifiable merged = (GIdentifiable) SplitUtils.getMergedInstance((GARObject) notMerged);
				if (!mergedList.contains(merged))
				{
					mergedList.add(merged);
				}
			}
			return mergedList;
		}
		return getEObjectsWithQualifiedName(current.eResource(), qualifiedName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getGIdentifiableWithRelativeName(gautosar.ggenericstructure.
	 * ginfrastructure .GIdentifiable, java.lang.String)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#3")
	public GIdentifiable getGIdentifiableWithRelativeName(GIdentifiable current, String relativeName)
	{
		if (!isMerged(current))
		{
			return MetaModelUtils.getRelativeIdentifiables(current, relativeName);
		}
		else
		{
			List<GIdentifiable> gIdentList = getMergedGIdentifiablesWithRelativeName(current, relativeName);

			if (gIdentList.size() != 1)
			{
				return null;
			}
			return gIdentList.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getIdentifiables(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	public GIdentifiable getIdentifiable(EObject root, String shortName)
	{
		List<GIdentifiable> identifiablesWithShortName = getIdentifiables(root, shortName);
		if (identifiablesWithShortName == null)
		{
			return null;
		}
		return identifiablesWithShortName.get(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getIdentifiables(org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	public List<GIdentifiable> getIdentifiables(EObject parent, String shortName)
	{
		List<GIdentifiable> childObj = new ArrayList<GIdentifiable>();
		if (parent != null)
		{
			EList<EObject> contents = parent.eContents();
			for (EObject eObject: contents)
			{
				if (eObject instanceof GIdentifiable)
				{
					if (((GIdentifiable) eObject).gGetShortName().equals(shortName))
					{
						childObj.add((GIdentifiable) eObject);
					}
				}
			}
		}
		if (childObj.isEmpty())
		{
			return null;
		}
		return childObj;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#createAutosarFile(org.eclipse.core.resources.IFile, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Resource getDefinedResource(IFile autosarFile)
	{
		Assert.isNotNull(autosarFile);

		if (autosarFile.exists())
		{
			Resource resource = EcorePlatformUtil.getResource(autosarFile);
			return resource;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getReferencedBy(org.eclipse.emf.ecore.EObject)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#3")
	public Map<EObject, List<EReference>> getReferencedBy(EObject source)
	{
		Map<EObject, List<EReference>> map = new LinkedHashMap<EObject, List<EReference>>();

		try
		{
			// check whether the given object is a concrete or a merged one
			if (!isMerged(source))
			{
				IFile file = EcorePlatformUtil.getFile(source);
				if (file != null)
				{
					Collection<Resource> resources = EResourceUtils.getProjectResources(
						EcorePlatformUtil.getFile(source).getProject());
					map = getReferencedByOfConcreteObject(source, resources);
				}
			}
			else
			{
				map = getReferencedByOfMergedObject(source);
			}
		}
		catch (Throwable t) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(t);
		}

		return map;
	}

	/**
	 * A list of merged object is built. It doesn't contain duplicates
	 *
	 * @param found
	 *        list of EObjects
	 * @return a list of merged EObject
	 */
	private List<EObject> getMergedInstances(List<EObject> found)
	{
		List<EObject> returnList = new ArrayList<EObject>();

		for (EObject notMerged: found)
		{
			if (notMerged instanceof GARObject)
			{
				EObject merged = SplitUtils.getMergedInstance((GARObject) notMerged);
				if (!returnList.contains(merged))
				{
					returnList.add(merged);
				}
			}
			else
			{
				LoggerFactory.getLogger().warn(notMerged.toString() + "is not a GARObject!"); //$NON-NLS-1$
			}
		}
		return returnList;
	}

	/**
	 * Returns whether the given object represents a merged instance
	 *
	 * @param eObj
	 *        the EObject to inspect
	 * @return whether the passed object represents a merged instance
	 */
	private static boolean isMerged(EObject eObj)
	{
		return Splitables.isSplitable(eObj);
	}

	/**
	 * Returns a mapping of (merged EObject,list of references) that refers the given merged object.
	 *
	 * @param mergedEObject
	 *        the merged object
	 * @return the corresponding mapping
	 */
	private static Map<EObject, List<EReference>> getReferencedByOfMergedObject(EObject mergedEObject)
	{
		Assert.isTrue(mergedEObject instanceof GARObject);
		Assert.isTrue(SplitUtils.isMergedInstace((GARObject) mergedEObject));

		Map<EObject, List<EReference>> map = new LinkedHashMap<EObject, List<EReference>>();

		// obtain the fragments of the merged object
		Collection<GARObject> concreteInstances = SplitUtils.getConcreteInstances((GARObject) mergedEObject);

		GARObject activeInstance = SplitUtils.getActiveInstance((GARObject) mergedEObject);
		Collection<Resource> resources = EResourceUtils.getProjectResources(
			EcorePlatformUtil.getFile(activeInstance).getProject());

		for (GARObject instance: concreteInstances)
		{
			// search the references towards each concrete instance
			Map<EObject, List<EReference>> mappingPerInstance = getReferencedByOfConcreteObject(instance, resources);
			Set<EObject> referrringObjects = mappingPerInstance.keySet();

			for (EObject referringEObj: referrringObjects)
			{
				// get the merged object of the referring object
				GARObject mergedReferrringEObj = SplitUtils.getMergedInstance((GARObject) referringEObj);

				// add new entry if necessary
				if (!map.containsKey(mergedReferrringEObj))
				{
					map.put(mergedReferrringEObj, new ArrayList<EReference>());
				}

				List<EReference> existingReferences = map.get(mergedReferrringEObj);
				List<EReference> referencesPerInstance = mappingPerInstance.get(referringEObj);
				for (EReference ref: referencesPerInstance)
				{
					if (!existingReferences.contains(ref))
					{
						existingReferences.add(ref);
					}
				}
			}
		}

		return map;
	}

	/**
	 * Returns a mapping of (EObject,list of references) from the given resources that refers the given object.
	 *
	 * @param nonMergedObject
	 *        object from a physical file
	 * @param resources
	 *        collection of EMF resources where to search for references towards the given object
	 * @return corresponding map
	 */
	private static Map<EObject, List<EReference>> getReferencedByOfConcreteObject(EObject nonMergedObject,
		Collection<Resource> resources)
	{
		Map<EObject, List<EReference>> map = new LinkedHashMap<EObject, List<EReference>>();

		for (Resource resource: resources)
		{
			Collection<Setting> collection = SafeUsageCrossReferencer.find(nonMergedObject, resource);
			for (Setting setting: collection)
			{
				if (setting.getEStructuralFeature() instanceof EReference)
				{
					EReference ref = (EReference) setting.getEStructuralFeature();
					if (!ref.isContainment())
					{
						List<EReference> list;
						EObject eObject = setting.getEObject();

						list = map.get(eObject);
						if (null == list)
						{
							list = new ArrayList<EReference>();
							map.put(eObject, list);
						}
						list.add(ref);
					}
				}
			}
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getOrCreateARPackage(gautosar.ggenericstructure.ginfrastructure.
	 * GAUTOSAR , java.lang.String)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#2")
	public GARPackage getOrCreateARPackage(GAUTOSAR base, String arPackagePath)
	{
		if (isMerged(base))
		{
			throw new IllegalArgumentException("GAUTOSAR shall not be a merged object!"); //$NON-NLS-1$
		}

		EObject parent = base;
		boolean aux = true;

		final String[] splitedPath = MetaModelUtils.splitQualifiedName(
			MetaModelUtils.normalizeQualifiedName(arPackagePath));
		int j;
		for (j = 0; j < splitedPath.length && aux; j++)
		{
			EList<EObject> contents = parent.eContents();
			aux = false;
			for (int i = 0; i < contents.size(); i++)
			{
				EObject eObject = contents.get(i);
				if (eObject instanceof GARPackage)
				{
					GARPackage pack = (GARPackage) eObject;
					String shortName = pack.gGetShortName();
					if (splitedPath[j].equals(shortName))
					{
						parent = pack;
						aux = true;
						break;
					}
				}
			}
		}
		if (!aux)
		{

			// GARPackage does not exist... create one respecting the given path
			@SuppressWarnings("deprecation")
			final IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(base);

			final EObject secondBase = parent;
			final List<GARPackage> result = new ArrayList<GARPackage>();

			final int a = j;
			try
			{
				ExecutionService.getRunningManager(ModelUtils.getDefiningFile(base).getProject()).updateModel(
					new UpdateModelRunnable(splitedPath, secondBase, genericFactory, a, result));
			}
			catch (ExecutionException e)
			{
				// the package could not be created
				return null;
			}

			return result.get(0);
		}
		else
		{
			return (GARPackage) parent;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getGIdentifiablesWithRelativeName(gautosar.ggenericstructure.
	 * ginfrastructure.GIdentifiable, java.lang.String)
	 */
	@Requirement(
		reqID = "REQ_API_MODELUTILS_MERGED#3")
	public List<GIdentifiable> getGIdentifiablesWithRelativeName(GIdentifiable parent, String relativeQName)
	{
		List<GIdentifiable> result = new ArrayList<GIdentifiable>();
		if (!isMerged(parent))
		{
			EList<EObject> eContents = parent.eContents();

			String[] splitedQualifiedName = MetaModelUtils.splitQualifiedName(
				MetaModelUtils.normalizeQualifiedName(relativeQName));
			String correctedRelativeQName = relativeQName.substring(splitedQualifiedName[0].length());

			for (int i = 0; i < eContents.size(); i++)
			{
				if (eContents.get(i) instanceof GIdentifiable)
				{
					GIdentifiable child = (GIdentifiable) eContents.get(i);
					if (child.gGetShortName().equals(splitedQualifiedName[0]))
					{
						GIdentifiable gIdentifiable;
						if (splitedQualifiedName.length > 1)
						{
							gIdentifiable = MetaModelUtils.getRelativeIdentifiables(child, correctedRelativeQName);
						}
						else
						{
							gIdentifiable = child;
						}
						if (gIdentifiable != null)
						{
							result.add(gIdentifiable);
						}
					}
				}
			}
		}
		else
		{
			result = getMergedGIdentifiablesWithRelativeName(parent, relativeQName);
		}
		return result;
	}

	/**
	 * Returns all merged model objects with the specified relative name, within the given <code>parent</code>. The
	 * <code>parent</code> is a merged object, too.
	 *
	 * @param parent
	 *        the object to search within
	 * @param relativeName
	 *        the relative name to search for
	 * @return list of identified merged objects
	 */
	private List<GIdentifiable> getMergedGIdentifiablesWithRelativeName(GIdentifiable parent, String relativeName)
	{
		GIdentifiable mergedObj = null;
		List<GIdentifiable> gIdentList = new ArrayList<GIdentifiable>();
		Collection<GIdentifiable> concreteInstances = SplitUtils.getConcreteInstances(parent);
		for (GIdentifiable instance: concreteInstances)
		{
			GIdentifiable identifiable = MetaModelUtils.getRelativeIdentifiables(instance, relativeName);
			if (identifiable != null)
			{
				mergedObj = SplitUtils.getMergedInstance(identifiable);

				if (!gIdentList.contains(mergedObj))
				{
					gIdentList.add(mergedObj);
				}
			}
		}
		return gIdentList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getNativeAUTOSARRelease(int)
	 */
	public Version getNativeAUTOSARRelease(int majorReleaseNumber)
	{
		AutosarReleaseDescriptor descriptor = getAUTOSARReleaseDescriptor(majorReleaseNumber);
		AutosarMetaModelVersionData data = descriptor.getAutosarVersionData();
		return new Version(data.getMajor(), data.getMinor(), data.getRevision());
	}

	/**
	 * @param majorReleaseNumber
	 * @return Autosar release descriptor
	 */
	public AutosarReleaseDescriptor getAUTOSARReleaseDescriptor(int majorReleaseNumber)
	{
		String pattern;
		switch (majorReleaseNumber)
		{
			case 2:
				pattern = "org.artop.aal.autosar21"; //$NON-NLS-1$
				break;
			case 3:
				pattern = "org.artop.aal.autosar3x"; //$NON-NLS-1$
				break;
			case 4:
				pattern = "org.artop.aal.autosar40"; //$NON-NLS-1$
				break;
			default:
				throw new IllegalArgumentException(
					"Unknown release number, only 2, 3 and 4 suppported:" + majorReleaseNumber); //$NON-NLS-1$
		}
		List<IMetaModelDescriptor> descriptors = MetaModelDescriptorRegistry.INSTANCE.getDescriptors(pattern);
		if (descriptors.size() != 1)
		{
			throw new InternalError("Expected one descriptor for the provided release (" //$NON-NLS-1$
				+ majorReleaseNumber + "), got " + descriptors.size()); //$NON-NLS-1$
		}
		IMetaModelDescriptor descr = descriptors.get(0);
		if (!(descr instanceof AutosarReleaseDescriptor))
		{
			throw new InternalError("Descriptor not instance of AutosarReleaseDescriptor"); //$NON-NLS-1$
		}
		return (AutosarReleaseDescriptor) descr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ModelUtils.Service#getModelDependencyLookup(org.eclipse.core.resources.IProject)
	 */
	@Override
	public IModelDependencyLookup getModelDependencyLookup(IProject project)
	{
		return MMSRegistry.INSTANCE.getMMService(project).getModelDependencyLookup();
	}

}
