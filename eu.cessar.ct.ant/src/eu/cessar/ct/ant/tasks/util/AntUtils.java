/**
 * 
 */
package eu.cessar.ct.ant.tasks.util;

import java.net.URI;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;

/**
 * Ant related utility methods
 */
public class AntUtils
{
	/**
	 * Return the project located at the path <code>projectPath</code>. If the
	 * path does not denote a project (this includes project not added to the
	 * workspace) it will throw <code>IllegalArgumentException</code>
	 * 
	 * @param projectPath
	 *        workspace relative project path
	 * @return
	 * 
	 */
	public static IProject getProject(final String projectPath)
	{
		IProject result = getProjectIfExists(projectPath);
		if (result == null)
		{
			IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
			String workspaceLoc = wsRoot.getLocation().toOSString();

			throw new IllegalArgumentException(
				NLS.bind(
					"No valid eclipse project could be found on the given path.\nCheck project path and workspace location.\nWorkspace location is {0}, given project path is {1}.", //$NON-NLS-1$
					new Object[] {workspaceLoc, projectPath}));
		}
		return result;
	}

	/**
	 * Return the project having given <code>projectName</code>. If the project
	 * cannot be located, it will throw an <code>IllegalArgumentException</code>
	 * 
	 * @param projectName
	 *        project name
	 * @return
	 */
	public static IProject getProjectByName(String projectName)
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = root.getProject(projectName);
		if (project.exists())
		{
			return project;
		}
		else
		{
			throw new IllegalArgumentException(NLS.bind(
				"No valid project with {0} name found inside the workspace", projectName)); //$NON-NLS-1$
		}
	}

	private static IProject getProjectIfExists(final String inputArg)
	{
		IProject result = null;
		IPath projectPath = new Path(inputArg).makeAbsolute();
		IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI uri = URIUtil.toURI(projectPath);
		IContainer[] containers = wsRoot.findContainersForLocationURI(uri);
		for (int i = 0; result == null && i < containers.length; i++)
		{
			IContainer container = containers[i];
			if (container instanceof IProject)
			{
				result = (IProject) container;
			}
		}
		return result;
	}
}
