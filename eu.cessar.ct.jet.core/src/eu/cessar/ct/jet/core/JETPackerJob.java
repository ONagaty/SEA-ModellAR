package eu.cessar.ct.jet.core;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.core.platform.util.JarBuilder;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.jet.core.compiler.IJETServicesProvider;
import eu.cessar.ct.jet.core.compiler.JETServicesProviderFactory;
import eu.cessar.ct.jet.core.compliance.EJetComplianceLevel;
import eu.cessar.ct.jet.core.debug.JetTemplateBreakpointManager;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.jet.core.internal.JETCoreMessages;
import eu.cessar.ct.jet.core.internal.compiler.Java2JetClassTransformer;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * This class can be used to compile and pack specified JET files into JAR files. The client classes must set the KET
 * files to be compiled and packed using provided methods.
 */
public class JETPackerJob extends WorkspaceJob
{
	/** internal array that keeps the JAR files to be compiled into JARs */
	private List<IFile> jetFilesList;
	private final IProject project;

	private boolean inDebugMode;
	private EJetComplianceLevel complianceLevel;
	private IStatus packagingResult = Status.OK_STATUS;
	/** internal map that keeps the files to be compiled and the jars to be overridden and are readOnly */
	private Map<IFile, IFile> readOnlyFilesMap;

