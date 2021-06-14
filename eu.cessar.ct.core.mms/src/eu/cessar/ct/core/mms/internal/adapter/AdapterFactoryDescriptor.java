package eu.cessar.ct.core.mms.internal.adapter;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.core.mms.internal.CessarPluginActivator;
import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;

/**
 * Descriptor class that implements a wrapper around adapter factories
 * contributed through extension points.
 */
class AdapterFactoryDescriptor
{
	/** extension point adapter factory class wrapper */
	ExtensionClassWrapper<IEMFAdapterFactory> adapterFactoryResolver;

	/** the adapter factory priority */
	int priority;

	/** the list of adapters which wrapped adapter factory knows to create */
	String adaptersList[];

	/**
	 * Default class constructor
	 * 
	 * @param adaptResolver
	 *        the instance that is able to resolve adapter factory class when
	 *        requested
	 * @param pr
	 *        the adapter factory descriptor priority; a bigger value means that
	 *        this descriptor is considered in favor of other descriptors for
	 *        the same target class
	 * @param adapters
	 *        the list of adapters which wrapped adapter factory knows to create
	 */
	public AdapterFactoryDescriptor(ExtensionClassWrapper<IEMFAdapterFactory> adaptResolver,
		int pr, String[] adapters)
	{
		Assert.isNotNull(adaptResolver);
		Assert.isNotNull(adapters);
		adapterFactoryResolver = adaptResolver;
		priority = pr;
		adaptersList = adapters;
	}

	/**
	 * Load and create an instance of wrapped adapter factory
	 * 
	 * @return An {@link IEMFAdapterFactory} instance of a contributed adapter
	 *         factory
	 */
	IEMFAdapterFactory resolveAdapterFactory()
	{
		try
		{
			return adapterFactoryResolver.getInstance();
		}
		catch (Throwable t) // SUPPRESS CHECKSTYLE returning null
		{
			CessarPluginActivator.getDefault().logError(new Exception(t));
			return null;
		}
	}

	/**
	 * Verify if given adapter class is contained through internal adapters list
	 * 
	 * @param type
	 *        the Java class that stands for the searched adapter
	 * @return <code>true</code> if wrapped adapter factory know to create
	 *         specified adapter, of <code>false</code> otherwise
	 */
	boolean hasAdapter(Class<?> type)
	{
		if (type != null)
		{
			for (int i = 0; i < adaptersList.length; i++)
			{
				if (adaptersList[i].equals(type.getName()))
				{
					return true;
				}
			}
		}
		return false;
	}

	int getPriority()
	{
		return priority;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof AdapterFactoryDescriptor)
		{
			AdapterFactoryDescriptor descriptor = (AdapterFactoryDescriptor) obj;
			return adapterFactoryResolver.equals(descriptor.adapterFactoryResolver)
				&& priority == descriptor.priority && adaptersList.equals(descriptor.adaptersList);
		}
		else
		{
			return super.equals(obj);
		}
	}
}
