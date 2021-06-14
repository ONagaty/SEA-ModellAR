/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractSingleMasterReferenceWrapper;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleIntermediateFeatureWrapper extends AbstractSingleMasterReferenceWrapper
	implements IMasterReferenceWrapper
{
	private EObject intermediateNode;
	// private final EMFProxyObjectImpl slave;
	private final EReference slaveReference;
	private final EObject master;
	private final EReference masterReference;

	/**
	 * @param engine
	 */
	public SingleIntermediateFeatureWrapper(IEMFProxyEngine engine, /*EMFProxyObjectImpl slave,*/
		EReference slaveReference, EObject master, EReference masterReference)
	{
		super(engine);
		// this.slave = slave;
		this.slaveReference = slaveReference;
		this.master = master;
		this.masterReference = masterReference;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public EObject getValue()
	{
		if (intermediateNode != null && isSetValue())
		{
			return intermediateNode;
		}
		else
		{
			// signal that we need an intermediateClassResolver
			// pass the slavereference.type
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE,
				slaveReference.getEReferenceType());
			intermediateNode = getEngine().getSlaveObject(getContext(), master, parameters);
			((EMFProxyObjectImpl) intermediateNode).eSetProxyEngine(engine);
			return intermediateNode;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		if (newValue != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_INTERMEDIATE_SLAVE_TYPE,
				slaveReference.getEReferenceType());
			((EMFProxyObjectImpl) newValue).eSetProxyEngine(engine);
			// check for a previously set master (when a feature was set on a
			// proxy object that hadn't been added yet to the parent)
			List<?> masterObjects = ((EMFProxyObjectImpl) newValue).eGetMasterWrapper().getAllMasterObjects();
			if (masterObjects.size() > 0)
			{
				final EObject previousMaster = (EObject) masterObjects.get(0);
				if (previousMaster != null && master.eClass() == previousMaster.eClass())
				{
					EList<EStructuralFeature> features = previousMaster.eClass().getEAllStructuralFeatures();
					for (final EStructuralFeature feature: features)
					{
						if (previousMaster.eIsSet(feature) && !master.eIsSet(feature))
						{
							updateModel(new Runnable()
							{
								public void run()
								{
									master.eSet(feature, previousMaster.eGet(feature));
								}
							});
						}
					}
				}

			}
			engine.updateSlave(getContext(), (EMFProxyObjectImpl) newValue, master, parameters);
		}
		else
		{
			unsetValue();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return master.eIsSet(masterReference);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{

		updateModel(new Runnable()
		{
			public void run()
			{
				intermediateNode = null;
				master.eUnset(masterReference);
			}
		});

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return ICompatConstants.INTERMEDIATE_CONTEXT;
	}
}
