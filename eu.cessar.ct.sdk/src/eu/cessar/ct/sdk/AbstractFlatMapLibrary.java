package eu.cessar.ct.sdk;

import java.util.List;

/**
 * <copyright>
 *
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 *
 * File created by uid95856<br/>
 * Sep 16, 2016 2:35:48 PM
 *
 * </copyright>
 */

/**
 *
 * Abstract class used by FlatMapGenerator; Do not create another constructor for extending classes
 *
 * @author uid95856
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public abstract class AbstractFlatMapLibrary
{

	/**
	 *
	 */
	public AbstractFlatMapLibrary()
	{

	}

	/**
	 * Returns true if the candidate should be considered as a target - a flat instance descriptor will be created for
	 * it
	 *
	 * @param candidate
	 * @return if the candidate is a target
	 */
	public abstract boolean isFlatmapElement(Object candidate);

	/**
	 * Fills the FID with the list of context elements
	 *
	 * A transaction shall be used if modifying the model. Example:
	 *
	 * <code>
	 * TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(fid);
	 *  try {
	 * 		WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable() {
	 *
	 * 			public void run() {
	 * 				//your code
	 *
	 * 			}
	 * 		}, "Name of transaction");
	 * 	} catch (Exception e) {
	 * 		LoggerFactory.getLogger().error(e);
	 * 	}
	 * 	</code>
	 *
	 * @param fid
	 * @param context
	 */
	public abstract void fillDescriptor(Object fid, List<Object> context);

	/**
	 * Add any additional data to the FID
	 *
	 * A transaction shall be used if modifying the model. Example:
	 *
	 * <code>
	 * TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(fid);
	 *  try {
	 * 		WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable() {
	 *
	 * 			public void run() {
	 * 				//your code
	 *
	 * 			}
	 * 		}, "Name of transaction");
	 * 	} catch (Exception e) {
	 * 		LoggerFactory.getLogger().error(e);
	 * 	}
	 * 	</code>
	 *
	 * @param fid
	 * @param context
	 */
	public abstract void modifyDescriptor(Object fid, List<Object> context);

}
