/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jun 2, 2013 3:31:52 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import org.eclipse.emf.common.util.BasicEList.UnmodifiableEList;

import eu.cessar.ct.sdk.sea.util.ISEAList;

/**
 * An unmodifiable version of {@link ISEAList}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Aug 21 09:32:01 2013 %
 * 
 *         %version: 2 %
 * @param <E>
 */
public class UnmodifiableSEAEList<E> extends UnmodifiableEList<E> implements ISEAList<E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UnmodifiableSEAEList(int arg0, Object[] arg1)
	{
		super(arg0, arg1);
	}

}
