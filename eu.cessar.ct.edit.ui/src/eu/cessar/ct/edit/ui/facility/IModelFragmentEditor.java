/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:25:22 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.platform.ui.events.EFocusEvent;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.edit.ui.facility.parts.EEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IValidationPart;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingManager;
import eu.cessar.req.Requirement;

/**
 * The root interface in the <i>model fragment editors hierarchy</i>. <br>
 * It encapsulates the UI capable to edit a meta-model fragment (one or more features).
 * 
 * @author uidl6458
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#SPLIT#1")
public interface IModelFragmentEditor
{

	/**
	 * Returns the editor part corresponding to the {@link EEditorPart} literal given as parameter. <br>
	 * If the editor does not support the requested part, <code>null</code> is returned. <br>
	 * 
	 * @param editorPartType
	 *        literal object corresponding to the requested part
	 * @return the editor part corresponding to the <code>editorPartType</code> literal if supported, <code>null</code>
	 *         otherwise
	 */
	public IModelFragmentEditorPart getPart(EEditorPart editorPartType);

	/**
	 * Set the provider of this editor. Editor consumers should not call this method, it should be called by the editor
	 * provider during editor creation.
	 * 
	 * @param editorProvider
	 */
	public void setEditorProvider(IModelFragmentEditorProvider editorProvider);

	/**
	 * Return the provider of the editor.
	 * 
	 * @return
	 */
	public IModelFragmentEditorProvider getEditorProvider();

	/**
	 * Return the editor's type ID
	 * 
	 * @return
	 */
	public String getTypeId();

	/**
	 * Return the editor's instance ID <br>
	 * instanceID = typeID + "#" +...
	 * 
	 * @return
	 */
	public String getInstanceId();

	/**
	 * Set the input object on which editing should happen
	 * 
	 * @param object
	 */
	public void setInput(EObject object);

	/**
	 * Return the edited object
	 * 
	 * @return
	 */
	public EObject getInput();

	/**
	 * @param toolkit
	 */
	public void setFormToolkit(CessarFormToolkit toolkit);

	/**
	 * @return
	 */
	public CessarFormToolkit getFormToolkit();

	/**
	 * @return true if the editor edits multiple values
	 */
	public boolean isMultiValueEditor();

	/**
	 * @return true if there is a value represented by the editor, eg. the editor is not empty
	 */
	public boolean isValueSet();

	/**
	 * @return true if a valid configuration imply that there should be a value inside the editor
	 */
	public boolean isValueMandatory();

	/**
	 * Refresh the all editor parts with fresh informations got from the input.
	 */
	public void refresh();

	/**
	 * Refresh the all editor parts with fresh informations got from the input and sends notification to all listeners
	 * of type {@link IModelEditorChangeListener}.
	 */
	public void refreshWithNotifications();

	/**
	 * 
	 */
	public void dispose();

	/**
	 * Return the part of the editor used to represent the caption, never null
	 * 
	 * @deprecated this method should not be used
	 * @see #getPart(EEditorPart)
	 * @return
	 */
	@Deprecated
	public ICaptionPart getCaptionPart();

	/**
	 * Return the part of the editor that represent the validation
	 * 
	 * @deprecated this method should not be used
	 * @see #getPart(EEditorPart)
	 * @return
	 */
	@Deprecated
	public IValidationPart getValidationPart();

	/**
	 * Return the part of the editor that represent the editing area
	 * 
	 * @deprecated this method should not be used
	 * @see #getPart(EEditorPart)
	 * @return
	 */
	@Deprecated
	public IEditorPart getEditorPart();

	/**
	 * Return the part of the editor that represent the action part
	 * 
	 * @deprecated this methos should not be used
	 * @see #getPart(EEditorPart)
	 * @return
	 */
	@Deprecated
	public IActionPart getActionPart();

	/**
	 * @param dropDownActionPart
	 */
	public void populateActionPart(IActionPart dropDownActionPart);

	/**
	 * Returns a status describing the editable state of the editor.
	 * 
	 * @return an <code>IStatus</code> object with status code <code>OK</code>, if the editor is read-write. an
	 *         <code>IStatus</code> object with status code <code>INFO</code>, carrying the reason why the editor is
	 *         read-only.
	 */
	public IStatus getEditableStatus();

	/**
	 * Return a status indicating the visibility of the editing area's content
	 * 
	 * @return an <code>IStatus</code> object with severity <code>OK</code>, if the content of the editing area is to be
	 *         shown, otherwise a <code>IStatus</code> object with severity <code>INFO</code>, carrying the reason why
	 *         the content must not be displayed.
	 */
	public IStatus getEditingAreaContentVisibility();

	/**
	 * Set the enablement state of the editor.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Returns the enablement state of the editor.
	 * 
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * 
	 * @param listener
	 */
	public void addModelEditorChangeListener(IModelEditorChangeListener listener);

	/**
	 * Register a listener to receive notifications when events like: Focus lost, ENTER/ESC pressed occur for the
	 * controls that reside in the contents area of the editor. <br>
	 * USE CASE: editor activation/de-activation within a table's cell editor
	 * 
	 * @param listener
	 */
	public void setEventListener(IFocusEventListener listener);

	/**
	 * Called by the editor to enable/disable the delivery of the Focus Lost event to the editor's consumer. The
	 * <code>enablement</code> flag indicates whether the focus of one of the editors' controls was lost in favor of an
	 * internal/external control.<br>
	 * E.g. for multi values editor, pressing the browse button will open a message dialog - which is considered an
	 * internal Focus lost, in such a case a sequence like following is needed: <br>
	 * <br>
	 * <code> deliverFocusLost(false); <br> 
	 * dialog.open(); <br>
	 * deliverFocusLost(true);
	 * </code>
	 * 
	 * @param enablement
	 *        whether the focus is lost to an external control (outside the editor)
	 */
	public void deliverFocusLost(boolean enablement);

	/**
	 * 
	 * @return <code>true</code> if {@link EFocusEvent#FOCUS_OUT} received from the registered
	 *         {@link IFocusEventListener} is to be taken into account, i.e. the focus is lost within the editor to an
	 *         external control, <code>false</code> otherwise
	 * 
	 */
	public boolean shouldDeliverFocusLost();

	/**
	 * Returns the event listener registered by the client or <code>null</code> if no listener set. <br>
	 * Called by the {@link IModelFragmentEditorPart} which in turn, forwards the obtained listener to the CESSAR-CT
	 * platform widgets which will notify the listener if an {@link EFocusEvent} occurs
	 * 
	 * @return
	 */
	public IFocusEventListener getEventListener();

	/**
	 * 
	 * @param listener
	 */
	public void removeModelEditorChangeListener(IModelEditorChangeListener listener);

	/**
	 * Returns whether the editor has only informative purpose. This is the case of editors that, in turn, provide an
	 * expansion
	 * 
	 * @return <code>true</code> if the editor has only informative purpose, <code>false</code> otherwise
	 */
	public boolean isInformative();

	/**
	 * Return whether the input set on the editor is a wrapper over splitted fragments
	 * 
	 * @return <code>true</code> if the editor's input is of {@link Splitable} type, <code>false</code> otherwise
	 */
	public boolean isInputSplitable();

	/**
	 * 
	 * @return the manager to be used by the editor's parts if the input is of {@link Splitable} type, <code>null</code>
	 *         otherwise
	 */
	public ISplitableContextEditingManager getSplitableContextEditingManager();
}
