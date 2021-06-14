/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Jun 17, 2016 3:20:03 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import eu.cessar.ct.core.platform.ui.ant.AntRunner;
import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.runtime.ecuc.ui.internal.Messages;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * This is Ant run Dialog class. User can select perticular Ant file and press OK to start execution.
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class AntExecutionDialog extends Dialog
{
	private IProject project;
	private final String ANT_EXTENSION = "ant"; //$NON-NLS-1$
	private final String ANT_EXTENSION1 = ".ant"; //$NON-NLS-1$
	private TableViewer viewerProject;
	private IFile selectedAnt;

	/**
	 * @param parentShell
	 * @param project
	 */
	public AntExecutionDialog(Shell parentShell, IProject project)
	{
		super(parentShell);
		this.project = project;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.ANT_EXECUTION);
		newShell.setImage(newShell.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		List<IFile> projectAnts = new ArrayList<>();
		if (project != null)
		{
			try
			{
				projectAnts = ResourceUtils.getProjectFiles(project, ANT_EXTENSION);
			}
			catch (CoreException e)
			{
				return parent;
			}
		}
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout(1, false));

		GridLayout gridLayout = new GridLayout(1, false);
		container.setLayout(gridLayout);

		Label origLabel = new Label(container, SWT.NONE);
		origLabel.setText(Messages.ANT_PROJECT + ":"); //$NON-NLS-1$
		FontData fontData = origLabel.getFont().getFontData()[0];
		Font bfont = new Font(parent.getDisplay(), fontData.getName(), fontData.getHeight(), SWT.BOLD);
		origLabel.setFont(bfont);

		GridData labelData = new GridData();
		labelData.verticalAlignment = SWT.TOP;
		origLabel.setLayoutData(labelData);

		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_table.heightHint = 150;
		gd_table.widthHint = 750;

		// CONFIGURE LOCAL ANT TABLE
		viewerProject = new TableViewer(container, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		Table ptable = viewerProject.getTable();

		createColumns(viewerProject);

		ptable.setLayoutData(gd_table);
		ptable.setHeaderVisible(true);
		ptable.setLinesVisible(true);

		viewerProject.setContentProvider(ArrayContentProvider.getInstance());

		viewerProject.setInput(projectAnts);

		viewerProject.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				selectedAnt = getObjectFromSelection(IFile.class, viewerProject.getSelection());

				getButton(IDialogConstants.OK_ID).setEnabled(true);
			}
		});

		ViewerComparator comparator = new ViewerComparator()
		{
			@Override
			public int compare(Viewer viewer, Object e1, Object e2)
			{
				return ((IFile) e1).getName().compareTo(((IFile) e2).getName());
			}
		};

		viewerProject.setComparator(comparator);
		viewerProject.getTable().getColumn(0).pack();

		Dialog.applyDialogFont(container);
		return container;
	}

	/**
	 * Utility method
	 *
	 * @param clz
	 * @param selection
	 * @return IFile
	 */
	protected IFile getObjectFromSelection(Class<IFile> clz, ISelection selection)
	{
		if (selection instanceof StructuredSelection)
		{
			StructuredSelection sSelection = (StructuredSelection) selection;
			if (sSelection.size() == 1)
			{
				Object firstElement = sSelection.getFirstElement();
				if (clz.isInstance(firstElement))
				{
					IFile result = (IFile) firstElement;
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * To create columns
	 *
	 * @param viewer
	 * @param hasDetails
	 */
	private void createColumns(final TableViewer viewer)
	{
		TableViewerColumn col = createTableViewerColumn(Messages.ANT_NAME, 200, viewer);
		col.setLabelProvider(new CellLabelProvider()
		{
			@Override
			public void update(ViewerCell cell)
			{
				IFile element = (IFile) cell.getElement();
				String title = element.getName();
				if (title.endsWith(ANT_EXTENSION1))
				{
					title = title.substring(0, title.lastIndexOf('.'));
				}

				cell.setText(title);
			}
		});

		col = createTableViewerColumn(Messages.ANT_PATH, 500, viewer);
		col.setLabelProvider(new CellLabelProvider()
		{
			@Override
			public void update(ViewerCell cell)
			{
				IFile element = (IFile) cell.getElement();
				String title = element.getFullPath().toString();
				cell.setText(title);
			}
		});
	}

	/**
	 * @param title
	 *        represents the title of the column
	 * @param bound
	 * @param colNumber
	 *        represents the index of the Column
	 * @return A TableViewer Column
	 */
	private TableViewerColumn createTableViewerColumn(String title, int bound, TableViewer viewer)
	{
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		// OK button
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		// Cancel button
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed()
	{
		executeAnt(new NullProgressMonitor(), selectedAnt);
		super.okPressed();
	}

	/**
	 * @param nullProgressMonitor
	 * @param selectedAnt2
	 */
	private boolean executeAnt(NullProgressMonitor monitor, IFile antFile)
	{
		monitor.subTask(Messages.EXECUTE_ANT_MESSAGE);
		if (antFile == null)
		{
			LoggerFactory.getLogger().warn(Messages.NO_ANT_FILES);
		}
		else
		{
			AntRunner.runAnt(antFile, "run", null); //$NON-NLS-1$

		}
		return true;
	}

}
