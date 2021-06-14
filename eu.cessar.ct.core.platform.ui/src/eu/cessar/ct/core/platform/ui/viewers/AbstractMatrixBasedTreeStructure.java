package eu.cessar.ct.core.platform.ui.viewers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import eu.cessar.ct.sdk.collections.BitMatrix;

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
 * @param <T>
 *        The type of the elements that are handled by the current matrix based tree structure
 */
public abstract class AbstractMatrixBasedTreeStructure<T> implements ITreeContentProvider
{
	/**
	 * a flag that is set to <code>true</code> if candidate elements were found for current instance reference
	 */
	protected boolean hasCandidates;

	/**
	 * this is the list of all objects that will contribute to the creation of tree structure
	 */
	private final List<T> allObjectsList;

	private Map<T, Integer> object2IndexMap = new HashMap<>();

	private int currentIndex = 0;

	/**
	 * the square bits matrix where parent -> child links are marked
	 */
	private final BitMatrix linksMatrix;

	/**
	 * the root elements of the tree structure
	 */
	private Set<T> rootElements;

	/**
	 * Default class constructor
	 */
	public AbstractMatrixBasedTreeStructure()
	{
		allObjectsList = new ArrayList<T>();
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
	 * @return true if there are candidates, false otherwise
	 */
	public boolean hasCandidates()
	{
		return hasCandidates;
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
	public boolean containsObject(final T object)
	{
		// if (object != null)
		// {
		// return allObjectsList.contains(object);
		// }
		// else
		// {
		// return false;
		// }

		if (object != null)
		{
			return object2IndexMap.containsKey(object);
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
	public T getObject(final int index)
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
	public int getIndexOf(final T object)
	{
		if (object != null)
		{
			Integer index = object2IndexMap.get(object);
			return index != null ? index : -1;
			// return allObjectsList.indexOf(object);
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
	public void addObject(final T object)
	{
		if ((object != null) && !containsObject(object))
		{
			allObjectsList.add(object);
			object2IndexMap.put(object, currentIndex++);

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
	public void addAllObjects(final Collection<T> collection)
	{
		if (collection != null)
		{
			for (final T object: collection)
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
	public void setLinkToMatrix(final T parent, final T child, final boolean value)
	{
		// final int childIndex = allObjectsList.indexOf(child);

		// if (childIndex == -1)
		// {
		// return;
		// }

		Integer childIndex = object2IndexMap.get(child);
		if (childIndex == null)
		{
			return;
		}

		// final int parentIndex = allObjectsList.indexOf(parent);
		// if (parentIndex == -1)
		// {
		// return;
		// }

		Integer parentIndex = object2IndexMap.get(parent);
		if (parentIndex == null)
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
	public List<T> getRootElements(final Object inputElement)
	{
		if (shouldLookupRootElements() || rootElements == null)
		{
			// seraching the matrix for root elements is not efficient and should be,
			// therefore, avoided by collecting the root elements during the matrix construction and
			// stored internally using storeRootElements(Set<T>)

			return lookupRootElements();
		}
		else
		{
			// the root elements can be identified during the matrix construction
			return new ArrayList<>(rootElements);
		}

	}

	/**
	 * @param rootElements
	 */
	protected void storeRootElements(Set<T> rootElements)
	{
		this.rootElements = rootElements;
	}

	/**
	 * @return whether it is necessary to look up for the root elements
	 */
	protected boolean shouldLookupRootElements()
	{
		// by default, return true
		return true;
	}

	/**
	 * Returns a list with all the root elements of the tree structure. <br>
	 *
	 * NOTE: the method is time-consuming (it inspects the matrix) and should be avoided
	 *
	 * @return a list with all the root elements from the matrix
	 */
	private List<T> lookupRootElements()
	{
		List<T> rootElements = new ArrayList<T>();
		if ((linksMatrix != null) || (allObjectsList != null))
		{
			// the root elements are all the elements that
			// have no bit set on the links matrix column
			for (int i = 0; i < linksMatrix.getColCount(); i++)
			{
				if (linksMatrix.getColSetBits(i) == 0)
				{
					final T cObj = allObjectsList.get(i);

					rootElements.add(cObj);
				}
			}
		}

		return rootElements;
	}

	/**
	 * The method that returns a list with the children elements of a specified parent element.
	 *
	 * @param parentElement
	 *        the parent element
	 * @return a non-null array
	 */
	public List<T> getChildrenGenerics(final T parentElement)
	{
		final List<T> result = new ArrayList<T>();

		// if the parent element is not found into the objects list, it has no
		// children
		// final int index = allObjectsList.indexOf(parentElement);
		// if (index == -1)
		// {
		// return result;
		// }

		Integer index = object2IndexMap.get(parentElement);
		if (index == null)
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

	/**
	 * Method called by the tree provider in order to know if there are children elements under current one.
	 *
	 * @param element
	 *        the current element
	 * @return <code>true</code> if given element has children, or <code>false</code> otherwise
	 */
	public boolean hasChildrenGenerics(final T element)
	{
		// final int index = allObjectsList.indexOf(element);
		// if (index != -1)
		// {
		// return linksMatrix.getRowSetBits(index) > 0;
		// }
		// else
		// {
		// return false;
		// }

		Integer index = object2IndexMap.get(element);
		if (index != null)
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
	public T getParentGenerics(final T element)
	{
		// if ((element != null) && allObjectsList.contains(element))
		// {
		// final int parentIndex = getFirstParentIndex(getIndexOf(element));
		// return getObject(parentIndex);
		// }
		// return null;

		if ((element != null) && containsObject(element))
		{
			final int parentIndex = getFirstParentIndex(getIndexOf(element));
			return getObject(parentIndex);
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement)
	{
		List<T> rootElements = getRootElements(inputElement);
		return rootElements.toArray();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement)
	{
		@SuppressWarnings("unchecked")
		List<T> children = getChildrenGenerics((T) parentElement);
		return children.toArray();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getParent(Object element)
	{

		return getParentGenerics((T) element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasChildren(Object element)
	{

		return hasChildrenGenerics((T) element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// the new input is the list of EObject
		@SuppressWarnings("unchecked")
		List<T> input = (List<T>) newInput;
		addAllObjects(input);

		// !!!!!! No need to reconstruct the matrix for a null newInput
		if (newInput != null)
		{
			constructMatrix();
		}
	}

	/**
	 *
	 */
	public void dispose()
	{
		allObjectsList.clear();
		linksMatrix.clear();

		object2IndexMap.clear();
		currentIndex = 0;
	}

	// ////////////////////////////////////
	// INTERNAL USE METHODS

}
