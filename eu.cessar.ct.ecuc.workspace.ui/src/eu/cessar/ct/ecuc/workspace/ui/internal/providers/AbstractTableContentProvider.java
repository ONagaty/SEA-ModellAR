package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * Base class for providers implementing
 * {@link org.eclipse.jface.viewers.IStructuredContentProvider} and
 * {@link org.eclipse.jface.viewers.ITableLabelProvider}.
 * 
 * @author tiziana_brinzas
 * 
 */
public abstract class AbstractTableContentProvider implements IStructuredContentProvider,
	ITableLabelProvider
{
	abstract public Object[] getElements(Object inputElement);

	abstract public String getColumnText(Object element, int columnIndex);

	abstract public Image getColumnImage(Object element, int columnIndex);

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// Does nothing by default
	}

	public boolean isLabelProperty(Object element, String property)
	{
		return false;
	}

	public void dispose()
	{
		// Does nothing by default
	}

	public void addListener(ILabelProviderListener listener)
	{
		// Does nothing by default
	}

	public void removeListener(ILabelProviderListener listener)
	{
		// Does nothing by default
	}
}
