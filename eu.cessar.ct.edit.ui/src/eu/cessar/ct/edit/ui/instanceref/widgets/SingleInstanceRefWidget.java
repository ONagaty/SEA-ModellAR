/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 May 7, 2010 10:23:43 AM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import eu.cessar.ct.core.mms.internal.instanceref.impl.ContextType;
import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.instanceref.GenericIRefSelectionDialog;
import eu.cessar.ct.edit.ui.instanceref.IInstReferenceLabelProvider;
import eu.cessar.ct.edit.ui.instanceref.IReferenceLabelProvider;
import eu.cessar.ct.edit.ui.instanceref.SystemIRefFeatureWrapper;
import eu.cessar.ct.edit.ui.instanceref.Wrapper;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 * @param <T>
 *
 */

// Added @SuppressWarnings for solving the ContextType dependency on eu.cessar.ct.core.mms/internal package.
// This has to be solved in a dedicated refactoring (interfaces) action regarding the core.mms internal classes.
@Requirement(
	reqID = "REQ_EDIT_PROP#3")
@SuppressWarnings("restriction")
public class SingleInstanceRefWidget<T> extends AbstractInstancerefWidget<Wrapper<T>>
{
	private Text targetField;
	private Button chooseButton;
	private Label targetLabel;

	private CessarFormToolkit toolkit;

	private IChooseIRefHandler handler;
	private IInstReferenceLabelProvider<T> labelProvider;

	private Group contextGroup;
	private ContextWidget contextWidget;
	private Group targetGroup;
	private boolean firstInput;

	/**
	 * @author uidu3379
	 *
	 */
	private final class ChooseButtonSelectionListener extends SelectionAdapter
	{
		@Override
		public void widgetSelected(SelectionEvent e)
		{
			handler.init();
			try
			{
				handler.computeCandidates();
			}
			catch (InstanceRefConfigurationException ex)
			{
				editor.deliverFocusLost(false);

				MessageDialog.openError(chooseButton.getShell(), Messages.MessageDialog_error_title, ex.getMessage());

				editor.deliverFocusLost(true);
				return;
			}

			// ECUC instance reference
			if (!handler.isSystemInstanceRef())
			{
				handleECUCInstance();
			}
			else
			{
				// system instance reference
				handleSystemInstanceRef();
			}
		}

		private void handleECUCInstance()
		{
			if (handler.hasCandidatesForCompleteConfig() || handler.hasCandidatesForIncompleteConfig())
			{
				SingleInstanceRefWidget.this.editor.deliverFocusLost(false);

				GenericIRefSelectionDialog dialog = new GenericIRefSelectionDialog(
					Display.getCurrent().getActiveShell(), handler);

				dialog.open();
				SingleInstanceRefWidget.this.editor.deliverFocusLost(true);
			}
			else
			{
				SingleInstanceRefWidget.this.editor.deliverFocusLost(false);

				MessageDialog.openWarning(chooseButton.getShell(), Messages.MessageDialog_error_title,
					NLS.bind(Messages.MessageDialog_no_targets, handler.getDialogTitle()));

				SingleInstanceRefWidget.this.editor.deliverFocusLost(true);

			}
		}

		private void handleSystemInstanceRef()
		{
			boolean userPref = handler.areIncompleteConfigsPermitted();
			if (handler.hasCandidatesForCompleteConfig())
			{
				GenericIRefSelectionDialog dialog = new GenericIRefSelectionDialog(
					Display.getCurrent().getActiveShell(), handler);

				SingleInstanceRefWidget.this.editor.deliverFocusLost(false);
				dialog.open();
				SingleInstanceRefWidget.this.editor.deliverFocusLost(true);
			}
			else if (handler.hasCandidatesForIncompleteConfig())
			{
				if (userPref)
				{
					GenericIRefSelectionDialog dialog = new GenericIRefSelectionDialog(
						Display.getCurrent().getActiveShell(), handler);

					SingleInstanceRefWidget.this.editor.deliverFocusLost(false);
					dialog.open();
					SingleInstanceRefWidget.this.editor.deliverFocusLost(true);
				}
				else
				{
					StringBuffer msgBuffer = new StringBuffer();
					msgBuffer.append(Messages.NoCandidatesForCompleteConfig);
					msgBuffer.append("."); //$NON-NLS-1$
					msgBuffer.append(Messages.CandidatesForIncompleteConfig);

					// ask user weather to show them or not
					showReferenceSelectionDialog(msgBuffer);
				}
			}
			else
			{
				SingleInstanceRefWidget.this.editor.deliverFocusLost(false);
				MessageDialog.openWarning(chooseButton.getShell(), Messages.MessageDialog_error_title,
					NLS.bind(Messages.MessageDialog_no_targets, handler.getDialogTitle()));
				SingleInstanceRefWidget.this.editor.deliverFocusLost(true);

			}
		}

