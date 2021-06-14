package eu.cessar.ct.edit.ui.internal.facility.editors;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author uidl6870
 * 
 */
public class EnumLabelProvider extends LabelProvider
{
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element)
	{
		// TODO Auto-generated method stub
		return super.getImage(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element)
	{
		if (element instanceof Enumerator)
		{
			return ((Enumerator) element).getLiteral();
		}
		return ""; //$NON-NLS-1$
	}
}
