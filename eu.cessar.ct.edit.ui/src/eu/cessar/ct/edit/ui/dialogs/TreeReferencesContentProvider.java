package eu.cessar.ct.edit.ui.dialogs;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.platform.ui.viewers.AbstractMatrixBasedTreeStructure;

/**
 * Class that implements a tree content provider, based on a matrix tree structure
 */
public class TreeReferencesContentProvider extends AbstractMatrixBasedTreeStructure<EObject>
{
	/**
	 * Creates a tree content provider based on the matrix tree structure that receives a list of EOBject candidates.
	 * <p>
	 * The candidates represents the leafs of the tree
	 */
	public TreeReferencesContentProvider()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.internal.platform.util.MatrixBasedTreeStructure#constructMatrix()
	 */
	@Override
	public void constructMatrix()
	{
		Set<EObject> roots = new HashSet<>();

		// scan all received objects for getting their parent elements
		int initialSize = getSize();
		for (int i = 0; i < initialSize; i++)
		{
			EObject child = getObject(i);

			EObject parent = child.eContainer();
			while (parent != null)
			{
				addObject(parent);
				setLinkToMatrix(parent, child, true);
				child = parent;
				parent = child.eContainer();
			}

			// a child without a parent is a root
			roots.add(child);
		}

		// store internally the collected root elements
		storeRootElements(roots);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.viewers.AbstractMatrixBasedTreeStructure#shouldLookupRootElements()
	 */
	@Override
	protected boolean shouldLookupRootElements()
	{
		// no need to look up for the root elements, they will be collected during matrix construction
		return false;
	}

	@Override
	public EObject getParentGenerics(EObject child)
	{
		return child.eContainer();
	}

}
