/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jul 19, 2012 9:16:46 AM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.rules;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.EnumValueProblem;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GEnumerationLiteralDef;
import gautosar.gecucparameterdef.GEnumerationParamDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidu0944
 * 
 */
public class EnumRule extends AbstractRule
{

	private ArrayList<AbstractProblem> listOfProblems;
	private static String ENUM_CHECK = "Value {0} not found in the enumeration { {1} }"; //$NON-NLS-1$
	private String currentValue;
	private String listOfValues;

	/**
	 * 
	 */
	public EnumRule()
	{
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule#passesRule(gautosar.ggenericstructure.ginfrastructure
	 * .GIdentifiable, java.util.HashMap)
	 */
	@Override
	public boolean passesRule(GIdentifiable objConf, Map<String, Object> options)
	{
		boolean result = true;
		listOfProblems = new ArrayList<AbstractProblem>();

		EList<EObject> eContents = objConf.eContents();
		for (EObject eItem: eContents)
		{
			if (eItem instanceof GParameterValue)
			{
				GConfigParameter paramDef = ((GParameterValue) eItem).gGetDefinition();
				if (paramDef instanceof GEnumerationParamDef)
				{
					// check for a default value
					IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(paramDef.eClass());
					if (mmService != null)
					{
						IEcucMMService ecucMMService = mmService.getEcucMMService();
						Object currValue = ecucMMService.getParameterValue((GParameterValue) eItem);
						if (!currValue.toString().equals("") && !ecucMMService.isSetDefaultValue(paramDef))
						{
							EList<GEnumerationLiteralDef> literals = ((GEnumerationParamDef) paramDef).gGetLiterals();
							if (literals.size() > 0)
							{
								StringBuffer sB = new StringBuffer("{"); //$NON-NLS-1$
								ArrayList<String> defValues = new ArrayList<String>();
								for (GEnumerationLiteralDef def: literals)
								{
									sB.append(def.gGetShortName());
									sB.append(","); //$NON-NLS-1$
									defValues.add(def.gGetShortName());
								}
								if (!defValues.contains(currValue))
								{
									sB.delete(sB.length() - 1, sB.length());
									sB.append("}"); //$NON-NLS-1$
									listOfProblems.add(new EnumValueProblem(eItem, currValue.toString(), sB.toString()));
									return false;
								}
							}
							else
							{
								listOfProblems.add(new EnumValueProblem(eItem, currValue.toString(), "[]"));
								return false;
							}
						}
					}
				}
			}

		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule#getRuleDescription()
	 */
	@Override
	public String getRuleDescription()
	{
		return MessageFormat.format(ENUM_CHECK, currentValue, listOfValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule#isAplicableTo(gautosar.ggenericstructure.ginfrastructure
	 * .GIdentifiable)
	 */
	@Override
	public boolean isAplicableTo(GIdentifiable objConf)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule#getDeepFlag()
	 */
	@Override
	protected Boolean getDeepFlag()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule#getBlockingType()
	 */
	@Override
	protected Boolean getBlockingType()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule#createSpecificOptions(java.util.HashMap)
	 */
	@Override
	protected HashMap<String, Object> createSpecificOptions(Map<String, Object> options)
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule#configureProblems(gautosar.ggenericstructure.
	 * ginfrastructure.GIdentifiable, java.util.HashMap)
	 */
	@Override
	protected List<AbstractProblem> configureProblems(GIdentifiable elemConf, Map<String, Object> options)
	{
		return listOfProblems;
	}

}
