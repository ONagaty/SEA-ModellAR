/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Feb 3, 2011 10:16:00 AM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.rules;

import java.util.List;

import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidt2045
 * 
 */
public class ValitationResult
{
	private List<AbstractProblem> listOfProblems;
	private Boolean goDeepFlag;
	private Boolean blockingFlag;
	private GIdentifiable ecucInstance;
	private GIdentifiable ecucDef;

	public GIdentifiable getEcucInstance()
	{
		return ecucInstance;
	}

	public GIdentifiable getEcucDef()
	{
		return ecucDef;
	}

	/**
	 * 
	 */
	public ValitationResult(GIdentifiable ecucInstance, Boolean goDeep, Boolean blocking,
		List<AbstractProblem> listOfProblems)
	{
		this.ecucInstance = ecucInstance;
		this.listOfProblems = listOfProblems;
		this.goDeepFlag = goDeep;
		this.blockingFlag = blocking;
	}

	/**
	 * returns true if applying the rule to the children of the tested
	 * parameters makes sense
	 * 
	 * @return
	 */
	public boolean canGoDeeper()
	{
		return goDeepFlag;
	}

	/**
	 * @return the list of problems resulted after applying the rule on the
	 *         given parameters
	 */
	public List<AbstractProblem> getListOfProblems()
	{
		return listOfProblems;
	}

	/**
	 * if the rule <b>did not pass</b> then <br>
	 * if this method returns <b>false</b> then it makes no sense to continue
	 * with the checking of the rules because the ecucInstance is incorrect and
	 * needs to be deleted <br>
	 * if then method returns <b>true</b> then you can continue with the rest of
	 * the rules
	 */
	public boolean shouldContinue()
	{
		return blockingFlag;
	}

}
