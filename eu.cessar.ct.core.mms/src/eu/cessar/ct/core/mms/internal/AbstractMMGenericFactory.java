/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 10.10.2012 12:01:35
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import eu.cessar.ct.core.mms.IGenericFactory;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

/**
 * Abstract superclass of the generic factory
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed Oct 10 12:27:51 2012 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractMMGenericFactory implements IGenericFactory
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IGenericFactory#createParameterValue(gautosar.gecucparameterdef.GConfigParameter,
	 * boolean)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public final GParameterValue createParameterValue(GConfigParameter paramDef, boolean useDefaultValue)
	{
		if (useDefaultValue)
		{
			return createParameterValueWithDefault(paramDef);
		}
		else
		{
			return createParameterValue(paramDef);
		}
	}
}
