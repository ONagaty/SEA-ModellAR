/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 1, 2011 2:04:51 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.viewers.AbstractTableLabelProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * @author uidl6870
 * 
 */
public class RefinedModuleDefinitionTreeLabelProvider extends AbstractTableLabelProvider
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

		if (columnIndex == 0)
		{
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
			if (element instanceof GModuleDef)
			{
				return CessarPluginActivator.getDefault().getImage(
					CessarPluginActivator.ICON_BSW_MODULE);
			}
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
		switch (columnIndex)
		{
			case 0:
				return ((GModuleDef) element).gGetShortName();
			case 1:
			{
				IFile file = EcorePlatformUtil.getFile((EObject) element);
				return (null == file ? null : file.getFullPath().toString());
			}
		}
		return null;

	}

}
