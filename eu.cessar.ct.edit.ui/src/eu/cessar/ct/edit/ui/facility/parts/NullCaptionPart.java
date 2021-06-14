/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Apr 14, 2011 4:44:30 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart;

/**
 * Editors that do not support a caption part should use this implementation.
 * 
 * @author uidt2045
 * 
 */
public class NullCaptionPart extends AbstractEditorPart implements ICaptionPart
{
	/**
	 * 
	 */
	protected Label label;
	/**
	 * 
	 */
	protected String caption;

	/**
	 * @param editor
	 * @param caption
	 */
	public NullCaptionPart(IModelFragmentEditor editor, String caption)
	{
		super(editor);
		this.caption = caption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractCaptionPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		label = getFormToolkit().createLabel(parent, ""); //$NON-NLS-1$
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractCaptionPart#dispose()
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
	 * @see eu.cessar.ct.edit.ui.facility.parts.ICaptionPart#getDocumentation()
	 */
	public String getDocumentation()
	{
		String code = ""; //$NON-NLS-1$
		// show the code only if in whitebox
		if (PlatformUtils.isTestPluginLoaded())
		{
			code += getEditor().getInstanceId();
			code += "-->"; //$NON-NLS-1$
		}

		return code + "is a null caption part"; //$NON-NLS-1$
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
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	public void refresh()
	{
		// does nothing
	}

	@Override
	protected CessarFormToolkit getFormToolkit()
	{
		return getEditor().getFormToolkit();
	}

}
