/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 29, 2010 5:39:33 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

/**
 * 
 */
public interface INonsplitedSingleEAttributeAccessor
{

	public void unset();

	public boolean isSet();

	public void setValue(Object value);

	public Object getValue();

}
