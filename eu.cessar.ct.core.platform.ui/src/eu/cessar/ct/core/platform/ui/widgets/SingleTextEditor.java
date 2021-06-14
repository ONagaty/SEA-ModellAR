package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Editor for a multiline string value.
 *
 * @Requirement(reqID = "27004")
 *
 */
public class SingleTextEditor extends SingleStringEditor
{

	/**
	 * Creates an instance of the editor.
	 *
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the default value empty string.
	 */
	public SingleTextEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#createTextEditor(org.eclipse.swt.widgets.Composite
	 * )
	 */
	@Override
	protected Text createTextEditor(Composite parent)
	{

		// add another parent to be able to set bottom margin
		Composite textParentComposite = getToolkit().createComposite(parent);
		GridLayout layoutTextParent = new GridLayout(1, false);
		layoutTextParent.marginBottom = 5;
		layoutTextParent.marginLeft = 0;
		layoutTextParent.marginHeight = 0;
		layoutTextParent.marginWidth = 0;

		textParentComposite.setLayout(layoutTextParent);
		GridData gridDataTextParent = new GridData(SWT.FILL, SWT.TOP, true, false);
		textParentComposite.setLayoutData(gridDataTextParent);

		final Text text = getToolkit().createText(textParentComposite, "", SWT.BORDER | SWT.MULTI | SWT.V_SCROLL); //$NON-NLS-1$
		GridData gridDataText = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridDataText.heightHint = 4 * text.getLineHeight();

		text.setLayoutData(gridDataText);

		return text;

	}

}
