/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import org.eclipse.emf.common.notify.Adapter;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;
import gautosar.gecucparameterdef.GAbstractStringParamDef;

/**
 *
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class GAbstractStringParamDefAdapterFactory implements IEMFAdapterFactory
{

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter, Object target, Class<?> type)
	{
		if (target instanceof GAbstractStringParamDef)
		{
			if (IPMElementInfo.class.equals(type))
			{
				GAbstractStringParamDefPMInfo result = new GAbstractStringParamDefPMInfo();
				return result;
			}
		}
		return null;
	}
}
