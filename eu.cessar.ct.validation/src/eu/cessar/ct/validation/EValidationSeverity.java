/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4449<br/>
 * Feb 4, 2014 6:46:22 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum class for severity
 * 
 * @author uidg4449
 * 
 *         %created_by: uidg4449 %
 * 
 *         %date_created: Thu Feb  6 10:31:03 2014 %
 * 
 *         %version: 1 %
 */
public enum EValidationSeverity
{
	/**
	 * Severity Level
	 */
	INFO(1),

	/**
	 * Severity Level
	 */
	WARNING(2),

	/**
	 * Severity Level
	 */
	ERROR(4),

	/**
	 * Severity Level
	 */
	CANCEL(8);

	private static final Map<Integer, EValidationSeverity> INT_TO_SEVERITY = new HashMap<Integer, EValidationSeverity>();
	private final int value;

	/**
	 * 
	 */
	private EValidationSeverity(int id)
	{
		value = id;
	}

	static
	{
		for (EValidationSeverity type: EValidationSeverity.values())
		{
			INT_TO_SEVERITY.put(type.value, type);
		}
	}

	/**
	 * @param id
	 *        the value in Integer of severity
	 * @return return the EValidationSeverity from the vale of int If the value of int is not defined it return the
	 *         ERROR type
	 */
	public static EValidationSeverity getSeverity(int id)
	{
		EValidationSeverity eValidationSeverity = INT_TO_SEVERITY.get(Integer.valueOf(id));
		if (eValidationSeverity != null)
		{
			return eValidationSeverity;
		}
		return EValidationSeverity.ERROR;
	}

}
