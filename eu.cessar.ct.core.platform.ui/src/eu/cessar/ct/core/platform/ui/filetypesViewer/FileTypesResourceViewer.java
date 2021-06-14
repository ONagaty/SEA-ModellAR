/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Oct 31, 2011 10:57:45 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.filetypesViewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISaveablesLifecycleListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.SaveablesLifecycleEvent;
import org.eclipse.ui.internal.SaveablesList;

import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;

/**
 *
 * Viewer specialized in showing a subset of {@link IResource}s from a given {@link IContainer} , i.e. {@link IFile}s
 * having particular content type(s) and the {@link IContainer}s having such descendants
 *
 * @author uidl6870
 */
public class FileTypesResourceViewer
{
	private TreeViewer viewer;
	private boolean checkBoxBehavior;
	private boolean dirtyFeedback;

	private FileTypesContentProvider contentProvider;

	private ILabelProviderListener labelProviderListener;

	private ISaveablesLifecycleListener saveablesLifecycleListener;
	private List<IFile> checkedFiles = new ArrayList<IFile>();
	private List<String> allowedExtensions = new ArrayList<String>();

	/**
	 * @param allowedExtensions
	 *        list of allowed extensions
	 * @param dirtyFeedback
	 *        whether the viewer should display dirty status next to the name of the resource. <br>
	 *        NOTE: Projects/ folders will also have dirty status if they contain at least 1 dirty file
	 */
	public FileTypesResourceViewer(List<String> allowedExtensions, boolean dirtyFeedback)
	{
		this.allowedExtensions = allowedExtensions;
		this.dirtyFeedback = dirtyFeedback;
	}

	/**
	 * @param contentTypes
	 *        list of allowed {@link IContentType}s
	 * @param dirtyFeedback
	 *        whether the viewer should display dirty status next to the name of the resource. <br>
	 *        NOTE: Projects/ folders will also have dirty status if they contain at least 1 dirty file
	 */
	public FileTypesResourceViewer(boolean dirtyFeedback, List<IContentType> contentTypes)
	{

		allowedExtensions = PlatformUIUtils.getFileExtensionsForContentTypes(contentTypes);

		this.dirtyFeedback = dirtyFeedback;
	}

	/**
	 * Sets whether the viewer shall have a check box behavior. Default: false <br>
	 * Note: The method must be invoked before calling {@link FileTypesResourceViewer#createContents(Composite, Object)}
	 *
	 * @param checkBehavior
	 */
	public void setCheckBoxBehavior(boolean checkBehavior)
	{
		checkBoxBehavior = checkBehavior;
	}

	/**
	 * Note: The method must be invoked before calling {@link FileTypesResourceViewer#createContents(Composite, Object)}
	 *
	 * @param selectedFiles
	 */
	public void setInitialCheckedFiles(List<IFile> selectedFiles)
	{
		checkedFiles.addAll(selectedFiles);
	}

	private void refresh()
	{
		if (!viewer.getTree().isDisposed())
		{

			viewer.refresh();
		}
	}

	/**
	 * Creates the viewer inside the given <code>parentComposite</code> composite.
	 *
	 * @param parentComposite
	 *        the parent composite
	 * @param input
	 *        the input for the viewer. Should be one of the following: <li>the {@link IWorkspaceRoot}</li> <li>List of
	 *        desired {@link IProject}s</li> <li>
	 *        an {@link IContainer}</li>
	 */
	@SuppressWarnings("restriction")
	public void createContents(Composite parentComposite, Object input)
	{
		viewer = createViewer(parentComposite);

		Tree tree = viewer.getTree();
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = 200;
		tree.setLayoutData(gridData);

		contentProvider = new FileTypesContentProvider(allowedExtensions);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new FileTypesLabelProvider(dirtyFeedback));

		registerLabelProviderListener();

		TreeColumn tc = new TreeColumn(tree, SWT.H_SCROLL);
		tc.setWidth(800);

		tree.pack();
		viewer.setInput(input);

		viewer.getTree().addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				// remove listener on dispose
				PlatformUI.getWorkbench().getDecoratorManager().removeListener(labelProviderListener);

				getService().removeModelLifecycleListener(saveablesLifecycleListener);
			}
		});

		saveablesLifecycleListener = new ISaveablesLifecycleListener()
		{

			public void handleLifecycleEvent(SaveablesLifecycleEvent event)
			{
				if (event.getEventType() == SaveablesLifecycleEvent.DIRTY_CHANGED)
				{

					refresh();
				}
			}
		};

		SaveablesList saveablesService = getService();
		saveablesService.addModelLifecycleListener(saveablesLifecycleListener);
	}

	/**
	 * @param parentComposite
	 */
	private TreeViewer createViewer(Composite parentComposite)
	{
		TreeViewer viewer;
		if (checkBoxBehavior)
		{
			viewer = createCheckBoxTreeViewer(parentComposite);
		}
		else
		{
			viewer = new TreeViewer(parentComposite, SWT.BORDER | SWT.H_SCROLL);
		}

		return viewer;
	}

	/**
	 * @return
	 */
	private TreeViewer createCheckBoxTreeViewer(Composite parentComposite)
	{
		CheckboxTreeViewer viewer = new CheckboxTreeViewer(parentComposite, SWT.H_SCROLL);
		// viewer.getTree().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));

		viewer.addCheckStateListener(new CheckStateListener(viewer));

		viewer.setCheckStateProvider(new CheckStateProvider(viewer, getCheckedFiles()));

		return viewer;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("restriction")
	private SaveablesList getService()
	{
		SaveablesList l = (SaveablesList) PlatformUI.getWorkbench().getService(ISaveablesLifecycleListener.class);
		return l;
	}

	private void registerLabelProviderListener()
	{
		labelProviderListener = new ILabelProviderListener()
		{
			public void labelProviderChanged(LabelProviderChangedEvent event)
			{
				// need to re-request labels, so refresh viewer
				Object[] elements = event.getElements();
				for (Object object: elements)
				{
					viewer.update(object, null);
				}

				// refresh();

			}
		};
		PlatformUI.getWorkbench().getDecoratorManager().addListener(labelProviderListener);

	}

	/**
	 * Obtain the tree viewer
	 *
	 * @return the viewer
	 */
	public TreeViewer getViewer()
	{
		return viewer;
	}

	/**
	 * @return the content provider associated with the viewer
	 */
	public FileTypesContentProvider getContentProvider()
	{
		return contentProvider;
	}

	/**
	 * @return the checkedFiles
	 */
	private List<IFile> getCheckedFiles()
	{
		return checkedFiles;
	}

}
