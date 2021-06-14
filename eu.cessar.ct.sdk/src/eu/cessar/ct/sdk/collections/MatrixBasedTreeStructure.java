package eu.cessar.ct.sdk.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 * This class contains the logic for a tree content provider, when the tree structure is recorded into a square matrix
 * of bits. Here is how this class should be used:
 * 
 * <pre>
 * class ClientClass extends MatrixBasedTreeStructure implements ITreeContentProvider
 * </pre>
 * 
 * The square matrix has bits as values and the number of lines and columns equal with the size of "all EObjects list".
 * The lines of the matrix represents the Objects that are parents (sources) and the columns represents the objects that
 * are the children (destinations). <br>
 * Having the following tree structure:
 * 
 * <pre>
 *          Object: rootNode
 *              Object: subNode1
 *                  Object: childNode
 *              Object: subNode2
 * </pre>
 * 
 * it could be represented using the objects list and the bits matrix, as follow:
 * 
 * <pre>
 *      allObjectsList
 *          ------------------------------
 *          | index |       value        |
 *          |   0   | Object: subNode1  |
 *          |   1   | : childNode |
 *          |   2   | Object: rootNode  |
 *          |   3   | Object: subNode2  |
 *          ------------------------------
 *     linksMatrix
 *          ---------------------
 *          |   | 0 | 1 | 2 | 3 |    &lt;- indexes for children Objects (destinations)
 *          | 0 |   | x |   |   |
 *          | 1 |   |   |   |   |
 *          | 2 | x |   |   | x |
 *          | 3 |   |   |   |   |
 *          ---------------------
 *            &circ;
 *            indexes for Object elements that are parents (sources)
 * </pre>
 * 
 * @author Radu Bretean
 * @deprecated As of CESSAR CT 2014A_M1, replaced by the AbstractMatrixBasedTreeStrucure from eu.cessar.ct.platform.ui
 *             plugin
 */
// SUPPRESS CHECKSTYLE No warning for wrong naming, too late to change the name
@Deprecated
public abstract class MatrixBasedTreeStructure
{
	/**
	 * a flag that is set to <code>true</code> if candidate elements were found for current instance reference
	 */
	protected boolean hasCandidates;

	/**
	 * this is the list of all objects that will contribute to the creation of tree structure
	 */
	private final List<Object> allObjectsList;

	/**
	 * the square bits matrix where parent -> child links are marked
	 */
	private final BitMatrix linksMatrix;

	/**
	 * this member keep the text used to filter the elements within tree viewer
	 */
	private String filteringText;

	/**
	 * <code>true</code> if the filter text should be considered case sensitive, or <code>false</code> otherwise.
	 */
	private boolean isCaseSensitive;

	/**
	 * Default class constructor
	 */
	public MatrixBasedTreeStructure()
	{
		allObjectsList = new ArrayList<Object>();
		linksMatrix = new BitMatrix(0, 0);
	}

	// ////////////////////////////////////
	// ABSTRACT METHODS

	/**
	 * This method must be implemented in the concrete class because the links between matrix elements are specific to
	 * the implementation.
	 */
	public abstract void constructMatrix();

	/**
	 * This method verifies if a specified element is accepted by the filter or not.
	 * 
	 * @param object
	 *        the element to be verified if is filtered or not
	 * @return <code>true</code> if the given element is accepted by the filter, or <code>false</code> otherwise.
	 */
	public abstract boolean isAcceptedByFilter(final Object object);

	/**
	 * @return true if there are candidates, false otherwise
	 */
	public boolean hasCandidates()
	{
		return hasCandidates;
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
	}

	/**
	 * Set a filtering value
	 * 
	 * @param text
	 *        the filter value or <code>null</code> if the filtering is not desired.
	 */
	public void setFilteringText(final String text)
	{
		if ((text == null) || (text.length() <= 0))
		{
			filteringText = null;
		}
		else
		{
			filteringText = text;
		}
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

		String filter = StringUtils.escapeRegexForSearch(filteringText);
		Pattern pattern = Pattern.compile(filter);
		String text = objectText;
		if (!isCaseSensitive)
		{
			text = objectText.toLowerCase();
			pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
		}

		return pattern.matcher(text).lookingAt();
	}

	// ////////////////////////////////////
	// OBJECTS MANAGEMENET METHODS

