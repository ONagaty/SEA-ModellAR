/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jun 21, 2012 9:23:19 AM </copyright>
 */
package eu.cessar.ct.core.mms;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;

/**
 * @author uidt2045
 * 
 */
@SuppressWarnings("javadoc")
public class FileExtensionPreferenceAccessor extends AbstractPreferencesAccessor
{
	public static final String NAMESPACE = "eu.cessar.ct.core.mms.extensions";//$NON-NLS-1$
	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	// extensions
	public static final String KEY_CONFIG_BSWMD_EXT = "bswmd.extension"; //$NON-NLS-1$
	public static final String KEY_CONFIG_ECUC_EXT = "ecuc.extension"; //$NON-NLS-1$
	public static final String DEFAULT_BSWMD_EXTENSION = "autosar"; //$NON-NLS-1$ 
	public static final String DEFAULT_ECUC_EXTENSION = "ecuconfig"; //$NON-NLS-1$ 

	// -----------------autosar files extensions

	/**
	 * retrieve all extensions defined for the project version
	 * 
	 * @param project
	 * @return String[]
	 */
	public static String[] getExtensions(IProject project)
	{
		if (project != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(project);
			return mmService.getFileExtensions();
		}
		else
		{
			IContentType contentType = Platform.getContentTypeManager().getContentType(
				AutosarReleaseDescriptor.ARXML_BASE_CONTENT_TYPE_ID);
			return contentType.getFileSpecs(IContentType.FILE_EXTENSION_SPEC);
		}

	}

	/**
	 * returns the current selection index
	 * 
	 * @param project
	 * @param autosarResource
	 * @return integer
	 */
	public static int getCurrentExtensionIndex(IProject project, String autosarResource)
	{
		int currIndex = 0;

		String[] autosarExtensions = getExtensions(project);

		String defaultExt = null;
		if (autosarResource.equals(KEY_CONFIG_BSWMD_EXT))
		{
			defaultExt = getStringPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, autosarResource,
				DEFAULT_BSWMD_EXTENSION);
		}
		if (autosarResource.equals(KEY_CONFIG_ECUC_EXT))
		{
			defaultExt = getStringPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, autosarResource,
				DEFAULT_ECUC_EXTENSION);
		}
		for (int i = 0; i < autosarExtensions.length; i++)
		{
			if (autosarExtensions[i].equals(defaultExt))
			{
				currIndex = i;
				break;
			}
		}
		return currIndex;
	}

	/**
	 * returns the default index
	 * 
	 * @param autosarResource
	 * @return integer
	 */
	public static int getDefaultExtensionIndex(String autosarResource)
	{
		int currIndex = 0;

		String[] autosarExtensions = getExtensions(null);

		String defaultExt = null;
		if (autosarResource.equals(KEY_CONFIG_BSWMD_EXT))
		{
			defaultExt = DEFAULT_BSWMD_EXTENSION;
		}
		if (autosarResource.equals(KEY_CONFIG_ECUC_EXT))
		{
			defaultExt = DEFAULT_ECUC_EXTENSION;
		}
		for (int i = 0; i < autosarExtensions.length; i++)
		{
			if (autosarExtensions[i].equals(defaultExt))
			{
				currIndex = i;
				break;
			}
		}
		return currIndex;
	}

	/**
	 * sets the configRadix BSW module definition extension in project
	 * 
	 * @param project
	 * @param value
	 */
	public static void setBswMdExtensionInProject(IProject project, String value)
	{
		setStringPref(project, NAMESPACE, KEY_CONFIG_BSWMD_EXT, value);
	}

	/**
	 * sets the configRadix BSW module definition extension in workspace
	 * 
	 * @param value
	 */
	public static void setBswMdExtensionInWS(String value)
	{
		setWorkspaceStringPref(NAMESPACE, KEY_CONFIG_BSWMD_EXT, value);
	}

	/**
	 * sets the ECU configuration extension in project
	 * 
	 * @param project
	 * @param value
	 */
	public static void setEcucExtensionInProject(IProject project, String value)
	{
		setStringPref(project, NAMESPACE, KEY_CONFIG_ECUC_EXT, value);
	}

	/**
	 * sets the ECU configuration extension in workspace
	 * 
	 * @param value
	 */
	public static void setEcucExtensionInWS(String value)
	{
		setWorkspaceStringPref(NAMESPACE, KEY_CONFIG_ECUC_EXT, value);
	}

	/**
	 * @param project
	 * @return the bswmdExtension
	 */
	public static String getBswmdExtension(IProject project)
	{
		return getStringPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_CONFIG_BSWMD_EXT,
			DEFAULT_BSWMD_EXTENSION);
	}

	/**
	 * @param project
	 * @return the ecucExtension
	 */
	public static String getEcucExtension(IProject project)
	{
		return getStringPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_CONFIG_ECUC_EXT,
			DEFAULT_ECUC_EXTENSION);
	}

	/**
	 * returns if flag is set for project preference (true) or workspace preference (false)
	 * 
	 * @param project
	 * @return boolean
	 */
	public static boolean isProjectSpecificSettings(IProject project)
	{
		return getBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, false);
	}

	/**
	 * Sets the project specific Flag. If the flag is true, the preferences will be returned from the Project preference
	 * store, if the flag is false the preferences will be returned from the Workspace preference store
	 * 
	 * @param project
	 * @param value
	 */
	public static void setProjectSpecificSettings(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, value);
	}
}
