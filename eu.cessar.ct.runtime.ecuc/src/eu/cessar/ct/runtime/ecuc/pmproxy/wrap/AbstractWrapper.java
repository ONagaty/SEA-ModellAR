/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 19, 2010 9:37:53 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import org.eclipse.core.commands.ExecutionException;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * 
 */
public abstract class AbstractWrapper implements IMasterWrapper
{

	protected boolean readOnly;
	protected final IEMFProxyEngine engine;
	private EMFProxyObjectImpl proxyObject;

	/**
	 * @param engine2
	 */
	public AbstractWrapper(IEMFProxyEngine engine)
	{
		if (engine == null)
		{
			throw new PMRuntimeException("Internal error: engine cannot be null"); //$NON-NLS-1$
		}
		this.engine = engine;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterWrapper#setProxyObject(eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public void setProxyObject(EMFProxyObjectImpl proxyObject)
	{
		this.proxyObject = proxyObject;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterWrapper#getProxyObject()
	 */
	public EMFProxyObjectImpl getProxyObject()
	{
		return proxyObject;
	}

	/**
	 * @return
	 */
	protected Object getContext()
	{
		EMFProxyObjectImpl proxy = getProxyObject();

		if (proxy == null)
		{
			return null;
		}
		else
		{
			return proxy.eGetContext();
		}
	}

	/**
	 * @return
	 */
	protected IEMFProxyEngine getEngine()
	{
		return engine;
	}

	/**
	 * Return true if the feature wrapper is read-only. Trying to write to a
	 * feature wrapper will throw some sort of unchecked exception
	 * 
	 * @param checkManager
	 *        if <strong>checkManager</strong> is true then also the running
	 *        task manager will be asked for capability. If there is no such
	 *        task manager the wrapper will be readOnly.
	 * @return
	 */
	public boolean isReadOnly(boolean checkManager)
	{
		if (!readOnly && checkManager)
		{
			ICessarTaskManager<?> manager = getCurrentManager();
			return manager == null || !manager.canWrite();
		}
		return readOnly;
	}

	/**
	 * @param readOnly
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/**
	 * @return
	 */
	private ICessarTaskManager<?> getCurrentManager()
	{
		if (engine == null || engine.getProject() == null)
		{
			return null;
		}
		else
		{
			return CessarRuntime.getExecutionSupport().getCurrentManager(engine.getProject());
		}
	}

	/**
	 * Throw an exception if the model cannot be changed
	 */
	private void checkReadOnly()
	{
		if (isReadOnly(true))
		{
			throw new PMRuntimeException("Model is read only"); //$NON-NLS-1$
		}
	}

	/**
	 * @param task
	 */
	protected void updateModel(Runnable task)
	{
		checkReadOnly();
		try
		{
			getCurrentManager().updateModel(task);
		}
		catch (ExecutionException e)
		{
			throw new PMRuntimeException(e);
		}
	}

}
