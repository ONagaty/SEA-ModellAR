/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu3379<br/>
 * 25.01.2013 18:21:01
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets.expandShelf;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import eu.cessar.ct.core.internal.platform.ui.Messages;

/**
 * Viewer class for the {@link CessarExpandShelf} control. Eases the use of the control, the creation and the updating
 * of its content items.
 * 
 * @author uidu3379
 * 
 *         %created_by: uidv3687 %
 * 
 *         %date_created: Wed Aug 14 16:03:14 2013 %
 * 
 *         %version: 9 %
 */
public class ShelfViewer extends StructuredViewer
{
	private PShelf expandShelf;

	private volatile boolean userAction;

	private Image backgroundImage;

	private Label noWorkFlowLabel;

	/**
	 * Safe runnable used to update an item.
	 */
	class UpdateItemSafeRunnable extends SafeRunnable
	{
		private Object element;

		private Item item;

		UpdateItemSafeRunnable(Item item, Object element)
		{
			this.item = item;
			this.element = element;
		}

		public void run()
		{
			doUpdateItem(item, element);
		}

	}

	/**
	 * Creates an instance of {@link ShelfViewer} using the provided parameters. Constructs the underlying
	 * {@link CessarExpandShelf} widget inside the parent Composite.
	 * 
	 * @param parentComposite
	 *        the parent of the {@link CessarExpandShelf}
	 * @param style
	 *        the style of the widget
	 */
	public ShelfViewer(Composite parentComposite, int style)
	{
		// default FillLayout does not fit with desired look
		GridData wrapData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parentComposite.setLayout(new GridLayout(1, false));
		parentComposite.setLayoutData(wrapData);

		expandShelf = new PShelf(parentComposite, style);
		expandShelf.setRenderer(new ExtendedShelfRenderer());
		expandShelf.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				fireSelectionChanged();
				userAction = false;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				fireSelectionChanged();
			}
		});

		expandShelf.addMouseListener(new MouseAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseDown(MouseEvent e)
			{
				userAction = true;
			}

		});
		setUseHashlookup(true);

		// add info message in case no workflow is selected. It will be removed when an relevant input will be selected
		noWorkFlowLabel = getNoWorkflowLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
	 */
	@Override
	protected Widget doFindInputItem(Object element)
	{
		if (equals(element, getRoot()))
		{
			return getControl();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	@Override
	protected Widget doFindItem(Object element)
	{
		Item[] children = doGetItems();
		for (int i = 0; i < children.length; i++)
		{
			Item item = children[i];
			Object data = item.getData();
			if (data != null && equals(data, element))
			{
				return item;
			}
		}

		return null;
	}

	/**
	 * @return
	 */
	private Item[] doGetItems()
	{
		return expandShelf.getItems();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(org.eclipse.swt.widgets.Widget, java.lang.Object,
	 * boolean)
	 */
	@Override
	protected void doUpdateItem(Widget widget, Object element, boolean fullMap)
	{

		// currently, only the items structure changes, no items are added ore removed

	}

	/**
	 * Copies the attributes of the given element into the given SWT item.
	 * 
	 * @param item
	 *        the SWT item
	 * @param element
	 *        the element
	 */
	protected void doUpdateItem(final Item item, Object element)
	{

		// currently, only the items structure changes, no items are added ore removed

		if (!item.getData().equals(element) && getLabelProvider() instanceof IShelfItemLabelProvider)
		{
			item.setText(((IShelfItemLabelProvider) getLabelProvider()).getItemHeaderText(element));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	@Override
	protected List getSelectionFromWidget()
	{
		return Arrays.asList(new Object[] {expandShelf.getSelection()});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	@Override
	protected void internalRefresh(Object element)
	{
		// element is the input of the ShelfViewer
		// TODO this will refresh all, add method to refresh only desired element
		expandShelf.redraw();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#reveal(java.lang.Object)
	 */
	@Override
	public void reveal(Object element)
	{
		Assert.isNotNull(element);
		Widget w = findItem(element);
		if (w instanceof PShelfItem)
		{
			doShowItem((PShelfItem) w);
		}

	}

	/**
	 * Draws the desired item.
	 * 
	 * @param item
	 *        the {@link CessarExpandItem} to draw
	 */
	private void doShowItem(@SuppressWarnings("unused") PShelfItem item)
	{
		// TODO show item
		expandShelf.redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List, boolean)
	 */
	@Override
	protected void setSelectionToWidget(List l, boolean reveal)
	{
		if (l != null && l.size() > 0 && l.get(0) instanceof PShelfItem)
		{
			expandShelf.setSelection((PShelfItem) l.get(0));
		}

		fireSelectionChanged();

	}

	/**
	 * Fires a {@link SelectionChangedEvent}.
	 */
	private void fireSelectionChanged()
	{
		SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
		fireSelectionChanged(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl()
	{
		return expandShelf;
	}

	/**
	 * Gets whether the currently being processed action is triggered by the user or not.
	 * 
	 * @return the <code>true</code> if the user triggered the action inside the shelf, <code>false</code> otherwise
	 */
	public boolean isUserAction()
	{
		return userAction;
	}

	/**
	 * Sets the <code>userAction</code> variable that specifies if the currently action that is processed is triggered
	 * by the user.
	 * 
	 * @param userTriggeredAction
	 *        if the action was triggered by a manual user input
	 */
	public void setUserAction(boolean userTriggeredAction)
	{
		userAction = userTriggeredAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput)
	{
		if (getContentProvider() instanceof IStructuredContentProvider)
		{
			disposeExpandItems();
			setPShelfLayout();
			constructExpandItems(((IStructuredContentProvider) getContentProvider()).getElements(input));
		}
	}

	/**
	 * When a relevant input is set, info message is not needed anymore and PShelf shall fill the entire area
	 * 
	 */
	private void setPShelfLayout()
	{
		// info message is not needed anymore
		if (noWorkFlowLabel != null)
		{
			noWorkFlowLabel.dispose();
			noWorkFlowLabel = null;

			expandShelf.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			expandShelf.getParent().layout();
		}
	}

	/**
	 * Constructs the content of the {@link CessarExpandShelf}: its items given a provided input has been set.
	 * 
	 * @param inputElements
	 *        the input of the widget
	 */
	private void constructExpandItems(Object[] inputElements)
	{
		for (Object itemData: inputElements)
		{
			PShelfItem expandItem = new PShelfItem(expandShelf, SWT.NONE);
			expandItem.setData(itemData);

			expandItem.getBody().setLayout(new FillLayout());

			if (getLabelProvider() instanceof IShelfItemLabelProvider)
			{
				IShelfItemLabelProvider labelProvider = (IShelfItemLabelProvider) getLabelProvider();
				labelProvider.constructItemBodyComposite(itemData, expandItem.getBody());
				expandItem.setText(labelProvider.getItemHeaderText(itemData));
				expandItem.setImage(backgroundImage);
				expandItem.setData(itemData);
			}

		}
	}

	private void disposeExpandItems()
	{
		if (expandShelf.isDisposed())
		{
			return;
		}

		for (PShelfItem item: expandShelf.getItems())
		{
			if (!item.isDisposed())
			{
				item.dispose();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh()
	 */
	@Override
	public void refresh()
	{
		if (getInput() != null)
		{
			super.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh(boolean)
	 */
	@Override
	public void refresh(boolean updateLabels)
	{
		if (getInput() != null)
		{
			super.refresh(updateLabels);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh(java.lang.Object)
	 */
	@Override
	public void refresh(Object element)
	{
		// element is the input of the ShelfViewer
		if (getInput() != null)
		{
			super.refresh(element);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh(java.lang.Object, boolean)
	 */
	@Override
	public void refresh(Object element, boolean updateLabels)
	{
		if (getInput() != null)
		{
			super.refresh(element, updateLabels);
		}
	}

	/**
	 * Updates the item with the provided data.
	 * 
	 * @param data
	 *        the model data of the item to update
	 */
	public void updateItem(Object data)
	{
		PShelfItem shelfItem = null;
		for (PShelfItem item: expandShelf.getItems())
		{
			if (item.getData() == data)
			{
				shelfItem = item;
				break;
			}
		}

		if (shelfItem != null)
		{
			((IShelfItemLabelProvider) getLabelProvider()).constructItemBodyComposite(data, shelfItem.getBody());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setLabelProvider(org.eclipse.jface.viewers.IBaseLabelProvider)
	 */
	@Override
	public void setLabelProvider(IBaseLabelProvider labelProvider)
	{
		if (expandShelf.getRenderer() instanceof ExtendedShelfRenderer
			&& labelProvider instanceof IShelfItemLabelProvider)
		{
			((ExtendedShelfRenderer) expandShelf.getRenderer()).setLabelProvider((IShelfItemLabelProvider) labelProvider);
		}
		super.setLabelProvider(labelProvider);
	}

	/**
	 * Expands the provided item and collapses the current item at the same time.
	 * 
	 * @param itemToExpand
	 *        the {@link CessarExpandItem} to expand/open
	 */
	public void expandItem(PShelfItem itemToExpand)
	{
		expandShelf.setSelection(itemToExpand);

		fireSelectionChanged();
	}

	/**
	 * Expands the item with the provided data and collapses the currently open item at the same time.
	 * 
	 * @param itemData
	 *        the data of the {@link CessarExpandItem} to expand
	 */
	public void expandItemWithData(Object itemData)
	{
		Widget widget = doFindItem(itemData);
		if (widget instanceof PShelfItem)
		{
			expandShelf.setSelection((PShelfItem) widget);
		}
	}

	/**
	 * Special image with a transparent background that is set as an initial placeholder for the implementation-specific
	 * icons added in this Shelf's items' headers.
	 * 
	 * @param image
	 *        the transparent placeholder image
	 */
	public void setItemHeaderBackgroundImage(Image image)
	{
		backgroundImage = image;
	}

	/**
	 * Restore the viewer content to the initial state. Dispose the PShelf items and display the info label
	 */
	public void restoreInitialContent()
	{
		disposeExpandItems();
		if (noWorkFlowLabel == null)
		{
			noWorkFlowLabel = getNoWorkflowLabel();
		}

		expandShelf.getParent().layout();

	}

	/**
	 * Create a label used to inform the user that no workflow is selected
	 * 
	 * @return new label
	 */
	private Label getNoWorkflowLabel()
	{
		Label label = new Label(getControl().getParent(), SWT.WRAP);
		label.setText(Messages.MESSAGE_NO_WORKFLOW);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return label;
	}

}
