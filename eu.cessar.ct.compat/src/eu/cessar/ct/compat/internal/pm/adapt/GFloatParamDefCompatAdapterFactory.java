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
import gautosar.gecucparameterdef.GFloatParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GFloatParamDefCompatAdapterFactory implements IEMFAdapterFactory
{

	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (target instanceof GFloatParamDef)
		{
			if (IPMCompatElementInfo.class.equals(type))
			{
				GFloatParamDefCompatPMInfo result = new GFloatParamDefCompatPMInfo();
				return result;
			}
		}
		return null;
	}

}
