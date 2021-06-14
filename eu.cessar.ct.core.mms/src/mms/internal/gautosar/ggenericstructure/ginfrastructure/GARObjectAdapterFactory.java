package mms.internal.gautosar.ggenericstructure.ginfrastructure;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import eu.cessar.ct.core.mms.adapter.AbstractEMFAdapterFactory;
import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * An adapter factory implementation that creates providers for a
 * {@link gautosar.ggenericstructure.ginfrastructure.GARObject} object.
 * 
 */
public class GARObjectAdapterFactory extends AbstractEMFAdapterFactory
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.adapter.AbstractEMFAdapterFactory#adapt(eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory, org.eclipse.emf.common.notify.Adapter, java.lang.Object, java.lang.Class)
	 */
	@Override
	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (IEditingDomainItemProvider.class.equals(type)
			|| ITreeItemContentProvider.class.equals(type))
		{
			if (target instanceof GARObject)
			{
				return new GARObjectItemProvider((AdapterImpl) parentAdapter);
			}
		}
		return super.adapt(adapterFactory, parentAdapter, target, type);
	}
}
