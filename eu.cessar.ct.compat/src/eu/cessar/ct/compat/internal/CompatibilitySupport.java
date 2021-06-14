/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 23, 2010 4:07:19 PM </copyright>
 */
package eu.cessar.ct.compat.internal;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.gautosar.services.DefaultMetaModelServiceProvider;
import org.artop.aal.gautosar.services.IMetaModelServiceProvider;
import org.artop.aal.gautosar.services.splitting.ISplitableElementService;
import org.eclipse.core.resources.IProject;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;

import eu.cessar.ct.core.mms.MetaModelUtils;

/**
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class CompatibilitySupport
{

	private static final IMetaModelServiceProvider provider = new DefaultMetaModelServiceProvider();

	/**
	 * @param project
	 * @return
	 */
	public static IModelConstants getModelConstants(IProject project)
	{
		AutosarReleaseDescriptor release = MetaModelUtils.getAutosarRelease(project);
		return getModelConstants(release);
	}

	/**
	 * @param project
	 * @return
	 * @deprecated Should be moved to MMS
	 */
	@Deprecated
	public static ISplitableElementService getSplitableService(IMetaModelDescriptor release)
	{
		return provider.getService(release, ISplitableElementService.class);
	}

	/**
	 * @param release
	 * @return
	 */
	public static IModelConstants getModelConstants(IMetaModelDescriptor release)
	{
		if (release != null)
		{
			return provider.getService(release, IModelConstants.class);
		}
		else
		{
			return null;
		}
	}
}
