package eu.cessar.ct.runtime.ecuc.internal.classpath;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.classpath.ICessarClasspathContributor;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.internal.cfs.util.CFSUtils;
import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * This contributor provides a CPEntry for each module that exists inside the
 * project and also a CPEntry for glue
 */
public class EcucModelClasspathContributor implements ICessarClasspathContributor
{
	private static final String EMFPROXY_PLUGIN_ID = "eu.cessar.ct.emfproxy";

	private IProject project;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#getClasspathEntries(org.eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public IClasspathEntry[] getClasspathEntries(List<IClasspathEntry> existingEntries,
		String containerID, IJavaProject javaProject)
	{
		if (true)
		{
			return new IClasspathEntry[0];
		}
		project = javaProject.getProject();
		checkModelFolder();

		if (!project.getFolder(CessarRuntime.HIDDEN_MODEL_FOLDER_NAME).exists())
		{
			return new IClasspathEntry[0];
		}
		List<IClasspathEntry> cpeList = new ArrayList<IClasspathEntry>();
		String uriRoot = CessarRuntime.CFS_SCHEME + "://" + javaProject.getElementName() + "/"
			+ CessarRuntime.HIDDEN_MODEL_FOLDER_NAME;

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project.getProject());

		List<GModuleDef> moduleDefs = ecucModel.getAllModuleDefs();
		for (GModuleDef module: moduleDefs)
		{
			String qualifiedName = MetaModelUtils.getAbsoluteQualifiedName(module);
			cpeList.add(JavaCore.newLibraryEntry(new Path(uriRoot + qualifiedName), null, null));
		}

		if (cpeList.size() > 0)
		{
			Path gluePath = new Path(uriRoot + "/" + CFSUtils.GLUE_FILE_NAME); //$NON-NLS-1$
			cpeList.add(JavaCore.newLibraryEntry(gluePath, null, null));
		}

		return cpeList.toArray(new IClasspathEntry[cpeList.size()]);
	}

	/**
	 * Recreates .model folder if necessary
	 */
	private void checkModelFolder()
	{
		IFolder modelFolder = project.getFolder(CessarRuntime.HIDDEN_MODEL_FOLDER_NAME);
		if (project.isAccessible() && !modelFolder.exists())
		{
			Job job = new WorkspaceJob("Creating .model folder")
			{
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException
				{
					if (!project.isAccessible())
					{
						return Status.CANCEL_STATUS;
					}
					IFolder modelFolder = project.getFolder(CessarRuntime.HIDDEN_MODEL_FOLDER_NAME);
					if (modelFolder.exists())
					{
						return Status.CANCEL_STATUS;
					}
					try
					{
						modelFolder.createLink(
							new URI(CessarRuntime.CFS_SCHEME + "://" + project.getName()), //$NON-NLS-1$
							IFolder.ALLOW_MISSING_LOCAL | IResource.BACKGROUND_REFRESH,
							new NullProgressMonitor());
					}
					catch (Exception e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}
					return Status.OK_STATUS;
				}

			};
			job.setRule(project);
			job.schedule();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#updateExecutionLoader(eu.cessar.ct.runtime.execution.IModifiableExecutionLoader)
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader)
	{
		IEcucPresentationModel pm = IEcucCore.INSTANCE.getEcucPresentationModel(executionLoader.getProject());
		executionLoader.setBinaryClassResolver(pm);
		// we will need also this plugin and it's dependencies during execution
		CollectionUtils.addNoDuplicates(executionLoader.getBundleIDs(), CessarPluginActivator.PLUGIN_ID);
	}
}
