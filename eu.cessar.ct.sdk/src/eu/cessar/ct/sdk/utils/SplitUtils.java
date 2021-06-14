/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 19, 2012 2:48:08 PM </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

/**
 * <p>
 * Utility class that deals with all aspects of a project's merged model.
 * <p/>
 * A merged model is made from all AUTOSAR objects that exist inside an AUTOSAR project. The merge is created by the
 * splitting rules as defined inside the <strong>AUTOSAR_InteroperabilityOfAutosarTools.pdf</strong>, requirement
 * <strong>[ATI0042] Authoring tool SHALL support for merging of AUTOSAR models</strong>. <br/>
 * <br/>
 * All APIs are related to the work with AUTOSAR model (<strong>not</strong> Presentation Model)
 *
 */
@Requirement(
	reqID = "REQ_API_SPLIT#1")
public final class SplitUtils
{
	private static final IService SERVICE = PlatformUtils.getService(IService.class);

	private SplitUtils()
	{
		// hide constructor
	}

	/**
	 * @author uidl6870
	 *
	 *         %created_by: uidu2337 %
	 *
	 *         %date_created: Tue Mar 18 13:03:26 2014 %
	 *
	 *         %version: 6 %
	 */
	public static interface IService
	{

		@SuppressWarnings("javadoc")
		public GAUTOSAR getMergedModelInstance(IProject project);

		@SuppressWarnings("javadoc")
		public boolean isMergedInstace(GARObject obj);

		@SuppressWarnings("javadoc")
		public <T extends GARObject> T getMergedInstance(T arObject);

		@SuppressWarnings("javadoc")
		public <T extends GARObject> Collection<T> getConcreteInstances(T mergedInstance);

		@SuppressWarnings("javadoc")
		public Collection<Resource> getInstanceResources(GARObject mergedInstance);

		@SuppressWarnings("javadoc")
		public <T extends GARObject> T getActiveInstance(T mergedInstance);

		@SuppressWarnings("javadoc")
		public Resource getActiveResource(GARObject mergedInstance);

		@SuppressWarnings("javadoc")
		public <T extends GARObject> void setActiveInstance(T mergedInstance, T activeInstance);

		@SuppressWarnings("javadoc")
		public void setActiveResource(GARObject mergedInstance, Resource activeResource);

		@SuppressWarnings("javadoc")
		public GARObject splitObject(GARObject arObject, Resource resource) throws SplitableException;

		@SuppressWarnings("javadoc")
		public boolean canSplit(GARObject arObject);
	}

	/**
	 * Return the root of the merged model of a project.
	 *
	 * @param project
	 *        the AUTOSAR project
	 * @return the root of the merged model of the specified <code>project</code> or <code>null</code> if there are no
	 *         AUTOSAR resources defined inside the project. The returned model is read-write.
	 */
	public static GAUTOSAR getMergedModelInstance(IProject project)
	{
		return SERVICE.getMergedModelInstance(project);
	}

	/**
	 * Return <code>true</code> if the provided {@link GARObject} is a merged object, <code>false</code> if it is a
	 * concrete object stored inside a physical file.
	 *
	 * @param obj
	 *        the {@link GARObject} to check
	 * @return <code>true</code> if is a merged object, <code>false</code> otherwise
	 */
	public static boolean isMergedInstace(GARObject obj)
	{
		return SERVICE.isMergedInstace(obj);
	}

	/**
	 * Return the merged object of a concrete object. If the provided parameter is already the merged object, it will be
	 * simply returned.
	 * <p/>
	 * Uniqueness notes:
	 * <ul>
	 * <li>Calling multiple time this method with the same parameter will return the same object.</li>
	 * <li>Calling this method with different objects that are instances of the same merged object will return the same
	 * merged object.</li>
	 * </ul>
	 *
	 * @param arObject
	 *        a concrete object whose merged object need to be returned.
	 * @return the merged object or the provided object if is already a merged object
	 */
	public static <T extends GARObject> T getMergedInstance(T arObject)
	{
		return SERVICE.getMergedInstance(arObject);
	}

	/**
	 * Return the concrete instances of a merged object. <br>
	 * If the provided object is not a merged object as per {@link #isMergedInstace(GARObject)}, an
	 * {@link IllegalArgumentException} will be thrown.
	 *
	 * @param mergedInstance
	 *        the merged instance
	 * @return the collection of the concrete instances in no particular order. The collection will contain at least one
	 *         element
	 */
	public static <T extends GARObject> Collection<T> getConcreteInstances(T mergedInstance)
	{
		return SERVICE.getConcreteInstances(mergedInstance);
	}

