/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 5, 2014 3:37:30 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import eu.cessar.ct.core.platform.ui.dialogs.CessarTreeRegexViewerFilter;

/**
 * Filter for the validation tree viewer.
 *
 * @author uidj9791
 *
 *         %created_by: uidu4748 %
 *
 *         %date_created: Fri Jul  3 18:31:36 2015 %
 *
 *         %version: RAUTOSAR~2 %
 */
public class ValidationCessarViewerFilter extends CessarTreeRegexViewerFilter
{
	/**
	 * @param leaf
	 * @return leaf name or empty string
	 */
	@Override
	protected String getLeafName(Object leaf)
	{
		if (leaf instanceof ICessarTreeNode)
		{
			return ((ICessarTreeNode) leaf).getTextLabel();
		}
		return ""; //$NON-NLS-1$
	}
}
