package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common;

import java.util.Collections;
import java.util.List;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.filetypesViewer.FileTypesResourceViewer;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;

public class ResourceSelectionDialog extends Dialog implements ISelectionChangedListener
{
	protected IProject project;
	protected IStructuredSelection selection;
	protected IFile selectedFile = null;
	protected boolean isModuleConfiguration;

	/**
	 * Create the dialog
	 *
	 * @param parentShell
	 */
	public ResourceSelectionDialog(Shell parentShell, IProject project, IStructuredSelection selection,
		boolean isModuleConfiguration)
	{
		super(parentShell);
		this.project = project;
		this.selection = selection;
		this.isModuleConfiguration = isModuleConfiguration;
	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.ResourceSelectionDialog_Title);
	}

	/**
	 * Create contents of the button bar
	 *
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(400, 500);
	}

	/**
	 * Create contents of the dialog
	 *
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		int layoutsOffsets = 10;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = layoutsOffsets;
		gridLayout.verticalSpacing = layoutsOffsets;
		gridLayout.marginWidth = layoutsOffsets;
		gridLayout.marginTop = layoutsOffsets;
		gridLayout.marginRight = layoutsOffsets;
		gridLayout.marginLeft = layoutsOffsets;
		gridLayout.marginHeight = layoutsOffsets;
		gridLayout.marginBottom = layoutsOffsets;

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(gridLayout);

		Label label = new Label(container, SWT.NONE);
		if (isModuleConfiguration)
		{
			label.setText(Messages.ResourceSelectionDialog_SelectModuleConfFile);
		}
		else
		{
			label.setText(Messages.ResourceSelectionDialog_SelectModuleDefFile);
		}

		List<String> acceptedFileTypes = getAcceptedFileTypes();

		// get the content types for each of the given file types
		List<IContentType> contentTypes = PlatformUIUtils.getContentTypesForFilesTypes(acceptedFileTypes);

		final FileTypesResourceViewer fileTypeViewer = new FileTypesResourceViewer(false, contentTypes);

		fileTypeViewer.createContents(container, Collections.singletonList(project));
		fileTypeViewer.getViewer().setSelection(selection);
		fileTypeViewer.getViewer().expandAll();
		fileTypeViewer.getViewer().addSelectionChangedListener(this);

		final Button chkShowHidden = new Button(container, SWT.CHECK);
		chkShowHidden.setText(Messages.ResourceSelectionDialog_ShowHiddenFolders);
		chkShowHidden.setSelection(false);
		chkShowHidden.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				fileTypeViewer.getContentProvider().showHidden(chkShowHidden.getSelection());
				fileTypeViewer.getViewer().refresh();
			}
		});

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent
	 * )
	 */
	public void selectionChanged(SelectionChangedEvent event)
	{
		setSelectedFile(null);
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		if (null != selection && !selection.isEmpty())
		{
			Object selected = selection.getFirstElement();
			if (selected instanceof IFile)
			{
				setSelectedFile((IFile) selected);
			}
		}
		getButton(IDialogConstants.OK_ID).setEnabled(null != getSelectedFile());
	}

	protected List<String> getAcceptedFileTypes()
	{
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);
		return autosarRelease.getContentTypeIds();
	}

	public IFile getSelectedFile()
	{
		return selectedFile;
	}

	public void setSelectedFile(IFile selectedFile)
	{
		this.selectedFile = selectedFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	@Override
	public boolean close()
	{
		// TODO Auto-generated method stub
		return super.close();
	}

}
