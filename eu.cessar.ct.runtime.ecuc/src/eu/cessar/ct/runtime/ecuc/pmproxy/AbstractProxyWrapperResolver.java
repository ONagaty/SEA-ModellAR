/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 19, 2010 12:23:49 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;

/**
 * 
 */
public abstract class AbstractProxyWrapperResolver implements IEMFProxyConstants
{

	public final static String ATTR_URI = "URI"; //$NON-NLS-1$

	public final static String ATTR_VALUE = "VALUE"; //$NON-NLS-1$

	public final static String ATTR_TARGET = "TARGET"; //$NON-NLS-1$

	public final static String ATTR_NAME = "NAME"; //$NON-NLS-1$

	public final static String ATTR_ECUCTYPE = "ECUCTYPE"; //$NON-NLS-1$

	public final static String ATTR_CONTAINING_FEATURE = "eContainingFeature"; //$NON-NLS-1$

	/**
	 * @param engine
	 * @return
	 */
	protected IEcucPresentationModel getEcucPresentationModel(IEMFProxyEngine engine)
	{
		return IEcucCore.INSTANCE.getEcucPresentationModel(engine.getProject());
	}

	/**
	 * @param engine
	 * @return
	 */
	protected IEcucModel getEcucModel(IEMFProxyEngine engine)
	{
		return IEcucCore.INSTANCE.getEcucModel(engine.getProject());
	}
}
