/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Feb 7, 2011 10:07:57 AM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidt2045
 * 
 */

public class SyncMessages extends NLS
{

	private static String BUNDLE_NAME = SyncMessages.class.getCanonicalName();
	public static String WarningPage_DESC;
	public static String WarningPage_PROBLEMS;
	public static String WizardTitle;

	public static String Selection_Execute;
	public static String Selection_Name;
	public static String Selection_Location;
	public static String SynchronizerWizard_WARNING_PAGE;

	public static String DecisionPageDescr;
	public static String ActionsExecutor_Execute;
	public static String ActionsCollector_Action;
	public static String ActionsCollector_Elements;
	public static String ActionsCollector_Type;
	public static String ActionsCollector_Problem;
	public static String ActionsDecisionPage_CheckUncheck;
	public static String Type_Container;
	public static String Type_ModuleConfig;
	public static String Type_Reference;
	public static String Type_Unknown;
	public static String Type_Parameter;
	public static String ContentPage;
	public static String DecisionPage;
	public static String ReportPage;
	public static String CleanerWizard_InfoMessage2;
	public static String Progress_LoadingProject;
	public static String ContentPageDescr;
	public static String Type_ModuleDef;
	public static String Type_ChoiceContainer;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SyncMessages.class);
	}
}
