/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 May 10, 2010 11:44:51 AM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler;
import eu.cessar.ct.sdk.collections.MatrixBasedTreeStructure;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 * 
 */
public class InstanceRefTreeContentProvider extends MatrixBasedTreeStructure implements ITreeContentProvider
{
	private Map<GIdentifiable, List<List<GIdentifiable>>> map;
	/**
	 * this member is an array of EObjects, representing the root elements of the tree
	 */
	private List<EObject> roots = new ArrayList<EObject>();

	private IChooseIRefHandler handler;

	/**
	 * 
	 * @param map
	 *        K: target candidate <br>
	 *        V: list of lists of elements representing contexts
	 * @param handler
	 */
	public InstanceRefTreeContentProvider(Map<GIdentifiable, List<List<GIdentifiable>>> map, IChooseIRefHandler handler)
	{
		this.map = map;
		this.handler = handler;

		sortMap();
		add();
		constructMatrix();
	}

	/**
	 * 
	 */
	private void sortMap()
	{
		Set<GIdentifiable> keySet = map.keySet();
		for (GIdentifiable candidate: keySet)
		{
			List<List<GIdentifiable>> lists = map.get(candidate);

			Collections.sort(lists, new Comparator<List<GIdentifiable>>()
			{
				public int compare(List<GIdentifiable> o1, List<GIdentifiable> o2)
				{
					if (o1.size() == o2.size())
					{
						return 0;
					}
					if (o1.size() < o2.size())
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
			});
		}
	}

	/**
	 * Adds all elements from the map received on the constructor to the internal list
	 */
	private void add()
	{
		List<GIdentifiable> elements = new ArrayList<GIdentifiable>();

		Set<GIdentifiable> keySet = map.keySet();
		for (GIdentifiable candidate: keySet)
		{
			elements.add(candidate);
			List<List<GIdentifiable>> lists = map.get(candidate);

			for (List<GIdentifiable> list: lists)
			{
				if (list.size() == 0)
				{
					if (!roots.contains(candidate))
					{
						roots.add(candidate);
					}
				}

				int i = 0;
				for (GIdentifiable context: list)
				{
					if (i == 0)
					{
						if (!roots.contains(context))
						{
							roots.add(context);
						}
					}
					// avoid duplicates
					if (!elements.contains(context))
					{
						elements.add(context);
					}

					i++;
				}
			}
		}

		addAllObjects(elements);
	}

	@Override
	public Object[] getElements(Object inputElement)
	{
		if (handler.isSystemInstanceRef())
		{ // no candidates for complete/incomplete configuration->empty array
			if (!handler.hasCandidatesForCompleteConfig() && !handler.areIncompleteConfigsPermitted())
			{
				return new Object[0];
			}
		}

		Object[] result = null;

		// return all root elements for ECUC instance references or for System
		// instance ref if incomplete configuration is permitted
		if (handler.areIncompleteConfigsPermitted() || !handler.isSystemInstanceRef())
		{
			result = getECUCInstanceRefRootElements();
		}
		else
		{
			result = getSystemInstanceRefRootElements();
		}

		if (result == null)
		{
			result = roots.toArray();
		}

		return result;
	}

	private Object[] getECUCInstanceRefRootElements()
	{
		Object[] result;
		if (handler.getFilterString() != null)
		{
			List<EObject> elements = new ArrayList<EObject>();
			for (EObject root: roots)
			{
				if (lookupInModel((GIdentifiable) root, false))
				{
					elements.add(root);
				}
			}
			result = elements.toArray();
		}
		else
		{
			result = roots.toArray();
		}

		return result;
	}

	private Object[] getSystemInstanceRefRootElements()
	{
		Object[] result = null;

		List<IContextType> wrapperList = handler.getContextTypes();

		if (wrapperList.size() > 0)
		{
			List<EObject> elements = new ArrayList<EObject>();

			for (EObject root: roots)
			{
				IContextType wrapper = wrapperList.get(0);
				if (wrapper.getType().isInstance(root) && getChildren(root).length > 0)
				{
					elements.add(root);
				}
			}
			if (handler.getFilterString() != null)
			{
				List<EObject> elements1 = new ArrayList<EObject>();
				for (EObject root: elements)
				{
					if (lookupInWraperList((GIdentifiable) root, false))
					{
						elements1.add(root);
					}
				}
				result = elements1.toArray();
			}
			else
			{
				result = elements.toArray();
			}
		}

		return result;
	}

	private boolean lookupInWraperList(GIdentifiable root, boolean result)
	{
		boolean b = false;

		Object[] tmpList = getChildren(root);
		if (tmpList != null && tmpList.length > 0)
		{
			boolean continueLoop = true;

			for (int i = 0; i < tmpList.length && continueLoop; i++)
			{
				if (getChildren(tmpList[i]) != null && getChildren(tmpList[i]).length > 0)
				{
					if (!result)
					{
						b = lookupInWraperList((GIdentifiable) tmpList[i], result);
						continueLoop = !b;
					}
				}
				else
				{
					b = match(((GIdentifiable) tmpList[i]).gGetShortName(), handler.getFilterString());
					continueLoop = false;
				}
			}
		}
		return b;
	}

	private static boolean match(String t, String c)
	{
		if (t == null)
		{
			return false;
		}

		if (c == null)
		{
			return true;
		}

		String filter = StringUtils.escapeRegexForSearch(c);
		Pattern pattern = Pattern.compile(filter);
		pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);

		return pattern.matcher(t).lookingAt();
	}

