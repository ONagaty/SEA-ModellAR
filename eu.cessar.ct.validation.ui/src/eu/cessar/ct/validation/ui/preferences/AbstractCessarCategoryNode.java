/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 9, 2014 3:19:33 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

/**
 * Abstract class that implements operations on a category node.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Wed Jan 28 13:50:48 2015 %
 *
 *         %version: 6 %
 */
public abstract class AbstractCessarCategoryNode extends AbstractCessarTreeNode implements ICessarCategoryNode
{

	private final Category category;
	private IConstraintFilter filter;
	private ICessarTreeNode[] children;

	/**
	 * @param tree
	 * @param parent
	 * @param category
	 * @param filter
	 */
	protected AbstractCessarCategoryNode(CheckboxTreeViewer tree, ICessarCategoryNode parent, Category category,
		IConstraintFilter filter)
	{
		super(tree, parent, getIdSafe(category), getNameSafe(category), getDescriptionSafe(category));
		this.category = category;
		this.filter = filter;
	}

	private static String getIdSafe(Category category)
	{
		if (category != null)
		{
			return category.getId();
		}

		return ""; //$NON-NLS-1$
	}

	private static String getNameSafe(Category category)
	{
		if (category != null)
		{
			return category.getName();
		}

		return ""; //$NON-NLS-1$
	}

	private static String getDescriptionSafe(Category category)
	{
		if (category != null)
		{
			return category.getDescription();
		}

		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#getTextLabel()
	 */
	@Override
	public String getTextLabel()
	{
		return getName();
	}

	/**
	 * @return list
	 */
	protected abstract List<ICessarTreeNode> createChildren();

	/**
	 * @param cat
	 * @return true
	 */
	protected boolean isAllEmpty(Category cat)
	{

		Set<IConstraintDescriptor> constraints = getFilteredConstraints(cat, getFilter());
		boolean isEmpty = constraints.isEmpty();

		if (isEmpty)
		{
			Set<Category> categoryList = cat.getChildren();

			for (Category c: categoryList)
			{
				isEmpty = isAllEmpty(c);
				if (!isEmpty)
				{
					return isEmpty;
				}
			}
		}

		return isEmpty;
	}

	/**
	 * @param category
	 * @param filter
	 * @return filteredConstraints
	 */
	protected static Set<IConstraintDescriptor> getFilteredConstraints(Category category, IConstraintFilter filter)
	{
		Set<IConstraintDescriptor> filteredConstraints = new HashSet<>();

		for (IConstraintDescriptor descriptor: category.getConstraints())
		{
			if (filter.accept(descriptor, null))
			{
				filteredConstraints.add(descriptor);
			}
		}

		return filteredConstraints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarCategoryNode#getCategory()
	 */
	@Override
	public Category getCategory()
	{
		return category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#getChildren()
	 */
	@Override
	public ICessarTreeNode[] getChildren()
	{
		if (children == null)
		{
			List<ICessarTreeNode> list = createChildren();
			children = list.toArray(new ICessarTreeNode[list.size()]);
		}
		return children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#isMandatory()
	 */
	@Override
	public boolean isMandatory()
	{
		return category.isMandatory();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#isErrored()
	 */
	@Override
	public boolean isErrored()
	{
		return false;
	}

	/**
	 *
	 * @return description
	 */
	public String getFormatedDescription()
	{
		String description = getDescription();
		if (description == null)
		{
			description = CessarValidationUIMessages.preferences_no_category_description;
		}

		if (category.isMandatory())
		{
			description += CessarValidationUIMessages.preferences_mandatory_category;
		}

		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarCategoryNode#getFilter()
	 */
	@Override
	public IConstraintFilter getFilter()
	{
		return filter;
	}

}
