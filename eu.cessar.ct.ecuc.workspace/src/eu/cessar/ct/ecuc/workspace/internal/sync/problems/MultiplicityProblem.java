/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jan 31, 2011 5:13:50 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.problems;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger;
import eu.cessar.ct.ecuc.workspace.sync.changers.CreateChoiceContainerEcucItem;
import eu.cessar.ct.ecuc.workspace.sync.changers.CreateContainerEcucItem;
import eu.cessar.ct.ecuc.workspace.sync.changers.CreateParameterEcucItem;
import eu.cessar.ct.ecuc.workspace.sync.changers.CreateReferenceEcucItem;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public class MultiplicityProblem extends AbstractProblem
{

	private GIdentifiable elementDef;
	private GIdentifiable elementConf;
	private Map<String, Object> options;

	/**
	 * @param elemDef
	 * @param elemConf
	 * @param options
	 */
	public MultiplicityProblem(GIdentifiable elemDef, GIdentifiable elemConf, Map<String, Object> options)
	{
		elementConf = elemConf;
		elementDef = elemDef;
		this.options = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.problems.AbstractProblem#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return SynchronizeConstants.multiplicityDescr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.problems.AbtractProblem#getChangers()
	 */
	@Override
	public List<AbstractEcucItemChanger> getChangers()
	{
		List<AbstractEcucItemChanger> listOfChangers = new ArrayList<AbstractEcucItemChanger>();
		AbstractEcucItemChanger createChanger;

		if ((Boolean) options.get(SynchronizeConstants.multCreate))
		{
			// this means that we must create EcucItems
			createChanger = getTypeOfChanger();
			int nrOfNeededInstances;

			nrOfNeededInstances = getNrOfNeededInstances();

			for (int i = 0; i < nrOfNeededInstances; i++)
			{
				listOfChangers.add(createChanger.copy());
			}
		}
		else
		{
			// this means that upperMult is the problem
			// and in this case I do not know which item to delete

			// listOfChangers.add(new DeleteModuleChanger(elementConf));
		}
		return listOfChangers;
	}

	/**
	 * @return
	 */
	private int getNrOfNeededInstances()
	{
		int nrOfNeededInstances;
		// get from the options hashMap the information on how many should
		// be created
		Integer nrOfInstances = (Integer) options.get(SynchronizeConstants.itemNrOfInstances);

		Integer allowedUpperMult;
		Integer allowedLowerMult;
		if (elementDef.eContainer() instanceof GChoiceContainerDef)
		{
			String lowerMultiplicityAsString = ((GChoiceContainerDef) elementDef.eContainer()).gGetLowerMultiplicityAsString();
			String upperMultiplicityAsString = ((GChoiceContainerDef) elementDef.eContainer()).gGetUpperMultiplicityAsString();
			allowedLowerMult = Integer.valueOf(lowerMultiplicityAsString);
			if (upperMultiplicityAsString.equals(SynchronizeConstants.multStar))
			{
				allowedUpperMult = Integer.MAX_VALUE;
			}
			else
			{
				allowedUpperMult = Integer.valueOf(upperMultiplicityAsString);
			}
			nrOfInstances = elementConf.eContents().size();
		}
		else
		{
			allowedLowerMult = (Integer) options.get(SynchronizeConstants.itemLowerMult);
			allowedUpperMult = (Integer) options.get(SynchronizeConstants.itemUpperMult);
		}
		if ((Boolean) options.get(SynchronizeConstants.multOptionalFlag))
		{
			Integer optNrOfInst = (Integer) options.get(SynchronizeConstants.multOptionalValue);
			if (optNrOfInst < allowedUpperMult)
			{
				nrOfNeededInstances = optNrOfInst - nrOfInstances;
			}
			else
			{
				nrOfNeededInstances = allowedUpperMult - nrOfInstances;
			}
		}
		else
		{
			nrOfNeededInstances = allowedLowerMult - nrOfInstances;
		}
		return nrOfNeededInstances;
	}

	/**
	 * @return
	 */
	private AbstractEcucItemChanger getTypeOfChanger()
	{
		AbstractEcucItemChanger createChanger;
		if (elementDef instanceof GChoiceContainerDef)
		{
			createChanger = new CreateChoiceContainerEcucItem(elementConf, (GChoiceContainerDef) elementDef,
				elementDef.gGetShortName());
		}
		else
		{

			if (elementDef instanceof GContainerDef)
			{
				createChanger = new CreateContainerEcucItem(elementConf, (GContainerDef) elementDef);
			}
			else
			{
				if (elementDef instanceof GConfigParameter)
				{
					createChanger = new CreateParameterEcucItem(elementConf, (GConfigParameter) elementDef);
				}
				else
				{
					if (elementDef instanceof GConfigReference)
					{
						createChanger = new CreateReferenceEcucItem(elementConf, (GConfigReference) elementDef);
					}
					else
					{
						createChanger = null;
					}
				}
			}
		}
		return createChanger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.problems.AbstractProblem#getActionType()
	 */
	@Override
	public String getProblemType()
	{
		if ((Boolean) options.get(SynchronizeConstants.multCreate))
		{
			return SynchronizeConstants.problemCreateType;
		}
		return SynchronizeConstants.upperMultiplicityType;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.problems.AbstractProblem#getElement()
	 */
	@Override
	public EObject getElement()
	{
		return elementDef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getAffectedElement()
	 */
	@Override
	public EObject getAffectedElement()
	{
		// TODO Auto-generated method stub
		return elementConf;
	}

}
