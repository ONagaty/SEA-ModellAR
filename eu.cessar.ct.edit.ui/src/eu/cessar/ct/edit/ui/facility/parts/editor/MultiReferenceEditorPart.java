/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Mar 19, 2010 4:06:14 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.platform.ui.dialogs.CessarTreeRegexViewerFilter;
import eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler;
import eu.cessar.ct.core.platform.ui.dialogs.MultiValueDialog;
import eu.cessar.ct.core.platform.ui.dialogs.ViewerFiltering;
import eu.cessar.ct.core.platform.ui.events.EFocusEvent;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.edit.ui.dialogs.IReferenceMultiValueHandler;
import eu.cessar.ct.edit.ui.dialogs.TreeReferencesContentProvider;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.instanceref.IReferenceLabelProvider;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.sdk.IPostBuildContext;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 *
 */
public class MultiReferenceEditorPart extends AbstractFeatureEditorPart implements IEditorPart
{
	/**
	 *
	 */
	private static final String STR_COMMA = ","; //$NON-NLS-1$

	/**
	 *
	 */
	private static final String STR_COMMA_SPACE = ", "; //$NON-NLS-1$

	private static final String MISSING_TARGET_MSG = "[MISSING] "; //$NON-NLS-1$

	private Text text;
	private Button browseBtn;

	/** editor inside MultiValueDialog */
	private TreeViewer treeViewer;

	private TreeReferencesContentProvider treeContentProvider;
	private ILabelProvider treeLabelProvider;

	private MultiValueHandler multiValueHandler = null;
	private ViewerFiltering viewerFiltering;
	private MultiValueDialog<EObject> dialog = null;
	private CessarFormToolkit toolkit;

	/**
	 * @param editor
	 */
	public MultiReferenceEditorPart(IModelFragmentFeatureEditor editor)
	{
		this(editor, null);
	}

	/**
	 * @param editor
	 * @param treelabelProvider
	 */
	public MultiReferenceEditorPart(IModelFragmentFeatureEditor editor, ILabelProvider treelabelProvider)
	{
		super(editor);
		treeLabelProvider = treelabelProvider;
	}

