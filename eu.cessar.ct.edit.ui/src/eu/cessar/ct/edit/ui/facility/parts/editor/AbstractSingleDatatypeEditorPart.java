/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 11, 2010 3:19:03 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.events.IEditorListener;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextSingleFeatureEditingManager;

/**
 * An editor part that should be use and IDatatypeEditor for editing
 *
 * @param <T>
 *
 */
public abstract class AbstractSingleDatatypeEditorPart<T> extends AbstractFeatureEditorPart implements IEditorPart
{

	/**
	 * IDatatypeEditor Editor
	 */
	protected IDatatypeEditor<T> datatypeEditor;
	/**
	 * control
	 */
	protected Control control;

	/**
	 * @param editor
	 */
	public AbstractSingleDatatypeEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		datatypeEditor = createDatatypeEditor();
		// datatypeEditor.setReadOnly(getEditor().getEditableStatus());
		datatypeEditor.setIdentificationPrefix(getEditor().getInstanceId());
		control = datatypeEditor.createEditor(parent);

		datatypeEditor.setEventListener(getEditor().getEventListener());
		datatypeEditor.addEditorListener(new IEditorListener<T>()
		{

			public boolean acceptData(T oldData, T newData)
			{
				if (getInputObject().eResource() == null)
				{
					return false;
				}

				return doAcceptData(oldData, newData);
			}
		});
		return control;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart#setFocus()
	 */
	@Override
	public void setFocus()
	{
		if (datatypeEditor != null)
		{
			datatypeEditor.setFocus();
		}
	}

	/**
	 * @param oldData
	 * @param newData
	 * @return boolean
	 */
	protected boolean doAcceptData(@SuppressWarnings("unused") T oldData, final T newData)
	{
		// perform the change into the model and accept data
		return performChangeWithChecks(new Runnable()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see java.lang.Runnable#run()
			 */
			public void run()
			{
				applyNewData(newData);
			}
		}, "Updating data..."); //$NON-NLS-1$
	}

	/**
	 * If the editor's input is not of Splitable type, applies the given <code>newData</code> on the input's edited
	 * feature. Otherwise, applies the value on the relevant fragments of the input's wrapped element.
	 *
	 * @param newData
	 *        the new value to be applied
	 */
	protected void applyNewData(T newData)
	{
		boolean splitableInput = getEditor().isInputSplitable();
		if (!splitableInput)
		{
			setNewData(getInputObject(), newData);
		}
		else
		{
			ISplitableContextSingleFeatureEditingManager editorManager = getEditor().getSplitableContextEditingManager();
			List<EObject> fragmentsInScope = editorManager.getStrategy().getFragmentsInScope();
			for (EObject eObject: fragmentsInScope)
			{
				setNewData(eObject, newData);
			}
		}
	}

	/**
	 * Sets the edited feature of the given <code>input</code> to the <code>newData</code> value.
	 *
	 * @param input
	 *        the input on which to set the new value
	 * @param newData
	 *        the new value
	 */
	protected void setNewData(EObject input, T newData)
	{
		if (newData != null)
		{
			input.eSet(getInputFeature(), newData);
		}
		else
		{
			input.eUnset(getInputFeature());
		}
	}

	/**
	 * @return IDatatypeEditor
	 */
	protected abstract IDatatypeEditor<T> createDatatypeEditor();

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	public void dispose()
	{
		if (control == null || control.isDisposed())
		{
			return;
		}
		Composite parent = control.getParent();
		control.dispose();
		if (parent == null || parent.isDisposed())
		{
			return;
		}
		parent.dispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	public void refresh()
	{
		if (datatypeEditor != null)
		{
			datatypeEditor.setReadOnly(!getEditor().getEditableStatus().isOK());

			IStatus contentVisibility = getEditor().getEditingAreaContentVisibility();
			boolean ok = contentVisibility.isOK();

			// if content not visible, disable editor and return
			datatypeEditor.setEnabled(ok);
			if (!ok)
			{
				datatypeEditor.unsetInputData();
				return;
			}

			// propagate the enablement state set by the client (default: true)
			datatypeEditor.setEnabled(getEditor().isEnabled());

			EObject inputObject = getInputObject();
			// do refresh
			if (inputObject != null)
			{

				if (!getInputObject().eIsSet(getInputFeature()))
				{
					datatypeEditor.unsetInputData();
				}
				else
				{

					datatypeEditor.setInputData(getInputData());
				}
			}
			else
			{
				datatypeEditor.unsetInputData();
			}
		}
	}

	/**
	 * @return T
	 */
	protected T getInputData()
	{
		return (T) getInputObject().eGet(getInputFeature());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		if (datatypeEditor != null)
		{
			datatypeEditor.setEnabled(enabled);
		}
	}

}
