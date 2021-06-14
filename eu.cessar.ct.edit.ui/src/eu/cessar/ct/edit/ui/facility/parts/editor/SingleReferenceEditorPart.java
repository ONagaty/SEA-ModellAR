/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Mar 19, 2010 11:45:54 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
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
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.events.EFocusEvent;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;
import eu.cessar.ct.edit.ui.dialogs.IChooseReferenceHandler;
import eu.cessar.ct.edit.ui.dialogs.ISetReferenceTextHandler;
import eu.cessar.ct.edit.ui.dialogs.ReferenceSelectionDialog;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextSingleFeatureEditingManager;
import eu.cessar.ct.edit.ui.instanceref.IReferenceLabelProvider;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * @author uidl6870
 *
 */
public class SingleReferenceEditorPart extends AbstractFeatureEditorPart implements IEditorPart
{
	private static final String MISSING_TARGET_MSG = "[MISSING] "; //$NON-NLS-1$
	private static final String NOT_FOUND_RESOURCE_MSG = "[RESOURCE NOT FOUND] "; //$NON-NLS-1$
	private Text text;
	private Button chooseButton;
	private ILabelProvider treeLabelProvider;

	IModelFragmentFeatureEditor editor;

	/**
	 * @param editor
	 */
	public SingleReferenceEditorPart(IModelFragmentFeatureEditor editor)
	{
		this(editor, null);
		this.editor = editor;

	}

	/**
	 * @param editor
	 * @param treelableProvider
	 */
	public SingleReferenceEditorPart(IModelFragmentFeatureEditor editor, ILabelProvider treelableProvider)
	{
		super(editor);
		treeLabelProvider = treelableProvider;
		this.editor = editor;

	}

	private ISetReferenceTextHandler setRefHandler;
	private IChooseReferenceHandler chooseRefHandler;

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

		text = getFormToolkit().createText(composite, "", SWT.BORDER | SWT.BORDER); //$NON-NLS-1$
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		addFocusListener(text);
		addKeyListener(text);

		chooseButton = getFormToolkit().createButton(composite, "...", SWT.PUSH); //$NON-NLS-1$
		chooseButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		chooseButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				chooseRefHandler = new ChooseReferenceHandler();

				ReferenceSelectionDialog dialog = new ReferenceSelectionDialog(chooseButton.getShell(),
					chooseRefHandler);

