/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * 20.02.2013 16:38:03
 *
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.artop.aal.gautosar.services.splitting.SplitableEObjectsProvider;
import org.artop.aal.gautosar.services.splitting.Splitables;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.osgi.util.NLS;

import com.google.common.base.Predicate;

import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.ct.sdk.utils.SplitUtils.IService;
import eu.cessar.ct.sdk.utils.SplitableException;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

/**
 * Implementation of the SplitUtils from SDK
 *
 * @author uidl6870
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Mon Jul 21 12:45:48 2014 %
 *
 *         %version: 8 %
 */
public class SDKSplitUtils implements IService
{

	/** the singleton */
	public static final IService INSTANCE = new SDKSplitUtils();

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.MergeUtils.IService#getMergedModelInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public GAUTOSAR getMergedModelInstance(IProject project)
	{
		Assert.isNotNull(project);
		GAUTOSAR mergedRoot = null;

		Collection<Resource> resources = EResourceUtils.getProjectResources(project);
		if (!resources.isEmpty())
		{
			Resource resource = resources.iterator().next();
			GAUTOSAR root = (GAUTOSAR) resource.getContents().get(0);
			mergedRoot = getSplitableProvider().splitableFor(root);
		}
		return mergedRoot;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#isMergedInstace(gautosar.ggenericstructure.ginfrastructure.GARObject)
	 */
	@Override
	public boolean isMergedInstace(GARObject arObject)
	{
		Assert.isNotNull(arObject);
		return Splitables.isSplitable(arObject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#getMergedInstance(gautosar.ggenericstructure.ginfrastructure.GARObject
	 * )
	 */
	@Override
	public <T extends GARObject> T getMergedInstance(T arObject)
	{
		Assert.isNotNull(arObject);
		return getSplitableProvider().splitableFor(arObject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#getConcreteInstances(gautosar.ggenericstructure.ginfrastructure.GARObject
	 * )
	 */
	@Override
	public <T extends GARObject> Collection<T> getConcreteInstances(T mergedInstance)
	{
		assertIsMergedInstance(mergedInstance);

		List<T> l = new ArrayList<T>();

		Splitable s = (Splitable) mergedInstance;
		EObject wrappedElement = s.wrappedElement();
		Iterable<EObject> iterable = SplitableUtils.INSTANCE.getSplitableEObjectsQuery().get(wrappedElement);

		Iterator<EObject> iterator = iterable.iterator();
		while (iterator.hasNext())
		{
			l.add((T) iterator.next());
		}

		return l;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#getInstanceResources(gautosar.ggenericstructure.ginfrastructure.GARObject
	 * )
	 */
	@Override
	public Collection<Resource> getInstanceResources(GARObject mergedInstance)
	{
		assertIsMergedInstance(mergedInstance);

		return SplitableUtils.INSTANCE.getAllResources((Splitable) mergedInstance);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#getActiveInstance(gautosar.ggenericstructure.ginfrastructure.GARObject
	 * )
	 */
	@Override
	public <T extends GARObject> T getActiveInstance(T mergedInstance)
	{
		assertIsMergedInstance(mergedInstance);

		return Splitables.unwrap(mergedInstance);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#getActiveResource(gautosar.ggenericstructure.ginfrastructure.GARObject
	 * )
	 */
	@Override
	public Resource getActiveResource(GARObject mergedInstance)
	{
		assertIsMergedInstance(mergedInstance);

		return SplitableUtils.INSTANCE.getActiveResource((Splitable) mergedInstance);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#setActiveInstance(gautosar.ggenericstructure.ginfrastructure.GARObject
	 * , gautosar.ggenericstructure.ginfrastructure.GARObject)
	 */
	@Override
	public <T extends GARObject> void setActiveInstance(T mergedInstance, T activeInstance)
	{
		assertIsMergedInstance(mergedInstance);
		Assert.isNotNull(activeInstance);

		Collection<T> concreteInstances = getConcreteInstances(mergedInstance);
		if (!concreteInstances.contains(activeInstance))
		{
			String qName1 = ModelUtils.getAbsoluteQualifiedName(activeInstance);

			T actualActiveInstance = SplitUtils.getActiveInstance(mergedInstance);
			String qName2 = ModelUtils.getAbsoluteQualifiedName(actualActiveInstance);

			throw new IllegalArgumentException(NLS.bind(Messages.SplitAPI_argumentNotAmongConcreteInstances,
				new Object[] {qName1, qName2}));
		}

		setActiveInstanceInternal(mergedInstance, activeInstance);

	}

	/**
	 * @param mergedInstance
	 * @param activeInstance
	 */
	private <T extends GARObject> void setActiveInstanceInternal(T mergedInstance, T activeInstance)
	{
		SplitableUtils.INSTANCE.changeWrappedElementInSplittable(mergedInstance, activeInstance);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.MergeUtils.IService#setActiveResource(gautosar.ggenericstructure.ginfrastructure.GARObject
	 * , org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	public void setActiveResource(GARObject mergedInstance, Resource newActiveResource)
	{
		assertIsMergedInstance(mergedInstance);
		Assert.isNotNull(newActiveResource);

		// check whether passed resource is valid
		Collection<Resource> instanceResources = getInstanceResources(mergedInstance);
		if (!instanceResources.contains(newActiveResource))
		{
			GARObject activeInstance = SplitUtils.getActiveInstance(mergedInstance);
			String qName = ModelUtils.getAbsoluteQualifiedName(activeInstance);

			String errMsg = NLS.bind(Messages.SplitAPI_argumentNotAmongInstanceResources, new Object[] {
				newActiveResource.getURI(), qName});
			throw new IllegalArgumentException(errMsg);
		}

		// already the active one
		if (getActiveResource(mergedInstance) == newActiveResource)
		{
			return;
		}

		// identify the corresponding concrete instance and set it as the active one
		Collection<GARObject> concreteInstances = getConcreteInstances(mergedInstance);
		for (GARObject garObject: concreteInstances)
		{
			if (garObject.eResource() == newActiveResource)
			{
				setActiveInstanceInternal(mergedInstance, garObject);
				break;
			}
		}
	}

	private static SplitableEObjectsProvider getSplitableProvider()
	{
		return SplitableUtils.INSTANCE.getSplitableProvider();
	}

	private <T extends GARObject> void assertIsMergedInstance(T mergedInstance)
	{
		if (!isMergedInstace(mergedInstance))
		{
			throw new IllegalArgumentException(NLS.bind(Messages.SplitAPI_argumentNotMergedObject,
				ModelUtils.getAbsoluteQualifiedName(mergedInstance)));
		}
	}

	/**
	 * Get the containment hierarchy for {@code arObject}.
	 *
	 * @param arObject
	 *        the {@code EObject}
	 * @return the containment hierarchy
	 */
	private Stack<EObject> getEObjectContainmentHierarchy(EObject arObject)
	{
		EObject currentObject = arObject;

		Stack<EObject> hierarchy = new Stack<EObject>();
		while (null != currentObject)
		{
			hierarchy.add(currentObject);
			currentObject = currentObject.eContainer();
		}

		return hierarchy;
	}

	/**
	 * Sanity-check the split request.
	 *
	 * Check if passed object is splittable and if the resource is valid (contains a single AUTOSAR root element).
	 *
	 * @param arObject
	 *        the object to create a new fragment for into {@link resource}
	 * @param resource
	 *        the destination {@code Resource}
	 * @throws SplitableException
	 *         specialized user-exposed exception for splittable
	 *
	 */
	private void sanityCheckSplit(final GARObject arObject, final Resource resource) throws SplitableException
	{
		if (!canSplit(arObject))
		{
			// should be SplittableException but refactoring will be needed
			throw new SplitableException("The supplied object: " + arObject + " is not splitable."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		EList<EObject> currentContents = resource.getContents();
		if (1 != currentContents.size())
		{
			// should be SplittableException but refactoring will be needed
			throw new SplitableException(
				"The supplied resource: " + resource + " is not a valid AUTOSAR resource (contents size is not 1)."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(arObject.eClass());
		if (null == mmService.getRootAUTOSARObject(resource))
		{
			// should be SplittableException but refactoring will be needed
			throw new SplitableException(
				"The supplied resource: " + resource + " is not a valid AUTOSAR resource (root is not AUTOSAR)."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Check if a child {@code EObject} already exists or needs to be created for split.
	 *
	 * @param parent
	 *        the parent {@code EObject}
	 * @param child
	 *        the child {@code EObject}
	 * @return the corresponding child if it exists, {@code null} otherwise.
	 */
	private static EObject getSplitChild(EObject parent, final EObject child)
	{
		EObject result = null;

		Predicate<EObject> sameAsChild = new Predicate<EObject>()
			{
			public boolean apply(EObject e)
			{
				return SplitKeyUtils.sameKey(e, child);
			}
			};

			EStructuralFeature feature = child.eContainmentFeature();
			if (null == feature)
			{
				return null;
			}

			Object value = parent.eGet(feature);

			if (feature.isMany())
			{
				if (value instanceof EList)
				{
					EList<EObject> currentContents = (EList<EObject>) value;
					for (EObject potentialChild: currentContents)
					{
						if (sameAsChild.apply(potentialChild))
						{
							result = potentialChild;
							break;
						}
					}
				}
			}
			else
			{
				if (value instanceof EObject)
				{
					EObject valueObj = (EObject) value;
					if (sameAsChild.apply(valueObj))
					{
						result = valueObj;
					}
				}
			}

			return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.SplitUtils.IService#splitObject(gautosar.ggenericstructure.ginfrastructure.GARObject,
	 * org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	public GARObject splitObject(final GARObject arObject, final Resource resource) throws SplitableException
	{
		sanityCheckSplit(arObject, resource);
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(arObject.eClass());
		EObject parentObj = mmService.getRootAUTOSARObject(resource);
		Stack<EObject> hierarchy = getEObjectContainmentHierarchy(arObject);
		EObject currentObject = null;
		if (!hierarchy.isEmpty())
		{
			currentObject = hierarchy.pop();
		}

		while (!hierarchy.isEmpty())
		{
			currentObject = hierarchy.pop();

			EObject nextChild = getSplitChild(parentObj, currentObject);
			if (null == nextChild) // Subsequent child does not exist, must be created.
			{
				EObject copyObject = mmService.splitEObject(currentObject);

				EStructuralFeature parentFeature = currentObject.eContainingFeature();

				if (parentFeature.isMany())
				{
					Object existingValue = parentObj.eGet(parentFeature);
					if (null != existingValue)
					{
						if (existingValue instanceof EList) // this should always be an EList for containment features
						{
							EList<EObject> existingValueList = (EList<EObject>) existingValue;
							existingValueList.add(copyObject);
						}
					}
					else
					{
						// This should never be the case, an empty list is always returned.
						// parentObj.eSet(parentFeature, singletonEList(copyObject));
					}
				}
				else
				{
					parentObj.eSet(parentFeature, copyObject);
				}

				parentObj = copyObject;
			}
			else
			{
				parentObj = nextChild;
			}
		}
		return (GARObject) parentObj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.SplitUtils.IService#canSplit(gautosar.ggenericstructure.ginfrastructure.GARObject)
	 */
	@Override
	public boolean canSplit(GARObject arObject)
	{
		EClass clz = arObject.eClass();
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(clz);
		return mmService.isSplitable(clz);
	}

}
