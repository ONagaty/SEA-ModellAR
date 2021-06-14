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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.CTProxyUtils;
import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.compat.sdk.IEREF;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public abstract class AbstractEREFFeatureWrapper<T> extends AbstractMasterFeatureWrapper<T>
{
	protected EObject masterReferringObject;
	protected EReference masterReference;
	protected HashMap<IEREF, EObject> erefCache;

	/**
	 * @param engine
	 */
	public AbstractEREFFeatureWrapper(IEMFProxyEngine engine, EObject masterReferringObject,
		EReference masterReference)
	{
		super(engine);
		this.masterReferringObject = masterReferringObject;
		this.masterReference = masterReference;
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

		// EClass masterReferenceType = masterReference.getEReferenceType();
		// if (!masterReferenceType.isInstance(newMasterDest))
		// {
		//				throw new InternalProxyConfigurationError("The type of " + value //$NON-NLS-1$
		//					+ "is not compatibile with " + masterReferenceType.getName()); //$NON-NLS-1$
		// }

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

		IEREF eref = (IEREF) EcoreUtil.create(CompatibilitySupport.getModelConstants(
			getEngine().getProject()).getEREFEClass());
		// after creation, the engine has to be explicitly set, otherwise the
		// wrong engine corresponding to PM will be set
		((EMFProxyObjectImpl) eref).eSetProxyEngine(this.engine);
		((EMFProxyObjectImpl) eref).eSetStore(this.engine.getStore());
		return eref;
	}

	// protected void initializeEREF(IEREF eref, EObject master)
	// {
	// eref.setValue(MetaModelUtils.getAbsoluteQualifiedName(master));
	// eref.setDest(BasicExtendedMetaData.INSTANCE.getName(master.eClass()));
	// // EAnnotation annotation =
	// // master.eClass().getEAnnotation(CommonConstants.EXTENDED_META_DATA);
	// // if (annotation != null)
	// // {
	//		//			String dest = annotation.getDetails().get("name"); //$NON-NLS-1$
	// // eref.setDest(dest);
	// // }
	//
	// }

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return ICompatConstants.EREF_CONTEXT;

	}
}
