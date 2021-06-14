/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 05.10.2012 09:41:12
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.req.Requirement;

/**
 * Part of a {@link IModelFragmentEditor} indicating the resource(s) in which are stored the values of the edited model
 * fragment (used exclusively in the context of a splitable input)
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Nov 16 19:25:12 2012 %
 * 
 *         %version: 3 %
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#SPLIT#1")
public interface IResourcesPart extends IModelFragmentEditorPart, IDocumentationOwner
{
	/**
	 * 
	 * @return the number of resources in which are stored the values of the edited model fragment
	 */
	public int getResourcesNo();
}
