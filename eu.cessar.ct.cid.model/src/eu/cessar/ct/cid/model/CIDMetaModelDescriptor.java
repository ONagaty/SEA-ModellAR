package eu.cessar.ct.cid.model;

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 11.12.2013 14:13:51
 * 
 * </copyright>
 */

import org.eclipse.sphinx.emf.metamodel.AbstractMetaModelDescriptor;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Jan 10 17:45:27 2014 %
 * 
 *         %version: 1 %
 * @generated NOT
 */
public class CIDMetaModelDescriptor extends AbstractMetaModelDescriptor
{
	/**
	 * The instance as requested by Sphinx
	 */
	public static final CIDMetaModelDescriptor INSTANCE = new CIDMetaModelDescriptor();

	/**
	 * The CID content type ID
	 */
	public static final String CID_CONTENT_TYPE_ID = "eu.cessar.ct.cid"; //$NON-NLS-1$

	private static final String ID = "eu.cessar.ct.cid"; //$NON-NLS-1$

	private static final String ROOT_NS = "http://www.cessar.eu/cid"; //$NON-NLS-1$

	private static final String MM_NAME = "Cessar Integrated Descriptor"; //$NON-NLS-1$

	/**
	 * 
	 */
	protected CIDMetaModelDescriptor()
	{
		super(ID, ROOT_NS, MM_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.sphinx.emf.metamodel.AbstractMetaModelDescriptor#getDefaultContentTypeId()
	 */
	@Override
	public String getDefaultContentTypeId()
	{
		return CID_CONTENT_TYPE_ID;
	}

}
