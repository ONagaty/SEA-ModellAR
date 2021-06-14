/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Feb 4, 2014 3:40:46 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

/**
 * An {@link ESplitableList} whose content is produced from multiple sources, and which allows controlling its read-only
 * status and its active source. <br>
 * The 'active' source is the one that will be used when adding elements to the list using {@link #add(Object)} method.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Feb 4 15:57:56 2014 %
 * 
 *         %version: 1 %
 * @param <E>
 */
public interface MultiESplitableList<E> extends ESplitableList<E>
{
	/**
	 * Returns <code>true</code> if the list does not allow altering, <code>false</code> otherwise.
	 * 
	 * @return whether the list is read-only
	 */
	public boolean isReadOnly();

	/**
	 * Sets the read only status of the list.
	 * 
	 * @param readOnly
	 *        <code>false</code> if the list should allow altering, <code>true</code> otherwise.
	 */
	public void setReadOnly(boolean readOnly);

	/**
	 * Updates the active source of the list.
	 * <p>
	 * <b>NOTE:</b> If the <code>newActiveSource</code> is not among the sources from which the content of the list is
	 * produced, an unchecked exception will be thrown!
	 * <p>
	 * 
	 * @param newActiveSource
	 *        the new active source, could be <code>null</code>
	 */
	public void setActiveSource(Object newActiveSource);

	/**
	 * Returns the active source of the list.
	 * 
	 * 
	 * @return the active source, could be <code>null</code>
	 */
	public Object getActiveSource();
}
