/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Jul 8, 2009 2:02:49 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.internal.platform.ui.Messages;
import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.PostBuildPreferencesAccessor;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.core.platform.ui.widgets.MultiDatatypeValueHandler;
import eu.cessar.ct.sdk.IPostBuildContext;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * A dialog displayed for editing parameters/references with multiple values. It is composed of 3 parts: input
 * part(editor), center part(add, remove, up and down buttons) and output part (list)
 *
 * @author uidl6870
 * @param <T>
 * @Review uidl6458 - 19.04.2012
 */
public class MultiValueDialog<T> extends TitleAreaDialog
{
	/**
	 * The distance to the top inside the composite
	 */
	private static final int CENTER_PART_VERTICAL_INDENT = 80;
	private static final String SHELL_TITLE = Messages.MultiValueDialog_title;
	private static final String ADD_TEXT = Messages.MultiValueDialog_button_add;
	private static final String REMOVE_TEXT = Messages.MultiValueDialog_button_remove;
	private static final String UP_TEXT = Messages.MultiValueDialog_button_up;
	private static final String DOWN_TEXT = Messages.MultiValueDialog_button_down;

	Button addButton;
	Button removeButton;
	Button upButton;
	Button downButton;
	Button enablePBFilterBtn;
	Button linkElements;
	Button collapseAll;
	Button expandAll;
	private Composite leftComposite;
	private Composite contents;
	private Composite centerComposite;
	private Transfer[] transfer = new Transfer[] {LocalSelectionTransfer.getTransfer()};

	/**
	 * Tracks the currently active drag source. We need this as there is no chance of getting the source out of a drop
	 * event. For the sake of knowing whether it is a reordering of items or a new value being dropped in the table we
	 * need to know this.
	 */
	private Control currentDragSource;

	private final IMultiValueHandler<T> handler;

	/* maximum number of values that can be added in the list */
	private int maxValues;

	/* editor */
	private Control editor;

	private TableViewer resultViewer;

	/* list with current values */
	private List<T> currentValuesList;

	private ViewerFiltering inputViewerFiltering;
	private ViewerFiltering outputViewerFiltering;

	/**
	 *
	 * @param parentShell
	 * @param handler
	 */
	public MultiValueDialog(Shell parentShell, IMultiValueHandler<T> handler)
	{
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.handler = handler;
		currentValuesList = new ArrayList<>(handler.getValues());
		maxValues = handler.getMaxValues();
	}

	/**
	 * @param filtering
	 */
	public void setInputViewerFiltering(ViewerFiltering filtering)
	{
		this.inputViewerFiltering = filtering;
	}

	/**
	 * @param filtering
	 */
	public void setOutputViewerFiltering(ViewerFiltering filtering)
	{
		this.outputViewerFiltering = filtering;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.AreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		updateDialogHeader();

		Composite parentComposite = (Composite) super.createDialogArea(parent);
		parentComposite.setLayout(new GridLayout(1, false));
		parentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// main composite
		contents = new Composite(parentComposite, SWT.NONE);
		contents.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		contents.setLayout(new GridLayout(3, false));

		// left composite
		leftComposite = new Composite(contents, SWT.RESIZE);
		if (inputViewerFiltering != null && !leftComposite.isDisposed())
		{
			inputViewerFiltering.createFilterArea(leftComposite);
		}

		// center composite
		centerComposite = new Composite(contents, SWT.NONE);

		// right composite
		Composite rightComposite = new Composite(contents, SWT.RESIZE);
		setOutputViewerFiltering(new ViewerFiltering(new ResultTableViewerFilter()));
		if (outputViewerFiltering != null && !leftComposite.isDisposed())
		{
			outputViewerFiltering.createFilterArea(rightComposite);
			outputViewerFiltering.setInput(resultViewer);
		}

		createInputPart(leftComposite);
		createCenterPart(centerComposite, shouldEnable());
		createOutputPart(rightComposite);

		// right composite
		if (PostBuildPreferencesAccessor.isEnabledPostBuildContext(MetaModelUtils.getProject(handler.getInputObject()))
			&& getPBContext() != null)
		{
			createBottomPart(new Composite(parentComposite, SWT.None));
		}

		addListeners();

		if (currentValuesList.isEmpty())
		{
			removeButton.setEnabled(false);
		}

		return parentComposite;

	}

