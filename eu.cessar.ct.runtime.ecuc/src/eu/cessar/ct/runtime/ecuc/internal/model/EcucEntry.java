/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 13, 2009 6:41:28 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.model;

/**
 * This class represents an entry inside the EcucModel
 */
abstract class EcucEntry
{
	/**
	 * 
	 */
	public static EcucEntry ROOT = new EcucEntry()
	{
		@Override
		public String getName()
		{
			return "__ROOT__"; //$NON-NLS-1$
		}
	};

	/**
	 * Return the name of the entry
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * Return true if this entry have the same name as <code>otherName</code>
	 * 
	 * @param otherName
	 *        the name to check, could be null
	 * @return
	 */
	public boolean sameName(String otherName)
	{
		String name = getName();
		if (name == null && otherName == null)
		{
			return true;
		}
		else if (name != null && name.equals(otherName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
