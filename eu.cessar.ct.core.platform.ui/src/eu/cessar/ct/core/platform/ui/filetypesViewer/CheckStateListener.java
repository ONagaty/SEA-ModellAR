/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 23.07.2012 16:34:43 </copyright>
 */
package eu.cessar.ct.core.platform.ui.filetypesViewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.custom.BusyIndicator;

/**
 * @author uidl6870
 * 
 */
public class CheckStateListener implements ICheckStateListener
{
	private Object fCurrentTreeSelection;

	private List<ICheckStateListener> fListeners = new ArrayList<ICheckStateListener>();
	private CheckboxTreeViewer checkboxTreeViewer;

	private List<Object> fWhiteCheckedTreeItems = new ArrayList<Object>();

	private List fCheckedStateStore = new ArrayList();

	private Object lastChecked;

	public CheckStateListener(CheckboxTreeViewer viewer)
	{
		this.checkboxTreeViewer = viewer;

		fListeners.add(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse.jface.viewers.CheckStateChangedEvent)
	 */
	public void checkStateChanged(final CheckStateChangedEvent event)
	{
		lastChecked = event.getSource();
		// Potentially long operation - show a busy cursor
		BusyIndicator.showWhile(checkboxTreeViewer.getControl().getDisplay(), new Runnable()
		{
			public void run()
			{
				if (event.getCheckable().equals(checkboxTreeViewer))
				{
					treeItemChecked(event.getElement(), event.getChecked());
				}

				// notifyCheckStateChangeListeners(event);
			}
		});
	}

	/**
	 * Callback that's invoked when the checked status of an item in the tree is
	 * changed by the user.
	 * 
	 * @param treeElement
	 * @param state
	 */
	protected void treeItemChecked(Object treeElement, boolean state)
	{

		// recursively adjust all child tree elements appropriately

		List<Object> l = new ArrayList<Object>();
		setTreeChecked(treeElement, state, l);
		System.out.println(l.size());

		Object parent = ((ITreeContentProvider) checkboxTreeViewer.getContentProvider()).getParent(treeElement);
		if (parent == null)
		{
			return;
		}

		// now update upwards in the tree hierarchy
		if (state)
		{
			List<Object> l1 = new ArrayList<Object>();
			grayCheckHierarchy(parent, l1);
		}
		else
		{
			ungrayCheckHierarchy(parent);
		}

		updateHierarchy(treeElement);
	}

	/**
	 * Sets the checked state of self and all ancestors appropriately
	 * 
	 * @param treeElement
	 */
	protected void updateHierarchy(Object treeElement)
	{
		boolean hasChildren = getContentProvider().hasChildren(treeElement);
		if (hasChildren)
		{
			boolean whiteChecked = determineShouldBeWhiteChecked(treeElement);
			boolean shouldBeAtLeastGray = determineShouldBeAtLeastGrayChecked(treeElement);

			checkboxTreeViewer.setChecked(treeElement, whiteChecked || shouldBeAtLeastGray);
			setWhiteChecked(treeElement, whiteChecked);
			if (whiteChecked)
			{
				checkboxTreeViewer.setGrayed(treeElement, false);
			}
			else
			{
				checkboxTreeViewer.setGrayed(treeElement, shouldBeAtLeastGray);
			}
		}
		// proceed up the tree element hierarchy
		Object parent = getContentProvider().getParent(treeElement);
		if (parent != null)
		{
			updateHierarchy(parent);
		}
	}

	/**
	 * Logically un-gray-check all ancestors of treeItem iff appropriate.
	 * 
	 * @param treeElement
	 */
	protected void ungrayCheckHierarchy(Object treeElement)
	{
		if (!determineShouldBeAtLeastGrayChecked(treeElement))
		{
			fCheckedStateStore.remove(treeElement);
		}

		Object parent = getContentProvider().getParent(treeElement);
		if (parent != null)
		{
			ungrayCheckHierarchy(parent);
		}
	}

	/**
	 * Returns a boolean indicating whether the passed tree element should be at
	 * LEAST gray-checked. Note that this method does not consider whether it
	 * should be white-checked, so a specified tree item which should be
	 * white-checked will result in a <code>true</code> answer from this method.
	 * To determine whether a tree item should be white-checked use method
	 * #determineShouldBeWhiteChecked(Object).
	 * 
	 * @param treeElement
	 *        java.lang.Object
	 * @return boolean
	 * @see #determineShouldBeWhiteChecked(java.lang.Object)
	 */
	protected boolean determineShouldBeAtLeastGrayChecked(Object treeElement)
	{

		// if any children of treeElement are still gray-checked then
		// treeElement
		// must remain gray-checked as well
		Object[] children = getTreeChildren(treeElement);
		for (int i = 0; i < children.length; ++i)
		{
			if (fCheckedStateStore.contains(children[i]))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Logically gray-check all ancestors of treeItem by ensuring that they
	 * appear in the checked table
	 * 
	 * @param treeElement
	 * @param l1
	 */
	protected void grayCheckHierarchy(Object treeElement, List<Object> l1)
	{

		if (l1.contains(treeElement))
		{
			System.out.println("a");
		}
		l1.add(treeElement);

		// if this tree element is already gray then its ancestors all are as
		// well
		if (fCheckedStateStore.contains(treeElement))
		{
			return; // no need to proceed upwards from here
		}

		fCheckedStateStore.add(treeElement);
		if (determineShouldBeWhiteChecked(treeElement))
		{
			setWhiteChecked(treeElement, true);
		}
		Object parent = getContentProvider().getParent(treeElement);
		if (parent != null)
		{
			grayCheckHierarchy(parent, l1);
		}
	}

	private ITreeContentProvider getContentProvider()
	{
		return ((ITreeContentProvider) checkboxTreeViewer.getContentProvider());
	}

	/**
	 * Adjusts the collection of references to white-checked tree elements
	 * appropriately.
	 * 
	 * @param treeElement
	 *        java.lang.Object
	 * @param isWhiteChecked
	 *        boolean
	 */
	protected void setWhiteChecked(Object treeElement, boolean isWhiteChecked)
	{
		if (isWhiteChecked)
		{
			if (!fWhiteCheckedTreeItems.contains(treeElement))
			{
				fWhiteCheckedTreeItems.add(treeElement);
			}
		}
		else
		{
			fWhiteCheckedTreeItems.remove(treeElement);
		}
	}

	/**
	 * Returns a boolean indicating whether the passed tree item should be
	 * white-checked.
	 * 
	 * @return boolean
	 * @param treeElement
	 *        java.lang.Object
	 */
	protected boolean determineShouldBeWhiteChecked(Object treeElement)
	{
		return areAllChildrenWhiteChecked(treeElement);
	}

	/**
	 * Returns a boolean indicating whether all children of the passed tree
	 * element are currently white-checked
	 * 
	 * @return boolean
	 * @param treeElement
	 *        java.lang.Object
	 */
	protected boolean areAllChildrenWhiteChecked(Object treeElement)
	{
		Object[] children = getTreeChildren(treeElement);

		for (int i = 0; i < children.length; ++i)
		{
			if (!fWhiteCheckedTreeItems.contains(children[i]))
			{
				return false;
			}
		}

		return true;
	}

	private Object[] getTreeChildren(Object element)
	{
		return getContentProvider().getChildren(element);
	}

	/**
	 * Returns the result of running the given elements through the filters.
	 * 
	 * @param filters
	 * 
	 * @param elements
	 *        the elements to filter
	 * @return only the elements which all filters accept
	 */
	protected Object[] filter(ViewerFilter[] filters, Object[] elements)
	{
		if (filters != null)
		{
			ArrayList<Object> filtered = new ArrayList<Object>(elements.length);
			for (int i = 0; i < elements.length; i++)
			{
				boolean add = true;
				for (int j = 0; j < filters.length; j++)
				{
					add = filters[j].select(null, null, elements[i]);
					if (!add)
					{
						break;
					}
				}
				if (add)
				{
					filtered.add(elements[i]);
				}
			}
			return filtered.toArray();
		}
		return elements;
	}

	/**
	 * Sets the checked state of the passed tree element appropriately, and do
	 * so recursively to all of its child tree elements as well
	 * 
	 * @param treeElement
	 * @param state
	 * @param l
	 */
	protected void setTreeChecked(Object treeElement, boolean state, List<Object> l)
	{
		if (l.contains(treeElement))
		{
			System.out.println("already in list!" + treeElement);
			return;
		}
		l.add(treeElement);
		if (state)
		{
			fCheckedStateStore.add(treeElement);
		}
		else
		{
			fCheckedStateStore.remove(treeElement);
		}

		setWhiteChecked(treeElement, state);
		checkboxTreeViewer.setChecked(treeElement, state);
		checkboxTreeViewer.setGrayed(treeElement, false);

		// now logically check/uncheck all children as well
		Object[] children = getTreeChildren(treeElement);
		for (int i = 0; i < children.length; ++i)
		{
			setTreeChecked(children[i], state, l);
		}
	}

	/**
	 * Notifies all checked state listeners that the passed element has had its
	 * checked state changed to the passed state
	 * 
	 * @param event
	 */
	protected void notifyCheckStateChangeListeners(CheckStateChangedEvent event)
	{
		Iterator<ICheckStateListener> listenersEnum = fListeners.iterator();
		while (listenersEnum.hasNext())
		{
			listenersEnum.next().checkStateChanged(event);
		}
	}
}
