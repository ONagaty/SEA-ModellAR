package mms.internal.gautosar.gecucdescription;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;

import eu.cessar.ct.core.mms.adapter.AbstractEMFAdapterFactory;
import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.providers.ICategorizedChildrenProvider;

/**
 * A generic adapter factory implementation that create generic provider
 * implementations for GContainer.
 */
public class GContainerAdapterFactory extends AbstractEMFAdapterFactory
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.adapter.AbstractEMFAdapterFactory#adapt(eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory, org.eclipse.emf.common.notify.Adapter, java.lang.Object, java.lang.Class)
	 */
	@Override
	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (IEditingDomainItemProvider.class.equals(type))
		{
			if (parentAdapter instanceof IEditingDomainItemProvider)
			{
				return new GContainerEditingDomainItemProvider(
					(IEditingDomainItemProvider) parentAdapter);
			}
		}
		else if (ICategorizedChildrenProvider.class.equals(type))
		{
			return new EcucContainerCategorizedChildrenProvider();
		}

		return super.adapt(adapterFactory, parentAdapter, target, type);
	}
}
