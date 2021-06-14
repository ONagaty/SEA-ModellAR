/**
 * 
 */
package eu.cessar.ct.ecuc.workspace.sort;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.ecuc.workspace.internal.sort.ContainerSortProviderImpl;
import eu.cessar.ct.ecuc.workspace.internal.sort.ModuleConfigurationSortProviderImpl;
import eu.cessar.ct.workspace.sort.ISortProvider;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * @author uidl6458
 * 
 */
public class EcuSortProvider
{

	public static ISortProvider create(final TransactionalEditingDomain domain,
		final EObject parentObject)
	{
		if (parentObject instanceof GModuleConfiguration)
		{
			return new ModuleConfigurationSortProviderImpl(domain,
				(GModuleConfiguration) parentObject,
				((GModuleConfiguration) parentObject).gGetDefinition());
		}
		else if (parentObject instanceof GContainer)
		{
			GContainer container = ((GContainer) parentObject);
			return new ContainerSortProviderImpl(domain, container, container.gGetDefinition());
		}
		else
		{
			return null;
		}
	}
}
