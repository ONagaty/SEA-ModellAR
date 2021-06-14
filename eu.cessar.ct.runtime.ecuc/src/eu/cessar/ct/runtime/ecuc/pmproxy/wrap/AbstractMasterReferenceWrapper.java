/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:48:56 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractMasterReferenceWrapper extends AbstractMasterFeatureWrapper<EObject>
	implements IMasterReferenceWrapper
{

	/**
	 * @param engine
	 */
	public AbstractMasterReferenceWrapper(IEMFProxyEngine engine)
	{
		super(engine);
	}

}
