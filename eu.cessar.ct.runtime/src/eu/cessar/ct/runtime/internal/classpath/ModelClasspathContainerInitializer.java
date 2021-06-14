package eu.cessar.ct.runtime.internal.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.runtime.classpath.ModelClasspathContainer;

/**
 * {@link ClasspathContainerInitializer} for the {@link ModelClasspathContainer} which contains the entries of the
 * CESSAR Model Libraries in the build path.
 *
 * @author uidg4098
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class ModelClasspathContainerInitializer extends ClasspathContainerInitializer
{

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException
	{
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project},
			new IClasspathContainer[] {new ModelClasspathContainer(project)}, new NullProgressMonitor());
	}

}
