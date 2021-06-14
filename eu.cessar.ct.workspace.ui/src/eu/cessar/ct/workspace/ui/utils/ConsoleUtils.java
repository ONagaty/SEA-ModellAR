/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg3464<br/>
 * Nov 20, 2013 8:57:30 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;

/**
 * TODO: Please comment this class
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Wed Nov 20 17:22:45 2013 %
 * 
 *         %version: 1 %
 */
public final class ConsoleUtils
{
	/**
	 * Settings used by LoggerConsole
	 */

	/**
	 * Setting for the color of the DebugStream.
	 */
	public static final String ID_DEBUG_COLOR_EDIT = "eu.cessar.ct.workspace.ui.consoleEdit.debugcolor"; //$NON-NLS-1$

	/**
	 * Setting for the color of the InfoStream.
	 */
	public static final String ID_INFO_COLOR_EDIT = "eu.cessar.ct.workspace.ui.consoleEdit.infocolor"; //$NON-NLS-1$

	/**
	 * Setting for the color of the WarnStream.
	 */
	public static final String ID_WARN_COLOR_EDIT = "eu.cessar.ct.workspace.ui.consoleEdit.warningcolor"; //$NON-NLS-1$

	/**
	 * Setting for the color of the ErrorStream.
	 */
	public static final String ID_ERROR_COLOR_EDIT = "eu.cessar.ct.workspace.ui.consoleEdit.errorcolor"; //$NON-NLS-1$

	private ConsoleUtils()
	{
		// do nothing
	}

	/**
	 * @return The current theme
	 */
	public static ITheme getCurrentTheme()
	{

		return PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
	}

	/**
	 * Return the color setting associated with the provided ID
	 * 
	 * @param id
	 * @return The current color based of the given id
	 */
	public static Color getColorSettings(String id)
	{
		return getCurrentTheme().getColorRegistry().get(id);

	}

}
