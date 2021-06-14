/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 22.10.2012 16:08:29
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.results;

/**
 * Convenient class that can be used as a small worker class in various scenarios
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Oct 23 16:27:02 2012 %
 * 
 *         %version: 1 %
 * @param <Q>
 *        The type on which this result acts
 * @param <T>
 *        the type returned by result
 */
public interface Result<T, Q>
{

	/**
	 * Run the logic and return the expected result
	 * 
	 * @param param
	 *        The parameter that the logic shall use. It could be null. It's real content depend on the usage
	 * @return the result, depending on the usage it could be also null
	 */
	public T run(Q param);

}
