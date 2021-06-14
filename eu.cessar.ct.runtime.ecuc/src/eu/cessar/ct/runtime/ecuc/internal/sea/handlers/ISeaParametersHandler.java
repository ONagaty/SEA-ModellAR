/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 15.05.2013 10:37:17
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers;

import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GConfigParameter;

import java.math.BigInteger;

/**
 * Sea handler for manipulating parameter values
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Jan 16 11:38:08 2014 %
 * 
 *         %version: 3 %
 */
public interface ISeaParametersHandler extends ISeaAttributesHandler<GConfigParameter>
{
	@SuppressWarnings("javadoc")
	public Boolean getBoolean(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public BigInteger getInteger(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public Double getFloat(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public String getEnum(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public String getString(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public Boolean getBoolean(ISEAContainer parent, String defName, Boolean defaultValue);

	@SuppressWarnings("javadoc")
	public BigInteger getInteger(ISEAContainer parent, String defName, BigInteger defaultValue);

	@SuppressWarnings("javadoc")
	public Double getFloat(ISEAContainer parent, String defName, Double defaultValue);

	@SuppressWarnings("javadoc")
	public String getEnum(ISEAContainer parent, String defName, String defaultValue);

	@SuppressWarnings("javadoc")
	public String getString(ISEAContainer parent, String defName, String defaultValue);

	@SuppressWarnings("javadoc")
	public ISEAList<Boolean> getBooleans(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public ISEAList<BigInteger> getIntegers(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public ISEAList<Double> getFloats(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public ISEAList<String> getEnums(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public ISEAList<String> getStrings(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public boolean isSetBoolean(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public boolean isSetInteger(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public boolean isSetFloat(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public boolean isSetEnum(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public boolean isSetString(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public void unSetBoolean(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public void unSetInteger(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public void unSetFloat(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public void unSetEnum(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public void unSetString(ISEAContainer parent, String defName);

	@SuppressWarnings("javadoc")
	public void setBoolean(GContainer activeContainer, ISEAContainer parent, String defName, boolean value);

	@SuppressWarnings("javadoc")
	public void setInteger(GContainer activeContainer, ISEAContainer parent, String defName, BigInteger value);

	@SuppressWarnings("javadoc")
	public void setFloat(GContainer activeContainer, ISEAContainer parent, String defName, Double value);

	@SuppressWarnings("javadoc")
	public void setEnum(GContainer activeContainer, ISEAContainer parent, String defName, String value);

	@SuppressWarnings("javadoc")
	public void setString(GContainer activeContainer, ISEAContainer parent, String defName, String value);

	@SuppressWarnings("javadoc")
	public void setBoolean(GContainer activeContainer, ISEAContainer parent, String defName, boolean... value);

	@SuppressWarnings("javadoc")
	public void setInteger(GContainer activeContainer, ISEAContainer parent, String defName, BigInteger... values);

	@SuppressWarnings("javadoc")
	public void setFloat(GContainer activeContainer, ISEAContainer parent, String defName, Double... values);

	@SuppressWarnings("javadoc")
	public void setEnum(GContainer activeContainer, ISEAContainer parent, String defName, String... values);

	@SuppressWarnings("javadoc")
	public void setString(GContainer activeContainer, ISEAContainer parent, String defName, String... values);

}
