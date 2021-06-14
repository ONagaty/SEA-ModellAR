package eu.cessar.ct.runtime.ui.internal.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.runtime.ui.internal.Messages;

/**
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Thu Dec 12 07:57:59 2013 %
 * 
 *         %version: 1 %
 */
public class SimpleClasspathContainerPage extends WizardPage implements IClasspathContainerPage, IExecutableExtension,
	IClasspathContainerPageExtension
{
	private Label text;
	private String message;

	/**
	 * 
	 */
	public SimpleClasspathContainerPage()
	{
		super("", "CP Description", null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension#initialize(org.eclipse.jdt.core.IJavaProject,
	 * org.eclipse.jdt.core.IClasspathEntry[])
	 */
	@Override
	public void initialize(IJavaProject project, IClasspathEntry[] currentEntries)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void createControl(Composite parent)
	{
		text = new Label(parent, SWT.WRAP);
		text.setText(message);
		setControl(parent);
	}

	@Override
	public boolean finish()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IClasspathEntry getSelection()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelection(IClasspathEntry containerEntry)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement
	 * , java.lang.String, java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
		throws CoreException
	{

		if (data instanceof String)
		{
			if ("model".equals(data)) //$NON-NLS-1$ 
			{
				message = Messages.modelClassPathContainerDescription;
				setTitle(Messages.modelClassPathContainerName);

			}
			else if ("project".equals(data)) //$NON-NLS-1$
			{
				message = Messages.projectClassPathContainerDescription;
				setTitle(Messages.projectClassPathContainerName);
			}

		}

	}
}
