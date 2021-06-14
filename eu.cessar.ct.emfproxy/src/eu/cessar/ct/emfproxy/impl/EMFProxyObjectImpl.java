/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Nov 24, 2009 5:36:32 PM </copyright>
 */
package eu.cessar.ct.emfproxy.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EStoreEObjectImpl;
import org.eclipse.emf.edit.domain.EditingDomain;

import eu.cessar.ct.core.platform.emf.IAltEditingDomainProvider;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IProxifiedEPackage;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.IProxyOperationResolver;
import eu.cessar.ct.emfproxy.internal.EMFProxyEngine;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * @author uidl6458
 *
 */
public class EMFProxyObjectImpl extends EStoreEObjectImpl implements IEMFProxyObject, IAltEditingDomainProvider
{

	private static final IMasterFeatureWrapper<?>[] ENO_WRAPPEDFEATURES = new IMasterFeatureWrapper[0];

	private IMasterObjectWrapper<?> eMasterObjectWrapper;

	private IMasterFeatureWrapper<?>[] eCachedWrappedValues;

	private IProxyClassResolver<?> resolver;

	private IEMFProxyEngine proxyEngine;

	private Object context = IEMFProxyConstants.DEFAULT_CONTEXT;

	/**
	 * The protected constructor of the proxy object. It sets the store and the class resolver
	 */
	protected EMFProxyObjectImpl()
	{
		super();
		EClass eClass = eClass();
		if (eClass != null)
		{
			if (eClass.getEPackage() instanceof IProxifiedEPackage)
			{
				IProxifiedEPackage pack = (IProxifiedEPackage) eClass.getEPackage();
				proxyEngine = pack.eGetProxyEngine();
				eSetStore(proxyEngine.getStore());
			}
			else
			{
				// locate the proxyEngine by other means
				String nsURI = eClass.getEPackage().getNsURI();
				proxyEngine = EMFProxyEngine.getEngineForCurrentThread(nsURI);
				eSetStore(proxyEngine.getStore());
			}
		}
	}

	/**
	 * @return
	 */
	public IEMFProxyEngine eGetProxyEngine()
	{
		return proxyEngine;
	}

	/**
	 * @param proxyEngine
	 */
	public void eSetProxyEngine(IEMFProxyEngine proxyEngine)
	{
		this.proxyEngine = proxyEngine;
	}

