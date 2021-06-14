package eu.cessar.ct.core.mms;

import eu.cessar.ct.core.mms.internal.SDKSplitUtils;
import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.ct.sdk.utils.SplitUtils.IService;

/**
 * 
 * Provider for the SplitUtils service
 * 
 * @author uidl6870
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Wed Mar  5 15:58:57 2014 %
 * 
 *         %version: 1 %
 */
public class SplitUtilsProvider implements IServiceProvider<SplitUtils.IService>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#getService(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public IService getService(Class<IService> serviceClass, Object... args)
	{
		return SDKSplitUtils.INSTANCE;
	}

}
