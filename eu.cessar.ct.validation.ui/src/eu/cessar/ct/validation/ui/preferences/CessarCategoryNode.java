/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 10, 2014 3:03:08 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.CategoryManager;
import org.eclipse.emf.validation.service.ConstraintRegistry;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * Node from the tree that represents a Category.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Wed Jan 28 13:51:29 2015 %
 *
 *         %version: 5 %
 */
public class CessarCategoryNode extends AbstractCessarCategoryNode
{

	private static class RootNode extends AbstractCessarCategoryNode
	{

		/**
		 * Initializes the node with the tree that owns it
		 *
		 * @param tree
		 *        the tree that owns the node
		 * @param filter
		 *        a filter that specifies which constraints to use
		 */
		public RootNode(CheckboxTreeViewer tree, IConstraintFilter filter)
		{
			super(tree, null, null, filter);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.validation.ui.preferences.AbstractCessarCategoryNode#createChildren()
		 */
		@Override
		protected List<ICessarTreeNode> createChildren()
		{
			Collection<Category> topLevelCategories = CategoryManager.getInstance().getTopLevelCategories();
			List<ICessarTreeNode> rootChildren = new ArrayList<>(topLevelCategories.size());

			for (Category category: topLevelCategories)
			{
				if (!isAllEmpty(category))
				{
					rootChildren.add(new CessarCategoryNode(getTree(), category, this, getFilter()));
				}
			}

			return rootChildren;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.validation.ui.preferences.AbstractCessarTreeNode#isChecked()
		 */
		@Override
		public boolean isChecked()
		{
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.validation.ui.preferences.AbstractCessarTreeNode#isGrayed()
		 */
		@Override
		public boolean isGrayed()
		{
			return false;
		}
	}

	/**
	 * Initialize.
	 *
	 * @param tree
	 *        the tree viewer that this category is part of
	 * @param category
	 *        the category that is represented by this structure
	 * @param parent
	 *        the parent node in the tree
	 * @param filter
	 *        a filter the specifies which constraints this category contains
	 */
	public CessarCategoryNode(CheckboxTreeViewer tree, Category category, ICessarCategoryNode parent,
		IConstraintFilter filter)
	{
		super(tree, parent, category, filter);
	}

	/**
	 * Creates the root node of the tree. The root discovers the complete tree structure.
	 *
	 * @param tree
	 *        the tree viewer that
	 * @param filter
	 *        a filter the specifies which constraints this category contains
	 * @return rootNode
	 */
	public static ICessarTreeNode createRoot(CheckboxTreeViewer tree, final IConstraintFilter filter)
	{
		IConstraintFilter registeredConstraintsFilter = new IConstraintFilter()
		{

			@Override
			public boolean accept(IConstraintDescriptor constraint, EObject target)
			{
				if (ConstraintRegistry.getInstance().getDescriptor(constraint.getId()) == constraint)
				{
					return filter.accept(constraint, target);
				}
				return false;
			}
		};
		ICessarTreeNode rootNode = new RootNode(tree, registeredConstraintsFilter);

		rootNode.revertFromPreferences();

		return rootNode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.validation.ui.AbstractCessarCategoryNode#createChildren()
	 */
	@Override
	protected List<ICessarTreeNode> createChildren()
	{
		final Category category = getCategory();
		Collection<Category> categoryChildren = category.getChildren();

		List<ICessarTreeNode> children = new ArrayList<>(categoryChildren.size());

		Set<IConstraintDescriptor> constraints = getFilteredConstraints(category, getFilter());

		if (!constraints.isEmpty())
		{
			for (IConstraintDescriptor descriptor: constraints)
			{
				children.add(new CessarConstraintNode(getTree(), this, descriptor));
			}
		}

		for (Category child: categoryChildren)
		{
			if (!isAllEmpty(child))
			{
				children.add(new CessarCategoryNode(getTree(), child, this, getFilter()));
			}
		}

		return children;
	}
}
