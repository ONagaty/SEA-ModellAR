/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Mar 8, 2014 2:25:23 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import org.eclipse.emf.ecore.EObject;

/**
 * TODO: Please comment this class
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Sat Mar  8 16:42:42 2014 %
 * 
 *         %version: 1 %
 */
public class CessarEObjectObjectPair
{
	private EObject eObject;
	private Object value;

	CessarEObjectObjectPair(EObject eObject, Object value)
	{
		this.eObject = eObject;
		this.value = value;
	}

	public EObject getEObject()
	{
		return eObject;
	}

	public Object getValue()
	{
		return value;
	}

}
