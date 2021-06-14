/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Oct 22, 2009 1:16:29 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath;

import java.util.ArrayList;
import java.util.List;

import org.artop.aal.common.metamodel.AutosarMetaModelVersionData;
import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor;

/**
 * This contributor inspects the projects's AUTOSAR release and provides the corresponding meta-model bundleID. <br>
 * </br>
 * NOTE: In order for the Cessar Model Library to show only meta-model related libraries, all direct required and
 * re-exported bundles of the provided bundle have to be included in the bundleIDs array of generic classpath
 * contributors
 */
public class AutosarModelClasspathContributor extends AbstractPluginsClasspathContributor
{
	private static final String GAUTOSAR_PLUGIN_ID = "org.artop.aal.gautosar"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor#getBundleIDs()
	 */
	@Override
	protected String[] getBundleIDs(IJavaProject javaProject)
	{
		IProject project = javaProject.getProject();

		ECompatibilityMode mode = CESSARPreferencesAccessor.getCompatibilityMode(project);

		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);
		if (autosarRelease != null)
		{
			if (mode.haveNewAPI())
			{
				List<String> result = new ArrayList<String>();
				result.add(GAUTOSAR_PLUGIN_ID);
				AutosarMetaModelVersionData data = autosarRelease.getAutosarVersionData();
				result.add("org.artop.aal.autosar" + data.getCanonicalVersionNumber()); //$NON-NLS-1$
				return result.toArray(new String[result.size()]);
			}
		}
		return new String[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor#collectDependencies()
	 */
	@Override
	protected boolean collectDependencies()
	{
		// do not collect dependencies; all direct required and
		// re-exported bundles of getBundleIDs() have to be
		// manually included inside the bundleIDs array of
		// the generic contributors
		return false;
	}

}
