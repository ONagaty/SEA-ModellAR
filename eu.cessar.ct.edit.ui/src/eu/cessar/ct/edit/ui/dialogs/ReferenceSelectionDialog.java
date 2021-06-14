/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Jul 17, 2009 4:42:31 PM </copyright>
 */
package eu.cessar.ct.edit.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.progress.WorkbenchJob;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.PostBuildPreferencesAccessor;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ui.viewers.MatrixBasedTreeFilter;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.sdk.IPostBuildContext;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;

/**
 * Dialog class that allows the user the possibility to select one EObject reference.
 * <p>
 *
 * @author uidl6870
 * @param <T>
 *        the type of the objects handled by this dialog. The type of the objects shall be the same for candidates,
 *        return object and MatrixBasedTreeFilter. Because of the implementation of the Content provider the T must be
 *        an EObject. See {@link TreeReferencesContentProvider}
 *
 */
// SUPPRESS CHECKSTYLE FAN OUT AND CLASS ABSCTRACTION
@Requirement(
	reqID = "REQ_EDIT_PROP#EDITOR_REFERENCE_SELECTION#1")
public class ReferenceSelectionDialog<T> extends TitleAreaDialog implements ISelectionChangedListener
{
	private static final String SHELL_TITLE = Messages.ReferenceSelectionDialog_title;

	private Button enablePBFilterBtn;

	private TreeViewer treeViewer;
	private IChooseReferenceHandler<T> handler;

	/** a list with valid selections */
	private List<Object> candidates;

	private T result;

	/** UI controls */
	private Text filterTxt;
	private Button caseSntBtn;
	private Button autoFilterBtn;

	private boolean isPBFilteringChecked = true;
	/**
	 * The tree viewer filter. It shows only the elements that passes the entered filter text
	 */
	private MatrixBasedTreeFilter<EObject> filter;
	/**
	 * Button used to trigger on/OFF the autofiltering capabilities
	 */
	private boolean autoFilter = true;

	/**
	 * The job used to update the tree.
	 */
	private Job refreshJob;

	/**
	 * @param parentShell
	 * @param handler
	 */
	public ReferenceSelectionDialog(Shell parentShell, IChooseReferenceHandler<T> handler)
	{
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.handler = handler;
		createRefreshJob();

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

		// now the dialog is fully created - including the OK button.
		// we can set the default value
		T value = handler.getReferencedValue();
		if (value != null)
		{
			this.result = value;
			treeViewer.setSelection(new StructuredSelection(value));
		}

		return control;
	}

	private void createFilterArea(Composite parent)
	{
		Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.ReferenceSelectionDialog_filter);

		filterTxt = new Text(parent, SWT.BORDER);
		filterTxt.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		caseSntBtn = new Button(parent, SWT.CHECK);
		caseSntBtn.setText(Messages.ReferenceSelectionDialog_case_sensitive);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		caseSntBtn.setLayoutData(gd);

