/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Feb 25, 2011 3:38:48 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.problems;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger;
import eu.cessar.ct.ecuc.workspace.sync.changers.DeleteEcucItem;
import gautosar.gecucdescription.GConfigReferenceValue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public class DeleteRefProblem extends AbstractProblem
{

	private GConfigReferenceValue value;

	/**
	 * @param value
	 */
	public DeleteRefProblem(GConfigReferenceValue value)
	{
		// TODO Auto-generated constructor stub
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return "delete reference description";
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
		return SynchronizeConstants.problemDelRefType;
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
