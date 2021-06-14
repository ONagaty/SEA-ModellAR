/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jan 26, 2012 9:45:24 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.events;

import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;

/**
 * 
 * Listener for events like: Focus out, ESC/ENTER key released (events described
 * by the {@link EEvent} enum) issued by the CESSAR-CT platform widgets (i.e
 * that implement the {@link IDatatypeEditor} interface)
 * 
 * @author uidl6870
 * 
 * @Review uidl6458 - 19.04.2012
 * 
 */
public interface IFocusEventListener
{

	/**
	 * Notifies the listener that the <code>event</code> has occurred
	 * 
	 * @param event
	 */
	public void notify(EFocusEvent event);

}
