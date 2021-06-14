/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl7321 Jul 13, 2010 3:24:55 PM </copyright>
 */
package eu.cessar.ct.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.service.prefs.BackingStoreException;

import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * @author uidl7321
 *
 */
public final class CessarRuntimeUtils
{

	public static final String VM_ARGS_DEFAULT = "execution.VMArgs"; //$NON-NLS-1$

	public static final String SYS_PROP_MM40 = "cessar.ActiveAutosarMetamodel.40"; //$NON-NLS-1$

	public static final String SYS_PROP_MM3x = "cessar.ActiveAutosarMetamodel.3x"; //$NON-NLS-1$ // SUPPRESS CHECKSTYLE ok

	// non-instantiable
	private CessarRuntimeUtils()
	{
	}

	/**
	 * Returns the preference for the VM arguments, in the form '-Xmx512M\n etc.'.
	 *
	 * @return the corresponding preference.
	 */
	public static String getVMArgs()
	{
		return Platform.getPreferencesService().getString(CessarPluginActivator.PLUGIN_ID,
			CessarRuntimeUtils.VM_ARGS_DEFAULT, "", //$NON-NLS-1$
			null);
	}

	/**
	 * Sets the preference for the VM arguments, with the given value.
	 *
	 * @param value
	 */
	public static void setVMArgs(String value)
	{
		IEclipsePreferences preferences = new InstanceScope().getNode(CessarPluginActivator.PLUGIN_ID);
		preferences.put(CessarRuntimeUtils.VM_ARGS_DEFAULT, value);
		try
		{
			preferences.flush();
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Returns the default value of the preference for the VM arguments.
	 *
	 * @return the default preference
	 */
	public static String getDefaultVMArgs()
	{
		IEclipsePreferences preferences = new DefaultScope().getNode(CessarPluginActivator.PLUGIN_ID);
		return preferences.get(CessarRuntimeUtils.VM_ARGS_DEFAULT, ""); //$NON-NLS-1$
	}

	/**
	 * Collects the pluget files from the given project.
	 *
	 * @param project
	 *        the project where to search the pluget files
	 * @param files
	 *        the result list of pluget files
	 */
	public static void collectPlugetFiles(IProject project, final List<IFile> files)
	{
		IContentType contentType = Platform.getContentTypeManager().getContentType(CessarRuntime.PLUGET_CONTENT_TYPE_ID);

		try
		{
			List<IFile> filesForContentType = ResourceUtils.getIFileFromProjectForContentType(project, contentType);
			files.addAll(filesForContentType);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Method that recursively collect pluget files from specified {@link IResource} containers.
	 *
	 * @param containers
	 *        the array with {@link IResource} entities
	 * @return the collected files
	 * @throws CoreException
	 * @throws IOException
	 */
	public static List<IFile> collectPlugetFiles(final Object[] containers) throws CoreException
	{
		List<IFile> collectedFiles = new ArrayList<IFile>();

		for (int i = 0; i < containers.length; i++)
		{
			if (containers[i] instanceof IFile)
			{
				IFile iFile = (IFile) containers[i];

				// consider current IFile only if has JET content type
				if (isPlugetFile(iFile))
				{
					collectedFiles.add(iFile);
				}
			}
			else if (containers[i] instanceof IContainer)
			{
				collectedFiles.addAll(collectPlugetFiles(((IContainer) containers[i]).members()));
			}
		}

		return collectedFiles;
	}

	/**
	 * Return true if the <code>file</code> is a pluget file. The checking is done by verifying the content type of the
	 * file.
	 *
	 * @param file
	 * @return
	 */
	public static boolean isPlugetFile(IFile file)
	{
		if (file != null)
		{
			try
			{
				IContentDescription description = file.getContentDescription();
				if (description != null)
				{
					IContentType contentType = description.getContentType();
					if ((contentType != null) && (CessarRuntime.PLUGET_CONTENT_TYPE_ID.equals(contentType.getId())))
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
	 * Collects the pluget classses from the given project.
	 *
	 * @param project
	 *        the project where to search the pluget classes
	 * @return an array of <code>IType</code> representing the pluget classes.
	 */
	public static IType[] collectPlugetClasses(IProject project)
	{
		List<IType> classes = new ArrayList<IType>();
		if (project == null || !project.isAccessible())
		{
			return new IType[] {};
		}
		IJavaProject javaProject = JavaCore.create(project);
		try
		{
			IType searchedType = javaProject.findType("eu.cessar.ct.sdk.ICessarPluget"); //$NON-NLS-1$
			if (searchedType == null)
			{
				return new IType[] {};
			}
			IRegion region = getRegion(javaProject);
			ITypeHierarchy typeHierarchy = javaProject.newTypeHierarchy(searchedType, region, null);
			IType[] subtypes = typeHierarchy.getAllSubtypes(searchedType);
			for (int i = 0; i < subtypes.length; i++)
			{
				IType type = subtypes[i];
				int cachedFlags = typeHierarchy.getCachedFlags(type);
				if (!Flags.isAbstract(cachedFlags) && Flags.isPublic(cachedFlags))
				{
					classes.add(type);
				}
			}
		}
		catch (JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return classes.toArray(new IType[classes.size()]);
	}

	/**
	 * @param javaProject
	 * @return
	 * @throws JavaModelException
	 */
	private static IRegion getRegion(IJavaProject javaProject) throws JavaModelException
	{
		IRegion result = JavaCore.newRegion();
		IPackageFragmentRoot[] roots = javaProject.getPackageFragmentRoots();
		for (int i = 0; i < roots.length; i++)
		{
			if (!roots[i].isArchive())
			{
				result.add(roots[i]);
			}
		}
		return result;
	}
}
