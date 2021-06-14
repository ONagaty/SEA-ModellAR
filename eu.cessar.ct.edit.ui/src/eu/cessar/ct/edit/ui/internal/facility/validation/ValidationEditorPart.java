/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidv3687<br/>
 * Feb 19, 2013 1:53:58 PM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.validation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Table;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.validation.ui.ValidateAutosarContentHandler;
import eu.cessar.req.Requirement;

/**
 * Construct the Validation specific editor
 *
 * @author uidv3687
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Sep 17 14:46:14 2014 %
 *
 *         %version: 1 %
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#VALIDATION#1")
public class ValidationEditorPart implements IEditorPart, IResourceChangeListener
{

	private static EValidationTableColumn[] cSplitable = new EValidationTableColumn[] {
		EValidationTableColumn.DESCRIPTION, EValidationTableColumn.OBJECT, EValidationTableColumn.URI,
		EValidationTableColumn.EOBJECT_TYPE}; // EValidationTableColumn.RESOURCE,

	private static EValidationTableColumn[] cNonSpltable = new EValidationTableColumn[] {
		EValidationTableColumn.DESCRIPTION, EValidationTableColumn.OBJECT, EValidationTableColumn.URI,
		EValidationTableColumn.EOBJECT_TYPE};

	private IModelFragmentEditor editor;
	private ValidationTableViewer tableViewer;
	private boolean validateChildren;

	/**
	 *
	 * @param editor
	 */
	public ValidationEditorPart(IModelFragmentEditor editor)
	{
		this.editor = editor;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEditor(eu.cessar.ct.edit.ui.facility.
	 * IModelFragmentEditor)
	 */
	@Override
	public void setEditor(IModelFragmentEditor editor)
	{
		this.editor = editor;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#getEditor()
	 */
	@Override
	public IModelFragmentEditor getEditor()
	{
		return editor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContents(Composite parent)
	{
		Composite composite = editor.getFormToolkit().createComposite(parent);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gd);

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 5;
		layout.marginWidth = 2;
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.WRAP);
		label.setText(Messages.ValidationNote_text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		final Button checkBox = new Button(composite, SWT.CHECK);
		checkBox.setText(Messages.ValidationCheckBox_text);
		checkBox.setSelection(true);
		validateChildren = checkBox.getSelection();

		checkBox.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				validateChildren = checkBox.getSelection();
			}
		});

		final Button button = new Button(composite, SWT.PUSH);
		button.setText(Messages.ValidationButton_text);
		button.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ValidateAutosarContentHandler.runValidation(getEditor().getInput(), validateChildren);
			}

		});

		tableViewer = new ValidationTableViewer(composite);

		new ValidationTabContextMenu(tableViewer);

		Table control = (Table) tableViewer.getControl();
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(data);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setFocus()
	 */
	@Override
	public void setFocus()
	{
		// nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	@Override
	public void refresh()
	{
		EObject input = getEditor().getInput();

		tableViewer.getTable().setRedraw(false);

		try
		{
			tableViewer.updateMarkers(input, validateChildren);
			// Set the correct table headers coresponding to the view they are displayed in
			if (SplitableUtils.INSTANCE.isSplitable(input))
			{
				tableViewer.updateColumns(cSplitable);
			}
			else
			{
				tableViewer.updateColumns(cNonSpltable);
			}
		}
		finally
		{
			tableViewer.getTable().setRedraw(true);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	@Override
	public void dispose()
	{
		tableViewer = null;
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	@Override
	public String getText()
	{
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	@Override
	public Image getImage()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent
	 * )
	 */
	@Override
	public void resourceChanged(IResourceChangeEvent event)
	{
		if (hasMarkerDelta(event))
		{
			// trigger table refres
			Display.getDefault().syncExec(new Runnable()
			{

				@Override
				public void run()
				{
					ValidationEditorPart.this.refresh();
				}
			});

			return;
		}

	}

	private static boolean hasMarkerDelta(IResourceChangeEvent event)
	{
		String[] markerTypes = new String[] {IMarker.PROBLEM};

		for (String element: markerTypes)
		{
			if (event.findMarkerDeltas(element, true).length > 0)
			{
				return true;
			}
		}
		return false;
	}
}
