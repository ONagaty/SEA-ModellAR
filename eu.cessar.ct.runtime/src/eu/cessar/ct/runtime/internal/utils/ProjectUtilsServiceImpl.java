/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 21, 2010 9:18:57 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.utils;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.CodegenPreferencesAccessor;
import eu.cessar.ct.sdk.utils.ProjectUtils;

/**
 * @author uidl6458
 * 
 */
public final class ProjectUtilsServiceImpl implements ProjectUtils.Service
{

	public static final ProjectUtilsServiceImpl eINSTANCE = new ProjectUtilsServiceImpl();

	/**
	 * The private constructor of the singleton
	 */
	private ProjectUtilsServiceImpl()
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.ProjectUtils.Service#getProjectVariant(org.eclipse.core.resources.IProject)
	 */
	public String getProjectVariant(IProject project)
	{
		EProjectVariant variant = CESSARPreferencesAccessor.getProjectVariant(project);
		if (variant == null)
		{
			return null;
		}
		else
		{
			return variant.name();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.ProjectUtils.Service#getCompatibilityMode(org.eclipse.core.resources.IProject)
	 */
	public String getCompatibilityMode(IProject project)
	{
		ECompatibilityMode mode = CESSARPreferencesAccessor.getCompatibilityMode(project);
		if (mode == null)
		{
			return null;
		}
		else
		{
			return mode.name();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.ProjectUtils.Service#getOutputFolder(org.eclipse.core.resources.IProject,
	 * org.eclipse.core.resources.IFile)
	 */
	public IFolder getOutputFolder(IProject project, IFile generator)
	{
		if (CodegenPreferencesAccessor.isUsingCustomOutputFolder(project))
		{
			try
			{
				IPath path = CodegenPreferencesAccessor.getResolvedCustomOutputFolder(project);

				IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);

				return folder;
			}
			catch (CoreException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			return generator.getParent().getFolder(new Path(CessarRuntime.CODEGEN_DEFAULT_OUTPUT_FOLDER));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.ProjectUtils.Service#getAutosarReleaseOrdinal(org.eclipse.core.resources.IProject)
	 */
	public int getAutosarReleaseOrdinal(IProject project)
	{
		AutosarReleaseDescriptor release = MetaModelUtils.getAutosarRelease(project);
		if (release != null)
		{
			return release.getAutosarVersionData().getMajor();
		}
		else
		{
			return -1;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.ProjectUtils.Service#getCessarVersion()
	 */
	public Version getCessarVersion()
	{
		Bundle bundle = CessarPluginActivator.getDefault().getBundle();
		if (bundle != null)
		{
			return bundle.getVersion();
		}
		return null;
	}
}
