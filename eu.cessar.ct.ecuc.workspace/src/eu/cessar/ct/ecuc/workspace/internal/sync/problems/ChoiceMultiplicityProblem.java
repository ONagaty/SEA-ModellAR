/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Feb 11, 2011 9:41:37 AM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.problems;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author uidt2045
 * 
 */
public class ChoiceMultiplicityProblem extends MultiplicityProblem
{

	/**
	 * @param elemDef
	 * @param elemConf
	 * @param options
	 */
	public ChoiceMultiplicityProblem(GIdentifiable elemDef, GIdentifiable elemConf, Map<String, Object> options)
	{
		super(elemDef, elemConf, options);
	}

	@Override
	public String getDescription()
	{
		return SynchronizeConstants.choiceMultdescr;
	}

	@Override
	public List<AbstractEcucItemChanger> getChangers()
	{
		// TODO Auto-generated method stub
		return new ArrayList<AbstractEcucItemChanger>();
	}

	@Override
	public String getProblemType()
	{
		// TODO Auto-generated method stub
		return SynchronizeConstants.choiceMultiplicityType;
	}

}
