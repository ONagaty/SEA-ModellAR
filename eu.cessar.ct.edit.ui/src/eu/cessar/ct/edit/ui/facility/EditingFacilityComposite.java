package eu.cessar.ct.edit.ui.facility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.edit.ui.IEditingFacility;
import eu.cessar.ct.edit.ui.facility.composition.ISimpleComposition;
import eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion;
import eu.cessar.ct.edit.ui.facility.parts.EEditorPart;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * This class provides UI widgets and behavior for the properties of a selected resource, for a certain category.
 *
 * @author uidg4020
 *
 *         %created_by: uidv1701 %
 *
 *         %date_created: Fri Dec 19 16:57:33 2014 %
 *
 *         %version: 4 %
 */
public class EditingFacilityComposite implements IModelEditorChangeListener
{
	/**
	 * Key for identifying the composite that holds the properties view tabs
	 */
	public static final String KEY_PROPERTIES_TAB_COMPOSITE = "compositeType"; //$NON-NLS-1$
	/**
	 * Value of key for identifying the composite that holds the properties view tabs
	 */
	public static final String VALUE_PROPERTIES_TAB_COMPOSITE = "DynamicSectionWrapper"; //$NON-NLS-1$

	private static final Image EXPAND_ENABLED_IMAGE = CessarPluginActivator.getDefault().getImage(
		CessarPluginActivator.EDITOR_EXPAND_ENABLED_ICON_ID);
	private static final Image EXPAND_DISABLED_IMAGE = CessarPluginActivator.getDefault().getImage(
		CessarPluginActivator.EDITOR_EXPAND_DISABLED_ICON_ID);
	private static final Image COLLAPSE_IMAGE = CessarPluginActivator.getDefault().getImage(
		CessarPluginActivator.EDITOR_COLLAPSE_MULTIINPUT_ICON_ID);
	private static final Image ADD_MULTI_INPUT_IMAGE = CessarPluginActivator.getDefault().getImage(
		CessarPluginActivator.EDITOR_ADD_MULTIINPUT_ICON_ID);
	private static final Image DELETE_MULTI_INPUT_IMAGE = CessarPluginActivator.getDefault().getImage(
		CessarPluginActivator.EDITOR_DELETE_MULTIINPUT_ICON_ID);
	private static final String ADD_VALUE_EXPANSION = "Add new value"; //$NON-NLS-1$
	private static final String DELETE_ALL_EXPANSION = "Delete all"; //$NON-NLS-1$
	private static final String DELETE_VALUE_EXPANSION = "Delete"; //$NON-NLS-1$
	private static final String MULTI_EXPANSION_COMPOSITE_DATA = "multi_expansion_composite"; //$NON-NLS-1$

	/**
	 * Interface to be implemented by classes that receive changed modifications from this composite
	 *
	 * @author uidg4020
	 *
	 */
	public static interface IFacilityPageChangedListener extends EventListener
	{
		/**
		 * Method called when this composite has changed
		 */
		public void facilityPageChanged();
	}

	private CessarFormToolkit formToolkit;
	private ScrolledComposite parent;
	private Composite editorComposite;
	private List<IModelFragmentEditor> editorsList = new ArrayList<IModelFragmentEditor>();
	private EObject inputObject;
	private Long previousChangeStamp;

	private List<IModelFragmentEditor> changedModelEditorList = new ArrayList<IModelFragmentEditor>();
	private Map<IModelFragmentEditor, ToolItem> editorToToolItemMap = new HashMap<IModelFragmentEditor, ToolItem>();
	private Map<IModelFragmentEditor, List<Label>> editorToLabelsMap = new HashMap<IModelFragmentEditor, List<Label>>();
	private IEditingFacility editingFacility;
	private IFacilityPageChangedListener pageListener;
	private EEditorCategory editorCategory;

	/**
	 * @param editingFacility
	 * @param facilityPagelistener
	 *        - listener to be noted when the page needs to be resized
	 * @param editorCategory
	 *        - category of the editor
	 * @param scrolledParent
	 *        - the scrolled composite that will hold all widgets
	 * @param cFormToolkit
	 *        - the form toolkit to be used
	 */
	public EditingFacilityComposite(IEditingFacility editingFacility,
		IFacilityPageChangedListener facilityPagelistener, EEditorCategory editorCategory,
		ScrolledComposite scrolledParent, CessarFormToolkit cFormToolkit)
	{
		this.editingFacility = editingFacility;
		pageListener = facilityPagelistener;
		this.editorCategory = editorCategory;
		formToolkit = cFormToolkit;
		parent = scrolledParent;
	}

