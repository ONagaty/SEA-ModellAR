/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 4, 2010 8:20:21 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;

/**
 * @author uidl6458
 * 
 */
public class ClassifierDefinitionElement extends SingularDefinitionElement
{

	public static enum EFeatureType
	{
		PRIMITIVE
		{
			/* (non-Javadoc)
			 * @see eu.cessar.ct.edit.ui.internal.facility.ClassifierDefinitionElement.EFeatureType#match(org.eclipse.emf.ecore.EStructuralFeature)
			 */
			@Override
			public boolean match(EStructuralFeature feature)
			{
				if (feature instanceof EAttribute)
				{
					EAttribute attr = (EAttribute) feature;
					return !(attr.getEAttributeType() instanceof EEnum);
				}
				return false;
			}
		},
		ENUM
		{
			/* (non-Javadoc)
			 * @see eu.cessar.ct.edit.ui.internal.facility.ClassifierDefinitionElement.EFeatureType#match(org.eclipse.emf.ecore.EStructuralFeature)
			 */
			@Override
			public boolean match(EStructuralFeature feature)
			{
				if (feature instanceof EAttribute)
				{
					EAttribute attr = (EAttribute) feature;
					return attr.getEAttributeType() instanceof EEnum;
				}
				return false;
			}

		},
		REFERENCE
		{
			/* (non-Javadoc)
			 * @see eu.cessar.ct.edit.ui.internal.facility.ClassifierDefinitionElement.EFeatureType#match(org.eclipse.emf.ecore.EStructuralFeature)
			 */
			@Override
			public boolean match(EStructuralFeature feature)
			{
				return feature instanceof EReference;
			}

		};

		/**
		 * @param feature
		 * @return
		 */
		public abstract boolean match(EStructuralFeature feature);

		/**
		 * @param feature
		 * @return
		 */
		public static EFeatureType getFeatureType(EStructuralFeature feature)
		{
			if (REFERENCE.match(feature))
			{
				return REFERENCE;
			}
			else if (ENUM.match(feature))
			{
				return ENUM;
			}
			else
			{
				return PRIMITIVE;
			}
		}
	}

	private EFeatureType featureType;

	private String classifierClass;

	private String classifierType;

	private boolean isDefault;

	/**
	 * @param extension
	 */
	public ClassifierDefinitionElement(IConfigurationElement extension)
	{
		super(extension);
		String str = extension.getAttribute(FacilityConstants.ATT_FEATURE_TYPE);
		featureType = EFeatureType.valueOf(str);
		classifierClass = extension.getAttribute(FacilityConstants.ATT_CLASSIFIER_CLASS);
		classifierType = extension.getAttribute(FacilityConstants.ATT_CLASSIFIER_TYPE);
		isDefault = Boolean.parseBoolean(extension.getAttribute(FacilityConstants.ATT_DEFAULT));
	}

	/**
	 * @return
	 */
	public boolean isDefault()
	{
		return isDefault;
	}

	/**
	 * @return
	 */
	public EFeatureType getFeatureType()
	{
		return featureType;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.internal.facility.SingularDefinitionElement#isSelected(eu.cessar.ct.core.mms.IMetaModelService, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
		EStructuralFeature feature)
	{
		boolean isSelected = false;

		if (featureType.match(feature))
		{
			switch (featureType)
			{
				case ENUM:
				case PRIMITIVE:
					isSelected = isSelectedEnumOrPrimitive(feature);
					break;
				case REFERENCE:
					isSelected = isSelectedReference(feature);
					break;
				default:
					isSelected = false;
			}
		}
		return isSelected;
	}

	private boolean isSelectedEnumOrPrimitive(EStructuralFeature feature)
	{
		boolean isSelected = false;

		EAttribute eAttribute = (EAttribute) feature;
		EDataType eDataType = eAttribute.getEAttributeType();
		if (eDataType != null)
		{
			String className = eDataType.getInstanceClassName();
			String typeName = eDataType.getName();
			if (className.matches(classifierClass) && typeName.matches(classifierType))
			{
				isSelected = true;
			}
		}
		return isSelected;
	}

	private boolean isSelectedReference(EStructuralFeature feature)
	{
		boolean isSelected = false;

		EReference eReference = (EReference) feature;
		EClass refType = eReference.getEReferenceType();
		if (refType != null)
		{
			String className = refType.getInstanceClassName();
			String typeName = refType.getInstanceTypeName();
			if (!eReference.isContainment() && className.matches(classifierClass)
				&& typeName.matches(classifierType))
			{
				isSelected = true;
			}
		}

		return isSelected;
	}

}
