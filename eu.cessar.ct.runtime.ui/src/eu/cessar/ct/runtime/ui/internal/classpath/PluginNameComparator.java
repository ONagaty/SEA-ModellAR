package eu.cessar.ct.runtime.ui.internal.classpath;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

/**
 * This class is used as a support in order to offer the possibility of sorting the TabelViewer used in
 * CessarLibrariesClasspathContainerPage. It offers the possibility of sorting the TabelViewer by the plugin's
 * state,Checked/Unchecked, or by it's name.
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Fri Jan 17 10:01:27 2014 %
 * 
 *         %version: 1 %
 */
public class PluginNameComparator extends ViewerComparator
{
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;
	private int propertyIndex;

	/**
	 * 
	 */
	public PluginNameComparator()
	{
		propertyIndex = 0;
		direction = DESCENDING;
	}

	/**
	 * @return
	 */
	public int getDirection()
	{
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	/**
	 * @param column
	 */
	public void setColumn(int column)
	{
		if (column == propertyIndex)
		{
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		}
		else
		{
			// New column; do an ascending sort
			propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2)
	{
		PluginModelBaseWrapper p1 = (PluginModelBaseWrapper) e1;
		PluginModelBaseWrapper p2 = (PluginModelBaseWrapper) e2;
		int rc = 0;
		switch (propertyIndex)
		{
			case 0:
				if (p1.isChecked() == p2.isChecked())
				{
					rc = 0;
				}
				else
				{
					rc = (p1.isChecked() ? 1 : -1);
				}
				break;

			case 1:
				rc = p1.getPluginModelBase().getPluginBase().getId().compareTo(
					p2.getPluginModelBase().getPluginBase().getId());
				break;
			default:
				rc = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING)
		{
			rc = -rc;
		}
		return rc;
	}

}
