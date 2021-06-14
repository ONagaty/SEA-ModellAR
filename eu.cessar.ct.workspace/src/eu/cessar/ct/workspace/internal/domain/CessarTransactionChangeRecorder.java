/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458<br/>
 * 06.09.2013 11:37:34
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.domain;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionChangeRecorder;

/**
 * TODO: Please comment this class
 *
 * @author uidl6458
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Fri Sep 6 16:58:30 2013 %
 *
 *         %version: 1 %
 */
public class CessarTransactionChangeRecorder extends TransactionChangeRecorder
{

	private IUpdatableModelChangeStampProvider stampProvider;

	/**
	 * @param domain
	 * @param rset
	 */
	public CessarTransactionChangeRecorder(InternalTransactionalEditingDomain domain, ResourceSet rset)
	{
		super(domain, rset);
		if (rset instanceof IUpdatableModelChangeStampProvider)
		{
			stampProvider = (IUpdatableModelChangeStampProvider) rset;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.emf.transaction.impl.TransactionChangeRecorder#notifyChanged(org.eclipse.emf.common.notify.Notification
	 * )
	 */
	@Override
	public void notifyChanged(Notification notification)
	{
		if (stampProvider != null)
		{
			stampProvider.modelChanged();
		}
		super.notifyChanged(notification);

	}
}
