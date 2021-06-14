/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 14, 2013 7:03:14 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaAttributesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaForeignReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaInstanceReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaSimpleReferencesHandler;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAInstanceReference;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Base implementation of a SEA container
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Jun 19 15:09:22 2015 %
 * 
 *         %version: 15 %
 */
public abstract class AbstractSEAContainer extends AbstractSEAContainerParent implements ISEAContainer
{

	/**
	 * @param seaModel
	 * @param optionsHolder
	 */
	public AbstractSEAContainer(ISEAModel seaModel, ISeaOptions optionsHolder)
	{
		super(seaModel, optionsHolder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getBoolean(java.lang.String)
	 */
	@Override
	public Boolean getBoolean(String defName)
	{
		return getSeaParametersHandler().getBoolean(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getBoolean(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Boolean getBoolean(String defName, Boolean defValue)
	{
		return getSeaParametersHandler().getBoolean(this, defName, defValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getBooleans(java.lang.String)
	 */
	@Override
	public ISEAList<Boolean> getBooleans(String defName)
	{
		return getSeaParametersHandler().getBooleans(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getIntegers(java.lang.String)
	 */
	@Override
	public ISEAList<BigInteger> getIntegers(String defName)
	{
		return getSeaParametersHandler().getIntegers(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getFloats(java.lang.String)
	 */
	@Override
	public ISEAList<Double> getFloats(String defName)
	{
		return getSeaParametersHandler().getFloats(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getEnums(java.lang.String)
	 */
	@Override
	public ISEAList<String> getEnums(String defName)
	{
		return getSeaParametersHandler().getEnums(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getStrings(java.lang.String)
	 */
	@Override
	public ISEAList<String> getStrings(String defName)
	{
		return getSeaParametersHandler().getStrings(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String defName, String defaultValue)
	{
		return getSeaParametersHandler().getString(this, defName, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetBoolean(java.lang.String)
	 */
	@Override
	public boolean isSetBoolean(String defName)
	{
		return getSeaParametersHandler().isSetBoolean(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetInteger(java.lang.String)
	 */
	@Override
	public boolean isSetInteger(String defName)
	{
		return getSeaParametersHandler().isSetInteger(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetFloat(java.lang.String)
	 */
	@Override
	public boolean isSetFloat(String defName)
	{
		return getSeaParametersHandler().isSetFloat(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetEnum(java.lang.String)
	 */
	@Override
	public boolean isSetEnum(String defName)
	{
		return getSeaParametersHandler().isSetEnum(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetString(java.lang.String)
	 */
	@Override
	public boolean isSetString(String defName)
	{
		return getSeaParametersHandler().isSetString(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetBoolean(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetBoolean(String defName)
	{
		getSeaParametersHandler().unSetBoolean(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetString(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetString(String defName)
	{
		getSeaParametersHandler().unSetString(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetEnum(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetEnum(String defName)
	{
		getSeaParametersHandler().unSetEnum(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetInteger(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetInteger(String defName)
	{
		getSeaParametersHandler().unSetInteger(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetFloat(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetFloat(String defName)
	{
		getSeaParametersHandler().unSetFloat(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSet(java.lang.String)
	 */
	@Override
	public ISEAContainer unSet(String defName)
	{
		getGenericSeaHandler().unSet(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSet(java.lang.String)
	 */
	@Override
	public boolean isSet(String defName)
	{
		return getGenericSeaHandler().isSet(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setBoolean(java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer setBoolean(String defName, boolean value)
	{
		getSeaParametersHandler().setBoolean(null, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setBoolean(gautosar.gecucdescription.GContainer, java.lang.String,
	 * boolean)
	 */
	@Override
	public ISEAContainer setBoolean(GContainer activeContainer, String defName, boolean value)
	{
		getSeaParametersHandler().setBoolean(activeContainer, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setBoolean(java.lang.String, boolean[])
	 */
	@Override
	public ISEAContainer setBooleans(String defName, boolean... values)
	{
		getSeaParametersHandler().setBoolean(null, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setBooleans(gautosar.gecucdescription.GContainer, java.lang.String,
	 * boolean[])
	 */
	@Override
	public ISEAContainer setBooleans(GContainer activeContainer, String defName, boolean... values)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSEAContainerParent#unSetContainer(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetContainer(String defName)
	{
		super.unSetContainer(defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getInteger(java.lang.String)
	 */
	@Override
	public BigInteger getInteger(String defName)
	{
		return getSeaParametersHandler().getInteger(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getInteger(java.lang.String, java.math.BigInteger)
	 */
	@Override
	public BigInteger getInteger(String defName, BigInteger defValue)
	{
		return getSeaParametersHandler().getInteger(this, defName, defValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getFloat(java.lang.String)
	 */
	@Override
	public Double getFloat(String defName)
	{
		return getSeaParametersHandler().getFloat(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getFloat(java.lang.String, java.lang.Double)
	 */
	@Override
	public Double getFloat(String defName, Double defaultValue)
	{
		return getSeaParametersHandler().getFloat(this, defName, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setInteger(java.lang.String, java.math.BigInteger)
	 */
	@Override
	public ISEAContainer setInteger(String defName, BigInteger value)
	{
		getSeaParametersHandler().setInteger(null, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setInteger(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.math.BigInteger)
	 */
	@Override
	public ISEAContainer setInteger(GContainer activeContainer, String defName, BigInteger value)
	{
		getSeaParametersHandler().setInteger(activeContainer, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setFloat(java.lang.String, java.lang.Double)
	 */
	@Override
	public ISEAContainer setFloat(String defName, Double value)
	{
		getSeaParametersHandler().setFloat(null, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setFloat(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.Double)
	 */
	@Override
	public ISEAContainer setFloat(GContainer activeContainer, String defName, Double value)
	{
		getSeaParametersHandler().setFloat(activeContainer, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setInteger(java.lang.String, java.math.BigInteger[])
	 */
	@Override
	public ISEAContainer setIntegers(String defName, BigInteger... values)
	{
		getSeaParametersHandler().setInteger(null, this, defName, values);
		return this;
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setIntegers(gautosar.gecucdescription.GContainer, java.lang.String,
 * java.math.BigInteger[])
 */
	@Override
	public ISEAContainer setIntegers(GContainer activeContainer, String defName, BigInteger... values)
	{
		getSeaParametersHandler().setInteger(activeContainer, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setFloat(java.lang.String, java.lang.Double[])
	 */
	@Override
	public ISEAContainer setFloats(String defName, Double... values)
	{
		getSeaParametersHandler().setFloat(null, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setFloats(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.Double[])
	 */
	@Override
	public ISEAContainer setFloats(GContainer activeContainer, String defName, Double... values)
	{
		getSeaParametersHandler().setFloat(activeContainer, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setReference(java.lang.String,
	 * gautosar.ggenericstructure.ginfrastructure.GIdentifiable)
	 */
	@Override
	public ISEAContainer setForeignReference(String defName, GIdentifiable value)
	{
		setForeignReference(null, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setForeignReference(gautosar.gecucdescription.GContainer,
	 * java.lang.String, gautosar.ggenericstructure.ginfrastructure.GIdentifiable)
	 */
	@Override
	public ISEAContainer setForeignReference(GContainer activeContainer, String defName, GIdentifiable value)
	{
		getSeaForeignReferencesHandler().setReference(activeContainer, this, defName, value);
		return this;
	}

	private SeaAttributesHandler getGenericSeaHandler()
	{
		return SeaHandlersManager.getSeaAttributesHandler(getSEAModel(), getSeaOptionsHolder());
	}

	private ISeaParametersHandler getSeaParametersHandler()
	{
		ISeaParametersHandler handler = SeaHandlersManager.getSeaParametersHandler(getSEAModel(), getSeaOptionsHolder());
		return handler;
	}

	private SeaForeignReferencesHandler getSeaForeignReferencesHandler()
	{
		SeaForeignReferencesHandler handler = SeaHandlersManager.getSeaForeignReferencesHandler(getSEAModel(),
			getSeaOptionsHolder());
		return handler;
	}

	private SeaSimpleReferencesHandler getSeaSimpleReferencesHandler()
	{
		SeaSimpleReferencesHandler handler = SeaHandlersManager.getSeaSimpleReferencesHandler(getSEAModel(),
			getSeaOptionsHolder());
		return handler;
	}

	private SeaInstanceReferencesHandler getSeaInstanceReferencesHandler()
	{
		return SeaHandlersManager.getSeaInstanceReferencesHandler(getSEAModel(), getSeaOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setForeignReference(java.lang.String, java.util.List)
	 */
	@Override
	public ISEAContainer setForeignReferences(String defName, List<GIdentifiable> values)
	{
		setForeignReferences(null, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setForeignReferences(gautosar.gecucdescription.GContainer,
	 * java.lang.String, java.util.List)
	 */
	@Override
	public ISEAContainer setForeignReferences(GContainer activeContainer, String defName, List<GIdentifiable> values)
	{
		getSeaForeignReferencesHandler().setReference(activeContainer, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getForeignReference(java.lang.String)
	 */
	@Override
	public GIdentifiable getForeignReference(String defName)
	{
		return getSeaForeignReferencesHandler().getReference(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getForeignReferences(java.lang.String)
	 */
	@Override
	public ISEAList<GIdentifiable> getForeignReferences(String defName)
	{
		return getSeaForeignReferencesHandler().getReferences(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getEnum(java.lang.String)
	 */
	@Override
	public String getEnum(String defName)
	{
		return getSeaParametersHandler().getEnum(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getString(java.lang.String)
	 */
	@Override
	public String getString(String defName)
	{
		return getSeaParametersHandler().getString(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getEnum(java.lang.String, java.lang.String)
	 */
	@Override
	public String getEnum(String defName, String defValue)
	{
		return getSeaParametersHandler().getEnum(this, defName, defValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setEnum(java.lang.String, java.lang.String[])
	 */
	@Override
	public ISEAContainer setEnums(String defName, String... values)
	{
		getSeaParametersHandler().setEnum(null, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setEnums(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public ISEAContainer setEnums(GContainer activeContainer, String defName, String... values)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setEnum(java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer setEnum(String defName, String value)
	{
		setEnum(null, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setEnum(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ISEAContainer setEnum(GContainer activeContainer, String defName, String value)
	{
		getSeaParametersHandler().setEnum(activeContainer, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer setString(String defName, String value)
	{
		getSeaParametersHandler().setString(null, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setString(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ISEAContainer setString(GContainer activeContainer, String defName, String value)
	{
		getSeaParametersHandler().setString(activeContainer, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setString(java.lang.String, java.lang.String[])
	 */
	@Override
	public ISEAContainer setStrings(String defName, String... values)
	{
		getSeaParametersHandler().setString(null, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setStrings(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public ISEAContainer setStrings(GContainer activeContainer, String defName, String... values)
	{
		getSeaParametersHandler().setString(activeContainer, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getReference(java.lang.String)
	 */
	@Override
	public ISEAContainer getReference(String defName)
	{
		return getSeaSimpleReferencesHandler().getReference(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getReferences(java.lang.String)
	 */
	@Override
	public ISEAList<ISEAContainer> getReferences(String defName)
	{
		return getSeaSimpleReferencesHandler().getReferences(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setReference(java.lang.String, eu.cessar.ct.sdk.sea.ISEAContainer)
	 */
	@Override
	public ISEAContainer setReference(String defName, ISEAContainer value)
	{
		setReference(null, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setReference(gautosar.gecucdescription.GContainer, java.lang.String,
	 * eu.cessar.ct.sdk.sea.ISEAContainer)
	 */
	@Override
	public ISEAContainer setReference(GContainer activeContainer, String defName, ISEAContainer value)
	{
		getSeaSimpleReferencesHandler().setReference(activeContainer, this, defName, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setReference(java.lang.String, java.util.List)
	 */
	@Override
	public ISEAContainer setReferences(String defName, List<ISEAContainer> values)
	{
		getSeaSimpleReferencesHandler().setReference(null, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#setReferences(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.util.List)
	 */
	@Override
	public ISEAContainer setReferences(GContainer activeContainer, String defName, List<ISEAContainer> values)
	{
		getSeaSimpleReferencesHandler().setReference(activeContainer, this, defName, values);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetForeignReference(java.lang.String)
	 */
	@Override
	public boolean isSetForeignReference(String defName)
	{
		return getSeaForeignReferencesHandler().isSet(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetReference(java.lang.String)
	 */
	@Override
	public boolean isSetReference(String defName)
	{
		return getSeaSimpleReferencesHandler().isSet(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetForeignReference(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetForeignReference(String defName)
	{
		getSeaForeignReferencesHandler().unSet(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetReference(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetReference(String defName)
	{
		getSeaSimpleReferencesHandler().unSet(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getInstanceRef(java.lang.String)
	 */
	@Override
	public ISEAInstanceReference getInstanceReference(String defName)
	{
		return getSeaInstanceReferencesHandler().getReference(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#getInstanceRefs(java.lang.String)
	 */
	@Override
	public ISEAList<ISEAInstanceReference> getInstanceReferences(String defName)
	{
		return getSeaInstanceReferencesHandler().getReferences(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#isSetInstanceRef(java.lang.String)
	 */
	@Override
	public boolean isSetInstanceReference(String defName)
	{
		return getSeaInstanceReferencesHandler().isSet(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#unSetInstanceRef(java.lang.String)
	 */
	@Override
	public ISEAContainer unSetInstanceReference(String defName)
	{
		getSeaInstanceReferencesHandler().unSet(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createContainer(gautosar.gecucdescription.GContainer, java.lang.String)
	 */
	@Override
	public ISEAContainer createContainer(GContainer activeContainer, String defName)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createContainer(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createContainer(GContainer activeContainer, String defName, String shortName,
		boolean deriveNameFromSuggestion)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createContainer(gautosar.gecucdescription.GContainer, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ISEAContainer createContainer(GContainer activeContainer, String defName, String shortName)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createChoiceContainer(gautosar.gecucdescription.GContainer,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer createChoiceContainer(GContainer activeContainer, String defName, String chosenDefName)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createChoiceContainer(gautosar.gecucdescription.GContainer,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer createChoiceContainer(GContainer activeContainer, String defName, String chosenDefName,
		String shortName)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createChoiceContainer(gautosar.gecucdescription.GContainer,
	 * java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createChoiceContainer(GContainer activeContainer, String defName, String chosenDefName,
		String shortName, boolean deriveNameFromSuggestion)
	{
		// TODO: implement
		throw new UnsupportedOperationException();
	}

	/**
	 * The equality implies that the {@link #arGetContainers()} contain the same elements, but not necessarily in the
	 * same order
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ISEAContainer)
		{
			List<GContainer> containers1 = ((ISEAContainer) obj).arGetContainers();
			List<GContainer> containers2 = arGetContainers();

			int size1 = containers1.size();
			int size2 = containers2.size();

			return size1 == size2 && containers1.containsAll(containers2);

		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(19, 39);

		List<GContainer> containers = arGetContainers();

		List<GContainer> copyContainers = new ArrayList<GContainer>(containers);
		if (copyContainers.size() > 1)
		{
			sort(copyContainers);
		}

		for (GContainer container: copyContainers)
		{
			hashCodeBuilder.append(container.hashCode());
		}

		return hashCodeBuilder.toHashCode();

	}

}
