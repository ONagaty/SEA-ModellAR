package eu.cessar.ct.core.mms.internal.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.core.mms.internal.CessarPluginActivator;
import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;

/**
 * Adapter factories contributions manager This class is responsible with
 * managing the adapter factories specialized for each AUTOSAR MM and
 * contributed by MMS plugins.
 */
@SuppressWarnings("nls")
final class AdapterFactoriesExtensionsManager
{
	private static final String FACTORY = "factory";

	public static AdapterFactoriesExtensionsManager eINSTANCE = new AdapterFactoriesExtensionsManager();

	private Map<Class<?>, List<AdapterFactoryDescriptor>> targetAdaptorsMap;

	/**
	 * 
	 */
	private AdapterFactoriesExtensionsManager()
	{
		targetAdaptorsMap = new HashMap<Class<?>, List<AdapterFactoryDescriptor>>();
		init();
	}

	private void init()
	{
		// Iterate through adapter factories extension points
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
			CessarPluginActivator.PLUGIN_ID, "emfAdaptorOverride");

		IConfigurationElement[] extensions = extensionPoint.getConfigurationElements();
		for (int i = 0; i < extensions.length; i++)
		{
			if (!FACTORY.equals(extensions[i].getName()))
			{
				continue;
			}
			addConfigurationElement(extensions[i]);
		}
	}

	/**
	 * The method that create an adapter factory descriptor for a given
	 * extension point and index it.
	 * 
	 * @param configElement
	 *        contributed adapter factory extension point instance
	 */
	private void addConfigurationElement(IConfigurationElement configElement)
	{
		// create class wrapper for contributed adapter factory class
		String contributingPlugin = configElement.getContributor().getName();
		String factoryClassQName = configElement.getAttribute(FACTORY);
		ExtensionClassWrapper<IEMFAdapterFactory> factoryWrapper = new ExtensionClassWrapper<IEMFAdapterFactory>(
			contributingPlugin, factoryClassQName);

		// read adapter classes that current factory has attached
		List<String> adaptersList = new ArrayList<String>();
		IConfigurationElement[] children = configElement.getChildren("adapter");
		for (int i = 0; i < children.length; i++)
		{
			String adapterTp = children[i].getAttribute("type");
			if ((adapterTp != null) && (adapterTp.length() > 5))
			{
				adaptersList.add(adapterTp);
			}
		}
		((ArrayList<String>) adaptersList).trimToSize();

		// create adapter descriptor instance based on the data collected from
		// current configuration element (extension point)
		String priority = configElement.getAttribute("priority");
		AdapterFactoryDescriptor adapterDescriptor = new AdapterFactoryDescriptor(factoryWrapper,
			Integer.parseInt(priority), adaptersList.toArray(new String[adaptersList.size()]));

		String target = configElement.getAttribute("target");
		Class<?> targetClass = getClass(contributingPlugin, target);

		if (targetAdaptorsMap.containsKey(targetClass))
		{
			// update descriptors list for current target class, if a similar
			// adapter descriptor was not assigned yet
			if (!targetAdaptorsMap.get(targetClass).contains(adapterDescriptor))
			{
				targetAdaptorsMap.get(targetClass).add(adapterDescriptor);
			}
		}
		else
		{
			// create a new adapter descriptors array for current target class
			List<AdapterFactoryDescriptor> descriptors = new ArrayList<AdapterFactoryDescriptor>();
			descriptors.add(adapterDescriptor);
			targetAdaptorsMap.put(targetClass, descriptors);
		}
	}

	private synchronized Class<?> getClass(String ownerPlugin, String className)
	{
		Bundle bundle = Platform.getBundle(ownerPlugin);
		if (bundle == null)
		{
			CessarPluginActivator.getDefault().logError("Cannot locate plugin: {0}", ownerPlugin);
		}
		try
		{
			return bundle.loadClass(className);
		}
		catch (ClassNotFoundException ex)
		{
			CessarPluginActivator.getDefault().logError(ex);
			return null;
		}
	}

	/**
	 * Scan internal adapter descriptors map and return the appropriate adapter
	 * factory. In scanning process, it consider the type on which must be
	 * adapted as well as the extension point priority and the one with the
	 * highest priority will be returned/
	 * 
	 * @param object
	 *        the object that must be adapted
	 * @param type
	 *        the adapter to which the object must be adapted
	 * @return An {@link IEMFAdapterFactory} instance representing the factory
	 *         which knows to construct requested adapter, or <code>null</code>
	 *         if no such instance is contributed yet. The caller must use the
	 *         default adapter factory,
	 */
	public IEMFAdapterFactory getAppropriateAdapterFactory(Object object, Class<?> type)
	{
		AdapterFactoryDescriptor afDescriptor = null;
		Set<Class<?>> keySet = targetAdaptorsMap.keySet();
		Iterator<Class<?>> classIt = keySet.iterator();
		while (classIt.hasNext())
		{
			Class<?> clazz = classIt.next();
			if (clazz.isInstance(object))
			{
				List<AdapterFactoryDescriptor> descriptosList = targetAdaptorsMap.get(clazz);
				for (AdapterFactoryDescriptor descriptor: descriptosList)
				{
					if (descriptor.hasAdapter(type))
					{
						// an adapter factory descriptor was found; verify the
						// priority
						if (afDescriptor == null)
						{
							afDescriptor = descriptor;
						}
						else if (descriptor.getPriority() > afDescriptor.getPriority())
						{
							afDescriptor = descriptor;
						}
					}
				}
			}
		}
		if (afDescriptor != null)
		{
			return afDescriptor.resolveAdapterFactory();
		}
		else
		{
			return null;
		}
	}
}
