package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import org.eclipse.emf.common.notify.Adapter;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;
import gautosar.gecucparameterdef.GIntegerParamDef;

public class GIntegerParamDefAdapterFactory implements IEMFAdapterFactory
{

	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (target instanceof GIntegerParamDef)
		{
			if (IPMElementInfo.class.equals(type))
			{
				GIntegerParamDefPMInfo result = new GIntegerParamDefPMInfo();
				return result;
			}
		}
		return null;
	}

}
