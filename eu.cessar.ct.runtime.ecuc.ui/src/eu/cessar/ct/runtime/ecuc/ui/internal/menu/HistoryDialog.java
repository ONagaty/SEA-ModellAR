/**
 *
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener;

/**
 * @author uidt2045
 *
 */
public class HistoryDialog extends TitleAreaDialog implements IPlugetParametersCompositeChangedListener
{

	/**
	 * Dialog title
	 */
	public final static String DIALOG_TITLE = "Edit pluget parameters"; //$NON-NLS-1$
	/**
	 * Shell title
	 */
	public final static String SHELL_TITLE = "Execute Pluget(s)"; //$NON-NLS-1$

	// composite for the pluget parameters widgets and their actions
	private PlugetParametersComposite parametersComposite;

	/**
	 * Calls constructor from super class. Initializes the parameters composite.
	 *
	 * @param parentShell
	 */
	public HistoryDialog(Shell parentShell, IProject project)
	{
		super(parentShell);
		setShellStyle(SWT.TITLE | SWT.RESIZE | SWT.MAX);
		parametersComposite = new PlugetParametersComposite(this);
		parametersComposite.setProject(project);
	}

	/**
	 *
	 * @param history
	 *        -the history to be set
	 */
	public void setHistory(String[] history)
	{
		if (history != null)
		{
			parametersComposite.setHistory(history);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		setTitle(DIALOG_TITLE);
		getShell().setText(SHELL_TITLE);
		return control;
	}

	@Override
	protected Control createDialogArea(Composite fparent)
	{
		Composite control = (Composite) super.createDialogArea(fparent);
		control.setLayout(new GridLayout(1, false));
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		control = parametersComposite.createParametersArea(control);
		return control;
	}

	/**
	 * must be implemented to return the parsed parameters
	 *
	 * @return parsed parameters
	 */
	public String getIntroducedValues()
	{
		String introducedText = parametersComposite.getIntroducedText();
		return introducedText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener
	 * #setRunPlugetControlsEnabled(boolean)
	 */
	@Override
	public void setRunPlugetControlsEnabled(boolean isValid)
	{
		if (getButton(IDialogConstants.OK_ID) != null)
		{
			getButton(IDialogConstants.OK_ID).setEnabled(isValid);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener
	 * #setErrorCode(java.lang.String)
	 */
	@Override
	public void setErrorCode(String error)
	{
		setErrorMessage(error);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener
	 * #handleControl()
	 */
	@Override
	public void handleControl()
	{
		boolean enabled = getButton(IDialogConstants.OK_ID).isEnabled();
		if (enabled)
		{
			okPressed();
		}

	}

}
