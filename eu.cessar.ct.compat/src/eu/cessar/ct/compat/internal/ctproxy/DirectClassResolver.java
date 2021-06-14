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

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.wrap.DirectObjectWrapper;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.utils.ProjectUtils;

/**
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class DirectClassResolver implements IProxyClassResolver<EObject>
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
	public boolean isValidMasterObject(IEMFProxyEngine engine, Object context, Object object,
		Map<String, Object> parameters)
	{
		if (parameters != null
			&& (parameters.get(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE) != null))
		{
			return false;
		}
		if (engine == null || engine.getProject() == null)
		{
			return false;
		}
		if (object == null)
		{
			return false;
		}
		IProject objProject = MetaModelUtils.getProject(object);

		if (objProject == null)
		{
			return getMasterClassType(engine).isInstance(object);
		}

		if (objProject == engine.getProject())
		{
			int autosarReleaseOrdinal = ProjectUtils.getAutosarReleaseOrdinal(objProject);
			if (autosarReleaseOrdinal == CompatibilitySupport.getModelConstants(objProject).getAutosarReleaseOrdinal()
				&& CESSARPreferencesAccessor.getCompatibilityMode(objProject) == ECompatibilityMode.FULL)
			{
				return getMasterClassType(engine).isInstance(object);
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, EObject master,
		Map<String, Object> parameters)
	{
		if (master != null)
		{
			EClass masterEClass = master.eClass();
			return CompatibilitySupport.getModelConstants(engine.getProject()).getSlaveEClass(
				masterEClass);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<? extends EObject> getWrapper(IEMFProxyEngine engine,
		Object context, IEMFProxyObject slave, EObject master, Map<String, Object> parameters)
	{
		return new DirectObjectWrapper(engine, master);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<? extends EObject> createWrapper(IEMFProxyEngine engine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		// look into emfProxyObjectImpl.eClass for the "Direct" annotation
		// parameters
		// locate Artop eClass and create an instance of the artop eClass (use
		// Autoasar21Package and Autoasar21Factory
		// return a DirectObjectWrapper(engine, newMaster)
		EClass emfProxyObjectEClass = emfProxyObjectImpl.eClass();
		String ar_package = engine.getProxyElementAnnotation(emfProxyObjectEClass,
			ICompatConstants.ANN_KEY_AR_PACKAGE);
		InternalProxyConfigurationError.assertTrue(ar_package != null, "Missing value of " //$NON-NLS-1$
			+ ICompatConstants.ANN_KEY_AR_PACKAGE
			+ " annotation for slave object " + emfProxyObjectImpl); //$NON-NLS-1$
		String ar_classifier = engine.getProxyElementAnnotation(emfProxyObjectEClass,
			ICompatConstants.ANN_KEY_AR_CLASSIFIER);

		EClassifier eClassifier = CompatibilitySupport.getModelConstants(engine.getProject()).getMasterRootPackage().getEClassifier(
			ar_classifier != null ? ar_classifier : emfProxyObjectEClass.getName());
		EObject eObject = CompatibilitySupport.getModelConstants(engine.getProject()).getMasterRootFactory().create(
			(EClass) eClassifier);
		return new DirectObjectWrapper(engine, eObject);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#postCreateSlave(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	public void postCreateSlave(IEMFProxyEngine engine, Object context, EObject master,
		IEMFProxyObject slave)
	{
		// nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getContainingFeature(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public EStructuralFeature getContainingFeature(IEMFProxyEngine engine,
		EMFProxyObjectImpl parentObject, EMFProxyObjectImpl childObject)
	{

		EList<EReference> eAllReferences = parentObject.eClass().getEAllReferences();
		for (EReference eReference: eAllReferences)
		{
			if (eReference.getEReferenceType().getInstanceClass().isAssignableFrom(
				childObject.getClass()))
			{
				return eReference;
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
		return IEMFProxyConstants.DEFAULT_CONTEXT;
	}
}