	/**
	 * Default constructor
	 *
	 * @param project
	 * @param complianceLevel
	 */
	public JETPackerJob(IProject project, EJetComplianceLevel complianceLevel)
	{
		super("JET 2 JAR job"); //$NON-NLS-1$
		this.project = project;
		this.complianceLevel = complianceLevel;

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// set scheduling rule if specified
		setRule(root);
		try
		{
			IJavaProject javaProject = JavaCore.create(project);
			IFolder saveFolder = JETCoreUtils.getDumpJavaSourceFolderPref(project);
			createSaveFolderIfNeededAndBuildProject(saveFolder);
			boolean sourceFolderIsAdded = false;
			IClasspathEntry[] oldEntries;

			oldEntries = javaProject.getRawClasspath();

			for (IClasspathEntry iClasspathEntry: oldEntries)
			{
				if (iClasspathEntry.getPath().equals(saveFolder.getFullPath()))
				{
					sourceFolderIsAdded = true;
				}
			}
			if (!sourceFolderIsAdded)
			{
				// +1 for our src/main/java entry
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];

				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);

				IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(saveFolder);
				newEntries[oldEntries.length] = JavaCore.newSourceEntry(packageRoot.getPath(), new Path[] {});
				javaProject.setRawClasspath(newEntries, null);
			}
		}
		catch (JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

	}

	private synchronized void createSaveFolderIfNeededAndBuildProject(IFolder saveFolder)
	{
		if (saveFolder == null || !saveFolder.exists())
		{
			try
			{
				ResourceUtils.mkDirs(saveFolder, null);

			}
			catch (CoreException e)
			{
				LoggerFactory.getLogger().log(e);
			}
		}
	}

	/**
	 * !!!!!!!!
	 *
	 * @param inDebugMode
	 */
	public void setDebugMode(boolean inDebugMode)
	{
		this.inDebugMode = inDebugMode;
	}

	/**
	 * Allow users to add a JET file to be packed
	 *
	 * @param jfile
	 *        a JET file instance to be packed
	 */
	public void addFile(final IFile jfile)
	{
		if (jetFilesList == null)
		{
			jetFilesList = new ArrayList<>();
		}
		jetFilesList.add(jfile);
	}

	/**
	 * Allow clients to specify a list of JET files to be packed
	 *
	 * @param files
	 *        an JET files array to be be packed
	 */
	public void setJetFiles(final List<IFile> files)
	{
		jetFilesList = files;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.jobs.Job#shouldSchedule()
	 */
	@Override
	public boolean shouldSchedule()
	{
		return true;
	}

	/**
	 *
	 * @return read-only jars and files that cannot be compiled
	 */
	public Map<IFile, IFile> getReadOnlyJarsMap()
	{
		if (readOnlyFilesMap == null)
		{
			readOnlyFilesMap = new HashMap<>();
		}
		return readOnlyFilesMap;
	}

	/**
	 * @return status
	 */
	public IStatus getPackagingResult()
	{
		return packagingResult;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.resources.WorkspaceJob#runInWorkspace(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException
	{
		IStatus result = Status.OK_STATUS;
		IStatus status;
		// report if there are no JET files to be packed
		if ((jetFilesList == null) || (jetFilesList.size() <= 0))
		{
			status = new Status(IStatus.OK, CessarPluginActivator.PLUGIN_ID, JETCoreMessages.error_NoJetFiles);
			LoggerFactory.getLogger().log(result);
			return status;
		}

		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, jetFilesList.size());
		int workUnits = 0;

		// initialize or reinitialize readOnlyFilesMap
		readOnlyFilesMap = new HashMap<>();

		// iterate for all JET files within internal array
		for (IFile jetFile: jetFilesList)
		{
			subMonitor.setTaskName(JETCoreMessages.format(JETCoreMessages.task_CompilingJet, jetFile.getName()));

			try
			{
				IFile jarFile;
				if (inDebugMode)
				{
					jarFile = JETCoreUtils.getDebugJarFile(jetFile);
				}
				else
				{
					// compute the JAR file where current JET is compiled
					jarFile = JETCoreUtils.getJarFile(jetFile);
				}
				if (jarFile.isReadOnly())
				{
					// JAR file is read-only, so don't touch it
					// LoggerFactory.getLogger().error(JETCoreMessages.error_ReadOnlyJAR);
					status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, JETCoreConstants.READ_ONLY,
						JETCoreMessages.format(JETCoreMessages.error_ReadOnlyJAR, jarFile.getName()), null);
					LoggerFactory.getLogger().log(status);
					result = PlatformUtils.combineStatus(result, status);

					// add this read-only jar and the jet file to the map
					readOnlyFilesMap.put(jarFile, jetFile);
				}
				else
				{
					// create a JET services provider instance, compile current
					// JET file and update
					// the problem markers for this JET file
					IJETServicesProvider jetSProvider = JETServicesProviderFactory.createJETServicesProvider(jetFile,
						complianceLevel);

					jetSProvider.compile(inDebugMode, monitor);
					IJavaProject create = JavaCore.create(project);
					IPath outputLocation = create.getOutputLocation();
					String lastSegment = outputLocation.lastSegment();
					IFolder folder = project.getFolder(lastSegment);
					if (folder != null)
					{
						folder.refreshLocal(IResource.DEPTH_INFINITE, subMonitor);
					}
					// pack current JET file only if there are no compilation
					// problems
					if (jetSProvider.isValid())
					{
						IPath fullPath = jetFile.getFullPath();
						IPath relativeFullPath = fullPath.makeRelative();
						LoggerFactory.getLogger().log(new Status(IStatus.INFO, CessarPluginActivator.PLUGIN_ID,
							JETCoreMessages.format(JETCoreMessages.task_Completed,
								relativeFullPath.toString()/*
															 * .getName ()
															 */, jetSProvider.getProblems().size(), jarFile.getName())));

						// if JET file is linked resource, JAR file should be as
						// well.
						if (jetFile.isLinked())
						{
							IPath iPath = jetFile.getLocation();
							IPath jarFolderPath = iPath.removeLastSegments(1);
							IPath jarFilePath = jarFolderPath.append(jarFile.getName());
							jarFile.createLink(jarFilePath, IResource.ALLOW_MISSING_LOCAL | IResource.REPLACE, monitor);
						}

						// the message for writing .jar
						LoggerFactory.getLogger().log(new Status(IStatus.INFO, CessarPluginActivator.PLUGIN_ID,
							JETCoreMessages.format(JETCoreMessages.task_WriteJar,
								jarFile.getFullPath().makeRelative().toString(), jetSProvider.getProblems().size(),
								jarFile.getName())));
						IResourceChangeListener listener = new IResourceChangeListener()
						{

							@Override
							public void resourceChanged(IResourceChangeEvent event)
							{
								try
								{
									writeJar(jetFile, jarFile, jetSProvider, subMonitor);
									ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
								}
								catch (Exception e)
								{
									CessarPluginActivator.getDefault().logError(e);
								}
							}
						};
						ResourcesPlugin.getWorkspace().addResourceChangeListener(listener,
							IResourceChangeEvent.POST_BUILD);

						result = PlatformUtils.combineStatus(result, Status.OK_STATUS);

					}
					else
					{
						// in log, report that the JET file could not be
						// compiled; see Problems view
						status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
							JETCoreMessages.format(JETCoreMessages.error_JETCompilationProblems, jetFile.getName()));
						LoggerFactory.getLogger().log(status);
						result = PlatformUtils.combineStatus(result, status);
					}
				}
			}
			catch (Exception e)
			{
				// exception encountered during compilation of current JAR file

				LoggerFactory.getLogger().log(e);
				status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
					JETCoreMessages.format(JETCoreMessages.error_JETCompilationProblems, jetFile.getName()));
				result = PlatformUtils.combineStatus(result, status);

			}

			// cancel JET compilation problem if user requested
			if (subMonitor.isCanceled())
			{
				subMonitor.done();

				result = PlatformUtils.combineStatus(result, Status.CANCEL_STATUS);
			}
			subMonitor.worked(++workUnits);
		}
		subMonitor.done();
		packagingResult = result;
		return Status.OK_STATUS;
	}

	/**
	 * This method fill the content of the JAR file with Java classes compiled out of JET file
	 *
	 * @param jetFile
	 *        the JET file
	 * @param jarFile
	 *        the JAR file to be updated with compiled code
	 * @param jetSProvider
	 *        an {@link IJETServicesProvider} instance
	 * @param monitor
	 *        a progress monitor instance
	 * @throws Exception
	 *         An exception is thrown if the JAR cannot be updated.
	 */
	private void writeJar(final IFile jetFile, final IFile jarFile, final IJETServicesProvider jetSProvider,
		final IProgressMonitor monitor) throws Exception
	{
		final IClassFile[] classFiles = jetSProvider.getClassFiles();
		EJetExecutionMode execMode = jetSProvider.getExecutionMode(monitor);
		if ((classFiles != null) && (classFiles.length > 0))
		{
			String rootClassQName = jetSProvider.getJETClassName();
			JarBuilder jarBuilder = new JarBuilder(jarFile);
			IFile javaFile = null;

			if (inDebugMode)
			{
				// add also the source for the java file
				javaFile = jetSProvider.getJavaFile(monitor);
				Assert.isNotNull(javaFile);

				JetTemplateBreakpointManager breakpointManager = JETCoreUtils.getBreakpointManager();
				breakpointManager.addJavaBreakpoints(jetFile, jetSProvider);

				IPath javaFilePath = javaFile.getProjectRelativePath().removeFirstSegments(1);
				jarBuilder.addToJar(javaFilePath.toString(), javaFile, monitor);

			}

			String javaPackage = jetSProvider.getJavaPackage();
			rootClassQName = javaPackage + "." + jetSProvider.getJETClassName(); //$NON-NLS-1$
			for (IClassFile classFile: classFiles)
			{

				String className = javaPackage.replace(".", "/") + "/" + classFile.getElementName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ByteArrayInputStream classStream = new ByteArrayInputStream(classFile.getBytes());

				if (!inDebugMode)
				{ // apply instrumentation
					ClassWriter cw = new ClassWriter(0);
					Java2JetClassTransformer ca = new Java2JetClassTransformer(cw, jetSProvider, jetFile);
					ClassReader cr = new ClassReader(classStream);
					cr.accept(ca, 0);
					byte[] newClass = cw.toByteArray();
					classStream = new ByteArrayInputStream(newClass);
				}

				jarBuilder.addToJar(className, classStream, monitor);
				classStream.close();
			}

			// update JAR manifest
			setJarManifest(jarBuilder.getManifest(), rootClassQName, execMode);
			jarBuilder.done(monitor);

		}

	}

	/**
	 * @param monitor
	 * @param javaFile
	 * @throws CoreException
	 */
	@SuppressWarnings("unused")
	private void deleteDumpedJavaSourceHierarchy(IResource file, IProgressMonitor monitor) throws CoreException
	{
		if (file.getType() == IResource.FOLDER)
		{
			IFolder folder = (IFolder) file;
			if (folder.members().length == 0)
			{
				ResourceUtils.doubleTryDelete(folder, true, monitor);
				deleteDumpedJavaSourceHierarchy(file.getParent(), monitor);
			}
		}
		else
		{
			if (file.getType() == IResource.FILE)
			{
				file.delete(true, monitor);
				deleteDumpedJavaSourceHierarchy(file.getParent(), monitor);
			}
		}

	}

	private void setJarManifest(final Manifest manifest, final String classQName, EJetExecutionMode executionMode)
	{
		manifest.getMainAttributes().put(new Attributes.Name(JETCoreConstants.MANIFEST_ATTR_JET_CLASS), classQName);
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);
		String modelVersion = "unknown"; //$NON-NLS-1$
		if (autosarRelease != null)
		{
			modelVersion = autosarRelease.getNamespace();
		}

		// make current manifest
		manifest.getMainAttributes().put(new Attributes.Name(JETCoreConstants.MANIFEST_ATTR_TOOL_VERSION),
			PlatformConstants.PRODUCT_VERSION);
		manifest.getMainAttributes().put(new Attributes.Name(JETCoreConstants.MANIFEST_ATTR_METAMODEL_VERSION),
			modelVersion);
		manifest.getMainAttributes().put(new Attributes.Name(JETCoreConstants.MANIFEST_ATTR_EXECUTION_MODE),
			executionMode.name());
		manifest.getMainAttributes().put(new Attributes.Name(JETCoreConstants.MANIFEST_ATTR_COMPLIANCE),
			complianceLevel.name());
	}
}
