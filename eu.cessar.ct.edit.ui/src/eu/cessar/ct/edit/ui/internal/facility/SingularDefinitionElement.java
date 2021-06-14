/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 4, 2010 8:21:12 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * @author uidl6458
 * 
 */
public abstract class SingularDefinitionElement extends AbstractDefinitionElement
{

	private int priority = 50;

	private String category;

	private ExtensionClassWrapper<IModelFragmentFeatureEditor> classWrapper;

	/**
	 * @param extension
	 */
	public SingularDefinitionElement(IConfigurationElement extension)
	{
		super(extension);
		category = extension.getAttribute(FacilityConstants.ATT_CATEGORY);
		if (category == null)
		{
			category = ""; //$NON-NLS-1$
		}
		String str = extension.getAttribute(FacilityConstants.ATT_PRIORITY);
		if (str != null)
		{
			try
			{
				priority = Integer.parseInt(str);
			}
			catch (NumberFormatException e)
			{ // SUPPRESS CHECKSTYLE change in future
				// ignore, will keep the default
			}
		}
		classWrapper = new ExtensionClassWrapper<IModelFragmentFeatureEditor>(extension,
			FacilityConstants.ATT_CLASS);
	}

	/**
	 * @return
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @return
	 */
	public int getPriority()
	{
		return priority;
	}

	/**
	 * @return
	 */
	public IModelFragmentFeatureEditor createNewEditor()
	{
		try
		{
			return classWrapper.newInstance();
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(e);
			return null;
		}
	}

	/**
	 * @param mmService
	 * @param object
	 * @param clz
	 * @param feature
	 * @return
	 */
	protected abstract boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
		EStructuralFeature feature);

	public IModelFragmentEditorProvider[] getEditorProviders(IMetaModelService mmService,
		EObject object, EClass clz, EStructuralFeature feature)
	{
		if (isSelected(mmService, object, clz, feature))
		{
			return new IModelFragmentEditorProvider[] {new FeatureEditorProvider(this, clz,
				feature, object)};
		}
		else
		{
			return new IModelFragmentEditorProvider[0];
		}
	}
}
