/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 20, 2010 12:31:31 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.prefandprop;

/**
 * Change listener used by <code>DialogField</code>
 * 
 * @author uidt2045
 * @Review uidl6458 - 19.04.2012
 */
public interface IDialogFieldListener
{

	/**
	 * The dialog field has changed.
	 * 
	 * @param field
	 *        the dialog field that changed
	 */
	void dialogFieldChanged(DialogField field);

}