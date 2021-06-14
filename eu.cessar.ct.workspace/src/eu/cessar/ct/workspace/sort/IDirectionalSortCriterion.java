/**
 * 
 */
package eu.cessar.ct.workspace.sort;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public interface IDirectionalSortCriterion
{

	/**
	 * @return
	 */
	public ESortDirection getSortDirection();

	/**
	 * @return
	 */
	public ISortCriterion getSortCriterion();

	/**
	 * @param left
	 * @param right
	 * @return
	 */
	public int compare(EObject left, EObject right);

}
