package eu.cessar.ct.runtime.ecuc.internal;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.EcucUtils.Service;

public class EcucUtilsServiceProvider implements IServiceProvider<Service>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#getService(java.lang.Class)
	 */
	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return SDKEcucUtils.eINSTANCE;
	}
}
