/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 11.05.2012 11:13:25 </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;

import eu.cessar.ct.core.mms.internal.CessarPluginActivator;

/**
 * A cross referencer that finds each usage of an EObject or collection of
 * EObjects.<br/>
 * It is "safe" because all exception will be caught and logged.
 */
public class SafeUsageCrossReferencer extends UsageCrossReferencer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2915721980140164095L;

	private final IProgressMonitor monitor;

	/**
	 * Returns a collection of usage references from the specified content tree.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param eObject
	 *        an object whose content trees should be considered.
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		EObject eObject)
	{
		return find(eObjectOfInterest, eObject, null);
	}

	/**
	 * Returns a collection of usage references from the specified content tree.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param eObject
	 *        an object whose content trees should be considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		EObject eObject, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(eObject, monitor).findUsage(eObjectOfInterest);
	}

	/**
	 * Returns a collection of usage references from the specified content tree.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param resource
	 *        a resource whose content tree should be considered.
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		Resource resource)
	{
		return find(eObjectOfInterest, resource, null);
	}

	/**
	 * Returns a collection of usage references from the specified content tree.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param resource
	 *        a resource whose content tree should be considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		Resource resource, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(resource, monitor).findUsage(eObjectOfInterest);
	}

	/**
	 * Returns a collection of usage references from the specified content tree.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param resourceSet
	 *        a resource set whose content tree should be considered.
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		ResourceSet resourceSet)
	{
		return find(eObjectOfInterest, resourceSet, null);
	}

	/**
	 * Returns a collection of usage references from the specified content tree.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param resourceSet
	 *        a resource set whose content tree should be considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		ResourceSet resourceSet, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(resourceSet, monitor).findUsage(eObjectOfInterest);
	}

	/**
	 * Returns a collection of usage references from the combined content trees
	 * of the specified collection of objects.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param emfObjectsToSearch
	 *        a collection of objects whose combined content trees should be
	 *        considered.
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		Collection<?> emfObjectsToSearch)
	{
		return find(eObjectOfInterest, emfObjectsToSearch, null);
	}

	/**
	 * Returns a collection of usage references from the combined content trees
	 * of the specified collection of objects.
	 * 
	 * @param eObjectOfInterest
	 *        the usage target.
	 * @param emfObjectsToSearch
	 *        a collection of objects whose combined content trees should be
	 *        considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a collection of cross references.
	 */
	public static Collection<EStructuralFeature.Setting> find(EObject eObjectOfInterest,
		Collection<?> emfObjectsToSearch, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(emfObjectsToSearch, monitor).findUsage(eObjectOfInterest);
	}

	/**
	 * Returns a map of usage references from the specified content tree.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param eObject
	 *        an object whose content trees should be considered.
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, EObject eObject)
	{
		return findAll(eObjectsOfInterest, eObject, null);
	}

	/**
	 * Returns a map of usage references from the specified content tree.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param eObject
	 *        an object whose content trees should be considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, EObject eObject, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(eObject, monitor).findAllUsage(eObjectsOfInterest);
	}

	/**
	 * Returns a map of usage references from the specified content tree.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param resource
	 *        a resource whose content tree should be considered.
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, Resource resource)
	{
		return findAll(eObjectsOfInterest, resource, null);
	}

	/**
	 * Returns a map of usage references from the specified content tree.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param resource
	 *        a resource whose content tree should be considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, Resource resource, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(resource, monitor).findAllUsage(eObjectsOfInterest);
	}

	/**
	 * Returns a map of usage references from the specified content tree.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param resourceSet
	 *        a resource set whose content tree should be considered.
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, ResourceSet resourceSet)
	{
		return findAll(eObjectsOfInterest, resourceSet, null);
	}

	/**
	 * Returns a map of usage references from the specified content tree.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param resourceSet
	 *        a resource set whose content tree should be considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, ResourceSet resourceSet, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(resourceSet, monitor).findAllUsage(eObjectsOfInterest);
	}

	/**
	 * Returns a map of usage references from the combined content trees of the
	 * specified collection of objects.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param emfObjectsToSearch
	 *        a collection of objects whose combined content trees should be
	 *        considered.
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, Collection<?> emfObjectsToSearch)
	{
		return findAll(eObjectsOfInterest, emfObjectsToSearch, null);
	}

	/**
	 * Returns a map of usage references from the combined content trees of the
	 * specified collection of objects.
	 * 
	 * @param eObjectsOfInterest
	 *        a collection of usage targets.
	 * @param emfObjectsToSearch
	 *        a collection of objects whose combined content trees should be
	 *        considered.
	 * @param monitor
	 *        a progress monitor that can cancel the search, can be null
	 * @return a map of cross references.
	 */
	public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(
		Collection<?> eObjectsOfInterest, Collection<?> emfObjectsToSearch, IProgressMonitor monitor)
	{
		return new SafeUsageCrossReferencer(emfObjectsToSearch, monitor).findAllUsage(eObjectsOfInterest);
	}

	/**
	 * Creates an instance for the given object.
	 * 
	 * @param eObject
	 *        the object to cross reference.
	 */
	protected SafeUsageCrossReferencer(EObject eObject, IProgressMonitor monitor)
	{
		super(eObject);
		this.monitor = monitor == null ? new NullProgressMonitor() : monitor;
	}

	/**
	 * Creates an instance for the given resource.
	 * 
	 * @param resource
	 *        the resource to cross reference.
	 */
	protected SafeUsageCrossReferencer(Resource resource, IProgressMonitor monitor)
	{
		super(resource);
		this.monitor = monitor == null ? new NullProgressMonitor() : monitor;
	}

	/**
	 * Creates an instance for the given resource set.
	 * 
	 * @param resourceSet
	 *        the resource set to cross reference.
	 */
	protected SafeUsageCrossReferencer(ResourceSet resourceSet, IProgressMonitor monitor)
	{
		super(resourceSet);
		this.monitor = monitor == null ? new NullProgressMonitor() : monitor;
	}

	/**
	 * Creates an instance for the given collection of objects.
	 * 
	 * @param emfObjects
	 *        the collection of objects to cross reference.
	 */
	protected SafeUsageCrossReferencer(Collection<?> emfObjects, IProgressMonitor monitor)
	{
		super(emfObjects);
		this.monitor = monitor == null ? new NullProgressMonitor() : monitor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer#handleCrossReference(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected void handleCrossReference(EObject eObject)
	{
		try
		{
			InternalEObject internalEObject = (InternalEObject) eObject;
			for (EContentsEList.FeatureIterator<EObject> crossReferences = getCrossReferences(internalEObject); crossReferences.hasNext();)
			{
				if (monitor.isCanceled())
				{
					return;
				}
				EObject crossReferencedEObject = crossReferences.next();
				if (crossReferencedEObject != null)
				{
					EReference eReference = (EReference) crossReferences.feature();
					if (crossReference(internalEObject, eReference, crossReferencedEObject))
					{
						add(internalEObject, eReference, crossReferencedEObject);
					}
				}
			}
		}
		catch (Throwable t) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(t);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer#crossReference()
	 */
	@Override
	protected void crossReference()
	{
		for (TreeIterator<Notifier> contents = newContentsIterator(); contents.hasNext();)
		{
			if (monitor.isCanceled())
			{
				return;
			}
			Object content = contents.next();
			if (content instanceof EObject)
			{
				EObject eObject = (EObject) content;
				if (containment(eObject))
				{
					handleCrossReference(eObject);
				}
				else
				{
					contents.prune();
				}
			}
		}
	}
}
