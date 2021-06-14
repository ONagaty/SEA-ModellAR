package eu.cessar.ct.core.mms.internal;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.ModelUtils.Service;

public class ModelUtilsProvider implements IServiceProvider<Service>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#getService(java.lang.Class)
	 */
	public Service getService(Class<Service> serviceClass, Object... args)
	{
		return SDKModelUtils.eINSTANCE;
	}
}
