package eu.cessar.ct.core.mms.internal.proxy;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.domain.factory.ITransactionalEditingDomainFactoryListener;

public class EditingDomainListener implements ITransactionalEditingDomainFactoryListener
{

	@Override
	public void postCreateEditingDomain(TransactionalEditingDomain editingDomain)
	{
		// editingDomain.addResourceSetListener(ResourceSetListener.eINSTANCE);
		// install the proxy manager
		// CessarProxyManager.install();
	}

	@Override
	public void preDisposeEditingDomain(TransactionalEditingDomain editingDomain)
	{
		// editingDomain.removeResourceSetListener(ResourceSetListener.eINSTANCE);
	}

}
