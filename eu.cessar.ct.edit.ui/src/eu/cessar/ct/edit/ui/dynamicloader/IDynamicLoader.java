/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4449<br/>
 * Jul 21, 2015 10:27:39 AM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.dynamicloader;

import java.util.List;

/**
 * TODO: Please comment this class
 *
 * @author uidg4449
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 * @param <T>
 * @param <P>
 */
public interface IDynamicLoader<T, P>
{
	/**
	 * @param input
	 * @return an array of type
	 *         <P>
	 */
	public List<P> load(T input);
}
