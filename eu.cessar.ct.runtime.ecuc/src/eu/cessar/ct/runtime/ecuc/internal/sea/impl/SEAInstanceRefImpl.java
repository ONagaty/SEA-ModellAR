/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * May 26, 2013 6:57:42 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaInstanceReferencesHandler;
import eu.cessar.ct.sdk.sea.ISEAInstanceReference;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Implementation of the wrapper over an ECUC Instance reference value, used by the SEA
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 16:04:03 2013 %
 * 
 *         %version: 4 %
 */
public class SEAInstanceRefImpl extends AbstractSEAObject implements ISEAInstanceReference
{
	private final GInstanceReferenceValue iReferenceValue;

	/**
	 * @param seaModel
	 * @param iReferenceValue
	 * @param optionsHolder
	 */
	public SEAInstanceRefImpl(ISEAModel seaModel, GInstanceReferenceValue iReferenceValue,
		ISeaOptions optionsHolder)
	{
		super(seaModel, optionsHolder);
		this.iReferenceValue = iReferenceValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ISEAInstanceReference)
		{
			return ((ISEAInstanceReference) obj).getValue() == iReferenceValue;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#getTarget()
	 */
	@Override
	public GIdentifiable getTarget()
	{
		IEcucMMService ecucMMService = getMMService().getEcucMMService();
		return ecucMMService.getInstanceRefTarget(iReferenceValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#getContexts()
	 */
	@Override
	public EList<GIdentifiable> getContexts()
	{
		IEcucMMService ecucMMService = getMMService().getEcucMMService();
		return ecucMMService.getInstanceRefContext(iReferenceValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#setTarget(gautosar.ggenericstructure.ginfrastructure.GIdentifiable)
	 */
	@Override
	public ISEAInstanceReference setTarget(GIdentifiable target)
	{
		getSeaInstanceReferencesHandler().setTarget(iReferenceValue, target);

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#setContexts(java.util.List)
	 */
	@Override
	public ISEAInstanceReference setContexts(List<GIdentifiable> contexts)
	{
		getSeaInstanceReferencesHandler().setContexts(iReferenceValue, contexts);

		return this;
	}

	private SeaInstanceReferencesHandler getSeaInstanceReferencesHandler()
	{
		return SeaHandlersManager.getSeaInstanceReferencesHandler(getSEAModel(), getSeaOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#isSetTarget()
	 */
	@Override
	public boolean isSetTarget()
	{
		return getTarget() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#isSetContexts()
	 */
	@Override
	public boolean isSetContexts()
	{
		return getContexts().size() != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#unSetTarget()
	 */
	@Override
	public ISEAInstanceReference unSetTarget()
	{
		return setTarget(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#unSetContexts()
	 */
	@Override
	public ISEAInstanceReference unSetContexts()
	{
		return setContexts(new ArrayList<GIdentifiable>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAInstanceRef#getArValue()
	 */
	@Override
	public GInstanceReferenceValue getValue()
	{
		return iReferenceValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return super.hashCode();
	}

}
