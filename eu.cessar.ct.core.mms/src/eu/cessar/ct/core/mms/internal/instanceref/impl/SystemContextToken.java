/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 08.07.2013 10:19:40
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.core.mms.internal.instanceref.ISystemContextToken;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 10:35:16 2013 %
 * 
 *         %version: 1 %
 */
public class SystemContextToken extends AbstractContextToken implements ISystemContextToken
{

	private EReference reference;

	/**
	 * @param token
	 * @param reference
	 */
	public SystemContextToken(String token, EReference reference)
	{
		super(token);
		this.reference = reference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.instanceref.impl.ISystemContextToken#getReference()
	 */
	@Override
	public EReference getReference()
	{
		return reference;
	}

}
