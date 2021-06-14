package eu.cessar.ct.runtime.ecuc.util;

import java.util.Collections;

/**
 * A entry that stores the short name, qualified name of a object and the list
 * of objects with which the object is considered splitted.
 */
public class SplitedEntry<E>
{
	private String qualifiedName;
	private String shortName;
	private ESplitableList<E> splitableList;

	public void setName(String shortName)
	{
		this.shortName = shortName;
	}

	public String getName()
	{
		return shortName;
	}

	public void setQualifiedName(String qualifiedName)
	{
		this.qualifiedName = qualifiedName;
	}

	public String getQualifiedName()
	{
		return qualifiedName;
	}

	public void setSplitableList(ESplitableList<E> splitableList)
	{
		this.splitableList = splitableList;
	}

	public ESplitableList<E> getSplitableList()
	{
		if (splitableList == null)
		{
			return new ESplitableList.Unmodifiable<E>(new Object[] {}, false,
				Collections.emptyList());
		}
		return splitableList;
	}
}
