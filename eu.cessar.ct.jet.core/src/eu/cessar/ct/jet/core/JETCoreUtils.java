package eu.cessar.ct.jet.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.jet.core.compliance.EJetComplianceLevel;
import eu.cessar.ct.jet.core.debug.JetTemplateBreakpointManager;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.CodegenPreferencesAccessor;
import eu.cessar.ct.sdk.utils.ProjectUtils;

/**
 * JET Core utilities class.
 */
@SuppressWarnings("nls")
public final class JETCoreUtils
{
	/**
	 * Spaces constant.
	 */
	public static final String SPACES = "        ";
	private static JetTemplateBreakpointManager breakPointManagerEnhancer;
	private static final Pattern NL_PATTERN = Pattern.compile("([\\n][\\r]?|[\\r][\\n]?)", Pattern.MULTILINE);
	private static final String NL = System.getProperties().getProperty("line.separator");

	/**
	 * This method should verify with License manager engine if the JET feature is enabled according with installed
	 * license
	 *
	 * @return <code>true</code> if JET feature is enable, <code>false</code> otherwise.
	 */
	public static boolean isLicensed()
	{
		// if (!MosartLicenseHandler.getInstance().checkLicenseAndInstall(
		// MosartLicenseConstants.PRODUCT_CESSAR,
		// MosartLicenseConstants.MODULE_CODEGENERATORFRAMEWORK))
		return true;
	}