	/**
	 * @return
	 */
	private ILabelProvider getLabelProvider()
	{
		return ((IModelFragmentReferenceEditor) getEditor()).getLabelProvider();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		Composite composite = getFormToolkit().createComposite(parent);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout(gridLayout);

		text = getToolkit().createText(composite, "", SWT.BORDER | SWT.MULTI | SWT.READ_ONLY); //$NON-NLS-1$
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		addFocusListener(text);
		addKeyListener(text);

		browseBtn = getToolkit().createButton(composite, "...", SWT.PUSH); //$NON-NLS-1$
		browseBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		browseBtn.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				launchMultiValueDialog();
			}
		});
		addFocusListener(browseBtn);

		// updateReadOnlyState();
		return composite;
	}

	private void addFocusListener(Control control)
	{
		control.addFocusListener(new FocusListener()
		{
			public void focusLost(FocusEvent e)
			{
				IFocusEventListener eventListener = getEditor().getEventListener();
				if (eventListener != null)
				{
					eventListener.notify(EFocusEvent.FOCUS_OUT);
				}
			}

			public void focusGained(FocusEvent e)
			{
				// do nothing
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart#setFocus()
	 */
	@Override
	public void setFocus()
	{
		if (text != null && !text.isDisposed())
		{
			text.setFocus();
			text.selectAll();
		}

	}

	private void launchMultiValueDialog()
	{
		dialog = new MultiValueDialog<>(getShell(), getMultiValueDialogHandler());

		List<Object> handlerCandidates = multiValueHandler.getCandidates();
		viewerFiltering = new ViewerFiltering(new MultiRefViewerFilter());
		dialog.setInputViewerFiltering(viewerFiltering);
		dialog.setPossibleCandidates(handlerCandidates);

		getEditor().deliverFocusLost(false);
		dialog.open();
		getEditor().deliverFocusLost(true);
	}

	private IMultiValueHandler<EObject> getMultiValueDialogHandler()
	{
		if (multiValueHandler == null)
		{
			multiValueHandler = new MultiValueHandler();
		}
		return multiValueHandler;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void refresh()
	{
		updateReadOnlyState();
		ILabelProvider labelProvider = getLabelProvider();
		if (text != null && !text.isDisposed() && labelProvider != null)
		{
			text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_BLACK));

			String multiValueStr = ""; //$NON-NLS-1$
			StringBuilder multiValue = new StringBuilder(multiValueStr);

			List<? extends Object> values = getValuesForEObject(getInputObject());
			if (values.size() > 0)
			{
				for (Object object: values)
				{
					boolean isProxy = object instanceof GIdentifiable && ((GIdentifiable) object).eIsProxy();
					constructMultiValue(multiValue, labelProvider.getText(object), isProxy);
					if (isProxy)
					{
						text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
					}
				}
				multiValueStr = multiValue.substring(0, multiValue.lastIndexOf(STR_COMMA));
			}
			if (labelProvider instanceof IReferenceLabelProvider)
			{
				String tooltip = ((IReferenceLabelProvider) labelProvider).getTooltip(values);
				tooltip = shortOutputText(tooltip, 200);

				text.setToolTipText(tooltip);
			}
			multiValueStr = shortOutputText(multiValueStr, 255);
			text.setText(multiValueStr);

		}

	}

	/**
	 * @param multiValueStr
	 * @return
	 */
	private String shortOutputText(String multiValueStr, int size)
	{
		int length = multiValueStr.length();
		if (length > size)
		{
			String substring = multiValueStr.substring(0, size);
			String concat = substring.concat("..."); //$NON-NLS-1$
			multiValueStr = concat;
		}
		return multiValueStr;
	}

	private static StringBuilder constructMultiValue(StringBuilder builder, String content, boolean isProxy)
	{
		if (isProxy)
		{
			builder.append(MISSING_TARGET_MSG);
		}
		builder.append(content);
		builder.append(STR_COMMA_SPACE);

		return builder;
	}

	/**
	 *
	 */
	private void updateReadOnlyState()
	{
		if (browseBtn != null && !browseBtn.isDisposed())
		{
			browseBtn.setEnabled(getEditor().getEditableStatus().isOK());
		}
	}

	private Control createEditorForMultiValueDialog(final Composite parent)
	{
		Tree tree = new Tree(parent, SWT.BORDER | SWT.MULTI);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		treeViewer = new TreeViewer(tree);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				// check if a tree selection is send through the event
				if (!(event.getSelection() instanceof TreeSelection))
				{
					dialog.setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
					dialog.enableOrDisableAddButton();
					return;
				}

				// extract selected object from tree selection object
				TreeSelection treeSelection = (TreeSelection) event.getSelection();
				Object selectedObject = treeSelection.getFirstElement();
				if ((selectedObject == null) || !(selectedObject instanceof EObject))
				{
					dialog.setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
					dialog.enableOrDisableAddButton();
					return;
				}

				// If we decide to check and do not allow the add for duplicate objects this part needs to be
				// uncommented
				// if (multiValueHandler.getCandidates().contains(selectedObject))
				// {
				// dialog.setErrorMessage(null);
				// dialog.enableOrDisableAddButton(selectedObject);
				// }
				// else
				// {
				// dialog.setErrorMessage(Messages.ReferenceSelectionDialog_selected_not_Valid);
				// dialog.enableOrDisableAddButton();
				// }
			}
		});

		treeViewer.setLabelProvider(getTreeLabelProvider());
		treeViewer.setContentProvider(getTreeContentProvider());

		List<Object> candidates = ((IModelFragmentReferenceEditor) getEditor()).getCandidates();
		treeViewer.setInput(candidates);

		treeViewer.expandAll();

		if (viewerFiltering != null)
		{
			viewerFiltering.setInput(treeViewer);
		}

		return tree;
	}

	private IContentProvider getTreeContentProvider()
	{
		// we cannot reuse the same provider each time the dialog is opened
		treeContentProvider = new TreeReferencesContentProvider();
		return treeContentProvider;
	}

	/**
	 * Create and return the label provider for the tree viewer inside ReferenceSelectionDialog
	 *
	 * @return
	 */
	private ILabelProvider getTreeLabelProvider()
	{
		if (treeLabelProvider == null)
		{
			TransactionalEditingDomain editingDomain = getEditingDomain();

			treeLabelProvider = new TransactionalAdapterFactoryLabelProvider(editingDomain,
				((AdapterFactoryEditingDomain) editingDomain).getAdapterFactory());

		}
		return treeLabelProvider;
	}

	private class MultiValueHandler implements IReferenceMultiValueHandler<EObject>
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#createSingleValueEditor(org.eclipse.swt.widgets.
		 * Composite)
		 */
		@Override
		public Control createSingleValueEditor(Composite parent)
		{
			return createSingleValueEditor(parent, false, null);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#createSingleValueEditor(org.eclipse.swt.widgets.
		 * Composite, boolean, eu.cessar.ct.sdk.IPostBuildContext)
		 */
		@Override
		public Control createSingleValueEditor(Composite parent, boolean isPBFiltering, IPostBuildContext pBContext)
		{
			return createEditorForMultiValueDialog(parent);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getEditorValue()
		 */
		@Override
		public List<EObject> getEditorValue()
		{
			return MultiReferenceEditorPart.this.getEditorValue();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#setEditorValue(java.lang.Object)
		 */
		@Override
		public void setEditorValue(EObject value)
		{
			MultiReferenceEditorPart.this.setEditorValue(value);
		}

		public void clearEditorValue()
		{
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getValues()
		 */
		@Override
		public List<EObject> getValues()
		{
			List<Object> list = getValuesForEObject(getInputObject());
			return (List<EObject>) (List<?>) list;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#acceptValues(java.util.List)
		 */
		@Override
		public boolean acceptValues(List<EObject> newValues)
		{
			applyValues(newValues);
			refresh();
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getMaxValues()
		 */
		@Override
		public int getMaxValues()
		{
			return getInputFeature().getUpperBound();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#setInputObject(org.eclipse.emf.ecore.EObject)
		 */
		@Override
		public void setInputObject(EObject object)
		{
			getEditor().setInput(object);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getInputObject()
		 */
		@Override
		public EObject getInputObject()
		{
			return MultiReferenceEditorPart.this.getInputObject();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getTypeName(boolean)
		 */
		@Override
		public String getTypeName(boolean plural)
		{
			return getInputFeature().getName();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#computeCandidateList(boolean,
		 * eu.cessar.ct.sdk.IPostBuildContext)
		 */
		@Override
		public Control computeCandidateList(boolean isPBFiltering, IPostBuildContext pBContext)
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.edit.ui.dialogs.IReferenceMultiValueHandler#getCandidates()
		 */
		@Override
		public List<Object> getCandidates()
		{
			return ((IModelFragmentReferenceEditor) getEditor()).getCandidates();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getLabelProvider()
		 */
		@Override
		public ILabelProvider getLabelProvider()
		{
			return MultiReferenceEditorPart.this.getLabelProvider();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.edit.ui.dialogs.IReferenceMultiValueHandler#getTreeLabelProvider()
		 */
		@Override
		public ILabelProvider getTreeLabelProvider()
		{
			return MultiReferenceEditorPart.this.getTreeLabelProvider();
		}
	}

	/**
	 * If the editor's input is not of Splitable type, applies the given <code>values</code> on the input's edited
	 * feature. TODO: Otherwise, applies the values on the relevant fragments of the input's wrapped element.
	 *
	 * @param values
	 */
	private void applyValues(List<? extends Object> values)
	{
		boolean splitableInput = getEditor().isInputSplitable();
		if (!splitableInput)
		{
			executeSetNewData(getInputObject(), values);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.ui.internal.properties.defaults.Abstract4PartSection#setEditorValue(java.lang.Object)
	 */
	private void setEditorValue(Object value)
	{
		if (value != null)
		{
			treeViewer.setSelection(new StructuredSelection(value), true);
		}

	}

	private List<EObject> getEditorValue()
	{
		List<EObject> returnList = new ArrayList<>();

		ISelection selection = treeViewer.getSelection();
		if (!(selection instanceof StructuredSelection))
		{
			return null;
		}
		else
		{
			StructuredSelection structuredSelection = (StructuredSelection) selection;
			if (structuredSelection.size() > 1)
			{
				List<?> list = structuredSelection.toList();
				for (Object object: list)
				{
					List<?> possibleCandidates = ((IModelFragmentReferenceEditor) getEditor()).getCandidates();
					if (possibleCandidates.contains(object))
					{
						returnList.add((EObject) object);
					}
				}
			}
			else
			{
				Object firstElement = structuredSelection.getFirstElement();

				List<?> possibleCandidates = ((IModelFragmentReferenceEditor) getEditor()).getCandidates();
				if (possibleCandidates.contains(firstElement))
				{
					returnList.add((EObject) firstElement);
				}
			}
		}
		return returnList;
	}

	/**
	 * Return a list with the values of the edited feature of the given <code>eObj</code>. The list could be empty,
	 * never null
	 *
	 * @return
	 */
	private List<Object> getValuesForEObject(EObject eObj)
	{
		List<Object> list = new ArrayList<>();
		if (getInputObject() != null && getInputFeature() != null)
		{
			Object object = eObj.eGet(getInputFeature());
			if (object instanceof EList<?>)
			{
				list.addAll((List<?>) object);
				return list;
			}
		}
		return list;
	}

	/**
	 * Sets the edited feature of the given <code>input</code> with the specified <code>values</code>, by executing a
	 * remove command followed by a set command
	 *
	 * @param values
	 */
	private void executeSetNewData(EObject input, List<? extends Object> values)
	{
		EStructuralFeature feature = getInputFeature();
		EditingDomain editingDomain = getEditingDomain();

		List<? extends Object> oldValues = getValuesForEObject(input);

		if (oldValues.size() > 0)
		{
			Command command = RemoveCommand.create(editingDomain, input, feature, oldValues);
			if (command.canExecute())
			{
				editingDomain.getCommandStack().execute(command);
			}
		}
		if (values != null && values.size() > 0)
		{
			Command command = SetCommand.create(editingDomain, input, feature, values);
			if (command.canExecute())
			{
				editingDomain.getCommandStack().execute(command);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return
	 */
	private Shell getShell()
	{
		if (text != null)
		{
			return text.getShell();
		}
		else
		{
			return PlatformUI.getWorkbench().getDisplay().getActiveShell();
		}
	}

	/**
	 * @return the toolkit
	 */
	public CessarFormToolkit getToolkit()
	{
		if (toolkit == null)
		{
			toolkit = new CessarFormToolkit(Display.getCurrent());
		}
		return toolkit;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		ILabelProvider labelProvider = getLabelProvider();
		if (labelProvider != null)
		{
			String multiValue = ""; //$NON-NLS-1$
			List<? extends Object> values = getValuesForEObject(getInputObject());
			if (values.size() > 0)
			{
				for (Object object: values)
				{
					multiValue += labelProvider.getText(object);
					multiValue += STR_COMMA_SPACE;
				}
				StringBuffer strBuf = new StringBuffer(multiValue);
				multiValue = strBuf.substring(0, strBuf.lastIndexOf(STR_COMMA));

			}
			return multiValue;
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		if (text != null && !text.isDisposed())
		{
			text.setEnabled(enabled);
		}

		if (browseBtn != null && !browseBtn.isDisposed())
		{
			browseBtn.setEnabled(enabled);
		}

	}

	private void addKeyListener(Control ctrl)
	{
		ctrl.addKeyListener(new KeyListener()
		{

			public void keyReleased(KeyEvent e)
			{
				if (e.character == SWT.CR)
				{
					IFocusEventListener eventListener = getEditor().getEventListener();
					if (eventListener != null)
					{
						eventListener.notify(EFocusEvent.CR);
					}
				}
			}

			public void keyPressed(KeyEvent e)
			{
				// do nothing

			}
		});
	}

	private class MultiRefViewerFilter extends CessarTreeRegexViewerFilter
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.core.platform.ui.dialogs.CessarViewerFilter#getLeafName(java.lang.Object)
		 */
		@Override
		protected String getLeafName(Object leaf)
		{
			return treeLabelProvider.getText(leaf);
		}
	}
}