	/**
	 * Gets the size of objects array and links matrix (is a square matrix).
	 * 
	 * @return The number of objects within the internal array.
	 */
	public int getSize()
	{
		return allObjectsList.size();
	}

	/**
	 * Test if a specified object is contained into the internal list.
	 * 
	 * @param object
	 *        the object that must be checked
	 * @return <code>true</code> if the object is contained or <code>false</code> otherwise.
	 */
	public boolean containsObject(final Object object)
	{
		if (object != null)
		{
			return allObjectsList.contains(object);
		}
		else
		{
			return false;
		}
	}

	/**
	 * Gets the object from specified index.
	 * 
	 * @param index
	 *        the index of the object to be retrieved
	 * @return The object from specified index, or <code>null</code> if the index is invalid.
	 */
	public Object getObject(final int index)
	{
		if ((index < 0) || (index >= allObjectsList.size()))
		{
			return null;
		}
		return allObjectsList.get(index);
	}

	/**
	 * Retrieve the index of a specified object, if it is contained within internal list.
	 * 
	 * @param object
	 *        the object whose index is requested
	 * @return The internal list index of specified object, or -1 if the object could not be found.
	 */
	public int getIndexOf(final Object object)
	{
		if (object != null)
		{
			return allObjectsList.indexOf(object);
		}
		else
		{
			return -1;
		}
	}

	/**
	 * Allow to add an object to the internal list. If the object is successfully added, the matrix size is automaticaly
	 * incremented.
	 * 
	 * @param object
	 *        the object to be added.
	 */
	public void addObject(final Object object)
	{
		if ((object != null) && !allObjectsList.contains(object))
		{
			allObjectsList.add(object);

			linksMatrix.setRowCount(allObjectsList.size());
			linksMatrix.setColCount(allObjectsList.size());
		}
	}

	/**
	 * Allow addition of a collection of objects to the internal list. The size of the links matrix is automatically
	 * increased.
	 * 
	 * @param collection
	 *        the collection of objects to be added
	 */
	public void addAllObjects(final Collection<?> collection)
	{
		if (collection != null)
		{
			for (final Object object: collection)
			{
				addObject(object);
			}
		}
	}

	// ////////////////////////////////////
	// MATRIX IMPLEMENTATION

	/**
	 * Return the number of bits that are set from the specified column
	 * 
	 * @param colIndex
	 *        the column to inspect
	 * @return the number of bits set to <code>true</code>
	 */
	public int getColSetBits(final int colIndex)
	{
		if ((colIndex < 0) || (colIndex >= linksMatrix.getColCount()))
		{
			return -1;
		}
		return linksMatrix.getColSetBits(colIndex);
	}

	/**
	 * This method can be used to check if there is a relation (link) between two objects specified by their indexes.
	 * 
	 * @param parentIndex
	 *        the index or the parent object
	 * @param childIndex
	 *        the index of the child object
	 * @return <code>true</code> if the link is marked into the matrix, or <code>false</code> otherwise.
	 */
	public boolean isSetMatrixLink(final int parentIndex, final int childIndex)
	{
		if ((parentIndex < 0) || (parentIndex >= linksMatrix.getRowCount()))
		{
			return false;
		}
		if ((childIndex < 0) || (childIndex >= linksMatrix.getColCount()))
		{
			return false;
		}
		return linksMatrix.get(childIndex, parentIndex);
	}

	/**
	 * With this method can be added or removed a link (relation) within the matrix
	 * 
	 * @param parent
	 *        the parent object or the source from where the link should start
	 * @param child
	 *        the child object or the destination where the link should end
	 * @param value
	 *        <code>true</code> if the link should be marked, or <code>false</code> if the link should be deleted.
	 */
	public void setLinkToMatrix(final Object parent, final Object child, final boolean value)
	{
		// int childIndex = allObjectsList.indexOf(child);
		final int childIndex = internalIndexOf(child);
		if (childIndex == -1)
		{
			return;
		}

		final int parentIndex = internalIndexOf(parent);
		if (parentIndex == -1)
		{
			return;
		}

		// check if is necessarily to increase the matrix size
		if (linksMatrix.getColCount() <= childIndex)
		{
			linksMatrix.setRowCount(childIndex + 1);
			linksMatrix.setColCount(childIndex + 1);
		}
		if (linksMatrix.getRowCount() <= parentIndex)
		{
			linksMatrix.setRowCount(parentIndex + 1);
			linksMatrix.setColCount(parentIndex + 1);
		}

		// mark the child -> parent link into the matrix
		linksMatrix.set(childIndex, parentIndex, value);
	}

