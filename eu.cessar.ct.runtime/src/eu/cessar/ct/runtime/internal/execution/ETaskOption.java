/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95856<br/>
 * Jan 12, 2016 1:17:50 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import eu.cessar.ct.runtime.execution.CessarTask;

/**
 * Enum for task option ( {@link CessarTask}. )
 *
 * @author uid95856
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public enum ETaskOption
{
	/**
	 *
	 */
	MERGED_REFERENCES("MergedReferences"), //$NON-NLS-1$
	/**
	 *
	 */
	SPLIT_CHECKING("SplitChecking"); //$NON-NLS-1$

	private String option;

	/**
	 *
	 */
	private ETaskOption(String option)
	{
		this.option = option;
	}

	/**
	 * @return the option
	 */
	public String getOption()
	{
		return option;
	}

}
