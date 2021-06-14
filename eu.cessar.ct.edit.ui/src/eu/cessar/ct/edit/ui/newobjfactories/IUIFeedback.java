/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 11.04.2013 10:26:39
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.newobjfactories;

/**
 * Interface used to provide feedback callbacks from the {@link INewEObjectComposition} up to the caller.
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Apr 16 19:14:59 2013 %
 * 
 *         %version: 1 %
 */
public interface IUIFeedback
{

	/**
	 * The current error message
	 * 
	 * @return the error message or null if there is none
	 */
	public String getErrorMessage();

	/**
	 * Set the current error message. If a non-null error message is given, the UI will not be finished. It could be
	 * canceled, of course.
	 * 
	 * @param message
	 *        an error message, could be null
	 */
	public void setErrorMessage(String message);

	/**
	 * The current warning message
	 * 
	 * @return the warning message or null if there is none
	 */
	public String getWarningMessage();

	/**
	 * Set a warning message or remove the existing one if the message is null. A message if present will not prevent
	 * the user to finish the UI
	 * 
	 * @param message
	 *        the message
	 */
	public void setWarningMessage(String message);

}
