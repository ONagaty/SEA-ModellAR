package eu.cessar.ct.core.mms.internal.adapter;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;

/**
 * A CESSAR specific adapter factory implementation that extends EMF
 * {@link AdapterFactory}
 */
public class CessarAdapterFactory implements ICessarAdapterFactory
{
	private final AdapterFactory parent;

	public CessarAdapterFactory(AdapterFactory parent)
	{
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory#getParentAdapterFactory()
	 */
	public AdapterFactory getParentAdapterFactory()
	{
		return parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#adapt(java.lang.Object, java.lang.Object)
	 */
	public Object adapt(Object object, Object type)
	{
		Object obj = parent.adapt(object, type);
		if (type instanceof Class<?>)
		{
			Class<?> clz = (Class<?>) type;
			IEMFAdapterFactory contributedFactory = AdapterFactoriesExtensionsManager.eINSTANCE.getAppropriateAdapterFactory(
				object, clz);
			if (contributedFactory != null)
			{
				return contributedFactory.adapt(this, (Adapter) obj, object, clz);
			}
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#adapt(org.eclipse.emf.common.notify.Notifier, java.lang.Object)
	 */
	public Adapter adapt(Notifier target, Object type)
	{
		// use adapter override if possible
		return parent.adapt(target, type);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#adaptAllNew(org.eclipse.emf.common.notify.Notifier)
	 */
	public void adaptAllNew(Notifier notifier)
	{
		// just delegate
		parent.adaptAllNew(notifier);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#adaptNew(org.eclipse.emf.common.notify.Notifier, java.lang.Object)
	 */
	public Adapter adaptNew(Notifier target, Object type)
	{
		// just delegate ???
		return parent.adaptNew(target, type);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.AdapterFactory#isFactoryForType(java.lang.Object)
	 */
	public boolean isFactoryForType(Object type)
	{
		// look into override first then in parent
		return parent.isFactoryForType(type);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IDisposable#dispose()
	 */
	public void dispose()
	{
		if (parent instanceof IDisposable)
		{
			((IDisposable) parent).dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#addListener(org.eclipse.emf.edit.provider.INotifyChangedListener)
	 */
	public void addListener(INotifyChangedListener notifyChangedListener)
	{
		if (parent instanceof IChangeNotifier)
		{
			((IChangeNotifier) parent).addListener(notifyChangedListener);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#removeListener(org.eclipse.emf.edit.provider.INotifyChangedListener)
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener)
	{
		if (parent instanceof IChangeNotifier)
		{
			((IChangeNotifier) parent).removeListener(notifyChangedListener);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#fireNotifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void fireNotifyChanged(Notification notification)
	{
		if (parent instanceof IChangeNotifier)
		{
			((IChangeNotifier) parent).fireNotifyChanged(notification);
		}
	}
}
