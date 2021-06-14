/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import org.eclipse.emf.common.notify.Adapter;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * 
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class GARPackageCompatAdapterFactory implements IEMFAdapterFactory
{

	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (target instanceof GARPackage)
		{
			if (IPMCompatElementInfo.class.equals(type))
			{
				GARPackageCompatPMInfo result = new GARPackageCompatPMInfo();
				return result;
			}
		}
		return null;
	}

}
