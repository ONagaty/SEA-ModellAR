/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 May 7, 2010 5:19:06 PM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import eu.cessar.ct.edit.ui.EditingPreferencesAccessor;
import eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Configuration dialog for setting ECUC/System instance references
 * 
 * @author uidl6870
 * 
 */
public class GenericIRefSelectionDialog extends TitleAreaDialog implements ISelectionChangedListener
{
	/**
	 * @author uidu3379
	 * 
	 */
	private final class ShowIncompleteConfigsSelectionListener extends SelectionAdapter
	{
		private final Button showIncompleteConfigs;

		private ShowIncompleteConfigsSelectionListener(Button showIncompleteConfigs)
		{
			this.showIncompleteConfigs = showIncompleteConfigs;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			boolean showIncompleteCfgs = showIncompleteConfigs.getSelection();
			// store the current setting in order to apply it as a workspace
			// preference on "OK"
			userPrefArray[0] = showIncompleteCfgs;
			handler.setPermitIncompleteConfigs(showIncompleteCfgs);
			treeViewer.refresh();

			if (!showIncompleteCfgs)
			{
				if (!handler.hasCandidatesForCompleteConfig())
				{
					setErrorMessage(Messages.NoCandidatesForCompleteConfig);
				}
			}
			else
			{
				setErrorMessage(null);
			}
		}
	}

	/**
	 * @author uidu3379
	 * 
	 */
	private final class TableListener implements Listener
	{
		/**
		 * 
		 */
		private final Listener labelListener;
		/**
		 * 
		 */
		private final Tree tree;
		private Shell tip;
		private Label label;

		/**
		 * @param labelListener
		 * @param tree
		 */
		private TableListener(Listener labelListener, Tree tree)
		{
			this.labelListener = labelListener;
			this.tree = tree;
		}

