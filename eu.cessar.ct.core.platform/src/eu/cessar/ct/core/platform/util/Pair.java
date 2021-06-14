/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Sep 10, 2014 12:46:36 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.util.AbstractMap;

/**
 * Utility pair class.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Thu Sep 11 09:53:59 2014 %
 * 
 *         %version: 1 %
 * 
 * @param <K>
 *        key
 * @param <V>
 *        value
 */
public class Pair<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
{
	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 2437804542309848020L;

	/**
	 * Pair constructor.
	 * 
	 * @param k
	 *        the key
	 * @param v
	 *        the value
	 */
	public Pair(K k, V v)
	{
		super(k, v);
	}
}
