package eu.cessar.ct.runtime.execution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.DefaultSourceContainer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.sourcelookup.containers.ClasspathVariableSourceContainer;
import org.eclipse.jdt.launching.sourcelookup.containers.JavaProjectSourceContainer;
import org.eclipse.jdt.launching.sourcelookup.containers.PackageFragmentRootSourceContainer;

import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * An abstract source lookup director for CESSAR-CT specific applications.
 * 
 */
public abstract class AbstractCessarSourceLookupDirector extends AbstractSourceLookupDirector
{

	/**
	 * 
	 * @return
	 */
	protected abstract String getProjectAttribute();

	private class CessarCompositeSourceContainer extends DefaultSourceContainer
	{
		private IProject project;

		public CessarCompositeSourceContainer(IProject project)
		{
			super();
			this.project = project;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.debug.core.sourcelookup.containers.DefaultSourceContainer#createSourceContainers()
		 */
		@Override
		protected ISourceContainer[] createSourceContainers() throws CoreException
		{
			List<ISourceContainer> containers = doGetSourceContainers(project);
			return containers.toArray(new ISourceContainer[containers.size()]);
		}

		// /* (non-Javadoc)
		// * @see
		// org.eclipse.debug.core.sourcelookup.containers.CompositeSourceContainer#findSourceElements(java.lang.String)
		// */
		// @Override
		// public Object[] findSourceElements(String name) throws CoreException
		// {
		// int index = name.lastIndexOf('.');
		// String ext = name.substring(index + 1);
		// if (ext.equals("java"))
		// {
		// String rest = name.substring(0, index);
		// name = rest.replace('.', '/') + '.' + ext;
		// }
		// else
		// {
		// name = name.replace('.', '/') + ".java";
		// }
		// return super.findSourceElements(name);
		// }
	}

	/**
	 * @param project
	 * @return
	 */
	protected List<ISourceContainer> doGetSourceContainers(IProject project)
	{
		IJavaProject javaProject = null;
		List<ISourceContainer> containers = new ArrayList<ISourceContainer>();

		if (project != null && project.isAccessible())
		{
			javaProject = JavaCore.create(project);
		}

		if (javaProject != null)
		{

			containers.add(new JavaProjectSourceContainer(javaProject));
			IClasspathEntry[] rawClasspath;
			try
			{
				rawClasspath = javaProject.getRawClasspath();
				for (IClasspathEntry entry: rawClasspath)
				{
					addSourceForDependencies(javaProject, entry, containers);
				}
			}
			catch (JavaModelException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

		}
		return containers;
	}

	/**
	 * @param javaProject
	 * @param entry
	 * @param containers
	 * @throws JavaModelException
	 */
	private void addSourceForDependencies(IJavaProject javaProject, IClasspathEntry entry,
		List<ISourceContainer> containers) throws JavaModelException
	{
		ISourceContainer sourceContainer = null;

		switch (entry.getEntryKind())
		{
			case IClasspathEntry.CPE_CONTAINER:
				IClasspathContainer container = JavaCore.getClasspathContainer(entry.getPath(),
					javaProject);
				IClasspathEntry[] classpathEntries = container.getClasspathEntries();
				for (IClasspathEntry iClasspathEntry: classpathEntries)
				{
					addSourceForDependencies(javaProject, iClasspathEntry, containers);
				}
				break;
			case IClasspathEntry.CPE_LIBRARY:
				IPackageFragmentRoot root = javaProject.findPackageFragmentRoot(entry.getPath());
				if (root != null)
				{
					sourceContainer = new PackageFragmentRootSourceContainer(root);
					sourceContainer.getName();
				}
				break;
			case IClasspathEntry.CPE_PROJECT:
				String name = entry.getPath().segment(0);
				IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
				if (p.exists())
				{
					IJavaProject jp = JavaCore.create(p);
					if (jp.exists())
					{
						sourceContainer = new JavaProjectSourceContainer(jp);
					}
				}
				break;
			case IClasspathEntry.CPE_VARIABLE:
				sourceContainer = new ClasspathVariableSourceContainer(entry.getPath());
				break;
			default:
				break;
		}

		if (sourceContainer != null)
		{
			for (ISourceContainer container: containers)
			{
				if (container.getName().equals(sourceContainer.getName()))
				{
					return;
				}
			}
			containers.add(sourceContainer);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector#initializeDefaults(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void initializeDefaults(final ILaunchConfiguration configuration) throws CoreException
	{
		dispose();
		setLaunchConfiguration(configuration);
		// add also the project as source container
		IProject project = null;
		try
		{
			String projectStr = configuration.getAttribute(getProjectAttribute(), ""); //$NON-NLS-1$
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectStr);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		setSourceContainers(new ISourceContainer[] {new CessarCompositeSourceContainer(project)});
		initializeParticipants();
	}
}
