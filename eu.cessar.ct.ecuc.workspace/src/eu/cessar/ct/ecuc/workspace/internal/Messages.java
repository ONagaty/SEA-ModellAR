/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Sep 24, 2009 12:38:35 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6870
 * 
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{
	// CHECKSTYLE:OFF
	public static String AbstractCreateJob_CreateChildCommand_Transaction;

	public static String Error_InvalidMultiplicity;
	public static String Error_InvalidMultiplicityDetails;
	public static String Error_InvalidMultiplicityMissing;

	public static String job_creatingModuleConfiguration;

	public static String CessarModuleConfigurationJob_NoProject;
	public static String CessarModuleConfigurationJob_NoFile;
	public static String CessarModuleConfigurationJob_InvalidProject;
	public static String CessarModuleConfigurationJob_InvalidRelease;
	public static String CessarModuleConfigurationJob_InvalidEcuc;
	public static String CessarModuleConfigurationJob_InvalidPackage;
	public static String CessarModuleConfigurationJob_NoDestinationPackage;
	public static String CessarModuleConfigurationJob_NoModuleDefinition;
	public static String CessarModuleConfigurationJob_NoRemainingMulitplicity;
	public static String CessarModuleConfigurationJob_NoModuleConfigurationName;
	public static String CessarModuleConfigurationJob_InvalidContainerType;

	public static String CessarModuleConfigurationJob_SetupARPackage_Transaction;
	public static String CessarModuleConfigurationJob_SetupModuleConfiguration_Transaction;
	public static String CessarModuleConfigurationJob_SetupModuleContainer_Transaction;

	public static String CessarModuleConfigurationJob_GetModelRoot_Task;
	public static String CessarModuleConfigurationJob_GetDestinationPackage_Task;
	public static String CessarModuleConfigurationJob_CreateModuleConf_Task;
	public static String CessarModuleConfigurationJob_AddMandatoryElements_Task;
	public static String CessarModuleConfigurationJob_SaveModuleConf_Task;

	public static String job_creatingModuleDefinition;

	public static String CessarModuleDefinitionJob_NoProject;
	public static String CessarModuleDefinitionJob_NoFile;
	public static String CessarModuleDefinitionJob_InvalidProject;
	public static String CessarModuleDefinitionJob_InvalidRelease;
	public static String CessarModuleDefinitionJob_InvalidEcuc;
	public static String CessarModuleDefinitionJob_InvalidPackage;
	public static String CessarModuleDefinitionJob_NoDestinationPackage;
	public static String CessarModuleDefinitionJob_NotAutosarPackage;
	public static String CessarModuleDefinitionJob_NoModuleDefinition;
	public static String CessarModuleDefinitionJob_NoModuleDefinitionName;
	public static String CessarModuleDefinitionJob_StandardModulePresent;

	public static String CessarModuleDefinitionJob_SetupARPackage_Transaction;
	public static String CessarModuleDefinitionJob_SetupModuleDefinition_Transaction;

	public static String CessarModuleDefinitionJob_GetModelRoot_Task;
	public static String CessarModuleDefinitionJob_GetDestinationPackage_Task;
	public static String CessarModuleDefinitionJob_CreateModuleDef_Task;
	public static String CessarModuleDefinitionJob_SaveModuleDef_Task;

	public static String CleanUpJob_CleaningOperation;

	public static String CleanUpJob_CleanUp;

	public static String CleanUpJob_DeleteElement;

	public static String CleanUpJob_SkipElement;

	public static String ModuleConfigurationCleaner_Container;

	public static String ModuleConfigurationCleaner_Parameter;

	public static String ModuleConfigurationCleaner_Reference;

	public static String SyncAction_Create;

	public static String SyncAction_Delete;

	public static String ModuleConfigurationInitializationPage_DefaultPackageName;

	public static String ModuleConfigurationInitializationPage_NamePrefix;

	public static String duplicate_moduleDef_inconsistency;

	// CHECKSTYLE:ON

	private final static String BUNDLE_NAME = Messages.class.getCanonicalName();

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
