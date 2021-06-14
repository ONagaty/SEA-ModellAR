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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.wrap.IntermediateObjectWrapper;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.utils.ProjectUtils;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class IntermediateClassResolver implements IProxyClassResolver<EObject>
{
	private EClass intermediateSlaveType;

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
			&& parameters.get(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE) == null)
		{
			return false;
		}

		if (parameters != null
			&& parameters.get(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE) != null)
		{
			intermediateSlaveType = (EClass) parameters.get(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE);
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
		if (parameters != null
			&& parameters.get(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE) != null)
		{
			intermediateSlaveType = (EClass) parameters.get(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE);
			return intermediateSlaveType;
		}
		// if (master != null)
		// {
		// EClass masterEClass = master.eClass();
		// return CompatAutosar21Utils.getSlaveEClass(masterEClass);
		// }
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<? extends EObject> getWrapper(IEMFProxyEngine engine,
		Object context, IEMFProxyObject slave, EObject master, Map<String, Object> parameters)
	{
		return new IntermediateObjectWrapper(engine, master,
			(parameters != null ? parameters.get(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE)
				: null));
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<? extends EObject> createWrapper(IEMFProxyEngine engine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		// EClass emfProxyObjectEClass = emfProxyObjectImpl.eClass();
		// String usage = engine.getProxyElementAnnotation(emfProxyObjectEClass,
		// "USAGE");
		//		InternalProxyConfigurationError.assertTrue(usage != null, "Missing value of " //$NON-NLS-1$
		//			+ "USAGE" + " annotation for slave object " + emfProxyObjectImpl); //$NON-NLS-1$
		//
		// List<String> classifiers = new ArrayList<String>();
		//		String[] fullClassifiers = usage.split(","); //$NON-NLS-1$
		// for (String fullClassifier: fullClassifiers)
		// {
		//			int index = usage.indexOf("#"); //$NON-NLS-1$
		// InternalProxyConfigurationError.assertTrue(
		// index != -1,
		//				"Missing # in the annotation " + "USAGE" + " for the slave object " + emfProxyObjectImpl); //$NON-NLS-1$//$NON-NLS-2$
		// classifiers.add(fullClassifier.substring(index + 1));
		// }
		//
		// // create instances of the classifiers
		// List<GARObject> instances = new ArrayList<GARObject>();
		// for (String classifierName: classifiers)
		// {
		// EClass eClassifier =
		// CompatibilitySupport.getModelConstants(engine.getProject()).getMasterEClass(
		// classifierName);
		// instances.add((GARObject) EcoreUtil.create(eClassifier));
		// }
		//
		// // emfProxyObjectImpl PARAMETERSType
		// // see usage autosar21 ParamConfContainerDef
		// // search slave ParamConfContainerDef for feature of type
		// PARAMETERSType
		// // look in AR_FEATURE(-> autosar 21 parameters)
		// EClass slaveEClassForUsage =
		// CompatibilitySupport.getModelConstants(engine.getProject()).getSlaveEClass(
		// instances.get(0).eClass());
		//
		// EStructuralFeature slaveFeatureForUsage = null;
		// EStructuralFeature masterFeatureForUsage = null;
		//
		// EList<EStructuralFeature> eAllStructuralFeatures =
		// slaveEClassForUsage.getEAllStructuralFeatures();
		// for (EStructuralFeature feature: eAllStructuralFeatures)
		// {
		// if (feature instanceof EReference)
		// {
		// if (((EReference) feature).getEReferenceType() ==
		// emfProxyObjectEClass)
		// {
		// slaveFeatureForUsage = feature;
		// }
		// }
		//
		// }
		// if (slaveFeatureForUsage != null)
		// {
		// String masterFeatureNameForUsage = engine.getProxyElementAnnotation(
		// slaveFeatureForUsage, ICompatConstants.ANN_KEY_AR_FEATURE);
		// masterFeatureForUsage =
		// instances.get(0).eClass().getEStructuralFeature(
		// masterFeatureNameForUsage);
		// }
		//
		// EObject master = EcoreUtil.create((EClass)
		// masterFeatureForUsage.getEType());

		return new IntermediateObjectWrapper(engine, null, intermediateSlaveType);
	}

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
