/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 20.06.2012 11:19:08 </copyright>
 */
package eu.cessar.ct.workspace.ui.saveables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.ISaveablesLifecycleListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.SaveablesLifecycleEvent;
import org.eclipse.ui.internal.SaveablesList;

/**
 * {@link ISaveablesLifecycleListener} implementation to be used in
 * {@link IWorkbenchPart workbench parts}s which want the site-level
 * {@link Saveable saveable} management to be automatically notified about all
 * changes in {@link Saveable saveables} signaled to this
 * {@link ISaveablesLifecycleListener listener}.
 * 
 * @see Saveable
 * @see ISaveablesLifecycleListener
 */
public class SiteNotifyingSaveablesLifecycleListener implements ISaveablesLifecycleListener
{

	protected IWorkbenchPart workbenchPart;

	protected ISaveablesLifecycleListener siteSaveablesLifecycleListener;

	/**
	 * Constructor.
	 */
	public SiteNotifyingSaveablesLifecycleListener(IWorkbenchPart workbenchPart)
	{
		Assert.isNotNull(workbenchPart);

		this.workbenchPart = workbenchPart;
		siteSaveablesLifecycleListener = (ISaveablesLifecycleListener) workbenchPart.getSite().getService(
			ISaveablesLifecycleListener.class);
	}

	public void handleLifecycleEvent(SaveablesLifecycleEvent event)
	{
		/*
		 * !! Important Note !! Try to use internal API for filtering out irrelevant saveables. Otherwise warnings
		 * are raised at org.eclipse.ui.internal.SaveablesList#addModel(Object, Saveable) and
		 * org.eclipse.ui.internal.SaveablesList#removeModel(Object, Saveable) in case that the same saveable is
		 * signaled multiple times.
		 */
		List<Saveable> saveables = new ArrayList<Saveable>(Arrays.asList(event.getSaveables()));
		if (siteSaveablesLifecycleListener instanceof SaveablesList)
		{
			SaveablesList siteSaveablesList = (SaveablesList) siteSaveablesLifecycleListener;
			for (Iterator<Saveable> iter = saveables.iterator(); iter.hasNext();)
			{
				List<IWorkbenchPart> workbenchParts = Arrays.asList(siteSaveablesList.getPartsForSaveable(iter.next()));
				if (event.getEventType() == SaveablesLifecycleEvent.POST_OPEN
					&& workbenchParts.contains(workbenchPart))
				{
					iter.remove();
				}
				if (event.getEventType() == SaveablesLifecycleEvent.POST_CLOSE
					&& !workbenchParts.contains(workbenchPart))
				{
					iter.remove();
				}
			}
		}

		// Notify site-level saveable management about changed saveables, if
		// any
		if (!saveables.isEmpty())
		{
			Saveable[] saveablesArray = saveables.toArray(new Saveable[saveables.size()]);
			SaveablesLifecycleEvent newEvent = new SaveablesLifecycleEvent(workbenchPart,
				event.getEventType(), saveablesArray, event.isForce());
			siteSaveablesLifecycleListener.handleLifecycleEvent(newEvent);
		}
	}
}