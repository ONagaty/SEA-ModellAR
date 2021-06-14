package eu.cessar.ct.workspace.internal.logger;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.workspace.logging.LoggerFactoryImpl;

public class LoggerServiceProvider implements IServiceProvider<LoggerFactory.Service>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.util.IServiceProvider#createService(java.lang.Class)
	 */
	public LoggerFactory.Service getService(Class<LoggerFactory.Service> serviceClass,
		Object... args)
	{
		return LoggerFactoryImpl.getInstance();
	}

}
