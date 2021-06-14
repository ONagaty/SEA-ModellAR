package eu.cessar.ct.ecuc.workspace.internal.sync.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.ChoiceMultiplicityProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.MultiplicityProblem;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * This rule verifies that the lowerMultiplicity is satisfied. <br>
 * Given an element from the Definition (defElement), checks if the given module Configuration contains the necessary
 * number of instances of that defElements (checks if the number of instances is bigger than/equal to the
 * lowerMultiplicity) <br>
 * It also checks that the UpperMultiplicity is satisfied.<br>
 * Given an element from the Definition (defElement)<br>
 * checks if the number of instances is smaller than/equal to the upperMultiplicity
 * 
 * @author uidt2045
 * 
 */
public class MultiplicityRule extends AbstractRule
{

	/**
	 * 
	 */
	private static final String ZERO = "0"; //$NON-NLS-1$
	private List<AbstractProblem> listOfProblems;

	@Override
	public String getRuleDescription()
	{
		return SynchronizeConstants.multiplicityRuleDescr;
	}

	@Override
	public boolean passesRule(GIdentifiable itemConf, Map<String, Object> options)
	{
		boolean result = true;
		boolean pass;
		listOfProblems = new ArrayList<AbstractProblem>();

		GParamConfMultiplicity itemDef;
		if (itemConf instanceof GModuleConfiguration)
		{
			itemDef = ((GModuleConfiguration) itemConf).gGetDefinition();
		}
		else
		{
			if (itemConf instanceof GContainer)
			{
				itemDef = ((GContainer) itemConf).gGetDefinition();
			}
			else
			{
				return false;
			}
		}
		if (itemDef instanceof GChoiceContainerDef)
		{
			return true;
		}
		for (EObject objD: itemDef.eContents())
		{
			if (objD instanceof GIdentifiable)
			{
				if (objD instanceof GChoiceContainerDef)
				{
					Map<String, Object> itemSpecificOptions = new HashMap<String, Object>();
					itemSpecificOptions.putAll(options);
					pass = initialPassForChoice((GChoiceContainerDef) objD, itemConf, itemSpecificOptions);
					if (!pass)
					{

						listOfProblems.add(new ChoiceMultiplicityProblem((GIdentifiable) objD, itemConf,
							itemSpecificOptions));
						result = false;
					}
				}
				else
				{
					Map<String, Object> itemSpecificOptions = new HashMap<String, Object>();
					itemSpecificOptions.putAll(options);
					pass = initialPass((GIdentifiable) objD, itemConf, itemSpecificOptions);
					if (!pass)
					{
						listOfProblems.add(new MultiplicityProblem((GIdentifiable) objD, itemConf, itemSpecificOptions));
						result = false;
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param objD
	 * @param itemConf
	 * @param options
	 * @return
	 */
	public boolean initialPassForChoice(GChoiceContainerDef objD, GIdentifiable itemConf, Map<String, Object> options)
	{
		// if we have more than one ChoiceContainer instance then the rule
		// fails... and we need to delete one of them
		int nrOfInstances = getNrOfInstances(objD, itemConf);
		if (nrOfInstances > 1)
		{
			options.put(SynchronizeConstants.multCreate, false);
			return false;
		}

		// set the options
		int low = 0;
		if (objD.gGetLowerMultiplicityAsString() != null)
		{
			try
			{
				low = Integer.valueOf(objD.gGetLowerMultiplicityAsString());
			}
			catch (NumberFormatException e)
			{
				// do nothing
			}
			finally
			{
				if (low < 0)
				{
					low = 0;
				}
			}
		}
		options.put(SynchronizeConstants.itemLowerMult, low);
		String upperMultiplicityAsString = objD.gGetUpperMultiplicityAsString();
		if (objD.gGetUpperMultiplicityInfinite() || SynchronizeConstants.multStar.equals(upperMultiplicityAsString))
		{
			options.put(SynchronizeConstants.itemUpperMult, Integer.MAX_VALUE);
			upperMultiplicityAsString = SynchronizeConstants.multStar;
		}
		else
		{
			if (upperMultiplicityAsString == null || upperMultiplicityAsString.length() < 1)
			{
				upperMultiplicityAsString = "1"; //$NON-NLS-1$
			}

			try
			{
				if (Integer.valueOf(upperMultiplicityAsString) < 0)
				{
					upperMultiplicityAsString = String.valueOf(1);
				}
			}
			catch (NumberFormatException e)
			{
				upperMultiplicityAsString = String.valueOf(1);
			}
			finally
			{
				options.put(SynchronizeConstants.itemUpperMult, Integer.valueOf(upperMultiplicityAsString));
			}

		}

		// get the nr of instances, of the defChild definitions
		int nrOfChildInst = 0;
		for (EObject defChild: objD.eContents())
		{
			if (defChild instanceof GIdentifiable)
			{
				nrOfChildInst += getNrOfInstances4Choice((GIdentifiable) defChild, itemConf);
			}
		}
		options.put(SynchronizeConstants.itemNrOfInstances, nrOfChildInst);

		return checkMult(options, nrOfChildInst, String.valueOf(low), upperMultiplicityAsString);
	}

	/**
	 * @param defChild
	 * @param itemConf
	 * @return
	 */
	private int getNrOfInstances4Choice(GIdentifiable defChild, GIdentifiable itemConf)
	{
		GIdentifiable itemChild = null;

		for (EObject obj: itemConf.eContents())
		{
			if (obj instanceof GContainer)
			{
				if (((GContainer) obj).gGetDefinition().equals(defChild.eContainer()))
				{
					itemChild = (GIdentifiable) obj;
				}
			}
		}
		return getNrOfInstances(defChild, itemConf) + getNrOfInstances(defChild, itemChild);
	}

	public boolean initialPass(GIdentifiable objDef, GIdentifiable owner, Map<String, Object> options)
	{
		int instancesCount = 0;

		instancesCount = getNrOfInstances(objDef, owner);
		options.put(SynchronizeConstants.itemNrOfInstances, instancesCount);

		GParamConfMultiplicity obj = (GParamConfMultiplicity) objDef;
		String lowerMultiplicityAsString = obj.gGetLowerMultiplicityAsString();
		String upperMultiplicityAsString = obj.gGetUpperMultiplicityAsString();
		if (obj.gGetUpperMultiplicityInfinite() || SynchronizeConstants.multStar.equals(upperMultiplicityAsString))
		{
			options.put(SynchronizeConstants.itemUpperMult, Integer.MAX_VALUE);
			upperMultiplicityAsString = SynchronizeConstants.multStar;
		}
		else
		{
			try
			{
				if (upperMultiplicityAsString == null || upperMultiplicityAsString.length() < 1
					|| Integer.valueOf(upperMultiplicityAsString) < 0)
				{
					upperMultiplicityAsString = String.valueOf(1);
				}
			}
			catch (NumberFormatException e)
			{
				upperMultiplicityAsString = String.valueOf(1);
			}
			finally
			{
				options.put(SynchronizeConstants.itemUpperMult, Integer.valueOf(upperMultiplicityAsString));
			}

		}
		try
		{
			if (lowerMultiplicityAsString != null && lowerMultiplicityAsString.length() >= 1)
			{
				if (Integer.valueOf(lowerMultiplicityAsString) < 0)
				{
					lowerMultiplicityAsString = ZERO;
				}
			}
			else
			{
				lowerMultiplicityAsString = ZERO;
			}
		}
		catch (NumberFormatException e)
		{
			lowerMultiplicityAsString = ZERO;

		}
		finally
		{
			options.put(SynchronizeConstants.itemLowerMult, Integer.valueOf(lowerMultiplicityAsString));
		}
		return checkMult(options, instancesCount, lowerMultiplicityAsString, upperMultiplicityAsString);
	}

	/**
	 * @param options
	 * @param instancesCount
	 * @param lowerMultiplicityAsString
	 * @param upperMultiplicityAsString
	 * @return
	 */
	private boolean checkMult(Map<String, Object> options, int instancesCount, String lowerMultiplicityAsString,
		String upperMultiplicityAsString)
	{
		Boolean flag = (Boolean) options.get(SynchronizeConstants.multOptionalFlag);
		Integer optInst = 0;
		if (flag)
		{
			optInst = (Integer) options.get(SynchronizeConstants.multOptionalValue);
		}

		int low = 1;
		if (lowerMultiplicityAsString != null)
		{
			low = Integer.valueOf(lowerMultiplicityAsString);
		}

		if (low > instancesCount)
		{
			options.put(SynchronizeConstants.multCreate, true);
			return false;
		}
		if (upperMultiplicityAsString.equals(SynchronizeConstants.multStar))
		{
			if (optInst <= instancesCount)
			{
				return true;
			}
			else
			{
				options.put(SynchronizeConstants.multCreate, true);
				return false;
			}
		}
		else
		{
			if (Integer.valueOf(upperMultiplicityAsString) < instancesCount)
			{
				options.put(SynchronizeConstants.multCreate, false);
				return false;
			}
			else
			{
				if (optInst > instancesCount && (instancesCount != Integer.valueOf(upperMultiplicityAsString)))
				{
					options.put(SynchronizeConstants.multCreate, true);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param objDef
	 * @param owner
	 * @param instancesCount
	 * @return
	 */
	private int getNrOfInstances(GIdentifiable objDef, GIdentifiable owner)
	{
		int instancesCount = 0;
		if (owner instanceof GContainer)
		{
			if (objDef instanceof GContainerDef)
			{
				EList<GContainer> subContainers = ((GContainer) owner).gGetSubContainers();
				for (GContainer gContainer: subContainers)
				{
					if (objDef.equals(gContainer.gGetDefinition()))
					{
						instancesCount++;
					}
				}
			}
			else
			{
				EList<? extends GConfigReferenceValue> values1 = ((GContainer) owner).gGetReferenceValues();
				// count the instances
				for (GConfigReferenceValue gConfRefValue: values1)
				{
					if (objDef.equals(gConfRefValue.gGetDefinition()))
					{
						instancesCount++;
					}
				}

				EList<? extends GParameterValue> values = ((GContainer) owner).gGetParameterValues();
				// count the instances
				for (GParameterValue gParameterValue: values)
				{
					if (objDef.equals(gParameterValue.gGetDefinition()))
					{
						instancesCount++;
					}
				}
			}
		}
		else
		{
			if (owner instanceof GModuleConfiguration)
			{
				EList<GContainer> values = ((GModuleConfiguration) owner).gGetContainers();

				// count the instances
				for (GContainer gParameterValue: values)
				{
					if (objDef.equals(gParameterValue.gGetDefinition()))
					{
						instancesCount++;
					}
				}
			}
		}
		return instancesCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.synchronizer.internal.rules.AbstractRule#isAplicableTo(gautosar.ggenericstructure.ginfrastructure
	 * .GIdentifiable, gautosar.ggenericstructure.ginfrastructure.GIdentifiable)
	 */
	@Override
	public boolean isAplicableTo(GIdentifiable objConf)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.synchronizer.internal.rules.AbstractRule#configureProblems(gautosar.ggenericstructure.ginfrastructure
	 * .GIdentifiable, gautosar.ggenericstructure.ginfrastructure.GIdentifiable)
	 */
	@Override
	protected List<AbstractProblem> configureProblems(GIdentifiable elemConf, Map<String, Object> options)
	{
		return listOfProblems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.rules.AbstractRule#createSpecificOptions(java.util.HashMap)
	 */
	@Override
	protected Map<String, Object> createSpecificOptions(Map<String, Object> options)
	{
		Map<String, Object> multOptions = new HashMap<String, Object>();
		if (options != null)
		{
			multOptions.put(SynchronizeConstants.multOptionalFlag, options.get(SynchronizeConstants.multOptionalFlag));
			multOptions.put(SynchronizeConstants.multOptionalValue, options.get(SynchronizeConstants.multOptionalValue));
		}
		return multOptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.rules.AbstractRule#getDeepFlag()
	 */
	@Override
	protected Boolean getDeepFlag()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.rules.AbstractRule#getBlockingType()
	 */
	@Override
	protected Boolean getBlockingType()
	{
		return true;
	}

}
