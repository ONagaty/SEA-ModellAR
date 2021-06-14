/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jul 19, 2012 1:43:23 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.problems;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger;

/**
 * @author uidu0944
 * 
 */
public class EnumValueProblem extends AbstractProblem
{
	private static String ENUM_CHECK = "Value {0} not found in the enumeration {1} "; //$NON-NLS-1$

	private String value;
	private String enumList;
	private EObject objConf;

	public EnumValueProblem(EObject objConf, String value, String enumList)
	{
		this.objConf = objConf;
		this.value = value;
		this.enumList = enumList;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return MessageFormat.format(ENUM_CHECK, value, enumList);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getProblemType()
	 */
	@Override
	public String getProblemType()
	{
		return SynchronizeConstants.invalidEnumType;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getElement()
	 */
	@Override
	public EObject getElement()
	{
		return objConf;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getAffectedElement()
	 */
	@Override
	public EObject getAffectedElement()
	{
		return objConf;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getChangers()
	 */
	@Override
	public List<AbstractEcucItemChanger> getChangers()
	{
		return new ArrayList<AbstractEcucItemChanger>();
	}

}
