package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Generic editor to be used when no specific editor is available.
 * 
 */
public class MultiGenericEditor extends AbstractMultiEditor<Object>
{

	/**
	 * @param handler
	 */
	public MultiGenericEditor(MultiDatatypeValueHandler<Object> handler)
	{
		super(handler);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractMultiEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createEditor(Composite parent)
	{
		Control control = super.createEditor(parent);

		super.setErrorOnTextEditor();

		return control;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	/**
	 * also sets a warning color and message
	 */
	public IDatatypeEditor<Object> createSingleDatatypeEditor()
	{
		return new SingleGenericEditor(isAcceptingNull());
	}

	/* (non-Javadoc)
	* @see eu.cessar.ct.core.platform.ui.widgets.AbstractMultiEditor#isReadOnly()
	*/
	@Override
	public boolean isReadOnly()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractMultiEditor#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean isReadOnly)
	{
		// do nothing, should not be editable
	}

}
