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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.AbstractCompatObjectWrapper;
import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class EREFObjectWrapper extends AbstractCompatObjectWrapper<EObject>
{

	private final EObject masterReferredObject;
	private EObject masterReferringObject;
	private EReference masterReference;

	/**
	 * @param engine
	 * @param masterClass
	 */
	public EREFObjectWrapper(IEMFProxyEngine engine, EObject masterReferredObject,
		EObject masterReferringObject, EReference masterReference)
	{
		super(engine, EObject.class);
		this.masterReferredObject = masterReferredObject;
		this.masterReferringObject = masterReferringObject;
		this.masterReference = masterReference;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getAllMasterObjects()
	 */
	public List<EObject> getAllMasterObjects()
	{
		return Collections.singletonList(masterReferredObject);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterContainer()
	 */
	public Object getMasterContainer()
	{
		Map<String, Object> parameters = getParameters();
		if (parameters != null)
		{
			return parameters.get(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper, EStructuralFeature parentFeature)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return IEMFProxyConstants.DEFAULT_CONTEXT;
		// return ICompatConstants.EREF_CONTEXT;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterObjectWrapper#getParameters()
	 */
	@Override
	public Map<String, Object> getParameters()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT, masterReferringObject);
		parameters.put(ICompatConstants.KEY_EREF_MASTER_REFERENCE, masterReference);
		return parameters;
	}

}