	/**
	 * Cleans mappings between close or null widgets and the changed editor that triggered the event and updates tool
	 * items and labels for this editor
	 */
	@Override
	public void modelEditorChanged(ModelEditorChangeEvent event)
	{
		if (event != null)
		{
			updateMappings(editorToLabelsMap);
			IModelFragmentEditor changedModelEditor = event.getChangedModelEditor();
			if (changedModelEditor != null)
			{
				IModelFragmentEditorExpansion editorExpansion = editingFacility.getEditorExpansion(changedModelEditor);

				if (editorExpansion != null)
				{

					if (!changedModelEditorList.contains(changedModelEditor))
					{
						changedModelEditorList.add(changedModelEditor);
					}
					ToolItem toolItem = editorToToolItemMap.get(changedModelEditor);
					if (toolItem != null && !toolItem.isDisposed())
					{
						updateExpansionToolItem(toolItem, editorExpansion);
					}

					if (editorExpansion.isMultiValueEditor())
					{
						List<Label> labels = editorToLabelsMap.get(changedModelEditor);
						List<EObject> expansionInputList = (List<EObject>) editorExpansion.getExpansionInput();
						if (labels != null)
						{
							for (int i = 0; i < expansionInputList.size(); i++)
							{
								if (labels.size() > i)
								{
									updateMultiInputLabel(labels.get(i), editorExpansion, expansionInputList.get(i));
								}
							}
						}

					}
				}
			}
		}

	}

	/**
	 * Method that initializes the input object. If the selected object is same as previous do nothing, else obtains
	 * editor providers for the category and creates the necessary widgets
	 *
	 * @param newInputObject
	 *        - the object to display properties for
	 * @param editorCategory
	 *        - the editor category
	 */
	public void setInput(EObject newInputObject)
	{
		ISimpleComposition simpleEditorComposition = editingFacility.getSimpleEditorComposition(newInputObject,
			editorCategory);
		List<IModelFragmentEditorProvider> editorProviders = new ArrayList<IModelFragmentEditorProvider>();
		if (simpleEditorComposition != null)
		{
			editorProviders = simpleEditorComposition.getEditorProviders();
		}

		// if the input is the same object as the previous one don't do anything
		if (inputObject == newInputObject)
		{
			// check if the model is modified
			boolean modifiedModel = isModifiedModel(inputObject);
			if (!modifiedModel)
			{
				return;
			}
		}
		// ToDo: what if the new input have exactly the same editors as the
		// previous input?
		// In such a case we could re-use the existing editor, just set a new
		// input.
		// This scenario is quite common imho.
		inputObject = newInputObject;
		parent.setRedraw(false);
		try
		{
			cleanUpWidgets();
			if (inputObject != null)
			{
				createWidgets(inputObject, editorProviders);
			}
		}
		finally
		{
			parent.setRedraw(true);
		}

	}

	/**
	 * Method that calls the {@link #cleanUpWidgets()} method and resets the input object to null
	 */
	public void reset()
	{
		cleanUpWidgets();
		inputObject = null;
	}

