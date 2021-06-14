/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 5, 2011 9:24:52 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.saveables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.model.IModelDescriptor;
import org.eclipse.sphinx.emf.model.IModelDescriptorChangeListener;
import org.eclipse.sphinx.emf.model.ModelDescriptorRegistry;
import org.eclipse.sphinx.emf.saving.SaveIndicatorUtil;
import org.eclipse.sphinx.emf.ui.util.EcoreUIUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.workspace.loading.ModelLoadManager;
import org.eclipse.sphinx.emf.workspace.saving.IModelSaveLifecycleListener;
import org.eclipse.sphinx.emf.workspace.saving.ModelSaveManager;
import org.eclipse.sphinx.emf.workspace.ui.saving.BasicModelSaveable;
import org.eclipse.sphinx.platform.ui.util.ExtendedPlatformUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISaveablesLifecycleListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.SaveablesLifecycleEvent;
import org.eclipse.ui.internal.SaveablesList;
import org.eclipse.ui.navigator.SaveablesProvider;

/**
 * 
 * Provider for {@link CessarSaveable}s.
 * 
 * @author uidl6870
 */
public class CessarSaveablesProvider extends SaveablesProvider
{

	/**
	 * Mapping between a {@link CessarSaveable} and a {@link Resource}
	 */
	private static Map<Resource, CessarSaveable> fileToSaveableMap = Collections.synchronizedMap(new HashMap<Resource, CessarSaveable>());

	private static IModelDescriptorChangeListener modelDescriptorChangeListener;

	private static IModelSaveLifecycleListener modelDirtyChangeListener;

	private static ResourceSetListener resourceRemovedListener;

	private static boolean initialized;// = false;

	private static List<CessarSaveablesProvider> providers = Collections.synchronizedList(new ArrayList<CessarSaveablesProvider>());

	private List<TransactionalEditingDomain> editingDomains = Collections.synchronizedList(new ArrayList<TransactionalEditingDomain>());

	/**
	 * Flag indicating if this provider has been disposed yet or not.
	 */
	private boolean disposed;// = false;

	/**
	 * 
	 */
	public CessarSaveablesProvider()
	{
		synchronized (CessarSaveablesProvider.class)
		{
			providers.add(this);
		}
	}

	/**
	 * Returns whether at least one saveable is dirty (among saveables managed by this provider).
	 * 
	 * @return <ul>
	 *         <li><tt><b>true</b>&nbsp;&nbsp;</tt> at least one saveable is dirty;</li>
	 *         <li><tt><b>false</b>&nbsp;</tt> otherwise.</li>
	 *         </ul>
	 */
	public boolean isDirty()
	{
		synchronized (fileToSaveableMap)
		{
			for (Saveable saveable: fileToSaveableMap.values())
			{
				if (saveable.isDirty())
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void doInit()
	{
		if (!initialized)
		{
			synchronized (CessarSaveablesProvider.class)
			{
				if (!initialized)
				{
					Collection<IModelDescriptor> allModels = ModelDescriptorRegistry.INSTANCE.getAllModels();

					for (IModelDescriptor iModelDescriptor: allModels)
					{
						// create saveables for resources which became dirty
						// before opening editor/viewer
						List<Resource> dirtyResources = getDirtyResources(iModelDescriptor);
						for (Resource resource: dirtyResources)
						{
							lookupSaveable(resource);

						}
					}

					modelDescriptorChangeListener = createModelDescriptorChangeListener();
					ModelDescriptorRegistry.INSTANCE.addModelDescriptorChangeListener(modelDescriptorChangeListener);

					modelDirtyChangeListener = createModelDirtyChangeListener();
					ModelSaveManager.INSTANCE.addModelDirtyChangedListener(modelDirtyChangeListener);

					resourceRemovedListener = createResourceRemovedListener();
					Collection<TransactionalEditingDomain> allEditingDomains = WorkspaceEditingDomainUtil.getAllEditingDomains();
					synchronized (editingDomains)
					{
						for (TransactionalEditingDomain transactionalEditingDomain: allEditingDomains)
						{
							transactionalEditingDomain.addResourceSetListener(resourceRemovedListener);
							editingDomains.add(transactionalEditingDomain);
						}
					}

					initialized = true;
				}
			}
		}

	}

	/**
	 * 
	 * @return
	 */
	private IModelDescriptorChangeListener createModelDescriptorChangeListener()
	{
		return new IModelDescriptorChangeListener()
		{

			public void handleModelAdded(IModelDescriptor modelDescriptor)
			{
				TransactionalEditingDomain editingDomain = modelDescriptor.getEditingDomain();
				synchronized (editingDomains)
				{
					if (!editingDomains.contains(editingDomain))
					{
						editingDomain.addResourceSetListener(resourceRemovedListener);
						editingDomains.add(editingDomain);
					}
				}
			}

			public void handleModelRemoved(IModelDescriptor modelDescriptor)
			{
				Saveable[] saveables = getSaveables();
				for (Saveable saveable: saveables)
				{
					Resource resource = ((CessarSaveable) saveable).getResource();
					boolean belongsToModel = modelDescriptor.belongsTo(resource, true);

					if (belongsToModel)
					{
						doRemoveSaveable(resource);
					}
				}
			}
		};
	}

	private ResourceSetListener createResourceRemovedListener()
	{
		return new ResourceSetListenerImpl(NotificationFilter.createFeatureFilter(
			EcorePackage.eINSTANCE.getEResourceSet(), ResourceSet.RESOURCE_SET__RESOURCES).and(
			NotificationFilter.createEventTypeFilter(Notification.REMOVE).or(
				NotificationFilter.createEventTypeFilter(Notification.REMOVE_MANY))))
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.
			 * ResourceSetChangeEvent)
			 */
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event)
			{
				Set<Resource> removedResources = new HashSet<Resource>();
				List<?> notifications = event.getNotifications();
				for (Object object: notifications)
				{
					if (object instanceof Notification)
					{
						Notification notification = (Notification) object;
						if (notification.getOldValue() instanceof Resource)
						{
							removedResources.add((Resource) notification.getOldValue());
						}
						if (notification.getOldValue() instanceof List<?>)
						{
							List<Resource> resources = (List<Resource>) notification.getOldValue();
							removedResources.addAll(resources);
						}
					}
				}

				for (Resource resource: removedResources)
				{
					doRemoveSaveable(resource);
				}
			}

		};
	}

