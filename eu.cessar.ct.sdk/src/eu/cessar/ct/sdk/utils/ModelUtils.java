/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 14, 2009 4:56:45 PM </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.osgi.framework.Version;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Various static methods that deal with {@linkplain EObject EObject}. <br>
 * NOTE: not all methods can handle objects from all model types defined in CESSAR, i.e.:
 * <ul>
 * <li>File based AUTOSAR models</li>
 * <li>Project based merged AUTOSAR model</li>
 * <li>Presentation model</li>.
 * </ul>
 * It is noted in each method that handles {@linkplain EObject EObject}s what model types it supports.
 */
public final class ModelUtils
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	private ModelUtils()
	{
		// avoid instance
	}

	/**
	 * INTERNAL API, DO NOT USE
	 */
	public static interface Service
	{

		@SuppressWarnings("javadoc")
		public String getAbsoluteQualifiedName(EObject object);

		@SuppressWarnings("javadoc")
		public Collection<Resource> getDirtyResources(IProject project);

		@SuppressWarnings("javadoc")
		public void saveResources(Collection<Resource> resources, IProgressMonitor monitor);

		@SuppressWarnings("javadoc")
		public EObject getEObjectWithQualifiedName(IProject project, String qualifiedName);

		@SuppressWarnings("javadoc")
		public List<EObject> getEObjectsWithQualifiedName(IProject project, String qualifiedName);

		@SuppressWarnings("javadoc")
		public <T> EList<T> getFilteredList(Class<T> subType, EList<? super T> ownerList);

		@SuppressWarnings("javadoc")
		public GAUTOSAR createAutosarFile(IFile autosarFile, boolean forceCreate, IProgressMonitor monitor);

		@SuppressWarnings("javadoc")
		public Collection<IFile> getAutosarFiles(IProject project);

		@SuppressWarnings("javadoc")
		public Collection<Resource> getAutosarResources(IProject project);

		@SuppressWarnings("javadoc")
		public List<EObject> getInstancesOfType(IProject project, EClass clazz, boolean exactMatch);

		@SuppressWarnings("javadoc")
		public List<EObject> getInstancesOfType(EObject context, EClass clazz, boolean exactMatch);

		@SuppressWarnings("javadoc")
		public IFile getDefiningFile(EObject eObject);

		@SuppressWarnings("javadoc")
		public IFile getDefiningFile(Resource resource);

		@SuppressWarnings("javadoc")
		public GIdentifiable getEObjectWithQualifiedName(Resource resource, String qualifiedName);

		@SuppressWarnings("javadoc")
		public List<GIdentifiable> getEObjectsWithQualifiedName(Resource resource, String qualifiedName);

		@SuppressWarnings("javadoc")
		public GIdentifiable getGIdentifiableWithRelativeName(GIdentifiable current, String relativeName);

		@SuppressWarnings("javadoc")
		public List<GIdentifiable> getGIdentifiablesWithRelativeName(GIdentifiable current, String relativeName);

		@SuppressWarnings("javadoc")
		public Resource getDefinedResource(IFile autosarFile);

		@SuppressWarnings("javadoc")
		public Map<EObject, List<EReference>> getReferencedBy(EObject source);

		@SuppressWarnings("javadoc")
		public GARPackage getOrCreateARPackage(GAUTOSAR base, String arPackagePath);

		@SuppressWarnings("javadoc")
		public Version getNativeAUTOSARRelease(int majorReleaseNumber);

		@SuppressWarnings("javadoc")
		public IModelDependencyLookup getModelDependencyLookup(IProject project);

		@SuppressWarnings("javadoc")
		public GIdentifiable getEObjectWithQualifiedName(IProject project, String qualifiedName, boolean merged);

		@SuppressWarnings("javadoc")
		public List<GIdentifiable> getEObjectsWithQualifiedName(IProject project, String qualifiedName, boolean merged);

		@SuppressWarnings("javadoc")
		public List<EObject> getInstancesOfType(IProject project, EClass clazz, boolean exactMatch, boolean merged);

		@SuppressWarnings("javadoc")
		public List<GIdentifiable> getEObjectsWithQualifiedName(EObject current, String qualifiedName);

		@SuppressWarnings("javadoc")
		public GIdentifiable getEObjectWithQualifiedName(EObject current, String qualifiedName);

	}

	/**
	 * Returns the absolute qualified name of the given Identifiable or ARObject. It starts with a "/" separated
	 * concatenation of the shortNames of the Identifiable(s) which contain the Identifiable or ARObject in question. It
	 * ends with the shortName of the given object in case that this one is an Identifiable, e.g.
	 * <code>/topLevelPackageAShortName/subPackageBShortName/moduleDefCShortName</code> . Otherwise, i.e. if the given
	 * object is an ARObject, the absolute qualified name ends with a "@containingFeatureName(.listPosition)?" formatted
	 * identifier of that ARObject, e.g. <code>/topLevelPackageAShortName/containerBShortName/@parameterValues.5</code>
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 *
	 * <br>
	 * </ul>
	 * <strong>Note:</strong> If the passed argument <code>object</code> that does not match the acceptance criteria,
	 * the behavior of the method is undefined.
	 * </ul>
	 *
	 * @param object
	 *        The Identifiable or ARObject whose absolute qualified name is to be returned.
	 * @return The given Identifiable or ARObject's absolute qualified name or an empty string if no such could be
	 *         established.
	 */
	public static String getAbsoluteQualifiedName(EObject object)
	{
		return SERVICE.getAbsoluteQualifiedName(object);
	}

	/**
	 * Return the resources that need saving for the current project
	 *
	 * @param project
	 * @return a collection of resources that need saving, never null
	 */
	public static Collection<Resource> getDirtyResources(IProject project)
	{
		return SERVICE.getDirtyResources(project);
	}

	/**
	 * Save all resources.
	 *
	 * @param resources
	 * @param monitor
	 */
	public static void saveResources(Collection<Resource> resources, IProgressMonitor monitor)
	{
		SERVICE.saveResources(resources, monitor);
	}

	/**
	 * Returns the defining file of a given AUTOSAR EObject.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed argument <code>eObject</code> is a merged object then the defining file of
	 * the active resource is returned.
	 * </ul>
	 * Same as {@link SplitUtils#getActiveResource(gautosar.ggenericstructure.ginfrastructure.GARObject)}
	 *
	 * @param eObject
	 *        the given object
	 * @return the corresponding file, or <br>
	 *         <code>null</code>, if the file cannot be retrieved
	 */
	public static IFile getDefiningFile(EObject eObject)
	{
		return SERVICE.getDefiningFile(eObject);
	}

	/**
	 * Return the file where the <code>resource</code> is stored
	 *
	 * @param resource
	 *        An EMF Resource
	 * @return the corresponding IFile or <code>null</code> if the file cannot be retrieved
	 */
	public static IFile getDefiningFile(Resource resource)
	{
		return SERVICE.getDefiningFile(resource);
	}

	/**
	 * Return the resource corresponding to the given <code>file</code>
	 *
	 * @param file
	 * @return the corresponding Resource or <code>null</code> if the file cannot be retrieved
	 */
	public static Resource getDefinedResource(IFile file)
	{
		return SERVICE.getDefinedResource(file);
	}

	/**
	 * Returns a single model object with the specified fully qualified name, within the given project.<br/>
	 * For historical reasons the result of the method is an EObject, but the object can be cast to an GIdentifiable.
	 * <br/>
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed argument <code>qualifiedName</code> does not match the acceptance criteria,
	 * the behavior of the method is undefined.
	 * </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param qualifiedName
	 *        the fully qualified name of the searched object
	 * @return the object if it is found, or <br>
	 *         <code>null</code>, if no such object is found or more then one object is found
	 */
	public static EObject getEObjectWithQualifiedName(IProject project, String qualifiedName)
	{
		return SERVICE.getEObjectWithQualifiedName(project, qualifiedName);
	}

	/**
	 * Returns a single model object with the specified fully qualified name, within the given project.<br/>
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param qualifiedName
	 *        the fully qualified name of the searched object
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the object if it is found, or <br>
	 *         <code>null</code>, if no such object is found or more then one object (different types) is found
	 */
	public static GIdentifiable getEObjectWithQualifiedName(IProject project, String qualifiedName, boolean merged)
	{
		return SERVICE.getEObjectWithQualifiedName(project, qualifiedName, merged);
	}

	/**
	 * Returns a single model object with the specified fully qualified name, contained in the same resource as
	 * <code>current</code> when <code>current</code> is a file-based object<br>
	 * or a merged object when <code>current</code> is also a merged object
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong>Yes. In this case, the returned object is also from the merged model.</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the arguments do not match the acceptance criteria, the behavior of the method is
	 * undefined.
	 * </ul>
	 *
	 * @param current
	 *        the current object to start the search at
	 * @param qualifiedName
	 *        the fully qualified name of the searched object
	 * @return the object if it is found, or <br>
	 *         <code>null</code> if no such object is found or more then one object is found
	 */
	public static GIdentifiable getEObjectWithQualifiedName(EObject current, String qualifiedName)
	{
		return SERVICE.getEObjectWithQualifiedName(current, qualifiedName);
	}

	/**
	 * Returns a single model object with the specified fully qualified name, contained in the given resource
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed <code>qualifiedName</code> does not match the acceptance criteria , the
	 * behavior of the method is undefined.
	 * </ul>
	 *
	 * @param resource
	 *        the EMF resource where to search for the object
	 * @param qualifiedName
	 *        the relative name of the searched objects
	 * @return the object if it is found, or <br>
	 *         <code>null</code> if no such object is found or more then one object is found
	 */
	public static GIdentifiable getEObjectWithQualifiedName(Resource resource, String qualifiedName)
	{
		return SERVICE.getEObjectWithQualifiedName(resource, qualifiedName);
	}

	/**
	 * Returns a single model object with the specified relative name, within the given current object.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes. In this case, the returned object is also from the merged model.</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed arguments do not match the acceptance criteria, <code>null</code> is
	 * returned
	 * </ul>
	 *
	 * @param current
	 *        the current object to start the search at
	 * @param relativeName
	 *        the relative name of the searched object
	 * @return the object if it is found, or <br>
	 *         <code>null</code>, if no such object is found or more then one object is found
	 */
	public static GIdentifiable getEObjectWithRelativeName(GIdentifiable current, String relativeName)
	{
		return SERVICE.getGIdentifiableWithRelativeName(current, relativeName);
	}

	/**
	 * Returns all the model objects with the specified fully qualified name, within the given project.<br/>
	 * For historical reasons the result of the method is a list of EObject, but each of the lists elements can be cast
	 * to an GIdentifiable. <br/>
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed argument <code>qualifiedName</code> does not match the acceptance criteria,
	 * the behavior of the method is undefined.
	 * </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param qualifiedName
	 *        the fully qualified name of the searched objects
	 * @return a list with found objects,or an empty one if no such object is found
	 */
	public static List<EObject> getEObjectsWithQualifiedName(IProject project, String qualifiedName)
	{
		return SERVICE.getEObjectsWithQualifiedName(project, qualifiedName);
	}

	/**
	 * Returns all the model objects with the specified fully qualified name, within the given project.<br/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed argument <code>qualifiedName</code> does not match the acceptance criteria
	 * an empty list is returned. If multiple object types with the same <code>qualifiedName</code> exist, then the list
	 * will contain multiple merged objects.
	 *
	 * @param project
	 *        the project where to search
	 * @param qualifiedName
	 *        the fully qualified name of the searched objects
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return a list with found objects,or an empty one if no such object is found
	 */
	public static List<GIdentifiable> getEObjectsWithQualifiedName(IProject project, String qualifiedName,
		boolean merged)
	{
		return SERVICE.getEObjectsWithQualifiedName(project, qualifiedName, merged);
	}

	/**
	 * Returns all the model objects with the specified fully qualified name, contained in the same resource as the
	 * <code>current</code> object when <code>current</code> is a file-based object<br>
	 * or all merged objects when <code>current</code> is also a merged object.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes. In this case, the returned objects are also from the merged model.</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed arguments do not match the acceptance criteria, the behavior of the method
	 * is undefined.
	 * </ul>
	 *
	 * @param current
	 *        the current object to start the search at
	 * @param qualifiedName
	 *        the fully qualified name of the searched objects
	 * @return a list with found objects, or an empty one if no such object is found
	 */
	public static List<GIdentifiable> getEObjectsWithQualifiedName(EObject current, String qualifiedName)
	{
		return SERVICE.getEObjectsWithQualifiedName(current, qualifiedName);
	}

	/**
	 * Returns all the model objects with the specified fully qualified name, contained in the given resource
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed <code>qualifiedName</code> does not match the acceptance criteria, the
	 * behavior of the method is undefined.
	 * </ul>
	 *
	 * @param resource
	 *        the EMF resource where to search for the objects
	 * @param qualifiedName
	 *        the fully qualified name of the searched objects
	 * @return a list with the found objects, or an empty one if no such object is found
	 */
	public static List<GIdentifiable> getEObjectsWithQualifiedName(Resource resource, String qualifiedName)
	{
		return SERVICE.getEObjectsWithQualifiedName(resource, qualifiedName);
	}

	/**
	 * Returns all the model objects with the specified relative name,within the given current object.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes. In this case, the returned object is also from the merged model.</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed GIdentifiable <code>current</code> does not match the acceptance criteria,
	 * empty list is returned.
	 * </ul>
	 *
	 * @param current
	 *        the current object to start the search at
	 * @param relativeName
	 *        the relative name of the searched objects
	 * @return a list with found objects, or an empty one if no such object is found. <br>
	 */
	public static List<GIdentifiable> getEObjectsWithRelativeName(GIdentifiable current, String relativeName)
	{
		return SERVICE.getGIdentifiablesWithRelativeName(current, relativeName);
	}

	/**
	 * Returns a mapping of (EObject,list of references) that refer the given object.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes. If the provided object is a merged object, also the returned values will
	 * be from the merged model.</li>
	 * <li><strong>Presentation Model object:</strong> No. For PM use the method
	 * {@link PMUtils#getReferencedBy(eu.cessar.ct.sdk.pm.IPMContainer)}</li>
	 * </ul>
	 * <strong>Note:</strong> If the passed EObject <code>source</code> does not match the acceptance criteria, the
	 * behavior of the method is undefined.
	 * </ul>
	 *
	 * @param source
	 *        the referenced object
	 * @return the corresponding map, or an empty one if no reference is found.
	 */
	public static Map<EObject, List<EReference>> getReferencedBy(EObject source)
	{
		return SERVICE.getReferencedBy(source);
	}

	/**
	 *
	 * Returns all instances of a given class within the given root.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> Yes. If the provided object is a merged one, the returned values will be also
	 * from the merged model.</li>
	 * <li><strong>Presentation Model object:</strong> Yes</li>
	 *
	 * @param root
	 *        the node to start the search at
	 * @param clazz
	 *        the class to locate instances of
	 * @param exactMatch
	 *        if false objects that are instances of subclasses of clazz will be returned if found
	 * @return a collection of all found instances or an empty one, if <code> clazz</code> is null or no instance is
	 *         found.
	 */
	public static List<EObject> getInstancesOfType(EObject root, EClass clazz, boolean exactMatch)
	{
		return SERVICE.getInstancesOfType(root, clazz, exactMatch);
	}

	/**
	 * Returns all instances of a given class within the given project.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> Yes</li>
	 * </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param clazz
	 *        the class to locate instances of
	 * @param exactMatch
	 *        if false objects that are instances of subclasses of <code>clazz</code> will be returned if found
	 * @return a collection of all found instances or an empty one, if <code> clazz</code> is null or no instance is
	 *         found.
	 */
	public static List<EObject> getInstancesOfType(IProject project, EClass clazz, boolean exactMatch)
	{
		return SERVICE.getInstancesOfType(project, clazz, exactMatch);
	}

	/**
	 * Returns all instances of a given class within the given project.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> Yes</li>
	 * </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param clazz
	 *        the class to locate instances of
	 * @param exactMatch
	 *        if false objects that are instances of subclasses of <code>clazz</code> will be returned if found
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return a collection of all found instances or an empty one, if <code> clazz</code> is null or no instance is
	 *         found.
	 */
	public static List<EObject> getInstancesOfType(IProject project, EClass clazz, boolean exactMatch, boolean merged)
	{
		return SERVICE.getInstancesOfType(project, clazz, exactMatch, merged);
	}

	/**
	 * Locates or creates a package from a given path and base.
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No.</li>
	 * <li><strong>Presentation Model object:</strong> No</li>
	 * </ul>
	 * <strong>Note:</strong> If the GAUTOSAR is the merged instance of the project, an IllegalArgumentException will be
	 * thrown.
	 *
	 *
	 * @param base
	 *        the <code>GAUTOSAR</code> base object
	 * @param arPackagePath
	 *        the path of the package
	 * @return the located or created package,or <br>
	 *         <code>null</code>, if it cannot be located or created.
	 * @throws throws
	 *         an IllegalArgumentException if called with a merged object argument
	 */
	public static GARPackage getOrCreateARPackage(GAUTOSAR base, String arPackagePath)
	{
		return SERVICE.getOrCreateARPackage(base, arPackagePath);

	}

	/**
	 * Return a modifiable list of elements of type <code>subType</code> from the list <code>ownerList</code>.<br/>
	 * Usage example:
	 *
	 * <pre>
	 * GARPackage arPack = ...
	 * EList&lt;GModuleConfiguration&gt; modules =
	 * 	ModelUtils.getFilteredList(arPack.gGetElements(), GModuleConfiguration.class);
	 * </pre>
	 *
	 * <p/>
	 * <strong>Object types acceptance:</strong>
	 * <ul>
	 * <li><strong>File based object:</strong> Yes</li>
	 * <li><strong>Merged object:</strong> No</li>
	 * <li><strong>Presentation Model object:</strong> Yes</li>
	 *
	 * @param <T>
	 * @param subType
	 * @param ownerList
	 * @return the filtered list
	 */
	public static <T> EList<T> getFilteredList(Class<T> subType, EList<? super T> ownerList)
	{
		return SERVICE.getFilteredList(subType, ownerList);
	}

	/**
	 * Creates a new AUTOSAR file.
	 *
	 * @param autosarFile
	 *        the file to be created. It shall have one of the extensions accepted by running CESSAR instance. A list
	 *        can be seen in the content types preferences.
	 * @param forceCreate
	 *        if <code>true</code> and the file already exists, it will be overwritten by the new created one if
	 *        <code>false</code> and the file already exists, the existing one will be returned
	 * @param monitor
	 *        a progress monitor to use, could be null
	 * @return the new created file, always from the file-based model, or <br>
	 *         <code>null</code>, if it cannot be retrieved or created
	 */
	public static GAUTOSAR createAutosarFile(IFile autosarFile, boolean forceCreate, IProgressMonitor monitor)
	{
		return SERVICE.createAutosarFile(autosarFile, forceCreate, monitor);
	}

	/**
	 * Locates the AUTOSAR related files (*.autosar, *.ecuconfig ...) from a given project in no particular order.
	 *
	 * @param project
	 *        the project where the files are located
	 * @return a collection with the located files, or an empty one if no file is found
	 */
	public static Collection<IFile> getAutosarFiles(IProject project)
	{
		return SERVICE.getAutosarFiles(project);
	}

	/**
	 * Return all AUTOSAR related EMF resources from a given project in no particular order.
	 *
	 * @param project
	 *        the project where the files are located
	 * @return a collection with the located resources, never null
	 */
	public static Collection<Resource> getAutosarResources(IProject project)
	{
		return SERVICE.getAutosarResources(project);
	}

	/**
	 * Return the major, minor and micro version of the supported native AUTOSAR releases. If the provided
	 * <code>majorReleaseNumber</code> is not one of the supported version an {@link IllegalArgumentException} will be
	 * thrown.
	 *
	 * @param majorReleaseNumber
	 *        one of the supported major releases numbers 2, 3 or 4
	 * @return the version of the native AUTOSAR releases. Only the major, minor and micro fields will be filled.
	 */
	public static Version getNativeAUTOSARRelease(int majorReleaseNumber)
	{
		return SERVICE.getNativeAUTOSARRelease(majorReleaseNumber);
	}

	/**
	 * Return the {@code IModelDependencyLookup} interface associated with this project.
	 *
	 * The interface can be used to perform a variety of model-aware searches.
	 *
	 * @see IModelDependencyLookup
	 *
	 * @param project
	 *        the project
	 * @return the {@code IModelDependencyLookup} interface
	 */
	public static IModelDependencyLookup getModelDependencyLookup(IProject project)
	{
		return SERVICE.getModelDependencyLookup(project);
	}
}
