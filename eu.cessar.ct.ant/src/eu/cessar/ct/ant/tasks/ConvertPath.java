package eu.cessar.ct.ant.tasks;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.osgi.util.NLS;

/**
 * Ant Task (for project that are not in compatibility mode) for obtaining
 * <ul>
 * <li>the workspace relative path of a resource given by it's absolute path</li>
 * <li>the absolute path of a resource given by it's workspace relative path.</li>
 * </ul>
 * <br/>
 * NOTE: If multiple workspace relative resources are found for a given
 * <code>filesystempath</code> and <code>projectname</code> is not set, a
 * {@link BuildException} is thrown indicating that in this case,
 * <code>projectname</code> argument is mandatory
 * 
 */
public class ConvertPath extends AbstractConvertPath
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.NON_COMPAT_TASK;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractConvertPath#doConvertFileSystemPath(org.eclipse.core.resources.IResource[])
	 */
	@Override
	protected void doConvertFileSystemPath(IResource[] resources)
	{
		if (resources.length == 1)
		{
			// if projectname or projectpath are set, we have to check that
			// found res is inside that project
			if (iProject != null)
			{
				IProject proj = resources[0].getProject();
				boolean isInProj = proj.getName().equals(iProject.getName());
				if (isInProj)
				{
					// 1 match, res inside project -> OK, set property
					getProject().setUserProperty(getProperty(),
						resources[0].getFullPath().toPortableString());
				}
				else
				{
					throw new BuildException(NLS.bind(
						"Found local resource {0} outside the given project {1} ", //$NON-NLS-1$
						new Object[] {resources[0].getFullPath(), iProject.getName()}));
				}
			}
			else
			{
				// 1 match, set property
				getProject().setUserProperty(getProperty(),
					resources[0].getFullPath().toPortableString());
			}

		}
		else if (resources.length > 1)
		{

			// multiple matches, in this case projectname must be set
			if (projectname == null)
			{

				throw new BuildException(
					NLS.bind(
						"project must be set, as more than one resource have been found inside the workspace for {0}", //$NON-NLS-1$
						getFilesystempath()));
			}

			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectname);
			if (project.isAccessible())
			{

				IResource target = null;
				for (IResource iRes: resources)
				{

					if (project == iRes.getProject())
					{
						if (target != null)
						{
							throw new BuildException(
								NLS.bind(
									"More then one linked resource has been found into the provided project for ", //$NON-NLS-1$
									getFilesystempath()));
						}
						target = iRes;
					}

				}
				if (target != null)
				{
					// OK, set property
					getProject().setUserProperty(getProperty(),
						target.getFullPath().toPortableString());
				}
			}
		}
	}

}
