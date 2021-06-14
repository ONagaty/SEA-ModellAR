package eu.cessar.ct.runtime.internal.execution.pluget;

import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.execution.AbstractTaskManagerFactory;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;

/**
 * External pluget manager factory
 * 
 * @author uidg4449
 * 
 *         %created_by: uidg4449 %
 * 
 *         %date_created: Mon Mar 10 09:27:33 2014 %
 * 
 *         %version: 2 %
 */
public class PlugetExternalTaskManagerFactory extends AbstractTaskManagerFactory<URL>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#createManager(org.eclipse.core.resources.IProject,
	 * java.util.Map)
	 */
	@Override
	public ICessarTaskManagerImpl<URL> createManager(IProject project, Map<String, Object> map)
	{
		PlugetExternalTaskManager manager = new PlugetExternalTaskManager(getDescriptor(), project);
		return manager;
	}

}
