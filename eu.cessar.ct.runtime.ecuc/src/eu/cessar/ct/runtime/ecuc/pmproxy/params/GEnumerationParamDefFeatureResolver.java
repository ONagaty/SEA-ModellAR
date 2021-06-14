package eu.cessar.ct.runtime.ecuc.pmproxy.params;

import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.PMEnumConvertor;
import gautosar.gecucdescription.GEnumerationValue;
import gautosar.gecucparameterdef.GEnumerationParamDef;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;

public class GEnumerationParamDefFeatureResolver extends
	GConfigParameterFeatureResolver<GEnumerationValue, GEnumerationParamDef, Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getAttributeValueClass()
	 */
	@Override
	protected Class<GEnumerationValue> getParameterValueClass()
	{
		return GEnumerationValue.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getParameterDefinitionClass()
	 */
	@Override
	protected Class<GEnumerationParamDef> getParameterDefinitionClass()
	{
		return GEnumerationParamDef.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getConvertor()
	 */
	@Override
	protected IParameterValueConvertor<GEnumerationValue, GEnumerationParamDef, Object> getConvertor(
		EAttribute attribute, GEnumerationParamDef paramDefinition)
	{
		EEnum eEnum = (EEnum) attribute.getEType();
		return new PMEnumConvertor(eEnum, paramDefinition);
	}

}
