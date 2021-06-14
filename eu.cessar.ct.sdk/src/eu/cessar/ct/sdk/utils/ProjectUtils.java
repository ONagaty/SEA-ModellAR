/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 21, 2009 12:08:31 PM </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.workspace.loading.ModelLoadManager;
import org.osgi.framework.Version;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.core.platform.util.StringUtils;

/**
 * Various methods related to projects
 * 
 */
/**
 * @author uidt2045
 * 
 */
public final class ProjectUtils
{
	private ProjectUtils()
	{
		// avoid instance

	}

	public static interface Service
	{
		@SuppressWarnings("javadoc")
		public String getProjectVariant(IProject project);

		@SuppressWarnings("javadoc")
		public String getCompatibilityMode(IProject project);

		@SuppressWarnings("javadoc")
		public IFolder getOutputFolder(IProject project, IFile generator);

		@SuppressWarnings("javadoc")
		public int getAutosarReleaseOrdinal(IProject project);

		@SuppressWarnings("javadoc")
		public Version getCessarVersion();
	}

	public static interface JetService
	{
		@SuppressWarnings("javadoc")
		public IStatus compileJet(IProject project, IFile jetFile);

		@SuppressWarnings("javadoc")
		public IStatus compileJets(IProject project, Collection<IFile> jetFiles);
	}

	private static final Service service = PlatformUtils.getService(Service.class);

	private static final JetService jetService = PlatformUtils.getService(JetService.class);

	/**
	 * Return the variant of the project as it is defined inside project settings.
	 * 
	 * @param project
	 *        the Cessar project
	 * @return the project variant, one of the strings OTHER, PRE_COMPILE, LINK_TIME, POST_BUILD
	 */
	public static String getProjectVariant(IProject project)
	{
		return service.getProjectVariant(project);
	}

	/**
	 * Return the compatibility mode of the project. Only projects with support for AUTOSAR 2.1 and 3.x support this
	 * flag. For unsupported projects it will return NONE
	 * 
	 * @param project
	 *        the Cessar project
	 * @return the compatibility mode, one of the strings NONE, MODE
	 */
	public static String getCompatibilityMode(IProject project)
	{
		return service.getCompatibilityMode(project);
	}

	/**
	 * Return the folder used as output when the generated is executed. Please note that the output folder might be
	 * located into another project
	 * 
	 * @param project
	 * @param generator
	 * @return
	 */
	public static IFolder getOutputFolder(IProject project, IFile generator)
	{
		return service.getOutputFolder(project, generator);
	}

	/**
	 * Return the major part of the release corresponding to the AUTOSAR model of the <code>project</code> or -1 if this
	 * cannot be determined. Currently, the possible values are 2, 3 or 4.
	 * 
	 * @param project
	 * @return
	 */
	public static int getAutosarReleaseOrdinal(IProject project)
	{
		return service.getAutosarReleaseOrdinal(project);
	}

	/**
	 * The pattern supports only the fileName, NOT the path to it. Only '?'(representing any character, exactly one) and
	 * '*' (representing any character, a sequence or none ) are allowed in a pattern
	 * 
	 * If the container doesn't exist the method will throw an CoreException. If the parameter recursive is true it will
	 * search recursively throw the container, if it is false it will search only at the first level of depth.
	 * 
	 * Te*st.java -> Test.java, Teast.java, Tebbst.java, Teacbdst.java ... Te?st.java -> Teast.java, Tebst.java,
	 * Teast.java ..
	 * 
	 * @param folder
	 * @param pattern
	 * @param recursive
	 * @return
	 * @throws CoreException
	 */

