/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 1:59:42 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GecucdescriptionPackage;
import gautosar.gecucparameterdef.GModuleDef;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;

/**
 * 
 */
public class GModuleConfigurationObjectWrapper extends MultiObjectWrapper<GModuleConfiguration>
{

	private static final String ATTR_SHORTNAME = "shortName"; //$NON-NLS-1$

	/**
	 * @param wrapped
	 */
	public GModuleConfigurationObjectWrapper(IEMFProxyEngine engine,
		List<GModuleConfiguration> wrapped)
	{
		super(engine, GModuleConfiguration.class, wrapped);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#getMasterEClass()
	 */
	@Override
	public EClass getMasterEClass()
	{
		IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(getEngine().getProject());
		return factory.getConcreteEClass(GecucdescriptionPackage.Literals.GMODULE_CONFIGURATION);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterContainer()
	 */
	public Object getMasterContainer()
	{
		List<GModuleConfiguration> list = getMasterObject();
		if (list == null || list.isEmpty())
		{
			return null;
		}
		else
		{
			GModuleDef moduleDef = list.get(0).gGetDefinition();
			return moduleDef.eContainer();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper, EStructuralFeature parentFeature)
	{
		throw new PMRuntimeException(
			"Cannot alter a module configuration using the presentation model"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#preNotifySingleAttrSet(org.eclipse.emf.ecore.EAttribute, java.lang.Object)
	 */
	@Override
	public void preNotifySingleAttrSet(EAttribute attr, Object newValue)
	{
		if (ATTR_SHORTNAME.equals(attr.getName())
			&& (newValue == null || newValue == EStructuralFeatureImpl.NIL))
		{
			throw new PMRuntimeException("Can not set a null shortName"); //$NON-NLS-1$
		}
		super.preNotifySingleAttrSet(attr, newValue);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#preNotifySingleAttrUnset(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public void preNotifySingleAttrUnset(EAttribute attr)
	{
		if (ATTR_SHORTNAME.equals(attr.getName())
			&& (attr.getDefaultValue() == null || attr.getDefaultValue() == EStructuralFeatureImpl.NIL))
		{
			throw new PMRuntimeException("Can not set a null shortName"); //$NON-NLS-1$
		}
		super.preNotifySingleAttrUnset(attr);
	}

}
