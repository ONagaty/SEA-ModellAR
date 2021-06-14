/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 9, 2009 2:49:15 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;
import org.eclipse.sphinx.emf.metamodel.MetaModelDescriptorRegistry;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * A filter that matches object that are part of the ecuc definition (MM independent)
 */
public class EcucDefChangeNotificationFilter extends NotificationFilter.Custom
{

	/**
	 * 
	 */
	public EcucDefChangeNotificationFilter()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.transaction.NotificationFilter#matches(org.eclipse.emf.common.notify.Notification)
	 */
	@Override
	public boolean matches(Notification notification)
	{
		boolean ecucModelChanged = false;
		if (notification.getFeature() instanceof EStructuralFeature && notification.getNotifier() instanceof EObject)
		{
			EObject obj = (EObject) notification.getNotifier();

			EStructuralFeature feature = (EStructuralFeature) notification.getFeature();
			boolean isShortNameAffected = MetaModelUtils.SHORT_NAME_FEATURE.equals(feature.getName());

			if (obj instanceof GARPackage)
			{
				ecucModelChanged = true;
			}
			else if (obj instanceof GModuleConfiguration && isShortNameAffected)
			{
				ecucModelChanged = true;
			}
			else
			{
				IMetaModelDescriptor descriptor = MetaModelDescriptorRegistry.INSTANCE.getDescriptor(notification.getNotifier());
				if (descriptor != null)
				{
					IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(descriptor);
					if (mmService != null && mmService.getEcucMMService() != null)
					{
						ecucModelChanged = mmService.getEcucMMService().isEcucAPIDefinitionFeature(obj, feature);
					}
				}
			}
		}
		return ecucModelChanged;
	}
}
