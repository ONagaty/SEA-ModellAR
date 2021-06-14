/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.adapter;

import java.util.WeakHashMap;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.workspace.domain.WorkspaceEditingDomainManager;
import org.eclipse.sphinx.emf.workspace.domain.mapping.IWorkspaceEditingDomainMapping;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.internal.adapter.CessarAdapterFactory;

/**
 * Factory class for AdapterFactory classes. It provides the engine used by the
 * CESSAR specific AUTOSAR providers.
 * 
 * @Review uidl6458 - 29.03.2012
 */
public final class CessarAdapterFactoryFactory
{

	/**
	 * The instance of the factory
	 */
	public static final CessarAdapterFactoryFactory eINSTANCE = new CessarAdapterFactoryFactory();

	private static final String PROJECT_CANNOT_BE_NULL = "Project cannot be null"; //$NON-NLS-1$

	private static final String OBJECT_CANNOT_BE_NULL = "Object cannot be null"; //$NON-NLS-1$

	private WeakHashMap<TransactionalEditingDomain, AdapterFactory> adapters = new WeakHashMap<TransactionalEditingDomain, AdapterFactory>();

	/**
	 * The private constructor
	 */
	private CessarAdapterFactoryFactory()
	{
		// singleton
	}

	/**
	 * 
	 * Return the CessarAdapterFactory associated with the AUTOSAR Editing
	 * domain from the <code>project</code>
	 * 
	 * @param project
	 *        a not-null project
	 * @return the adapter factory
	 */
	public AdapterFactory getAdapterFactory(IProject project)
	{
		Assert.isNotNull(project, PROJECT_CANNOT_BE_NULL);
		AutosarReleaseDescriptor releaseDescriptor = MetaModelUtils.getAutosarRelease(project);
		Assert.isNotNull(releaseDescriptor, "Cannot locate a release descriptor for the project " //$NON-NLS-1$
			+ project);
		IWorkspaceEditingDomainMapping domainMapping = WorkspaceEditingDomainManager.INSTANCE.getEditingDomainMapping();
		TransactionalEditingDomain domain = domainMapping.getEditingDomain(project,
			releaseDescriptor);
		return getAdapterFactory(domain);
	}

	/**
	 * Return the AdapterFactory associated with the EditingDomain of the
	 * <code>object</code>
	 * 
	 * @param object
	 * @return
	 */
	public AdapterFactory getAdapterFactory(EObject object)
	{
		Assert.isNotNull(object, OBJECT_CANNOT_BE_NULL);
		return getAdapterFactory(TransactionUtil.getEditingDomain(object));
	}

	/**
	 * Return the CessarAdapterFactory associated with the <code>domain</code>
	 * 
	 * @param domain
	 * @return
	 */
	public AdapterFactory getAdapterFactory(TransactionalEditingDomain domain)
	{
		Assert.isNotNull(domain, "Editing domain cannot be null"); //$NON-NLS-1$
		Assert.isLegal(domain instanceof AdapterFactoryEditingDomain,
			"Editing domain should be instanceof AdapterFactoryEditingDomain"); //$NON-NLS-1$
		if (adapters.containsKey(domain))
		{
			return adapters.get(domain);
		}
		else
		{
			AdapterFactoryEditingDomain adapted = (AdapterFactoryEditingDomain) domain;
			CessarAdapterFactory result = new CessarAdapterFactory(adapted.getAdapterFactory());
			adapters.put(domain, result);
			return result;
		}
	}

}
