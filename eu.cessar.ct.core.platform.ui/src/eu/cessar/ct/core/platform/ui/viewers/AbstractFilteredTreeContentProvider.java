/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.viewers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is an
 * {@link eu.cessar.ct.core.platform.ui.viewers.AbstractTreeContentProvider}
 * implementing recursive filtering.
 * <p>
 * Filtering is based on moving elements and children gathering in two
 * additional methods and applying a validity check at the level of a single
 * element. Containers are considered valid if they have at least one valid
 * descendant.
 * </p>
 * 
 * @author tiziana_brinzas
 * 
 * @Review uidl6458 - 19.04.2012
 * 
 */
public abstract class AbstractFilteredTreeContentProvider extends AbstractTreeContentProvider
{
	protected HashMap<Object, Boolean> validChildren = new HashMap<Object, Boolean>();

	/**
	 * Returns a unique identification object for the given element.
	 * <p>
	 * The identifier must be calculated in a way that ensures one element is
	 * given the same identifier on subsequent calls and no two different
	 * elements share the same identifier. Most implementations inside Autosar
	 * will probably use qualified names, or full paths, or hash-codes on such
	 * strings.
	 * </p>
	 * 
	 * @param element
	 *        Element for which the identifier is requested.
	 * @return Unique identifier for the given element.
	 */
	abstract protected Object getId(Object element);

	/**
	 * Checks whether the given element matches the filtering criteria.
	 * <p>
	 * <b>Note:</b> Returning true for a container will prevent children
	 * recursion - in this case only the container will be included in the
	 * provided content. In the case false is returned, the validation mechanism
	 * will include this container if and only if it has at least one valid
	 * descendant.
	 * </p>
	 * 
	 * @param element
	 *        Element for which the filter criteria is checked.
	 * @return True if leaf element is valid, false otherwise.
	 */
	abstract protected boolean isValidElement(Object element);

	/**
	 * Gather and return all top-level elements, regardless of whether they pass
	 * filtering criteria. All information required to decide if an element
	 * passes filter criteria must be available before this method returns,
	 * because the internal validity map for the whole contents is built as soon
	 * as potential top-level elements are indicated.
	 * 
	 * @param element
	 *        Input element for this content provider
	 * @return Array of top-level elements
	 * @see #getAllElements(Object)
	 */
	abstract protected Object[] getAllElements(Object element);

	/**
	 * Gather and return all children of the given element, regardless of
	 * whether they pass filtering criteria.
	 * 
	 * @param element
	 *        Element whose children are requested.
	 * @return Array of children
	 */
	abstract protected Object[] getAllChildren(Object element);

	/**
	 * Get all (non-filtered) top-level elements and check whether they pass the
	 * filtering criteria.
	 */

	public final Object[] getElements(Object inputElement)
	{
		ArrayList<Object> elements = new ArrayList<Object>();

		Object[] allElements = getAllElements(inputElement);
		for (Object element: allElements)
		{
			if (isValid(element))
			{
				elements.add(element);
			}
		}

		return elements.toArray();
	}

	/**
	 * Get all (non-filtered) children elements and return only the subset that
	 * fulfills the filtering criteria.
	 * <p>
	 * Note: This is based on the already built validity map.
	 * </p>
	 */

	public final Object[] getChildren(Object parentElement)
	{
		ArrayList<Object> children = new ArrayList<Object>();
		Object[] allChildren = getAllChildren(parentElement);
		for (Object child: allChildren)
		{
			Boolean valid = validChildren.get(getId(child));
			if (valid != null && true == valid)
			{
				children.add(child);
			}
		}
		return children.toArray();
	}

	/**
	 * Base implementation that returns true if the given element has children
	 * according with the already built validity map.
	 * <p>
	 * <b>Note:</b> When overriding this method, please call super
	 * implementation to account for the validation statuses already registered
	 * on the first pass. fulfills the filtering criteria.
	 * </p>
	 */

	public boolean hasChildren(Object element)
	{
		Boolean valid = validChildren.get(getId(element));
		return (valid != null && true == valid);
	}

	/**
	 * Checks is the given element is valid with respect to the filtering
	 * criteria.
	 * <p>
	 * This method browses the sub-tree rooted in element, checking the criteria
	 * on every node and filling the validity map along the way.
	 * </p>
	 * 
	 * @param element
	 *        Element to be validated
	 * @return Validity status
	 */
	protected final boolean isValid(Object element)
	{
		boolean isValid = isValidElement(element);
		if (isValid)
		{
			return registerValidationStatus(element, isValid);
		}

		// Recurse children
		isValid = false;
		Object[] allChildren = getAllChildren(element);
		if (null != allChildren && allChildren.length > 0)
		{
			for (Object child: allChildren)
			{
				isValid = isValid(child) || isValid;
			}
		}

		return registerValidationStatus(element, isValid);
	}

	/**
	 * Stores the validations status of the element in the internal map, using
	 * element's Id as key.
	 * 
	 * @param element
	 *        Element for which the validation status is registered.
	 * @param status
	 *        Validation status of the element
	 * @return Validation status (just a convenience for speed)
	 */
	protected final boolean registerValidationStatus(Object element, boolean status)
	{
		validChildren.put(getId(element), status);
		return status;
	}
}
