/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 29, 2010 12:22:52 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.decorator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.WorkspaceUIConstants;

/**
 * @author uidt2045
 * 
 */
public class FileStatusDecorator extends BaseLabelProvider implements ILightweightLabelDecorator
{

	private Color readOnlyColor;

	/**
	 * Return the color to be used to represent resources that are read only
	 * 
	 * @return
	 */
	private Color getReadOnlyColor()
	{
		if (readOnlyColor == null)
		{
			synchronized (FileStatusDecorator.class)
			{
				if (readOnlyColor == null)
				{
					final Color[] color = new Color[1];
					Display.getDefault().syncExec(new Runnable()
					{

						public void run()
						{
							color[0] = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
						}
					});
					readOnlyColor = color[0];
				}
			}
		}
		return readOnlyColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelDecorator#decorateImage(org.eclipse.swt.graphics.Image, java.lang.Object)
	 */
	public void decorate(Object element, final IDecoration decoration)
	{
		ImageDescriptor roDescr = CessarPluginActivator.getDefault().getImageRegistry().getDescriptor(
			WorkspaceUIConstants.IMAGE_ID_RO_DECO);
		IResource resource = null;
		if (element instanceof IResource)
		{
			resource = (IResource) element;
		}

		if (resource != null)
		{
			ResourceAttributes atr = resource.getResourceAttributes();
			if (atr != null && atr.isReadOnly())
			{
				decoration.setForegroundColor(getReadOnlyColor());
				decoration.addOverlay(roDescr);
			}
		}
	}

}
