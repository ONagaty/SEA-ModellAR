/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 13, 2013 10:47:00 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * {@link IGIdentifiableFilter} implementation that accepts elements whose qualified name match the filter's path
 * fragment.
 * 
 * @see {@link ISEAModel#pathFragment}
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Aug 21 10:05:15 2013 %
 * 
 *         %version: 1 %
 */
public class GIdentifiableFilterByPathFragment implements IGIdentifiableFilter
{
	private final String pathFragment;
	private final boolean ignoreCase;

	/**
	 * @param pathFragment
	 *        path fragment, as per {@linkplain ISEAModel#pathFragment}
	 * @param ignoreCase
	 *        case insensitive or not
	 */
	public GIdentifiableFilterByPathFragment(String pathFragment, boolean ignoreCase)
	{
		Assert.isNotNull(pathFragment);

		this.pathFragment = normalizePathFragment(pathFragment);
		this.ignoreCase = ignoreCase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaFilter#isPassingFilter(gautosar.ggenericstructure.ginfrastructure.
	 * GIdentifiable)
	 */
	public boolean isPassingFilter(GIdentifiable identifiable)
	{
		Assert.isNotNull(identifiable);

		int i = pathFragment.indexOf("/"); //$NON-NLS-1$
		if (i == -1)
		{
			String name = identifiable.gGetShortName();
			return StringUtils.equals(pathFragment, name, ignoreCase);
		}
		else
		{
			String qualifiedName = ModelUtils.getAbsoluteQualifiedName(identifiable);
			return StringUtils.endsWith(qualifiedName, pathFragment, ignoreCase);
		}
	}

	/**
	 * Return a string derived from the passed one, where all backslashes are replaced by slash
	 * 
	 * @param pathFragment
	 * @return normalized path fragment
	 */
	private static String normalizePathFragment(String pathFragment)
	{
		return pathFragment.replace('\\', '/');
	}

}
