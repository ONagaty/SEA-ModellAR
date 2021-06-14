/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 02.04.2013 11:41:30
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.ISEAObject;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;

/**
 * Base implementation of a SEA object
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Aug 23 15:54:24 2013 %
 * 
 *         %version: 4 %
 */
public abstract class AbstractSEAObject implements ISEAObject
{
	private final ISEAModel seaModel;
	private ISeaOptions optionsHolder;

	/**
	 * @param seaModel
	 * @param optionsHolder
	 */
	public AbstractSEAObject(ISEAModel seaModel, ISeaOptions optionsHolder)
	{
		this.seaModel = seaModel;
		this.optionsHolder = optionsHolder;
	}

	/**
	 * @return the meta-model service
	 */
	protected IMetaModelService getMMService()
	{
		return MMSRegistry.INSTANCE.getMMService(getSEAModel().getProject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAObject#getSEAModel()
	 */
	@Override
	public ISEAModel getSEAModel()
	{
		return seaModel;
	}

	/**
	 * @return sea options holder
	 */
	protected ISeaOptions getSeaOptionsHolder()
	{
		return optionsHolder;
	}

}
