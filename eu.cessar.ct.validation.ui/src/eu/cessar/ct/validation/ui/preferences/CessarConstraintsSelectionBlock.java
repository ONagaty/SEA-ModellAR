/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 3, 2014 2:59:29 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.validation.preferences.EMFModelValidationPreferences;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.platform.ui.dialogs.ViewerFiltering;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.validation.ui.internal.CessarPluginActivator;

/**
 * The selection block of the constraints. It contains the constraints tree, the description area and the filtering
 * area.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Thu Feb 12 16:23:53 2015 %
 *
 *         %version: 12 %
 */
public class CessarConstraintsSelectionBlock
{
	private CheckboxTreeViewer constraintsTree;
	private StyledText descriptionArea;
	private ViewerFiltering viewerFiltering;

	private SelectionListener selectionListener;

	private ICessarTreeNode rootNode;

	private final IConstraintFilter filter;
	private List<SelectionAdapter> listeners = new ArrayList<>();
	static
	{
		ModelValidationService.getInstance().loadXmlConstraintDeclarations();
	}

	/**
	 * Initializes without a constraint filter
	 */
	public CessarConstraintsSelectionBlock()
	{
		filter = IConstraintFilter.IDENTITY_INSTANCE;
	}

	/**
	 * Initializes with a constraint filter
	 *
	 * @param filter
	 */
	public CessarConstraintsSelectionBlock(IConstraintFilter filter)
	{
		if (filter == null)
		{
			throw new IllegalArgumentException("null filter"); //$NON-NLS-1$
		}

		this.filter = filter;
	}

	/**
	 * Content provider for the constraints tree
	 */

	private class TreeContentProvider implements ITreeContentProvider
	{

		@Override
		public Object[] getElements(Object inputElement)
		{
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement)
		{
			return ((ICessarTreeNode) parentElement).getChildren();
		}

		@Override
		public Object getParent(Object element)
		{
			return ((ICessarTreeNode) element).getParent();
		}

		@Override
		public boolean hasChildren(Object element)
		{
			return ((ICessarTreeNode) element).hasChildren();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{
			// nothing to do
		}

		@Override
		public void dispose()
		{
			// nothing to do
		}

	}

	/**
	 * Listener that mediates between the tree actions and the description area.
	 */

	private class SelectionListener implements ISelectionChangedListener, ICheckStateListener
	{

		/*
		 * Implements method of interface ICheckStateListener.
		 */
		@Override
		public void checkStateChanged(CheckStateChangedEvent event)
		{
			ICessarTreeNode treeNode = (ICessarTreeNode) event.getElement();
			treeNode.checkStateChanged(event);
		}

		/*
		 * Implements method of interface ISelectionChangedListener.
		 */
		@Override
		public void selectionChanged(SelectionChangedEvent event)
		{
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			if (!selection.isEmpty())
			{
				setDescription((ICessarTreeNode) selection.getFirstElement());
			}
			else
			{
				clearDescriptionArea();
			}
		}

		/**
		 * Sets the description area for the selected <code>treeNode</code>
		 *
		 * @param treeNode
		 */
		private void setDescription(ICessarTreeNode treeNode)
		{

			List<StyleRange> styles = new ArrayList<>(32);

			String description = CessarDescriptionUtils.formatStyle(treeNode.getFormatedDescription(), styles);

			getDescriptionArea().setText(description);
			getDescriptionArea().setStyleRanges(styles.toArray(new StyleRange[styles.size()]));
		}

		/**
		 * Clears the description area.
		 */
		protected void clearDescriptionArea()
		{
			getDescriptionArea().setText(CessarValidationUIMessages.preferences_no_selection);
		}
	}

	/**
	 * Creates the constraints selection composite and the filtering area.
	 *
	 * @param parent
	 *        of the new composite
	 * @return the <code>parent</code> with the new structure.
	 */

