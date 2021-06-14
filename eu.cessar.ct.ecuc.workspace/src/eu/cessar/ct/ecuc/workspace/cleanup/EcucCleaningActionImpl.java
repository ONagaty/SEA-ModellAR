/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 11:25:27 AM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import org.eclipse.emf.ecore.EObject;

/**
 * @author aurel_avramescu
 * 
 *         Implementation of IEcucCleaningAction interface
 */
public class EcucCleaningActionImpl implements IEcucCleaningAction
{

	private String elementName;
	private ECleaningProblemsType type;
	private boolean selected;
	private String elementType;
	private ISyncAction action;

	/**
	 * Default constructor
	 */
	public EcucCleaningActionImpl()
	{
		selected = true;
	}

	/**
	 * Constructor
	 * 
	 * @param elementName
	 * @param type
	 * @param elementType
	 * @param action
	 */
	public EcucCleaningActionImpl(String elementName, ECleaningProblemsType type,
		String elementType, ISyncAction action)
	{
		this.elementName = elementName;
		this.type = type;
		this.selected = true;
		this.elementType = elementType;
		this.action = action;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#getElementName()
	 */
	public String getElementName()
	{
		return elementName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#getProblemType()
	 */
	public ECleaningProblemsType getProblemType()
	{
		return type;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#setElementName(java.lang.String)
	 */
	public void setElementName(String elementName)
	{
		this.elementName = elementName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#setProblemType(eu.cessar.ct.ecuc.workspace.cleanup.CleaningProblemsType)
	 */
	public void setProblemType(ECleaningProblemsType type)
	{
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#execute()
	 */
	public void execute()
	{
		action.execute();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#isSelected()
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#setSelected(boolean)
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#getElementType()
	 */
	public String getElementType()
	{
		return this.elementType;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#setElementType(java.lang.String)
	 */
	public void setElementType(String type)
	{
		this.elementType = type;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#getActionName()
	 */
	public String getActionName()
	{
		return this.action.getActionName();
	}

	@Override
	public boolean equals(Object other)
	{
		if (other != null && other instanceof EcucCleaningActionImpl)
		{
			EcucCleaningActionImpl temp = (EcucCleaningActionImpl) other;
			if (this.elementName != null && temp.getElementName() != null
				&& this.elementName.equalsIgnoreCase(temp.getElementName()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		if (elementName != null)
		{
			return elementName.hashCode();
		}
		return super.hashCode();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction#getActionOwner()
	 */
	public EObject getActionOwner()
	{
		return action.getOwner();
	}

}
