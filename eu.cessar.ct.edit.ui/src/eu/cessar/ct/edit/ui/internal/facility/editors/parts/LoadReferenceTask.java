/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jul 12, 2011 4:20:30 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.core.mms.SafeUsageCrossReferencer;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;

/**
 * @author uidu0944
 * 
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#REFERENCED_BY#1")
class LoadReferenceTask extends Thread
{
	private ISearchReferencesMonitor searchMonitor;
	private EObject input;
	private Collection<Resource> resources;

	public LoadReferenceTask(ISearchReferencesMonitor searchMonitor, EObject input, Collection<Resource> resources)
	{
		this.searchMonitor = searchMonitor;
		this.input = input;
		this.resources = resources;
	}

	@Override
	public void run()
	{
		List<EObject> result = new ArrayList<EObject>();
		try
		{
			for (Resource resource: resources)
			{
				Collection<Setting> collection = SafeUsageCrossReferencer.find(input, resource,
					new NullProgressMonitor()
					{
						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.core.runtime.NullProgressMonitor#isCanceled()
						 */
						@Override
						public boolean isCanceled()
						{
							return searchMonitor.isCanceled();
						}
					});
				boolean isDisposed = searchMonitor.isCanceled();
				if (isDisposed)
				{
					return;
				}
				for (Setting setting: collection)
				{
					EStructuralFeature feature = setting.getEStructuralFeature();
					if (!feature.isDerived() && !feature.isTransient()
						&& ((feature instanceof EAttribute) || !((EReference) feature).isContainment()))
					{
						result.add(setting.getEObject());
					}
				}
			}
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		finally
		{
			searchMonitor.setReferences(result);
		}
	}
}
