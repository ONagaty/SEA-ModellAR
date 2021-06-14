/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.10.2012 10:03:08
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart;

/**
 * Base implementation of a {@link IResourcesPart}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Oct 16 13:28:25 2012 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractResourcesPart extends AbstractEditorPart implements IResourcesPart
{
	private Label label;

	/**
	 * @param editor
	 */
	public AbstractResourcesPart(IModelFragmentEditor editor)
	{
		super(editor);
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
		label = new Label(parent, SWT.NONE);
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	@Override
	public void refresh()
	{
		if (label != null && !label.isDisposed())
		{
			if (!getEditor().isInputSplitable())
			{
				// not applicable
				return;
			}

			label.setText(getResourcesNo() + ""); //$NON-NLS-1$
			label.setVisible(getVisibility());
			label.setToolTipText(getDocumentation());
		}
	}

	/**
	 * @return whether the resources part should be visible or not
	 */
	protected abstract boolean getVisibility();

	public abstract String getDocumentation();

	/**
	 * @param resources
	 *        list with resources
	 * @return a String comprising the project relative paths of the given <code>resources</code>, separated by a new
	 *         line
	 */
	protected static String getResourcesAsString(List<Resource> resources)
	{
		StringBuffer buffer = new StringBuffer();

		for (Resource resource: resources)
		{
			buffer.append("\n"); //$NON-NLS-1$
			IFile file = EcorePlatformUtil.getFile(resource);
			buffer.append("-"); //$NON-NLS-1$
			buffer.append(PlatformConstants.PATH_SEPARATOR);
			buffer.append(file.getProjectRelativePath());
		}

		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	@Override
	public void dispose()
	{
		if (label != null)
		{
			label.dispose();
			label = null;
		}
	}

}
