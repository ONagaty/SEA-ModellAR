/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jan 31, 2011 5:33:17 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.sync.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.DeleteParamProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.DeleteProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.DeleteRefProblem;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidt2045
 * 
 */
public class DefinitionExistRule extends AbstractRule
{
	private ArrayList<AbstractProblem> listOfProblems;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.rules.AbstractRule#checkRule(gautosar.ggenericstructure.ginfrastructure.
	 * GIdentifiable, gautosar.ggenericstructure.ginfrastructure.GIdentifiable)
	 */
	@Override
	public boolean passesRule(GIdentifiable objConf, Map<String, Object> options)
	{
		if (objConf == null)
		{
			return false;
		}
		listOfProblems = new ArrayList<AbstractProblem>();
		boolean result = true;
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(objConf);
		IEcucMMService service = mmService.getEcucMMService();
		IStatus status;
		if (objConf instanceof GModuleConfiguration)
		{
			status = service.validateDefinition((GModuleConfiguration) objConf);
			if (status.getCode() != 0)
			{
				listOfProblems.add(new DeleteProblem(objConf));
				result = false;
			}
		}
		else
		{
			if (objConf instanceof GContainer)
			{
				status = service.validateDefinition((GContainer) objConf);
				if (status.getCode() != 0)
				{
					// no point continuing with the verification
					listOfProblems.add(new DeleteProblem(objConf));
					result = false;
				}
				else
				{
					// go deep to the parameters and children
					result = goDeepOnParam(service, (GContainer) objConf) && goDeepOnRef(service, (GContainer) objConf);
				}
			}
		}
		return result;
	}

	/**
	 * corresponding to the reference, and the status returned we can create the necessary problem
	 * 
	 * @param service
	 * @param objConf
	 * @return
	 */
	private boolean goDeepOnRef(IEcucMMService service, GContainer objConf)
	{
		// TODO Auto-generated method stub
		boolean result = true;
		IStatus status;
		for (GConfigReferenceValue value: objConf.gGetReferenceValues())
		{
			status = service.validateDefinition(value);
			if (status.getCode() != 0)
			{
				result = false;
				listOfProblems.add(new DeleteRefProblem(value));
			}
		}
		return result;
	}

	/**
	 * corresponding to the parameter, and the status returned we can create the necessary problem
	 * 
	 * @param service
	 * @param objConf
	 * @return
	 */
	private boolean goDeepOnParam(IEcucMMService service, GContainer objConf)
	{
		// TODO Auto-generated method stub
		boolean result = true;
		IStatus status;
		for (GParameterValue value: objConf.gGetParameterValues())
		{
			status = service.validateDefinition(value);
			if (status.getCode() != 0)
			{
				result = false;
				listOfProblems.add(new DeleteParamProblem(value));
			}
		}
		return result;
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
	 * @see eu.cessar.ct.synchronizer.internal.rules.AbstractRule#getRuleDescription()
	 */
	@Override
	public String getRuleDescription()
	{
		return SynchronizeConstants.defExistRuleDescr;
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
		return new HashMap<String, Object>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.rules.AbstractRule#getDeepFlag()
	 */
	@Override
	protected Boolean getDeepFlag()
	{
		return false;
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
