/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:38:33 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * Common interface of all the parts representing an editing area inside a {@link IModelFragmentEditor}.
 * 
 * @author uidl6458
 * 
 */
public interface IEditorPart extends IModelFragmentEditorPart
{

	/**
	 * @return a textual representation of the editor's value, or an empty string if the value is not set.
	 */
	public String getText();

	/**
	 * 
	 * 
	 * @return the image corresponding to the editor, or null if the image doesn't exist or cannot be retrieved.
	 */
	public Image getImage();

}
