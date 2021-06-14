/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 10:48:08 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import gautosar.gecucdescription.GEnumerationValue;
import gautosar.gecucparameterdef.GEnumerationParamDef;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * 
 */
public class PMEnumConvertor extends
	AbstractGAttributeConvertorWithDef<GEnumerationValue, GEnumerationParamDef, Object>
{

	private final EEnum enumDef;
	private final EFactory enumFactory;

	/**
	 * @param enumDef
	 * @param paramDefinition
	 */
	public PMEnumConvertor(EEnum enumDef, GEnumerationParamDef paramDefinition)
	{
		super(paramDefinition);
		this.enumDef = enumDef;
		enumFactory = enumDef.getEPackage().getEFactoryInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValueClass()
	 */
	public Class<Object> getValueClass()
	{
		return Object.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#createValue(gautosar.gecucparameterdef.GConfigParameter
	 * , java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	public GEnumerationValue createValue(GEnumerationParamDef paramDefinition, Object dataValue)
	{
		IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(paramDefinition);
		GEnumerationValue result = (GEnumerationValue) factory.createParameterValue(paramDefinition);
		setValue(result, dataValue);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValue(gautosar.gecucdescription.GParameterValue)
	 */
	public Object getValue(GEnumerationValue paramValue)
	{
		if (paramValue == null || !paramValue.gIsSetValue())
		{
			return getValueForNull();
		}
		else
		{
			String value = paramValue.gGetValue();
			EEnumLiteral literal = enumDef.getEEnumLiteralByLiteral(value);
			if (literal == null)
			{
				// FIXME: warning, there is an unknown string literal in
				return getValueForNull();
			}
			else
			{
				return enumFactory.createFromString(enumDef, literal.getLiteral());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValueForNull()
	 */
	public Object getValueForNull()
	{
		return EStructuralFeature.Internal.DynamicValueHolder.NIL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#isSetValue(gautosar.gecucdescription.GParameterValue
	 * )
	 */
	public boolean isSetValue(GEnumerationValue paramValue)
	{
		return paramValue != null && paramValue.gIsSetValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#setValue(gautosar.gecucdescription.GParameterValue,
	 * java.lang.Object)
	 */
	public void setValue(GEnumerationValue paramValue, Object dataValue)
	{
		if (dataValue instanceof Enumerator)
		{
			paramValue.gSetValue(((Enumerator) dataValue).getLiteral());
		}
		else
		{
			paramValue.gUnsetValue();
		}
	}

}
