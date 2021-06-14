/**
 * 
 */
package eu.cessar.ct.ecuc.workspace.internal.sort;

import eu.cessar.ct.workspace.sort.ISortCriterion;
import eu.cessar.ct.workspace.sort.ISortTarget;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractEcuSortCriterion implements ISortCriterion
{

	private final ContainerDefSortTarget sortTarget;
	private String label;
	private Object image;

	public AbstractEcuSortCriterion(final ContainerDefSortTarget sortTarget)
	{
		this.sortTarget = sortTarget;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getImage()
	 */
	public Object getImage()
	{
		return image;
	}

	public void setImage(final Object image)
	{
		this.image = image;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getLabel()
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param value
	 */
	protected void setLabel(final String value)
	{
		label = value;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getSortTarget()
	 */
	public ISortTarget getSortTarget()
	{
		return sortTarget;
	}

}
