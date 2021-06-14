/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 4, 2014 11:38:15 AM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * Common interface for the a tree node which can be a Constraint or a Category.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Wed Jan 28 13:56:59 2015 %
 *
 *         %version: 3 %
 */
public interface ICessarTreeNode
{
	/**
	 * Gets the ID of the category/constraint to be displayed.
	 *
	 * @return the ID of the category/constraint
	 */
	String getId();

	/**
	 * Gets the name of the category/constraint to be displayed.
	 *
	 * @return the name of the category/constraint
	 */
	String getName();

	/**
	 * Gets the description of the category/constraint to be displayed.
	 *
	 * @return the description of the category/constraint
	 */
	String getDescription();

	/**
	 * Gets the text label of the category/constraint to be displayed.
	 *
	 * @return the text label of the category/constraint
	 */
	String getTextLabel();

	/**
	 * Gets the subclasses with access to the tree that the category/constraint belongs to.
	 *
	 * @return tree
	 */
	CheckboxTreeViewer getTree();

	/**
	 * Gets the parent category of current category/constraint.
	 *
	 * @return the parent or <code>null</code> if root node.
	 */
	ICessarTreeNode getParent();

	/**
	 * Verifies if the node has children.
	 *
	 * @return <code>true</code> if the node has children <code>false</code> if it is a leaf
	 */
	boolean hasChildren();

	/**
	 * Gets the children of current category.
	 *
	 * @return the children or empty array if leaf
	 */
	ICessarTreeNode[] getChildren();

	/**
	 * Verifies if the node is checked.
	 *
	 * @return <code>true</code> if the node is checked <code>false</code> if it is unchecked
	 */
	boolean isChecked();

	/**
	 * Verifies if the node tree is grayed. Only a node of type CassarCategory can be grayed. A node is grayed if at
	 * least one of its children is checked and at least one is unchecked.
	 *
	 * @return <code>true</code> if the node is grayed, <code>false</code>, otherwise
	 */
	boolean isGrayed();

	/**
	 * Updates the category with the <code>child</code> state.
	 *
	 * @param child
	 */
	void updateCheckState(ICessarTreeNode child);

	/**
	 * Changes the state of the category/constraint based on the <code>event</code>.
	 *
	 * @param event
	 *        of check state
	 */
	void checkStateChanged(CheckStateChangedEvent event);

	/**
	 * Sets the new check state and propagates it to the tree. It can completely check or completely uncheck (according
	 * to the <code>state</code>) the entire subtree. The state propagates up the tree, and can change the gray state of
	 * the ancestors.
	 *
	 * @param state
	 *        the new state
	 */

	void setChecked(boolean state);

	/**
	 * Verifies if the tree node is mandatory for selection. If it is mandatory it can not be de-selected.
	 *
	 * @return <code>true</code> if it is mandatory, <code>false</code>, otherwise
	 */
	boolean isMandatory();

	/**
	 * Verifies if the constraint is "errored". If errored, it cannot be selected because is broken.
	 *
	 * @return <code>true</code> if constraint is errored, <code>false</code>, otherwise
	 */
	boolean isErrored();

	/**
	 * Formats the description of the tree node and returns it.
	 *
	 * @return text the formated text
	 */
	String getFormatedDescription();

	/**
	 * Restores the state to default.
	 */

	void restoreDefaults();

	/**
	 * Applies the state to the constraint preferences.
	 */
	void applyToPreferences();

	/**
	 * Reverts the state from the constraint preferences.
	 */

	void revertFromPreferences();
}
