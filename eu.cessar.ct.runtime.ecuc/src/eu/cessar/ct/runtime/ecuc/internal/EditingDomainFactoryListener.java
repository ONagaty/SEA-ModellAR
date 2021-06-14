package eu.cessar.ct.runtime.ecuc.internal;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.domain.factory.ITransactionalEditingDomainFactoryListener;

public class EditingDomainFactoryListener implements ITransactionalEditingDomainFactoryListener
{

	public void postCreateEditingDomain(TransactionalEditingDomain editingDomain)
	{
		editingDomain.addResourceSetListener(EcucModelResourceListener.eINSTANCE);
	}

	public void preDisposeEditingDomain(TransactionalEditingDomain editingDomain)
	{
		editingDomain.removeResourceSetListener(EcucModelResourceListener.eINSTANCE);
	}

}
