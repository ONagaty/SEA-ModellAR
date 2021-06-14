/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 27, 2010 5:14:33 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.convertors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.convertors.PMEnumConvertor;
import gautosar.gecucdescription.GEnumerationValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GEnumerationLiteralDef;
import gautosar.gecucparameterdef.GEnumerationParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class EnumConvertor extends PMEnumConvertor
{

	private final EEnum enumDef;
	private final EFactory enumFactory;

	/**
	 * @param enumDef
	 * @param paramDefinition
	 */
	public EnumConvertor(EEnum enumDef, GEnumerationParamDef paramDefinition)
	{
		super(enumDef, paramDefinition);
		this.enumDef = enumDef;
		enumFactory = enumDef.getEPackage().getEFactoryInstance();
	}

	private Object createInstanceFromString(String value)
	{
		EEnumLiteral literal = null;
		literal = enumDef.getEEnumLiteralByLiteral(value);
		if (literal == null)
		{
			// FIXME: warning, there is an unknown string literal in
			// the cfg
			return null;
		}
		else
		{
			return enumFactory.createFromString(enumDef, literal.getLiteral());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.PMEnumConvertor#getValueForNull()
	 */
	@Override
	public Object getValueForNull()
	{
		GConfigParameter paramDef = getConfigParameterDefinition();
		if (paramDef instanceof GEnumerationParamDef)
		{
			// check for a default value
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(paramDef.eClass());
			if (mmService != null)
			{
				Object defaultValue = null;
				IEcucMMService ecucMMService = mmService.getEcucMMService();
				if (!ecucMMService.isSetDefaultValue(paramDef))
				{
					EList<GEnumerationLiteralDef> literals = ((GEnumerationParamDef) paramDef).gGetLiterals();
					if (literals.size() > 0)
					{
						defaultValue = literals.get(0).gGetShortName();
					}

				}
				else
				{
					defaultValue = ecucMMService.getDefaultValue(paramDef);
				}
				// create instance
				Object instance;
				if (defaultValue instanceof String)
				{
					instance = createInstanceFromString((String) defaultValue);
					if (instance != null)
					{
						return instance;
					}
				}
			}
		}

		return super.getValueForNull();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.PMEnumConvertor#getValue(gautosar.gecucdescription.GEnumerationValue)
	 */
	@Override
	public Object getValue(GEnumerationValue paramValue)
	{
		if (paramValue == null)
		{
			return getValueForNull();
		}
		else
		{

			String value = paramValue.gGetValue();
			if (value != null)
			{
				Object instance = createInstanceFromString(value);
				if (instance != null)
				{
					return instance;
				}

			}
		}

		return getValueForNull();
	}
}
