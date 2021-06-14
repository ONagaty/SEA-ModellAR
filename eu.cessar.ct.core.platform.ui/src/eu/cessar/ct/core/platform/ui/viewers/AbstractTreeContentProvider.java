package eu.cessar.ct.core.platform.ui.viewers;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Base class for providers implementing
 * {@link org.eclipse.jface.viewers.ITreeContentProvider}
 * 
 * @author tiziana_brinzas
 * 
 * @Review uidl6458 - 19.04.2012
 * 
 */
public abstract class AbstractTreeContentProvider implements ITreeContentProvider

{
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// Does nothing by default
	}

	public void dispose()
	{
		// Does nothing by default
	}

}
