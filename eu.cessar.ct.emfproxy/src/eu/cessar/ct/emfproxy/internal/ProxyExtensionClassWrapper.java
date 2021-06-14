package eu.cessar.ct.emfproxy.internal;

import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * Allow lazy loading for classes found in extension points contributed for
 * proxy framework.
 */
public class ProxyExtensionClassWrapper<T> extends ExtensionClassWrapper<T>
{
	public ProxyExtensionClassWrapper(String ownerPlugin, String className)
	{
		super(ownerPlugin, className);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.util.ExtensionClassWrapper#getInstance()
	 */
	@Override
	public T getInstance()
	{
		try
		{
			return super.getInstance();
		}
		catch (Throwable t)
		{
			throw new InternalProxyConfigurationError(t);
		}
	}
}
