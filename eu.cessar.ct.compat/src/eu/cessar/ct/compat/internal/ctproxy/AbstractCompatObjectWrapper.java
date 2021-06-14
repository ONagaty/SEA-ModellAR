package eu.cessar.ct.compat.internal.ctproxy;

import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterObjectWrapper;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public abstract class AbstractCompatObjectWrapper<T> extends AbstractMasterObjectWrapper<T>
{

	/**
	 * @param engine
	 * @param masterClass
	 */
	public AbstractCompatObjectWrapper(IEMFProxyEngine engine, Class<T> masterClass)
	{
		super(engine, masterClass);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getSlaveContainer(eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	public IEMFProxyObject getSlaveContainer(IEMFProxyObject object)
	{
		EMFProxyObjectImpl proxyObject = (EMFProxyObjectImpl) object;
		Object masterContainer = proxyObject.eGetMasterWrapper().getMasterContainer();
		if (masterContainer == null)
		{
			return null;
		}
		else
		{
			// if the parent support multi context use the same context as the
			// children, otherwise let the engine decide
			IProxyClassResolver<Object> classResolver = engine.getFirstClassResolverForMasterObject(
				IEMFProxyConstants.DEFAULT_CONTEXT, masterContainer, null);
			if (classResolver.supportMultiContext(engine))
			{
				return engine.getSlaveObject(proxyObject.eGetContext(), masterContainer);
			}
			else
			{
				return engine.getSlaveObject(null, masterContainer);
			}
		}
	}

}
