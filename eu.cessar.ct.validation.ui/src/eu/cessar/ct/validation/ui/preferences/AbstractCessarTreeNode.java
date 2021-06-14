/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 5, 2014 4:22:04 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * Abstract class that partially implements operations from {@link ICessarTreeNodes}.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Wed Jan 28 13:51:14 2015 %
 *
 *         %version: 6 %
 */
public abstract class AbstractCessarTreeNode implements ICessarTreeNode
{
	/**
	 * Retrieves the checked state of a tree node.
	 */
	protected boolean checked = true;
	private boolean grayed;

	private CheckboxTreeViewer tree;
	private final ICessarCategoryNode parent;

	private String id;
	private String name;
	private String description;

	/**
	 * @param tree
	 * @param parent
	 * @param id
	 * @param name
	 * @param description
	 */
	protected AbstractCessarTreeNode(CheckboxTreeViewer tree, ICessarCategoryNode parent, String id, String name,
		String description)
	{
		this.tree = tree;
		this.parent = parent;
		this.id = id;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the checked
	 */
	@Override
	public boolean isChecked()
	{
		if (isErrored())
		{
			checked = false;
		}
		return checked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#isGrayed()
	 */
	@Override
	public boolean isGrayed()
	{
		return grayed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#hasChildren()
	 */
	@Override
	public boolean hasChildren()
	{
		return getChildren().length > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#getId()
	 */
	@Override
	public String getId()
	{
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#getParent()
	 */
	@Override
	public ICessarTreeNode getParent()
	{
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#getTree()
	 */
	@Override
	public CheckboxTreeViewer getTree()
	{
		return tree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#checkStateChanged(org.eclipse.jface.viewers.
	 * CheckStateChangedEvent)
	 */
	@Override
	public void checkStateChanged(CheckStateChangedEvent event)
	{
		boolean state = event.getChecked();
		if (isGrayed())
		{
			// state transition: grayed / checked / unchecked
			setChecked(true);
		}
		else
		{
			setChecked(state);
		}

		grayed = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#setChecked(boolean)
	 */
	@Override
	public void setChecked(boolean state)
	{
		if (isMandatory())
		{
			checked = true;
		}
		else if (isErrored())
		{
			checked = false;
		}
		else
		{
			checked = state;
		}

		updateTreeDown(state);
		updateTree();
	}

	/**
	 * Updates the tree up with the new state.
	 */
	private void updateTree()
	{
		if (tree.getChecked(this) != isChecked())
		{
			tree.setChecked(this, isChecked());
		}

		if (tree.getGrayed(this) != isGrayed())
		{
			tree.setGrayed(this, isGrayed());
		}

		updateParent();
	}

	/**
	 * Updates the parent according to the new state.
	 */
	private void updateParent()
	{
		if (getParent() != null)
		{
			getParent().updateCheckState(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#updateCheckState(eu.cessar.ct.validation.ui.preferences
	 * .ICessarTreeNode)
	 */
	@Override
	public void updateCheckState(ICessarTreeNode child)
	{
		if (child.isGrayed())
		{
			grayed = true;
			checked = true;
		}
		else
		{
			boolean newState = child.isChecked();

			ICessarTreeNode[] children = getChildren();

			for (ICessarTreeNode node: children)
			{
				if (node.isGrayed() || (node.isChecked() != newState))
				{
					grayed = true;
					checked = true;

					updateTree();

					return;
				}
			}

			grayed = false;
			checked = newState;
		}

		updateTree();
	}

	/**
	 * Updates the children according to the new state.
	 */
	private void updateTreeDown(boolean state)
	{
		ICessarTreeNode[] children = getChildren();

		for (ICessarTreeNode treeNode: children)
		{
			treeNode.setChecked(state);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#restoreDefaults()
	 */
	@Override
	public void restoreDefaults()
	{
		ICessarTreeNode[] children = getChildren();

		for (ICessarTreeNode treeNode: children)
		{
			treeNode.setChecked(true);
			treeNode.restoreDefaults();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#applyToPreferences()
	 */
	@Override
	public void applyToPreferences()
	{
		ICessarTreeNode[] children = getChildren();

		for (ICessarTreeNode treeNode: children)
		{
			treeNode.applyToPreferences();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#revertFromPreferences()
	 */
	@Override
	public void revertFromPreferences()
	{
		ICessarTreeNode[] children = getChildren();

		for (ICessarTreeNode treeNode: children)
		{
			treeNode.revertFromPreferences();
			updateCheckState(treeNode);
		}
	}
}
