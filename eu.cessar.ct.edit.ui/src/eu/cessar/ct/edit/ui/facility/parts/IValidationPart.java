/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:38:19 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.core.runtime.IStatus;

/**
 * Common interface of all the model fragment editor parts used to provide a validation feedback
 * 
 * @author uidl6458
 * 
 */
public interface IValidationPart extends IModelFragmentEditorPart
{

	/**
	 * Returns the status of the validation
	 * 
	 * @return status
	 */
	public IStatus getValidationStatus();

}
