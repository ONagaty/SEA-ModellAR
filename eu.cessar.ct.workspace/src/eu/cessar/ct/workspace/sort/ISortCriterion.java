/**
 * 
 */
package eu.cessar.ct.workspace.sort;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public interface ISortCriterion
{
	/**
	 * @return
	 */
	public ISortTarget getSortTarget();

	/**
	 * @return
	 */
	public String getLabel();

	/**
	 * @return
	 */
	public Object getImage();

	/**
	 * @param parent
	 * @return
	 */
	public Object getObjectToCompare(EObject parent);

	/**
	 * @param direction
	 * @return
	 */
	public IDirectionalSortCriterion createDirectionalSortCriterion(ESortDirection direction);

	public String getTypeName();
}
