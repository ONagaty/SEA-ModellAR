package eu.cessar.ct.workspace.ui.internal;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.domain.factory.ITransactionalEditingDomainFactoryListener;

/**
 * TODO: Please comment this class
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Fri Nov 14 09:08:56 2014 %
 * 
 *         %version: 1 %
 */
public class EditingDomainFactoryListener implements ITransactionalEditingDomainFactoryListener
{

	@Override
	public void postCreateEditingDomain(TransactionalEditingDomain editingDomain)
	{

		editingDomain.addResourceSetListener(ModelResourceListener.eINSTANCE);

	}

	@Override
	public void preDisposeEditingDomain(TransactionalEditingDomain editingDomain)
	{
		editingDomain.removeResourceSetListener(ModelResourceListener.eINSTANCE);

	}
}