	/**
	 *
	 */
	private void updateDialogHeader()
	{
		getShell().setText(SHELL_TITLE);
		setMessage("Select values for " + handler.getTypeName(true)); //$NON-NLS-1$
		setTitleImage(CessarPluginActivator.getDefault().getImage(PlatformUIConstants.IMAGE_ID_MULTI_VALUE));

	}

	private void addListeners()
	{
		addButton.addSelectionListener(new AddSelectionListner());
		removeButton.addSelectionListener(new RemoveSelectionListener());
		upButton.addSelectionListener(new UpSelectionListener());
		downButton.addSelectionListener(new DownSelectionListener());

		expandAll.addSelectionListener(new ExpandAllSelectionListener());
		collapseAll.addSelectionListener(new CollapseAllSelectionListener());
		// input fields
		handleComboAndCComboEditors();
		editor.addKeyListener(new EnterPressedListener());
		editor.addMouseListener(new InputPartMouseAdapter());
		// output fields
		resultViewer.getTable().addMouseListener(new OutputPartMouseAdapter());
		resultViewer.getTable().addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.character == SWT.BS || e.character == SWT.DEL)
				{
					removeSelectedResults();
				}
			}
		});
	}

	private void handleComboAndCComboEditors()
	{
		if (editor instanceof CCombo)
		{
			((CCombo) editor).addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					changeAddButtonState(shouldEnable());
				}
			});
		}

		if (editor instanceof Combo)
		{
			((Combo) editor).addSelectionListener(new SelectionAdapter()
			{

				@Override
				public void widgetSelected(SelectionEvent e)
				{
					changeAddButtonState(shouldEnable());
				}

			});
		}
	}

	/**
	 * @param contents
	 */
	private void createCenterPart(Composite composite, boolean shouldEnable)
	{
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		composite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, false, true);
		layoutData.widthHint = 100;
		layoutData.verticalIndent = CENTER_PART_VERTICAL_INDENT;
		composite.setLayoutData(layoutData);
		addButton = new Button(composite, SWT.PUSH);
		addButton.setText(ADD_TEXT);
		changeAddButtonState(shouldEnable);
		GridData buttonLayoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		addButton.setLayoutData(buttonLayoutData);
		removeButton = new Button(composite, SWT.PUSH);
		removeButton.setText(REMOVE_TEXT);
		removeButton.setLayoutData(buttonLayoutData);
		upButton = new Button(composite, SWT.PUSH);
		upButton.setText(UP_TEXT);
		upButton.setLayoutData(buttonLayoutData);
		downButton = new Button(composite, SWT.PUSH);
		downButton.setText(DOWN_TEXT);
		downButton.setLayoutData(buttonLayoutData);

	}

	private void changeAddButtonState(boolean shouldEnable)
	{
		if (editor instanceof Tree)
		{
			boolean empty = candidates.isEmpty();
			if (!empty)
			{
				Object object = candidates.get(0);
				setAddButtonState(shouldEnable, object);
			}
			else
			{
				addButton.setEnabled(false);
			}
			checkInputPart();
		}
		else
		{
			List<T> editorValue = handler.getEditorValue();
			if (!editorValue.isEmpty())
			{
				T t = editorValue.get(0);
				setAddButtonState(shouldEnable, t);
			}
			else
			{
				addButton.setEnabled(false);
			}
			checkInputPart();
		}
	}

	private void setAddButtonState(boolean shouldEnable, Object object)
	{

		if (object != null)
		{

			addButton.setEnabled(shouldEnable);
		}
		else
		{
			addButton.setEnabled(false);
		}
	}

	@Requirement(
		reqID = "83737")
	private void createOutputPart(Composite composite)
	{
		GridLayout layout = new GridLayout(1, false);
		setLayoutSettings(layout);
		composite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(layoutData);
		Composite labelComposite = new Composite(composite, SWT.NONE);

		GridLayout layout2 = new GridLayout(2, false);
		layout2.horizontalSpacing = 0;

		layout2.marginWidth = 0;
		labelComposite.setLayout(layout2);
		GridData layoutData2 = new GridData(SWT.FILL, SWT.TOP, true, false);
		layoutData2.heightHint = 25;
		labelComposite.setLayoutData(layoutData2);
		Label label = new Label(labelComposite, SWT.NONE);
		label.setText(Messages.MultiValueDialog_available_values);

		GridData featureTableGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		featureTableGridData.widthHint = 200;
		featureTableGridData.heightHint = 150;

		resultViewer = new TableViewer(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		final Table resultTable = resultViewer.getTable();
		resultTable.setLayoutData(featureTableGridData);

		resultViewer.addDropSupport(DND.DROP_MOVE, transfer, new OutputPartDropAdapter());

		DragSource dndSource = new DragSource(resultTable, DND.DROP_MOVE);
		dndSource.setTransfer(transfer);

		dndSource.addDragListener(new DragSourceAdapter()
		{
			@Override
			public void dragStart(DragSourceEvent event)
			{
				if (resultTable.getSelectionCount() == 0)
				{
					event.doit = false;
				}

				LocalSelectionTransfer.getTransfer().setSelection(resultViewer.getSelection());
				currentDragSource = resultTable;
			}

			@Override
			public void dragFinished(DragSourceEvent event)
			{
				super.dragFinished(event);

				// cleanup
				currentDragSource = null;
			}
		});

		resultViewer.setLabelProvider(handler.getLabelProvider());
		resultViewer.setContentProvider(new ArrayContentProvider());

		// keep this order as otherwise the filter property will not work
		resultViewer.setInput(currentValuesList);
		if (outputViewerFiltering != null)
		{
			outputViewerFiltering.setInput(resultViewer);
		}
		// end of order
	}

	/**
	 *
	 * @param contents
	 */
	private void createInputPart(Composite composite)
	{
		GridLayout layout = new GridLayout(1, false);
		setLayoutSettings(layout);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Display display = composite.getDisplay();

		Composite buttonComposite = new Composite(composite, SWT.NONE);
		CessarFormToolkit cessarFormToolkit = new CessarFormToolkit(display);
		GridLayout layout2 = new GridLayout(4, false);
		setLayoutSettings(layout2);

		buttonComposite.setLayout(layout2);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);

		buttonComposite.setLayoutData(layoutData);

		// create label
		Label label = new Label(buttonComposite, SWT.NONE);

		GridData labelLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		label.setLayoutData(labelLayoutData);

		createToolbarButtons(buttonComposite, cessarFormToolkit);

		// ask handler to create the editor
		// uidu0944 - post build
		// check whether the PB filtering checkbox has been rendered and is
		// checked
		editor = handler.createSingleValueEditor(composite,
			(isPBFilteringChecked && getPBContext() != null && PostBuildPreferencesAccessor.isEnabledPostBuildContext(
				MetaModelUtils.getProject(handler.getInputObject()))),
			getPBContext());

		DragSource dndSource = new DragSource(editor, DND.DROP_MOVE);
		dndSource.setTransfer(transfer);

		dndSource.addDragListener(new InputPartDragListener());

		if (editor instanceof Composite)
		{
			DropTarget dndTarget = new DropTarget(editor, DND.DROP_MOVE);
			dndTarget.setTransfer(transfer);
			dndTarget.addDropListener(new InputPartDropListener());
		}

		GridData edGd;

		edGd = createEditorGridData(editor);

		boolean testConditionNot = ((editor instanceof Text) || (editor instanceof Button)
			|| (editor instanceof Label));
		testConditionNot = testConditionNot || !((editor instanceof Combo) || (editor instanceof CCombo));

		if (testConditionNot)
		{
			editor.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseDoubleClick(MouseEvent e)
				{
					addValueToEditor();
				}
			});

			edGd.widthHint = 200;
			edGd.heightHint = 150;
		}
		edGd.verticalAlignment = SWT.FILL;
		editor.setLayoutData(edGd);

		// set label text
		label.setText((editor instanceof org.eclipse.swt.widgets.List || editor instanceof Combo)
			? Messages.MultiValueDialog_options : Messages.MultiValueDialog_edit_value);
		editor.setFocus();

		checkInputPart();
	}

	@SuppressWarnings("static-method")
	private GridData createEditorGridData(Control currentEditor)
	{
		GridData edGd;
		if (currentEditor instanceof Button)
		{
			edGd = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true);
		}
		else
		{
			edGd = new GridData(SWT.FILL, SWT.TOP, true, true);
		}
		return edGd;
	}

	@SuppressWarnings("static-method")
	private void setLayoutSettings(GridLayout layout)
	{
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
	}

	private void createToolbarButtons(Composite buttonComposite, CessarFormToolkit cessarFormToolkit)
	{
		expandAll = cessarFormToolkit.createButton(buttonComposite, "", SWT.PUSH); //$NON-NLS-1$
		expandAll.setImage(
			CessarPluginActivator.getDefault().getImageRegistry().get(PlatformUIConstants.IMAGE_EXPAND_ALL));
		expandAll.setEnabled(true);
		GridData buttonLayoutData = new GridData(SWT.RIGHT, SWT.TOP, false, false);
		expandAll.setLayoutData(buttonLayoutData);

		collapseAll = cessarFormToolkit.createButton(buttonComposite, "", SWT.PUSH); //$NON-NLS-1$
		collapseAll.setImage(
			CessarPluginActivator.getDefault().getImageRegistry().get(PlatformUIConstants.IMAGE_COLLAPSE_ALL));
		collapseAll.setEnabled(true);
		collapseAll.setLayoutData(buttonLayoutData);

		linkElements = cessarFormToolkit.createButton(buttonComposite, "", SWT.TOGGLE); //$NON-NLS-1$
		linkElements.setImage(
			CessarPluginActivator.getDefault().getImageRegistry().get(PlatformUIConstants.IMAGE_LINK_WITH_EDITOR));
		linkElements.setEnabled(true);
		linkElements.setLayoutData(buttonLayoutData);
	}

	private void checkInputPart()
	{
		if (editor instanceof Tree)
		{
			boolean empty = candidates.isEmpty();
			if (!empty)
			{
				Object object = candidates.get(0);
				setToolbarButtonsState(object);
			}
			else
			{
				collapseAll.setEnabled(false);
				expandAll.setEnabled(false);
				linkElements.setEnabled(false);
			}
		}
		else
		{
			collapseAll.setVisible(false);
			expandAll.setVisible(false);
			linkElements.setVisible(false);
		}
	}

	private void setToolbarButtonsState(Object object)
	{
		if (object != null)
		{

			collapseAll.setEnabled(true);
			expandAll.setEnabled(true);
			linkElements.setEnabled(true);
		}
		else
		{
			collapseAll.setEnabled(false);
			expandAll.setEnabled(false);
			linkElements.setEnabled(false);
		}
	}

	@Override
	protected void okPressed()
	{
		if (handler.acceptValues(currentValuesList))
		{
			super.okPressed();
		}
	}

	/**
	 *
	 */
	public void enableOrDisableAddButton()
	{

		if (shouldEnable())
		{
			addButton.setEnabled(true);
		}
		else
		{
			addButton.setEnabled(false);
		}
	}

	// If we decide to check and do not allow the add for duplicate objects this part needs to be uncommented
	// /**
	// * This method is checking if the selected object that you want to add is already into the list. In this case, the
	// * add button shall be disable, otherwise the add button is enable or not based on the maximum values that you can
	// * add into the list.
	// *
	// * @param selectedObject
	// */
	// public void enableOrDisableAddButton(Object selectedObject)
	// {
	// if (currentValuesList.contains(selectedObject))
	// {
	// addButton.setEnabled(false);
	// }
	// else
	// {
	// enableOrDisableAddButton();
	// }
	//
	// }

	/**
	 * Used to disable the "Add" button if current number of values reaches the maximum number of allowed values
	 *
	 * @return false if the number of current values is smaller than the number of maximum values, true otherwise
	 */
	private boolean shouldEnable()
	{
		// upper bound = "*"
		if (maxValues < 0)
		{
			return true;
		}
		else
		{
			return maxValues > currentValuesList.size();
		}
	}

	private void removeSelectedResults()
	{
		// edit the result list INDEX BASED. do not change!
		int[] selectionIndices = resultViewer.getTable().getSelectionIndices();
		if (selectionIndices == null || selectionIndices.length == 0)
		{
			return;
		}

		List<Integer> indexList = new ArrayList<>(selectionIndices.length);
		for (int index: selectionIndices)
		{
			indexList.add(index);
		}
		Collections.sort(indexList);
		Collections.reverse(indexList);

		int lastSelectionIndex = indexList.get(0);
		for (int selectionIndex: indexList)
		{
			currentValuesList.remove(selectionIndex);
		}

		resultViewer.refresh();
		resultViewer.getTable().deselectAll();
		if (lastSelectionIndex > currentValuesList.size() - 1)
		{
			lastSelectionIndex = currentValuesList.size() - 1;
		}
		resultViewer.getTable().select(lastSelectionIndex);

		if (currentValuesList.isEmpty())
		{
			removeButton.setEnabled(false);
		}
		changeAddButtonState(shouldEnable());
	}

	/**
	 * @param sourceIndices
	 * @param distance
	 *        </br>
	 *        positive -> downwards</br>
	 *        negative -> upwards
	 * @return the new indices after the objects have been moved or NULL if the move action exceeded the ranges and
	 *         nothing at all was executed.
	 */
	private int[] moveResultItem(int[] sourceIndices, int distance)
	{
		Table resultTable = resultViewer.getTable();
		if (!moveIsValid(sourceIndices, distance))
		{
			resultTable.setSelection(sourceIndices);
			return null;
		}

		List<Integer> newSelectionIndices = new ArrayList<>();
		if (distance > 0)
		{
			// do not change order here. start from the end.
			for (int i = sourceIndices.length - 1; i >= 0; i--)
			{
				int selectionIndex = sourceIndices[i];
				if (selectionIndex < currentValuesList.size())
				{
					int newIndex = selectionIndex + distance;
					T toMove = currentValuesList.get(selectionIndex);
					currentValuesList.remove(selectionIndex);
					currentValuesList.add(newIndex, toMove);
					newSelectionIndices.add(newIndex);
				}
				else
				{
					break;
				}
			}
		}
		else if (distance < 0)
		{
			for (int selectionIndex: sourceIndices)
			{
				if (selectionIndex > 0)
				{
					int newIndex = selectionIndex + distance;
					T toMove = currentValuesList.get(selectionIndex);
					currentValuesList.remove(selectionIndex);
					currentValuesList.add(newIndex, toMove);
					newSelectionIndices.add(newIndex);
				}
				else
				{
					break;
				}
			}
		}

		resultViewer.refresh(true);

		// primitive type lists...
		int[] newIndices = new int[newSelectionIndices.size()];
		for (int i = 0; i < newSelectionIndices.size(); i++)
		{
			newIndices[i] = newSelectionIndices.get(i);
		}
		if (distance == 0 || newIndices.length != 0)
		{
			resultTable.setSelection(newIndices);
		}

		resultTable.setFocus();

		return newIndices;
	}

	private boolean moveIsValid(int[] sourceIndices, int distance)
	{
		for (int index: sourceIndices)
		{
			// too close to top?
			if (distance > 0)
			{
				if (index + distance >= currentValuesList.size())
				{
					return false;
				}
			}
			// too close to bottom?
			else if (distance < 0)
			{
				if (index + distance < 0)
				{
					return false;
				}
			}
		}

		return true;
	}

	private void addValueToEditor()
	{
		// retrieve the editor's value
		if (addButton.isEnabled())
		{
			List<T> editorValue = handler.getEditorValue();
			handler.clearEditorValue();
			if ((editorValue != null) && (!editorValue.isEmpty()))
			{
				if (editorValue.get(0) != null)
				{
					currentValuesList.addAll(editorValue);

					resultViewer.refresh();
					// select based on indices as items are not unique
					resultViewer.getTable().deselectAll();
					resultViewer.getTable().select(currentValuesList.size() - editorValue.size(),
						currentValuesList.size() - 1);
					removeButton.setEnabled(true);
				}
			}

		}
		changeAddButtonState(shouldEnable());

		editor.setFocus();
	}

	@Requirement(
		reqID = "54487")
	private final class OutputPartDropAdapter extends ViewerDropAdapter
	{
		/**
		 * @param viewer
		 * @param resultTable
		 */
		private OutputPartDropAdapter()
		{
			super(resultViewer);
		}

		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType)
		{
			return true;
		}

		@Override
		public boolean performDrop(Object data)
		{

			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ViewerDropAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		@Override
		public void drop(DropTargetEvent event)
		{
			super.drop(event);
			if (shouldEnable())
			{
				if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType))
				{
					// default: end of list
					int currentValueListSize = currentValuesList.size();
					int targetIndex = currentValueListSize - 1;

					Point targetPoint = event.display.map(null, resultViewer.getTable(), event.x, event.y);
					TableItem targetItem = resultViewer.getTable().getItem(targetPoint);
					if (targetItem != null)
					{
						// drop on an existing element
						targetIndex = resultViewer.getTable().indexOf(targetItem);
					}
					else
					{
						// if out of bounds, append at the end of the list
						targetIndex = currentValueListSize - 1;
					}

					switch (determineLocation(event))
					{
						case LOCATION_BEFORE:
							// black line when mouse over -> insert inbetween
							break;
						default:
							// dropped directly on an item -> insert after
							targetIndex++;
							break;
					}

					if (currentDragSource == resultViewer.getTable())
					{
						int distance = 0;
						int sourceIndex = Integer.MAX_VALUE;
						for (int index: resultViewer.getTable().getSelectionIndices())
						{
							if (index < sourceIndex)
							{
								sourceIndex = index;
							}
						}
						if (sourceIndex > targetIndex)
						{
							distance = targetIndex - sourceIndex;
						}
						else if (sourceIndex < targetIndex)
						{
							distance = targetIndex - sourceIndex - resultViewer.getTable().getSelectionCount();
						}
						moveResultItem(resultViewer.getTable().getSelectionIndices(), distance);
					}
					else if (currentDragSource == editor)
					{
						List<T> sourceItems = handler.getEditorValue();
						handler.clearEditorValue();
						if ((sourceItems != null) && (!sourceItems.isEmpty()))
						{
							if (sourceItems.get(0) != null)
							{
								if (maxValues == -1)
								{
									currentValuesList.addAll(targetIndex, sourceItems);
								}
								else if (currentValueListSize <= maxValues)
								{
									if (currentValueListSize + sourceItems.size() <= maxValues)
									{
										currentValuesList.addAll(targetIndex, sourceItems);
									}
									else
									{
										int possibleValuesToAdd = maxValues - currentValueListSize;
										for (int i = 0; i < possibleValuesToAdd; i++)
										{
											currentValuesList.add(sourceItems.get(i));
										}
									}
								}
								resultViewer.refresh();

								removeButton.setEnabled(true);
								changeAddButtonState(shouldEnable());

								resultViewer.getTable().deselectAll();
								resultViewer.getTable().select(targetIndex, targetIndex + sourceItems.size() - 1);

							}
						}
					}
				}
			}
		}

	}

	private final class InputPartMouseAdapter extends MouseAdapter
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		@Override
		public void mouseDown(MouseEvent e)
		{
			boolean linkOn = linkElements.getSelection();
			if (!linkOn)
			{
				return;
			}
			if (editor instanceof Tree)
			{
				TreeItem[] selection = ((Tree) editor).getSelection();
				for (TreeItem treeItem: selection)
				{
					Object data = treeItem.getData();

					Object list = resultViewer.getInput();
					if (list != null)
					{
						if (list instanceof List<?>)
						{
							boolean contains = ((List<?>) list).contains(data);
							if (contains)
							{
								StructuredSelection structuredSelection = new StructuredSelection(data);
								resultViewer.setSelection(structuredSelection, true);
							}
							else
							{
								resultViewer.setSelection(null);
							}
						}
						else if (data.equals(list))
						{
							StructuredSelection structuredSelection = new StructuredSelection(data);
							resultViewer.setSelection(structuredSelection, true);
						}
					}

				}
			}
		}
	}

	private final class OutputPartMouseAdapter extends MouseAdapter
	{
		private TreeItem elementToSelect;

		@Override
		public void mouseDoubleClick(MouseEvent e)
		{
			removeSelectedResults();
		}

		@Override
		public void mouseDown(MouseEvent e)
		{
			boolean linkOn = linkElements.getSelection();
			if (linkOn)
			{
				ISelection selection = resultViewer.getSelection();
				if (!(selection instanceof IStructuredSelection))
				{
					return;
				}
				else
				{
					IStructuredSelection ss = (IStructuredSelection) selection;
					Object firstElement = ss.getFirstElement();
					if (editor instanceof Tree && firstElement instanceof GReferrable)
					{
						Tree treeEditor = (Tree) editor;
						TreeItem items[] = treeEditor.getItems();
						searchItem(items, firstElement);
						if (elementToSelect != null)
						{
							treeEditor.setSelection(elementToSelect);
						}
					}
				}
			}
		}

		// Search through the entire tree to see if we can find the item
		private void searchItem(TreeItem[] items, Object firstElement)
		{
			for (TreeItem i: items)
			{
				Object data = i.getData();
				if (data.equals(firstElement))
				{
					elementToSelect = i;
				}
				else
				{
					searchItem(i.getItems(), firstElement);

				}
			}

		}
	}

	@Requirement(
		reqID = "54487")
	private final class InputPartDropListener extends DropTargetAdapter
	{
		@Override
		public void dropAccept(DropTargetEvent event)
		{
			super.dropAccept(event);
			if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType))
			{
				if (currentDragSource == resultViewer.getTable())
				{
					removeSelectedResults();
				}
			}
			else
			{
				event.detail = DND.DROP_NONE;
			}
		}

		@Override
		public void dragEnter(DropTargetEvent event)
		{
			if (currentDragSource == editor)
			{
				// do not suggest that D&D inside the input control is working
				event.detail = DND.DROP_NONE;
			}
		}
	}

	private final class InputPartDragListener extends DragSourceAdapter
	{
		@Override
		public void dragStart(DragSourceEvent event)
		{
			if (handler.getEditorValue().size() == 0)
			{
				event.doit = false;
			}
			LocalSelectionTransfer.getTransfer().setSelection(new StructuredSelection(handler.getEditorValue()));
			currentDragSource = editor;
		}

		@Override
		public void dragFinished(DragSourceEvent event)
		{
			super.dragFinished(event);

			// cleanup
			currentDragSource = null;
		}
	}

	private class CollapseAllSelectionListener extends SelectionAdapter
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			if (editor instanceof Tree)
			{
				TreeItem[] items = ((Tree) editor).getItems();
				for (TreeItem treeItem: items)
				{
					treeItem.setExpanded(false);
				}
			}
		}
	}

	// Go through the entire tree and expand all tree items if possible
	private void fullyExpand(TreeItem[] items)
	{
		for (TreeItem i: items)
		{
			i.setExpanded(true);
			fullyExpand(i.getItems());
		}
	}

	private class ExpandAllSelectionListener extends SelectionAdapter
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			if (editor instanceof Tree)
			{
				TreeItem[] items = ((Tree) editor).getItems();
				fullyExpand(items);
			}
		}
	}

	private class AddSelectionListner extends SelectionAdapter
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			addValueToEditor();
		}
	}

	private class RemoveSelectionListener extends SelectionAdapter
	{
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			removeSelectedResults();
		}
	}

	private class UpSelectionListener extends SelectionAdapter
	{
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			ISelection selection = resultViewer.getSelection();
			if (!(selection instanceof IStructuredSelection) || (selection.isEmpty()))
			{
				return;
			}
			else
			{
				moveResultItem(resultViewer.getTable().getSelectionIndices(), -1);
			}
		}
	}

	private class DownSelectionListener extends SelectionAdapter
	{
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			ISelection selection = resultViewer.getSelection();
			if (!(selection instanceof IStructuredSelection) || (selection.isEmpty()))
			{
				return;
			}
			else
			{
				moveResultItem(resultViewer.getTable().getSelectionIndices(), 1);
			}
		}
	}

	private class EnterPressedListener extends KeyAdapter
	{
		@Override
		public void keyReleased(KeyEvent e)
		{
			if (handler instanceof MultiDatatypeValueHandler)
			{
				changeAddButtonState(shouldEnable());
			}
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			if (editor instanceof Text)
			{
				// handle multi line values so that will not transfer data to the right
				int style = ((Text) editor).getStyle();
				if (SWT.MULTI == (style & SWT.MULTI))
				{
					return;
				}
			}

			if (e.character == SWT.CR)
			{
				e.doit = false;
				addValueToEditor();
			}
		}
	}

	private boolean isPBFilteringChecked = true;
	private List<Object> candidates = new ArrayList<>();

	private void createBottomPart(Composite composite)
	{
		composite.setLayout(new GridLayout(1, false));

		enablePBFilterBtn = new Button(composite, SWT.CHECK);
		enablePBFilterBtn.setText("Limit to post build context '" + getPBContext() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		enablePBFilterBtn.setSelection(isPBFilteringChecked);

		enablePBFilterBtn.addListener(SWT.Selection, new Listener()
		{

			public void handleEvent(Event e)
			{
				switch (e.type)
				{
					case SWT.Selection:
						isPBFilteringChecked = !isPBFilteringChecked;

						if (editor instanceof Tree)
						{
							handler.computeCandidateList((isPBFilteringChecked && getPBContext() != null
								&& PostBuildPreferencesAccessor.isEnabledPostBuildContext(
									MetaModelUtils.getProject(handler.getInputObject()))),
								getPBContext());
						}

						// }
						break;
					default:
						break;
				}
			}
		});

	}

	private IPostBuildContext getPBContext()
	{
		IPostBuildContext context = null;
		EObject inputObject = handler.getInputObject();
		if (inputObject instanceof GContainer)
		{
			context = EcucMetaModelUtils.getPostBuildContext((GContainer) inputObject, true);
		}
		return context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		super.createButtonsForButtonBar(parent);

		Button buttonHidden = new Button(parent, SWT.NONE);
		buttonHidden.setVisible(false); // the button is not visible
		buttonHidden.setEnabled(false);

		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		gd.heightHint = 0;
		buttonHidden.setLayoutData(gd);

		getShell().setDefaultButton(buttonHidden);
	}

	@Override
	public Control createContents(Composite parent)
	{
		Control localContents = super.createContents(parent);
		return localContents;
	}

	/**
	 * @param candidates2
	 */
	public void setPossibleCandidates(List<Object> candidates2)
	{
		candidates = candidates2;
	}

	@Requirement(
		reqID = "83735")
	private class ResultTableViewerFilter extends AbstractRegexViewerFilter
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element)
		{
			// handle AUTOSAR Referrable objects (reference values mostly)
			if (element != null && element instanceof GReferrable)
			{
				GReferrable referrable = (GReferrable) element;
				return matchText(referrable.gGetShortName(), getSearchString());
			}

			// handle the rest (parameter values)
			if (element != null)
			{
				return matchText(element.toString(), getSearchString());
			}

			return false;
		}
	}

}