	public static List<IFile> getMembers(final IContainer folder, final String pattern, final boolean recursive)
		throws CoreException
	{
		if (!folder.exists())
		{
			throw new CoreException(new Status(IStatus.ERROR, "eu.cessar.ct.sdk.utils", //$NON-NLS-1$
				NLS.bind("The given IContainer: {0} does not exists", //$NON-NLS-1$
					folder.getFullPath().toPortableString())));
		}
		else
		{
			final List<IFile> result = new ArrayList<IFile>();
			if (recursive)
			{
				IResourceProxyVisitor visitor = new IResourceProxyVisitor()
				{
					public boolean visit(IResourceProxy proxy) throws CoreException
					{
						if (proxy.getType() == IResource.FILE)
						{
							String fileName = proxy.getName();
							if (checkPattern(fileName, pattern))
							{
								result.add((IFile) proxy.requestResource());
							}
						}
						return true;
					}
				};

				folder.accept(visitor, IContainer.INCLUDE_HIDDEN);

				return result;
			}
			else
			{
				IResource[] members = folder.members();

				for (int i = 0; i < members.length; i++)
				{
					if (members[i] instanceof IFile)
					{
						// check if it passes the pattern
						String fileName = members[i].getName();
						if (checkPattern(fileName, pattern))
						{
							result.add((IFile) members[i]);
						}
					}
				}
			}
			return result;
		}
	}

	/**
	 * Check if the string is conforming to the pattern
	 * 
	 * @param fileName
	 * @param pattern
	 * @return
	 */
	private static boolean checkPattern(String fileName, String pattern)
	{
		Pattern pttrn = Pattern.compile(StringUtils.escapeRegexForSearch(pattern));
		Matcher matcher = pttrn.matcher(fileName);
		return matcher.matches();
	}

	/**
	 * compile the given jetFile
	 * 
	 * @param project
	 * @param jetFile
	 * @return OK status if compilation done successfully, or an Error status if errors occurred
	 */
	public static IStatus compileJet(IProject project, IFile jetFile)
	{
		return jetService.compileJet(project, jetFile);
	}

	/**
	 * compile the given collection of jetFiles
	 * 
	 * @param project
	 * @param jetFile
	 * @return OK status if all jet files are compiled successfully, or an Error status if errors occurred
	 * @param project
	 * @param jetFiles
	 * @return
	 */
	public static IStatus compileJets(IProject project, Collection<IFile> jetFiles)
	{
		return jetService.compileJets(project, jetFiles);
	}

	/**
	 * 
	 * Returns the current CESSAR-CT version.
	 * 
	 * @see Version
	 * @return
	 */
	public static Version getCessarVersion()
	{
		return service.getCessarVersion();
	}

	/**
	 * Create or update a workspace file using an atomic workspace operation
	 * 
	 * @param file
	 *        the output file, if it does not exists it will be created otherwise it will be rewritten
	 * @param in
	 *        the {@link InputStream} holding the new content of the file
	 * @param monitor
	 *        a progress monitor, or <code>null</code> if progress reporting is not desired.
	 */
	public static void createIFile(final IFile file, final InputStream in, IProgressMonitor monitor)
		throws CoreException
	{
		file.getWorkspace().run(new IWorkspaceRunnable()
		{

			public void run(IProgressMonitor monitor) throws CoreException
			{
				if (file.exists())
				{
					file.setContents(in, IResource.FORCE | IResource.KEEP_HISTORY, monitor);
				}
				else
				{
					file.create(in, true, monitor);
				}
			}
		}, file.getParent(), IWorkspace.AVOID_UPDATE, monitor);
	}

