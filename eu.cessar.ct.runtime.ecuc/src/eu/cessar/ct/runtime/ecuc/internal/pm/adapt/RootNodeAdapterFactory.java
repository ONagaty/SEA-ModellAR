package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.Adapter;

import eu.cessar.ct.core.mms.adapter.ICessarAdapterFactory;
import eu.cessar.ct.core.mms.adapter.IEMFAdapterFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;

public class RootNodeAdapterFactory implements IEMFAdapterFactory
{

	public Adapter adapt(ICessarAdapterFactory adapterFactory, Adapter parentAdapter,
		Object target, Class<?> type)
	{
		if (target instanceof IProject)
		{
			if (IPMElementInfo.class.equals(type))
			{
				RootNodePMInfo result = new RootNodePMInfo();
				return result;
			}
		}
		return null;
	}
}
