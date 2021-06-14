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
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.CTProxyUtils;
import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * Wrapper for references of type EREF, considered as part of a group.
 * 
 * @Review uidl7321 - Apr 11, 2012
 */
public class MultiWrappedGroupEREFWrapper extends AbstractWrappedGroupWrapper<Object> implements
	IMultiMasterFeatureWrapper<Object>
{

	protected HashMap<IEREF, EObject> erefCache;
	private EList<EObject> masterValues;

	protected EObject getMasterReferredObjectForEREF(IEREF eRef)
	{
		if (erefCache.get(eRef) != null)
		{
			return erefCache.get(eRef);
		}

		String value = eRef.getValue();
		final EObject newMasterReferredObject = CTProxyUtils.getRealOrProxyObject(
			engine.getProject(),
			value,
			eRef.getDest(),
			(getMasterWrappedUsageFeature() != null) ? ((EReference) getMasterWrappedUsageFeature()).getEReferenceType()
				: null);
		return newMasterReferredObject;
	}

	private IEREF getEREFForMasterReferredObject(EObject master)
	{

		Set<IEREF> erefs = erefCache.keySet();
		for (IEREF eref: erefs)
		{
			if (erefCache.get(eref) == master)
			{
				return eref;
			}
		}

		return (IEREF) EcoreUtil.create(CompatibilitySupport.getModelConstants(
			getEngine().getProject()).getEREFEClass());
	}

	// private void initializeEREF(IEREF eref, EObject master)
	// {
	// eref.setValue(MetaModelUtils.getAbsoluteQualifiedName(master));
	// eref.setDest(BasicExtendedMetaData.INSTANCE.getName(master.eClass()));
	// }

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return ICompatConstants.EREF_CONTEXT;
	}

	/**
	 * @param engine
	 * @param intermediateSlaveNode
	 * @param masterWrappedUsage
	 * @param masterWrappedUsageFeature
	 */
	@SuppressWarnings("unchecked")
	public MultiWrappedGroupEREFWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl intermediateSlaveNode, EObject masterWrappedUsage,
		EStructuralFeature masterWrappedUsageFeature)
	{
		super(engine, intermediateSlaveNode, masterWrappedUsage, masterWrappedUsageFeature);
		masterValues = (EList<EObject>) masterWrappedUsage.eGet(masterWrappedUsageFeature);
		erefCache = new HashMap<IEREF, EObject>();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<Object> getFeatureClass()
	{
		return Object.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		int hashCode = 1;
		Object[] array = toArray();
		for (Object object: array)
		{
			hashCode = 31 * hashCode + (object == null ? 0 : object.hashCode());
		}
		return hashCode;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public Object get(int index)
	{
		EObject masterValue = masterValues.get(index);

		// find the corresponding slave
		if (masterValue != null)
		{
			IEREF eref = getEREFForMasterReferredObject(masterValue);
			erefCache.put(eref, masterValue);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT,
				getMasterWrappedUsage());
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE,
				getMasterWrappedUsageFeature());
			engine.updateSlave(getContext(), (EMFProxyObjectImpl) eref, masterValue, parameters);
			// initializeEREF(eref, masterValue);

			// search in intermediateNode the corresponding feature for the
			// slave object
			EList<EReference> eAllReferences = getIntermediateSlaveNode().eClass().getEAllReferences();
			InternalProxyConfigurationError.assertTrue(eAllReferences.size() == 1,
				"Expected one EREF feature in the wrapped group from " + getIntermediateSlaveNode()); //$NON-NLS-1$

			return new EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry(
				(EReferenceImpl) eAllReferences.get(0), (EMFProxyObjectImpl) eref);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#isEmpty()
	 */
	public boolean isEmpty()
	{
		return masterValues == null || masterValues.isEmpty();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#size()
	 */
	public int size()
	{
		if (masterValues == null)
		{
			return 0;
		}
		return masterValues.size();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray()
	 */
	public Object[] toArray()
	{
		throw new UnsupportedOperationException();// unreachable code
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray(S[])
	 */
	public <S> S[] toArray(S[] array)
	{
		throw new UnsupportedOperationException();// unreachable code
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object slaveValue)
	{
		throw new UnsupportedOperationException();// unreachable code
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object slaveValue)
	{
		throw new UnsupportedOperationException();// unreachable code
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	public void clear()
	{
		throw new UnsupportedOperationException();// unreachable code
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, Object value)
	{
		if (value != null)
		{
			IEREF slaveValue = (IEREF) ((FeatureMap.Entry) value).getValue();
			final EObject[] masterValue = {getMasterReferredObjectForEREF(slaveValue)};
			erefCache.put(slaveValue, masterValue[0]);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT,
				getMasterWrappedUsage());
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE,
				getMasterWrappedUsageFeature());
			engine.updateSlave(getContext(), (EMFProxyObjectImpl) slaveValue, masterValue[0],
				parameters);
			updateModel(new Runnable()
			{
				public void run()
				{
					masterValues.set(index, masterValue[0]);
				}
			});
		}

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, Object value)
	{
		if (value != null)
		{
			IEREF slaveValue = (IEREF) ((FeatureMap.Entry) value).getValue();
			final EObject[] masterValue = {getMasterReferredObjectForEREF(slaveValue)};
			erefCache.put(slaveValue, masterValue[0]);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT,
				getMasterWrappedUsage());
			parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE,
				getMasterWrappedUsageFeature());
			engine.updateSlave(getContext(), (EMFProxyObjectImpl) slaveValue, masterValue[0],
				parameters);
			updateModel(new Runnable()
			{
				public void run()
				{
					masterValues.add(index, masterValue[0]);
				}
			});
		}

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(final int index)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				masterValues.remove(index);
			}
		});

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
	 */
	public Object move(final int targetIndex, final int sourceIndex)
	{
		final Object[] result = {null};
		updateModel(new Runnable()
		{

			public void run()
			{
				result[0] = masterValues.move(targetIndex, sourceIndex);
			}
		});
		return get(sourceIndex);
	}

}
