/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 5, 2014 4:20:36 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.service.IConstraintFilter;

/**
 * Interface for a node in the tree that represents a Category.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Thu Jan 15 13:22:56 2015 %
 *
 *         %version: 3 %
 */
public interface ICessarCategoryNode extends ICessarTreeNode
{
	/**
	 * Gets the category that I represent.
	 *
	 * @return category
	 */

	Category getCategory();

	/**
	 * Gets the constraint filter for this category.
	 *
	 * @return constraint filter
	 */
	IConstraintFilter getFilter();
}
