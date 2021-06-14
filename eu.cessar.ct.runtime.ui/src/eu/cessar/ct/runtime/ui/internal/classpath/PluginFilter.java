package eu.cessar.ct.runtime.ui.internal.classpath;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * 
 * @author uidg3464 Is used for filtering the TabelViewer after the searchedText inside the column that contains the
 *         plugin's name %created_by: uidg3464 %
 * 
 *         %date_created: Fri Jan 17 10:01:30 2014 %
 * 
 *         %version: 2 %
 */
public class PluginFilter extends ViewerFilter
{

	private String searchString;

	/**
	 * @param s
	 */
	public void setSearchText(String s)
	{
		// Search must be a substring of the existing value
		searchString = ".*" + s + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element)
	{
		if (searchString == null || searchString.length() == 0)
		{
			return true;
		}
		PluginModelBaseWrapper p = (PluginModelBaseWrapper) element;
		String id = p.getPluginModelBase().getPluginBase().getId();
		Pattern pattern = Pattern.compile(searchString);
		Matcher matcher = pattern.matcher(id);
		if (matcher.find())
		{
			return true;
		}

		return false;
	}
}
