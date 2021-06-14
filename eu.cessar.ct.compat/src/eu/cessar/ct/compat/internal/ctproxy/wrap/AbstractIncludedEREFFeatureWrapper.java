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
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.CTProxyUtils;
import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public abstract class AbstractIncludedEREFFeatureWrapper<T> extends AbstractIncludedWrapper
{
	protected EObject masterReferringObject;
	protected EReference masterReference;
	protected HashMap<IEREF, EObject> erefCache;

	/**
	 * @param engine
	 */
	public AbstractIncludedEREFFeatureWrapper(IEMFProxyEngine engine, EObject master,
		EStructuralFeature lvl1MasterFeature, EClass lvl1ExpectedType,
		EStructuralFeature finalMasterFeature)
	{
		super(engine, master, lvl1MasterFeature, lvl1ExpectedType, null, null, finalMasterFeature);
		this.masterReferringObject = (EObject) master.eGet(lvl1MasterFeature);
		this.masterReference = (EReference) finalMasterFeature;
		erefCache = new HashMap<IEREF, EObject>();
	}

	protected EObject getMasterReferredObjectForEREF(IEREF eRef)
	{
		if (erefCache.get(eRef) != null)
		{
			return erefCache.get(eRef);
		}

		String value = eRef.getValue();
		final EObject newMasterReferredObject = CTProxyUtils.getRealOrProxyObject(
			engine.getProject(), value, eRef.getDest(),
			(masterReference != null) ? masterReference.getEReferenceType() : null);
		return newMasterReferredObject;
	}

	protected IEREF getEREFForMasterReferredObject(EObject master)
	{

		Set<IEREF> erefs = erefCache.keySet();
		for (IEREF eref: erefs)
		{
			if (erefCache.get(eref).equals(master))
			{
				return eref;
			}
		}

		return (IEREF) EcoreUtil.create(CompatibilitySupport.getModelConstants(
			getEngine().getProject()).getEREFEClass());
	}

	protected void initializeEREF(IEREF eref, EObject master)
	{
		eref.setValue(MetaModelUtils.getAbsoluteQualifiedName(master));
		eref.setDest(BasicExtendedMetaData.INSTANCE.getName(master.eClass()));
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return ICompatConstants.EREF_CONTEXT;
	}
}