		private void showReferenceSelectionDialog(StringBuffer msgBuffer)
		{
			SingleInstanceRefWidget.this.editor.deliverFocusLost(false);

			boolean openConfirmed = MessageDialog.openQuestion(chooseButton.getShell(),
				Messages.ReferenceSelectionDialog_title, msgBuffer.toString());
			if (openConfirmed)
			{
				handler.setPermitIncompleteConfigs(true);
				GenericIRefSelectionDialog dialog = new GenericIRefSelectionDialog(
					Display.getCurrent().getActiveShell(), handler);
				dialog.open();
			}
			SingleInstanceRefWidget.this.editor.deliverFocusLost(true);
		}
	}

	private class ContextWidget
	{
		private Composite parentComposite;
		private List<Text> contextFieldList;
		private List<Label> contextLabelList;

		private boolean hasContextFieldTextSet[];

		/**
		 * @param fieldComposite
		 * @param string
		 */
		public ContextWidget(Composite fieldComposite)
		{
			this.parentComposite = fieldComposite;
			contextFieldList = new ArrayList<Text>();
			contextLabelList = new ArrayList<Label>();
		}

		public List<Text> getContextFields()
		{
			return contextFieldList;
		}

		public void create(IChooseIRefHandler refHandler)
		{
			// try
			// {
			// handler.computeCandidates();
			// }
			// catch (InstanceRefConfigurationException e)
			// {
			// CessarPluginActivator.getDefault().logError(e);
			// }
			List<IContextType> contextWrapperList = refHandler.getContextTypes();
			if (contextWrapperList.isEmpty())
			{
				return;
			}
			for (int i = 0; i < contextWrapperList.size(); i++)
			{
				contextLabelList.add(createContextLabel(parentComposite, contextWrapperList.get(i)));

				contextFieldList.add(createContextField(contextWrapperList.get(i)));
			}

		}

		/**
		 * @param parentComposite2
		 * @param contextFeatureWrapper
		 * @return
		 */
		private Label createContextLabel(Composite parent, IContextType contextFeatureWrapper)
		{
			String featureName = contextFeatureWrapper.getType().getName();
			Label contextLabel = getToolkit().createLabel(parent, featureName);
			contextLabel.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix()
				+ "#" + IdentificationUtils.CONTEXT_LABEL_ID + "#" + featureName); //$NON-NLS-1$ //$NON-NLS-2$
			return contextLabel;
		}

		private Text createContextField(IContextType contextFeatureWrapper)
		{
			Text newContext = getToolkit().createText(parentComposite, "", //$NON-NLS-1$
				SWT.READ_ONLY | SWT.BORDER);
			newContext.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix()
				+ "#" + IdentificationUtils.CONTEXT_TEXT_ID); //$NON-NLS-1$

			newContext.setData(contextFeatureWrapper);

