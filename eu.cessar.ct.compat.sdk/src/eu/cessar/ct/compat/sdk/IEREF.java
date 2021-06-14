/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 31, 2011 9:54:27 AM </copyright>
 */
package eu.cessar.ct.compat.sdk;

/**
 * Interface for <code>EREF</code>.
 * 
 * @author uidl6458
 * @Review uidl7321 - Apr 3, 2012
 */
public interface IEREF
{
	/**
	 * Returns the dest of the reference.
	 * 
	 * @return the dest
	 */
	public String getDest();

	/**
	 * Returns the value of the reference.
	 * 
	 * @return the value
	 */
	public String getValue();

	/**
	 * Sets the dest of the reference with the given <code>dest</code>.
	 * 
	 * @param dest
	 *        the given destination
	 */
	public void setDest(String dest);

	/**
	 * Sets the value of the reference with the given <code>value</code>.
	 * 
	 * @param value
	 *        the given value
	 */
	public void setValue(String value);
}
