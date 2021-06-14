/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import org.eclipse.emf.common.notify.Adapter;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import gautosar.gecucparameterdef.GIntegerParamDef;

/**
 *
 * @Review uidl7321 - Apr 11, 2012
 *
 */
public class GIntegerParamDefCompatAdapterFactory implements IEMFAdapterFactory
{

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter, Object target, Class<?> type)
	{
		if (target instanceof GIntegerParamDef)
		{
			if (IPMCompatElementInfo.class.equals(type))
			{
				GIntegerParamDefCompatPMInfo result = new GIntegerParamDefCompatPMInfo();
				return new PMElementToPMCompatDelegate(result);
			}
		}
		return null;
	}

}
