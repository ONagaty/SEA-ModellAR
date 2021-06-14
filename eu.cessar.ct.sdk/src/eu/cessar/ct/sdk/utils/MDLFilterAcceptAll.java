/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Jun 13, 2013 12:50:27 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.sdk.utils.IModelDependencyLookup.IMDLFilter;

/**
 * Accept-all {@code IMDLFilter}, used by {@code ModelDependencyLookup}.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Mon Jul 29 14:03:06 2013 %
 * 
 *         %version: 1 %
 */
public class MDLFilterAcceptAll implements IMDLFilter
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter#accept(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EReference, org.eclipse.emf.ecore.EObject, int)
	 */
	@Override
	public boolean accept(EObject oldContext, EReference path, EObject newContext, int currentHops)
	{
		return true;
	}
}
