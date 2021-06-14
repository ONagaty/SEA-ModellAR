/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 21.06.2012 10:57:28 </copyright>
 */
package eu.cessar.ct.workspace.internal.domain;

import java.util.Collection;

import org.artop.aal.workspace.domain.AbstractAutosarWorkspaceEditingDomainFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;

/**
 * @author uidl6458
 * 
 */
public class CessarWorkspaceEditingDomainFactory extends AbstractAutosarWorkspaceEditingDomainFactory
{

	/**
	 * Default constructor.
	 */
	public CessarWorkspaceEditingDomainFactory()
	{
		// Use an instance of ExtendedDiagramEditingDomainFactory as
		// IExtendedTransactionalEditingDomainFactory
		// delegate
		super(new CessarExtendedWorkspaceEditingDomainFactory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.sphinx.emf.workspace.domain.factory.DelegatingTransactionalEditingDomainFactory#createEditingDomain
	 * (java.util.Collection)
	 */
	@Override
	public TransactionalEditingDomain createEditingDomain(Collection<IMetaModelDescriptor> metaModelDescriptors)
	{
		TransactionalEditingDomain result = super.createEditingDomain(metaModelDescriptors);
		configure(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.artop.aal.workspace.domain.AbstractAutosarWorkspaceEditingDomainFactory#createEditingDomain(java.util.Collection
	 * , org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	@Override
	public TransactionalEditingDomain createEditingDomain(Collection<IMetaModelDescriptor> metaModelDescriptors,
		ResourceSet resourceSet)
	{
		TransactionalEditingDomain result = super.createEditingDomain(metaModelDescriptors, resourceSet);
		configure(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.workspace.domain.AbstractAutosarWorkspaceEditingDomainFactory#createResourceSet()
	 */
	@Override
	protected ResourceSet createResourceSet()
	{
		return new CessarResourceSetImpl();
	}

	/**
	 * @param result
	 */
	private void configure(TransactionalEditingDomain domain)
	{
		ResourceSet resourceSet = domain.getResourceSet();
		// remove the cross referencer if added
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(resourceSet);
		if (adapter != null)
		{
			resourceSet.eAdapters().remove(adapter);
		}
	}
}
