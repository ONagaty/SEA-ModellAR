/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 May 18, 2010 6:24:04 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.utils.EditUtils;

/**
 * Base implementation of an fragment editor's caption part, which creates a label and sets its color and font based on
 * the set/unset, mandatory/optional properties.
 *
 * @author uidl6870
 *
 */
public abstract class AbstractCaptionPart implements ICaptionPart
{
	private Text label;
	private String caption;

	private IModelFragmentEditor editor;
	private CaptionHoverInformationControlManager manager;

	/**
	 * @param editor
	 * @param caption
	 */
	public AbstractCaptionPart(IModelFragmentEditor editor, String caption)
	{
		this.editor = editor;
		this.caption = caption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.ICaptionPart#getCaption()
	 */
	public String getCaption()
	{
		return caption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.ICaptionPart#setCaption(java.lang.String)
	 */
	@Override
	public void setCaption(String captionName)
	{
		caption = captionName;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		label = getFormToolkit().createText(parent, null, SWT.READ_ONLY);
		manager = new CaptionHoverInformationControlManager(this);
		manager.install(label);
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
			// DefaultInformationControl
			// IInformationControlCreator
			// AbstractHoverInformationControlManager
			label.setData(IdentificationUtils.KEY_NAME, getEditor().getInstanceId()
				+ IdentificationUtils.CAPTION_PART_ID);

			label.setText(caption);

			String doc = getDocumentation();
			if (doc != null)
			{ // SUPPRESS CHECKSTYLE check in future
				// label.setToolTipText(doc);
			}
			boolean isSet = getEditor().isValueSet();
			boolean isMnd = isMandatory();
			EditUtils.setCaptionFontAndColor(label, isSet, isMnd);
		}
	}

	/**
	 * Computes if the editor value is mandatory
	 *
	 * @return true if it is mandatory
	 */
	public boolean isMandatory()
	{
		return getEditor().isValueMandatory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setFocus()
	 */
	public void setFocus()
	{
		if (label != null && !label.isDisposed())
		{
			label.setFocus();
		}

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
			manager.dispose();
			label.dispose();
			manager = null;
			label = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#getEditor()
	 */
	public IModelFragmentEditor getEditor()
	{
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEditor(eu.cessar.ct.edit.ui.facility.
	 * IModelFragmentEditor)
	 */
	public void setEditor(IModelFragmentEditor editor)
	{
		this.editor = editor;
	}

	/**
	 * @return CessarFormToolkit
	 */
	protected CessarFormToolkit getFormToolkit()
	{
		return getEditor().getFormToolkit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.ICaptionPart#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled)
	{
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.ICaptionPart#getDocumentation()
	 */
	public String getDocumentation()
	{
		StringBuilder documentation = new StringBuilder();
		// if NOT in whiteBox do nothing

		if (PlatformUtils.isTestPluginLoaded())
		{
			documentation.append("<b>EditorID:</b>["); //$NON-NLS-1$
			documentation.append(getEditor().getInstanceId());
			documentation.append("]<br/><br/>"); //$NON-NLS-1$
		}
		return documentation.toString();
	}

}
