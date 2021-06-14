/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Dec 13, 2011 1:08:38 PM </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;

/**
 * Marker interface for notifications
 * 
 * @author uidu0944
 * 
 * @Review uidl6458 - 30.03.2012
 * 
 */
public interface INotificationCallback
{
	/**
	 * The model has been changed
	 * 
	 * @param notifications
	 */
	void modelChanged(List<Notification> notifications);
}
