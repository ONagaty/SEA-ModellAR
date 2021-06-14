/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 4, 2010 11:13:05 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * Editors that do not support a validation part should use this implementation.
 * 
 * @author uidl6458
 * 
 */
public class NullValidationPart extends AbstractEditorPart implements IValidationPart
{

	private Label label;

	/**
	 * 
	 * @param editor
	 */
	public NullValidationPart(IModelFragmentEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		label = new Label(parent, SWT.NONE);
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditorPart#refresh()
	 */
	public void refresh()
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	public void dispose()
	{
		if (label != null)
		{
			label.dispose();
			label = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IValidationPart#getValidationMarkers()
	 */
	public IMarker[] getValidationMarkers()
	{
		// TODO Auto-generated method stub
		return new IMarker[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IValidationPart#getValidationStatus()
	 */
	public IStatus getValidationStatus()
	{
		return new Status(IStatus.OK, CessarPluginActivator.PLUGIN_ID, ""); //$NON-NLS-1$
	}

}
