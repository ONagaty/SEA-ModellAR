
package eu.cessar.ct.core.platform.ui.events;

import java.util.EventObject;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * Event for menu drop down selection triggered from the breadcrumb viewer.
 * 
 * @see IMenuSelectionListener
 */
public class MenuSelectionEvent extends EventObject {


	/**
	 * The selection.
	 */
	protected ISelection selection;

	/**
	 * Creates a new event for the given source and selection.
	 * 
	 */
	public MenuSelectionEvent(final Viewer source, final ISelection selection) {
		super(source);
		Assert.isNotNull(selection);
		this.selection = selection;
	}

	/**
	 * Returns the selection.
	 */
	public ISelection getSelection() {
		return selection;
	}

	/**
	 * Returns the viewer that is the source of this event.
	 */
	public Viewer getViewer() {
		return (Viewer) getSource();
	}
}
