
package eu.cessar.ct.core.platform.ui.events;

/**
 * A listener which is notified of menu drop down selection events on breadcrumb
 * viewers.
 */
public interface IMenuSelectionListener {

	/**
	 * Notifies of a menu drop down selection event.
	 */
	public void menuSelect(MenuSelectionEvent event);
}
