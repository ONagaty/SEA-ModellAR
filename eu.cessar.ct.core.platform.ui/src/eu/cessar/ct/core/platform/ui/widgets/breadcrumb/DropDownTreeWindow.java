package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

import java.lang.reflect.InvocationTargetException;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.sphinx.emf.metamodel.MetaModelDescriptorRegistry;
import org.eclipse.sphinx.emf.model.IModelDescriptor;
import org.eclipse.sphinx.emf.model.ModelDescriptorRegistry;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.workspace.loading.ModelLoadManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.platform.ui.filetypesViewer.FileTypesResourceViewer;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;

@SuppressWarnings("restriction")
public class DropDownTreeWindow extends DropDownWindow
{

	public DropDownTreeWindow(BreadcrumbItemExpand itemExpand, Shell shell)
	{
		super(itemExpand, shell);
	}

	private Shell shell;

	@Override
	public int open()
	{
		// create shell
		shell = getShell();
		if (shell == null || shell.isDisposed())
		{
			shell = null;
			create();
			shell = getShell();
		}
		itemExpand.setShellBounds(shell);

		// select top index
		/*
		 * BreadcrumbItem parentItem = itemExpand.getParentItem(); Object object = parentItem.fragment;
		 */

		final Tree tree = ((TreeViewer) structuredViewer).getTree();
		final TreeItem[] selection = tree.getSelection();
		if (selection.length > 0)
		{
			tree.setTopItem(selection[0]);
			// tree.notifyListeners(SWT.Selection, new Event());
		}

		if (!shell.isDisposed())
		{
			shell.setVisible(true);
			installCloser(shell);
		}
		return OK;
	}