			newContext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			addFocusListener(newContext);
			addKeyListener(newContext);
			return newContext;
		}

		/**
		 * Checks if the context text controls are disposed.
		 *
		 * @return true, if is disposed
		 */
		public boolean isDisposed()
		{
			for (Text context: contextFieldList)
			{
				if (context.isDisposed())
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * @param enabled
		 */
		public void setEnabled(boolean enabled)
		{
			for (Text context: contextFieldList)
			{
				context.setEnabled(enabled);
			}
		}

		public void setToolTipText(Wrapper<T> wrapper)
		{
			List<T> contextList = wrapper.getContext();

			for (int index = 0; index < contextList.size(); index++)
			{
				List<T> context = new ArrayList<T>();
				context.add(contextList.get(index));
				Text contextField = getContextField(contextList.get(index));
				// contextField.setToolTipText(labelProvider.getTooltip(context));
				int indexOf = contextFieldList.indexOf(contextField);
				contextFieldList.get(indexOf).setToolTipText(labelProvider.getTooltip(context));
			}
		}

		/**
		 * Sets the context field text with proxy notification.
		 *
		 * @param display
		 *        the display
		 * @param values
		 *        the values for the provided contexts
		 * @param isProxy
		 *        indicates if the provided values are proxy elements
		 */
		public void setContextFieldText(Display display, List<T> values, List<Boolean> isProxy)
		{
			hasContextFieldTextSet = new boolean[contextFieldList.size()];

			// clear all the context fields
			for (Text field: contextFieldList)
			{
				field.setText(""); //$NON-NLS-1$
				field.setToolTipText(""); //$NON-NLS-1$
			}
			for (Label label: contextLabelList)
			{
				label.setToolTipText(""); //$NON-NLS-1$
			}

			if (values != null && values.size() > 0)
			{
				int i = 0;
				for (Object object: values)
				{
					String multiValue = ""; //$NON-NLS-1$
					Text context = getContextField(object);

					context.setForeground(isProxy.get(i) ? display.getSystemColor(SWT.COLOR_RED)
						: display.getSystemColor(SWT.COLOR_BLACK));

					if (context.getText() != null)
					{
						multiValue += context.getText();
					}
					if (object instanceof GIdentifiable)
					{
						setContextFieldTextForGIdentifiable(display, object, multiValue, context, isProxy.get(i));
					}
					else
					{
						context.setText(computeMultiValue(multiValue, labelProvider.getText(object), isProxy.get(i)));
					}
					i++;
				}
			}

		}

		private void setContextFieldTextForGIdentifiable(Display display, Object object, String multiValue,
			Text context, boolean isProxy)
		{
			String result = null;

			if (((GIdentifiable) object).eIsProxy())
			{
				context.setForeground(display.getSystemColor(SWT.COLOR_RED));

				if (((GIdentifiable) object).gGetShortName() == null
					|| "".equals(((GIdentifiable) object).gGetShortName())) //$NON-NLS-1$
				{

					result = computeMultiValue(multiValue, ((EObject) object).eClass().getName(), true);
				}
				else
				{
					result = computeMultiValue(multiValue, ((GIdentifiable) object).gGetShortName(), true);
				}
			}
			else
			{
				result = computeMultiValue(multiValue, ((GIdentifiable) object).gGetShortName(), isProxy);
			}

			if (result != null)
			{
				context.setText(result);
			}
		}

		private String computeMultiValue(String multiValue, String objectName, boolean isProxy)
		{
			StringBuilder result = new StringBuilder(multiValue);
			if (multiValue.length() > 1)
			{
				result.append(", "); //$NON-NLS-1$
			}
			if (isProxy)
			{
				result.append(MISSING_ELEMENT_MSG);
			}
			result.append(objectName);

			return result.toString();
		}

		/**
		 * @param object
		 * @return
		 */
		private Text getContextField(Object object)
		{
			for (Text contextField: contextFieldList)
			{
				ContextType wrapper = (ContextType) contextField.getData();
				if (object instanceof EObject)
				{
					if (wrapper.getType().isSuperTypeOf(((EObject) object).eClass()))
					{
						boolean b = hasContextFieldTextSet[contextFieldList.indexOf(contextField)];

						if (b && !wrapper.allowsInfiniteInstances())
						{
							continue;
						}

						hasContextFieldTextSet[contextFieldList.indexOf(contextField)] = true;

						return contextField;
					}

				}
				else
				{
					if (object instanceof SystemIRefFeatureWrapper)
					{
						EReference feature = ((SystemIRefFeatureWrapper) object).getFeature();
						if (wrapper.getType().equals(feature.getEReferenceType()))
						{
							return contextField;
						}

					}
				}
			}
			return contextFieldList.get(0);
		}

		/**
		 * @param givenColor
		 */
		public void setForeground(Color givenColor)
		{
			for (Text context: contextFieldList)
			{
				context.setForeground(givenColor);
			}
		}
	}

	/**
	 * Instantiates a new single instance ref widget.
	 *
	 * @param labelProvider
	 *        the label provider
	 * @param handler
	 *        the handler
	 * @param editor
	 *        the editor
	 */
	public SingleInstanceRefWidget(IInstReferenceLabelProvider<T> labelProvider, IChooseIRefHandler handler,
		IModelFragmentEditor editor)
	{
		super(editor);
		this.handler = handler;
		this.labelProvider = labelProvider;
		firstInput = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ecuc.ui.widgets.IInstanceRefEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	public Control createEditor(Composite parent)
	{
		Composite composite = getToolkit().createComposite(parent);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout(gridLayout);

		Composite fieldComposite = getToolkit().createComposite(composite);
		gridLayout.verticalSpacing = 0;
		fieldComposite.setLayout(gridLayout);
		fieldComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		initializeTargetGroup(gridLayout, fieldComposite);

		initializeContextGroup(gridLayout, fieldComposite);

		firstInput = true;

		initializeChooseButton(composite);

		doSetReadOnly();

		return composite;
	}

	private void initializeTargetGroup(GridLayout gridLayout, Composite fieldComposite)
	{
		targetGroup = getToolkit().createGroup(fieldComposite, "Target"); //$NON-NLS-1$
		targetGroup.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix()
			+ "#" + IdentificationUtils.TARGET_LABEL_ID); //$NON-NLS-1$
		GridData gridDataGroupTarget = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataGroupTarget.horizontalSpan = 2;
		targetGroup.setLayoutData(gridDataGroupTarget);
		targetGroup.setLayout(gridLayout);

		targetLabel = getToolkit().createLabel(targetGroup, "featureName "); //$NON-NLS-1$
		targetLabel.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix()
			+ "#" + IdentificationUtils.TARGET_LABEL_ID); //$NON-NLS-1$

		targetField = getToolkit().createText(targetGroup, "", SWT.READ_ONLY | SWT.BORDER); //$NON-NLS-1$
		targetField.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix()
			+ "#" + IdentificationUtils.TARGET_TEXT_ID); //$NON-NLS-1$
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		targetField.setLayoutData(gridData);
		addFocusListener(targetField);
		addKeyListener(targetField);
	}

	private void initializeContextGroup(GridLayout gridLayout, Composite fieldComposite)
	{
		contextGroup = getToolkit().createGroup(fieldComposite, "Context"); //$NON-NLS-1$
		contextGroup.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix()
			+ "#" + IdentificationUtils.CONTEXT_LABEL_ID); //$NON-NLS-1$
		GridData gridDataGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataGroup.horizontalSpan = 2;
		contextGroup.setLayoutData(gridDataGroup);
		contextGroup.setLayout(gridLayout);

		contextWidget = new ContextWidget(contextGroup);
	}

	private void initializeChooseButton(Composite composite)
	{
		GridLayout gridLayout;
		Composite btnComposite = getToolkit().createComposite(composite);
		gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		btnComposite.setLayout(gridLayout);

		chooseButton = getToolkit().createButton(btnComposite, "...", SWT.PUSH); //$NON-NLS-1$
		chooseButton.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" //$NON-NLS-1$
			+ IdentificationUtils.PUSH_BUTTON_ID);

		addFocusListener(chooseButton);
		chooseButton.addSelectionListener(new ChooseButtonSelectionListener());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.IInstanceRefWidget#setFocus()
	 */
	public void setFocus()
	{
		if (targetField != null && !targetField.isDisposed())
		{
			targetField.setFocus();
			targetField.selectAll();
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

	/**
	 * @return
	 */
	private boolean haveUI()
	{
		return isValidWidget(targetField) && !contextWidget.isDisposed() && isValidWidget(contextGroup);
	}

	private boolean isValidWidget(Widget widget)
	{
		return widget != null && !widget.isDisposed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ecuc.ui.widgets.IInstanceRefEditor#setStatusMessage(org.eclipse.core.runtime.IStatus)
	 */
	@Override
	public void setStatusMessage(IStatus status)
	{
		// ...
	}

	private static final String MISSING_ELEMENT_MSG = "[MISSING] "; //$NON-NLS-1$

	/**
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	private void setDataToUI(Wrapper<T> wrapper)
	{
		Display display = targetField.getDisplay();
		if (wrapper != null)
		{
			contextWidget.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			targetField.setForeground(display.getSystemColor(SWT.COLOR_BLACK));

			List<T> context = wrapper.getContext();
			T target = wrapper.getTarget();

			if (target instanceof GIdentifiable)
			{
				if (((GIdentifiable) target).eIsProxy())
				{
					targetField.setForeground(display.getSystemColor(SWT.COLOR_RED));
					targetField.setText(MISSING_ELEMENT_MSG + labelProvider.getText(target));
				}
				else
				{
					targetField.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
					targetField.setText(labelProvider.getText(target));
				}
			}
			else
			{
				if (target instanceof SystemIRefFeatureWrapper)
				{
					Object gObj = ((SystemIRefFeatureWrapper) target).getValue();
					if (gObj != null && (gObj instanceof GIdentifiable))
					{
						if (((GIdentifiable) gObj).eIsProxy())
						{
							targetField.setForeground(display.getSystemColor(SWT.COLOR_RED));
							targetField.setText(MISSING_ELEMENT_MSG + labelProvider.getText(target));
						}
						else
						{
							targetField.setText(labelProvider.getText(target));
						}
					}
					else
					// after delete
					{
						targetField.setText(""); //$NON-NLS-1$
					}
				}
				else
				{
					targetField.setText(labelProvider.getText(target));
				}
			}

			// contextField.setText(labelProvider.getText(context));

			Object gContext = null;
			List<Boolean> isMissingContext = new ArrayList<Boolean>();

			if ((context != null) && (context.size() >= 1))
			{
				for (int i = 0; i < context.size(); i++)
				{
					if (context.get(i) instanceof GIdentifiable)
					{
						gContext = context.get(i);
						isMissingContext.add(((GIdentifiable) gContext).eIsProxy());
					}
					else
					{
						if (context.get(i) instanceof SystemIRefFeatureWrapper)
						{
							gContext = ((SystemIRefFeatureWrapper) context.get(i)).getValue();

							if (!(gContext instanceof BasicEList))
							{
								isMissingContext.add(((GIdentifiable) gContext).eIsProxy());
							}
							else
							{
								BasicEList<EObject> eRefList = (BasicEList<EObject>) gContext;
								Iterator<EObject> itRefList = eRefList.iterator();
								while (itRefList.hasNext())
								{
									isMissingContext.add(((GIdentifiable) itRefList.next()).eIsProxy());
								}
							}
						}
					}
				}
			}
			contextWidget.setContextFieldText(display, context, isMissingContext);
			contextWidget.setToolTipText(wrapper);
		}
		else
		{
			targetField.setText(""); //$NON-NLS-1$
			contextWidget.setContextFieldText(display, null, null);
		}

		GIdentifiable target = handler.getTarget();
		if (target != null)
		{
			// targetLabel.setToolTipText(ModelUtils.getAbsoluteQualifiedName(target));
			targetField.setToolTipText(ModelUtils.getAbsoluteQualifiedName(target)
				+ IReferenceLabelProvider.OF_TYPE_CONSTANT + target.eClass().getName());
		}
		else
		{
			targetField.setToolTipText(""); //$NON-NLS-1$
		}
		targetGroup.setToolTipText(labelProvider.getTargetCaptionTooltip());

		contextGroup.setToolTipText(labelProvider.getContextCaptionTooltip(true));

	}

	public void setInputData(Wrapper<T> data)
	{
		if (firstInput)
		{
			contextWidget.create(handler);
			contextGroup.redraw();
			firstInput = false;

			EClass targetType = handler.getTargetType();
			if (targetType != null)
			{
				targetLabel.setText(handler.getTargetType().getName());
			}

			List<Object> candidates = handler.getCandidates();
			if (candidates != null && !candidates.isEmpty())
			{
				Object object = candidates.get(0);
				if (object != null && object instanceof GIdentifiable)
				{
					targetLabel.setText(((GIdentifiable) object).gGetShortName());
				}
				else
				{
					targetLabel.setText(object.getClass().getName());
				}
			}
		}
		if (haveUI())
		{
			setDataToUI(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.AbstractInstancerefWidget#doSetReadOnly(boolean)
	 */
	@Override
	protected void doSetReadOnly()
	{
		if (chooseButton != null && !chooseButton.isDisposed())
		{
			chooseButton.setEnabled(!isReadOnly());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.AbstractInstancerefWidget#doSetEnabled(boolean)
	 */
	@Override
	protected void doSetEnabled(boolean enabled)
	{
		if (targetField != null && !targetField.isDisposed())
		{
			targetField.setEnabled(enabled);
		}

		if (!contextWidget.getContextFields().isEmpty() && !contextWidget.isDisposed())
		{
			contextWidget.setEnabled(enabled);
		}

		if (chooseButton != null && !chooseButton.isDisposed())
		{
			chooseButton.setEnabled(enabled);
		}

	}

}