	private boolean lookupInModel(GIdentifiable root, boolean result)
	{
		boolean found = false;
		Object[] tmpList = getChildren(root);
		if (tmpList != null && tmpList.length > 0)
		{
			for (int i = 0; i < tmpList.length && !found; i++)
			{
				if (getChildren(tmpList[i]) != null && getChildren(tmpList[i]).length > 0)
				{
					if (!result)
					{
						found = lookupInModel((GIdentifiable) tmpList[i], result);
					}
				}
				else
				{
					if (match(((GIdentifiable) tmpList[i]).gGetShortName(), handler.getFilterString()))
					{
						found = true;
					}
				}
			}
		}
		return found;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.collections.MatrixBasedTreeStructure#getChildren(java.lang.Object)
	 */
	// CHECKSTYLE:OFF
	@Override
	public Object[] getChildren(Object parentElement)
	{
		super.setFilteringText(handler.getFilterString());
		Object[] children = super.getChildren(parentElement);
		if (handler.areIncompleteConfigsPermitted() || !handler.isSystemInstanceRef())
		{
			return children;
		}

		List<Object> childrenList = new ArrayList<Object>();
		EClass nextType = null;
		IContextType parentWrapper = null;

		List<IContextType> wrapperList = handler.getContextTypes();
		for (int i = 0; i < wrapperList.size(); i++)
		{
			IContextType wrapper = wrapperList.get(i);

			if (wrapper.getType().isInstance(parentElement))
			{
				parentWrapper = wrapper;

				if (wrapperList.size() > i + 1)
				{
					nextType = wrapperList.get(i + 1).getType();
				}

				if (i == wrapperList.size() - 1)
				{
					return children;
				}
				break;
			}
		}

		if (parentWrapper == null || nextType == null)
		{
			return new Object[0];
		}

		for (Object obj: children)
		{
			if (parentWrapper.allowsInfiniteInstances() && parentWrapper.getType().isInstance(obj))
			{
				childrenList.add(obj);
			}
			else if (nextType.isInstance(obj))
			{
				childrenList.add(obj);
			}
		}

		return childrenList.toArray(new Object[childrenList.size()]);
	}

	// CHECKSTYLE:ON

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.MatrixBasedTreeStructure#constructMatrix()
	 */
	@Override
	public void constructMatrix()
	{
		Set<GIdentifiable> keySet = map.keySet();
		for (GIdentifiable candidate: keySet)
		{
			List<List<GIdentifiable>> lists = map.get(candidate);

			for (List<GIdentifiable> list: lists)
			{
				int i = 0;
				GIdentifiable prev = null;
				for (GIdentifiable current: list)
				{
					if (i > 0)
					{
						// set link between contexts
						setLinkToMatrix(prev, current, true);
					}
					prev = current;
					i++;
				}
				if (list.size() > 0)
				{
					// set link between context and target candidate
					setLinkToMatrix(list.get(list.size() - 1), candidate, true);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.MatrixBasedTreeStructure#isAcceptedByFilter(java.lang.Object)
	 */
	@Override
	public boolean isAcceptedByFilter(Object object)
	{
		if (hasChildren(object))
		{
			return lookupInWraperList((GIdentifiable) object, false);
		}
		else
		{
			return match(((GIdentifiable) object).gGetShortName(), handler.getFilterString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// TODO Auto-generated method stub
	}

}
