/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jun 16, 2010 5:12:38 PM </copyright>
 */
package eu.cessar.ct.core.mms;

import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.AbstractTreeIterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sphinx.emf.util.EObjectUtil;

/**
 * This class contain various static methods that can be used to search for EObjects in a model
 * 
 * @Review uidl6458 - 29.03.2012
 * 
 */
public final class EObjectLookupUtils
{

	/**
	 * 
	 */
	private static final String SUB_PACKAGES_FEATURE_NAME = "subPackages"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String AR_PACKAGES_FEATURE_NAME = "arPackages"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String PACKAGES_FEATURE_NAME = "topLevelPackages"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final int INVALID_VALUE = Integer.MIN_VALUE;

	private EObjectLookupUtils()
	{
		// avoid instance
	}

	/**
	 * @param contextObject
	 * @return
	 */
	private static TreeIterator<EObject> getAllContentsIncludingRoot(EObject contextObject)
	{
		TreeIterator<EObject> allContents = new AbstractTreeIterator<EObject>(contextObject, true)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<EObject> getChildren(Object object)
			{
				return ((EObject) object).eContents().iterator();
			}
		};
		return allContents;
	}

	/**
	 * Return all objects that are of a particular class from an project
	 * 
	 * @param project
	 *        the project to search into
	 * @param clazz
	 *        the EClass that all objects should be instances of
	 * @param exactMatch
	 *        if false the returned objects could be also instances of subclasses
	 * @return a list with all objects found in no particular order, never null
	 */
	public static List<EObject> getInstancesOfType(IProject project, EClass clazz, boolean exactMatch)
	{
		Collection<Resource> resources = EResourceUtils.getProjectResources(project);
		List<EObject> result = new ArrayList<EObject>();
		for (Resource resource: resources)
		{
			result.addAll(getInstancesOfType(resource, clazz, exactMatch));
		}
		return result;
	}

	/**
	 * Return all objects that are of a particular class from an EMF Resource
	 * 
	 * @param resource
	 *        the resource to search into
	 * @param clazz
	 *        the EClass that all objects should be instances of
	 * @param exactMatch
	 *        if false the returned objects could be also instances of subclasses
	 * @return a list with all objects found in no particular order, never null
	 */
	public static List<EObject> getInstancesOfType(Resource resource, EClass clazz, boolean exactMatch)
	{
		List<EObject> result = new ArrayList<EObject>();
		for (EObject obj: resource.getContents())
		{
			result.addAll(getInstancesOfType(obj, clazz, exactMatch));
		}
		return result;
	}

	/**
	 * Return all objects that are of a particular class and are direct or indirect children of a particular EObject
	 * 
	 * @param context
	 *        the owner object where to search into
	 * @param clazz
	 *        the EClass that all objects should be instances of
	 * @param exactMatch
	 *        if false the returned objects could be also instances of subclasses
	 * @return a list with all objects found in no particular order, never null
	 */
	public static List<EObject> getInstancesOfType(EObject context, EClass clazz, boolean exactMatch)
	{
		List<EObject> result = new ArrayList<EObject>();
		TreeIterator<EObject> it = getAllContentsIncludingRoot(context);
		while (it.hasNext())
		{
			EObject candidate = it.next();
			if (isInstance(candidate, clazz, exactMatch))
			{
				result.add(candidate);
			}
		}
		return result;
	}

	/**
	 * Return true if the candidate is an instanceof of <code>clazz</code> or not.
	 * 
	 * @param candidate
	 *        the object to check
	 * @param clazz
	 *        the {@link EClass} to match
	 * @param exactMatch
	 *        if true the {@link EObject#eClass()} need to be equal with the clazz otherwise it clazz can be a
	 *        superclass of {@link EObject#eClass()}
	 * @return true if is a match or false if not or null
	 */
	public static boolean isInstance(EObject candidate, EClass clazz, boolean exactMatch)
	{
		if (candidate == null)
		{
			return false;
		}
		else
		{
			if (!exactMatch)
			{
				return clazz.isInstance(candidate);
			}
			else
			{
				return candidate.eClass() == clazz;
			}

		}
	}

	/**
	 * Return all objects with a particular qualified name from a collection of resources
	 * 
	 * @param resources
	 *        the collection of resource where to search
	 * @param qName
	 *        the qualified name
	 * @return the list of found objects or an empty list.
	 */
	public static List<EObject> getEObjectsWithQName(Collection<Resource> resources, String qName)
	{
		if (resources.isEmpty())
		{
			return Collections.emptyList();
		}
		List<EObject> result = new ArrayList<EObject>();
		for (Resource resource: resources)
		{
			result.addAll(getEObjectsWithQName(resource, qName));
		}
		return result;
	}

	/**
	 * Return all objects with a particular qualified name from a collection of resources, ignoring case considerations.
	 * 
	 * @param resources
	 *        the collection of resource where to search
	 * @param qName
	 *        the qualified name
	 * @return the list of found objects or an empty list.
	 */
	public static List<EObject> getEObjectsWithQNameNoCase(Collection<Resource> resources, String qName)
	{
		if (resources.isEmpty())
		{
			return Collections.emptyList();
		}
		List<EObject> result = new ArrayList<EObject>();
		for (Resource resource: resources)
		{
			result.addAll(getEObjectsWithQNameNoCase(resource, qName));
		}
		return result;
	}

	/**
	 * Return all objects with the given qName from the given resource
	 * 
	 * @param resource
	 *        resource where to search
	 * @param qName
	 *        the qualified name
	 * @return the list of found objects or an empty list.
	 */
	public static List<EObject> getEObjectsWithQName(Resource resource, String qName)
	{
		List<EObject> result = new ArrayList<EObject>();

		if (resource == null || qName == null)
		{
			return result;
		}
		if (!qName.startsWith(MetaModelUtils.QNAME_SEPARATOR_STR))
		{
			return result;
		}

		String fragment = qName.substring(1);
		EList<EObject> contents = resource.getContents();
		if (contents != null && contents.size() > 0)
		{
			EObject autosar = contents.get(0);
			if (autosar instanceof GAUTOSAR)
			{
				EClass eClass = autosar.eClass();
				EStructuralFeature packagesFeature = eClass.getEStructuralFeature(PACKAGES_FEATURE_NAME);
				if (packagesFeature == null)
				{
					// For AUTOSAR 4.0 release
					packagesFeature = eClass.getEStructuralFeature(AR_PACKAGES_FEATURE_NAME);
				}
				EList<GARPackage> topLevelPackages = ((GAUTOSAR) autosar).gGetArPackages();
				for (GARPackage topLevelPackage: topLevelPackages)
				{
					List<EObject> resolvedARObject = resolveAgainstARPackage(topLevelPackage, fragment, false);
					result.addAll(resolvedARObject);
				}

				// Resolve against Identifiables other than top level packages
				// and
				// ARObjects contained by given AUTOSAR object
				List<EObject> resolvedARObject = resolveAgainstARObject(autosar, fragment, 2,
					new EStructuralFeature[] {packagesFeature}, false);

				result.addAll(resolvedARObject);
			}
		}

		return result;
	}

	/**
	 * Return all objects with the given qName from the given resource, ignoring case considerations.
	 * 
	 * @param resource
	 *        resource where to search
	 * @param qName
	 *        the qualified name
	 * @return the list of found objects or an empty list.
	 */
	public static List<EObject> getEObjectsWithQNameNoCase(Resource resource, String qName)
	{
		List<EObject> result = new ArrayList<EObject>();

		if (resource == null || qName == null)
		{
			return result;
		}
		if (!qName.startsWith(MetaModelUtils.QNAME_SEPARATOR_STR))
		{
			return result;
		}

		String fragment = qName.substring(1);
		EList<EObject> contents = resource.getContents();
		if (contents != null && contents.size() > 0)
		{
			EObject autosar = contents.get(0);
			if (autosar instanceof GAUTOSAR)
			{
				EClass eClass = autosar.eClass();
				EStructuralFeature packagesFeature = eClass.getEStructuralFeature(PACKAGES_FEATURE_NAME);
				if (packagesFeature == null)
				{
					// For AUTOSAR 4.0 release
					packagesFeature = autosar.eClass().getEStructuralFeature(AR_PACKAGES_FEATURE_NAME);
				}
				EList<GARPackage> topLevelPackages = ((GAUTOSAR) autosar).gGetArPackages();
				for (GARPackage topLevelPackage: topLevelPackages)
				{
					List<EObject> resolvedARObject = resolveAgainstARPackage(topLevelPackage, fragment, true);
					result.addAll(resolvedARObject);
				}

				// Resolve against Identifiables other than top level packages
				// and
				// ARObjects contained by given AUTOSAR object
				List<EObject> resolvedARObject = resolveAgainstARObject(autosar, fragment, 2,
					new EStructuralFeature[] {packagesFeature}, true);

				result.addAll(resolvedARObject);
			}
		}

		return result;
	}

	/**
	 * Return all objects with a particular qualified name from all resources of the project
	 * 
	 * @param project
	 *        the project where to search
	 * @param qName
	 *        the qualified name
	 * @return the list of found objects or an empty list.
	 */
	public static List<EObject> getEObjectsWithQName(IProject project, String qName)
	{
		return getEObjectsWithQName(EResourceUtils.getProjectResources(project), qName);
	}

	/**
	 * Return all objects with a particular qualified name from all resources of the project, ignoring case
	 * considerations.
	 * 
	 * @param project
	 *        the project where to search
	 * @param qName
	 *        the qualified name
	 * @return the list of found objects or an empty list.
	 */
	public static List<EObject> getEObjectsWithQNameNoCase(IProject project, String qName)
	{
		return getEObjectsWithQNameNoCase(EResourceUtils.getProjectResources(project), qName);
	}

	/**
	 * Returns a list of Identifiable(s) and/or ARObject(s) within or equal to given ARObject which matches given qName
	 * 
	 * @param rootObject
	 *        the object where to start the search at
	 * @param qName
	 *        the qualified name
	 * @return the list of found objects or an empty list.
	 */
	public static List<EObject> getEObjectsWithQName(EObject rootObject, String qName)
	{
		return resolveAgainstARObject(rootObject, qName, EObjectUtil.DEPTH_INFINITE, false);
	}

	/**
	 * Returns a list of Identifiable and/or ARObject within or equal to given ARPackage which matches given URI
	 * fragment
	 * <p>
	 * For the sake of performance optimization, the tree consisting of given ARPackage and its contained Identifiables
	 * and ARObjects is traversed and searched for the given URI fragment according to the probability of occurrence in
	 * descending order as follows:
	 * <ul>
	 * <li>the ARPachage itself</li>
	 * <li>its contained packageable elements</li>
	 * <li>its contained sub packages</li>
	 * </ul>
	 * </p>
	 * 
	 * @param arPackage
	 *        The containing ARPackage.
	 * @param uriFragment
	 *        The URI fragment to be resolved starting from given ARPackage.
	 * @param ignoreCase
	 *        if case should be ignore
	 * @return The matching Identifiable or ARObject if any, or empty list otherwise.
	 */
	public static List<EObject> resolveAgainstARPackage(GARPackage arPackage, String uriFragment, boolean ignoreCase)
	{
		List<EObject> result = new ArrayList<EObject>();

		// Given URI fragment matching given ARPackage?
		String arPackageSegment = getAbsoluteQualifiedNameSegment(arPackage);
		String leadingSegment = getLeadingAbsoluteQualifiedNameSegment(uriFragment);
		if (matchesARObjectSegmentOf(arPackageSegment, leadingSegment, ignoreCase))
		{
			// Resolve against given ARPackage itself

			List<EObject> list = resolveAgainstARObject(arPackage, uriFragment, EObjectUtil.DEPTH_ZERO, ignoreCase);
			if (list.size() > 0)
			{
				return list;
			}
			int nextSeparatorIndex = uriFragment.indexOf(MetaModelUtils.QNAME_SEPARATOR_STR);
			String remainingSegments = uriFragment.substring(nextSeparatorIndex + 1);

			EList<GPackageableElement> elements = arPackage.gGetElements();
			for (GPackageableElement element: elements)
			{
				result.addAll(resolveAgainstARObject(element, remainingSegments, 2, ignoreCase));
			}

			// Resolve against sub packages as well as Identifiables and
			// ARObjects contained by sub packages
			EClass eClass = arPackage.eClass();
			EStructuralFeature aRPackagePackagesFeature = eClass.getEStructuralFeature(SUB_PACKAGES_FEATURE_NAME);
			if (aRPackagePackagesFeature == null)
			{
				aRPackagePackagesFeature = eClass.getEStructuralFeature(AR_PACKAGES_FEATURE_NAME);
			}
			EList<GARPackage> subPackages = arPackage.gGetSubPackages();
			for (GARPackage subPackage: subPackages)
			{
				result.addAll(resolveAgainstARPackage(subPackage, remainingSegments, ignoreCase));

			}
			// Resolve against Identifiables other than packageable elements and
			// sub packages and ARObjects contained by
			// given ARPackage
			// result.addAll(resolveAgainstARObject(arPackage, qName, 2, new
			// EStructuralFeature[] {
			// elementsFeature, aRPackagePackagesFeature}));
		}

		return result;
	}

	/**
	 * Returns a list of Identifiable(s) and/or ARObject(s) within or equal to given ARObject which matches given URI
	 * fragment
	 * 
	 * @param arObject
	 *        The containing ARObject.
	 * @param uriFragment
	 *        The URI fragment to be resolved starting from given ARObject.
	 * @param depth
	 *        The depth of ARObject tree traversal which has to be taken into account for URI fragment resolving
	 *        (EObjectUtil.DEPTH_ZERO, EObjectUtil.DEPTH_ONE, or EObjectUtil.DEPTH_INFINITE).
	 * @param ignoreCase
	 *        whether to ignore case considerations
	 * @return The matching Identifiable or ARObject if any, or an empty list otherwise.
	 */
	public static List<EObject> resolveAgainstARObject(EObject arObject, String uriFragment, int depth,
		boolean ignoreCase)
	{
		return resolveAgainstARObject(arObject, uriFragment, depth, new EStructuralFeature[0], ignoreCase);
	}

	/**
	 * Returns a list of Identifiable(s) or/and ARObject(s) within or equal to given ARObject which matches given URI
	 * fragment
	 * 
	 * @param arObject
	 *        The containing ARObject.
	 * @param qName
	 *        The qualified name of the object to be resolved starting from given ARObject.
	 * @param depth
	 *        The depth of ARObject tree traversal up to which URI fragment resolving has to be carried out
	 *        (EObjectUtil.DEPTH_ZERO, EObjectUtil.DEPTH_ONE, or EObjectUtil.DEPTH_INFINITE).
	 * @param featuresToBeIgnored
	 *        The features of given ARObject to be ignored for URI fragment resolving (only relevant if depth is greater
	 *        than IResource.DEPTH_ZERO).
	 * @param ignoreCase
	 *        if case should be ignored
	 * @return list, never null
	 */
	private static List<EObject> resolveAgainstARObject(EObject arObject, String qName, int depth,
		EStructuralFeature[] featuresToBeIgnored, boolean ignoreCase)
	{
		List<EObject> result = new ArrayList<EObject>();

		// Given URI fragment matching given ARObject?
		String arObjectSegment = getAbsoluteQualifiedNameSegment(arObject);
		String leadingSegment = getLeadingAbsoluteQualifiedNameSegment(qName);
		if (matchesARObjectSegmentOf(arObjectSegment, leadingSegment, ignoreCase))
		{
			// No more segments to deal with, last segment left equal to
			// that of
			// given ARObject, and destination type
			// matching?
			int nextSeparatorIndex = qName.indexOf(MetaModelUtils.QNAME_SEPARATOR_STR);
			// logic extracted to a method to reduce cyclomatic complexity of method
			boolean segmentsEquals = areSegmentsEqual(leadingSegment, arObjectSegment, ignoreCase);

			if (nextSeparatorIndex == -1 && segmentsEquals
				&& EObjectUtil.isAssignableFrom(arObject.eClass(), EObject.class.getName()))
			{
				// Return given ARObject
				result.add(arObject);
			}
			else
			{
				int newDepth = getNewDepth(depth);
				if (newDepth == INVALID_VALUE)
				{
					return result;
				}

				String remainingSegments = ""; //$NON-NLS-1$
				if (!segmentsEquals && arObjectSegment.length() > 1
					&& arObjectSegment.startsWith(MetaModelUtils.NON_IDENTIFIABLE_SEGMENT_PREFIX))
				{
					// got here because the arObjectSegment only starts with @ (does not contain only @, like the root
					// AUTOSAR object)
					// remainingSegments stays the same, in order to skip that arObject that was not an identifiable
					remainingSegments = qName;
				}
				else
				{

					remainingSegments = qName.substring(nextSeparatorIndex + 1);
				}
				List<EStructuralFeature> featuresToBeIgnoredList = Arrays.asList(featuresToBeIgnored);
				for (EObject containedARObject: arObject.eContents())
				{
					if (featuresToBeIgnoredList.size() == 0
						|| !featuresToBeIgnoredList.contains(containedARObject.eContainingFeature()))
					{
						result.addAll(resolveAgainstARObject(containedARObject, remainingSegments, newDepth,
							featuresToBeIgnored, ignoreCase));
					}
				}
			}

		}
		return result;
	}

	/**
	 * 
	 * @param segm1
	 * @param segm2
	 * @param ignoreCase
	 * @return
	 */
	private static boolean areSegmentsEqual(String segm1, String segm2, boolean ignoreCase)
	{
		if (ignoreCase)
		{
			return segm1.equalsIgnoreCase(segm2);
		}
		else
		{
			return segm1.equals(segm2);
		}
	}

	private static int getNewDepth(int depth)
	{
		int newDepth = depth;
		if (depth == EObjectUtil.DEPTH_ZERO)
		{
			// Just indicate that given ARObject is not the target,
			// i.e.
			// don't run do any recursions
			newDepth = INVALID_VALUE;
		}
		else if (depth == EObjectUtil.DEPTH_ONE)
		{
			// Seek within all directly contained ARObjects only,
			// i.e.
			// abort recursion after first containment
			// level
			newDepth = EObjectUtil.DEPTH_ZERO;
		}
		else if (depth == EObjectUtil.DEPTH_INFINITE)
		{ // CHECKSTYLE IGNORE check FOR NEXT 3 LINES
			// Seek within all directly and indirectly contained
			// ARObjects, i.e. let recursion go through
		}
		return newDepth;
	}

	/**
	 * Tests if given absolute qualified name segment matches given ARObject absolute qualified name segment.
	 * <p>
	 * One of the following conditions must be met:
	 * <ul>
	 * <li>The given ARObject segment represents an AUTOSAR object (i.e. AROBject segments starts with an @)</li>
	 * <li>The given ARObject segment represents a non-identifiable ARObject (i.e. AROBject segments starts with an @)</li>
	 * <li>The given ARObject segment represents an Identifiable segment and is equal to the given absolute qualified
	 * name segment</li>
	 * </ul>
	 * </p>
	 */
	private static boolean matchesARObjectSegmentOf(String arObjectSegment, String absoluteQualifiedNameSegment,
		boolean ignoreCase)
	{
		if (ignoreCase)
		{
			return arObjectSegment.startsWith(MetaModelUtils.NON_IDENTIFIABLE_SEGMENT_PREFIX)
				|| absoluteQualifiedNameSegment.equalsIgnoreCase(arObjectSegment);
		}
		else
		{
			return arObjectSegment.startsWith(MetaModelUtils.NON_IDENTIFIABLE_SEGMENT_PREFIX)
				|| absoluteQualifiedNameSegment.equals(arObjectSegment);
		}
	}

	/**
	 * Retrieves the absolute qualified name segment of given ARObject.
	 * 
	 * @param arObject
	 *        the given object
	 * @return the absolute qualified name
	 */
	public static String getAbsoluteQualifiedNameSegment(EObject arObject)
	{
		String absoluteQName = ""; //$NON-NLS-1$

		if (arObject != null)
		{
			if (arObject instanceof GReferrable)
			{
				absoluteQName = ((GReferrable) arObject).gGetShortName();
			}
			else
			{
				// Is given ARObject the resource's root, i.e. an AUTOSAR
				// object?
				if (((InternalEObject) arObject).eDirectResource() != null)
				{
					absoluteQName = MetaModelUtils.NON_IDENTIFIABLE_SEGMENT_PREFIX;
				}
				else
				{
					absoluteQName = ((InternalEObject) arObject.eContainer()).eURIFragmentSegment(
						arObject.eContainingFeature(), arObject);
				}
			}
		}
		return absoluteQName;
	}

	/**
	 * Retrieves the given URI fragment's leading segment, representing the absolute qualified name.
	 * 
	 * @param uriFragment
	 * @return the absolute qualified name
	 */
	private static String getLeadingAbsoluteQualifiedNameSegment(String uriFragment)
	{
		if (uriFragment != null)
		{
			int nextSeparatorIndex = uriFragment.indexOf(MetaModelUtils.QNAME_SEPARATOR_STR);
			if (nextSeparatorIndex != -1)
			{
				return uriFragment.substring(0, nextSeparatorIndex);
			}
			else
			{

				return uriFragment;
			}
		}
		return ""; //$NON-NLS-1$
	}
}
