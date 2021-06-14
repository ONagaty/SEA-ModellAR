package eu.cessar.ct.runtime.ui.internal.classpath;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

/**
 * This class offers the editing support for the first column of the TableViewer used in
 * CessarLibrariesClasspathContainerPage,meaning that it basically allows the check/uncheck property
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Fri Jan 17 10:01:30 2014 %
 * 
 *         %version: 2 %
 */
public class CheckedEditingSupport extends EditingSupport
{

	private final TableViewer viewer;

	/**
	 * @param viewer
	 */
	public CheckedEditingSupport(TableViewer viewer)
	{
		super(viewer);
		this.viewer = viewer;
	}

	/**
	 * For the given element returns a new CheckboxCellEditor
	 * 
	 * @param element
	 * @return new CheckboxCellEditor
	 */
	@Override
	protected CellEditor getCellEditor(Object element)
	{
		return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);

	}

	/**
	 * Sets the element as editable
	 * 
	 * @param element
	 * @return true
	 */
	@Override
	protected boolean canEdit(Object element)
	{
		return true;
	}

	/**
	 * Cast's the given element to a PluginModelBaseWrapper, and returns whether it is flagged as checked or not
	 * 
	 * @param element
	 * @return boolean
	 */
	@Override
	protected Object getValue(Object element)
	{
		PluginModelBaseWrapper plugin = (PluginModelBaseWrapper) element;
		return plugin.isChecked();

	}

	/**
	 * Sets the changed value of the given element to true and the checked value to the given value
	 * 
	 * @param element
	 * @param value
	 */
	@Override
	protected void setValue(Object element, Object value)
	{
		PluginModelBaseWrapper plug = (PluginModelBaseWrapper) element;
		if (!plug.isReadOnly())
		{
			plug.setChanged(true);
			plug.setChecked((Boolean) value);
			viewer.update(element, null);
		}
	}

}
