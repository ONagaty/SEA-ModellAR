/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Nov 5, 2010 2:45:50 PM </copyright>
 */
package eu.cessar.ct.ant.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tools.ant.types.DataType;

/**
 * @author uidt2045
 * 
 */
public class URISet extends DataType
{
	private List<UriEntry> includeList = new ArrayList<URISet.UriEntry>();

	/**
	 * Inner UriEntry class
	 */
	public class UriEntry
	{
		private String uri;

		/**
		 * Set a Uri
		 * 
		 * @param uri
		 */
		public void setUri(String uri)
		{
			this.uri = uri;
		}

		/**
		 * @return the name attribute.
		 */
		public String getUri()
		{
			return uri;
		}

		/**
		 * @return a printable form of this object.
		 */
		@Override
		public String toString()
		{
			StringBuffer buf = new StringBuffer();
			if (uri == null)
			{
				buf.append("NoURI"); //$NON-NLS-1$
			}
			else
			{
				buf.append(uri);
			}
			return buf.toString();
		}
	}

	/**
	 * add a URI entry on the include list
	 * 
	 * @return a nested include element to be configured.
	 */
	public UriEntry createInclude()
	{
		if (isReference())
		{
			throw noChildrenAllowed();
		}
		return addURIToList(includeList);
	}

	private UriEntry addURIToList(List<UriEntry> list)
	{
		UriEntry result = new UriEntry();
		list.add(result);
		return result;
	}

	/**
	 * Appends <code>includes</code> to the current list of include uri's. uri's may be separated by a comma or a space.
	 * 
	 * @param includes
	 *        the string containing the include patterns
	 */
	public void setIncludes(String includes)
	{
		if (isReference())
		{
			throw tooManyAttributes();
		}
		if (includes != null && includes.length() > 0)
		{
			StringTokenizer tok = new StringTokenizer(includes, ", ", false); //$NON-NLS-1$
			while (tok.hasMoreTokens())
			{
				createInclude().setUri(tok.nextToken());
			}
		}
	}

	/**
	 * @return a list containing the UriEntries
	 */
	public List<UriEntry> getIncludeList()
	{
		return includeList;
	}

}
