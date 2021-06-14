/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Apr 4, 2014 10:36:51 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.jvmparameters;

import eu.cessar.req.Requirement;

/**
 * JVMParameter class - holds the encapsulation of a JVM parameter for configuration dialogs handling.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Tue Apr 15 10:40:11 2014 %
 * 
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_WORKSP#ACTIVE_JVM#2")
public class JVMParameter
{

	/** The parameter name. */
	private String parameterName;

	/** The parameter description. */
	private String parameterDescription;

	/** The parameter value. */
	private String parameterValue;

	/** The file parameter value. */
	private String fileParameterValue;

	/** The size unit. */
	private JVMSizeUnitType sizeUnit;

	/**
	 * Instantiates a new JVM parameter.
	 */
	public JVMParameter()
	{

	}

	/**
	 * Instantiates a new JVM parameter.
	 * 
	 * @param parameterName
	 *        the parameter name
	 * @param parameterDescription
	 *        the parameter description
	 * @param parameterValue
	 *        the parameter value
	 * @param sizeUnit
	 *        the size unit
	 */
	public JVMParameter(String parameterName, String parameterDescription, String parameterValue,
		JVMSizeUnitType sizeUnit)
	{
		super();
		this.parameterName = parameterName;
		this.parameterDescription = parameterDescription;
		this.parameterValue = parameterValue;
		this.sizeUnit = sizeUnit;
	}

	/**
	 * Gets the parameter name.
	 * 
	 * @return the parameterName
	 */
	public String getParameterName()
	{
		return parameterName;
	}

	/**
	 * Sets the parameter name.
	 * 
	 * @param parameterName
	 *        the parameterName to set
	 */
	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}

	/**
	 * Gets the parameter description.
	 * 
	 * @return the parameterDescription
	 */
	public String getParameterDescription()
	{
		return parameterDescription;
	}

	/**
	 * Sets the parameter description.
	 * 
	 * @param parameterDescription
	 *        the parameterDescription to set
	 */
	public void setParameterDescription(String parameterDescription)
	{
		this.parameterDescription = parameterDescription;
	}

	/**
	 * Gets the parameter value.
	 * 
	 * @return the parameterValue
	 */
	public String getParameterValue()
	{
		return parameterValue;
	}

	/**
	 * Sets the parameter value.
	 * 
	 * @param parameterValue
	 *        the parameter value
	 * @param sizeUnit
	 *        the size unit
	 */
	public void setParameterValue(String parameterValue, JVMSizeUnitType sizeUnit)
	{
		this.parameterValue = parameterValue;
		this.sizeUnit = sizeUnit;

		if ((parameterValue != null) && (!parameterValue.isEmpty()))
		{
			if (Integer.parseInt(parameterValue) > 0)
			{
				fileParameterValue = "-"; //$NON-NLS-1$
				fileParameterValue = fileParameterValue.concat(getParameterName());
				fileParameterValue = fileParameterValue.concat(parameterValue);
				fileParameterValue = fileParameterValue.concat(sizeUnit.equals(JVMSizeUnitType.MB) ? "M" : "G"); //$NON-NLS-1$ //$NON-NLS-2$
				setFileParameterValue(fileParameterValue);
			}
		}
	}

	/**
	 * @return the fileParameterValue
	 */
	public String getFileParameterValue()
	{
		return fileParameterValue;
	}

	/**
	 * @param fileParameterValue
	 *        the fileParameterValue to set
	 */
	private void setFileParameterValue(String fileParameterValue)
	{
		this.fileParameterValue = fileParameterValue;
	}

	/**
	 * Gets the size unit.
	 * 
	 * @return the sizeUnit
	 */
	public JVMSizeUnitType getSizeUnit()
	{
		return sizeUnit;
	}
}
