/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.wrap.EREFObjectWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class EREFClassResolver implements IProxyClassResolver<EObject>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getMasterClassType(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends EObject> getMasterClassType(IEMFProxyEngine engine)
	{
		return (Class<? extends EObject>) CompatibilitySupport.getModelConstants(
			engine.getProject()).getMasterARObjectClass();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#resolveMaster(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object)
	 */
	public EObject resolveMaster(IEMFProxyEngine engine, Object context, EObject master)
	{
		return master;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#isValidMasterObject(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public boolean isValidMasterObject(IEMFProxyEngine engine, Object context, Object master,
		Map<String, Object> parameters)
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, EObject master,
		Map<String, Object> parameters)
	{
		return CompatibilitySupport.getModelConstants(engine.getProject()).getEREFEClass();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<? extends EObject> getWrapper(IEMFProxyEngine engine,
		Object context, IEMFProxyObject slave, EObject master, Map<String, Object> parameters)
	{
		EObject masterReferringObject = null;
		EReference masterReference = null;
		if (parameters != null)
		{
			masterReferringObject = (EObject) parameters.get(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT);
			masterReference = (EReference) parameters.get(ICompatConstants.KEY_EREF_MASTER_REFERENCE);
		}
		return new EREFObjectWrapper(engine, master, masterReferringObject, masterReference);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<? extends EObject> createWrapper(IEMFProxyEngine engine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		return new EREFObjectWrapper(engine, null, null, null);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#postCreateSlave(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	public void postCreateSlave(IEMFProxyEngine engine, Object context, EObject master,
		IEMFProxyObject slave)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getContainingFeature(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public EStructuralFeature getContainingFeature(IEMFProxyEngine engine,
		EMFProxyObjectImpl parentObject, EMFProxyObjectImpl childObject)
	{
		// childObject is an EREF, parentObject is a normal slave object
		Map<String, Object> parameters = childObject.eGetMasterWrapper().getParameters();
		if (parameters != null)
		{
			EReference masterReference = (EReference) parameters.get(ICompatConstants.KEY_EREF_MASTER_REFERENCE);
			if (masterReference != null)
			{
				EList<EReference> eAllReferences = parentObject.eClass().getEAllReferences();
				for (EReference eReference: eAllReferences)
				{
					String ar_feature = engine.getProxyElementAnnotation(eReference,
						ICompatConstants.ANN_KEY_AR_FEATURE);
					if (ar_feature != null && ar_feature.equals(masterReference.getName()))
					{
						return eReference;
					}
				}
			}
		}

		return null;
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
		return ICompatConstants.EREF_CONTEXT;
	}

}
