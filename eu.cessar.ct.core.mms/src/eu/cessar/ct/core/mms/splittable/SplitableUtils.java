/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Sep 6, 2011 4:28:46 PM </copyright>
 */
package eu.cessar.ct.core.mms.splittable;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.artop.aal.gautosar.services.splitting.SplitableEObjectsProvider;
import org.artop.aal.gautosar.services.splitting.SplitableEObjectsProvider.Default;
import org.artop.aal.gautosar.services.splitting.SplitableEObjectsProvider.SplitableAdapter;
import org.artop.aal.gautosar.services.splitting.SplitableEObjectsQuery;
import org.artop.aal.gautosar.services.splitting.Splitables;
import org.artop.aal.gautosar.services.splitting.handler.IVisibleResourcesProvider;
import org.artop.aal.gautosar.services.splitting.handler.SplitableEListsProvider;
import org.artop.aal.gautosar.services.splitting.handler.StubHelper;
import org.artop.aal.gautosar.services.splitting.internal.SplitableEObject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import eu.cessar.ct.core.mms.internal.splittable.CessarSplitableEListProvider;
import eu.cessar.ct.core.mms.internal.splittable.CessarStubHelper;
import eu.cessar.ct.core.mms.internal.splittable.CessarVisibleResourcesProvider;
import eu.cessar.ct.core.platform.util.ReflectionUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * Class that provide various utilities around splittable services
 *
 * @author uidl6458
 *
 * @Review uidl6458 - 30.03.2012
 *
 */
public final class SplitableUtils
{

	/**
	 * The instance of this class
	 */
	public static final SplitableUtils INSTANCE = new SplitableUtils();

	private SplitableEObjectsProvider splittableEObjectsProvider;
	private SplitableEObjectsQuery splitableEObjectsQuery;
	private Injector injector;

	private volatile int readOnlyDepth;

	/**
	 * The private constructor.
	 */
	private SplitableUtils()
	{
		injector = Guice.createInjector(new AbstractModule()
		{

			@Override
			protected void configure()
			{
				bind(SplitableEListsProvider.class).to(CessarSplitableEListProvider.class);
				bind(IVisibleResourcesProvider.class).to(CessarVisibleResourcesProvider.class);
				// bind(SplitableEObjectsQuery.class).to(CessarSplitableEObjectsQuery.class);
				bind(StubHelper.class).to(CessarStubHelper.class);
			}

		});
		splittableEObjectsProvider = injector.getInstance(SplitableEObjectsProvider.class);
		splitableEObjectsQuery = injector.getInstance(SplitableEObjectsQuery.class);
	}

	/**
	 * Return true if the argument is a splited object, false otherwise
	 *
	 * @param object
	 * @return true if the object is an instanceof splitable
	 */
	public boolean isSplitable(EObject object)
	{
		return object instanceof Splitable;
	}

	/**
	 * If the object is splitable it will return the active fragment otherwise it will return itself.
	 *
	 * @param object
	 * @return itself if not splitable or the active fragment if splitable
	 */
	@SuppressWarnings("unchecked")
	public <T> T unWrapMergedObject(T object)
	{
		if (object instanceof Splitable)
		{
			return (T) SplitableUtils.INSTANCE.getActiveFragment((Splitable) object);
		}
		else
		{
			return object;
		}
	}