	/**
	 * Return the concrete {@linkplain Resource EMF resources} where the <code>mergedInstance</code> is stored. The
	 * method is equivalent with getting the {@linkplain #getConcreteInstances(GARObject) concrete instances} and
	 * retrieving the resource for each returned instance. <br>
	 * If the provided object is not a merged object as per {@link #isMergedInstace(GARObject)}, an
	 * {@link IllegalArgumentException} will be thrown.
	 *
	 * @param mergedInstance
	 *        the merged object
	 * @return the collection of the resources where the concrete instances are stored in no particular order. The
	 *         collection will contain at least one resource
	 */
	public static Collection<Resource> getInstanceResources(GARObject mergedInstance)
	{
		return SERVICE.getInstanceResources(mergedInstance);
	}

	/**
	 * Return the active instance of a merged object. The active instance is one of the concrete instances of the merged
	 * object that will collect any children added to the mergedObject. Every mergedObject has such an active instance. <br>
	 * If the provided object is not a merged object as per {@link #isMergedInstace(GARObject)}, an
	 * {@link IllegalArgumentException} will be thrown.
	 *
	 * @param mergedInstance
	 *        the merged instance
	 * @return the active instance.
	 */
	public static <T extends GARObject> T getActiveInstance(T mergedInstance)
	{
		return SERVICE.getActiveInstance(mergedInstance);
	}

	/**
	 * Return the active resource of a merged object. The active resource is the resource where the active instance of
	 * the merged object is stored. Every mergedObject has such an active instance. <br>
	 * If the provided object is not a merged object as per {@link #isMergedInstace(GARObject)}, an
	 * {@link IllegalArgumentException} will be thrown.
	 *
	 * @param mergedInstance
	 *        the merged instance
	 * @return the resource of the active instance.
	 */
	public static Resource getActiveResource(GARObject mergedInstance)
	{
		return SERVICE.getActiveResource(mergedInstance);
	}

	/**
	 * Change the active instance of the <code>mergedInstance</code> to the provided one. <br>
	 * If the provided <code>mergedInstance</code> is not a merged object as per {@link #isMergedInstace(GARObject)}, or
	 * the <code>activeInstance</code> is not one of the concrete instances of the <code>mergedInstance</code> as per
	 * {@link #getConcreteInstances(GARObject)}, an {@link IllegalArgumentException} will be thrown. <br/>
	 * <br/>
	 * <strong>Note: Presentation Model API is not influenced by this.</strong>
	 *
	 * @param mergedInstance
	 *        the merged object of which the active instance is to be changed
	 * @param activeInstance
	 *        the new active instance
	 */
	public static <T extends GARObject> void setActiveInstance(T mergedInstance, T activeInstance)
	{
		SERVICE.setActiveInstance(mergedInstance, activeInstance);
	}

	/**
	 * Change the active resource of the <code>mergedInstance</code> to the provided one. <br>
	 * If the provided <code>mergedInstance</code> is not a merged object as per {@link #isMergedInstace(GARObject)}, or
	 * the <code>activeResource</code> is not one of the instance resources of the <code>mergedInstance</code> as per
	 * {@link #getInstanceResources(GARObject)}, an {@link IllegalArgumentException} will be thrown. <br/>
	 * <br/>
	 *
	 * <strong>Note: Presentation Model API is not influenced by this.</strong>
	 *
	 * @param mergedInstance
	 *        the merged object of which the active instance is to be changed
	 * @param activeResource
	 *        the new active resource
	 */
	public static void setActiveResource(GARObject mergedInstance, Resource activeResource)
	{
		SERVICE.setActiveResource(mergedInstance, activeResource);
	}

	/**
	 * Indicates whether a {@link GARObject} can be split in multiple resources.
	 *
	 * @param arObject
	 *        a concrete object whose splitability is checked.
	 * @return true <b>iff</b> the arObject is splitable
	 *
	 */
	public static boolean canSplit(GARObject arObject)
	{
		return SERVICE.canSplit(arObject);
	}

	/**
	 * Splits an existing object into a new resource, creating all necessary parents.<br>
	 *
	 * @param arObject
	 *        the object to be split
	 * @param resource
	 *        resource where the object will be created
	 *
	 * @return the newly created arObject, never {@code null}.<br>
	 *         The returned object will not be a merged instance, but the concrete file-based object.
	 * @throws SplitableException
	 */
	public static GARObject splitObject(GARObject arObject, Resource resource) throws SplitableException
	{
		return SERVICE.splitObject(arObject, resource);
	}

	/**
	 * Indicates whether the passed {@code GARObject} is split in multiple files. <br>
	 * The method does not discriminate between valid and invalid splits, and there are no guarantees about the return
	 * value for invalid configurations (as this may change as ARTOP changes).
	 *
	 * @see ValidationUtils
	 *
	 * @param arObject
	 *        the object to be tested
	 * @return true <b>iff</b> {@code arObject} is split in multiple files.
	 */
	public static boolean isSplit(GARObject arObject)
	{
		GARObject mergedInstance = getMergedInstance(arObject);
		return getConcreteInstances(mergedInstance).size() > 1;
	}

}
