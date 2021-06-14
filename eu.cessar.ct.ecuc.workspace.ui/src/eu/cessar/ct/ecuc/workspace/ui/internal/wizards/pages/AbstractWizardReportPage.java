package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

abstract public class AbstractWizardReportPage extends AbstractWizardPage
{
	protected AbstractWizardReportPage(String pageName)
	{
		super(pageName);
	}

	abstract protected void addControls(Composite parent);

	/**
	 * Create contents of the wizard
	 * 
	 * @param parent
	 */
	@Override
	public void doCreateControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(super.getGridLayout(2, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		setControl(container);

		addControls(container);
	}

	@Override
	public boolean isPageComplete()
	{
		return true;
	}

	protected void setTitleStyle(Control control)
	{
		control.setFont(new Font(null, new FontData("Times New Roman", 20, SWT.BOLD))); //$NON-NLS-1$
		control.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.horizontalSpan = 2;
		gridData.verticalIndent = 16;
		control.setLayoutData(gridData);
	}

	protected void setLabelStyle(Control control)
	{
		control.setFont(new Font(null, new FontData("Times New Roman", 10, SWT.NORMAL))); //$NON-NLS-1$
		control.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		GridData gridDataLabel = new GridData();
		gridDataLabel.horizontalAlignment = SWT.LEFT;
		control.setLayoutData(gridDataLabel);
	}

	protected void setTextStyle(Control control)
	{
		control.setFont(new Font(null, new FontData("Times New Roman", 10, SWT.BOLD))); //$NON-NLS-1$
		control.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		GridData gridDataLabel = new GridData();
		gridDataLabel.horizontalAlignment = SWT.FILL;
		gridDataLabel.grabExcessHorizontalSpace = true;
		control.setLayoutData(gridDataLabel);
	}
}
