/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Apr 8, 2014 12:52:53 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.jvmparameters;

import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.workspace.configuration.CessarIniFileUpdater;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.Messages;
import eu.cessar.req.Requirement;

/**
 * JVMParametersUtils class.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Apr 16 12:03:36 2014 %
 * 
 *         %version: 4 %
 */
@Requirement(
	reqID = "REQ_WORKSP#ACTIVE_JVM#2")
public final class JVMParametersUtils
{
	/** The JVM parameters map. */
	private static Map<String, JVMParameter> parametersMap;

	private JVMParametersUtils()
	{
	}

	/**
	 * Gets the JVM environment parameters.
	 * 
	 */
	public static void getJVMEnvironmentParameters()
	{
		parametersMap = new HashMap<String, JVMParameter>();

		if ((JVMConstants.JVMParameterList != null) && (JVMConstants.JVMParameterDescriptionList != null)
			&& (JVMConstants.JVMParameterList.length == JVMConstants.JVMParameterDescriptionList.length))
		{
			for (int i = 0; i < JVMConstants.JVMParameterList.length; i++)
			{
				JVMParameter jvmParameter = new JVMParameter(JVMConstants.JVMParameterList[i],
					JVMConstants.JVMParameterDescriptionList[i],
					getParameterValueByName(getJVMEnvironmentParameter(JVMConstants.JVMParameterList[i])),
					getParameterSizeUnitByName(getJVMEnvironmentParameter(JVMConstants.JVMParameterList[i])));

				parametersMap.put(jvmParameter.getParameterName(), jvmParameter);
			}
		}
	}

	/**
	 * Gets the JVM parameter by name.
	 * 
	 * @param parameterName
	 *        the parameter name
	 * @return the JVM parameter by name
	 */
	public static JVMParameter getJVMParameterByName(String parameterName)
	{
		if (parametersMap.containsKey(parameterName))
		{
			return parametersMap.get(parameterName);
		}
		return null;
	}

	/**
	 * Sets the JVM parameter by name.
	 * 
	 * @param parameterName
	 *        the parameter name
	 * @param jvmParameter
	 *        the JVM parameter
	 * @return true, if successful
	 */
	public static boolean setJVMParameterByName(String parameterName, JVMParameter jvmParameter)
	{
		if ((parameterName != null) && (!parameterName.isEmpty()) && (jvmParameter != null))
		{
			parametersMap.put(parameterName, jvmParameter);
			return true;
		}
		return false;
	}

