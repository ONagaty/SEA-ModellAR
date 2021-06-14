package eu.cessar.ct.core.platform.ui.widgets;

/**
 * Editor for multiple boolean values.
 * 
 */
public class MultiBooleanEditor extends AbstractMultiEditor<Boolean>
{

	/**
	 * @param handler
	 */
	public MultiBooleanEditor(MultiDatatypeValueHandler<Boolean> handler)
	{
		super(handler);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<Boolean> createSingleDatatypeEditor()
	{
		return new SingleBooleanEditor(isAcceptingNull());
	}

}
