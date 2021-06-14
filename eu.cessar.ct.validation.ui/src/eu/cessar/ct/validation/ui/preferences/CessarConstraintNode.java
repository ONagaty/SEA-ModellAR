/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 10, 2014 2:56:11 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.preferences.EMFModelValidationPreferences;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IParameterizedConstraintDescriptor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.osgi.util.NLS;

/**
 * Node from the tree that represents a Constraint.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Fri Feb 13 18:33:11 2015 %
 *
 *         %version: 9 %
 */
public class CessarConstraintNode extends AbstractCessarTreeNode implements ICessarConstraintNode
{
	private static final String PARAM_CONSTRID = "constrId"; //$NON-NLS-1$

	private final IConstraintDescriptor constraint;

	/** categories that include this constraint */
	private final Set<ICessarCategoryNode> categories = new HashSet<>();

	/**
	 * Initializes with the constraint represented by this structure.
	 *
	 * @param tree
	 * @param parent
	 * @param constraint
	 */
	protected CessarConstraintNode(CheckboxTreeViewer tree, ICessarCategoryNode parent, IConstraintDescriptor constraint)
	{
		super(tree, parent, getConstraintIdSafe(constraint), getNameSafe(constraint), getDescriptionSafe(constraint));
		this.constraint = constraint;
		checked = constraint.isEnabled();
	}

	private static String getNameSafe(IConstraintDescriptor constraint)
	{
		if (constraint != null)
		{
			return constraint.getName();
		}

		return ""; //$NON-NLS-1$
	}

	private static String getDescriptionSafe(IConstraintDescriptor constraint)
	{
		if (constraint != null)
		{
			return constraint.getDescription();
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Gets the unique validation constraint ID from plugin.xml based on the constraint descriptor ID
	 *
	 * @param id
	 *        constraint ID
	 * @return <code>constraintId<code> for the specified constraint Id or empty String if no id exists
	 */
	private static String getConstraintIdSafe(IConstraintDescriptor constraint)
	{
		String constraintId = ""; //$NON-NLS-1$

		if (constraint instanceof IParameterizedConstraintDescriptor)
		{
			constraintId = ((IParameterizedConstraintDescriptor) constraint).getParameterValue(PARAM_CONSTRID);
		}

		if (constraintId == null)
		{
			constraintId = ""; //$NON-NLS-1$
		}

		return constraintId;
	}

	/*
	 * Implements method of interface ICessarTreeNode.
	 */
	@Override
	public String getTextLabel()
	{
		String textLabel;
		String id = getId();
		if (!id.isEmpty())
		{
			textLabel = "<" + id + ">" + getName();//$NON-NLS-1$//$NON-NLS-2$
		}
		else
		{
			textLabel = getName();
		}
		return textLabel;
	}

	/*
	 * Implements method of interface ICessarTreeNode.
	 */
	@Override
	public ICessarTreeNode[] getChildren()
	{
		return new ICessarTreeNode[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarConstraintNode#isMandatory()
	 */
	@Override
	public boolean isMandatory()
	{
		for (Category cat: constraint.getCategories())
		{
			if (cat.isMandatory())
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarTreeNode#formatDescription()
	 */

	public String getFormatedDescription()
	{
		String description = getDescription();
		if (description == null)
		{
			description = CessarValidationUIMessages.preferences_no_constraint_description;
		}

		String message;

		if (constraint.isError())
		{
			message = CessarValidationUIMessages.preferences_error_constraint_description;
		}
		else
		{
			message = CessarValidationUIMessages.preferences_constraint_description;
		}

		String text = NLS.bind(message,
			new Object[] {constraint.getEvaluationMode(), description, constraint.getSeverity()});

		if (categories.size() > 1)
		{
			text = text + CessarValidationUIMessages.preferences_mandatory_constraint;
		}

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#restoreDefaults()
	 */
	@Override
	public void restoreDefaults()
	{
		super.restoreDefaults();
		constraint.setEnabled(true);
		checked = constraint.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#applyToPreferences()
	 */
	@Override
	public void applyToPreferences()
	{
		EMFModelValidationPreferences.setConstraintDisabled(constraint.getId(), !isChecked());

		constraint.setEnabled(isChecked());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarTreeNode#revertFromPreferences()
	 */
	@Override
	public void revertFromPreferences()
	{
		setChecked(!EMFModelValidationPreferences.isConstraintDisabled(constraint.getId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarConstraintNode#isErrored()
	 */
	@Override
	public boolean isErrored()
	{
		return constraint.isError();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarConstraintNode#getCategories()
	 */
	@Override
	public Collection<Category> getCategories()
	{
		return constraint.getCategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.ICessarConstraintNode#addCategory(eu.cessar.ct.validation.ui.ICessarCategoryNode)
	 */
	@Override
	public void addCategory(ICessarCategoryNode category)
	{
		categories.add(category);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarConstraintNode#getEvaluationMode()
	 */
	@Override
	public String getEvaluationMode()
	{
		return constraint.getEvaluationMode().getLocalizedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.preferences.ICessarConstraintNode#getSeverity()
	 */
	@Override
	public String getSeverity()
	{
		return constraint.getSeverity().getLocalizedName();
	}
}