	/**
	 *
	 */
	public void eDoInit()
	{
		if (eMasterObjectWrapper == null)
		{
			proxyEngine.lookupContext(this);
			proxyEngine.createObjectWrapper(context, this);
			// it's a new object not saved into the disk model
			eContainer = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.impl.EObjectImpl#eBasicSetContainer(org.eclipse.emf.ecore.InternalEObject, int)
	 */
	@Override
	protected void eBasicSetContainer(InternalEObject newContainer, int newContainerFeatureID)
	{
		EReference feature = null;
		if (newContainer == null)
		{
			feature = eContainmentFeature();
		}
		InternalEObject prevContainer = eInternalContainer();
		super.eBasicSetContainer(newContainer, newContainerFeatureID);
		// notify the master object wrapper that the container has changed
		// IMasterObjectWrapper<?> parentWrapper = null;
		// if (newContainer instanceof EMFProxyObjectImpl)
		// {
		// parentWrapper = ((EMFProxyObjectImpl)
		// newContainer).eGetMasterWrapper();
		// }
		if (feature == null)
		{
			feature = eContainmentFeature();
		}
		// eMasterObjectWrapper.setContainer(parentWrapper, feature);
		if (prevContainer instanceof EMFProxyObjectImpl)
		{
			((EMFProxyObjectImpl) prevContainer).eSetCachedFeatureWrapper(feature, null);
		}
	}

	/**
	 * @param resolver
	 */
	public void eSetProxyClassResolver(IProxyClassResolver<?> resolver)
	{
		this.resolver = resolver;
	}

	/**
	 * @return
	 */
	public IProxyClassResolver<?> eGetProxyClassResolver()
	{
		if (resolver == null)
		{
			resolver = proxyEngine.getClassResolver(eClass());
			// if (resolver == null)
			// {
			// // maybe wrong engine
			// AutosarReleaseDescriptor autosarRelease =
			// IAutosarWorkspacePreferences.AUTOSAR_RELEASE.get(proxyEngine.getProject());
			// if (autosarRelease != null)
			// {
			// String masterURI = autosarRelease.getNamespace();
			// IEMFProxyEngine engine2 =
			// EMFProxyRegistry.eINSTANCE.getEMFProxyEngine(
			// masterURI, "http://cessar.eu/MM_Compat/3x",
			// proxyEngine.getProject());
			// if (engine2 != null)
			// {
			// resolver = engine2.getClassResolver(eClass());
			// }
			// }
			// }
		}
		return resolver;
	}

	/**
	 * @param feature
	 * @return
	 */
	public IMasterFeatureWrapper<?> eGetCachedFeatureWrapper(EStructuralFeature feature)
	{
		if (eCachedWrappedValues == null)
		{
			return null;
		}
		else
		{
			return eCachedWrappedValues[eDerivedStructuralFeatureID(feature)];
		}
	}

	/**
	 * @param feature
	 * @param wrapper
	 */
	public IMasterFeatureWrapper<?> eSetCachedFeatureWrapper(EStructuralFeature feature,
		IMasterFeatureWrapper<?> wrapper)
	{
		if (eCachedWrappedValues == null && wrapper == null)
		{
			return null;
		}
		if (eCachedWrappedValues == null)
		{
			int size = eClass().getFeatureCount() - eStaticFeatureCount();
			eCachedWrappedValues = size == 0 ? ENO_WRAPPEDFEATURES : new IMasterFeatureWrapper[size];
		}
		int featureID = eDerivedStructuralFeatureID(feature);
		IMasterFeatureWrapper<?> oldWrapper = eCachedWrappedValues[featureID];
		eCachedWrappedValues[featureID] = wrapper;
		if (oldWrapper != null)
		{
			oldWrapper.setProxyObject(null);
			oldWrapper.setWrappedFeature(null);
		}
		if (wrapper != null)
		{
			wrapper.setProxyObject(this);
			wrapper.setWrappedFeature(feature);
		}
		return oldWrapper;
	}

	/**
	 * @return
	 */
	public IMasterObjectWrapper<?> eGetMasterWrapper()
	{
		eDoInit();
		return eMasterObjectWrapper;
	}

	/**
	 * @return
	 */
	public void eSetMasterWrapper(IMasterObjectWrapper<?> wrapper)
	{
		if (eMasterObjectWrapper != null)
		{
			eMasterObjectWrapper.setProxyObject(null);
		}
		eMasterObjectWrapper = wrapper;
		if (wrapper != null)
		{
			wrapper.setProxyObject(this);
		}
		// clear feature cache, it's not relevant anymore
		eCachedWrappedValues = null;
	}

	/**
	 * @param newContext
	 */
	public void eSetContext(Object newContext)
	{
		context = newContext;
	}

	/**
	 * @return
	 */
	public Object eGetContext()
	{
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.impl.BasicEObjectImpl#eResolveProxy(org.eclipse.emf.ecore.InternalEObject)
	 */
	@Override
	public EObject eResolveProxy(InternalEObject proxy)
	{
		EObject result = null;
		if (proxy == null)
		{
			return null;
		}
		else if (proxy instanceof IEMFProxyObject)
		{
			result = super.eResolveProxy(proxy);
		}
		else
		{
			result = proxy.eResolveProxy(proxy);
		}
		if (null != result && result.eIsProxy())
		{
			result = proxy;
		}
		return result;
	}

	/**
	 * @param eOperation
	 * @param qualifiedName
	 * @return
	 */
	protected Object eExecOperation(EOperation eOperation, Object... params)
	{
		IProxyOperationResolver<?> opResolver = proxyEngine.getOperationResolver(eOperation);
		if (opResolver == null)
		{
			throw new InternalProxyConfigurationError("Cannot locate resolver for operation: " //$NON-NLS-1$
				+ eOperation.getName());
		}
		return opResolver.executeOperation(proxyEngine, this, eOperation, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.emf.IAltEditingDomainProvider#eGetEditingDomain()
	 */
	public EditingDomain eGetEditingDomain()
	{
		IMasterObjectWrapper<?> masterWrapper = eGetMasterWrapper();
		if (masterWrapper != null)
		{
			return masterWrapper.getEditingDomain();
		}

		return null;
	}
}