	/**
	 * The closer closes the given shell when the focus is lost.
	 *
	 * @param shell
	 *        the shell to install the closer to
	 */
	private void installCloser(final Shell shell)
	{
		final Listener focusListener = new Listener()
		{
			public void handleEvent(Event event)
			{
				Widget focusElement = event.widget;
				boolean isFocusBreadcrumbTreeFocusWidget = focusElement == shell || focusElement instanceof Tree
					&& ((Tree) focusElement).getShell() == shell;
				boolean isFocusWidgetParentShell = focusElement instanceof Control
					&& ((Control) focusElement).getShell().getParent() == shell;

				switch (event.type)
				{
					case SWT.FocusIn:

						if (!isFocusBreadcrumbTreeFocusWidget && !isFocusWidgetParentShell)
						{
							if (shell != null && !shell.isDisposed())
							{
								shell.close();
							}
						}
						break;

					case SWT.FocusOut:

						if (event.display.getActiveShell() == null)
						{

							if (shell != null && !shell.isDisposed())
							{

								shell.close();
								if (null != itemExpand)
								{
									itemExpand.dropDownOpen = false;
									if (null != itemExpand.getParentItem())
									{
										itemExpand.getParentItem().setDropdownOpen(false);
									}
								}
							}
						}
						break;

					default:
						Assert.isTrue(false);
				}
			}
		};

		final Display display = shell.getDisplay();

		display.addFilter(SWT.FocusIn, focusListener);
		display.addFilter(SWT.FocusOut, focusListener);

		shell.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				display.removeFilter(SWT.FocusIn, focusListener);
				display.removeFilter(SWT.FocusOut, focusListener);
			}
		});
	}

	private Composite selectComposite(final Composite parent, int index)
	{
		Tree tree = null;
		Composite content = new Composite(parent, SWT.BORDER);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		content.setLayout(layout);

		if (index >= 0)
		{
			Object input = (itemExpand.getViewer().getItem(index)).fragment;

			int style = SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL;
			if (!isLTR())
			{
				style |= SWT.RIGHT_TO_LEFT;
			}
			structuredViewer = new TreeViewer(content, style);
			structuredViewer.setUseHashlookup(true);

			tree = ((TreeViewer) structuredViewer).getTree();
			tree.setLayoutData(new GridData(GridData.FILL_BOTH));

			// separator
			final Label sep = new Label(content, SWT.SEPARATOR | SWT.HORIZONTAL);
			sep.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			if (index > 0)
			{
				input = (itemExpand.getViewer().getItem(index - 1)).fragment;
			}
			else
			{
				input = new BreadcrumbRootWrapper(itemExpand.getParentItem().fragment);
			}

			setupTreeListeners(content, tree, input);

			itemExpand.getViewer().configureDropDownViewer(structuredViewer, input);
			structuredViewer.setInput(input);

			// set selection in tree
			TreeItem[] items = tree.getItems();
			if (items != null)
			{
				for (int i = 0; i < items.length; i++)
				{
					if (items[i].getText().equals(itemExpand.getParentItem().getText()))
					{
						tree.select(items[i]);
					}
				}
			}

		}
		else
		// dirty explorer
		{
			java.util.List<AutosarReleaseDescriptor> descriptors = MetaModelDescriptorRegistry.INSTANCE.getDescriptors(AutosarReleaseDescriptor.INSTANCE);

			// get the content types list for the given descriptors
			java.util.List<IContentType> contentTypes = PlatformUIUtils.getContentTypesForReleaseDescriptors(descriptors);

			FileTypesResourceViewer ftrV = new FileTypesResourceViewer(true, contentTypes);
			ftrV.createContents(content, ResourcesPlugin.getWorkspace().getRoot());
			tree = ftrV.getViewer().getTree();

			structuredViewer = ftrV.getViewer();

			setupWorkspaceTree(content, itemExpand.getParentItem(), ((TreeViewer) structuredViewer).getTree());

			ftrV.getViewer().expandAll();

			// set selection in tree
			Object data = itemExpand.getParentItem().getData();

			if (null != data)
			{
				String path = ((IFile) itemExpand.getParentItem().getData()).getFullPath().toPortableString();
				String[] segments = path.split("/"); //$NON-NLS-1$
				TreeItem[] items = ftrV.getViewer().getTree().getItems();
				if (null != segments && items != null)
				{
					treeSelect(tree, items, segments, 1);
				}
			}

		}

		return content;
	}

	private void treeSelect(Tree tree, TreeItem[] items, String[] segments, int idx)
	{
		for (int i = 0; i < items.length; i++)
		{
			if ((items[i].getData() instanceof IProject))
			{
				if (((IProject) items[i].getData()).getFullPath().toString().equals("/" + segments[idx])) //$NON-NLS-1$
				{
					idx++;
					treeSelect(tree, items[i].getItems(), segments, idx);
				}
			}
			else
			{
				if (items[i].getData() instanceof IFolder)
				{
					if (((IFolder) items[i].getData()).getName().toString().equals(segments[idx]))
					{
						idx++;
						treeSelect(tree, items[i].getItems(), segments, idx);
					}
				}
				else
				{
					if (items[i].getData() instanceof IFile)
					{
						if (((IFile) items[i].getData()).getName().equals(segments[idx]))
						{
							tree.select(items[i]);
							return;
						}
					}
				}
			}
		}
	}

	private void setupWorkspaceTree(final Composite content, final BreadcrumbItem item, final Tree tree)
	{
		// status line
		final Composite statusLine = new Composite(content, SWT.NONE);
		statusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final GridLayout slLayout = new GridLayout(2, false);
		slLayout.marginWidth = slLayout.marginHeight = 0;
		statusLine.setLayout(slLayout);

		if (isLTR())
		{
			lblMessage = new CLabel(statusLine, SWT.NONE);
			lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		}
		else
		{
			lblMessage = new CLabel(statusLine, SWT.RIGHT_TO_LEFT);
			lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		structuredViewer.addOpenListener(new IOpenListener()
		{
			public void open(final OpenEvent event)
			{

				final ISelection selection = event.getSelection();
				if (!(selection instanceof IStructuredSelection))
				{
					return;
				}

				final Object element = ((IStructuredSelection) selection).getFirstElement();
				if (element == null)
				{
					return;
				}

				BreadcrumbItem parentItem = itemExpand.getParentItem();
				BreadcrumbViewer viewer = (BreadcrumbViewer) parentItem.getViewer();

				if (!item.isDisposed())
				{
					if (element instanceof IFile)
					{
						close();
						IFile iFile = (IFile) element;
						Resource res = loadResources(iFile);
						if (res != null)
						{
							viewer.reloadOnFileChanged(iFile);
						}
					}
					return;
				}

			}

		});

		tree.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(final Event event)
			{
				if (tree.getSelectionCount() == 0)
				{
					lblMessage.setText(null);
					lblMessage.setImage(null);
				}
				else
				{
					final TreeItem item = tree.getSelection()[0];
					lblMessage.setText(item.getText());
					lblMessage.setImage(item.getImage());
				}
			}
		});

		tree.addListener(SWT.MouseMove, new Listener()
		{
			TreeItem fLastItem = null;

			public void handleEvent(final Event e)
			{
				if (!tree.equals(e.widget))
				{
					return;
				}

				final TreeItem currentItem = tree.getItem(new Point(e.x, e.y));
				if (currentItem == null)
				{
					return;
				}

				if (!currentItem.equals(fLastItem))
				{
					updateSelection(currentItem, e);
				}
				else if (e.y < tree.getItemHeight() / 4)
				{
					// scroll up
					if (currentItem.getParentItem() == null)
					{
						final int index = tree.indexOf(currentItem);
						if (index < 1)
						{
							return;
						}
						updateSelection(tree.getItem(index - 1), e);
					}
					else
					{
						final Point p = tree.toDisplay(e.x, e.y);
						final Item item = structuredViewer.scrollUp(p.x, p.y);
						if (item != null && item instanceof TreeItem)
						{
							updateSelection((TreeItem) item, e);
						}
					}
				}
				else if (e.y > tree.getBounds().height - tree.getItemHeight() / 4)
				{
					// scroll down
					if (currentItem.getParentItem() == null)
					{
						final int index = tree.indexOf(currentItem);
						if (index >= tree.getItemCount() - 1)
						{
							return;
						}
						updateSelection(tree.getItem(index + 1), e);
					}
					else
					{
						final Point p = tree.toDisplay(e.x, e.y);
						final Item item = structuredViewer.scrollDown(p.x, p.y);
						if (item != null && item instanceof TreeItem)
						{
							updateSelection((TreeItem) item, e);
						}
					}
				}
			}

			protected void updateSelection(final TreeItem item, final Event e)
			{
				fLastItem = item;

				e.type = SWT.Selection;
				final TreeItem[] selection = new TreeItem[] {item};
				tree.setSelection(selection);
				tree.notifyListeners(SWT.Selection, e);
				shell.setActive();
			}
		});

		tree.addListener(SWT.MouseUp, new Listener()
		{
			public void handleEvent(final Event event)
			{
				if (event.button != 1)
				{
					return;
				}

				final Item item = tree.getItem(new Point(event.x, event.y));
				if (item == null)
				{
					return;
				}

				final Object element = item.getData();
				if (element == null)
				{
					return;
				}

				BreadcrumbItem parentItem = itemExpand.getParentItem();
				BreadcrumbViewer viewer = (BreadcrumbViewer) parentItem.getViewer();

				if (!item.isDisposed())
				{
					if (element instanceof IFile)
					{
						close();
						IFile iFile = (IFile) element;
						Resource res = loadResources(iFile);
						if (res != null)
						{
							viewer.reloadOnFileChanged(iFile);
						}
					}
					return;
				}
			}
		});

		// //////
		/*
		 * structuredViewer.addSelectionChangedListener(new ISelectionChangedListener() {
		 *
		 * public void selectionChanged(SelectionChangedEvent event) { final ISelection selection =
		 * event.getSelection(); if (!(selection instanceof IStructuredSelection)) { return; }
		 *
		 * final Object element = ((IStructuredSelection) selection).getFirstElement(); if (element == null) { return; }
		 *
		 * BreadcrumbItem parentItem = itemExpand.getParentItem(); BreadcrumbViewer viewer = (BreadcrumbViewer)
		 * parentItem.getViewer();
		 *
		 * if (!item.isDisposed()) { if (element instanceof IFile) { close(); IFile iFile = (IFile) element; Resource
		 * res = loadResources(iFile); if (res != null) { viewer.reloadOnFileChanged(iFile); } } return; } } });
		 */

		/*
		 * tree.addListener(SWT.MouseMove, new Listener() {
		 *
		 * public void handleEvent(final Event e) { shell.setActive(); }
		 *
		 * });
		 */
	}

	//
	private Resource loadResources(final IFile iFile)
	{
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		final Resource[] res = new Resource[1];

		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress()
		{
			public void run(final IProgressMonitor monitor) throws InvocationTargetException
			{
				monitor.beginTask("Loading autosar resources", IProgressMonitor.UNKNOWN);
				try
				{
					// check if selected file is loaded
					IModelDescriptor modelDescriptor = ModelDescriptorRegistry.INSTANCE.getModel(iFile);
					if (modelDescriptor != null)
					{
						EObject modelRoot = EcorePlatformUtil.getModelRoot(iFile);
						if (modelRoot == null)
						{
							ModelLoadManager.INSTANCE.loadModel(modelDescriptor, false, null);
						}
						// res[0] = modelRoot.eResource(); - null pointer
						// occasionally
						res[0] = ModelUtils.getDefinedResource(iFile);
					}
				}
				catch (Exception e)
				{
					CessarPluginActivator.getDefault().logError(e.getCause());
				}
				monitor.done();
			}
		};

		try
		{
			new ProgressMonitorDialog(shell).run(true, true, new WorkspaceModifyDelegatingOperation(
				runnableWithProgress));
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e.fillInStackTrace());
		}

		return res[0];
	}

	private void setupTreeListeners(Composite content, final Tree tree, Object input)
	{
		// status line
		final Composite statusLine = new Composite(content, SWT.NONE);
		statusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final GridLayout slLayout = new GridLayout(2, false);
		slLayout.marginWidth = slLayout.marginHeight = 0;
		statusLine.setLayout(slLayout);

		if (isLTR())
		{
			lblMessage = new CLabel(statusLine, SWT.NONE);
			lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		}
		else
		{
			lblMessage = new CLabel(statusLine, SWT.RIGHT_TO_LEFT);
			lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		structuredViewer.addOpenListener(new IOpenListener()
		{
			public void open(final OpenEvent event)
			{

				final ISelection selection = event.getSelection();
				if (!(selection instanceof IStructuredSelection))
				{
					return;
				}

				final Object element = ((IStructuredSelection) selection).getFirstElement();
				if (element == null)
				{
					return;
				}

				itemExpand.getViewer().fireMenuSelection(element);
				if (isShellDisposed())
				{
					return;
				}

				if (((TreeViewer) structuredViewer).getExpandedState(element))
				{
					((TreeViewer) structuredViewer).collapseToLevel(element, 1);
				}
				else
				{
					tree.setRedraw(false);
					try
					{
						((TreeViewer) structuredViewer).expandToLevel(element, 1);
						resizeShell(getShell());
					}
					finally
					{
						tree.setRedraw(true);
					}
				}
			}

		});

		tree.addListener(SWT.MouseUp, new Listener()
		{
			public void handleEvent(final Event event)
			{
				if (event.button != 1)
				{
					return;
				}

				final Item item = tree.getItem(new Point(event.x, event.y));
				if (item == null)
				{
					return;
				}

				final Object data = item.getData();
				if (data == null)
				{
					return;
				}

				// close();
				itemExpand.getViewer().fireMenuSelection(data);

				if (isShellDisposed())
				{
					return;
				}

				if (((TreeViewer) structuredViewer).getExpandedState(data))
				{
					((TreeViewer) structuredViewer).collapseToLevel(data, 1);
				}
				else
				{
					tree.setRedraw(false);
					try
					{
						((TreeViewer) structuredViewer).expandToLevel(data, 1);
						resizeShell(getShell());
					}
					finally
					{
						tree.setRedraw(true);
					}
				}
			}
		});

		tree.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(final Event event)
			{
				if (tree.getSelectionCount() == 0)
				{
					lblMessage.setText(null);
					lblMessage.setImage(null);
				}
				else
				{
					final TreeItem item = tree.getSelection()[0];
					lblMessage.setText(item.getText());
					lblMessage.setImage(item.getImage());
				}
			}
		});

		tree.addListener(SWT.MouseMove, new Listener()
		{
			TreeItem fLastItem = null;

			public void handleEvent(final Event e)
			{
				if (!tree.equals(e.widget))
				{
					return;
				}

				final TreeItem currentItem = tree.getItem(new Point(e.x, e.y));
				if (currentItem == null)
				{
					return;
				}

				if (!currentItem.equals(fLastItem))
				{
					updateSelection(currentItem, e);
				}
				else if (e.y < tree.getItemHeight() / 4)
				{
					// scroll up
					if (currentItem.getParentItem() == null)
					{
						final int index = tree.indexOf(currentItem);
						if (index < 1)
						{
							return;
						}
						updateSelection(tree.getItem(index - 1), e);
					}
					else
					{
						final Point p = tree.toDisplay(e.x, e.y);
						final Item item = structuredViewer.scrollUp(p.x, p.y);
						if (item != null && item instanceof TreeItem)
						{
							updateSelection((TreeItem) item, e);
						}
					}
				}
				else if (e.y > tree.getBounds().height - tree.getItemHeight() / 4)
				{
					// scroll down
					if (currentItem.getParentItem() == null)
					{
						final int index = tree.indexOf(currentItem);
						if (index >= tree.getItemCount() - 1)
						{
							return;
						}
						updateSelection(tree.getItem(index + 1), e);
					}
					else
					{
						final Point p = tree.toDisplay(e.x, e.y);
						final Item item = structuredViewer.scrollDown(p.x, p.y);
						if (item != null && item instanceof TreeItem)
						{
							updateSelection((TreeItem) item, e);
						}
					}
				}
			}

			protected void updateSelection(final TreeItem item, final Event e)
			{
				fLastItem = item;
				e.type = SWT.Selection;
				final TreeItem[] selection = new TreeItem[] {item};
				tree.setSelection(selection);
				tree.notifyListeners(SWT.Selection, e);
				shell.setActive();
			}
		});

		tree.addListener(SWT.KeyDown, new Listener()
		{
			public void handleEvent(final Event event)
			{
				switch (event.keyCode)
				{
					case SWT.ARROW_UP:
						handleSelection(false);
						break;
					case SWT.ARROW_LEFT:
						handleSelection(true);
						break;
				}
			}

			protected void handleSelection(final boolean leftKey)
			{
				// get selection
				final TreeItem[] selection = tree.getSelection();
				if (selection.length != 1)
				{
					return;
				}
				final TreeItem item = selection[0];

				if (leftKey)
				{
					// expanded ?
					if (item.getItemCount() != 0 && item.getExpanded())
					{
						return;
					}

					// root ?
					if (item.getParentItem() == null)
					{
						close();
					}
				}
				else
				{
					// first item ?
					final int selectionIndex = tree.indexOf(item);
					if (selectionIndex == 0)
					{
						close();
					}
				}
			}
		});

		((TreeViewer) structuredViewer).addTreeListener(new ITreeViewerListener()
		{
			public void treeCollapsed(final TreeExpansionEvent event)
			{
			}

			public void treeExpanded(final TreeExpansionEvent event)
			{
				tree.setRedraw(false);
				getShell().getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						if (isShellDisposed())
						{
							return;
						}
						try
						{
							resizeShell(getShell());
						}
						finally
						{
							tree.setRedraw(true);
						}
					}

					private void resizeShell(Shell shell)
					{
						itemExpand.resizeShell(shell);

					}
				});
			}
		});
	}

	@Override
	protected Control createContents(final Composite parent)
	{
		// show in the window the sibilings of the object for which the dropdown
		// is created
		int index = itemExpand.getViewer().indexOf(itemExpand.getParentItem());

		return selectComposite(parent, index);

	}
}
