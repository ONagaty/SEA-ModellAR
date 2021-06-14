/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 13, 2013 8:16:06 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import java.util.Map;

import eu.cessar.ct.sdk.sea.util.ISEAErrorHandler;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.sea.util.SEADefaultErrorHandler;
import eu.cessar.ct.sdk.sea.util.SEAModelUtil;

/**
 * Such an instance is available for each Sea model
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:52 2015 %
 * 
 *         %version: 3 %
 */
public class SeaOptions implements ISeaOptions
{
	private ISEAErrorHandler errorHandler;
	private boolean autoSave;
	private boolean readOnly;
	private boolean reuseFragment;

	/**
	 * @param params
	 */
	public SeaOptions(Map<String, Object> params)
	{
		initializeOptions();
		readParameters(params);
	}

	/**
	 * 
	 */
	private void initializeOptions()
	{
		errorHandler = new SEADefaultErrorHandler();
		autoSave = false;
		readOnly = true;
		reuseFragment = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaOptionsHolder#getErrorhandler()
	 */
	@Override
	public ISEAErrorHandler getErrorHandler()
	{
		return errorHandler;
	}

	/**
	 * @param params
	 */
	private void readParameters(Map<String, Object> params)
	{
		if (params == null)
		{
			return;
		}

		Object object = params.get(SEAModelUtil.ERROR_HANDLER);
		if (object instanceof ISEAErrorHandler)
		{
			errorHandler = (ISEAErrorHandler) object;
		}

		object = params.get(SEAModelUtil.AUTO_SAVE);
		if (object instanceof Boolean)
		{
			autoSave = (Boolean) object;
		}

		object = params.get(SEAModelUtil.READ_ONLY);
		if (object instanceof Boolean)
		{
			readOnly = (Boolean) object;
		}

		object = params.get(SEAModelUtil.REUSE_FRAGMENT);
		if (object instanceof Boolean)
		{
			reuseFragment = (Boolean) object;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaOptionsHolder#isAutoSave()
	 */
	@Override
	public boolean isAutoSave()
	{
		return autoSave;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaOptionsHolder#isReadOnly()
	 */
	@Override
	public boolean isReadOnly()
	{
		return readOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ISeaOptions)
		{
			ISeaOptions optionsStore = (ISeaOptions) obj;

			boolean autoSave2 = optionsStore.isAutoSave();
			boolean readOnly2 = optionsStore.isReadOnly();
			boolean reuseFragment2 = optionsStore.reuseFragment();
			ISEAErrorHandler errorHandler2 = optionsStore.getErrorHandler();

			if (autoSave == autoSave2 && readOnly == readOnly2 && reuseFragment == reuseFragment2)
			{
				if (isDefault(errorHandler) && isDefault(errorHandler2))
				{
					return true;
				}

				return errorHandler == errorHandler2;
			}

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hash = 1;
		hash = hash * 17 + (autoSave ? 1 : 0);
		hash = hash * 31 + (readOnly ? 1 : 0);
		hash = hash * 35 + (reuseFragment ? 1 : 0);
		hash = hash * 13 + (errorHandler == null ? 0 : errorHandler.hashCode());

		return hash;
	}

	private static boolean isDefault(ISEAErrorHandler errHandler)
	{
		return errHandler.getClass() == SEADefaultErrorHandler.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISeaOptions#reuseFragment()
	 */
	@Override
	public boolean reuseFragment()
	{
		return reuseFragment;
	}

}