	/**
	 * Compute and return the MD5 of the content of the provided file
	 * 
	 * @param file
	 *        the file
	 * @return the computed MD5
	 * @throws CoreException
	 */
	public static String getMD5(IFile file) throws CoreException
	{
		InputStream is = file.getContents();
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
			byte[] buffer = new byte[1024];
			int size = is.read(buffer);
			while (size > -1)
			{
				md.update(buffer, 0, size);
				size = is.read(buffer);
			}
			buffer = md.digest();
			String result = new BigInteger(1, buffer).toString(16);
			if (result.length() % 2 > 0)
			{
				result = "0" + result; //$NON-NLS-1$
			}
			return result;
		}
		catch (Exception e)
		{
			throw new CoreException(new Status(IStatus.ERROR, "eu.cessar.ct.sdk", e.getMessage(), e)); //$NON-NLS-1$
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				// ignore
			}
		}
	}

	/**
	 * Performs an unload followed by a load of the model contained by the given <code>file </code>
	 * 
	 * @param file
	 *        if null or not accessible an exception is thrown
	 * @param monitor
	 *        A progress monitor, or null if progress reporting is not desired.
	 */
	public static void reloadFile(IFile file, IProgressMonitor monitor)
	{
		Assert.isNotNull(file);
		Assert.isTrue(file.isAccessible());
		ModelLoadManager.INSTANCE.reloadFile(file, false, monitor);
	}

	/**
	 * Performs an unload followed by a load of the model contained by the given <code>project </code>
	 * 
	 * @param project
	 *        if null or not accessible an exception is thrown
	 * @param monitor
	 *        A progress monitor, or null if progress reporting is not desired.
	 **/
	public static void reloadProject(IProject project, IProgressMonitor monitor)
	{
		Assert.isNotNull(project);
		Assert.isTrue(project.isAccessible());
		ModelLoadManager.INSTANCE.reloadProject(project, false, false, monitor);
	}

	/**
	 * Unloads the model contained in the given <code>file</code><br>
	 * NOTE: Caution must be taken when calling this method as it has side-effects upon the UI that edits the model.
	 * 
	 * @param file
	 *        if null or not accessible an exception is thrown
	 * @param monitor
	 *        A progress monitor, or null if progress reporting is not desired.
	 */
	public static void unloadFile(IFile file, IProgressMonitor monitor)
	{
		Assert.isNotNull(file);
		Assert.isTrue(file.isAccessible());
		ModelLoadManager.INSTANCE.unloadFile(file, false, monitor);
	}

	/**
	 * Unloads the model contained in the given <code>project</code><br>
	 * NOTE: Caution must be taken when calling this method as it has side-effects upon the UI that edits the model.
	 * 
	 * @param project
	 *        if null or not accessible an exception is thrown
	 * @param monitor
	 *        A progress monitor, or null if progress reporting is not desired.
	 */
	public static void unloadProject(IProject project, IProgressMonitor monitor)
	{
		Assert.isNotNull(project);
		Assert.isTrue(project.isAccessible());
		ModelLoadManager.INSTANCE.unloadProject(project, false, false, monitor);
	}

	/**
	 * Performs a load of the model contained by the given <code>file </code>
	 * 
	 * @param file
	 *        if null or not accessible an exception is thrown
	 * @param monitor
	 *        A progress monitor, or null if progress reporting is not desired.
	 **/
	public static void loadFile(IFile file, IProgressMonitor monitor)
	{
		Assert.isNotNull(file);
		Assert.isTrue(file.isAccessible());
		ModelLoadManager.INSTANCE.loadFile(file, false, monitor);

	}

	/**
	 * Performs a load of the model contained by the given <code>project</code>
	 * 
	 * @param project
	 *        if null or not accessible an exception is thrown
	 * @param monitor
	 *        A progress monitor, or null if progress reporting is not desired.
	 **/
	public static void loadProject(IProject project, IProgressMonitor monitor)
	{
		Assert.isNotNull(project);
		Assert.isTrue(project.isAccessible());
		ModelLoadManager.INSTANCE.loadProject(project, false, false, monitor);
	}

	/**
	 * Wait the current thread for the model loader to finish loading the model. This method should not be executed from
	 * the Job that actually load the model otherwise some unchecked exception will be throw in order to avoid a
	 * deadlock.
	 * 
	 * @param project
	 * @param monitor
	 */
	public static void waitForModelLoading(IProject project, IProgressMonitor monitor)
	{
		// pass thru PlatformUtils implementation
		PlatformUtils.waitForModelLoading(project, monitor);
	}
}
