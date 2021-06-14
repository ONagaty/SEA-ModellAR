/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 1, 2011 2:07:09 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.viewers.AbstractTableLabelProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 * 
 */
public class PackagesTreeLabelProvider extends AbstractTableLabelProvider
{

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex)
	{
		if (element == null)
		{
			return null;
		}
		if (element instanceof IProject)
		{
			return CessarPluginActivator.getDefault().getImage(
				CessarPluginActivator.ICON_BSW_PROJECT);
		}
		if (element instanceof GARPackage)
		{
			return CessarPluginActivator.getDefault().getImage(
				CessarPluginActivator.ICON_AR_PACKAGE);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex)
	{
		if (null == element)
		{
			return null;
		}

		if (element instanceof IProject)
		{
			return ((IProject) element).getName();
		}
		if (element instanceof GIdentifiable)
		{
			return ((GIdentifiable) element).gGetShortName();
		}
		return null;
	}

}
