/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 12:50:37 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.ProjectObjectWrapper;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.pm.PMRuntimeException;

/**
 * 
 */
public class RootNodeClassResolver extends AbstractClassResolver<IProject>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractClassResolver#isValidMasterObject(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	@Override
	public boolean isValidMasterObject(IEMFProxyEngine engine, Object context, Object object,
		Map<String, Object> parameters)
	{
		return super.isValidMasterObject(engine, context, object, parameters)
			&& object == engine.getProject();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getMasterClassType(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public Class<IProject> getMasterClassType(IEMFProxyEngine engine)
	{
		return IProject.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, IProject master,
		Map<String, Object> parameters)
	{
		IEcucPresentationModel ecucPM = getEcucPresentationModel(engine);
		return ecucPM.getRootPMEClass();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<IProject> getWrapper(IEMFProxyEngine engine, Object context,
		IEMFProxyObject slave, IProject master, Map<String, Object> parameters)
	{
		return new ProjectObjectWrapper(engine, master);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<IProject> createWrapper(IEMFProxyEngine proxyEngine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		throw new PMRuntimeException("Cannot create a root node using the presentation model"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#supportMultiContext(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public boolean supportMultiContext(IEMFProxyEngine engine)
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getStandardContext(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	public Object getStandardContext(IEMFProxyEngine engine, IEMFProxyObject slave)
	{
		// no support for multi context
		return DEFAULT_CONTEXT;
	}

}
