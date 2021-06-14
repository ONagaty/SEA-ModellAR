/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 5, 2014 1:44:51 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.dialogs;

import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Filter for a tree viewer.
 *
 * @author uidj9791
 *
 *         %created_by: uidu4748 %
 *
 *         %date_created: Thu Jul  2 10:20:43 2015 %
 *
 *         %version: 1 %
 */
public class CessarTreeRegexViewerFilter extends AbstractRegexViewerFilter
{
	/**
	 * Check if a object is leaf (has no more children)
	 *
	 * @param viewer
	 * @param element
	 * @return
	 */
	private static boolean isLeaf(TreeViewer treeViewer, Object element)
	{
		ITreeContentProvider provider = (ITreeContentProvider) treeViewer.getContentProvider();
		return (provider.getChildren(element).length == 0);
	}

	/**
	 * Performs the search on a leaf.<br>
	 *
	 * @param viewer
	 *        the control on which the search is made
	 * @param leaf
	 *        the node that might be a leaf
	 * @return <code>true</code> if the given node is a leaf,<code>false</code> otherwise
	 */
	private boolean isMatchedLeaf(Object leaf)
	{
		String shortName = getLeafName(leaf);
		String search = getSearchString();

		if (search == null || search.length() == 0 || shortName == null || shortName.length() == 0)
		{
			return true;
		}

		return matchText(shortName, search);
	}

	/**
	 * Gets the name of leaf elements of type GIdentifiable.
	 *
	 * Clients of this method should overwrite for specific leaf elements.
	 *
	 * @param leaf
	 *        the node that might be a leaf
	 * @return leaf name or empty string
	 */
	protected String getLeafName(Object leaf)
	{
		if (leaf instanceof GIdentifiable)
		{
			return ((GIdentifiable) leaf).gGetShortName();
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element)
	{
		TreeViewer treeViewer = (TreeViewer) viewer;

		if (isLeaf(treeViewer, element))
		{
			return isMatchedLeaf(element);
		}
		else
		{
			ITreeContentProvider provider = (ITreeContentProvider) treeViewer.getContentProvider();
			for (Object child: provider.getChildren(element))
			{
				if (select(viewer, element, child))
				{
					return true;
				}
			}

			return false;
		}
	}
}
