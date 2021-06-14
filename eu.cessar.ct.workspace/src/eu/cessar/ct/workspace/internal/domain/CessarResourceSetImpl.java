/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 22.04.2013 14:21:12
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.common.resource.impl.AutosarResourceFactoryImpl;
import org.artop.aal.common.resource.impl.AutosarResourceSetImpl;
import org.artop.aal.common.resource.impl.AutosarXMLResourceImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.resource.ExtendedResource;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;

import eu.cessar.ct.core.mms.ResourcesComparator;
import eu.cessar.ct.core.platform.util.ReflectionUtils;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;

/**
 * Class implementing Resources management
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Mon Feb 10 12:10:28 2014 %
 * 
 *         %version: 7 %
 */
@SuppressWarnings("restriction")
@Requirement(
	reqID = "REQ_NFR#CORE#1")
public class CessarResourceSetImpl extends AutosarResourceSetImpl implements IUpdatableModelChangeStampProvider
{

	private AtomicLong stamp = new AtomicLong();

	private ResourceSetListener resourceLoadedListener;

	@Override
	public Resource createResource(URI uri, String contentType)
	{
		Resource resource = super.createResource(uri, contentType);
		if (resource instanceof AutosarXMLResourceImpl)
		{
			AutosarXMLResourceImpl arResource = ((AutosarXMLResourceImpl) resource);
			arResource.getDefaultSaveOptions().put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
				Resource.OPTION_SAVE_ONLY_IF_CHANGED_FILE_BUFFER);
			checkResourceDescriptor(arResource, uri, contentType);
		}
		if (resourceLoadedListener == null)
		{
			resourceLoadedListener = createResourceChangedListener();
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(resource);
			editingDomain.addResourceSetListener(resourceLoadedListener);
		}
		return resource;
	}

	/**
	 * Check the assigned ExtendedResource.OPTION_RESOURCE_VERSION_DESCRIPTOR of the resource load options. If set to
	 * null, it will be changed to the native version of the metamodel
	 * 
	 * @param contentType
	 * @param uri
	 * @param arResource
	 * 
	 */
	private void checkResourceDescriptor(AutosarXMLResourceImpl arResource, URI uri, String contentType)
	{
		Map<Object, Object> options = arResource.getDefaultLoadOptions();
		if (options.get(ExtendedResource.OPTION_RESOURCE_VERSION_DESCRIPTOR) == null)
		{
			// put the native resource version inside
			Resource.Factory resourceFactory = getResourceFactoryRegistry().getFactory(uri, contentType);
			if (resourceFactory instanceof AutosarResourceFactoryImpl)
			{
				// get the autosarRelease member from inside using reflection
				try
				{
					Object value = ReflectionUtils.getFieldValue(AutosarResourceFactoryImpl.class, resourceFactory,
						"autosarRelease"); //$NON-NLS-1$
					if (value instanceof AutosarReleaseDescriptor)
					{
						options.put(ExtendedResource.OPTION_RESOURCE_VERSION_DESCRIPTOR, value);
					}
					else
					{
						CessarPluginActivator.getDefault().logError(
							"AutosarResourceFactoryImpl.autosarRelease is not an AutosarReleaseDescriptor but it {0}", value); //$NON-NLS-1$						
					}
				}
				catch (NoSuchFieldException e)
				{
					CessarPluginActivator.getDefault().logError(e, "Internal error"); //$NON-NLS-1$
				}
				catch (IllegalAccessException e)
				{
					CessarPluginActivator.getDefault().logError(e, "Internal error"); //$NON-NLS-1$
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.IModelChangeStampProvider#getCurrentChangeStamp()
	 */
	@Override
	public long getCurrentChangeStamp()
	{
		return stamp.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.internal.domain.IUpdatableModelChangeStampProvider#modelChanged()
	 */
	@Override
	public void modelChanged()
	{
		stamp.incrementAndGet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.sphinx.emf.resource.ExtendedResourceSetImpl#getResources()
	 */
	@Override
	public EList<Resource> getResources()
	{
		if (resources == null)
		{
			resources = new CessarExtendedResourcesEList<Resource>();
		}
		return resources;
	}

	private ResourceSetListener createResourceChangedListener()
	{
		return new ResourceSetListenerImpl(NotificationFilter.createFeatureFilter(
			EcorePackage.eINSTANCE.getEResource(), Resource.RESOURCE__URI))
		{
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event)
			{
				// Retrieve loaded resources from notification
				List<?> notifications = event.getNotifications();
				for (Object object: notifications)
				{
					if (object instanceof Notification)
					{
						Notification notification = (Notification) object;

						if (notification.getOldValue() instanceof URI)
						{
							Resource resource = EcorePlatformUtil.getResource((URI) notification.getNewValue());

							// remove and add again the modified resource (URI changed or resource moved) from
							// "resources" EList
							// in order to obtain a sorted list; it is not sorted anymore due to call of
							// resource.setURI(newURI) that occurred before this point
							if (resource != null && resource.isLoaded())
							{
								getResources().remove(resource);
								getResources().add(resource);
							}
						}

					}
				}
			}
		};

	}

	/**
	 * Supporting class for sorted resources list
	 * 
	 * @author uidv3687
	 * 
	 *         %created_by: uidl6458 %
	 * 
	 *         %date_created: Mon Feb 10 12:10:28 2014 %
	 * 
	 *         %version: 7 %
	 * @param <E>
	 */
	protected class CessarExtendedResourcesEList<E extends Object & Resource> extends ExtendedResourcesEList<E>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(E object)
		{
			int index = Collections.binarySearch(this, object, ResourcesComparator.INSTANCE);
			if (index < 0)
			{
				index = ~index;
			}
			super.add(index, object);
			return true;
		}

	}

}
