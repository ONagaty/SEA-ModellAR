/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 29, 2010 11:44:26 AM </copyright>
 */
package eu.cessar.ct.compat.internal.sdk;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.compat.internal.sdk.CompatibilityUtils.Service;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 12, 2012
 */
public class CompatibilityUtilsService implements Service
{
	public static final CompatibilityUtilsService eINSTANCE = new CompatibilityUtilsService();

	/**
	 * The private constructor of the singleton
	 */
	private CompatibilityUtilsService()
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.sdk.CompatibilityUtils.Service#getPMRoot(org.eclipse.core.resources.IProject)
	 */
	public EObject getPMRoot(IProject project)
	{
		IEcucPresentationModel model = IEcucCore.INSTANCE.getEcucPresentationModel(project);
		return model.getPMModelRoot();
	}
}
