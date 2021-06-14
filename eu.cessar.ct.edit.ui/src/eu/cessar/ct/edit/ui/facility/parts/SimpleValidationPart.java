/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jul 2, 2010 5:01:57 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * Validation part that creates a label and applies an image and a tool-tip on the label according to the status
 */
public class SimpleValidationPart extends AbstractEditorPart implements IValidationPart, IDocumentationOwner
{
	private Label label;

	/*
	 * protected final static int OK = IStatus.OK; protected final static int INFO = IStatus.INFO; protected final
	 * static int WARN = IStatus.WARNING; protected final static int ERROR = IStatus.ERROR;
	 */

	public SimpleValidationPart(IModelFragmentEditor editor)
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
		label = new Label(parent, SWT.NONE);
		return label;
	}

	/**
	 * @return the label
	 */
	protected Label getLabel()
	{
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	public void refresh()
	{
		if (label != null && !label.isDisposed())
		{
			label.setVisible(true);
			IModelFragmentEditor editor = getEditor();

			if (editor != null)
			{
				String tooltipMsg = getDocumentation();

				// set tool-tip
				label.setToolTipText(tooltipMsg);

				int status = getStatus();
				switch (status)
				{
					case (IStatus.ERROR):
						Image errImage = CessarPluginActivator.getDefault().getImage(
							CessarPluginActivator.ERROR_ICON_ID);
						label.setImage(errImage);
						break;

					case (IStatus.WARNING):
						label.setImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.WARN_ICON_ID));
						break;

					case (IStatus.INFO):
						Image warnImage = CessarPluginActivator.getDefault().getImage(
							CessarPluginActivator.INFO_ICON_ID);
						label.setImage(warnImage);
						break;

					case (IStatus.OK):
						label.setImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.INFO_ICON_ID));
						// everything OK, hide it
						label.setVisible(false);
						break;
					default:
						// do nothing
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IValidationPart#getValidationStatus()
	 */
	public IStatus getValidationStatus()
	{
		return getEditor().getEditableStatus();
	}

	/**
	 * 
	 * @return the editable status's severity
	 */
	protected int getStatus()
	{
		return getEditor().getEditableStatus().getSeverity();
	}

	/**
	 * Returns the documentation regarding the validation status
	 */
	public String getDocumentation()
	{
		IStatus status = getValidationStatus();

		if (!status.isMultiStatus())
		{
			return status.getMessage();
		}
		else
		{
			StringBuffer buffer = new StringBuffer();

			IStatus[] children = ((MultiStatus) status).getChildren();
			for (IStatus s: children)
			{
				buffer.append("\n"); //$NON-NLS-1$
				buffer.append(getSeverityAsString(s));
				buffer.append(": "); //$NON-NLS-1$
				buffer.append(s.getMessage());
			}

			String res = buffer.toString();
			if (res.startsWith("\n")) //$NON-NLS-1$
			{
				return res.substring(1);
			}
			return res;
		}
	}

	/**
	 * @param status
	 * @return String representing a human-readable status
	 */
	protected static String getSeverityAsString(IStatus status)
	{
		String res = ""; //$NON-NLS-1$
		switch (status.getSeverity())
		{
			case (IStatus.ERROR):
				res = "ERROR"; //$NON-NLS-1$
				break;

			case (IStatus.WARNING):
				res = "WARN"; //$NON-NLS-1$
				break;

			case (IStatus.INFO):
				res = "INFO"; //$NON-NLS-1$
				break;
			default:
				res = ""; //$NON-NLS-1$
		}
		return res;
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

}