	/**
	 * Gets the index of the first element whose index is marked on the specified column within the links matrix.
	 * 
	 * @param colIndex
	 *        the matrix column where the marked index is search for
	 * @return The first marked index found into the links matrix, or -1 if there are no markings on specified column.
	 */
	public int getFirstParentIndex(final int colIndex)
	{
		for (int i = 0; i < linksMatrix.getRowCount(); i++)
		{
			if (linksMatrix.get(colIndex, i))
			{
				return i;
			}
		}
		return -1;
	}

	// ////////////////////////////////////
	// ITreeContentProvider IMPLEMENTATION

	/**
	 * The method that returns the root elements shown into the tree viewer.
	 * 
	 * @param inputElement
	 *        the input element of the tree viewer.
	 * @return a non-null array
	 */
	@SuppressWarnings("unused")
	public Object[] getElements(final Object inputElement)
	{
		final List<Object> roots = new ArrayList<Object>();
		if ((linksMatrix != null) || (allObjectsList != null))
		{
			// the root elements of the tree viewer are all the elements that
			// have no bit set on the links matrix column
			for (int i = 0; i < linksMatrix.getColCount(); i++)
			{
				if (linksMatrix.getColSetBits(i) == 0)
				{
					final Object cObj = allObjectsList.get(i);

					if (filteringText == null)
					{
						roots.add(cObj);
					}
					else
					{
						if (isAcceptedByFilter(cObj))
						{
							roots.add(cObj);
						}
					}
				}
			}
			// iterate leafs and add all their parents to the result
		}
		return roots.toArray();
	}

	/**
	 * The method that returns an array with the children elements of a specified parent element.
	 * 
	 * @param parentElement
	 *        the parent element
	 * @return a non-null array
	 */
	public Object[] getChildren(final Object parentElement)
	{
		final Collection<Object> allChildren = allChildren(parentElement);
		if (filteringText == null)
		{
			return allChildren.toArray();
		}
		else
		{
			final List<Object> result = new ArrayList<Object>();
			for (final Object child: allChildren)
			{
				if (isAcceptedByFilter(child))
				{
					result.add(child);
				}
			}
			return result.toArray();
		}
	}

	/**
	 * Method called by the tree provider in order to know if there are children elements under current one.
	 * 
	 * @param element
	 *        the current element
	 * @return <code>true</code> if given element has children, or <code>false</code> otherwise
	 */
	public boolean hasChildren(final Object element)
	{
		final int index = allObjectsList.indexOf(element);
		if (index != -1)
		{
			return linksMatrix.getRowSetBits(index) > 0;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Method that returns the parent of a specified object within tree structure. If the specified object might have
	 * more than one parents, first parent found will be returned.
	 * 
	 * @param element
	 *        the current element
	 * @return The parent object if exists, or <code>null</code> otherwise
	 */
	public Object getParent(final Object element)
	{
		if ((element != null) && allObjectsList.contains(element))
		{
			final int parentIndex = getFirstParentIndex(getIndexOf(element));
			return getObject(parentIndex);
		}
		return null;
	}

	/**
	 * 
	 */
	public void dispose()
	{
		allObjectsList.clear();
		linksMatrix.clear();
	}

	// ////////////////////////////////////
	// INTERNAL USE METHODS

	/**
	 * @param parentElement
	 * @return
	 */
	private Collection<Object> allChildren(final Object parentElement)
	{
		final List<Object> result = new ArrayList<Object>();

		// if the parent element is not found into the objects list, it has no
		// children
		final int index = allObjectsList.indexOf(parentElement);
		if (index == -1)
		{
			return result;
		}

		// the children of the specified element are all the elements that
		// have a bit set into the links matrix on the column indicated by the
		// index of the parent element into the objects list
		for (int i = 0; i < linksMatrix.getColCount(); i++)
		{
			if (linksMatrix.get(i, index))
			{
				result.add(allObjectsList.get(i));
			}
		}
		return result;
	}

	private int internalIndexOf(final Object obj)
	{
		if (obj != null)
		{
			final Object[] objects = allObjectsList.toArray();

			for (int i = 0; i < objects.length; i++)
			{
				if (objects[i] == obj)
				{
					return i;
				}
			}

			return allObjectsList.indexOf(obj);
		}
		return -1;
	}
}
