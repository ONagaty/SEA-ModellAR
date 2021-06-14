/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 06.08.2012 10:27:51 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import eu.cessar.ct.workspace.internal.consistencycheck.impl.AbstractInconsistency;
import eu.cessar.ct.workspace.xmlchecker.EXMLInconsistencyType;
import eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * Implementation of an {@link IXMLCheckerInconsistency}
 * 
 * @author uidl6870
 * 
 */
public class XMLCheckInconsistency extends AbstractInconsistency implements IXMLCheckerInconsistency
{
	private EXMLInconsistencyType inconsistencyType;
	private IInconsistencyDetail detailFromOriginal;
	private IInconsistencyDetail detailFromCopy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency#getInconsistencyType()
	 */
	public EXMLInconsistencyType getInconsistencyType()
	{
		return inconsistencyType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency#setInconsistencyType(eu.cessar.ct.workspace.xmlchecker
	 * .EXMLInconsistencyType)
	 */
	public void setInconsistencyType(EXMLInconsistencyType inconsistencyType)
	{
		this.inconsistencyType = inconsistencyType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistency#getDetailFromOriginal()
	 */
	public IInconsistencyDetail getDetailFromOriginalFile()
	{
		return detailFromOriginal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistency#getDetailFromCopy()
	 */
	public IInconsistencyDetail getDetailFromGeneratedFile()
	{
		return detailFromCopy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.xmlchecker.IInconsistency#setDetailFromOriginal(eu.cessar.ct.workspace.xmlchecker.IDetail)
	 */
	public void setDetailFromOriginalFile(IInconsistencyDetail detail)
	{
		detailFromOriginal = detail;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.xmlchecker.IInconsistency#setDetailFromCopy(eu.cessar.ct.workspace.xmlchecker.IDetail)
	 */
	public void setDetailFromGeneratedFile(IInconsistencyDetail detail)
	{
		detailFromCopy = detail;
	}

}
