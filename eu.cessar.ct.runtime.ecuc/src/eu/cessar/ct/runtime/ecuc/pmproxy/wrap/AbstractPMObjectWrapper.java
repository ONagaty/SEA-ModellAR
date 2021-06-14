package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * 
 */
public abstract class AbstractPMObjectWrapper<T> extends AbstractMasterObjectWrapper<T>
{

	/**
	 * @param engine
	 * @param masterClass
	 */
	public AbstractPMObjectWrapper(IEMFProxyEngine engine, Class<T> masterClass)
	{
		super(engine, masterClass);
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