	/**
	 * Checks reliably whether the given object has the given definition, even if a split context, meaning any of the
	 * given arguments can either be splitted or not.
	 *
	 * @param configObject
	 *        - the config object which shall be evaluated.
	 * @param definitionToCheck
	 *        - the definition to be checked for.
	 * @return whether the object has the definition.
	 */
	public boolean hasDefinition(GARObject configObject, GARObject definitionToCheck)
	{
		if (definitionToCheck == null)
		{
			return false; // fast failure
		}

		// we want to go to fragment level as definitions are not splitable and the equals of fragments is more
		// deterministic...
		GARObject defFragmentToCheck = definitionToCheck;
		if (defFragmentToCheck instanceof Splitable)
		{
			defFragmentToCheck = unWrapMergedObject(definitionToCheck);
		}

		EStructuralFeature definitionFeature = configObject.eClass().getEStructuralFeature("definition"); //$NON-NLS-1$
		if (definitionFeature == null)
		{
			return false; // has no definition by meta-model
		}

		// same again, we want to go to fragment level
		GARObject actualDef = (GARObject) configObject.eGet(definitionFeature);
		if (actualDef instanceof Splitable)
		{
			actualDef = unWrapMergedObject(actualDef);
		}

		return actualDef.equals(defFragmentToCheck);
	}

	/**
	 * Return the SplitableEObjectProvider
	 *
	 * @return the provider of splittable EObjects
	 */
	public SplitableEObjectsProvider getSplitableProvider()
	{
		return splittableEObjectsProvider;
	}

	/**
	 * Return the SplitableEObjectsQuery
	 *
	 * @return the query
	 */
	public SplitableEObjectsQuery getSplitableEObjectsQuery()
	{
		return splitableEObjectsQuery;
	}

	/**
	 * It returns the existing splitable for the given concrete (file based) object or <code>null</code> if no splitable
	 * is associated to it<br>
	 * NOTE: The method has been created as a substitute for {@link Splitables#getSplitable(EObject)}, taking into
	 * account that our implementation of {@link SplitableEObjectsProvider} uses {@link CessarSplitableAdapter}, whilst
	 * {@link Default} uses {@link SplitableAdapter}
	 *
	 * @param nonSplitableEObject
	 *        the EObject for which the splitable is queried
	 * @return an existing SplitableEObject proxy for the given EObject, otherwise <code>null</code>.
	 */
	public EObject getSplitable(EObject nonSplitableEObject)
	{
		SplitableAdapter<?> adapter = (SplitableAdapter<?>) EcoreUtil.getAdapter(nonSplitableEObject.eAdapters(),
			SplitableAdapter.class);
		return adapter != null ? adapter.getSplitable() : null;
	}

	/**
	 * @return the read-only status
	 */
	public boolean isReadOnly()
	{
		return readOnlyDepth > 0;
	}

	/**
	 *
	 */
	public void enterReadOnly()
	{
		readOnlyDepth++;
	}

	/**
	 *
	 */
	public void leaveReadOnly()
	{
		readOnlyDepth--;
	}

	/**
	 *
	 */
	public void resetReadOnly()
	{
		readOnlyDepth = 0;
	}

	/**
	 * Gets all fragments for a given splitable's wrapped element. For instance, if a splitable is stored in 3 files:
	 * file1, file2 and file3, the method returns all the fragments corresponding to the objects stored in these files.
	 *
	 * @param s
	 *        the splitable for which we ask for the fragments
	 * @return the fragments of the splitable object
	 */
	public List<EObject> getAllFragments(Splitable s)
	{
		List<EObject> fragments = new ArrayList<EObject>();
		for (EObject fragment: splitableEObjectsQuery.get(s.wrappedElement()))
		{
			if (!fragments.contains(fragment))
			{
				fragments.add(fragment);
			}
		}
		return fragments;
	}

	/**
	 * Gets all the files where the fragments of the splitable's wrapped element are stored in.
	 *
	 * @param s
	 *        the splitable referred
	 * @return the resources where all the fragments are stored in
	 */
	public List<Resource> getAllResources(Splitable s)
	{
		List<Resource> resources = new ArrayList<>();
		if (null != s)
		{
			for (EObject fragment: splitableEObjectsQuery.get(s.wrappedElement()))
			{
				Resource resource = fragment.eResource();
				if (!resources.contains(resource))
				{
					resources.add(resource);
				}
			}
		}
		return resources;
	}

