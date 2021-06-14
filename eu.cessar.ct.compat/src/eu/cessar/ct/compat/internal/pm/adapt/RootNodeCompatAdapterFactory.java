/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.Adapter;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class RootNodeCompatAdapterFactory implements IEMFAdapterFactory
{

	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (target instanceof IProject)
		{
			if (IPMCompatElementInfo.class.equals(type))
			{
				RootNodeCompatPMInfo result = new RootNodeCompatPMInfo();
				return result;
			}
		}
		return null;
	}

}
