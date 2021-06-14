/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95063<br/>
 * Jan 14, 2014 11:10:02 AM
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.viewers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 * Viewer filter
 *
 * @author uid95063
 *
 *         %created_by: Calin Cirstea
 *
 *         %date_created: creation date 20.01.2014
 *
 * @param <T>
 *        the type of the elements filtered by the current tree filter
 */
public class MatrixBasedTreeFilter<T> extends ViewerFilter
{

	private AbstractMatrixBasedTreeStructure<T> treeStructure;

	/**
	 * this member keep the text used to filter the elements within tree viewer
	 */
	private String filteringText;

	/**
	 * <code>true</code> if the filter text should be considered case sensitive, or <code>false</code> otherwise.
	 */
	private boolean isCaseSensitive;
	/**
	 * this field stores the reference to the label provider.
	 */
	private ILabelProvider labelProvider;

	/**
	 * Field that stores the first element that is matched by the filter
	 */
	private T firstMatchingElement;
	/**
	 * Cache the patter in order to speedup the filtering process. Pattern.compile() is very time consuming.
	 */
	private Pattern pattern;

	private List<T> matchingElements;

	/**
	 * Creates the base filter helper for tree build based on the Matrix structure. This class offers the base
	 * functionality to support easy filtering of elements.
	 *
	 * @param treeStructure
	 * @param labelProvider
	 */
	public MatrixBasedTreeFilter(AbstractMatrixBasedTreeStructure<T> treeStructure, ILabelProvider labelProvider)
	{
		this.treeStructure = treeStructure;
		this.labelProvider = labelProvider;
		matchingElements = new ArrayList<T>();
	}

	// ////////////////////////////////////
	// FILTERING METHODS

	/**
	 * Allow to check if the filtering is activated (if a value was set for the filter).
	 *
	 * @return <code>true</code> if a value is set for the filter, or <code>false</code> otherwise.
	 */
	public boolean isFilteringActivated()
	{
		return filteringText != null;
	}

	/**
	 * This method can be used to change the way in which the filter is matching the tree objects.
	 *
	 * @param isCS
	 *        if <code>true</code> the filter text is matched case sensitive, if <code>false</code> not
	 */
	public void setCaseSensitiveFilter(final boolean isCS)
	{
		isCaseSensitive = isCS;

		updatePattern();
	}

	/**
	 * Set a filtering value
	 *
	 * @param text
	 *        the filter value or <code>null</code> if the filtering is not desired.
	 */
	public void setFilteringText(final String text)
	{
		// reset the first matching element
		firstMatchingElement = null;
		if ((text == null) || (text.length() <= 0))
		{
			filteringText = null;
		}
		else
		{
			filteringText = text;
		}

		updatePattern();
	}

	/**
	 * This method is checking if a specified text match the filter.
	 *
	 * @param objectText
	 *        the string to be check if match the filter
	 * @return <code>true</code> if specified text match the filter or the filter is not active, or <code>false</code>
	 *         otherwise.
	 */
	public boolean matchFilter(final String objectText)
	{
		if (objectText == null)
		{
			return false;
		}

		if (filteringText == null)
		{
			return true;
		}
		String text = null;
		if (!isCaseSensitive)
		{
			text = objectText.toLowerCase();
		}
		else
		{
			text = objectText;
		}

		return pattern.matcher(text).lookingAt();
	}

	/**
	 * Update the filtering pattern and take into account the case sensitivity of the text
	 */

	private void updatePattern()
	{
		if (filteringText == null)
		{
			pattern = null;
			return;
		}
		String filter = StringUtils.escapeRegexForSearch(filteringText);
		if (!isCaseSensitive)
		{
			pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
		}
		else
		{
			pattern = Pattern.compile(filter);
		}

	}

	/**
	 * Searches for an ancestor that has passed filtering
	 *
	 * @param child
	 *        - current node
	 * @return <code> true</code> if the element has a valid ancestor taking into account the filter criteria
	 */
	public boolean hasValidAncestor(T child)
	{
		T parent = treeStructure.getParentGenerics(child);

		if (parent != null)
		{
			if (matchFilter(getText(parent)))
			{
				if (firstMatchingElement == null)
				{
					firstMatchingElement = parent;
				}
				return true;
			}
			else
			{
				return hasValidAncestor(parent);
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * This method verifies if a specified element is accepted by the filter or not.
	 *
	 * @param object
	 *        the element to be verified if is filtered or not
	 * @param checkAncestors
	 * @return <code>true</code> if the given element is accepted by the filter, or <code>false</code> otherwise.
	 */
	public boolean isAcceptedByFilter(T object)
	{
		boolean isValid = false;

		String objectText = getText(object);
		isValid = matchFilter(objectText);
		if (isValid)
		{
			if (firstMatchingElement == null)
			{
				firstMatchingElement = object;
			}
			matchingElements.add(object);
			return true;
		}

		boolean hasChildren = treeStructure.hasChildrenGenerics(object);
		if (hasChildren)
		{
			isValid = false;

			// specified object is not a target object, so check it's
			// children
			List<T> children = treeStructure.getChildrenGenerics(object);
			for (T child: children)
			{
				isValid |= isAcceptedByFilter(child);
			}
		}

		return isValid;

	}

	/**
	 * Filter out the elements. If one element ancestor is visible the child element will also be visible
	 *
	 * @param elements
	 *        the list of element to be filtered out
	 * @return the list of elements that are visible
	 */
	@SuppressWarnings("unchecked")
	// cast from T
	public Object[] filter(Object[] elements)
	{
		// there are no elements to verify
		if (elements.length == 0)
		{
			return elements;
		}

		if (!isFilteringActivated())
		{
			return elements;
		}
		List<Object> visibleElements = new ArrayList<Object>();

		// check first if the ancestors are visible. It is enough to test only the first element
		// there is at least one element in the list
		Object firstElement = elements[0];
		T castedElement = (T) firstElement;
		boolean isVisible = hasValidAncestor(castedElement);
		if (isVisible)
		{
			// if one parent is visible all elements shall be shown.
			return elements;
		}

		// check now the children
		for (Object element: elements)
		{
			castedElement = (T) element;
			boolean acceptedByFilter = isAcceptedByFilter(castedElement);
			if (acceptedByFilter)
			{
				visibleElements.add(element);
			}
		}
		return visibleElements.toArray();
	}

	/**
	 * Returns the display text for the given object. It uses the provided label provider
	 *
	 * @param object
	 *        the object to return the text
	 * @return the display text of the object
	 */
	private String getText(T object)
	{
		return labelProvider.getText(object);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ViewerFilter#filter(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	// due to cast from T
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element)
	{
		if (!isFilteringActivated())
		{
			return true;
		}
		T castedElement = (T) element;

		// check the ancestors
		boolean isVisible = hasValidAncestor(castedElement);
		if (isVisible)
		{
			// if one parent is visible all elements shall be shown.
			return true;
		}
		boolean acceptedByFilter = isAcceptedByFilter(castedElement);
		if (acceptedByFilter)
		{

			matchingElements.add(castedElement);

		}
		return acceptedByFilter;

	}

	/**
	 * @return the matchingElements
	 */
	public List<T> getMatchingElements()
	{
		return matchingElements;
	}

	/**
	 * @return the firstMatchingElement
	 */
	public T getFirstMatchingElement()
	{
		return firstMatchingElement;
	}
}
