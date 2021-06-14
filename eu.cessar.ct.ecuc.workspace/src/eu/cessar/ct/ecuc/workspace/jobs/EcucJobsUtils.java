/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Apr 17, 2013 9:34:01 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.ecuc.workspace.jobs;

import eu.cessar.ct.ecuc.workspace.internal.Messages;

/**
 * Utilities to be used by Ecuc Jobs that start wizards for creating new module definition/configuration.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Wed Apr 17 16:19:30 2013 %
 * 
 *         %version: 1 %
 */
public final class EcucJobsUtils
{
	private EcucJobsUtils()
	{

	}

	/**
	 * Returns the default destination pack name for creating a new module configuration.
	 * 
	 * @return the default name
	 */
	public static String getDefaultDestinationPackageName()
	{
		return Messages.ModuleConfigurationInitializationPage_DefaultPackageName;
	}

	/**
	 * Returns the default module configuration name, based on its definition's name.
	 * 
	 * @param moduleDefinitionName
	 *        the shortname of the module definition
	 * @return the default name
	 */
	public static String getDefaultDestinationModuleName(String moduleDefinitionName)
	{
		String defaultModuleName = Messages.ModuleConfigurationInitializationPage_NamePrefix;

		if (moduleDefinitionName != null)
		{
			defaultModuleName += moduleDefinitionName;
		}

		return defaultModuleName;
	}

	/**
	 * Returns whether to create mandatory elements inside a module configuration.
	 * 
	 * @return the default value for the creation of mandatory elements.
	 */
	public static boolean getDefaultCreateMandatory()
	{
		return true;
	}

	/**
	 * Returns whether to create optional elements inside a module configuration.
	 * 
	 * @return the default value for the creation of optional elements.
	 */
	public static boolean getDefaultCreateOptional()
	{
		return false;
	}

	/**
	 * Returns whether to create optional elements inside a module configuration based on the upper multiplicity.
	 * 
	 * @return the default value for the creation of optional elements.
	 */
	public static boolean getDefaultCreateBasedOnUpperMultiplicity()
	{
		return false;
	}

	/**
	 * Returns the maximum optional elements to create inside a module configuration, based on the upper multiplicity.
	 * 
	 * @return the default value for the number of optional elements.
	 */
	public static int getDefaultMaximumElementsCount()
	{
		return 8;
	}

	/**
	 * Returns whether to replace the file containing an already existing module configuration.
	 * 
	 * @return the default value for the file replace operation.
	 */
	public static boolean getDefaultReplaceFile()
	{
		return false;
	}

	/**
	 * Returns whether to append to the file containing an already existing module configuration.
	 * 
	 * @return the default value for the file append operation.
	 */
	public static boolean getDefaultAppendToFile()
	{
		return false;
	}

}
