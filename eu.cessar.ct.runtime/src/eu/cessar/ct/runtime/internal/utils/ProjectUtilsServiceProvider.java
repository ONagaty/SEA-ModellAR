package eu.cessar.ct.runtime.internal.utils;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.ProjectUtils;

public class ProjectUtilsServiceProvider implements IServiceProvider<ProjectUtils.Service>
{

	public ProjectUtils.Service getService(Class<ProjectUtils.Service> serviceClass, Object... args)
	{
		return ProjectUtilsServiceImpl.eINSTANCE;
	}

}
