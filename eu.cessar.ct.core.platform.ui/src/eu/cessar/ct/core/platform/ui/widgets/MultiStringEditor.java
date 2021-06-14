package eu.cessar.ct.core.platform.ui.widgets;

/**
 * Editor for multiple string values.
 * 
 */
public class MultiStringEditor extends AbstractMultiEditor<String>
{

	/**
	 * @param handler
	 */
	public MultiStringEditor(MultiDatatypeValueHandler<String> handler)
	{
		super(handler);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<String> createSingleDatatypeEditor()
	{
		return new SingleStringEditor(isAcceptingNull());
	}

}
