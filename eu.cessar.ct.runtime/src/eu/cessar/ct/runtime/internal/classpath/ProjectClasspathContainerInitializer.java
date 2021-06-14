package eu.cessar.ct.runtime.internal.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.runtime.classpath.ProjectClasspathContainer;

/**
 * @uml.dependency 
 *                 supplier="eu.cessar.ct.runtime.classpath.ProjectClasspathContainer"
 *                 stereotypes="Standard::Create"
 */
public class ProjectClasspathContainerInitializer extends ClasspathContainerInitializer
{

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException
	{
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project},
			new IClasspathContainer[] {new ProjectClasspathContainer(project)},
			new NullProgressMonitor());
	}
}
