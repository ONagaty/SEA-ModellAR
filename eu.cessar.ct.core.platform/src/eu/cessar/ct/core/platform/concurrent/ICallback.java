/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Oct 14, 2010 11:14:59 AM </copyright>
 */
package eu.cessar.ct.core.platform.concurrent;

/**
 *
 *
 * @param <V>
 *        the type of the work result
 */
public interface ICallback<V>
{

	/**
	 * Inform the client that the work has been performed
	 *
	 * @param result
	 *        the result of the work
	 */
	void workDone(V result);
}
