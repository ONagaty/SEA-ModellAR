/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 25, 2010 1:42:49 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionValidator;
import org.eclipse.ui.dialogs.SelectionDialog;

import eu.cessar.ct.core.internal.platform.ui.Messages;
import eu.cessar.ct.core.internal.platform.ui.dialogs.ContainerSelectionGroup;

/**
 * A standard selection dialog which solicits a container resource from the
 * user. The <code>getResult</code> method returns the selected container
 * resource.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * <p>
 * Example:
 * 
 * <pre>
 * ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), initialSelection,
 * 	allowNewContainerName(), msg);
 * dialog.open();
 * Object[] result = dialog.getResult();
 * </pre>
 * 
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * 
 * @author uidt2045
 * 
 * @Review uidl6458 - 20.04.2012
 */
public class ContainerSelectionDialog extends SelectionDialog
{
	/**
	 * 
	 */
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String HELP_ID = "org.eclipse.ui.ide.container_selection_dialog_context"; //$NON-NLS-1$

	// the widget group;
	ContainerSelectionGroup group;

	// the root resource to populate the viewer with
	private IContainer initialSelection;

	// allow the user to type in a new container name
	private boolean allowNewContainerName = true;

	// the validation message
	Label statusMessage;

	// for validating the selection
	ISelectionValidator validator;

	// show closed projects by default
	private boolean showClosedProjects = true;

	/**
	 * Creates a resource container selection dialog rooted at the given
	 * resource. All selections are considered valid.
	 * 
	 * @param parentShell
	 *        the parent shell
	 * @param initialRoot
	 *        the initial selection in the tree
	 * @param allowNewContainerName
	 *        <code>true</code> to enable the user to type in a new container
	 *        name, and <code>false</code> to restrict the user to just
	 *        selecting from existing ones
	 * @param message
	 *        the message to be displayed at the top of this dialog, or
	 *        <code>null</code> to display a default message
	 */
	public ContainerSelectionDialog(Shell parentShell, IContainer initialRoot,
		boolean allowNewContainerName, String message)
	{
		super(parentShell);
		setTitle(Messages.ContainerSelectionDialog_title);
		this.initialSelection = initialRoot;
		this.allowNewContainerName = allowNewContainerName;
		if (message != null)
		{
			setMessage(message);
		}
		else
		{
			setMessage(Messages.ContainerSelectionDialog_message);
		}
		setShellStyle(getShellStyle() | SWT.SHEET);
	}

	/* (non-Javadoc)
	 * Method declared in Window.
	 */
	@Override
	protected void configureShell(Shell shell)
	{
		super.configureShell(shell);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, HELP_ID);
	}

	/* (non-Javadoc)
	 * Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		// create composite
		Composite area = (Composite) super.createDialogArea(parent);

		Listener listener = new Listener()
		{
			public void handleEvent(Event event)
			{
				if (statusMessage != null && validator != null)
				{
					String errorMsg = validator.isValid(group.getContainerFullPath());
					if (errorMsg == null || errorMsg.equals(EMPTY_STRING))
					{
						statusMessage.setText(EMPTY_STRING);
						getOkButton().setEnabled(true);
					}
					else
					{
						statusMessage.setText(errorMsg);
						getOkButton().setEnabled(false);
					}
				}
			}
		};

		// container selection group
		group = new ContainerSelectionGroup(initialSelection, area, listener,
			allowNewContainerName, getMessage(), showClosedProjects);
		if (initialSelection != null)
		{
			group.setSelectedContainer(initialSelection);
		}

		statusMessage = new Label(area, SWT.WRAP);
		statusMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		statusMessage.setText(" \n "); //$NON-NLS-1$
		statusMessage.setFont(parent.getFont());

		return dialogArea;
	}

	/**
	 * The <code>ContainerSelectionDialog</code> implementation of this
	 * <code>Dialog</code> method builds a list of the selected resource
	 * containers for later retrieval by the client and closes this dialog.
	 */
	@Override
	protected void okPressed()
	{

		List<IPath> chosenContainerPathList = new ArrayList<IPath>();
		IPath returnValue = group.getContainerFullPath();
		if (returnValue != null)
		{
			chosenContainerPathList.add(returnValue);
		}
		setResult(chosenContainerPathList);
		super.okPressed();
	}

	/**
	 * Sets the validator to use.
	 * 
	 * @param validator
	 *        A selection validator
	 */
	public void setValidator(ISelectionValidator validator)
	{
		this.validator = validator;
	}

	/**
	 * Set whether or not closed projects should be shown in the selection
	 * dialog.
	 * 
	 * @param show
	 *        Whether or not to show closed projects.
	 */
	public void showClosedProjects(boolean show)
	{
		this.showClosedProjects = show;
	}
}
