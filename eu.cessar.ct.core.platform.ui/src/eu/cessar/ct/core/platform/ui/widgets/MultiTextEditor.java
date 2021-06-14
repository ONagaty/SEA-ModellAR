package eu.cessar.ct.core.platform.ui.widgets;

/**
 * Editor for multiple multiline string values.
 * 
 */
public class MultiTextEditor extends AbstractMultiEditor<String>
{

	/**
	 * @param handler
	 */
	public MultiTextEditor(MultiDatatypeValueHandler<String> handler)
	{
		super(handler);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<String> createSingleDatatypeEditor()
	{
		return new SingleTextEditor(isAcceptingNull());
	}

}
