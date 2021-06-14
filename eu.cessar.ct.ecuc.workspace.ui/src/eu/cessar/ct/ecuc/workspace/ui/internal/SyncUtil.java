/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu8153<br/>
 * May 6, 2016 10:12:13 AM
 *
 * </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal;

import org.eclipse.emf.ecore.EObject;

import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;

/**
 * TODO: Please comment this class
 *
 * @author uidu8153
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public final class SyncUtil
{
	/**
	 * @param selectedObj
	 * @return
	 */
	public static String getSelectedObjectType(EObject selectedObj)
	{
		String selectedObjType = SyncMessages.Type_Unknown;

		// check for obj from the Configuration
		if (selectedObj instanceof GModuleConfiguration)
		{
			selectedObjType = SyncMessages.Type_ModuleConfig;
		}
		else if (selectedObj instanceof GContainer)
		{
			selectedObjType = SyncMessages.Type_Container;
		}
		else if (selectedObj instanceof GParameterValue)
		{
			selectedObjType = SyncMessages.Type_Parameter;
		}
		else if (selectedObj instanceof GConfigReferenceValue)
		{
			selectedObjType = SyncMessages.Type_Reference;
		}

		else if (selectedObj instanceof GConfigParameter)
		{
			selectedObjType = SyncMessages.Type_Parameter;
		}
		else if (selectedObj instanceof GConfigReference)
		{
			selectedObjType = SyncMessages.Type_Reference;
		}
		else if (selectedObj instanceof GContainerDef)
		{
			selectedObjType = SyncMessages.Type_Container;
		}

		return selectedObjType;
	}
}
