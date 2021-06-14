/**
 * 
 */
package eu.cessar.ct.workspace.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author uidt2045
 * 
 */
public abstract class AbstractSortProviderImpl implements ISortProvider
{
	protected EObject parentObject;

	protected List<ISortTarget> sortTargets;

	protected TransactionalEditingDomain domain;

	/**
	 * @param parentObject
	 */
	public AbstractSortProviderImpl(TransactionalEditingDomain domain, EObject parentObject)
	{
		this.domain = domain;
		this.parentObject = parentObject;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortProvider#getEditingDomain()
	 */
	public AdapterFactoryEditingDomain getEditingDomain()
	{
		return (AdapterFactoryEditingDomain) domain;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortProvider#getParentObject()
	 */
	public EObject getParentObject()
	{
		return parentObject;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortProvider#getSortTargets()
	 */
	public List<ISortTarget> getSortTargets()
	{
		if (sortTargets == null)
		{
			sortTargets = doGetSortTargets();
		}
		return Collections.unmodifiableList(sortTargets);
	}

	/**
	 * 
	 */
	protected abstract List<ISortTarget> doGetSortTargets();

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortProvider#performSort(org.autosartools.general.core.sort.ISortTarget, java.util.List, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void performSort(final ISortTarget target,
		final List<IDirectionalSortCriterion> criteria, final boolean isGrouped,
		final IProgressMonitor monitor) throws OperationCanceledException, ExecutionException
	{
		monitor.beginTask("Sorting...", 1); //$NON-NLS-1$
		try
		{
			domain.getCommandStack().execute(new AbstractCommand()
			{

				/* (non-Javadoc)
				 * @see org.eclipse.emf.common.command.AbstractCommand#prepare()
				 */
				@Override
				protected boolean prepare()
				{
					return true;
				}

				public void execute()
				{
					doSort(target, criteria, isGrouped, monitor);
				}

				/* (non-Javadoc)
				 * @see org.eclipse.emf.common.command.AbstractCommand#canUndo()
				 */
				@Override
				public boolean canUndo()
				{
					return false;
				}

				public void redo()
				{
					// do nothing
				}

			});

		}
		finally
		{
			monitor.done();
		}
	}

	protected void doSort(final ISortTarget target, final List<IDirectionalSortCriterion> criteria,
		final boolean isGrouped, final IProgressMonitor monitor)
	{
		EList<EObject> allSortable = target.getAllSortableObjects(parentObject);
		EList<EObject> objectsToSort = target.getObjectsToSort(parentObject);
		final List<EObject> allSortableCopy = new ArrayList<EObject>(allSortable);
		final List<EObject> objectsToSortCopy = new ArrayList<EObject>(objectsToSort);

		if (isGrouped)
		{
			doGroupedSort(target, criteria, allSortable, allSortableCopy, objectsToSortCopy);
		}
		else
		{
			doUnGroupedSort(criteria, objectsToSort, allSortable);
		}

	}

	/**
	 * @param target
	 * @param criteria
	 * @param allSortable
	 * @param allSortableCopy
	 * @param objectsToSortCopy
	 */
	protected void doGroupedSort(final ISortTarget target,
		final List<IDirectionalSortCriterion> criteria, EList<EObject> allSortable,
		final List<EObject> allSortableCopy, final List<EObject> objectsToSortCopy)
	{
		ECollections.sort(allSortable, new Comparator<EObject>()
		{
			/**
			 * @param left
			 * @param right
			 * @return
			 */
			public int compare(EObject left, EObject right)
			{
				int cmp = target.getGroupName(left).compareToIgnoreCase(target.getGroupName(right));
				if (cmp != 0)
				{
					return cmp;
				}
				// from the same group
				boolean containsLeft = objectsToSortCopy.contains(left);
				boolean containsRight = objectsToSortCopy.contains(right);
				if (containsLeft && containsRight)
				{
					for (IDirectionalSortCriterion criterion: criteria)
					{
						int result = criterion.compare(left, right);
						if (result != 0)
						{
							return result;
						}
					}
					// objects still "equals"
					return left.toString().compareToIgnoreCase(right.toString());
				}
				else
				{
					if (containsLeft || containsRight)
					{
						// should never happen
						return 0;
					}
					else
					{
						return allSortableCopy.indexOf(left) - allSortableCopy.indexOf(right);
					}
				}
			}
		});
	}

	protected void doUnGroupedSort(final List<IDirectionalSortCriterion> criteria,
		EList<EObject> toSort, List<EObject> parentList)
	{

		// list of the initial positions where the elements are located.
		List<Integer> initialPos = new ArrayList<Integer>();

		for (EObject obj: toSort)
		{
			initialPos.add(parentList.indexOf(obj));
		}

		ECollections.sort(toSort, new Comparator<EObject>()
		{
			/**
			 * @param left
			 * @param right
			 * @return
			 */
			public int compare(EObject left, EObject right)
			{
				for (IDirectionalSortCriterion criterion: criteria)
				{
					int result = criterion.compare(left, right);
					if (result != 0)
					{
						return result;
					}
				}
				// if we reach this point the objects are still equal, compare
				// the hashcodes
				return left.hashCode() - right.hashCode();
			}
		});
		EObject[] arrayOfToSort = (EObject[]) toSort.toArray();

		// There is no need to move the first element, thats why i>0 and not
		// i>=0
		for (int i = initialPos.size() - 1; i > 0; i--)
		{
			ECollections.move(parentList, initialPos.get(i), arrayOfToSort[i]);
		}

	}
}