	/**
	 * Computes the JAR file based on specified JET file.
	 *
	 * @param jetFile
	 *        JET file for which JAR file must be computed
	 * @return An {@link IFile} instance where the JET file to be packed; must not return <code>null</code>.
	 * @throws CoreException
	 */
	public static IFile getJarFile(final IFile jetFile)
	{
		// check is the real name of the jet file has been given
		String jetName = jetFile.getName();

		if (!jetFile.exists())
		{
			File realJetFile = jetFile.getLocation().toFile();
			try
			{
				File canonicalFile = realJetFile.getCanonicalFile();
				if (canonicalFile != null)
				{
					jetName = canonicalFile.getName();
				}
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

		}

		String jarName = getJarFileName(jetName);
		IContainer jetParentFolder = jetFile.getParent();

		return jetParentFolder.getFile(new Path(jarName));
	}

	/**
	 * Computes the JAR file for debugging purposes based on specified JET file.
	 *
	 * @param jetFile
	 *        JET file for which JAR file must be computed
	 * @return the JAR file for debugging purposes
	 */
	public static IFile getDebugJarFile(IFile jetFile)
	{
		String jarFileName = getJarFileName(jetFile.getName());
		IFolder parentFolder = getDumpJavaSourceFolderPref(jetFile.getProject());
		IPath jarPath = new Path(jarFileName).removeFileExtension().addFileExtension("dbgjar"); //$NON-NLS-1$
		IFile jarFile = parentFolder.getFile(jarPath);
		return jarFile;
	}

	/**
	 * Returns the preference value related to whether to dump the java source or not.
	 *
	 * @param project
	 * @return the preference value
	 */
	public static boolean getDumpJavaSourcePref(IProject project)
	{

		return CodegenPreferencesAccessor.isDumpingJetSource(project);

		// boolean saveJava = false;
		// if (project != null)
		// {
		// ProjectScope projectScope = new ProjectScope(project);
		// IEclipsePreferences projectPreferences =
		// projectScope.getNode(Activator.PLUGIN_ID);
		// String pref =
		// projectPreferences.get(JETCoreConstants.PREF_DUMP_JAVA_SOURCE, null);
		// if (pref != null)
		//
		// {
		// saveJava = Boolean.parseBoolean(pref);
		//
		// }
		// }
		// return saveJava;
	}

	/**
	 * Returns the preference value corresponding to the parent folder where to dump the java source.
	 *
	 * @param project
	 * @return preference value
	 */
	public static IFolder getDumpJavaSourceFolderPref(IProject project)
	{
		try
		{
			// IPath folder =
			// CodegenPreferencesAccessor.getResolvedDumpJetSourceFolder(project);
			// return project.getParent().getFolder(folder);

			IPath folder = CodegenPreferencesAccessor.getResolvedDumpJetSourceFolder(project);
			String folderFullPath = folder.toString();
			int index = folderFullPath.indexOf(project.getName());
			if (index != -1)
			{
				String folderRelativePath = folderFullPath.substring(index + 1 + project.getName().length());
				return project.getFolder(folderRelativePath);
			}
			return project.getFolder(".src");

		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			// using .src as dump folder
			return project.getFolder(".src");
		}

	}

	/**
	 * Get the breakpoint manager
	 *
	 * @return breakpoint manager
	 */
	public static synchronized JetTemplateBreakpointManager getBreakpointManager()
	{
		if (breakPointManagerEnhancer == null)
		{
			breakPointManagerEnhancer = new JetTemplateBreakpointManager(
				DebugPlugin.getDefault().getBreakpointManager());
		}
		return breakPointManagerEnhancer;
	}

	/**
	 * Computes the JAR file name based on the specified name of the JET file.
	 *
	 * @param jetFileName
	 * @return JAR file name
	 */
	public static String getJarFileName(String jetFileName)
	{
		String jarFilename = "";
		int index = jetFileName.lastIndexOf(".");
		if (index > -1)
		{
			jarFilename = jetFileName.substring(0, index);
		}
		jarFilename += "_" + computeJarFileNamePart(jetFileName);
		String jarName = jarFilename + "." + JETCoreConstants.JAR;
		return jarName;
	}

	/**
	 * @return
	 */
	private static String computeJarFileNamePart(String name)
	{
		String extension = "";
		int index = name.lastIndexOf(".");
		if (index > -1)
		{
			extension = name.substring(index + 1);
		}
		if (extension.indexOf(JETCoreConstants.JET) > -1)
		{
			extension = extension.substring(0, extension.indexOf(JETCoreConstants.JET));
		}
		return extension;
	}

	/**
	 * Return the name of the output file from a JET file name. The <code>jetOrJarFileName</code> parameter must
	 * represent the name of a jet or a jar that has been made for a jet file, otherwise the method will return
	 * <code>null</code>
	 *
	 * @param jetOrJarFileName
	 *
	 * @param jetFile
	 *        the name (including extension) of the JET/jar file to determine output file name
	 * @return The name of the output file for the JET, or <code>null</code> if this name cannot be determined.
	 */
	public static String getJetOutputFileName(final String jetOrJarFileName)
	{
		int i = jetOrJarFileName.lastIndexOf(".");
		if (i == -1)
		{
			return null;
		}
		String ext = jetOrJarFileName.substring(i + 1, jetOrJarFileName.length());
		String fName = jetOrJarFileName.substring(0, i);
		if (ext.endsWith(JETCoreConstants.JET))
		{
			if (ext.equals(JETCoreConstants.JET))
			{
				// simply return the name of the jet (without extension)
				return fName;
			}
			else
			{
				ext = ext.substring(0, ext.length() - JETCoreConstants.JET.length());
				return fName + "." + ext;
			}
		}
		else
		{
			int index = fName.lastIndexOf("_");
			if (index > -1)
			{
				if (fName.length() > index + 1)
				{
					// test_c -> test.c
					return fName.substring(0, index) + "." + fName.substring(index + 1);
				}
				else
				{
					// no extension
					// test_ ->test
					return fName.substring(0, index);
				}
			}
			// simply return the name of the jar (without extension)
			return fName;
		}
	}

	/**
	 * Return the binary name of the jet stored inside the given <code>jarFile</code>
	 *
	 * @param jarFile
	 * @return binary name of the jet stored inside the given jarFile
	 * @throws IOException
	 */
	public static String getJetBinaryName(IFile jarFile) throws IOException
	{
		return getJetBinaryName(jarFile.getLocation().toFile());
	}

	/**
	 * Return the binary name of the jet stored inside the jar from the <code>jarLocation</code> location
	 *
	 * @param jarLocation
	 * @return binary name of the jet stored inside the jar from the specified location
	 * @throws IOException
	 */
	public static String getJetBinaryName(File jarLocation) throws IOException
	{
		String binaryName = null;
		if (jarLocation != null)
		{
			JarFile jar = new JarFile(jarLocation);
			try
			{
				Manifest manifest = jar.getManifest();
				if (manifest != null)
				{
					Attributes mainAttributes = manifest.getMainAttributes();
					binaryName = mainAttributes.getValue(JETCoreConstants.MANIFEST_ATTR_JET_CLASS);
				}
			}
			finally
			{
				jar.close();
			}
		}
		return binaryName;
	}

	/**
	 * Utility method that allow clients to update the markers on a specified JET file.
	 *
	 * @param jetFile
	 *        an {@link IFile} instance of the JET no which markers must be updated
	 * @param jetProblems
	 * @param monitor
	 *        a progress monitor instance
	 * @throws CoreException
	 *         thrown when the method fails; the reasons might be invalid or unaccessible JET file.
	 */
	public static void updateJETMarkers(final IFile jetFile, final List<IProblem> jetProblems) throws CoreException
	{
		// remove old problem markers from specified JET file
		jetFile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);

		// create a marker for each found problem
		for (IProblem iProblem: jetProblems)
		{
			IMarker marker = jetFile.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.LINE_NUMBER, iProblem.getSourceLineNumber());
			marker.setAttribute(IMarker.CHAR_START, iProblem.getSourceStart());
			marker.setAttribute(IMarker.CHAR_END, iProblem.getSourceEnd());
			marker.setAttribute(IMarker.MESSAGE, iProblem.getMessage());
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);

