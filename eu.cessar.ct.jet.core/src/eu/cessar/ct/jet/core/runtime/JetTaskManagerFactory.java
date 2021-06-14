package eu.cessar.ct.jet.core.runtime;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.execution.AbstractTaskManagerFactory;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;

public class JetTaskManagerFactory extends AbstractTaskManagerFactory<IFile>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#createManager(org.eclipse.core.resources.IProject, java.util.Map)
	 */
	public ICessarTaskManagerImpl<IFile> createManager(final IProject project,
		Map<String, Object> map)
	{
		return new JetTaskManager(getDescriptor(), project, map);
	}
}
