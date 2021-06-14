/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 29, 2010 5:37:00 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.sdk.pm.PMRuntimeException;

/**
 * 
 */
public class ConstantAttributeAccessor implements INonsplitedSingleEAttributeAccessor
{

	private Object value;

	public ConstantAttributeAccessor(Object value)
	{
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#unset()
	 */
	public void unset()
	{
		// not supported, throw an internal error
		throw new PMRuntimeException("Cannot unset a constant attribute"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#isSet()
	 */
	public boolean isSet()
	{
		return value != null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#setValue(java.lang.Object)
	 */
	public void setValue(Object value)
	{
		// not supported, throw an internal error
		throw new PMRuntimeException("Cannot change a constant attribute"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#getValue()
	 */
	public Object getValue()
	{
		return value;
	}

}
