/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Mar 18, 2014 8:21:39 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;

import eu.cessar.ct.runtime.ecuc.util.RunPlugetUtils;

/**
 * Utility class used to handle the parameters for the run pluget action. The parameters are held in a map and grouped
 * by the project that holds the run pluget.
 *
 * @author uidg4020
 *
 *         %created_by: uid95856 %
 *
 *         %date_created: Tue Jan 13 15:22:20 2015 %
 *
 *         %version: 4 %
 */
public final class HistoryParametersUtils
{
	/**
	 * Map containing the user inputs for pluget parameters for each project
	 */
	public static final WeakHashMap<IProject, List<String>> USER_INPUT_MAP = new WeakHashMap<IProject, List<String>>();
	private static final String HISTORY_FILENAME = ".plugetRunHistory"; //$NON-NLS-1$

	private HistoryParametersUtils()
	{
		// utility class does not allow instantiation
	}

	/**
	 * Returns parameters history for the project corresponding to current selection
	 *
	 * @param selection
	 *        current selection to obtain current project from
	 * @return parameters history
	 */
	public static String[] getParametersHistory(IStructuredSelection selection)
	{
		Object firstElement = selection.getFirstElement();
		IProject project = RunPlugetUtils.INSTANCE.getProject(firstElement);
		String[] parametersHistory = getParametersHistory(project);
		return parametersHistory;
	}

	/**
	 * Adds {@link parameters} to the history of the project that corresponds to this selection
	 *
	 * @param selection
	 *        current selection to obtain current project from
	 * @param parameters
	 *        parameters to be added
	 */
	public static void addHistoryParameters(IStructuredSelection selection, String parameters)
	{
		Object firstElement = selection.getFirstElement();
		IProject project = RunPlugetUtils.INSTANCE.getProject(firstElement);
		addHistoryParameters(project, parameters);
	}

	/**
	 * Returns parameters history for this project
	 *
	 * @param project
	 *        current project to obtain history for
	 * @return parameters history for this project
	 */
	public static String[] getParametersHistory(IProject project)
	{
		if (project != null)
		{
			String[] history = transformListToArray(USER_INPUT_MAP.get(project), project);

			if (history == null || history.length == 0)
			{
				history = readHistoryFromFile(project);

				if (history != null && history.length > 0)
				{
					for (String h: history)
					{
						addHistoryParameters(project, h);
					}
				}
			}

			return history;

		}

		return null;

	}

	/**
	 *
	 * @param project
	 */
	public static void clearParametersHistory(IProject project)
	{
		deleteHistoryFile(project);
		if (USER_INPUT_MAP.containsKey(project))
		{
			USER_INPUT_MAP.remove(project);
		}
	}

	/**
	 *
	 */
	private static void deleteHistoryFile(IProject project)
	{
		if (project != null)
		{
			File file = new File(project.getLocation().toOSString() + File.separator + HISTORY_FILENAME);
			if (file.exists())
			{
				file.delete();
			}
		}
	}

	/**
	 * Adds {@link parameters} to the history of the project
	 *
	 * @param project
	 *        project to add parameters history for
	 * @param parameters
	 */
	public static void addHistoryParameters(IProject project, String parameters)
	{
		if (project != null && parameters != null && parameters.length() > 0)
		{
			USER_INPUT_MAP.put(project, addToUserInputMap(project, parameters));
			writeHistoryToFile(project);
		}
	}

	private static List<String> addToUserInputMap(IProject project, String parameters)
	{
		List<String> lst = USER_INPUT_MAP.get(project);
		if (lst == null)
		{
			lst = new ArrayList<String>();
		}
		if (lst.contains(parameters))
		{

			lst.remove(parameters);
		}
		lst.add(0, parameters);
		return lst;
	}

	/**
	 * Read history from file
	 *
	 * @param project
	 * @return
	 */
	private static String[] readHistoryFromFile(IProject project)
	{
		File file = new File(project.getLocation().toOSString() + File.separator + HISTORY_FILENAME);
		if (file.exists())
		{
			try
			{
				ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
				Object[] objArray = (Object[]) inputStream.readObject();
				String[] history = Arrays.copyOf(objArray, objArray.length, String[].class);
				inputStream.close();

				return history;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 *
	 * @param historyArray
	 */
	private static void writeHistoryToFile(IProject project)
	{
		if (project != null)
		{
			String[] history = transformListToArray(USER_INPUT_MAP.get(project), project);

			if (history.length > 0)
			{
				try
				{
					File file = new File(project.getLocation().toOSString() + File.separator + HISTORY_FILENAME);
					if (!file.exists())
					{
						file.createNewFile();
					}

					if (file.exists())
					{
						ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
						outputStream.writeObject(history);
						outputStream.close();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return
	 */
	private static String[] transformListToArray(List<String> lst, IProject project)
	{
		List<String> list = lst;
		if (list != null)
		{
			list = USER_INPUT_MAP.get(project);
			String[] history = new String[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				history[i] = list.get(i);
			}
			return history;
		}
		return null;
	}
}