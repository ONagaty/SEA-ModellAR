package eu.cessar.ct.runtime.internal.execution;

import java.util.Map;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.execution.AbstractTaskManagerFactory;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;

public class JavaMainManagerFactory extends AbstractTaskManagerFactory<String>
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#createManager(org.eclipse.core.resources.IProject, java.util.Map)
	 */
	public ICessarTaskManagerImpl<String> createManager(IProject project, Map<String, Object> map)
	{
		JavaMainTaskManager manager = new JavaMainTaskManager(getDescriptor(), project);
		return manager;
	}

}
