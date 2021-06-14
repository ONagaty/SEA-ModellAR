package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import eu.cessar.ct.core.platform.ui.events.IBreadcrumbChangeListener;
import eu.cessar.ct.core.platform.ui.events.IBreadcrumbFileChangeListener;
import eu.cessar.ct.core.platform.ui.events.IBreadcrumbInputChangeListener;
import eu.cessar.ct.core.platform.ui.events.IDataRefreshListener;
import eu.cessar.ct.core.platform.ui.events.IMenuSelectionListener;
import eu.cessar.ct.core.platform.ui.events.MenuSelectionEvent;
import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * A breadcrumb viewer shows a the parent chain of its input element in a list. Each breadcrumb item of that list can be
 * expanded and a sibling of the element presented by the breadcrumb item can be selected.
 * <p>
 * Content providers for breadcrumb viewers must implement the <code>IContentProvider</code> interface.
 * </p>
 * <p>
 * Label providers for breadcrumb viewers must implement the <code>ILabelProvider</code> interface.
 * </p>
 */
public abstract class BreadcrumbViewer extends StructuredViewer implements IDataRefreshListener
{

	private static final boolean IS_GTK = "gtk".equals(SWT.getPlatform()); //$NON-NLS-1$

	protected Composite bcwContainer;
	protected Composite wContainer;
	protected Composite bcContainer;
	protected final ArrayList<BreadcrumbItem> items;
	private final ArrayList<MenuDetectListener> menuDetectListeners;
	private final ArrayList<IMenuSelectionListener> menuSelectionListeners;

	protected BreadcrumbItem selectedItem;
	protected ILabelProvider toolTipLabelProvider;
	protected boolean rootVisible = false;
	private BreadcrumbItem workspaceItem;
	protected boolean showRoot = true;

	/**
	 * Creates a breadcrumb viewer under the given parent. The control is created using the SWT style bits
	 * <code>HORIZONTAL</code>. The viewer has no input, no content provider, and no label provider.
	 *
	 * @param parent
	 *        the parent control.
	 */
	public BreadcrumbViewer(final Composite parent, boolean showRoot)
	{
		this(parent, SWT.NONE, showRoot);
	}

	/**
	 * @return the workspaceItem
	 */
	public BreadcrumbItem getWorkspaceItem()
	{
		return workspaceItem;
	}

	private IFile currentFile;

	/**
	 * @return the currentFile
	 */
	public IFile getCurrentFile()
	{
		return currentFile;
	}

	/**
	 * @return the workspaceItem
	 */
	public void updateDirtyExplorer(IFile file)
	{
		currentFile = file;
		if (showRoot && workspaceItem != null)
		{
			if (file != null)
			{
				workspaceItem.setText(file.getName());
			}
			else
			{
				workspaceItem.setText(eu.cessar.ct.core.internal.platform.ui.Messages.FirstBreadcrumbItem_label);
				for (IBreadcrumbChangeListener iterable: listenerList)
				{

					if (iterable instanceof IBreadcrumbInputChangeListener)
					{
						((IBreadcrumbInputChangeListener) iterable).inputBreadcrumbSelectionChanged(
							new BreadcrumbInputChangeEvent(null));

					}
				}
			}
			workspaceItem.setData(file);
			workspaceItem.fragment = file;
			// ?
			workspaceItem.setDropdownOpen(false);
		}
	}

	protected void createWorkspaceItem(Composite wContainer)
	{
		workspaceItem = new BreadcrumbItem(this, wContainer);
		workspaceItem.setLabelProvider(getLabelProvider());
		// item.setContentProvider(getContentProvider());
		if (toolTipLabelProvider != null)
		{
			workspaceItem.setToolTipLabelProvider(toolTipLabelProvider);
		}
		else
		{
			workspaceItem.setToolTipLabelProvider(getLabelProvider());
		}
		workspaceItem.setText(eu.cessar.ct.core.internal.platform.ui.Messages.FirstBreadcrumbItem_label);
	}

