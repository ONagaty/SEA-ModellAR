package eu.cessar.ct.validation.ui.internal.wizards;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * Utility class used to handle the selected files for the filtered validation action. The parameters are held in a map
 * and grouped by the project that holds the files.
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Tue Oct 21 13:43:55 2014 %
 * 
 *         %version: 2 %
 */
public final class FilteredSelectionUtils
{
	/**
	 * Map containing the user inputs for pluget parameters for each project
	 */
	public static final WeakHashMap<IProject, List<Object>> SELECTIONS_MAP = new WeakHashMap<>();
	/**
	 * Map containing the user inputs for pluget parameters for each project
	 */
	public static final WeakHashMap<IProject, List<Object>> GRAYED_OBJECTS_MAP = new WeakHashMap<>();

	private static IResourceChangeListener iResourceChangeListener;

	private FilteredSelectionUtils()
	{
		// utility class does not allow instantiation
	}

	/**
	 * Returns parameters history for this project
	 * 
	 * @param project
	 *        current project to obtain history for
	 * @return parameters history for this project
	 */
	public static List<Object> getSelectionsHistory(IProject project)
	{
		if (project != null)
		{
			List<Object> history = SELECTIONS_MAP.get(project);

			return history;

		}

		return null;

	}

	/**
	 * Returns selected files history for this project
	 * 
	 * @param project
	 *        current project to obtain history for
	 * @param map
	 * @return selected files history for this project. If no selected file, returns null
	 */
	public static List<Object> getHistory(IProject project, WeakHashMap<IProject, List<Object>> map)
	{
		if (project != null)
		{
			List<Object> history = map.get(project);

			return history;

		}

		return null;

	}

	/**
	 * Returns grayed files history for this project
	 * 
	 * @param project
	 *        current project to obtain history for
	 * @return grayed files history for this project. If no grayed file, returns null.
	 */
	public static List<Object> getGrayedElements(IProject project)
	{
		if (project != null)
		{
			List<Object> history = GRAYED_OBJECTS_MAP.get(project);

			return history;

		}

		return null;

	}

	private static void addResourceListener()
	{

		if (iResourceChangeListener != null)
		{
			return;
		}
		// create resource listener
		iResourceChangeListener = new IResourceChangeListener()
		{

			@Override
			public void resourceChanged(IResourceChangeEvent event)
			{
				IResource resource = event.getResource();
				if (resource instanceof IProject)
				{
					// delete project from map
					synchronized (SELECTIONS_MAP)
					{
						SELECTIONS_MAP.remove(resource);
						GRAYED_OBJECTS_MAP.remove(resource);
					}

				}

			}
		};
		// add a listener for the close and delete events
		ResourcesPlugin.getWorkspace().addResourceChangeListener(iResourceChangeListener,
			IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE);
	}

	/**
	 * Adds {@link parameters} to the history of the project
	 * 
	 * @param project
	 *        project to add parameters history for
	 * @param selectedObjects
	 */
	public static void addSelectedObjects(IProject project, List<Object> selectedObjects)
	{
		addObjects(project, selectedObjects, SELECTIONS_MAP);
	}

	/**
	 * Adds {@link parameters} to the history of the project
	 * 
	 * @param project
	 *        project to add parameters history for
	 * @param selectedObjects
	 */
	public static void addGrayedObjects(IProject project, List<Object> selectedObjects)
	{
		addObjects(project, selectedObjects, GRAYED_OBJECTS_MAP);

	}

	/**
	 * Adds {@link parameters} to the history of the project
	 * 
	 * @param project
	 *        project to add parameters history for
	 * @param selectedObjects
	 * @param map
	 */
	public static void addObjects(IProject project, List<Object> selectedObjects,
		WeakHashMap<IProject, List<Object>> map)
	{

		map.put(project, setUserInputMap(project, selectedObjects, map));

		addResourceListener();
	}

	private static List<Object> setUserInputMap(IProject project, List<Object> selectedObjects,
		WeakHashMap<IProject, List<Object>> map)
	{
		List<Object> lst = map.get(project);
		if (lst == null)
		{
			lst = new ArrayList<>();
		}

		lst.clear();
		lst.addAll(0, selectedObjects);
		return lst;
	}
}
