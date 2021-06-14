/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 11.04.2013 17:06:19
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.newobjfactories;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition;
import eu.cessar.ct.edit.ui.newobjfactories.IUIFeedback;

/**
 * Abstract basic implementation of an {@link INewEObjectComposition}
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Apr 16 19:14:57 2013 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractNewEObjectComposition implements INewEObjectComposition
{

	protected final EObject owner;
	protected final EStructuralFeature feature;
	protected final EObject children;
	protected IUIFeedback feedback;

	/**
	 * @param owner
	 * @param feature
	 * @param children
	 */
	public AbstractNewEObjectComposition(EObject owner, EStructuralFeature feature, EObject children)
	{
		this.owner = owner;
		this.feature = feature;
		this.children = children;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#getOwner()
	 */
	@Override
	public EObject getOwner()
	{
		return owner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#getChildrenFeature()
	 */
	@Override
	public EStructuralFeature getChildrenFeature()
	{
		return feature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#getChildren()
	 */
	@Override
	public EObject getChildren()
	{
		return children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#init(eu.cessar.ct.edit.ui.newobjfactories.IUIFeedback
	 * )
	 */
	@Override
	public void init(IUIFeedback feedbackListener)
	{
		feedback = feedbackListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#finish()
	 */
	@Override
	public List<Command> finish()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#cancel()
	 */
	@Override
	public void cancel()
	{
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#dispose()
	 */
	@Override
	public void dispose()
	{
		// nothing to do
	}

}