			if (iProblem.isError())
			{
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			}
			else if (iProblem.isWarning())
			{
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			}
			else
			{
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			}
		}
	}

	/**
	 * Return true if the <code>file</code> is a Jet file. The checking is done by verifying the content type of the
	 * file
	 *
	 * @param file
	 * @return true if the file is a Jet file
	 */
	public static boolean isJetFile(IFile file)
	{
		if (file != null)
		{
			try
			{
				IContentDescription description = file.getContentDescription();
				if (description != null)
				{
					IContentType contentType = description.getContentType();
					if ((contentType != null) && (JETCoreConstants.JET_CONTENT_TYPE_ID.equals(contentType.getId())))
					{
						return true;
					}
				}
			}
			catch (CoreException e)
			{
				// log and ignore, will return false
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		return false;
	}

	/**
	 * Return true if the <code>file</code> is a java archive containing a compiled jet
	 *
	 * @param file
	 * @return true if the file is a java archive containing a compiled jet
	 * @throws IOException
	 */
	public static boolean isJetJarFile(IFile file) throws IOException
	{
		if (file != null && file.getFileExtension() != null)
		{
			String fExt = file.getFileExtension().toLowerCase();
			if (JETCoreConstants.JAR.equals(fExt))
			{
				return getJetBinaryName(file) != null;
			}
		}
		return false;
	}

	/**
	 * Method that recursively collect JET files from specified {@link IResource} containers.
	 *
	 * @param resources
	 *
	 * @param containers
	 *        the array with {@link IResource} entities
	 * @param collectedFiles
	 *        the list of collected files
	 * @throws CoreException
	 */
	public static void collectJETFiles(final Object resources[], List<IFile> collectedFiles) throws CoreException
	{
		if (collectedFiles == null)
		{
			collectedFiles = new ArrayList<IFile>();
		}

		try
		{
			collectJetOrJarFiles(resources, collectedFiles, JETCoreConstants.JET);
		}
		catch (IOException e)
		{
			// actually, such exception will never be thrown when invoking
			// collectJetOrJarFiles method with 'jet' argument
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Method that recursively collect JAR files that contain compiled JETs from specified {@link IResource} resources.
	 *
	 * @param resources
	 * @param collectedFiles
	 *
	 * @param list
	 * @throws IOException
	 * @throws CoreException
	 */
	public static void collectCompiledJETFiles(final Object resources[], List<IFile> collectedFiles)
		throws CoreException, IOException
	{
		if (collectedFiles == null)
		{
			collectedFiles = new ArrayList<IFile>();
		}

		collectJetOrJarFiles(resources, collectedFiles, JETCoreConstants.JAR);
	}

	/**
	 * Collects the jets or the jars representing compiled jets, according to the <code>typeToCollect</code> parameter,
	 * from the given <code>resources</code>. <br>
	 * First, it checks that the given resources do not reside in folders which must be ignored (like the hidden and the
	 * output folders). Then, it calls {@link #doCollectJetOrJarFiles(Object[], List, String)} which recursively gathers
	 * the files from the sub-folders.
	 *
	 * @param resources
	 *        the resources from which to collect the JET or the JARs representing compiled jets
	 * @param collectedFiles
	 *        list to be populated
	 * @param fileTypeToCollect
	 *        {@link JETCoreConstants#JET} or {@link JETCoreConstants#JAR}
	 * @throws CoreException
	 * @throws IOException
	 *         thrown when a corrupt jar is encountered during the acquisition; this could be thrown only when the
	 *         method is invoked with {@link JETCoreConstants#JAR} argument
	 */
	private static void collectJetOrJarFiles(final Object resources[], List<IFile> collectedFiles,
		String fileTypeToCollect) throws CoreException, IOException
	{
		for (Object resource: resources)
		{
			if (resource instanceof IFile)
			{
				if (residesInIgnoredFolder((IFile) resource))
				{
					continue;
				}
				else
				{
					collectFile((IFile) resource, collectedFiles, fileTypeToCollect);
				}
			}
			else if (resource instanceof IContainer)
			{
				IContainer container = (IContainer) resource;

				if (container instanceof IFolder)
				{
					// ignore output and hidden folders
					if (shouldIgnore((IFolder) container))
					{
						continue;
					}

					if (residesInIgnoredFolder(container))
					{
						continue;
					}
				}

				// current container must not be ignored, so recursively collect
				// the jets from it
				doCollectJetOrJarFiles(container.members(), collectedFiles, fileTypeToCollect);
			}

		}

	}

	/**
	 * Return whether the given <code>resource</code> resides in a folder that must be ignored during the gathering of
	 * the JET files. <br>
	 * E.g: /.bin/jets/my.cjet
	 *
	 * @param resource
	 *        whose parent containers must be inspected
	 * @throws JavaModelException
	 * @return
	 */
	private static boolean residesInIgnoredFolder(IResource resource) throws JavaModelException
	{
		// ignore sub-folders that reside in output and hidden
		// folders
		IContainer parentContainer = resource.getParent();

		while (parentContainer instanceof IFolder)
		{
			if (shouldIgnore((IFolder) parentContainer))
			{
				return true;
			}
			parentContainer = parentContainer.getParent();
		}

		return false;
	}

	/**
	 *
	 * @param resources
	 * @param collectedFiles
	 * @param fileTypeToCollect
	 *        {@link JETCoreConstants#JAR} or {@link JETCoreConstants#JET }
	 * @throws CoreException
	 * @throws IOException
	 */
	private static void doCollectJetOrJarFiles(final Object resources[], List<IFile> collectedFiles,
		final String fileTypeToCollect) throws CoreException, IOException
	{
		for (Object resource: resources)
		{
			if (resource instanceof IFile)
			{
				collectFile((IFile) resource, collectedFiles, fileTypeToCollect);
			}
			else if (resource instanceof IContainer)
			{
				IContainer container = (IContainer) resource;
				if (container instanceof IFolder)
				{
					// ignore output and hidden folders
					if (shouldIgnore((IFolder) container))
					{
						continue;
					}
				}

				doCollectJetOrJarFiles(((IContainer) resource).members(), collectedFiles, fileTypeToCollect);
			}
		}

	}

	/**
	 * Returns <code>true</code> if the passed folder shall be ignored during the collection of the JET files or the JAR
	 * files representing compiled JETs. <br>
	 * This happens if the folder is either: <li>the default output location of the project <br>
	 * or <li>a custom output location</li> <br>
	 * or <li>a hidden folder (i.e its name starts with a dot: <b>.src</b>)</li>
	 *
	 * @param folder
	 *        the folder from which to collect or to ignore the *.jet files
	 * @throws JavaModelException
	 *
	 * @return <code>true</code> if the passed folder is hidden or represents an output location, <code>false</code>
	 *         otherwise
	 */
	private static boolean shouldIgnore(IFolder folder) throws JavaModelException
	{
		// check if it's a hidden folder
		boolean isHidden = folder.getName().startsWith(".");
		if (isHidden)
		{
			return true;
		}

		IProject project = folder.getProject();
		IJavaProject javaProject = JavaCore.create(project);

		// check if it's the default output location
		IPath outputLocation = javaProject.getOutputLocation();
		if (outputLocation.equals(folder.getFullPath()))
		{
			return true;
		}

		// check if it's a custom output folder
		IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();
		for (IClasspathEntry entry: classpathEntries)
		{
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE)
			{
				IPath customOutputLocation = entry.getOutputLocation();
				if (customOutputLocation != null)
				{
					if (folder.getFullPath().equals(customOutputLocation))
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Adds the given <code>file</code> to the list of <code>collectedFiles</code> if the file conforms to the
	 * <code>fileTypeToCollect</code>
	 *
	 * @param collectedFiles
	 * @param fileTypeToCollect
	 * @param file
	 * @throws IOException
	 */
	private static void collectFile(IFile file, List<IFile> collectedFiles, final String fileTypeToCollect)
		throws IOException
	{
		// add given IFile only if has JET content type
		if (fileTypeToCollect.equals(JETCoreConstants.JET))
		{
			if (JETCoreUtils.isJetFile(file))
			{
				collectedFiles.add(file);
			}
		}
		// or it's a compiled jet
		else if (fileTypeToCollect.equals(JETCoreConstants.JAR))
		{
			if (JETCoreUtils.isJetJarFile(file))
			{
				collectedFiles.add(file);
			}
		}
	}

	/**
	 * Return the execution mode specified by the <code>jetJarFile</code>. If this cannot be determined,
	 * {@link EJetExecutionMode#PARALEL PARALEL} will be returned.
	 *
	 * @param jetJarIFile
	 *
	 * @param input
	 * @return execution mode specified by the <code>jetJarFile</code>
	 */
	public static EJetExecutionMode getJetJarExecutionMode(IFile jetJarIFile)
	{
		if (jetJarIFile == null || !jetJarIFile.isAccessible() || jetJarIFile.getLocation() == null)
		{
			return EJetExecutionMode.PARALLEL;
		}
		else
		{
			File file = jetJarIFile.getLocation().toFile();
			try
			{
				JarFile jar = new JarFile(file);
				try
				{
					Manifest manifest = jar.getManifest();
					if (manifest != null)
					{
						Attributes mainAttributes = manifest.getMainAttributes();
						String execModeStr = mainAttributes.getValue(JETCoreConstants.MANIFEST_ATTR_EXECUTION_MODE);
						if (execModeStr != null)
						{
							try
							{
								return EJetExecutionMode.valueOf(execModeStr);
							}
							catch (IllegalArgumentException ex)
							{
								CessarPluginActivator.getDefault().logError(ex);
							}
						}
					}
				}
				finally
				{
					jar.close();
				}
			}
			catch (IOException ex)
			{
				CessarPluginActivator.getDefault().logError(ex);
			}
		}
		// at this point we failed to read the value from the jar, just return
		// paralel
		return EJetExecutionMode.PARALLEL;
	}

	/**
	 * @param project
	 * @return jet compliance level
	 */
	public static EJetComplianceLevel getJetComplianceLevel(IProject project)
	{
		ECompatibilityMode mode = CESSARPreferencesAccessor.getCompatibilityMode(project);
		if (mode == ECompatibilityMode.NONE)
		{
			return EJetComplianceLevel.JET4;
		}
		else
		{
			if (ProjectUtils.getAutosarReleaseOrdinal(project) == 2)
			{
				return EJetComplianceLevel.JET2;
			}
			else
			{
				return EJetComplianceLevel.JET3;
			}
		}
	}

	/**
	 * @param iFile
	 * @return suggested package name
	 */
	public static String suggestPackageName(final IFile iFile)
	{
		String result = JETCoreConstants.DEFAULT_JET_PACKAGE;
		if (iFile != null)
		{
			IPath basePath = iFile.getProjectRelativePath().removeLastSegments(1);
			String[] segments = basePath.segments();
			int length = segments.length;
			for (int i = 0; i < length; i++)
			{

				String convertToJavaIdentifier = StringUtils.convertToJavaIdentifier(segments[i]);
				String convertedSegment = convertToJavaIdentifier.toLowerCase();
				result += "." + convertedSegment;

			}
			// String prop = basePath.toString().replace('/', '.').toLowerCase();
			// // Character.isJavaIdentifierPart(ch)
			// prop = StringUtils.convertToJavaIdentifier(prop);

		}
		return result;
	}

	/**
	 * @param iFile
	 * @return suggested class name
	 */
	public static String suggestClassName(final IFile iFile)
	{
		String result = "GenericClassName";
		if (iFile != null)
		{
			String prop = iFile.getFullPath().lastSegment();
			if (prop != null)
			{
				if (prop.toLowerCase().endsWith(JETCoreConstants.JET))
				{
					prop = prop.substring(0, prop.length() - 3);
				}
				prop = StringUtils.convertToJavaIdentifier(prop);
				StringBuilder builder = new StringBuilder();
				builder.append(prop);
				result = builder.toString();
			}
		}
		return result;
	}

	/**
	 * @param jetFile
	 * @return jetReplacementString
	 */
	public static String getJetDirective(IFile jetFile)
	{
		EJetComplianceLevel complinaceLevel = getJetComplianceLevel(jetFile.getProject());

		String jetReplacementString = "<%@ jet package=\"" + JETCoreUtils.suggestPackageName(jetFile) + "\""
			+ JETCoreConstants.LNS;
		jetReplacementString += computeImports(complinaceLevel);
		jetReplacementString += SPACES + "class=\"" + JETCoreUtils.suggestClassName(jetFile) + "\"";
		if (complinaceLevel.getComplianceSettings().useExtendedSkeleton())
		{
			jetReplacementString += JETCoreConstants.LNS;
			jetReplacementString += SPACES + "parallel=\"true\"%>" + JETCoreConstants.LNS;
		}
		else
		{
			jetReplacementString += "%>" + JETCoreConstants.LNS;
		}
		return jetReplacementString;
	}

	/**
	 * Collect all JET imports and compute the "imports" content for JET directive
	 *
	 * @return A string with all the JET imports
	 */
	private static String computeImports(EJetComplianceLevel complianceLevel)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(JETCoreUtils.SPACES + "imports=\"");

		String[] imports = complianceLevel.getComplianceSettings().getSkeletonImports();

		for (String imp: imports)
		{
			buffer.append(imp);
			buffer.append(JETCoreConstants.LNS);
			buffer.append("                 ");
		}

		buffer.append("\"");
		buffer.append(JETCoreConstants.LNS);
		return buffer.toString();
	}

	/**
	 * Check if this status contains a read only message
	 *
	 * @param status
	 *        - the status to be checked
	 * @return <code> true </code> if this status or any of its children contains an read-only message
	 */
	public static boolean isReadOnlyStatus(IStatus status)
	{
		int code = status.getCode();
		IStatus[] children = status.getChildren();
		boolean readOnly = false;
		// check if status contains read-only error
		if (code == JETCoreConstants.READ_ONLY)
		{
			readOnly = true;
		}
		else
		{
			if (children != null)
			{
				// parse children
				for (IStatus child: children)
				{
					// check if child status contains readOnly message
					boolean readOnlyStatus = isReadOnlyStatus(child);
					if (readOnlyStatus)
					{
						readOnly = true;
						break;
					}

				}
			}
			else
			{
				readOnly = false;
			}
		}
		return readOnly;
	}

	/**
	 * Because the JET skeleton's body is adjusted to use the system newline representation, but most of the generators
	 * are not, the newline character can be generated inconsistently by for the skeleton's body and the generators.
	 * This is a problem for some features of the JET transformer/compiler which needs to match sections of the skeleton
	 * code to code snippets generated by the generators.
	 *
	 * The method shall be used in the generators to comply to the newline representation of the System.
	 *
	 * @param value
	 *        the string which' newline should be adjusted
	 * @return the string adjusted to use the System's newline representation.
	 */
	public static String convertLineFeed(String value)
	{
		Matcher matcher = NL_PATTERN.matcher(value);
		if (matcher.find())
		{
			String nl = matcher.group(1);
			if (!NL.equals(nl))
			{
				return value.replaceAll(nl, NL);
			}
		}

		return value;
	}
}