	/**
	 * Gets a list with the fragments for the given splitable's wrapped element, that have the specified
	 * <code>feature</code> set.
	 *
	 * @param s
	 *        the splittable object
	 * @param feature
	 *        the feature that the obtained fragments have set
	 * @return the list with fragments of interest, never <code>null</code>
	 */
	public List<EObject> getFragmentsWithFeature(Splitable s, EStructuralFeature feature)
	{
		List<EObject> fragments = new ArrayList<EObject>();

		List<EObject> allFragments = getAllFragments(s);
		for (EObject fragment: allFragments)
		{
			Object value = fragment.eGet(feature);
			if (value != null && fragment.eIsSet(feature))
			{
				fragments.add(fragment);
			}
		}
		return fragments;
	}

	/**
	 * Gets the active fragment for a given splitable. It usually is it's wrapped element.
	 *
	 * @param s
	 *        the splitable for which we ask for the active fragment
	 * @return the active fragment - wrapped element
	 */
	public EObject getActiveFragment(Splitable s)
	{
		return s.wrappedElement();
	}

	/**
	 * Gets the active resource for a given splitable.
	 *
	 * @param s
	 *        the splitable for which we ask for the active resource
	 * @return the resource which was set as active
	 */
	public Resource getActiveResource(Splitable s)
	{
		return getActiveFragment(s).eResource();
	}

	/**
	 * Sets the active resource for a given splitable element. For instance, if a splitable is stored in the files
	 * file1, file2 and file3, only one of these files can be marked as the active resource where persistent operations
	 * can be performed on the splitable.
	 *
	 * @param s
	 *        the splitable for which we set the active resource
	 * @param r
	 *        the resource which we set as active for the given splitable. The resource must be one of the files where
	 *        the splitable's fragments are stored.
	 * @throws SplittableException
	 *         when the provided resource is not among the resources where the splittable is saved, or when one of the
	 *         parameters is null
	 */
	@SuppressWarnings("nls")
	public void setActiveResource(Splitable s, Resource r) throws SplittableException
	{
		checkParameters(s, r);

		if (!getAllResources(s).contains(r))
		{
			throw new SplittableException("The resource " + r.getURI() + " is not a resource among which "
				+ s.wrappedElement() + " is stored.");
		}
		if (getActiveResource(s) == r)
		{
			return;
		}

		for (EObject fragment: getAllFragments(s))
		{
			if (fragment.eResource() == r)
			{
				setActiveFragment(s, fragment);
				break;
			}
		}
	}

	/**
	 * @param s
	 * @param r
	 * @throws SplittableException
	 */
	private static void checkParameters(Splitable s, Resource r) throws SplittableException
	{
		if (r == null)
		{
			throw new SplittableException("The Resource provided is null"); //$NON-NLS-1$
		}
		if (s == null)
		{
			throw new SplittableException("The Splitable provided is null"); //$NON-NLS-1$
		}

	}

	/**
	 * Sets the active fragment for a given splitable. Only one of the splitable's fragments can be marked as active.
	 *
	 * @param splitable
	 *        the splitable for which the active fragment is set
	 * @param activeFragment
	 *        the element which will be set as active fragment for the splitable s.
	 * @throws SplittableException
	 *         when the provided fragment is not among the fragments where the splittable is saved, or when one of the
	 *         parameters is null
	 */
	@SuppressWarnings("nls")
	public void setActiveFragment(Splitable splitable, EObject activeFragment) throws SplittableException
	{
		if (splitable == null)
		{
			throw new SplittableException("The Splitable provided is null");
		}
		if (activeFragment == null)
		{
			throw new SplittableException("The fragment provided is null");
		}

		if (!getAllFragments(splitable).contains(activeFragment))
		{
			throw new SplittableException("The fragment provided is not among the fragments where the splittable "
				+ splitable.wrappedElement() + " is saved");
		}

		if (splitable.wrappedElement() == activeFragment)
		{
			return;
		}

		changeWrappedElementInSplittable((EObject) splitable, activeFragment);
	}

