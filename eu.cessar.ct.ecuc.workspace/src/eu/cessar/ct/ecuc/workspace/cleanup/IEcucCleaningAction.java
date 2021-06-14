/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 11:14:26 AM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import org.eclipse.emf.ecore.EObject;

/**
 * @author aurel_avramescu
 * 
 *         IEcucCleaningAction is used for keep all the necessary information
 *         about synchronization or cleaning actions
 */
public interface IEcucCleaningAction
{

	/**
	 * Set the name of the element that will be processed
	 * 
	 * @param elementName
	 */
	void setElementName(String elementName);

	/**
	 * 
	 * @return the element name that will be processed
	 */
	String getElementName();

	/**
	 * Set the problem type
	 * 
	 * @param type
	 */
	void setProblemType(ECleaningProblemsType type);

	/**
	 * 
	 * @return the problem type
	 */
	ECleaningProblemsType getProblemType();

	/**
	 * Execute the action
	 */
	void execute();

	/**
	 * 
	 * @return true if the check box in the second wizard page is checked, false
	 *         otherwise
	 */
	boolean isSelected();

	/**
	 * 
	 * @param selected
	 */
	void setSelected(boolean selected);

	/**
	 * Set element type (Eg. Container, Parameter)
	 * 
	 * @param type
	 */
	void setElementType(String type);

	/**
	 * 
	 * @return the element type
	 */
	String getElementType();

	/**
	 * 
	 * @return the action name that can be Create or Delete
	 */
	String getActionName();

	EObject getActionOwner();

}