	/**
	 * @return
	 */
	private IModelSaveLifecycleListener createModelDirtyChangeListener()
	{
		return new IModelSaveLifecycleListener()
		{

			public void handleDirtyChangedEvent(IModelDescriptor modelDescriptor)
			{
				List<Resource> dirtyResources = getDirtyResources(modelDescriptor);
				// no dirty resources -> Save performed previously
				if (dirtyResources.size() == 0)
				{
					dispatch(null, SaveablesLifecycleEvent.DIRTY_CHANGED);
				}
				else
				{
					// a modification has been made
					for (Resource dirtyResource: dirtyResources)
					{
						Saveable saveable = getSaveable(dirtyResource);

						if (saveable != null)
						{
							dispatch(saveable, SaveablesLifecycleEvent.DIRTY_CHANGED);
						}
					}
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.sphinx.emf.workspace.saving.IModelSaveLifecycleListener#handlePreSaveEvent(org.eclipse.sphinx
			 * .emf.model.IModelDescriptor)
			 */
			@Override
			public void handlePreSaveEvent(IModelDescriptor modelDescriptor)
			{
				// nothing to do
			}
		};
	}

	/**
	 * Fires the suitable notification corresponding to the <code>saveablesLifecycleEventType</code> and
	 * <code>saveable</code> for each Saveables provider
	 * 
	 * @param saveable
	 * @param event
	 */
	private static void dispatch(Saveable saveable, int saveablesLifecycleEventType)
	{
		synchronized (providers)
		{
			for (CessarSaveablesProvider provider: providers)
			{
				provider.fireSaveablesLifecycleEventUIThread(saveable, saveablesLifecycleEventType, true);
			}
		}
	}

	/**
	 * Return weather the given <code>resource</code> is dirty
	 * 
	 * @param resource
	 * @return
	 */
	public boolean isDirty(Resource resource)
	{
		Saveable saveable = getSaveable(resource);
		return saveable != null && saveable.isDirty();
	}

	private SaveablesList getSaveableService()
	{
		SaveablesList l = (SaveablesList) PlatformUI.getWorkbench().getService(ISaveablesLifecycleListener.class);
		return l;
	}

	/**
	 * Returns whether the resource from the <code>file</code> is open inside other editor part
	 * 
	 * @param file
	 * @return
	 */
	private boolean stillOpenElsewhere(IFile file)
	{
		Saveable[] openModels = getSaveableService().getOpenModels();
		for (Saveable saveable: openModels)
		{
			// temporary solution until we will have the same unit of
			// saveability in all the ISaveablesSources (tree editor, table
			// view, etc)
			if (saveable instanceof BasicModelSaveable)
			{
				IWorkbenchPart[] partsForSaveable = getSaveableService().getPartsForSaveable(saveable);

				for (IWorkbenchPart iWorkbenchPart: partsForSaveable)
				{
					if (iWorkbenchPart instanceof IEditorPart)
					{
						IEditorInput editorInput = ((IEditorPart) iWorkbenchPart).getEditorInput();
						IFile fileFromEditorInput = EcoreUIUtil.getFileFromEditorInput(editorInput);
						if (file.getFullPath().equals(fileFromEditorInput.getFullPath()))
						{
							return true;
						}
					}
				}

			}
		}

		return false;
	}

	@Override
	public void dispose()
	{
		super.dispose();

		synchronized (CessarSaveablesProvider.class)
		{
			providers.remove(this);
			if (providers.size() == 0)
			{
				initialized = false;

				ModelSaveManager.INSTANCE.removeModelDirtyChangedListener(modelDirtyChangeListener);

				Saveable[] saveables = getSaveables();
				for (Saveable saveable: saveables)
				{
					CessarSaveable cessarSaveable = (CessarSaveable) saveable;

					if (cessarSaveable.isDirty())
					{
						boolean stillOpenElsewhere = stillOpenElsewhere(cessarSaveable.getIFile());
						if (!stillOpenElsewhere)
						{
							ModelLoadManager.INSTANCE.reloadFile(cessarSaveable.getIFile(), true,
								new NullProgressMonitor());
						}
					}
				}

				synchronized (fileToSaveableMap)
				{
					fileToSaveableMap.clear();
				}

				ModelDescriptorRegistry.INSTANCE.removeModelDescriptorChangeListener(modelDescriptorChangeListener);

				synchronized (editingDomains)
				{
					for (TransactionalEditingDomain editingDomain: editingDomains)
					{
						editingDomain.removeResourceSetListener(resourceRemovedListener);
					}
					editingDomains.clear();
				}
			}
		}

		disposed = true;
	}

	@Override
	public Object[] getElements(Saveable saveable)
	{
		if (saveable instanceof CessarSaveable)
		{
			Resource resource = ((CessarSaveable) saveable).getResource();
			return new Object[] {resource};
		}
		return new Object[0];
	}

	protected CessarSaveable createModelSaveable(Resource resource)
	{
		return new CessarSaveable(resource);
	}

	/*
	 * @see org.eclipse.ui.navigator.SaveablesProvider#getSaveable(java.lang.Object)
	 */
	@Override
	public Saveable getSaveable(Object element)
	{
		if (element instanceof Resource)
		{
			return lookupSaveable((Resource) element);
		}
		else if (element instanceof IFile)
		{
			Resource resource = EcorePlatformUtil.getResource(element);
			return lookupSaveable(resource);
		}
		return null;
	}

	/**
	 * @param saveable
	 * @param monitor
	 * @throws CoreException
	 */
	public void performSave(Saveable saveable, IProgressMonitor monitor) throws CoreException
	{
		saveable.doSave(monitor);

		dispatch(saveable, SaveablesLifecycleEvent.DIRTY_CHANGED);
	}

	/**
	 * 
	 * @param iFile
	 * @return the {@link Saveable} for the given <code>resource</code>
	 */
	protected Saveable lookupSaveable(Resource resource)
	{
		if (resource != null)
		{
			synchronized (fileToSaveableMap)
			{
				CessarSaveable saveable = fileToSaveableMap.get(resource);
				if (saveable == null)
				{
					saveable = createModelSaveable(resource);
					fileToSaveableMap.put(resource, saveable);

					dispatch(saveable, SaveablesLifecycleEvent.POST_OPEN);
				}
				return saveable;
			}
		}
		return null;
	}

	/*
	 * @see org.eclipse.ui.navigator.SaveablesProvider#getSaveables()
	 */
	@Override
	public Saveable[] getSaveables()
	{
		return fileToSaveableMap.values().toArray(new Saveable[fileToSaveableMap.values().size()]);
	}

	/**
	 * Returns the {@link CessarSaveable} saveables from the given <code>project</code>
	 * 
	 * @param project
	 * @return
	 */
	public List<Saveable> getSaveables(IProject project)
	{
		List<Saveable> res = new ArrayList<Saveable>();

		Saveable[] saveables = getSaveables();
		for (Saveable saveable: saveables)
		{
			IFile file = ((CessarSaveable) saveable).getIFile();
			if (file.getProject() == project)
			{
				res.add(saveable);
			}
		}

		return res;
	}

	public List<IProject> getDirtyProjects()
	{

		List<IProject> dirtyProjects = new ArrayList<IProject>();

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		for (IProject project: projects)
		{
			if (!project.isAccessible())
			{
				continue;
			}

			List<Saveable> dirtySaveables = getDirtySaveables(project);
			if (dirtySaveables.size() > 0)
			{
				dirtyProjects.add(project);
			}

		}

		return dirtyProjects;
	}

	/**
	 * Returns the dirty {@link CessarSaveable} saveables from the given <code>project</code>
	 * 
	 * @param project
	 * @return
	 */
	public List<Saveable> getDirtySaveables(IProject project)
	{
		List<Saveable> dirtySaveables = new ArrayList<Saveable>();

		List<Saveable> saveables = getSaveables(project);
		for (Saveable saveable: saveables)
		{
			if (saveable.isDirty())
			{
				dirtySaveables.add(saveable);
			}
		}

		return dirtySaveables;
	}

	/**
	 * Fires the suitable notification corresponding to the SaveablesLifecycleEventType.
	 * <p>
	 * Notification is performed inside UI thread.
	 * 
	 * @param saveable
	 *        The saveable created inside this provider.
	 */
	private void fireSaveablesLifecycleEventUIThread(final Saveable saveable, final int saveablesLifecycleEventType,
		boolean async)
	{
		if (disposed)
		{
			return;
		}
		Display display = ExtendedPlatformUI.getDisplay();
		if (display != null)
		{
			if (async)
			{
				display.asyncExec(new Runnable()
				{
					public void run()
					{
						doRun(saveable, saveablesLifecycleEventType);
					}
				});
			}
			else
			{
				display.syncExec(new Runnable()
				{
					public void run()
					{
						doRun(saveable, saveablesLifecycleEventType);
					}
				});
			}
		}
	}

	protected void doRun(final Saveable saveable, final int saveablesLifecycleEventType)
	{
		if (disposed)
		{
			return;
		}
		switch (saveablesLifecycleEventType)
		{
			case SaveablesLifecycleEvent.DIRTY_CHANGED:
				fireSaveablesDirtyChanged(new Saveable[] {saveable});
				break;
			case SaveablesLifecycleEvent.POST_OPEN:
				fireSaveablesOpened(new Saveable[] {saveable});
				break;
			case SaveablesLifecycleEvent.POST_CLOSE:
				fireSaveablesClosed(new Saveable[] {saveable});
				break;
			case SaveablesLifecycleEvent.PRE_CLOSE:
				fireSaveablesClosing(new Saveable[] {saveable}, true);
				break;
			default:
				break;
		}
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	private List<Resource> getDirtyResources(IModelDescriptor model)
	{
		List<Resource> dirtyResources = new ArrayList<Resource>();

		ResourceSet resourceSet = model.getEditingDomain().getResourceSet();
		EList<Resource> resources = resourceSet.getResources();
		for (Resource resource2: resources)
		{
			if (SaveIndicatorUtil.isDirty(model.getEditingDomain(), resource2))
			{
				boolean belongsToModel = model.belongsTo(resource2, true);
				if (belongsToModel)
				{
					dirtyResources.add(resource2);
				}
			}
		}
		return dirtyResources;
	}

	/**
	 * Removes {@link CessarSaveable} saveable associated with the given <code>resource</code> from the internal mapping
	 * and fires appropriate notifications
	 * 
	 * @param resource
	 */
	private void doRemoveSaveable(Resource resource)
	{
		synchronized (fileToSaveableMap)
		{
			CessarSaveable saveable = fileToSaveableMap.get(resource);

			if (saveable != null)
			{
				synchronized (providers)
				{
					for (CessarSaveablesProvider provider: providers)
					{
						/*
						 * !! Important Note !! As we are in a synchronized block here and have no control about which
						 * saveables lifecycle event listeners are around and what they do we must not fire PRE_CLOSE
						 * event synchronously. Otherwise we'd introduce a potential risk of deadlocks.
						 */
						provider.fireSaveablesLifecycleEventUIThread(saveable, SaveablesLifecycleEvent.PRE_CLOSE, true);

						if (fileToSaveableMap.containsKey(resource))
						{
							fileToSaveableMap.remove(resource);
						}
						provider.fireSaveablesLifecycleEventUIThread(saveable, SaveablesLifecycleEvent.POST_CLOSE, true);
					}
				}
			}
		}

	}

}