	public Composite createComposite(Composite parent)
	{
		viewerFiltering = new ViewerFiltering(new ValidationCessarViewerFilter());
		viewerFiltering.createFilterArea(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(layoutData);

		Control createConstraintsTree = createConstraintsTree(composite);
		createConstraintsTree.setLayoutData(layoutData);

		Control createDescriptionArea = createDescriptionArea(composite);
		GridData layoutData2 = new GridData(SWT.FILL, SWT.NONE, true, false);
		layoutData2.heightHint = 100;
		createDescriptionArea.setLayoutData(layoutData2);

		viewerFiltering.setInput(constraintsTree);

		return parent;
	}

	/**
	 * Creates the constraints tree.
	 *
	 * @param parent
	 * @return the constraints tree
	 */

	private Control createConstraintsTree(Composite parent)
	{
		createButtons(parent);

		constraintsTree = new CheckboxTreeViewer(parent);
		constraintsTree.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		constraintsTree.setSorter(new ViewerSorter());

		rootNode = CessarCategoryNode.createRoot(constraintsTree, filter);

		constraintsTree.setLabelProvider(new LabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				return ((ICessarTreeNode) element).getTextLabel();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
			 */
			@Override
			public Image getImage(Object element)
			{
				ICessarTreeNode node = (ICessarTreeNode) element;

				if (node.isErrored())
				{
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
				}
				else
				{
					return null;
				}
			}
		});

		constraintsTree.setCheckStateProvider(new ICheckStateProvider()
		{

			@Override
			public boolean isGrayed(Object element)
			{
				return ((ICessarTreeNode) element).isGrayed();
			}

			@Override
			public boolean isChecked(Object element)
			{
				return ((ICessarTreeNode) element).isChecked();
			}
		});

		constraintsTree.setContentProvider(new TreeContentProvider());
		constraintsTree.setInput(rootNode);

		constraintsTree.addCheckStateListener(getSelectionListenerr());
		constraintsTree.addSelectionChangedListener(getSelectionListenerr());

		return constraintsTree.getTree();
	}

	/**
	 * Creates the description area.
	 *
	 * @param parent
	 * @return the <code>descriptionArea</code>.
	 */
	private Control createDescriptionArea(Composite parent)
	{
		descriptionArea = new StyledText(parent, SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		descriptionArea.setWordWrap(true);
		descriptionArea.setText(CessarValidationUIMessages.preferences_no_selection);

		return descriptionArea;
	}

	private void createButtons(Composite parent)
	{

		Display display = parent.getDisplay();
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);

		Composite buttonsComposite = new Composite(parent, SWT.NONE);
		CessarFormToolkit cessarFormToolkit = new CessarFormToolkit(display);
		GridLayout layout2 = new GridLayout(4, false);
		layout2.horizontalSpacing = 0;
		layout2.verticalSpacing = 0;
		layout2.marginHeight = 0;
		layout2.marginWidth = 0;

		buttonsComposite.setLayout(layout2);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttonsComposite.setLayoutData(layoutData);

		GridLayout buttonLayout = new GridLayout(4, false);
		buttonsComposite.setLayout(buttonLayout);
		GridData buttonGridData = new GridData(SWT.RIGHT, SWT.None, true, false);
		buttonsComposite.setLayoutData(buttonGridData);

		Label label = new Label(buttonsComposite, SWT.NONE);

		GridData labelLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		label.setLayoutData(labelLayoutData);

		createButton(buttonsComposite, true, CessarPluginActivator.COLLAPSED_ICON_ID, cessarFormToolkit);
		createButton(buttonsComposite, false, CessarPluginActivator.EXPANDED_ICON_ID, cessarFormToolkit);
	}

	private void createButton(Composite buttonsContainer, boolean expand, String imageKey, CessarFormToolkit formToolkit)
	{
		final Button newButton = formToolkit.createButton(buttonsContainer, "", SWT.NONE); //$NON-NLS-1$

		GridData buttonLayoutData = new GridData(SWT.RIGHT, SWT.TOP, false, false);

		newButton.setLayoutData(buttonLayoutData);
		Image image = CessarPluginActivator.getDefault().getImageRegistry().get(imageKey);
		newButton.setImage(image);
		SelectionAdapter treeListener = createTreeListener(expand);
		newButton.addSelectionListener(treeListener);
		listeners.add(treeListener);
	}

	/**
	 * Create a listener to expand/collapse the {@code constraintsTree}
	 *
	 * @param state
	 * @return
	 */
	private SelectionAdapter createTreeListener(final boolean expand)
	{

		SelectionAdapter adapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				if (expand)
				{
					constraintsTree.expandAll();
				}
				else
				{
					constraintsTree.collapseAll();
				}

			}
		};
		return adapter;
	}

	/**
	 * Retrieves the description area.
	 *
	 * @return the <code>descriptionArea</code>
	 */
	private StyledText getDescriptionArea()
	{
		return descriptionArea;
	}

	/**
	 * Retrieves the selection listener.
	 *
	 * @return the <code>selectionListener</code>
	 */

	private SelectionListener getSelectionListenerr()
	{
		if (selectionListener == null)
		{
			selectionListener = new SelectionListener();
		}
		return selectionListener;
	}

	/**
	 * Restores the default constraints.
	 */
	public void performDefaults()
	{
		rootNode.restoreDefaults();
	}

	/**
	 * Saves the changes made after "Ok" button click to the validation preferences.
	 *
	 * @return true
	 */
	public boolean performOk()
	{
		rootNode.applyToPreferences();
		EMFModelValidationPreferences.save();

		return true;
	}

	/**
	 *
	 */
	public void dispose()
	{
		// clear buttons listeners
		if (listeners != null)
		{
			listeners.clear();
		}
	}

}
