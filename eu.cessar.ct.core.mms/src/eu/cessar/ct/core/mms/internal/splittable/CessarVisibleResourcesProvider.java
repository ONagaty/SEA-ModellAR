/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 06.04.2012 10:37:47 </copyright>
 */
package eu.cessar.ct.core.mms.internal.splittable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.artop.aal.gautosar.services.splitting.handler.IVisibleResourcesProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sphinx.emf.resource.ScopingResourceSet;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;

/**
 *
 * Class providing the set of resources that are part of the same {@link IProject}. <br>
 * To improve CPU usage, it keeps an internal cache between resources and the corresponding file <br>
 * NOTE: a bug report will be introduced in Artop in order to make the interface public
 *
 * @author uidl6870
 *
 */
public class CessarVisibleResourcesProvider implements IVisibleResourcesProvider
{
	private static final int MAXIMUM_SIZE = 2048;

	/* mapping between resource URIs and corresponding files */
	@SuppressWarnings("unchecked")
	private Map<URI, IFile> uriToFileMap = Collections.synchronizedMap(new LRUMap(MAXIMUM_SIZE));

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.artop.aal.gautosar.services.splitting.internal.IVisibleResourcesProvider#getVisibleResources(org.eclipse.
	 * emf.ecore.resource.Resource)
	 */
	public Iterable<Resource> getVisibleResources(Resource context)
	{
		if (context == null || context.getResourceSet() == null)
		{
			return Collections.emptyList();
		}

		ResourceSet rs = context.getResourceSet();

		if (!(rs instanceof ScopingResourceSet))
		{
			return rs.getResources();
		}
		else
		{

			URI contextUri = context.getURI();
			IFile contextFile;
			if (!uriToFileMap.containsKey(contextUri))
			{
				/* first query for context resource, cache its corresponding file */
				contextFile = EcorePlatformUtil.getFile(context);
				uriToFileMap.put(contextUri, contextFile);
			}
			else
			{
				/* retrieve corresponding file from cache */
				contextFile = uriToFileMap.get(contextUri);
			}
			IProject contextProject = contextFile.getProject();

			EList<Resource> resources = rs.getResources();
			List<Resource> resourcesInSameProject = new ArrayList<Resource>(resources.size());

			for (Resource resource: resources)
			{
				URI uri = resource.getURI();
				IFile iFile;
				if (!uriToFileMap.containsKey(uri)) /* add mapping, if missing */
				{

					iFile = EcorePlatformUtil.getFile(resource);
					uriToFileMap.put(uri, iFile);
				}
				else
				{
					/*
					 * obtain the underlying file from cache and compare it against the context project
					 */
					iFile = uriToFileMap.get(uri);
				}
				if (contextProject.equals(iFile.getProject()))
				{
					resourcesInSameProject.add(resource);
				}
			}
			((ArrayList<Resource>) resourcesInSameProject).trimToSize();
			return resourcesInSameProject;
		}

	}
}