	/**
	 * Gets the JVM parameter from environment.
	 * 
	 * @param jvmParameterName
	 *        the JVM parameter name
	 * @return the JVM argument value
	 */
	public static String getJVMEnvironmentParameter(String jvmParameterName)
	{
		String result = ""; //$NON-NLS-1$
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		List<String> paramList = bean.getInputArguments();

		if (paramList != null)
		{
			for (int i = 0; i < paramList.size(); i++)
			{
				if (paramList.get(i).toUpperCase().contains(jvmParameterName.toUpperCase()))
				{
					result = paramList.get(i);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Gets the parameter value by name.
	 * 
	 * @param parameterName
	 *        the parameter name
	 * @return the parameter value
	 */
	private static String getParameterValueByName(String parameterName)
	{
		String paramValue = ""; //$NON-NLS-1$

		if (parameterName != null && !parameterName.isEmpty())
		{
			paramValue = parameterName.replaceAll("\\D+", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return paramValue;
	}

	/**
	 * Gets the parameter size unit by name.
	 * 
	 * @param parameterName
	 *        the parameter name
	 * @return the parameter size unit
	 */
	private static JVMSizeUnitType getParameterSizeUnitByName(String parameterName)
	{
		JVMSizeUnitType result = JVMSizeUnitType.NONE;

		String paramSizeUnit = ""; //$NON-NLS-1$

		if (parameterName != null && !parameterName.isEmpty())
		{
			paramSizeUnit = parameterName.substring(parameterName.length() - 1, parameterName.length());
			// CHECKSTYLE:OFF
			result = paramSizeUnit.toUpperCase().equals("M") ? JVMSizeUnitType.MB : JVMSizeUnitType.GB; //$NON-NLS-1$
			// CHECKSTYLE:ON
		}
		return result;
	}

	/**
	 * Validate JVM parameters.
	 * 
	 * @return the string
	 */
	public static String validateJVMParameters()
	{
		String errorMessage = ""; //$NON-NLS-1$

		for (int i = 0; i < JVMConstants.JVMParameterList.length; i++)
		{
			errorMessage = errorMessage.concat(validateJVMParameter(getJVMParameterByName(JVMConstants.JVMParameterList[i])));
		}
		return errorMessage;
	}

	/**
	 * Validate JVM parameter.
	 * 
	 * @param parameterName
	 *        the parameter name
	 * @return the string
	 */
	private static String validateJVMParameter(JVMParameter jvmParameter)
	{
		String errorMessage = ""; //$NON-NLS-1$

		// 1. Check for empty value
		String paramError = ""; //$NON-NLS-1$
		if ((jvmParameter == null) || (jvmParameter.getParameterValue().isEmpty()))
		{
			errorMessage = errorMessage.concat(jvmParameter.getParameterName());
			paramError = errorMessage;
		}
		else
		{
			String jvmParamValue = jvmParameter.getParameterValue();

			if ((jvmParamValue != null) && (!jvmParamValue.isEmpty()))
			{
				// 2. Check for positive value
				int paramValue = Integer.parseInt(jvmParamValue);

				if (paramValue <= 0)
				{
					paramError = jvmParameter.getParameterName();
				}
			}
		}

		// Error message cleanup
		if (!paramError.isEmpty())
		{
			if (paramError.length() > 2)
			{
				if (paramError.indexOf("=") > -1) //$NON-NLS-1$
				{
					paramError = paramError.replace("=", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			errorMessage = NLS.bind(Messages.JVMParameters_invalid_parameter_value, paramError);
		}

		return errorMessage;
	}

	/**
	 * Check maximum JVM parameters value.
	 * 
	 * @return the string
	 */
	public static String checkMaximumJVMParametersValue()
	{
		String errorMessage = ""; //$NON-NLS-1$

		for (int i = 0; i < JVMConstants.JVMParameterList.length; i++)
		{
			// 3. Check for theoretical maximum value of JVM Xmx parameter
			if (JVMConstants.JVMParameterList[i].equals(JVMConstants.JVM_PARAM_XMX))
			{
				JVMParameter jvmParameter = getJVMParameterByName(JVMConstants.JVMParameterList[i]);
				int jvmParamValue = Integer.parseInt(jvmParameter.getParameterValue());
				JVMSizeUnitType jvmParamSizeUnit = jvmParameter.getSizeUnit();

				if (((jvmParamValue > 1500) && (jvmParamSizeUnit.equals(JVMSizeUnitType.MB)))
					|| (((jvmParamValue > 1) && (jvmParamSizeUnit.equals(JVMSizeUnitType.GB)))))
				{
					errorMessage = Messages.JVMParameters_validation_max_value;
				}
			}
		}
		return errorMessage;

	}

	/**
	 * Write JVM parameters to file.
	 * 
	 * @throws FileNotFoundException
	 */
	public static void writeJVMParametersToFile() throws FileNotFoundException
	{
		Map<String, String> jvmFileParameters = new HashMap<String, String>();

		for (int i = 0; i < JVMConstants.JVMParameterList.length; i++)
		{
			jvmFileParameters.put(JVMConstants.JVMParameterList[i],
				parametersMap.get(JVMConstants.JVMParameterList[i]).getFileParameterValue());
		}
		CessarIniFileUpdater.updateJVMParametersOnIniFile(jvmFileParameters);
	}

	/**
	 * Write JVM parameter to file.
	 * 
	 * @param jvmParameter
	 *        the JVM parameter
	 * @return the string
	 */
	public static String writeJVMParameterToFile(JVMParameter jvmParameter)
	{
		String errorMessage = validateJVMParameter(jvmParameter);

		if (errorMessage.isEmpty())
		{
			try
			{
				CessarIniFileUpdater.updateJVMParameterOnIniFile(jvmParameter);
			}
			catch (FileNotFoundException e)
			{
				CessarPluginActivator.getDefault().logError(e);
				return e.getMessage();
			}
		}
		return errorMessage;
	}

}
