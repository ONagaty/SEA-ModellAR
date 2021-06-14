/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.AbstractCompatObjectWrapper;
import eu.cessar.ct.compat.internal.ctproxy.CTProxyUtils;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class DirectObjectWrapper extends AbstractCompatObjectWrapper<EObject>
{

	private final EObject master;

	public DirectObjectWrapper(IEMFProxyEngine engine, EObject master)
	{
		super(engine, EObject.class);
		this.master = master;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getAllMasterObjects()
	 */
	public List<EObject> getAllMasterObjects()
	{
		return Collections.singletonList(master);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterContainer()
	 */
	public Object getMasterContainer()
	{
		return master.eContainer();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.compat.ctproxy.AbstractCompatObjectWrapper#getSlaveContainer(eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	@Override
	public IEMFProxyObject getSlaveContainer(IEMFProxyObject proxyObject)
	{
		EMFProxyObjectImpl slaveChild = (EMFProxyObjectImpl) proxyObject;
		List<Object> masterObjects = engine.getMasterObjects(slaveChild);
		InternalProxyConfigurationError.assertTrue(masterObjects.size() > 0,
			"No master object found for the slave object " + slaveChild); //$NON-NLS-1$

		EObject masterChild = (EObject) masterObjects.get(0);
		EStructuralFeature masterFeature = masterChild.eContainingFeature();
		if (masterFeature != null)
		{
			String masterFeatureName = masterFeature.getName();

			EObject masterParent = masterChild.eContainer();
			IEMFProxyObject slaveParent = engine.getSlaveObject(getContext(), masterParent);
			// slaveParent not necessary the direct eContainer for
			// slaveChild
			EStructuralFeature slaveFeature = null;
			EList<EStructuralFeature> eAllStructuralFeatures = slaveParent.eClass().getEAllStructuralFeatures();
			for (EStructuralFeature feature: eAllStructuralFeatures)
			{
				// search the slave feature that maps to the master feature
				String arFeatureAnnotation = CTProxyUtils.getARFeatureAnnotation(feature,
					masterFeatureName);
				if (arFeatureAnnotation != null && arFeatureAnnotation.equals(masterFeatureName))
				{

					slaveFeature = feature;
					break;
				}
			}
			InternalProxyConfigurationError.assertTrue(
				slaveFeature != null,
				"Cannot find the slave feature of the object " + slaveParent + "parent of " + proxyObject//$NON-NLS-1$ //$NON-NLS-2$
					+ " corresponding to the master feature " + masterFeatureName); //$NON-NLS-1$
			// get the type
			String type = engine.getProxyElementAnnotation(slaveFeature,
				IEMFProxyConstants.ANN_KEY_TYPE);
			if (type.equals("Direct")) //$NON-NLS-1$
			{
				return slaveParent;
			}
			if (type.equals("Intermediate")) //$NON-NLS-1$
			{
				return (IEMFProxyObject) slaveParent.eGet(slaveFeature);
			}
			throw new InternalProxyConfigurationError(
				"Type of the slave feature of the object " + slaveParent + "parent of " + proxyObject//$NON-NLS-1$ //$NON-NLS-2$
					+ " corresponding to the master feature " + masterFeatureName + ",not Direct or Intermediate as expected, but " + type); //$NON-NLS-1$ //$NON-NLS-2$);

		}

		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainerX(IMasterObjectWrapper<?> parentWrapper,
		final EStructuralFeature parentFeature)
	{
		final IMasterFeatureWrapper<?>[] removeFrom = {null};
		final IMasterFeatureWrapper<?>[] addTo = {null};

		EMFProxyObjectImpl currentSlave = this.getProxyObject();
		EReference currentSlaveFeature = currentSlave.eContainmentFeature();
		removeFrom[0] = engine.getMasterFeatureWrapper(currentSlave, currentSlaveFeature, false);
		if (parentWrapper != null)
		{
			EMFProxyObjectImpl newSlave = parentWrapper.getProxyObject();
			addTo[0] = engine.getMasterFeatureWrapper(newSlave, parentFeature, true);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper,
		final EStructuralFeature parentFeature)
	{
		// setContainerX(parentWrapper, parentFeature);
		if (master == null)
		{
			// do nothing
			return;
		}

		// status of data variables when moving slave AdminData from one slave
		// AUTOSAR(1) to another slave AUTOSAR(2)

		// this.getAllMasterObjects().get(0) is master AdminData
		// this.getProxyObject() is slave AdminData
		EMFProxyObjectImpl slave = this.getProxyObject();

		// this.getMasterContainer() is master AUTOSAR(1)
		// this.getProxyObject().eContainer() is slave AUTOSAR(1)
		final EObject currentMasterEContainer = (EObject) this.getMasterContainer();
		EMFProxyObjectImpl currentSlaveEContainer = (EMFProxyObjectImpl) this.getProxyObject().eContainer();
		// this.getProxyObject().eContainmentFeature() belong to slave
		// AUTOSAR(1)
		EReference slaveFeature = slave.eContainmentFeature();

		// ((EMFProxyObjectImpl)this.getProxyObject().eContainer()).eGetCachedFeatureWrapper(this.getProxyObject().eContainmentFeature()).getWrappedFeature()
		// belong to master AUTOSAR(1)

		final Object[] masterRemoveFromFeature = new Object[1];// [0]=masterFeature

		if (currentSlaveEContainer != null && currentMasterEContainer != null)
		{
			IMasterFeatureWrapper<?> featureWrapper = currentSlaveEContainer.eGetCachedFeatureWrapper(slaveFeature);
			if (featureWrapper != null)
			{
				masterRemoveFromFeature[0] = master.eContainmentFeature();
				// featureWrapper.getWrappedFeature();
			}
		}

		// parentWrapper.getAllMasterObjects().get(0) is master AUTOSAR(2)
		// parentWrapper.getProxyObject() is slave AUTOSAR(2)
		EMFProxyObjectImpl slaveNewEContainer = null;
		EObject newParent = null;
		if (parentWrapper != null)
		{
			slaveNewEContainer = parentWrapper.getProxyObject();
			if (!parentWrapper.getAllMasterObjects().isEmpty())
			{
				newParent = (EObject) parentWrapper.getAllMasterObjects().get(0);
			}
		}
		final EObject masterNewEContainer = newParent;

		final Object[] masterAddToFeature = new Object[1];// [0]=masterNewFeature
		// parentFeature belong to slave AUTOSAR(2)
		if (slaveNewEContainer != null && masterNewEContainer != null)
		{
			String masterNewFeatureName = CTProxyUtils.getARFeatureAnnotation(parentFeature,
				parentFeature.getName());
			masterAddToFeature[0] = masterNewEContainer.eClass().getEStructuralFeature(
				masterNewFeatureName);
		}
		if (masterRemoveFromFeature[0] == null && masterAddToFeature[0] == null)
		{
			// stop now, nothing to do
			return;
		}
		updateModel(new Runnable()
		{

			// cases:remove from current, remove from current and move to new,
			// add to new
			public void run()
			{
				// remove from current
				if (masterRemoveFromFeature[0] != null)
				{
					if (currentMasterEContainer.eIsSet((EStructuralFeature) masterRemoveFromFeature[0]))
					{
						removeValue(currentMasterEContainer,
							(EStructuralFeature) masterRemoveFromFeature[0], master);
					}
				}

				// add to new
				if (masterAddToFeature[0] != null)
				{
					addValue(masterNewEContainer, (EStructuralFeature) masterAddToFeature[0],
						master);
				}

			}

		});
		// we have to re-run the query
		getEngine().updateSlave(IEMFProxyConstants.DEFAULT_CONTEXT, slave, master);
		if (currentSlaveEContainer != null && currentMasterEContainer != null)
		{
			getEngine().updateSlave(IEMFProxyConstants.DEFAULT_CONTEXT, currentSlaveEContainer,
				currentMasterEContainer);
		}
		if (slaveNewEContainer != null && masterNewEContainer != null)
		{
			getEngine().updateSlave(IEMFProxyConstants.DEFAULT_CONTEXT, slaveNewEContainer,
				masterNewEContainer);
		}
	}

	@SuppressWarnings("unchecked")
	private void removeValue(EObject arObject, EStructuralFeature feature, EObject valueToRemove)
	{
		if (feature.isMany())
		{
			((EList<EObject>) arObject.eGet(feature)).remove(valueToRemove);
			// ((EList<ARObject>) values).remove(valueToRemove);
		}
		else
		{
			arObject.eUnset(feature);
		}
	}

	@SuppressWarnings("unchecked")
	private void addValue(EObject arObject, EStructuralFeature feature, EObject valueToAdd)
	{
		if (feature.isMany())
		{
			((EList<EObject>) arObject.eGet(feature)).add(valueToAdd);
			// ((EList<ARObject>) values).add(valueToAdd);
		}
		else
		{
			arObject.eSet(feature, valueToAdd);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return IEMFProxyConstants.DEFAULT_CONTEXT;
	}
}
