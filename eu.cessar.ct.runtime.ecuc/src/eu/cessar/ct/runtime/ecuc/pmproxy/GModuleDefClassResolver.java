/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 5:30:16 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GModuleConfigurationObjectWrapper;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

/**
 * 
 */
public class GModuleDefClassResolver extends AbstractECUCClassResolver<GModuleConfiguration>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getMasterClassType(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public Class<GModuleConfiguration> getMasterClassType(IEMFProxyEngine engine)
	{
		return GModuleConfiguration.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, GModuleConfiguration master,
		Map<String, Object> parameters)
	{
		IEcucPresentationModel model = getEcucPresentationModel(engine);
		if (context instanceof GModuleDef)
		{
			return (EClass) model.getPMClassifier((GModuleDef) context);
		}
		else
		{
			return (EClass) model.getPMClassifier(master.gGetDefinition());
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<GModuleConfiguration> getWrapper(IEMFProxyEngine engine,
		Object context, IEMFProxyObject slave, GModuleConfiguration master,
		Map<String, Object> parameters)
	{
		IEcucModel ecucModel = getEcucModel(engine);
		List<GModuleConfiguration> moduleCfgs = ecucModel.getModuleCfg(MetaModelUtils.getAbsoluteQualifiedName(master));
		return new GModuleConfigurationObjectWrapper(engine, moduleCfgs);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<GModuleConfiguration> createWrapper(IEMFProxyEngine proxyEngine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		throw new PMRuntimeException(
			"Cannot create a module configuration using the presentation model"); //$NON-NLS-1$
	}

}
