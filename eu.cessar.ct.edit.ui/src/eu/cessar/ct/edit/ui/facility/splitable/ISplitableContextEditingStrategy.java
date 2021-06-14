/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 23.10.2012 10:59:27
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.req.Requirement;

/**
 * It decides the editing strategy for a piece of model (a {@link IModelFragmentEditor} edits one or several such
 * pieces), in the context of a splittable input.
 * 
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Nov 16 19:25:23 2012 %
 * 
 *         %version: 2 %
 */

@Requirement(
	reqID = "REQ_EDIT_PROP#SPLIT#1,REQ_EDIT_PROP#SPLIT#2, REQ_EDIT_PROP#SPLIT#3, REQ_EDIT_PROP#SPLIT#4, REQ_EDIT_PROP#SPLIT#5, REQ_EDIT_PROP#SPLIT#6, REQ_EDIT_PROP#SPLIT#7 ")
public interface ISplitableContextEditingStrategy
{
	/**
	 * 
	 * @param splitable
	 */
	public void setInput(Splitable splitable);

	/**
	 * @return whether the edited piece of model allows splitting
	 */
	public boolean isSplittingAllowed();

	/**
	 * @return all fragments of the splittable input
	 */
	public List<EObject> getAllFragments();

	/**
	 * @return the fragments of the splittable input in which the piece of model is set
	 */
	public List<EObject> getFragmentsWithValue();

	/**
	 * @return the resources in which are stored the fragments of the splittable input in which the piece of model is
	 *         set.
	 * @see #getFragmentsWithValue()
	 */
	public List<Resource> getResourcesWithValue();

	/**
	 * Called by the editor parts to decide on which fragments to apply the edited value.
	 * 
	 * @return the fragments of the splittable object to which the value of the piece of model should be applied. <br>
	 * 
	 */
	public List<EObject> getFragmentsInScope();

	/**
	 * @return whether editing is allowed in the splittable's active resource
	 */
	public boolean isEditingAllowed();

	/**
	 * @return whether the values of the edited piece of model are consistent among the fragments in which the piece of
	 *         model is set
	 */
	public boolean areValuesConsistent();

}
