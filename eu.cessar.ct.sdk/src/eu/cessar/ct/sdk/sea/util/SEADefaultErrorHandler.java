/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 14:12:14
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAObject;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Default implementation of the {@link ISEAErrorHandler}
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jan 20 14:02:32 2014 %
 * 
 *         %version: 4 %
 */
@Requirement(
	reqID = "REQ_API#SEA#10")
public class SEADefaultErrorHandler implements ISEAErrorHandler
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#multipleDefinitionsFound(eu.cessar.ct.sdk.sea.ISEAObject,
	 * java.lang.String, java.util.List)
	 */
	@Override
	public GARObject multipleDefinitionsFound(ISEAObject parent, String defName, List<? extends GARObject> result)
	{
		throw new SEAMultipleDefinitionsFoundException(parent, defName, result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#noDefinitionFound(eu.cessar.ct.sdk.sea.ISEAObject,
	 * java.lang.String)
	 */
	@Override
	public GARObject noDefinitionFound(ISEAObject parent, String defName)
	{
		throw new SEANoDefinitionFoundException(parent, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#valueNotOfDestinationType(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, org.eclipse.emf.ecore.EClass, java.util.List)
	 */
	@Override
	public void valueNotOfDestinationType(ISEAContainer owner, String defName, EClass allowedDef,
		List<GIdentifiable> values)
	{
		// by default, do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#definitionNotOfExpectedType(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.util.List, java.lang.Class)
	 */
	@Override
	public void definitionNotOfExpectedType(ISEAContainer owner, String defName,
		List<Class<? extends GCommonConfigurationAttributes>> expectedTypes,
		Class<? extends GCommonConfigurationAttributes> actualType)
	{
		throw new SEADefinitionNotOfExpectedTypeException(owner, defName, expectedTypes, actualType);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#valueNotOfDestinationType(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public void valueNotOfDestinationType(ISEAContainer owner, String defName,
		List<GParamConfContainerDef> allowedDefs, List<ISEAContainer> values)
	{
		// by default, do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#multipleValuesFound(eu.cessar.ct.sdk.sea.ISEAObject,
	 * java.lang.String, java.util.List)
	 */
	@Override
	public <T> T multipleValuesFound(ISEAObject parent, String pathFragment, List<T> result)
	{
		Assert.isTrue(result.size() > 1);
		// return the first one
		return result.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#definitionNotAllowed(eu.cessar.ct.sdk.sea.ISEAContainerParent,
	 * java.util.List)
	 */
	@Override
	public void definitionNotAllowed(ISEAContainerParent owner, List<ISEAContainer> childContainers)
	{
		// by default, do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#enumLiteralNotFound(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public String enumLiteralNotFound(ISEAContainer container, String defName, List<String> acceptedLiterals,
		String value)
	{
		// default
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#wrongActiveConfiguration(eu.cessar.ct.sdk.sea.ISEAConfig,
	 * gautosar.gecucdescription.GModuleConfiguration)
	 */
	@Override
	public void wrongActiveConfiguration(ISEAConfig config, GModuleConfiguration activeConfiguration)
	{
		throw new SEAWrongActiveConfigurationException(config, activeConfiguration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.util.ISEAErrorHandler#wrongActiveContainer(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * gautosar.gecucdescription.GContainer)
	 */
	@Override
	public void wrongActiveContainer(ISEAContainer container, GContainer activeContainer)
	{
		throw new SEAWrongActiveContainerException(container, activeContainer);
	}

}
