/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 29.08.2013 11:17:35
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import java.util.List;

import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;

/**
 * Thrown to indicate that an attempt has been made to access a parameter or a reference of an expected definition, but
 * it resulted to be of a different one
 * 
 * @see ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, List, Class)
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:57:42 2013 %
 * 
 *         %version: 1 %
 */
public class SEADefinitionNotOfExpectedTypeException extends AbstractSEARuntimeException
{
	private static final String MSG = "Definition not of expected type (expected :{0}, actual {1}) for {2} in {3}"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;

	/**
	 * @param owner
	 *        the SEA container on which the method was executed
	 * @param defName
	 *        used short name for the parameter/reference definition
	 * @param expectedTypes
	 *        expected definition types, derived from the name of the executed method
	 * @param actualType
	 *        actual type of the accessed definition
	 */
	public SEADefinitionNotOfExpectedTypeException(ISEAContainer owner, String defName,
		List<Class<? extends GCommonConfigurationAttributes>> expectedTypes,
		Class<? extends GCommonConfigurationAttributes> actualType)
	{
		super(NLS.bind(MSG,
			new Object[] {getExpectedTypesAsString(expectedTypes), actualType.getName(), defName, owner.toString()}));
	}

	/**
	 * @param expectedTypes
	 * @return
	 */
	private static String getExpectedTypesAsString(List<Class<? extends GCommonConfigurationAttributes>> expectedTypes)
	{
		String[] array = new String[expectedTypes.size()];
		for (int i = 0; i < expectedTypes.size(); i++)
		{
			array[i] = expectedTypes.get(i).getName();
		}
		return StringUtils.concat(array, ","); //$NON-NLS-1$
	}
}
