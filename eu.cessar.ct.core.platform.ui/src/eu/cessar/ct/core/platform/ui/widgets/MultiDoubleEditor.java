package eu.cessar.ct.core.platform.ui.widgets;

/**
 * Editor for multiple double values.
 * 
 */
public class MultiDoubleEditor extends AbstractMultiEditor<Double>
{

	/**
	 * @param handler
	 */
	public MultiDoubleEditor(MultiDatatypeValueHandler<Double> handler)
	{
		super(handler);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<Double> createSingleDatatypeEditor()
	{
		return new SingleDoubleEditor(isAcceptingNull());
	}

}
