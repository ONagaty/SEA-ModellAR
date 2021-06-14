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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.AbstractCompatObjectWrapper;
import eu.cessar.ct.compat.internal.ctproxy.ICompatConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class IntermediateObjectWrapper extends AbstractCompatObjectWrapper<EObject>
{
	private EObject master;

	// private EClass intermediateSlaveType;

	/**
	 * @param master
	 * @param object
	 * @param engine2
	 * @param instances
	 * @param name
	 */
	public IntermediateObjectWrapper(IEMFProxyEngine engine, EObject master, Object object)
	{
		super(engine, EObject.class);
		this.master = master;
		// if (object != null)
		// {
		// this.intermediateSlaveType = (EClass) object;
		// }
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
		return master;
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
		return ICompatConstants.INTERMEDIATE_CONTEXT;
	}

}
