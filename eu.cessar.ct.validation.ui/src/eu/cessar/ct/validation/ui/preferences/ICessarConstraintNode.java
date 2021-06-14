/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 5, 2014 4:21:29 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import java.util.Collection;

import org.eclipse.emf.validation.model.Category;

/**
 * Interface for a node in the tree that represents a Constraint.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Wed Jan 28 13:56:47 2015 %
 *
 *         %version: 2 %
 */
public interface ICessarConstraintNode extends ICessarTreeNode
{
	/**
	 * Gets the categories of which the constraint is a member.
	 *
	 * @return the categories of the constraint
	 */
	Collection<Category> getCategories();

	/**
	 * Adds a category tree node as one of the categories affected by the checked state.
	 *
	 * @param category
	 *        a category that includes the current category
	 */
	void addCategory(ICessarCategoryNode category);

	/**
	 * Gets the evaluation mode of the constraint.
	 *
	 * @return the localized evaluation mode of the constraint
	 */
	String getEvaluationMode();

	/**
	 * Gets the severity of the constraint.
	 *
	 * @return the localized severity of the constraint
	 */
	String getSeverity();
}
