/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Feb 25, 2011 3:32:58 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.problems;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger;
import eu.cessar.ct.ecuc.workspace.sync.changers.DeleteEcucItem;
import gautosar.gecucdescription.GParameterValue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public class DeleteParamProblem extends AbstractProblem
{

	private GParameterValue value;

	public DeleteParamProblem(GParameterValue elemConf)
	{
		this.value = elemConf;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return "Delete parameter description";
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getChangers()
	 */
	@Override
	public List<AbstractEcucItemChanger> getChangers()
	{
		List<AbstractEcucItemChanger> listOfChangers = new ArrayList<AbstractEcucItemChanger>();
		listOfChangers.add(new DeleteEcucItem(value));
		return listOfChangers;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getProblemType()
	 */
	@Override
	public String getProblemType()
	{
		return SynchronizeConstants.problemDelParamType;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getElement()
	 */
	@Override
	public EObject getElement()
	{
		// TODO Auto-generated method stub
		return value;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getAffectedElement()
	 */
	@Override
	public EObject getAffectedElement()
	{
		// TODO Auto-generated method stub
		return value;
	}

}
