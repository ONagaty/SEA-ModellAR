/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4449<br/>
 * Nov 21, 2013 2:32:31 PM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.validation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.sphinx.emf.validation.ui.views.ConcreteMarker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.edit.ui.internal.Messages;

/**
 * Enumeration Class for creating the Validation Table
 *
 * @author uidg4449
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Sep 17 14:46:13 2014 %
 *
 *         %version: 1 %
 */
public enum EValidationTableColumn
{
	/**
	 * EValidationTableColumn is used for the validation table, It provides the column names, column width, text and
	 * Icons
	 */
	PROBLEM_TYPE(Messages.Validation_Problem_Type, 60)
	{
		@Override
		public Image getIcon(ConcreteMarker marker)
		{
			return null;

		}

		@Override
		public String getText(ConcreteMarker marker)
		{
			int severity = marker.getMarker().getAttribute(IMarker.SEVERITY, -1);
			String text = ""; //$NON-NLS-1$

			switch (severity)
			{
				case IMarker.SEVERITY_ERROR:
					text = "Error"; //$NON-NLS-1$
					break;
				case IMarker.SEVERITY_WARNING:
					text = "Warning"; //$NON-NLS-1$
					break;
				case IMarker.SEVERITY_INFO:
					text = "Info"; //$NON-NLS-1$
					break;
				default:
			}
			return text;
		}
	},

	/**
	 *
	 */
	DESCRIPTION(Messages.Validation_Description, 350)
	{
		@Override
		public Image getIcon(ConcreteMarker marker)
		{

			int severity = marker.getMarker().getAttribute(IMarker.SEVERITY, -1);

			Image img = null;

			switch (severity)
			{
				case IMarker.SEVERITY_ERROR:
					img = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
					break;
				case IMarker.SEVERITY_WARNING:
					img = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
					break;
				case IMarker.SEVERITY_INFO:
					img = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
					break;
				default:
			}

			return img;

		}

		@Override
		public String getText(ConcreteMarker marker)
		{
			return marker.getDescription();
		}
	},

	/**
	 *
	 */
	OBJECT(Messages.Validation_Object, 100)
	{
		@Override
		public Image getIcon(ConcreteMarker marker)
		{

			EObject eObject = null;
			IResource resource = marker.getResource();

			EPackage ePackage = MetaModelUtils.getAutosarModelDescriptor(resource.getProject()).getMetaModelDescriptor().getRootEPackage();

			EClassifier eClassifier = ePackage.getEClassifier(marker.getEObjectType());
			if (eClassifier != null && eClassifier instanceof EClass)
			{

				eObject = ePackage.getEFactoryInstance().create((EClass) eClassifier);
				ComposeableAdapterFactory adaptarFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
				IItemLabelProvider provider = (IItemLabelProvider) adaptarFactory.adapt(eObject,
					IItemLabelProvider.class);
				return ExtendedImageRegistry.getInstance().getImage(provider.getImage(eObject));
			}
			return null;

		}

		@Override
		public String getText(ConcreteMarker marker)
		{
			return marker.getEObjectID();
		}
	},

	/**
	 *
	 */
	URI(Messages.Validation_Uri, 300)
	{
		@Override
		public Image getIcon(ConcreteMarker marker)
		{
			return null;

		}

		@Override
		public String getText(ConcreteMarker marker)
		{
			return marker.getURIToDisplay();
		}
	},

	/**
	 *
	 */
	RESOURCE(Messages.Validation_Resource, 150)
	{
		@Override
		public Image getIcon(ConcreteMarker marker)
		{
			return null;

		}

		@Override
		public String getText(ConcreteMarker marker)
		{
			return marker.getResourceName();
		}
	},

	/**
	 *
	 */
	EOBJECT_TYPE(Messages.Validation_EObjectType, 250)
	{
		@Override
		public Image getIcon(ConcreteMarker marker)
		{
			return null;

		}

		@Override
		public String getText(ConcreteMarker marker)
		{
			return marker.getEObjectType();
		}

	};

	private final String name;

	private final int width;

	/**
	 *
	 */
	private EValidationTableColumn(String name, int width)
	{
		this.name = name;
		this.width = width;
	}

	/**
	 * Return Icon if used
	 *
	 * @param marker
	 *
	 * @param obj
	 *        Object
	 *
	 * @return Image
	 */
	public abstract Image getIcon(ConcreteMarker marker);

	/**
	 * @return String Name of Column
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return Int Name of Column Width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param marker
	 * @return String
	 */
	public abstract String getText(ConcreteMarker marker);

}