	/**
	 * Updates <code>splitable</code>, by changing its wrapped element to the given <code>toWrap</code> EObject
	 *
	 * @param splitable
	 *        SplitableEObject proxy (a merged object)
	 * @param toWrap
	 *        the new wrapped element
	 */
	@SuppressWarnings("restriction")
	public void changeWrappedElementInSplittable(EObject splitable, EObject toWrap)
	{
		SplitableEObject invocationHandler = (SplitableEObject) Proxy.getInvocationHandler(splitable);
		EObject original = invocationHandler.getWrappedElement();
		try
		{
			ReflectionUtils.setFieldValue(SplitableEObject.class, invocationHandler, "toWrap", toWrap); //$NON-NLS-1$
		}
		catch (Exception e) // SUPPRESS CHECKSTYLE catch all
		{
			// an exception here is fatal, it can happen if Artop has been updated but CT has not been adapted to it yet
			throw new RuntimeException("Fatal error in splittable", e); //$NON-NLS-1$
		}
		// Be sure that the split adapter is stored also on the new toWrap
		SplitableAdapter<?> originalAdapter = (SplitableAdapter<?>) EcoreUtil.getAdapter(original.eAdapters(),
			SplitableAdapter.class);
		SplitableAdapter<?> newAdapter = (SplitableAdapter<?>) EcoreUtil.getAdapter(toWrap.eAdapters(),
			SplitableAdapter.class);
		if (newAdapter == null)
		{
			toWrap.eAdapters().add(originalAdapter);
		}

	}

	/**
	 * Recursive find parent resources for enable to split or move
	 *
	 *
	 * @param eObject
	 * @param instanceResourcesOfParent
	 *
	 * @return
	 */
	public Collection<Resource> findRecursiveParentResources(EObject eObject,
		Collection<Resource> instanceResourcesOfParent)
		{

		GARObject gParent = (GARObject) eObject;
		instanceResourcesOfParent = SplitUtils.getInstanceResources(gParent);

		if (eObject.eContainer() == null)
		{
			return instanceResourcesOfParent;
		}
		return findRecursiveParentResources(eObject.eContainer(), instanceResourcesOfParent);
		}

	/**
	 * Recursive find parent eOBjects for perform operation to split or move
	 *
	 *
	 * @param eObject
	 * @param instanceEobjectsOfParent
	 *
	 * @return eObjects
	 */
	public List<EObject> findRecursiveParentEObjects(EObject eObject, List<EObject> instanceEobjectsOfParent)
	{

		Splitable splitable = (Splitable) eObject;

		for (EObject fragment: splitableEObjectsQuery.get(splitable.wrappedElement()))
		{
			if (!instanceEobjectsOfParent.contains(fragment))
			{
				instanceEobjectsOfParent.add(fragment);
			}
		}

		if (eObject.eContainer() == null)
		{
			return instanceEobjectsOfParent;
		}

		return findRecursiveParentEObjects(eObject.eContainer(), instanceEobjectsOfParent);
	}

	/**
	 * Recursive find parent eOBjects for perform operation to split or move
	 *
	 *
	 * @param eObject
	 *
	 * @return eObjects
	 */
	public List<EObject> findRecursiveParentEobjects(EObject eObject, List<EObject> instanceEobjectsOfParent)
	{

		Splitable splitable = (Splitable) eObject;

		for (EObject fragment: splitableEObjectsQuery.get(splitable.wrappedElement()))
		{
			if (!instanceEobjectsOfParent.contains(fragment))
			{
				instanceEobjectsOfParent.add(fragment);
			}
		}

		if (eObject.eContainer() == null)
		{
			return instanceEobjectsOfParent;
		}
		else
		{
			return findRecursiveParentEobjects(eObject.eContainer(), instanceEobjectsOfParent);
		}

	}

}