		public void handleEvent(Event event)
		{
			if (event.type == SWT.Dispose || event.type == SWT.KeyDown || event.type == SWT.MouseMove)
			{
				if (tip != null)
				{
					tip.dispose();
					tip = null;
					label = null;
				}
			}
			else if (event.type == SWT.MouseHover)
			{
				TreeItem item = tree.getItem(new Point(event.x, event.y));
				if (item != null)
				{
					if (tip != null && !tip.isDisposed())
					{
						tip.dispose();
					}
					tip = new Shell(tree.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
					tip.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
					FillLayout layout = new FillLayout();
					layout.marginWidth = 2;
					tip.setLayout(layout);
					label = new Label(tip, SWT.NONE);
					label.setForeground(tree.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
					label.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
					label.setData(KEY_TABLEITEM, item);

					Object data = item.getData();
					if (data instanceof GIdentifiable)
					{
						String qName = ModelUtils.getAbsoluteQualifiedName((GIdentifiable) data);
						label.setText(qName);
					}

					label.addListener(SWT.MouseExit, labelListener);
					label.addListener(SWT.MouseDown, labelListener);
					Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					Rectangle rect = item.getBounds(0);
					Point pt = tree.toDisplay(rect.x, rect.y);
					tip.setBounds(pt.x, pt.y + 20, size.x, size.y);
					tip.setVisible(true);
				}
			}

		}
	}

	/**
	 * @author uidu3379
	 * 
	 */
	private final class LabelListener implements Listener
	{
		/**
		 * 
		 */
		private final Tree tree;

		/**
		 * @param tree
		 */
		private LabelListener(Tree tree)
		{
			this.tree = tree;
		}

		public void handleEvent(Event event)
		{
			Label label = (Label) event.widget;
			Shell shell = label.getShell();
			if (event.type == SWT.MouseDown)
			{
				Event e = new Event();
				e.item = (TableItem) label.getData(KEY_TABLEITEM);
				// Assuming table is single select, set the selection as
				// if
				// the mouse down event went through to the table
				tree.setSelection(new TreeItem[] {(TreeItem) e.item});
				tree.notifyListeners(SWT.Selection, e);
				shell.dispose();
				tree.setFocus();
			}
			else
			{
				shell.dispose();
			}
		}
	}

	/**
	 * 
	 */
	private static final String KEY_TABLEITEM = "_TABLEITEM"; //$NON-NLS-1$

	private static final String SHELL_TITLE = Messages.ReferenceSelectionDialog_title;

	private TreeViewer treeViewer;
	private IChooseIRefHandler handler;

	private GIdentifiable newTarget;
	private List<GIdentifiable> newContext;

	private final boolean[] userPrefArray;
	private Text filterTxt;
	private boolean applyFilterOnKeyPress;

	/**
	 * 
	 * @param parentShell
	 * @param handler
	 */
	public GenericIRefSelectionDialog(Shell parentShell, IChooseIRefHandler handler)
	{
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.handler = handler;
		this.handler.setFilterString(null);
		userPrefArray = new boolean[] {handler.areIncompleteConfigsPermitted()};

		newContext = new ArrayList<GIdentifiable>();
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
		newContext.clear();

		// check if a tree selection is send through the event
		if (!(event.getSelection() instanceof TreeSelection))
		{
			setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		TreeSelection treeSelection = (TreeSelection) event.getSelection();
		Object selectedObj = treeSelection.getFirstElement();
		if (selectedObj == null || !(selectedObj instanceof GIdentifiable))
		{
			setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}

		if (handler.getCandidates().contains(selectedObj))
		{
			// enable OK button on the dialog
			getButton(IDialogConstants.OK_ID).setEnabled(true);
			setErrorMessage(null);
			newTarget = (GIdentifiable) selectedObj;

			TreePath[] paths = treeSelection.getPaths();
			if (paths != null)
			{
				TreePath treePath = paths[0];
				int segmentCount = treePath.getSegmentCount();
				for (int i = 0; i < segmentCount - 1; i++)
				{
					Object segment = treePath.getSegment(i);
					newContext.add((GIdentifiable) segment);
				}
			}

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
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);

		// treeViewer.expandAll();
		GIdentifiable target = handler.getTarget();

		if (target != null)
		{
			List<GIdentifiable> context = handler.getContext();

			Object[] segments = new Object[context.size() + 1];
			for (int i = 0; i < context.size(); i++)
			{
				segments[i] = context.get(i);
			}
			segments[segments.length - 1] = target;
			treeViewer.setSelection(new TreeSelection(new TreePath(segments)));
		}
		treeViewer.addSelectionChangedListener(this);

		// add tooltip
		createToolTip(treeViewer.getTree());

		return control;
	}

	/**
	 * 
	 */
	private void createToolTip(final Tree tree)
	{
		// Implement a "fake" tooltip
		final Listener labelListener = new LabelListener(tree);
		Listener tableListener = new TableListener(labelListener, tree);

		tree.addListener(SWT.Dispose, tableListener);
		tree.addListener(SWT.KeyDown, tableListener);
		tree.addListener(SWT.MouseMove, tableListener);
		tree.addListener(SWT.MouseHover, tableListener);
	}

	/**
	 * 
	 */
	private void updateDialogHeader()
	{
		getShell().setText(handler.getDialogTitle());
		setTitle(SHELL_TITLE);
		setMessage(Messages.ReferenceSelectionDialog_choose_object);
		setTitleImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.INSTANCE_REF_IMAGE_ID));
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

		Composite container1 = new Composite(area, SWT.NONE);
		container1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		container1.setLayout(new GridLayout(2, false));

		Label label = new Label(container1, SWT.NONE);
		label.setText(Messages.ReferenceSelectionDialog_filter);

		filterTxt = new Text(container1, SWT.BORDER);
		filterTxt.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		Composite container4 = new Composite(area, SWT.NONE);
		container4.setLayout(new GridLayout(1, false));
		createKeyPressedFilter(container4);

		Composite container2 = new Composite(area, SWT.NONE);
		container2.setLayout(new GridLayout(1, false));
		createButtonsForUpperArea(container2);

		Composite container3 = new Composite(area, SWT.NONE);
		container3.setLayout(new GridLayout(2, false));
		createSelectionButton(container3);

		// initialize the tree viewer control
		initializeTreeViewer(area);

		Label titleBarSeparator = new Label(area, SWT.HORIZONTAL | SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		addListeners();

		return area;
	}

	private void initializeTreeViewer(Composite area)
	{
		treeViewer = new TreeViewer(area, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		treeViewer.getTree().setLayoutData(gd);

		// create the tree viewer columns
		TreeColumn c1 = new TreeColumn(treeViewer.getTree(), SWT.NONE);
		treeViewer.getTree().setSortColumn(c1);
		c1.setText(Messages.Dialog_tree_column_prototype);
		c1.setWidth(500);

		TreeColumn c2 = new TreeColumn(treeViewer.getTree(), SWT.NONE);
		c2.setText(Messages.Dialog_tree_column_type);
		c2.setWidth(300);

		treeViewer.setSorter(new ViewerSorter()
		{
			@Override
			public int compare(Viewer viewer, Object obj1, Object obj2)
			{
				return ((GIdentifiable) obj1).gGetShortName().compareToIgnoreCase(
					((GIdentifiable) obj2).gGetShortName());
			}
		});

		treeViewer.setContentProvider(handler.getTreeContentProvider());
		treeViewer.setLabelProvider(handler.getTableLabelProvider());

		treeViewer.setInput(handler.getCanidatesMap());

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
	}

	private void addListeners()
	{
		filterTxt.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent event)
			{
				if (event.keyCode == SWT.CR && !applyFilterOnKeyPress)
				{
					if ("".equals(filterTxt.getText())) //$NON-NLS-1$
					{
						handler.setFilterString(null);
					}
					else
					{
						handler.setFilterString(filterTxt.getText());
					}
					treeViewer.refresh();
				}
			}
		});

