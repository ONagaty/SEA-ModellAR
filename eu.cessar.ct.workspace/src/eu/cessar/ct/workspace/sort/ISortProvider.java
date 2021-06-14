/**
 * 
 */
package eu.cessar.ct.workspace.sort;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

/**
 * @author uidt2045
 * 
 */
public interface ISortProvider
{

	/**
	 * @return
	 */
	public AdapterFactoryEditingDomain getEditingDomain();

	/**
	 * @return
	 */
	public EObject getParentObject();

	/**
	 * @return
	 */
	public List<ISortTarget> getSortTargets();

	/**
	 * @param target
	 * @param criteria
	 * @param monitor
	 * @throws ExecutionException
	 * @throws OperationCanceledException
	 */
	public void performSort(ISortTarget target, List<IDirectionalSortCriterion> criteria,
		boolean isGrouped, IProgressMonitor monitor) throws OperationCanceledException,
		ExecutionException;
}
