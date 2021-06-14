/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Jun 6, 2016 4:48:53 PM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

/**
 * Listener interface which will be notify by RadixChangeEvent
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public interface IRadixChangeListener
{
	/**
	 * Event notifier method
	 * 
	 * @param event
	 */
	public void radixChanged(RadixChangeEvent event);
}