		autoFilterBtn = new Button(parent, SWT.CHECK);
		autoFilterBtn.setText(Messages.ReferenceSelectionDialog_auto_filter);
		autoFilterBtn.setSelection(autoFilter);
		gd = new GridData();
		gd.horizontalSpan = 2;
		autoFilterBtn.setLayoutData(gd);
	}

	private void createTableArea(Composite parent)
	{
		Tree tree = new Tree(parent, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		treeViewer = new TreeViewer(tree);
		treeViewer.setUseHashlookup(true);// this is needed in order to speedup setSelection action

		TreeReferencesContentProvider provider = getTreeContentProvider(isPBFilteringChecked, getPBContext());
		treeViewer.setContentProvider(provider);
		ILabelProvider treeLabelProvider = handler.getTreeLabelProvider();
		treeViewer.setLabelProvider(treeLabelProvider);

		treeViewer.setInput(candidates);

		filter = new MatrixBasedTreeFilter<EObject>(provider, treeLabelProvider);

		treeViewer.addFilter(filter);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		updateDialogHeader();
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite composite = new Composite(area, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		createFilterArea(composite);
		createTableArea(composite);

		if (PostBuildPreferencesAccessor.isEnabledPostBuildContext(MetaModelUtils.getProject(handler.getInputObject()))
			&& getPBContext() != null)
		{
			createBottomPart(new Composite(area, SWT.None));
		}

		addListeners();
		return area;
	}

	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 550);
	}

	private void addListeners()
	{
		// close the dialog at double click in tree viewer when the selection is
		// valid
		treeViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				Button button = getButton(IDialogConstants.OK_ID);
				if ((button.isEnabled()))
				{
					okPressed();
				}
			}
		});

		// add table selection listener
		treeViewer.addSelectionChangedListener(this);

		treeViewer.getControl().addDisposeListener(new DisposeListener()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
			 */
			public void widgetDisposed(DisposeEvent e)
			{
				refreshJob.cancel();
			}
		});

		filterTxt.addModifyListener(new ModifyListener()
		{
			// set the current text as filter for the tree provider
			public void modifyText(ModifyEvent e)
			{
				// update viewer only if autofilter is ON
				if (autoFilter)
				{
					updateTreeViewer();
				}
			}
		});

		filterTxt.addKeyListener(new KeyListener()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				// if auto filter is on we have nothing to do
				if (autoFilter)
				{
					return;
				}

				int keyCode = e.keyCode;
				if (SWT.CR == keyCode)
				{
					// enter or tab was pressed so we have to update the filter
					updateTreeViewer();
				}

			}

			@Override
			public void keyPressed(KeyEvent e)
			{// nothing is required for key pressed

			}
		});

		caseSntBtn.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				filter.setCaseSensitiveFilter(caseSntBtn.getSelection());
				// re-do filtering ~ modifyText
				updateTreeViewer();
			}
		});

		autoFilterBtn.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				autoFilter = autoFilterBtn.getSelection();
			}
		});
	}

	/**
	 * Sets the current text (filterTxt.getText()) as a filter for the tree provider. <br>
	 * Triggers a new filtering with refresh. <br>
	 * Expands the tree to view all filtered leafs.
	 */
	private void updateTreeViewer()
	{
		// cancel currently running job first, to prevent unnecessary redraw
		refreshJob.cancel();
		refreshJob.schedule(200);
	}

	/**
	 *
	 */
	private void updateDialogHeader()
	{
		Shell shell = getShell();
		shell.setText(handler.getDialogTitle());
		setTitle(SHELL_TITLE);
		setMessage(Messages.ReferenceSelectionDialog_choose_object);
		setTitleImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.REFERENCE_IMAGE_ID));
	}

	@Override
	protected void okPressed()
	{
		handler.setReferencedValue(result);
		super.okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent
	 * )
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(SelectionChangedEvent event)
	{
		// check if a tree selection is send through the event
		if (!(event.getSelection() instanceof TreeSelection))
		{
			setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		// extract selected object from tree selection object
		TreeSelection treeSelection = (TreeSelection) event.getSelection();
		Object selectedObject = treeSelection.getFirstElement();
		if ((selectedObject == null) || !(selectedObject instanceof EObject))
		{
			setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		if (candidates.contains(selectedObject))
		{
			// enable OK button on the dialog
			getButton(IDialogConstants.OK_ID).setEnabled(true);
			setErrorMessage(null);
			result = (T) selectedObject;
		}
		else
		{
			setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		super.createButtonsForButtonBar(parent);

		// Create the hidden button used to unset the dialog ok button from being the default dialog button
		Button hiddenButton = new Button(parent, SWT.NONE);
		hiddenButton.setVisible(false); // the button is not visible
		hiddenButton.setEnabled(false);

		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		gd.heightHint = 0;// make sure the height is 0 so no free space is added at the end of filter area
		hiddenButton.setLayoutData(gd);

		getShell().setDefaultButton(hiddenButton);
	}

	private void createBottomPart(Composite composite)
	{
		composite.setLayout(new GridLayout(1, false));

		enablePBFilterBtn = new Button(composite, SWT.CHECK);
		enablePBFilterBtn.setText("Limit to post build context '" + getPBContext() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		enablePBFilterBtn.setSelection(isPBFilteringChecked);

		enablePBFilterBtn.addListener(SWT.Selection, new Listener()
		{

			public void handleEvent(Event e)
			{
				if (e.type == SWT.Selection)
				{
					isPBFilteringChecked = !isPBFilteringChecked;
					refreshCandidatesOnPBEnabled((isPBFilteringChecked && getPBContext() != null), getPBContext());
				}
			}
		});

	}

	private IPostBuildContext getPBContext()
	{
		IPostBuildContext context = null;
		EObject inputObject = handler.getInputObject();
		if (inputObject instanceof GContainer)
		{
			context = EcucMetaModelUtils.getPostBuildContext((GContainer) inputObject, true);
		}
		return context;
	}

	private void computeCandidateList(boolean isPBFiltering, IPostBuildContext pBContext)
	{
		this.candidates = handler.getCandidates();
		// uidu0944 - Post build filtering support
		if (isPBFiltering)
		{
			if (pBContext != null)
			{
				List<Object> filteredCandidates = new ArrayList<Object>();
				for (Object object: candidates)
				{
					IPostBuildContext context = EcucMetaModelUtils.getPostBuildContext((GContainer) object, true);
					if (context != null && context.getName().equals(pBContext.getName()))
					{
						filteredCandidates.add(object);
					}
				}
				this.candidates = filteredCandidates;
			}
		}
		int size = candidates.size();
		EObject object = null;
		if (!candidates.isEmpty())
		{
			if (candidates.get(0) != null)
			{
				object = (EObject) candidates.get(0);
			}
		}
		if (object != null)
		{
			IProject project = MetaModelUtils.getProject(object);
			boolean enablementOfFilterOnKeyPressed = CESSARPreferencesAccessor.getEnablementOfFilterOnKeyPressed(project);
			if (enablementOfFilterOnKeyPressed)
			{
				String filterOnKeyPressedLimitValue = CESSARPreferencesAccessor.getFilterOnKeyPressedLimitValue(project);
				if (size > Integer.valueOf(filterOnKeyPressedLimitValue))
				{
					autoFilter = false;
				}
				else
				{
					autoFilter = true;
				}

			}
		}
		else
		{
			autoFilter = true;
		}
		autoFilterBtn.setSelection(autoFilter);

	}

	private TreeReferencesContentProvider getTreeContentProvider(boolean isPBFiltering, IPostBuildContext pBContext)
	{
		computeCandidateList(
			isPBFiltering
				&& PostBuildPreferencesAccessor.isEnabledPostBuildContext(MetaModelUtils.getProject(handler.getInputObject())),
			pBContext);
		TreeReferencesContentProvider treeContentprovider = new TreeReferencesContentProvider();

		return treeContentprovider;
	}

	private void refreshCandidatesOnPBEnabled(boolean isPBFiltering, IPostBuildContext pBContext)
	{
		computeCandidateList(
			isPBFiltering
				&& PostBuildPreferencesAccessor.isEnabledPostBuildContext(MetaModelUtils.getProject(handler.getInputObject())),
			pBContext);

		treeViewer.setContentProvider(getTreeContentProvider(isPBFiltering, pBContext));
		filterTxt.setText(""); //$NON-NLS-1$
	}

	/**
	 * Create the refresh job and set it to be a system job
	 */
	private void createRefreshJob()
	{
		refreshJob = doCreateRefreshJob();
		refreshJob.setSystem(true);
	}

	/**
	 * Create the refresh job
	 *
	 * @return the refresh job
	 */
	protected WorkbenchJob doCreateRefreshJob()
	{
		return new WorkbenchJob("Refresh Filter") //$NON-NLS-1$
		{
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor)
			{
				if (treeViewer.getControl().isDisposed())
				{
					return Status.CANCEL_STATUS;
				}

				// disable redraw
				Tree tree = treeViewer.getTree();
				tree.setRedraw(false);
				try
				{
					String filterValue = filterTxt.getText();
					filter.setFilteringText(filterValue);
					treeViewer.refresh();

					EObject firstMatchingElement = filter.getFirstMatchingElement();
					if (firstMatchingElement != null)
					{
						StructuredSelection selection = new StructuredSelection(firstMatchingElement);
						treeViewer.setSelection(selection);
					}
					List<EObject> elements = new ArrayList<EObject>();
					List<EObject> matchingElements = filter.getMatchingElements();
					if ((matchingElements != null) && (!matchingElements.isEmpty()))
					{
						for (EObject eObject: matchingElements)
						{

							elements.add(eObject);

						}

					}

					for (EObject eObject: elements)
					{

						if (eObject != null)
						{
							treeViewer.expandToLevel(eObject, 0);
						}

					}
				}
				finally
				{
					// enable redraw
					tree.setRedraw(true);
				}
				return Status.OK_STATUS;
			}
		};
	}
}
