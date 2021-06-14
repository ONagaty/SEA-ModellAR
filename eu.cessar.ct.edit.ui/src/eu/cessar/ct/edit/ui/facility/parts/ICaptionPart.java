/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:28:06 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * Common type of all parts representing the label area of an {@link IModelFragmentEditor}
 * 
 * @author uidl6458
 * 
 */
public interface ICaptionPart extends IModelFragmentEditorPart
{

	/**
	 * @return the caption String that will be used by this part
	 */
	public String getCaption();

	/**
	 * Used to set a custom caption name for the editor
	 * 
	 * @param captionName
	 */
	public void setCaption(String captionName);

	/**
	 * 
	 * @return the documentation (if any) that will be used by this part
	 */
	public String getDocumentation();

}