	/**
	 * Removes the null or disposed widgets mappings to editors
	 *
	 * @param map
	 *        - contains editors mapped to widgets
	 */
	private void updateMappings(Map<IModelFragmentEditor, List<Label>> map)
	{
		Set<IModelFragmentEditor> editors = map.keySet();
		for (IModelFragmentEditor editor: editors)
		{
			List<Label> widgets = map.get(editor);
			Iterator<Label> iterator = widgets.iterator();
			while (iterator.hasNext())
			{
				Widget widget = iterator.next();
				if (widget == null || widget.isDisposed())
				{
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Enables the tool item if the expansion for the current editor is not null and can be expanded
	 *
	 * @param toolItem
	 *        - tool item
	 * @param currentModelFragmExpansionEditor
	 *        - the expansion for the current editor
	 */
	private void updateExpansionToolItem(ToolItem toolItem,
		IModelFragmentEditorExpansion currentModelFragmExpansionEditor)
	{

		if (currentModelFragmExpansionEditor != null && currentModelFragmExpansionEditor.canExpand())
		{
			toolItem.setEnabled(true);
		}
		else
		{
			toolItem.setEnabled(false);
		}
	}

	/**
	 * Set the multi-input label text value according to the input and redraws the parent composite
	 *
	 * @param multiInputLabel
	 *        - multi input label
	 * @param currentModelFragmExpansionEditor
	 *        - the expansion for the current editor
	 * @param expInput
	 *        - input
	 */
	private void updateMultiInputLabel(Label multiInputLabel,
		IModelFragmentEditorExpansion currentModelFragmExpansionEditor, EObject expInput)
	{
		if (multiInputLabel != null && !multiInputLabel.isDisposed())
		{
			// multiInputLabel.setRedraw(false);
			parent.setRedraw(false);
			try
			{
				ILabelProvider labelProvider = currentModelFragmExpansionEditor.getLabelProvider();
				String multiInputText = "null"; //$NON-NLS-1$
				if (labelProvider != null)
				{
					multiInputText = labelProvider.getText(expInput);
				}
				multiInputLabel.setText(multiInputText);
			}
			finally
			{
				// multiInputLabel.setRedraw(true);
				parent.setRedraw(true);
			}
		}
	}

	/**
	 * Returns true if the model is modified, else returns false
	 *
	 * @param input
	 *        -the object to be checked if modified
	 * @return true if the model has changed
	 */
	private boolean isModifiedModel(EObject input)
	{
		IModelChangeStampProvider provider = IModelChangeMonitor.INSTANCE.getChangeStampProvider(input);
		long currentStamp = provider.getCurrentChangeStamp();
		if (previousChangeStamp == null || previousChangeStamp != currentStamp)
		{
			previousChangeStamp = currentStamp;
			return true;
		}
		return false;

	}

	/**
	 * Create widgets for this editor providers and call the method to handle expansion.
	 *
	 * @param input
	 *        - input object to render properties for
	 * @param editorProviders
	 *        - editor providers
	 * @param selection
	 */
	private void createWidgets(final EObject input, List<IModelFragmentEditorProvider> editorProviders)
	{
		Composite composite = null;

		for (IModelFragmentEditorProvider provider: editorProviders)
		{
			if (composite == null)
			{
				// first call
				composite = formToolkit.createPlainComposite(parent, SWT.NONE);
				composite.setData(KEY_PROPERTIES_TAB_COMPOSITE, VALUE_PROPERTIES_TAB_COMPOSITE);
				configureLayoutAndLayoutData(composite, 5, 1);
				composite.setBackgroundMode(SWT.INHERIT_DEFAULT);

				formToolkit.paintBordersFor(composite);
			}

			List<IModelFragmentEditor> upperLevelEditors = new ArrayList<IModelFragmentEditor>();

			final IModelFragmentEditor editor = provider.createEditor();
			editor.addModelEditorChangeListener(this);
			editorsList.add(editor);
			upperLevelEditors.add(editor);
			handleExpansion(editor, upperLevelEditors, input, null, null, formToolkit, composite, false, 1);

		}

		if (composite != null)
		{
			editorComposite = composite;
			parent.setContent(editorComposite);
			// editorComposite.setSize(editorComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			Point minSize = editorComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			editorComposite.setSize(minSize);
			parent.layout(true, true);
		}
	}

	/**
	 * Set the layout and the layout data for this composite.
	 *
	 * @param composite
	 * @param nrOfColumns
	 *        - number of columns
	 * @param horizontalSpan
	 */
	private void configureLayoutAndLayoutData(Composite composite, int nrOfColumns, int horizontalSpan)
	{

		GridLayout gridLayout = new GridLayout(nrOfColumns, false);
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		GridData gd = new GridData(SWT.FILL, SWT.None, true, false);
		gd.horizontalSpan = horizontalSpan;
		composite.setLayoutData(gd);
	}

	/**
	 * Handles the expansion of the current editor. Expands only if the expansion of the current editor is not null.
	 *
	 * @param currentEditor
	 *        current editor
	 * @param upperLevelEditors
	 *        -
	 * @param input
	 *        -input object
	 * @param expInput
	 *        - expansion input
	 * @param modelFragmExpansionEditor
	 *        -expansion of the current editor
	 * @param toolKit
	 * @param composite
	 * @param hasPreviousVerticalSep
	 * @param level
	 */
	private void handleExpansion(final IModelFragmentEditor currentEditor,
		List<IModelFragmentEditor> upperLevelEditors, final EObject input, EObject expInput,
		IModelFragmentEditorExpansion modelFragmExpansionEditor, final CessarFormToolkit toolKit,
		final Composite composite, final boolean hasPreviousVerticalSep, final int level)
	{
		IModelFragmentEditorExpansion modelFrExpansionEditor = modelFragmExpansionEditor;
		final List<IModelFragmentEditor> expansionEditors = new ArrayList<IModelFragmentEditor>();

		if (modelFrExpansionEditor == null)
		{
			modelFrExpansionEditor = editingFacility.getEditorExpansion(currentEditor);
		}

		final IModelFragmentEditorExpansion currentModelFragmExpansionEditor = modelFrExpansionEditor;

		// create the expand button
		ToolBar expansionToolBar = null;
		GridData gd;

		if (currentModelFragmExpansionEditor != null)
		{
			// we will have an expansion later on, pre-create the toolbar to
			// allocate the UI space
			expansionToolBar = new ToolBar(composite, SWT.FLAT);
			expansionToolBar.setBackground(composite.getBackground());
			gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
			expansionToolBar.setLayoutData(gd);
		}
		else
		{
			// no expansion for this editor, create a label to fill the space
			new Label(composite, SWT.NONE);
		}
		if (currentEditor != null)
		{
			currentEditor.setFormToolkit(toolKit);
			createEditorContents(currentEditor, composite);
			ISafeRunnable runnable = new SafeRunnable()
			{
				public void run() throws Exception
				{
					currentEditor.setInput(input);
					currentEditor.refresh();
				}
			};
			SafeRunnable.run(runnable);
		}
		else
		{
			// no editor to create, it should be a multi-value expansion editor
			if (currentModelFragmExpansionEditor != null)
			{

				IModelFragmentEditor masterEditor = currentModelFragmExpansionEditor.getMasterEditor();

				// place content for each member value of the multi input
				Composite multiInputComposite = toolKit.createPlainComposite(composite, SWT.None);
				configureLayoutAndLayoutData(multiInputComposite, 2, 4);

				Label multiInputLabel = new Label(multiInputComposite, SWT.None);
				updateMultiInputLabel(multiInputLabel, currentModelFragmExpansionEditor, expInput);

				List<Label> labels = new ArrayList<Label>();
				labels.add(multiInputLabel);

				if (editorToLabelsMap.containsKey(masterEditor))
				{
					List<Label> list = editorToLabelsMap.get(masterEditor);
					list.add(multiInputLabel);
				}
				else
				{
					editorToLabelsMap.put(masterEditor, labels);
				}
				ToolBar buttonsToolBar = new ToolBar(multiInputComposite, SWT.FLAT);
				buttonsToolBar.setBackground(multiInputComposite.getBackground());
				gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
				buttonsToolBar.setLayoutData(gd);

				final ToolItem deleteToolItem = new ToolItem(buttonsToolBar, SWT.PUSH);
				deleteToolItem.setToolTipText(DELETE_VALUE_EXPANSION);
				deleteToolItem.setImage(DELETE_MULTI_INPUT_IMAGE);

				final EObject expInput1 = expInput;

				handleSelection(
					deleteToolItem,
					new Runnable()
					{

						public void run()
						{
							final List<EObject> multiInputList = (List<EObject>) currentModelFragmExpansionEditor.getExpansionInput();
							multiInputList.remove(expInput1);
						}
					}, input, DELETE_VALUE_EXPANSION, multiInputComposite, currentModelFragmExpansionEditor, toolKit,
					upperLevelEditors, hasPreviousVerticalSep, level);
			}

		}

		// ask for expansion if any
		if (currentModelFragmExpansionEditor != null)
		{
			// expansion editors not retrieved yet
			if (expInput == null)
			{
				if (!currentModelFragmExpansionEditor.isMultiValueEditor())
				{
					expInput = (EObject) currentModelFragmExpansionEditor.getExpansionInput();
				}
			}
			// create the holder composite for the expansion part
			final ExpandableComposite expandable = toolKit.createExpandableComposite(composite,
				ExpandableComposite.NO_TITLE | ExpandableComposite.CLIENT_INDENT);

			gd = new GridData(SWT.FILL, SWT.NONE, true, false);
			gd.horizontalSpan = 6 + level;
			expandable.clientVerticalSpacing = 0;
			expandable.setLayoutData(gd);
			expandable.setExpanded(false);

			final Composite compositeForExpansion = toolKit.createPlainComposite(expandable, SWT.None);
			expandable.setClient(compositeForExpansion);

			GridLayout gridLayout;

			if (hasPreviousVerticalSep)
			{
				gridLayout = new GridLayout(7, false);
			}
			else
			{
				gridLayout = new GridLayout(6, false);
			}
			gridLayout.marginHeight = 0;
			gridLayout.verticalSpacing = 0;
			gridLayout.marginWidth = 0;
			compositeForExpansion.setLayout(gridLayout);
			compositeForExpansion.setBackgroundMode(SWT.INHERIT_DEFAULT);

			final ToolItem expansionToolItem = new ToolItem(expansionToolBar, SWT.PUSH);
			expansionToolItem.setImage(EXPAND_ENABLED_IMAGE);
			expansionToolItem.setDisabledImage(EXPAND_DISABLED_IMAGE);
			updateExpansionToolItem(expansionToolItem, currentModelFragmExpansionEditor);
			editorToToolItemMap.put(currentEditor, expansionToolItem);
			// expand/collapse
			handleExpansionWidgetSelection(currentEditor, upperLevelEditors, expansionToolItem, toolKit, expandable,
				compositeForExpansion, currentModelFragmExpansionEditor, expansionEditors, input, expInput,
				hasPreviousVerticalSep, level);

		}
		else
		{
			// no expansion to create
		}
	}

	/**
	 * Adds a listener to handle expansion to the widget for collapse/expand
	 *
	 * @param currentEditor
	 *        - current editor
	 * @param upperLevelEditors
	 *        -upper level editors
	 * @param expansionToolItem
	 *        -widget used for collapse/expand
	 * @param toolKit
	 * @param expandable
	 *        - expandable composite
	 * @param compositeForExpansion
	 * @param currentModelFragmExpansionEditor
	 *        -expansion of the editor
	 * @param expansionEditors
	 * @param input
	 * @param expInput
	 *        - expansion input
	 * @param hasPreviousVerticalSep
	 * @param level
	 */
	private void handleExpansionWidgetSelection(final IModelFragmentEditor currentEditor,
		final List<IModelFragmentEditor> upperLevelEditors, final ToolItem expansionToolItem,
		final CessarFormToolkit toolKit, final ExpandableComposite expandable, final Composite compositeForExpansion,
		final IModelFragmentEditorExpansion currentModelFragmExpansionEditor,
		final List<IModelFragmentEditor> expansionEditors, final EObject input, final EObject expInput,
		final boolean hasPreviousVerticalSep, final int level)
	{
		expansionToolItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				handleExpansionWidgetSelected(currentEditor, upperLevelEditors, expansionToolItem, toolKit, expandable,
					compositeForExpansion, currentModelFragmExpansionEditor, expansionEditors, input, expInput,
					hasPreviousVerticalSep, level);

			}

		});
	}

	/**
	 * Handles the expansion of a widget. Method called by the listener of the expandable object
	 *
	 * @param currentEditor
	 *        - current editor
	 * @param upperLevelEditors
	 *        -upper level editors
	 * @param expansionToolItem
	 *        -the expansion item used to collapse/expand
	 * @param toolKit
	 * @param expandable
	 *        - expandable composite
	 * @param compositeForExpansion
	 * @param currentModelFragmExpansionEditor
	 *        -expansion of the editor
	 * @param expansionEditors
	 * @param input
	 * @param expInput
	 *        - expansion input
	 * @param hasPreviousVerticalSep
	 * @param level
	 */
	private void handleExpansionWidgetSelected(final IModelFragmentEditor currentEditor,
		final List<IModelFragmentEditor> upperLevelEditors, final ToolItem expansionToolItem,
		final CessarFormToolkit toolKit, final ExpandableComposite expandable, final Composite compositeForExpansion,
		final IModelFragmentEditorExpansion currentModelFragmExpansionEditor,
		final List<IModelFragmentEditor> expansionEditors, final EObject input, final EObject expInput,
		final boolean hasPreviousVerticalSep, final int level)
	{
		parent.setRedraw(false);
		EObject expansionInput = expInput;
		try
		{
			boolean shallExpand = !expandable.isExpanded() && currentModelFragmExpansionEditor.canExpand();
			if (currentEditor != null)
			{
				currentModelFragmExpansionEditor.setExpanded(shallExpand);
			}
			if (shallExpand)
			{
				boolean editorHasChanged = false;
				if (currentEditor != null && changedModelEditorList.contains(currentEditor))
				{
					editorHasChanged = true;
					expansionEditors.clear();
					disposeChildrenForComposite(compositeForExpansion);
					changedModelEditorList.remove(currentEditor);
					if (!currentModelFragmExpansionEditor.isMultiValueEditor())
					{
						expansionInput = (EObject) currentModelFragmExpansionEditor.getExpansionInput();
					}
				}
				if (expansionInput != null)
				{
					if (expansionEditors.isEmpty())
					{
						List<IModelFragmentEditorProvider> expansionProviders = currentModelFragmExpansionEditor.getEditorProviders(
							expansionInput, upperLevelEditors);
						for (IModelFragmentEditorProvider expansionProvider: expansionProviders)
						{
							IModelFragmentEditor editorForExpansion = expansionProvider.createEditor();
							editorForExpansion.addModelEditorChangeListener(EditingFacilityComposite.this);
							editorForExpansion.setInput(expansionInput);
							editorForExpansion.setFormToolkit(toolKit);
							expansionEditors.add(editorForExpansion);
						}
					}
				}
				// expand
				expansionToolItem.setImage(COLLAPSE_IMAGE);
				if (compositeForExpansion.getChildren().length == 0 || editorHasChanged)
				{
					boolean haveExpansionEditors = true;

					if (currentModelFragmExpansionEditor.isMultiValueEditor())
					{
						haveExpansionEditors = handleExpansionForMultiEditor(toolKit, compositeForExpansion,
							currentModelFragmExpansionEditor, expansionEditors, input, hasPreviousVerticalSep, level,
							true);

					}

					if (haveExpansionEditors)
					{
						createEditorsForExpansion(toolKit, compositeForExpansion, expansionEditors, expansionInput,
							hasPreviousVerticalSep, level);
					}
				}
			}
			else
			{
				// collapse
				expansionToolItem.setImage(EXPAND_ENABLED_IMAGE);
			}
			expandable.setExpanded(shallExpand);
			parent.layout(true, true);
			// Point minSize = editorComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			// parent.setMinSize(minSize);
			pageListener.facilityPageChanged();
			// tabbedPropertySheetPage.resizeScrolledComposite();
		}
		finally
		{
			parent.setRedraw(true);
		}
	}

	/**
	 * Handles multi editor expansion. Returns false if there are no editors to expand, else returns true
	 *
	 * @param toolKit
	 * @param compositeForExpansion
	 *        - composite for expansion
	 * @param currentModelFragmExpansionEditor
	 *        - expansion of the
	 * @param expansionEditors
	 *        -expansion editors
	 * @param input
	 * @param hasPreviousVerticalSep
	 * @param level
	 * @param createEntireExpansion
	 * @return
	 */
	private boolean handleExpansionForMultiEditor(final CessarFormToolkit toolKit,
		final Composite compositeForExpansion, final IModelFragmentEditorExpansion currentModelFragmExpansionEditor,
		final List<IModelFragmentEditor> expansionEditors, final EObject input, final boolean hasPreviousVerticalSep,
		final int level, boolean createEntireExpansion)
	{

		boolean haveExpansionEditors = true;
		List<EObject> finalMultiInputList = new ArrayList<EObject>();
		// no editors to expand yet
		if (expansionEditors.size() == 0)
		{
			haveExpansionEditors = false;

			final List<EObject> multiInputList = (List<EObject>) currentModelFragmExpansionEditor.getExpansionInput();

			Composite multiExpansionComposite = null;

			if (!createEntireExpansion)
			{
				// the existing expansion will be kept. controls for new input
				// will be added at the end (for add new value button)

				boolean found = false;
				if (compositeForExpansion.getData() != null
					&& compositeForExpansion.getData().equals(MULTI_EXPANSION_COMPOSITE_DATA))
				{
					found = true;
					multiExpansionComposite = compositeForExpansion;
				}
				if (!found)
				{
					Control[] children = compositeForExpansion.getChildren();
					for (Control child: children)
					{
						if (child instanceof Composite && child.getData() != null
							&& child.getData().equals(MULTI_EXPANSION_COMPOSITE_DATA))
						{
							multiExpansionComposite = (Composite) child;
							break;
						}
					}
				}

				EObject lastAddedInput = multiInputList.get(multiInputList.size() - 1);
				finalMultiInputList.add(lastAddedInput);

				// delete the horizontal separator, it will be recreated after
				// the addition of the new input
				Control[] children = multiExpansionComposite.getChildren();
				Control sep = children[children.length - 1];
				if (sep instanceof Canvas)
				{
					sep.dispose();
					sep = null;
				}
			}

			else
			{
				finalMultiInputList.addAll(multiInputList);
				// add vertical borders
				if (hasPreviousVerticalSep)
				{
					for (int i = 0; i < level; i++)
					{
						createVerticalSeparator(compositeForExpansion, SWT.COLOR_BLUE);
					}
				}
				multiExpansionComposite = toolKit.createPlainComposite(compositeForExpansion, SWT.None);
				multiExpansionComposite.setData(MULTI_EXPANSION_COMPOSITE_DATA);
				configureLayoutAndLayoutData(multiExpansionComposite, 6, 5);

				// add vertical borders
				createVerticalSeparator(multiExpansionComposite, SWT.COLOR_BLUE);

				// place buttons
				Composite actionsComposite = toolKit.createPlainComposite(multiExpansionComposite, SWT.None);
				configureLayoutAndLayoutData(actionsComposite, 2, 5);

				// create the add/delete buttons
				ToolBar buttonsToolBar = new ToolBar(actionsComposite, SWT.FLAT | SWT.RIGHT);
				buttonsToolBar.setBackground(actionsComposite.getDisplay().getSystemColor(SWT.COLOR_GRAY));
				GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
				buttonsToolBar.setLayoutData(gd);

				final ToolItem addToolItem = new ToolItem(buttonsToolBar, SWT.PUSH);
				addToolItem.setToolTipText(ADD_VALUE_EXPANSION);
				addToolItem.setImage(ADD_MULTI_INPUT_IMAGE);
				addToolItem.setText(ADD_VALUE_EXPANSION);

				new ToolItem(buttonsToolBar, SWT.SEPARATOR);
				final ToolItem deleteToolItem = new ToolItem(buttonsToolBar, SWT.PUSH);
				deleteToolItem.setToolTipText(DELETE_ALL_EXPANSION);
				deleteToolItem.setImage(DELETE_MULTI_INPUT_IMAGE);
				deleteToolItem.setText(DELETE_ALL_EXPANSION);

				// handle selections
				handleSelection(deleteToolItem, new Runnable()
				{

					public void run()
					{
						multiInputList.clear();
					}
				}, input, DELETE_ALL_EXPANSION, multiExpansionComposite, currentModelFragmExpansionEditor, toolKit,
					expansionEditors, hasPreviousVerticalSep, level);

				handleSelection(
					addToolItem,
					new Runnable()
					{

						public void run()
						{
							EObject newExpansionInputMember = currentModelFragmExpansionEditor.createExpansionInputMember();
							multiInputList.add(newExpansionInputMember);

						}
					}, input, ADD_VALUE_EXPANSION, multiExpansionComposite, currentModelFragmExpansionEditor, toolKit,
					expansionEditors, hasPreviousVerticalSep, level);
			}

			// create controls for each input
			for (EObject multiInput: finalMultiInputList)
			{
				int l = level;
				createVerticalSeparator(multiExpansionComposite, SWT.COLOR_BLUE);
				handleExpansion(null, expansionEditors, input, multiInput, currentModelFragmExpansionEditor, toolKit,
					multiExpansionComposite, true, ++l);
			}
			// add horizontal border
			createHorizontalSeparator(multiExpansionComposite, 6 + level, SWT.COLOR_BLUE, 5);
		}
		return haveExpansionEditors;

	}

	/**
	 * Adds a listener to the handle the selection on the toolItem depending on the label text.
	 *
	 * @param runnable
	 * @param label
	 *        - selected widget label
	 * @param multiExpansionComposite
	 * @param expansionEditors
	 * @param level
	 * @param hasPreviousVerticalSep
	 */
	private void handleSelection(ToolItem toolItem, final Runnable runnable, final EObject input, final String label,
		final Composite multiExpansionComposite, final IModelFragmentEditorExpansion currentModelFragmExpansionEditor,
		final CessarFormToolkit toolkit, final List<IModelFragmentEditor> expansionEditors,
		final boolean hasPreviousVerticalSep, final int level)
	{

		toolItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				handleWidgetSelectionByLabel(runnable, input, label, multiExpansionComposite,
					currentModelFragmExpansionEditor, toolkit, expansionEditors, hasPreviousVerticalSep, level);
			}
		});
	}

	/**
	 * Method that handles the widgets depending on the provided label text
	 *
	 * @param toolItem
	 * @param runnable
	 * @param input
	 * @param label
	 *        - depending on its value different actions are taken
	 * @param multiExpansionComposite
	 *        - parent composite of the widgets that are handled
	 * @param currentModelFragmExpansionEditor
	 * @param toolkit
	 * @param expansionEditors
	 * @param hasPreviousVerticalSep
	 * @param level
	 */
	private void handleWidgetSelectionByLabel(final Runnable runnable, final EObject input, final String label,
		final Composite multiExpansionComposite, final IModelFragmentEditorExpansion currentModelFragmExpansionEditor,
		final CessarFormToolkit toolkit, final List<IModelFragmentEditor> expansionEditors,
		final boolean hasPreviousVerticalSep, final int level)
	{
		try
		{
			parent.setRedraw(false);
			WorkspaceTransactionUtil.executeInWriteTransaction(WorkspaceEditingDomainUtil.getEditingDomain(input),
				runnable, label);
			Control[] children = multiExpansionComposite.getChildren();
			if (label.equals(DELETE_ALL_EXPANSION))
			{

				for (int i = 2; i < children.length - 1; i++)
				{
					// i from 1, skipping the buttons for add new/delete
					// all
					children[i].dispose();
					children[i] = null;
				}

			}
			else if (label.equals(ADD_VALUE_EXPANSION))
			{
				handleExpansionForMultiEditor(toolkit, multiExpansionComposite, currentModelFragmExpansionEditor,
					expansionEditors, input, hasPreviousVerticalSep, level, false);

			}
			else if (label.equals(DELETE_VALUE_EXPANSION))
			{

				List<Control> children1 = Arrays.asList(multiExpansionComposite.getParent().getChildren());
				Label firstLabel = (Label) multiExpansionComposite.getChildren()[0];

				List<Label> labels = editorToLabelsMap.get(currentModelFragmExpansionEditor.getMasterEditor());
				int stopIndex = labels.indexOf(firstLabel);

				if (labels.size() > stopIndex + 1)
				{
					Label nextLabel = labels.get(stopIndex + 1);
					stopIndex = getIndexOfWidgetComposite(nextLabel, multiExpansionComposite.getParent());
				}
				else
				{
					stopIndex = children1.size();
				}

				// search the parent composite of the delete button
				int startIndex = getIndexOfWidgetComposite(firstLabel, multiExpansionComposite.getParent());
				if (stopIndex == children1.size())
				{
					startIndex = startIndex - 2;
				}
				else
				{
					startIndex = startIndex - 1;
				}
				for (int i = startIndex; i < stopIndex - 1; i++)
				{
					Control current = children1.get(i);
					current.dispose();
					current = null;
				}

			}
			parent.layout(true, true);
			parent.setRedraw(true);

			// tabbedPropertySheetPage.resizeScrolledComposite();
			pageListener.facilityPageChanged();
			changedModelEditorList.add(currentModelFragmExpansionEditor.getMasterEditor());

		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Create contents for this editor on the provided composite
	 *
	 * @param editor
	 * @param composite
	 */
	private void createEditorContents(IModelFragmentEditor editor, Composite composite)
	{
		Control control = editor.getPart(EEditorPart.CAPTION).createContents(composite);
		GridData gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		control.setLayoutData(gd);

		// rationale: increasing the number of columns inside the main composite will break the layout of
		// hierarchical editors
		Composite resourcesAndValidationComposite = formToolkit.createComposite(composite);
		GridLayout gl = new GridLayout(2, false);

		resourcesAndValidationComposite.setLayout(gl);
		resourcesAndValidationComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));

		control = editor.getPart(EEditorPart.RESOURCES).createContents(resourcesAndValidationComposite);
		gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		control.setLayoutData(gd);

		control = editor.getPart(EEditorPart.VALIDATION).createContents(resourcesAndValidationComposite);
		gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		control.setLayoutData(gd);

		control = editor.getPart(EEditorPart.ACTION).createContents(composite);
		gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		control.setLayoutData(gd);

		control = editor.getPart(EEditorPart.EDITING_AREA).createContents(composite);
		Object data = control.getLayoutData();
		if (!(data instanceof GridData))
		{
			gd = new GridData(SWT.FILL, SWT.TOP, true, false);
			gd.widthHint = 200;
			control.setLayoutData(gd);
		}

	}

	/**
	 * Dispose all children of this composite
	 *
	 * @param composite
	 */
	private void disposeChildrenForComposite(Composite composite)
	{

		if (composite != null)
		{
			Control[] children = composite.getChildren();
			for (Control child: children)
			{
				child.dispose();
			}
		}
	}

	/**
	 *
	 * @param toolKit
	 * @param compositeForExpansion
	 * @param expansionEditors
	 * @param expansionInput
	 * @param hasPreviousVerticalSep
	 * @param level
	 */
	private void createEditorsForExpansion(final CessarFormToolkit toolKit, final Composite compositeForExpansion,
		final List<IModelFragmentEditor> expansionEditors, final EObject expansionInput,
		final boolean hasPreviousVerticalSep, final int level)
	{
		for (IModelFragmentEditor expansionEditor: expansionEditors)
		{
			// add vertical borders
			if (hasPreviousVerticalSep)
			{
				createVerticalSeparator(compositeForExpansion, SWT.COLOR_BLUE);
			}
			createVerticalSeparator(compositeForExpansion, SWT.COLOR_BLUE);
			handleExpansion(expansionEditor, expansionEditors, expansionInput, null, null, toolKit,
				compositeForExpansion, true, level);
		}

		// add horizontal border
		int horizontalIndent = hasPreviousVerticalSep ? 5 * (level + 1) : 5;
		createHorizontalSeparator(compositeForExpansion, /* (hasPreviousVerticalSep==true?(4+level):(5 + level)) */
			6 + level, SWT.COLOR_BLUE, horizontalIndent);
		if (hasPreviousVerticalSep)
		{
			createVerticalSeparator(compositeForExpansion, SWT.COLOR_BLUE);
		}
		Label separator = new Label(compositeForExpansion, SWT.None);
		GridData gd = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
		gd.horizontalSpan = hasPreviousVerticalSep ? (4 + level) : (5 + level);
		gd.heightHint = 5;
		separator.setLayoutData(gd);

	}

	/**
	 * Create vertical separator on this composite
	 *
	 * @param composite
	 */
	private void createVerticalSeparator(Composite composite, int color)
	{
		Canvas verticalSeparator = new Canvas(composite, SWT.None);
		// color = SWT.COLOR_BLUE;
		verticalSeparator.setBackground(verticalSeparator.getDisplay().getSystemColor(color));
		GridData gd = new GridData(SWT.LEFT, SWT.FILL, false, true);
		gd.widthHint = 1;
		gd.heightHint = 1;
		gd.horizontalIndent = 5;
		verticalSeparator.setLayoutData(gd);
	}

	/**
	 * Create horizontal separator on this composite
	 *
	 * @param composite
	 *
	 */
	private void createHorizontalSeparator(Composite composite, int horizontalSpan, int color, int horizontalIndent)
	{
		Canvas horizontalSeparator = new Canvas(composite, SWT.None);
		// color = SWT.COLOR_BLUE;
		horizontalSeparator.setBackground(horizontalSeparator.getDisplay().getSystemColor(color));
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.horizontalIndent = horizontalIndent;
		gd.verticalIndent = 0;
		gd.horizontalSpan = horizontalSpan;
		gd.heightHint = 1;
		gd.widthHint = 1;
		horizontalSeparator.setLayoutData(gd);
	}

	/**
	 * Returns the index of the composite containing this widget in the list of parent s children
	 *
	 * @param widget
	 * @param parentComposite
	 * @return
	 */
	private int getIndexOfWidgetComposite(Widget widget, Composite parentComposite)
	{
		List<Control> children1 = Arrays.asList(parentComposite.getChildren());

		// search the parent composite of the delete button
		for (Control child: children1)
		{
			if (child instanceof Composite)
			{
				List<Control> children2 = Arrays.asList(((Composite) child).getChildren());
				if (!children2.isEmpty() && children2.contains(widget))
				{
					return children1.indexOf(child);
				}
			}

		}
		return -1;
	}

	/**
	 * Disposes the main composite that holds the editors. It allows to clean the widgets and the class can be
	 * reinitialized calling {@link #setInput(EObject)} method
	 */
	private void cleanUpWidgets()
	{
		for (final IModelFragmentEditor editor: editorsList)
		{
			ISafeRunnable runnable = new SafeRunnable()
			{
				public void run() throws Exception
				{
					editor.removeModelEditorChangeListener(EditingFacilityComposite.this);
					editor.dispose();
				}
			};
			SafeRunnable.run(runnable);
		}
		editorsList.clear();
		if (editorComposite != null)
		{
			editorComposite.dispose();
			editorComposite = null;
		}
		changedModelEditorList.clear();
		editorToLabelsMap.clear();
		editorToToolItemMap.clear();
	}

}
