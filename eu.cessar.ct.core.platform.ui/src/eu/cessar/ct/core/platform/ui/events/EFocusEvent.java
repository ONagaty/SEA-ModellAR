/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jan 26, 2012 10:15:19 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.events;

/**
 * Enumeration for different type of events which are of interest for the client
 * of the CESSAR-CT platform widgets
 * 
 * @author uidl6870
 * 
 * @Review uidl6458 - 19.04.2012
 */
public enum EFocusEvent
{
	/** Focus lost */
	FOCUS_OUT,
	/** ESC key released */
	ESC,
	/** ENTER key released */
	CR;
}