		filterTxt.addFocusListener(new FocusListener()
		{

			public void focusLost(final org.eclipse.swt.events.FocusEvent e)
			{
				// getOKButton().setEnabled(true);
			}

			@SuppressWarnings("deprecation")
			public void focusGained(final org.eclipse.swt.events.FocusEvent e)
			{
				getOKButton().setEnabled(false);
			}
		});

		filterTxt.addModifyListener(new ModifyListener()
		{
			// set the current text as filter for the tree provider
			public void modifyText(ModifyEvent e)
			{
				if ("".equals(filterTxt.getText())) //$NON-NLS-1$
				{
					handler.setFilterString(null);
				}
				else
				{
					handler.setFilterString(filterTxt.getText());
				}
				if (applyFilterOnKeyPress)
				{
					treeViewer.refresh();
				}
			}
		});
	}

	private void createSelectionButton(Composite itemComposite)
	{
		final Button expandBtn = new Button(itemComposite, SWT.PUSH);
		final Button collapseBtn = new Button(itemComposite, SWT.PUSH);

		expandBtn.setText(" + "); //$NON-NLS-1$
		expandBtn.setToolTipText("Expand all"); //$NON-NLS-1$
		expandBtn.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				treeViewer.expandAll();
				expandBtn.setGrayed(true);
				collapseBtn.setGrayed(false);
			}
		});

		collapseBtn.setText(" - "); //$NON-NLS-1$
		collapseBtn.setToolTipText("Collapse all"); //$NON-NLS-1$
		collapseBtn.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				treeViewer.collapseAll();
				collapseBtn.setGrayed(true);
				expandBtn.setGrayed(false);
			}
		});
	}

	private void createButtonsForUpperArea(final Composite container)
	{
		final Button showIncompleteConfigs = new Button(container, SWT.CHECK);
		showIncompleteConfigs.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		showIncompleteConfigs.setText(Messages.Dialog_show_candidates_for_incomplete_configs_label);
		showIncompleteConfigs.setSelection(handler.areIncompleteConfigsPermitted());
		showIncompleteConfigs.setEnabled(handler.isSystemInstanceRef());
		showIncompleteConfigs.addSelectionListener(new ShowIncompleteConfigsSelectionListener(showIncompleteConfigs));
	}

	private void createKeyPressedFilter(final Composite container)
	{
		final Button applyFilterOnKeyPressed = new Button(container, SWT.CHECK);
		applyFilterOnKeyPressed.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		applyFilterOnKeyPressed.setText(Messages.ApplyFilterOnKeyPressed);
		applyFilterOnKeyPressed.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				applyFilterOnKeyPress = applyFilterOnKeyPressed.getSelection();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed()
	{
		if (newTarget != null)
		{
			handler.setReference(newTarget, newContext);
		}
		EditingPreferencesAccessor.setSysInstanceRefPrefInProject(handler.getProject(), userPrefArray[0]);

		super.okPressed();
	}

}
