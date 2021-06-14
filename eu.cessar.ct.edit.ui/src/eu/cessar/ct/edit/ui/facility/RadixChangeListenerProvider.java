/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Jun 7, 2016 9:46:19 AM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.ArrayList;
import java.util.List;

/**
 * This is utility class, designed to hold the IRadixChangeListener
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class RadixChangeListenerProvider
{

	private List<IRadixChangeListener> radixListeners = new ArrayList<>();

	private static RadixChangeListenerProvider instnace;

	private RadixChangeListenerProvider()
	{
		// private constructor to restrict instantiation from outside
	}

	/**
	 * Singleton method to initialize instance
	 *
	 * @return RadixChangeListenerProvider
	 */
	public static RadixChangeListenerProvider getInstance()
	{
		if (instnace == null)
		{
			instnace = new RadixChangeListenerProvider();
		}
		return instnace;
	}

	/**
	 * To add new listener to the list
	 *
	 * @param radixListener
	 */
	public void addRadixChangeListener(IRadixChangeListener radixListener)
	{
		if (!radixListeners.contains(radixListener))
		{
			radixListeners.add(radixListener);
		}
	}

	/**
	 * To remove listener from the list
	 *
	 * @param radixListener
	 */
	public void removeRadixChangeListener(IRadixChangeListener radixListener)
	{
		radixListeners.remove(radixListener);
	}

	/**
	 * @return List<IRadixChangeListener>
	 */
	public List<IRadixChangeListener> getRadixListeners()
	{
		return radixListeners;
	}

	/**
	 * To reset the listener provider on widgets dispose
	 */
	public void reset()
	{
		radixListeners.clear();
		instnace = null;
	}
}