	/**
	 * Creates a breadcrumb viewer under the given parent. The control is created using the given style bits. The viewer
	 * has no input, no content provider, and no label provider.
	 * <p>
	 * Allowed styles are one of:
	 * <ul>
	 * <li>SWT.NONE</li>
	 * <li>SWT.VERTICAL</li>
	 * <li>SWT.HORIZONTAL</li>
	 * </ul>
	 *
	 * @param parent
	 *        the parent control.
	 * @param style
	 *        SWT style bits.
	 */
	public BreadcrumbViewer(final Composite parent, final int style, boolean showRoot)
	{
		items = new ArrayList<BreadcrumbItem>();
		menuDetectListeners = new ArrayList<MenuDetectListener>();
		menuSelectionListeners = new ArrayList<IMenuSelectionListener>();

		bcwContainer = new Composite(parent, style);

		if (showRoot)
		{
			wContainer = new Composite(bcwContainer, style);
			// add listeners
			wContainer.addListener(SWT.Traverse, new Listener()
			{
				public void handleEvent(final Event event)
				{
					event.doit = true;
				}
			});
			wContainer.addListener(SWT.Resize, new Listener()
			{
				public void handleEvent(final Event event)
				{
					// update layout
					refresh();
				}
			});

			wContainer.addListener(SWT.FocusIn, new Listener()
			{
				public void handleEvent(final Event event)
				{
					if (selectedItem != null)
					{
						selectedItem.setHasFocus(true);
					}
				}
			});
			wContainer.addListener(SWT.MouseDown, new Listener()
			{
				public void handleEvent(final Event event)
				{
					if (event.button == 1)
					{
						setFocus();
					}

					if (event.button == 2 || event.button == 3)
					{
						return;
					}
				}
			});

			hookControl(wContainer);
			createWorkspaceItem(wContainer);
		}

		bcContainer = new Composite(bcwContainer, style);
		bcContainer.setBackgroundMode(SWT.INHERIT_DEFAULT);

		// add listeners
		bcContainer.addListener(SWT.Traverse, new Listener()
		{
			public void handleEvent(final Event event)
			{
				event.doit = true;
			}
		});
		bcContainer.addListener(SWT.Resize, new Listener()
		{
			public void handleEvent(final Event event)
			{
				// update layout
				refresh();
			}
		});

		bcContainer.addListener(SWT.FocusIn, new Listener()
		{
			public void handleEvent(final Event event)
			{
				if (selectedItem != null)
				{
					selectedItem.setHasFocus(true);
				}
			}
		});
		bcContainer.addListener(SWT.MouseDown, new Listener()
		{
			public void handleEvent(final Event event)
			{
				if (event.button == 1)
				{
					setFocus();
				}

				if (event.button == 2 || event.button == 3)
				{
					return;
				}
			}
		});

		hookControl(bcContainer);

		// layout
		int columns = 1000;
		if ((SWT.VERTICAL & style) != 0)
		{
			columns = 1;
		}
		GridLayout gridLayout = new GridLayout(columns, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = gridLayout.horizontalSpacing = 0;
		bcwContainer.setLayout(gridLayout);

		gridLayout = new GridLayout(columns, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = gridLayout.horizontalSpacing = 0;
		bcContainer.setLayout(gridLayout);

		if (showRoot)
		{
			gridLayout = new GridLayout(columns, false);
			gridLayout.marginWidth = gridLayout.marginHeight = 0;
			gridLayout.verticalSpacing = gridLayout.horizontalSpacing = 0;
			wContainer.setLayout(gridLayout);
		}

		GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, true);
		gridData.heightHint = SWT.DEFAULT;
		gridData.widthHint = SWT.DEFAULT;
		bcwContainer.setLayoutData(gridData);

		bcwContainer.layout();
	}

	/**
	 * Add the given listener to the set of listeners which will be informed when a context menu is requested for a
	 * breadcrumb item.
	 *
	 * @param listener
	 *        the listener to add
	 * @exception IllegalArgumentException
	 *            <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *            </ul>
	 */
	public void addMenuDetectListener(final MenuDetectListener listener)
	{
		if (listener == null)
		{
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		menuDetectListeners.add(listener);
	}

	/**
	 * Add the given listener to the set of listeners which will be informed when a selection of the drop down menu
	 * occurs.
	 *
	 * @param listener
	 *        the listener to add
	 * @exception IllegalArgumentException
	 *            <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *            </ul>
	 */
	public void addMenuSelectionListener(final IMenuSelectionListener listener)
	{
		if (listener == null)
		{
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		menuSelectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ContentViewer#getContentProvider()
	 */
	@Override
	public IContentProvider getContentProvider()
	{
		return super.getContentProvider();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl()
	{
		return bcContainer;
	}

	/**
	 * Returns the selection provider which provides the selection of the drop down currently opened or
	 * <code>null</code> if no drop down is open at the moment.
	 *
	 * @return the selection provider of the open drop down or <code>null</code> .
	 */
	public ISelectionProvider getDropDownSelectionProvider()
	{
		for (final BreadcrumbItem item: items)
		{
			if (item.isMenuShown())
			{
				return item.getDropDownSelectionProvider();
			}
		}
		return null;
	}

	/**
	 * The Window used for the shown drop down menu or <code>null</code> if no drop down is shown at the moment.
	 *
	 * @return the drop downs window or <code>null</code>.
	 */
	public Window getDropDownWindow()
	{
		if (getWorkspaceItem() != null && getWorkspaceItem().isMenuShown())
		{
			return getWorkspaceItem().getDropDownWindow();
		}
		for (final BreadcrumbItem item: items)
		{
			if (item.isMenuShown())
			{
				return item.getDropDownWindow();
			}
		}
		return null;
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver. Throws an exception if the index is out of
	 * range.
	 *
	 * @param index
	 *        the index of the item to return.
	 * @return the item at the given index.
	 * @throws IndexOutOfBoundsException
	 *         if the index is out of range ( <tt>index &lt; 0 || index &gt;= getItemCount()</tt>).
	 */
	public BreadcrumbItem getItem(final int index)
	{
		return items.get(index);
	}

	/**
	 * Returns the item at the given point in the receiver or null if no such item exists. The point is in the
	 * coordinate system of the receiver.
	 *
	 * @param point
	 *        the point used to locate the item.
	 * @return the item at the given point, or <code>null</code> if the point is not in a selectable item.
	 * @exception IllegalArgumentException
	 *            <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *            </ul>
	 */
	public BreadcrumbItem getItem(final Point point)
	{
		if (point == null)
		{
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		for (final BreadcrumbItem item: items)
		{
			if (item.getBounds().contains(point))
			{
				return item;
			}
		}
		return null;
	}

	/**
	 * Returns the number of items contained in the receiver.
	 *
	 * @return the number of items.
	 */
	public int getItemCount()
	{
		return items.size();
	}

	/**
	 * Returns a (possibly empty) array of items contained in the receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain its list of items, so modifying the array
	 * will not affect the receiver.
	 * </p>
	 *
	 * @return the items.
	 */
	public BreadcrumbItem[] getItems()
	{
		return items.toArray(new BreadcrumbItem[0]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ContentViewer#getLabelProvider()
	 */
	@Override
	public ILabelProvider getLabelProvider()
	{
		return (ILabelProvider) super.getLabelProvider();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelection()
	 */
	@Override
	public IStructuredSelection getSelection()
	{
		return (IStructuredSelection) super.getSelection();
	}

	/**
	 * Returns the index of the first occurrence of the specified element, or -1 if this does not contain the element.
	 *
	 * @param item
	 *        the item to search for.
	 * @return the index of the first occurrence of the specified element, or -1 if this does not contain the element.
	 * @exception IllegalArgumentException
	 *            <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *            </ul>
	 */
	public int indexOf(final BreadcrumbItem item)
	{
		if (item == null)
		{
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return items.indexOf(item);
	}

	/**
	 * Gets a value indicating if any of the items in the viewer is expanded.
	 *
	 * @return <code>true</code> if any of the items in the viewer is expanded.
	 */
	public boolean isDropDownOpen()
	{
		for (final BreadcrumbItem item: items)
		{
			if (item.isMenuShown())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the root visible.
	 *
	 * @return <code>true</code> if the root item is visible, <code>false</code> otherwise.
	 */
	public boolean isRootVisible()
	{
		return rootVisible;
	}

	/**
	 * Display the drop-down menu for the given element
	 *
	 * @param element
	 *        the element for open the drop-down menu.
	 */
	public void openDropDownMenu(final Object element)
	{
		// dirty explorer
		if (element == null && showRoot)
		{
			if (null != workspaceItem)
			{
				workspaceItem.setSelected(true);
				workspaceItem.setHasFocus(true);
				// currentFile
				// workspaceItem.setDropdownOpen(false);
				workspaceItem.openDropDownMenu();
			}
		}
		else
		// breadcrumb item
		{
			final BreadcrumbItem item = (BreadcrumbItem) doFindItem(element);
			if (item != null)
			{
				if (item.getItemDetail().isRightClick())
				{
					item.openDropDownMenu();
				}
				item.getItemDetail().enableRightClick(true);
			}
		}
	}

	/**
	 * Remove the given listener from the set of menu detect listeners. Does nothing if the listener is not element of
	 * the set.
	 *
	 * @param listener
	 *        the listener to remove.
	 * @exception IllegalArgumentException
	 *            <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *            </ul>
	 */
	public void removeMenuDetectListener(final MenuDetectListener listener)
	{
		if (listener == null)
		{
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		menuDetectListeners.remove(listener);
	}

	/**
	 * Remove the given listener from the set of menu selection listeners. Does nothing if the listener is not element
	 * of the set.
	 *
	 * @param listener
	 *        the listener to remove.
	 * @exception IllegalArgumentException
	 *            <ul>
	 *            <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *            </ul>
	 */
	public void removeMenuSelectionListener(final IMenuSelectionListener listener)
	{
		if (listener == null)
		{
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		menuSelectionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#reveal(java.lang.Object)
	 */
	@Override
	public void reveal(final Object element)
	{
		// all elements are always visible
	}

	/**
	 * Causes the receiver to have the <em>keyboard focus</em>, such that all keyboard events will be delivered to it.
	 * Focus reassignment will respect applicable platform constraints.
	 */
	public void setFocus()
	{
		bcContainer.setFocus();

		if (selectedItem != null)
		{
			selectedItem.setHasFocus(true);
		}
		/*
		 * else { final int size = items.size(); if (size == 0) { return; } BreadcrumbItem item = items.get(size - 1);
		 * if (item.fragment == null) { if (size < 2) { return; } item = items.get(size - 2); } item.setHasFocus(true);
		 * }
		 */
	}

	/**
	 * Sets the root item visibility.
	 *
	 * @param rootVisible
	 *        <code>true</code> to set the root item visible, <code>false</code> to hide.
	 */
	public void setRootVisible(final boolean rootVisible)
	{
		if (this.rootVisible == rootVisible)
		{
			return;
		}
		this.rootVisible = rootVisible;
		if (items.size() != 0)
		{
			final BreadcrumbItem item = items.get(0);
			item.setDetailsVisible(rootVisible);
			bcContainer.layout(true, true);
			if (rootVisible || selectedItem == null)
			{
				return;
			}

			// root selected ?
			if (selectedItem == item || selectedItem.equals(item))
			{
				if (items.size() > 1)
				{
					selectItem(items.get(1));
				}
			}
		}
	}

	/**
	 * The tool tip to use for the tool tip labels. <code>null</code> if the viewers label provider should be used.
	 *
	 * @param toolTipLabelProvider
	 *        the label provider for the tool tips or <code>null</code>.
	 */
	public void setToolTipLabelProvider(final ILabelProvider toolTipLabelProvider)
	{
		this.toolTipLabelProvider = toolTipLabelProvider;
	}

	/**
	 * Configure the given drop down viewer. The given input is used for the viewers input. Clients must at least set
	 * the label provider and the content provider for the viewer.
	 *
	 * @param viewer
	 *        the viewer to configure
	 * @param input
	 *        the input for the viewer.
	 */
	protected abstract void configureDropDownViewer(StructuredViewer viewer, Object input);

	/**
	 * Creates a new instance of a breadcrumb item.
	 */
	protected abstract BreadcrumbItem createItem();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
	 */
	@Override
	protected Widget doFindInputItem(final Object element)
	{
		if (element == null)
		{
			return null;
		}

		if (element == getInput() || element.equals(getInput()))
		{
			return doFindItem(element);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	@Override
	protected Widget doFindItem(final Object element)
	{
		if (element == null)
		{
			return null;
		}

		for (final BreadcrumbItem item: items)
		{
			if (element == item.fragment || element.equals(item.fragment))
			{
				return item;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(org.eclipse.swt.widgets.Widget, java.lang.Object,
	 * boolean)
	 */
	@Override
	protected void doUpdateItem(final Widget widget, final Object element, final boolean fullMap)
	{
		if (widget instanceof BreadcrumbItem)
		{
			final BreadcrumbItem item = (BreadcrumbItem) widget;

			// remember element we are showing
			if (fullMap)
			{
				associate(element, item);
			}
			else
			{
				final Object data = item.fragment;
				if (data != null)
				{
					unmapElement(data, item);
				}
				item.fragment = element;
				mapElement(element, item);
			}
			// update
			item.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRoot()
	 */
	@Override
	protected Object getRoot()
	{
		if (items.isEmpty())
		{
			return null;
		}
		return items.get(0).fragment;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	@Override
	protected List<?> getSelectionFromWidget()
	{
		if (selectedItem == null || selectedItem.fragment == null)
		{
			return Collections.EMPTY_LIST;
		}

		final ArrayList<Object> list = new ArrayList<Object>();
		list.add(selectedItem.fragment);
		return Collections.unmodifiableList(list);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object,java.lang.Object)
	 */
	@Override
	protected void inputChanged(Object fInput, final Object oldInput)
	{
		if (bcContainer.isDisposed())
		{
			return;
		}

		disableRedraw();

		if (fInput instanceof IFile)
		{
			for (IBreadcrumbChangeListener iterable: listenerList)
			{
				if (iterable instanceof IBreadcrumbFileChangeListener)
				{
					((IBreadcrumbFileChangeListener) iterable).inputFileChanged(
						new BreadcrumbFileChangeEvent((IFile) fInput));
				}
			}
			Resource res = ModelUtils.getDefinedResource((IFile) fInput);
			if (res != null)
			{
				EList<EObject> contents = res.getContents();
				fInput = contents.get(0);
			}
		}

		try
		{
			if (items.size() > 0)
			{
				final BreadcrumbItem last = items.get(items.size() - 1);
				last.setIsLastItem(false);
			}

			final int lastIndex = buildItemChain(fInput);

			if (lastIndex > 0)
			{
				final BreadcrumbItem last = items.get(lastIndex - 1);
				last.setIsLastItem(true);
			}

			while (lastIndex < items.size())
			{
				final BreadcrumbItem item = items.remove(items.size() - 1);
				if (item == selectedItem)
				{
					selectItem(null);
				}
				if (item.fragment != null)
				{
					unmapElement(item.fragment);
				}
				item.dispose();
			}

			if (!bcContainer.isDisposed())
			{
				updateSize();
				bcContainer.layout(true, true);
			}
		}
		finally
		{
			enableRedraw();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	@Override
	protected void internalRefresh(final Object element)
	{
		disableRedraw();
		try
		{
			final BreadcrumbItem item = (BreadcrumbItem) doFindItem(element);
			if (item == null)
			{
				for (final BreadcrumbItem current: items)
				{
					current.refresh();
				}
			}
			else
			{
				item.refresh();
			}
			if (updateSize())
			{
				bcContainer.layout(true, true);
			}
		}
		finally
		{
			enableRedraw();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List, boolean)
	 */
	@Override
	protected void setSelectionToWidget(@SuppressWarnings("rawtypes") final List list, final boolean reveal)
	{
		boolean focused = bcContainer.isFocusControl();
		BreadcrumbItem focusItem = null;

		for (final BreadcrumbItem item: items)
		{
			if (item.hasFocus())
			{
				focusItem = item;
			}
			item.setSelected(false);
			focused = focused | item.isFocusControl();
		}

		if (list == null)
		{
			return;
		}

		selectedItem = null;
		for (final Iterator<?> iter = list.iterator(); iter.hasNext();)
		{
			final Object element = iter.next();
			final BreadcrumbItem item = (BreadcrumbItem) doFindItem(element);
			if (item != null)
			{
				selectedItem = item;
				selectedItem.setSelected(true);
				if (item == focusItem)
				{
					item.setHasFocus(true);
				}
			}
		}
		if ((focused || reveal) && focusItem == null && selectedItem != null)
		{
			selectedItem.setHasFocus(true);
		}
	}

	/**
	 * Generates the parent chain of the given element.
	 *
	 * @param element
	 *        element to build the parent chain for
	 * @return the first index of an item in fBreadcrumbItems which is not part of the chain
	 */
	abstract protected int buildItemChain(final Object element);

	/**
	 * Disable redrawing of the breadcrumb.
	 * <p>
	 * <strong>A call to this method must be followed by a call to {@link #enableRedraw()}</strong>
	 * </p>
	 */
	private void disableRedraw()
	{
		if (IS_GTK)
		{
			return;
		}

		bcContainer.setRedraw(false);
	}

	/**
	 * Enable redrawing of the breadcrumb.
	 */
	protected void enableRedraw()
	{
		if (IS_GTK)
		{
			return;
		}
		if (!bcContainer.isDisposed())
		{
			bcContainer.setRedraw(true);
		}
	}

	/**
	 * Returns the current width of all items in the list.
	 *
	 * @return the width of all items.
	 */
	private int getCurrentWidth()
	{
		int result = 2;
		for (final BreadcrumbItem item: items)
		{
			result += item.getCurrentWidth();
		}
		return result;
	}

	/**
	 * Update the size of the items such that all items are visible, if possible.
	 *
	 * @return true if any item has changed, false otherwise.
	 */
	protected boolean updateSize()
	{
		boolean requiresLayout = false;
		int currentWidth = getCurrentWidth();
		final int width = bcContainer.getClientArea().width;

		if (currentWidth > width)
		{
			// hide texts
			int index = 0;
			while (currentWidth > width && index < items.size() - 1)
			{
				final BreadcrumbItem viewer = items.get(index);
				if (viewer.isShowText())
				{
					viewer.setTextVisible(false);
					currentWidth = getCurrentWidth();
					requiresLayout = true;
				}

				index++;
			}

		}
		else if (currentWidth < width)
		{
			// display texts
			int index = items.size() - 1;

			while (currentWidth < width && index >= 0)
			{
				final BreadcrumbItem item = items.get(index);
				if (!item.isShowText())
				{
					item.setTextVisible(true);
					currentWidth = getCurrentWidth();
					if (currentWidth > width)
					{
						item.setTextVisible(false);
						index = 0;
					}
					else
					{
						requiresLayout = true;
					}
				}
				index--;
			}
		}

		return requiresLayout;
	}

	/**
	 * Set selection, if possible, to the next or previous element.
	 *
	 * @param next
	 *        <code>true</code> for the next element, <code>false</code> for the previous element.
	 */
	abstract protected void doTraverse(final boolean next);

	/**
	 * Notify all double click listeners
	 */
	void fireDoubleClick()
	{
		fireDoubleClick(new DoubleClickEvent(this, getSelection()));
	}

	/**
	 * A context menu has been requested for the selected breadcrumb item.
	 *
	 * @param event
	 *        the event issued the menu detection
	 */
	void fireMenuDetect(final Event e)
	{
		final MenuDetectEvent event = new MenuDetectEvent(e);
		for (final MenuDetectListener listener: menuDetectListeners)
		{
			SafeRunnable.run(new SafeRunnable()
			{
				public void run()
				{
					listener.menuDetected(event);
				}
			});
		}
	}

	/**
	 * Fire event for the given element that was selected from a drop down menu.
	 *
	 * @param element
	 *        the selected element.
	 */
	void fireMenuSelection(final Object element)
	{

		final ISelection selection = new StructuredSelection(element);

		final MenuSelectionEvent event = new MenuSelectionEvent(this, selection);

		// for (final IMenuSelectionListener listener: menuSelectionListeners)
		// {
		SafeRunnable.run(new SafeRunnable()
		{
			public void run()
			{
				/*
				 * selectedItem.setSelected(false); selectedItem.getItemDetail().updateSelection();
				 * listener.menuSelect(event);
				 */

				selectedItem.setSelected(false);
				selectedItem.setHasFocus(false);

				selectedItem.getItemDetail().updateSelection();

				if (event.getSelection().isEmpty())
				{
					setFocus();
					return;
				}

				if (element instanceof EObject)
				{
					setInput(element);
					setFocus();
					selectedItem.setHasFocus(false);

					for (IBreadcrumbChangeListener iterable: listenerList)
					{
						if (iterable instanceof IBreadcrumbInputChangeListener)
						{
							((IBreadcrumbInputChangeListener) iterable).inputBreadcrumbSelectionChanged(
								new BreadcrumbInputChangeEvent(event.getSelection()));
						}
					}
				}
			}
		});
		// }

	}

	/**
	 * Notify all open listeners with the current selection.
	 */
	void fireOpen()
	{
		fireOpen(new OpenEvent(this, getSelection()));
	}

	private boolean fireDropdownOnSelectionChanged;

	/**
	 * @return the fireDropdownOnSelectionChanged
	 */
	public boolean isFireDropdownOnSelectionChanged()
	{
		return fireDropdownOnSelectionChanged;
	}

	/**
	 * @param fireDropdownOnSelectionChanged
	 *        the fireDropdownOnSelectionChanged to set
	 */
	public void setFireDropdownOnSelectionChanged(boolean fireDropdownOnSelectionChanged)
	{
		this.fireDropdownOnSelectionChanged = fireDropdownOnSelectionChanged;
	}

	/**
	 * Set a single selection to the given item, <code>null</code> to deselect all.
	 *
	 * @param item
	 *        the item to select or <code>null</code>
	 */
	protected void selectItem(final BreadcrumbItem item)
	{
		selectItemWithoutNotification(item);
		if (isFireDropdownOnSelectionChanged())
		{
			fireSelectionChanged(new SelectionChangedEvent(this, getSelection()));
		}
	}

	/**
	 * Set a single selection to the given item, <code>null</code> to deselect all. No notification is made.
	 *
	 * @param item
	 *        the item to select or <code>null</code>
	 */
	protected void selectItemWithoutNotification(final BreadcrumbItem item)
	{
		if (selectedItem != null)
		{
			selectedItem.setSelected(false);
		}

		selectedItem = item;
		setSelectionToWidget(getSelection(), false);

		if (item != null)
		{
			setFocus();
		}
		else
		{
			for (final BreadcrumbItem current: items)
			{
				current.setHasFocus(false);
			}
		}
	}

	/**
	 * Set a single selection to the given item, <code>-1</code> to deselect all.
	 *
	 * @param index
	 *        the item index to select or <code>-1</code>
	 */
	void selectItem(final int index)
	{
		if (index < 0 || index >= items.size())
		{
			selectItem(null);
		}
		else
		{
			selectItem(items.get(index));
		}
	}

	/**
	 * @param item
	 * @param container
	 * @return
	 */
	public BreadcrumbItemExpand createBreadcrumbItemExpand(BreadcrumbItem item, Composite container)
	{
		return new BreadcrumbItemTreeExpand(item, container);
	}

	protected void reloadOnFileChanged(IFile iFile)
	{
/*
 * TransactionalEditingDomain editingDomain = WorkspaceEditingDomainUtil.getEditingDomain(iFile);
 * configureProviders(editingDomain, ModelUtils.getDefinedResource(iFile));
 *
 * setInput(iFile); workspaceItem.setData(iFile); workspaceItem.setText(iFile.getName());
 * workspaceItem.setDropdownOpen(false);
 *
 * refreshLayout();
 *
 */ }

	protected void refreshLayout()
	{
		if (!bcContainer.isDisposed())
		{
			bcContainer.layout(true, true);
		}
		if (getWorkspaceItem() != null && !wContainer.isDisposed())
		{
			wContainer.layout(true, true);
		}
		if (!bcwContainer.isDisposed())
		{
			bcwContainer.layout(true, true);
		}

		// unselect all items, including
		// cleanup dropdown flags
		if (getItemCount() > 0)
		{
			BreadcrumbItem[] items = getItems();
			for (int i = 0; i < getItemCount(); i++)
			{
				items[i].setSelected(false);
				items[i].setDropdownOpen(false);
			}
		}
		if (showRoot && getWorkspaceItem() != null)
		{
			getWorkspaceItem().setSelected(false);
		}

	}

	List<IBreadcrumbChangeListener> listenerList = new ArrayList<IBreadcrumbChangeListener>();

	public void addInputChangedListener(IBreadcrumbChangeListener listener)
	{
		if (listener != null)
		{
			listenerList.add(listener);
		}
	}

	public void removeInputChangedListener(IBreadcrumbChangeListener listener)
	{
		if (listener != null)
		{
			listenerList.remove(listener);
		}
	}

	public void preSelectItem(BreadcrumbItem item)
	{
		setFireDropdownOnSelectionChanged(true);
		if (getItemCount() > 0)
		{
			BreadcrumbItem[] items = getItems();
			for (int i = 0; i < items.length; i++)
			{
				BreadcrumbItem bI = items[i];
				if (bI != item)
				{
					bI.setDropdownOpen(false);
					bI.setHasFocus(false);
					bI.setSelected(false);
				}
			}
			// BreadcrumbItem workspaceExpItem = getWorkspaceItem();
			if (workspaceItem != null && item != workspaceItem)
			{
				workspaceItem.setDropdownOpen(false);
				workspaceItem.setHasFocus(false);
				workspaceItem.setSelected(false);
			}
		}
		selectItem(item);
	}

}
