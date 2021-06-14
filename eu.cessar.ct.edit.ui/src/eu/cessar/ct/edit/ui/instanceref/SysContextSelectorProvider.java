/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 14, 2010 6:28:31 PM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;

/**
 * @author uidl6870
 * 
 */
public class SysContextSelectorProvider implements ISelector
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISelector#isSelected(eu.cessar.ct.core.mms.IMetaModelService,
	 * org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz, EStructuralFeature feature)
	{
		if (mmService == null)
		{
			return false;
		}

		boolean isSysInsRef = mmService.isSystemInstanceRef(clz);
		if (isSysInsRef)
		{
			List<EReference> contextFeatures = mmService.getContextFeatures(clz);
			return contextFeatures.contains(feature);
		}
		return false;
	}

}
