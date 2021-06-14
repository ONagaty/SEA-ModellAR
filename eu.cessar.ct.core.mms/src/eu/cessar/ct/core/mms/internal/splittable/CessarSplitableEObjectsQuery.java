/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 31.07.2012 17:57:04 </copyright>
 */
package eu.cessar.ct.core.mms.internal.splittable;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.singletonList;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.artop.aal.gautosar.services.splitting.SplitableEObjectsQuery;
import org.artop.aal.gautosar.services.splitting.handler.IVisibleResourcesProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author uidl6458
 * 
 */
@Singleton
public class CessarSplitableEObjectsQuery extends SplitableEObjectsQuery
{
	private static ResourceSetListener resourceSetListener;

	@SuppressWarnings("serial")
	private static class QualifiedName extends Stack<String>
	{

		public static QualifiedName from(EObject element)
		{
			EObject eContainer = element;
			QualifiedName qualifiedName = new QualifiedName();
			while (eContainer != null)
			{
				// no name is added for non-referrables (CR 9451)
				if (isIdentifiable(eContainer))
				{
					String shortName = ((GReferrable) eContainer).gGetShortName();
					if (shortName != null)
					{
						qualifiedName.add(shortName);
					}
				}

				eContainer = eContainer.eContainer();
			}
			return qualifiedName;
		}

		private static boolean isIdentifiable(Object object)
		{
			return object instanceof GReferrable;
		}

	}

	private IVisibleResourcesProvider resourceScopeProvider;
	private Map<IProject, Multimap<String, GReferrable>> cachedData;

	/**
	 * @param resourceScopeProvider
	 * @param definitionProvider
	 */
	@SuppressWarnings("restriction")
	@Inject
	public CessarSplitableEObjectsQuery(IVisibleResourcesProvider resourceScopeProvider)
	{
		super(resourceScopeProvider);
		this.resourceScopeProvider = resourceScopeProvider;
	}

	/**
	 * @param editingDomain
	 * @return
	 */
	private ResourceSetListener createResourceSetListener()
	{
		return new ResourceSetListenerImpl(NotificationFilter.createFeatureFilter(
			EcorePackage.eINSTANCE.getEResourceSet(), ResourceSet.RESOURCE_SET__RESOURCES).or(
			NotificationFilter.RESOURCE_LOADED).or(NotificationFilter.RESOURCE_UNLOADED))
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.
			 * ResourceSetChangeEvent)
			 */
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event)
			{
				if (cachedData != null)
				{
					synchronized (CessarSplitableEObjectsQuery.class)
					{
						cachedData = null;
					}
				}
			}

		};
	}

	// CHECKSTYLE:OFF
	// disabled checkstyle because it is to dangerous to refactor the algorithm
	// please enable checkstyles when changing this algorithm
	@Override
	public <T extends EObject> Iterable<T> get(T context)
	{
		if (context instanceof GAUTOSAR)
		{
			return allAutosarRootsFrom(context);
		}
		if (!(context instanceof GReferrable))
		{
			return singletonList(context);
		}
		String shortName = ((GReferrable) context).gGetShortName();
		if (shortName == null || shortName.length() == 0)
		{
			return singletonList(context);
		}
		if (context.eResource() == null || context.eResource().getResourceSet() == null)
		{
			return singletonList(context);
		}
		QualifiedName qualifiedName = QualifiedName.from(context);
		Multimap<String, ? extends GReferrable> candidates = getRootPackagesFrom((GReferrable) context);
		List<T> result = newLinkedList();
		while (!qualifiedName.isEmpty())
		{
			String name = qualifiedName.pop();
			Collection<? extends GReferrable> matchingCandidates = candidates.get(name);
			if (qualifiedName.isEmpty())
			{
				addAll(result, filter(matchingCandidates, (Class<T>) context.getClass()));
			}
			else
			{
				candidates = contentsOf(matchingCandidates);
			}
		}
		return result;
	}

	// CHECKSTYLE:ON

	private static Multimap<String, ? extends GReferrable> contentsOf(Collection<? extends EObject> parents)
	{
		Multimap<String, GReferrable> result = HashMultimap.create();

		Collection<EObject> nonReferrableChildren = newArrayListWithExpectedSize(10);

		for (EObject parent: parents)
		{
			for (EObject childObject: parent.eContents())
			{
				if (childObject instanceof GReferrable)
				{
					GReferrable child = (GReferrable) childObject;
					result.put(child.gGetShortName(), child);
				}
				else
				{
					nonReferrableChildren.add(childObject);
				}
			}
		}

		// For non-referrable children, go instead recursively through their children.
		if (!nonReferrableChildren.isEmpty())
		{
			result.putAll(contentsOf(nonReferrableChildren));
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private <T extends EObject> Iterable<T> allAutosarRootsFrom(T context)
	{
		Iterable<Resource> resourcesInScope = resourcesInScope(context);
		List<T> result = newArrayListWithExpectedSize(size(resourcesInScope));
		for (Resource resource: resourcesInScope)
		{
			for (EObject content: resource.getContents())
			{
				if (content instanceof GAUTOSAR)
				{
					result.add((T) content);
				}
			}
		}
		return result;
	}

	private Multimap<String, ? extends GReferrable> getRootPackagesFrom(GReferrable context)
	{
		TransactionalEditingDomain editingDomain = WorkspaceEditingDomainUtil.getEditingDomain(context);
		if (resourceSetListener == null)
		{
			resourceSetListener = createResourceSetListener();
		}

		if (editingDomain != null)
		{
			editingDomain.addResourceSetListener(resourceSetListener);
		}

		IFile file = EcorePlatformUtil.getFile(context);
		IProject project = file.getProject();

		if (cachedData == null || !cachedData.containsKey(project))
		{
			synchronized (CessarSplitableEObjectsQuery.class)
			{
				if (cachedData == null || !cachedData.containsKey(project))
				{
					Map<IProject, Multimap<String, GReferrable>> map = new HashMap<IProject, Multimap<String, GReferrable>>();

					Multimap<String, GReferrable> result = HashMultimap.create();
					Iterable<Resource> resourcesInScope = resourcesInScope(context);
					for (Resource resource: resourcesInScope)
					{
						for (GAUTOSAR content: filter(resource.getContents(), GAUTOSAR.class))
						{
							for (GARPackage garPackage: content.gGetArPackages())
							{
								result.put(garPackage.gGetShortName(), garPackage);
							}
						}
					}

					map.put(project, result);
					if (cachedData == null)
					{
						cachedData = map;
					}
					else
					{
						cachedData.putAll(map);
					}
				}
			}
		}

		return cachedData.get(project);
	}

	private Iterable<Resource> resourcesInScope(EObject context)
	{
		Iterable<Resource> resourcesInScope = resourceScopeProvider.getVisibleResources(context.eResource());
		return filter(resourcesInScope, notNull());
	}

}
