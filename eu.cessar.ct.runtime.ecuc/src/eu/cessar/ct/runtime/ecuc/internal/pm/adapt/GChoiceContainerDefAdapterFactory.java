package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import org.eclipse.emf.common.notify.Adapter;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;
import gautosar.gecucparameterdef.GChoiceContainerDef;

public class GChoiceContainerDefAdapterFactory implements IEMFAdapterFactory
{

	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (target instanceof GChoiceContainerDef)
		{
			if (IPMElementInfo.class.equals(type))
			{
				GChoiceContainerDefPMInfo result = new GChoiceContainerDefPMInfo();
				return result;
			}
		}
		return null;
	}

}
