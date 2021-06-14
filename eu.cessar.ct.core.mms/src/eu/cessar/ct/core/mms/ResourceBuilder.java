/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 13.09.2012 14:05:52 </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.sphinx.emf.workspace.domain.WorkspaceEditingDomainManager;
import org.eclipse.sphinx.emf.workspace.domain.factory.IExtendedTransactionalEditingDomainFactory;

/**
 * 
 * Builder that creates EMF resources into proprietary editing domains. <br>
 * Usage: creation of temporary resources outside the resource sets belonging to the editing domains associated to the
 * CESSAR-CT projects.
 * 
 * It holds proprietary editing domains (an editing domain per {@link IMetaModelDescriptor})
 * 
 * @author uidl6870
 * 
 */
public final class ResourceBuilder
{
	/** the singleton */
	public static final ResourceBuilder INSTANCE = new ResourceBuilder();

	private Map<IMetaModelDescriptor, TransactionalEditingDomain> map;

	private ResourceBuilder()
	{
		map = new HashMap<IMetaModelDescriptor, TransactionalEditingDomain>();
	}

	/**
	 * Creates a resource in a proprietary editing domain.
	 * 
	 * @param descriptor
	 *        The meta-model descriptor based on which to obtain the editing domain to be used
	 * 
	 * @param uri
	 *        the URI of the resource to create
	 * 
	 * @param contentType
	 *        the content type identifier of the URI
	 * 
	 * @param content
	 *        the content to be added to the resource
	 * @return the resource
	 * @throws OperationCanceledException
	 * @throws ExecutionException
	 */
	public Resource create(IMetaModelDescriptor descriptor, URI uri, String contentType, final EObject content)
		throws ExecutionException
	{
		if (!map.containsKey(descriptor))
		{
			IExtendedTransactionalEditingDomainFactory factory = WorkspaceEditingDomainManager.INSTANCE.getEditingDomainFactory(descriptor);

			List<IMetaModelDescriptor> descriptors = new ArrayList<IMetaModelDescriptor>();
			descriptors.add(descriptor);
			TransactionalEditingDomain editingDomain = factory.createEditingDomain(descriptors);
			editingDomain.setID("tempEditingDomain"); //$NON-NLS-1$
			map.put(descriptor, editingDomain);
		}

		// create resource
		TransactionalEditingDomain editingDomain = map.get(descriptor);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.createResource(uri, contentType);

		// append content
		final Resource[] resources = new Resource[1];
		resources[0] = resource;
		WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable()
		{
			public void run()
			{
				resources[0].getContents().add(content);
			}
		}, ""); //$NON-NLS-1$

		return resource;
	}

}
