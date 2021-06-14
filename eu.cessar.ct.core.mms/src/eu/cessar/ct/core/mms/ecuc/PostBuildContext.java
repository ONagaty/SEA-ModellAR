/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 27.06.2012 14:43:54 </copyright>
 */
package eu.cessar.ct.core.mms.ecuc;

import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.sdk.IPostBuildContext;

/**
 * A trivial implementation of {@link IPostBuildContext}
 */
public class PostBuildContext implements IPostBuildContext
{

	private String name;
	private int id;
	private boolean inUse;

	/**
	 * Creates a new {@link PostBuildContext} with the specified characteristics
	 * 
	 * @param name
	 * @param id
	 * @param inUse
	 */
	public PostBuildContext(String name, int id, boolean inUse)
	{
		this.name = name;
		this.id = id;
		this.inUse = inUse;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.IPostBuildContext#getName()
	 */
	public String getName()
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.IPostBuildContext#getID()
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * @param inUse
	 */
	public void setInUse(boolean inUse)
	{
		this.inUse = inUse;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.IPostBuildContext#isInUse()
	 */
	public boolean isInUse()
	{
		return inUse;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[" + getID() + "] " + getName(); //$NON-NLS-1$//$NON-NLS-2$
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof IPostBuildContext)
		{
			IPostBuildContext pbContext = (IPostBuildContext) obj;
			return StringUtils.equals(getName(), pbContext.getName())
			/*&& getID() == pbContext.getID()*/;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return getName().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o)
	{
		if (this.id == ((PostBuildContext) o).id)
		{
			return 0;
		}
		else if (this.id < ((PostBuildContext) o).id)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
