package eu.cessar.ct.compat.ant.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.ant.tasks.AbstractConvertPath;

/**
 * /** Ant Task (for projects in compatibility mode) for obtaining
 * <ul>
 * <li>the workspace relative path of a resource given by it's absolute path</li>
 * <li>the absolute path of a resource given by it's workspace relative path.</li>
 * </ul>
 * <br/>
 */
public class CompatConvertPath extends AbstractConvertPath
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ant.tasks.ConvertPath#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.COMPAT_TASK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ant.tasks.AbstractConvertPath#doConvertFileSystemPath(org.eclipse.core.resources.IResource[])
	 */
	@Override
	protected void doConvertFileSystemPath(IResource[] resources)
	{
		if (resources.length == 1)
		{
			// if projectpath or CESSAR_PROJECT_NAME are set, we have to check
			// that the found res is inside that project
			if (iProject != null)
			{
				IProject proj = resources[0].getProject();
				boolean isInProj = proj.getName().equals(iProject.getName());
				if (isInProj)
				{
					// 1 match, res inside project -> OK, set property
					getProject().setUserProperty(getProperty(), resources[0].getFullPath().toPortableString());
				}
				else
				{
					// do nothing
				}
			}
			else
			{
				// 1 match, set property
				getProject().setUserProperty(getProperty(), resources[0].getFullPath().toPortableString());
			}

		}
		else if (resources.length > 1)
		{
			// projectPath is set
			if (iProject != null)
			{

				boolean found = false;
				String foundPath = null;
				for (IResource iRes: resources)
				{

					if (iProject == iRes.getProject())
					{
						// OK (assume we don't have more than 1 linked resource
						// in the same
						// project) ->set property
						if (!found)
						{
							foundPath = iRes.getFullPath().toPortableString();
							getProject().setUserProperty(getProperty(), foundPath);
						}
						else
						{
							// log a warning, the same resource is found
							// multiple time in the same
							// project
							log(NLS.bind(
								"Multiple instances of {0} found inside the project {1}.  The first instance is returned.", //$NON-NLS-1$
								getFilesystempath(), iProject.getName()));
							break;
						}
						found = true;
					}
				}
			}
			// projectPath not set -> simply set the property with the first
			// resource found
			else
			{
				String foundPath = resources[0].getFullPath().toPortableString();
				getProject().setUserProperty(getProperty(), foundPath);
				log(NLS.bind("Multiple instances of {0} found inside the project {1}. Returned {2}.", //$NON-NLS-1$
					new Object[] {getFilesystempath(), iProject.getName(), foundPath}));
			}
		}
	}
}