				getEditor().deliverFocusLost(false);
				dialog.open();
				getEditor().deliverFocusLost(true);
			}
		});

		addFocusListener(chooseButton);
		// updateReadOnlyState();
		return composite;

	}

	private EObject getEObject(String pathEObject)
	{
		EObject value = null;
		try
		{

			IFile file = EcorePlatformUtil.getFile(getEditor().getInput());
			IProject project = file.getProject();
			value = ModelUtils.getEObjectWithQualifiedName(project, pathEObject);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return value;
	}

	/**
	 * textbox with reference is editable
	 *
	 * On focus lost , check if the current value of textbox is a valid reference for the current object
	 *
	 * @param control
	 */
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

				if (control instanceof Text)
				{
					// get the current value of text after focus was lost
					// validate if the path represents a valid resource
					setRefHandler = new SetReferenceTextHandler();
					String textValue = ((Text) control).getText().toString();
					EObject value = getEObject(textValue);

					if (value != null)
					{
						// if value is valid , set the reference for the current object
						setRefHandler.setReferencedValue(value);
					}

					// if resource is not found , the reference will not be set
					else
					{

						if (textValue.startsWith(MISSING_TARGET_MSG) || textValue.startsWith(NOT_FOUND_RESOURCE_MSG))
						{
							// the error message is already set , continue
						}

						else if (!textValue.isEmpty())
						{
							// set error message (resource not found)
							text.setText(NOT_FOUND_RESOURCE_MSG + textValue);
							text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
						}

						else
						{
							// if text was set to empty , delete the resource link
							EObject currentObject = getInputObject();
							Object currentFeature = getInputFeature();
							if (currentFeature != null)
							{
								remove(new Runnable()
								{
									public void run()
									{
										currentObject.eUnset(getInputFeature());
									}
								}, Messages.Command_removeValue);

							}

						}

					}

				}

			}

			public void focusGained(FocusEvent e)
			{
				String textValue = text.getText();

				if (textValue.startsWith(NOT_FOUND_RESOURCE_MSG))
				{
					text.setText(textValue.substring(NOT_FOUND_RESOURCE_MSG.length(), textValue.length()));
				}
			}
		});
	}

	private void remove(Runnable runnable, String command_removeValue)
	{
		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(
				WorkspaceEditingDomainUtil.getEditingDomain(editor.getInput()), runnable, command_removeValue);
		}
		catch (OperationCanceledException | ExecutionException e)
		{
			e.printStackTrace();
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

	/**
	 * @return
	 */
	private ILabelProvider getLabelProvider()
	{
		return ((IModelFragmentReferenceEditor) getEditor()).getLabelProvider();
	}

	private class SetReferenceTextHandler implements ISetReferenceTextHandler
	{

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.edit.ui.dialogs.ISetReferenceTextHandler#setReferencedValue(java.lang.Object)
		 */
		@Override
		public void setReferencedValue(final Object value)
		{
			performChangeWithChecks(new Runnable()
			{
				/*
				 * (non-Javadoc)
				 *
				 * @see java.lang.Runnable#run()
				 */
				public void run()
				{
					applyValue(value);
				}
			}, "Updating data..."); //$NON-NLS-1$

			refresh();
		}

		public Object getReferencedValue()
		{
			return getInputObject().eGet(getInputFeature());
		}

	}

	private class ChooseReferenceHandler implements IChooseReferenceHandler
	{

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.edit.ui.dialogs.IChooseReferenceHandler#getDialogTitle()
		 */
		public String getDialogTitle()
		{
			String title = getInputFeature().getName() + ": "; //$NON-NLS-1$
			EObject input = getInputObject();
			if (input instanceof GReferrable)
			{
				String shortName = ((GReferrable) input).gGetShortName();
				if (shortName != null)
				{
					title += shortName;
				}
			}
			return title;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.ui.properties.IChooseReferenceHandler#getReferencedValue()
		 */
		public Object getReferencedValue()
		{
			return getInputObject().eGet(getInputFeature());
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.ui.properties.IChooseReferenceHandler#setReferencedValue()
		 */
		public void setReferencedValue(final Object value)
		{
			performChangeWithChecks(new Runnable()
			{
				/*
				 * (non-Javadoc)
				 *
				 * @see java.lang.Runnable#run()
				 */
				public void run()
				{
					applyValue(value);
				}
			}, "Updating data..."); //$NON-NLS-1$

			refresh();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.ui.properties.IChooseReferenceHandler#getReference()
		 */
		public EReference getReference()
		{
			return (EReference) getInputFeature();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.ui.properties.IChooseReferenceHandler#getCandidates()
		 */
		public java.util.List<Object> getCandidates()
		{
			// return SingleReferenceEditorPart.this.getCandidates();
			return ((IModelFragmentReferenceEditor) getEditor()).getCandidates();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.ui.properties.IChooseReferenceHandler#getTreeLabelProvider()
		 */
		public ILabelProvider getTreeLabelProvider()
		{
			return SingleReferenceEditorPart.this.getTreeLabelProvider();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see eu.cessar.ct.edit.ui.dialogs.IChooseReferenceHandler#getInputObject()
		 */
		public EObject getInputObject()
		{
			return SingleReferenceEditorPart.this.getInputObject();
		}
	}

	/**
	 * If the editor's input is not of Splitable type, applies the given <code>value</code> on the input's edited
	 * feature. Otherwise, applies the value on the relevant fragments of the input's wrapped element.
	 *
	 * @param value
	 */
	private void applyValue(Object value)
	{
		boolean splitableInput = getEditor().isInputSplitable();
		if (!splitableInput)
		{
			setReferenceValue(getInputObject(), value);
		}
		else
		{
			ISplitableContextSingleFeatureEditingManager editorManager = getEditor().getSplitableContextEditingManager();
			List<EObject> fragmentsInScope = editorManager.getStrategy().getFragmentsInScope();
			for (EObject eObject: fragmentsInScope)
			{
				setReferenceValue(eObject, value);
			}
		}
	}

	/**
	 * @param input
	 *        feature's owner
	 * @param newValue
	 *        the new value for the edited feature
	 */
	private void setReferenceValue(final EObject input, final Object newValue)
	{
		Object oldValue = input.eGet(getInputFeature());
		boolean equal = true;
		if (newValue != null)
		{
			equal = (oldValue == null) ? false : newValue.equals(oldValue);
		}

		if (!equal)
		{
			input.eSet(getInputFeature(), newValue);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	public void refresh()
	{
		updateReadOnlyState();
		Object featureValue;
		EObject inputObject = getInputObject();
		if (inputObject != null)
		{
			featureValue = inputObject.eGet(getInputFeature());
		}
		else
		{
			featureValue = null;
		}
		if (text != null && !text.isDisposed())
		{

			IStatus contentVisibility = getEditor().getEditingAreaContentVisibility();
			boolean ok = contentVisibility.isOK();

			// if content not visible, disable editor and return
			text.setEnabled(ok);
			if (!ok)
			{
				text.setText(""); //$NON-NLS-1$
				return;
			}

			text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			if (featureValue != null)
			{
				if (featureValue instanceof GReferrable)
				{
					setTextForGReferrable(featureValue);
					// set the toolTip
					ILabelProvider labelProvider = getLabelProvider();
					setToolTip(featureValue, labelProvider);
				}
				else
				{
					// String value = getLabelProvider().getText(featureValue);
					ILabelProvider labelProvider = getLabelProvider();
					String value = labelProvider.getText(featureValue);
					text.setText(value);
					// set the toolTip
					setToolTip(featureValue, labelProvider);
				}
			}
			else
			{
				text.setText(""); //$NON-NLS-1$
			}
		}
	}

	private void setToolTip(Object featureValue, ILabelProvider labelProvider)
	{
		if (labelProvider instanceof IReferenceLabelProvider)
		{
			List<Object> list = new ArrayList<Object>();
			list.add(featureValue);
			@SuppressWarnings({"rawtypes", "unchecked"})
			String tooltip = ((IReferenceLabelProvider) labelProvider).getTooltip(list);
			text.setToolTipText(tooltip);
		}
	}

	private void setTextForGReferrable(Object featureValue)
	{
		String stext = MetaModelUtils.getAbsoluteQualifiedName((EObject) featureValue);
		if (((GReferrable) featureValue).eIsProxy())
		{
			stext = MISSING_TARGET_MSG + stext;
			text.setText(stext);
			text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
		else
		{
			text.setText(stext);
		}
	}

	/**
	 *
	 */
	private void updateReadOnlyState()
	{
		if (chooseButton != null && !chooseButton.isDisposed())
		{
			chooseButton.setEnabled(getEditor().getEditableStatus().isOK());
		}

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
		String text = ""; //$NON-NLS-1$

		if (getInputObject() == null)
		{
			return text;
		}
		Object featureValue = getInputObject().eGet(getInputFeature());
		if (featureValue != null)
		{
			if (featureValue instanceof GReferrable)
			{
				text = MetaModelUtils.getAbsoluteQualifiedName((EObject) featureValue);
			}
			else
			{
				text = getLabelProvider().getText(featureValue);
			}
		}
		return text;
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

		if (chooseButton != null && !chooseButton.isDisposed())
		{
			chooseButton.setEnabled(enabled);
		}

	}

}
