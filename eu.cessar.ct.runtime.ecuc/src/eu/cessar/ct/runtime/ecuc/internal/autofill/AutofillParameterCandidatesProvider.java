/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr8466<br/>
 * Mar 8, 2016 6:19:44 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.autofill;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType;
import eu.cessar.ct.sdk.autofill.IContainerFragmentFilter;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * TODO: Please comment this class
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class AutofillParameterCandidatesProvider extends AutofillCandidatesProvider
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.autofill.AutofillCandidatesProvider#computeCandidates(org.eclipse.core.resources
	 * .IProject, java.lang.String, java.lang.String, java.lang.String,
	 * eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType, eu.cessar.ct.sdk.autofill.IContainerFragmentFilter)
	 */
	@Override
	public <T> IStatus computeCandidates(IProject project, String moduleConfigQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, EAutomaticValuesFillType automaticFillType,
		IContainerFragmentFilter filter)
	{
		IStatus status = super.computeCandidates(project, moduleConfigQualifiedName, containerDefinitionQualifiedName,
			parameterName, automaticFillType, filter);
		if (status == Status.OK_STATUS)
		{
			parameterDefinition = getParameterDefByName(containerdefinition, parameterName);
		}

		return status;
	}

	/**
	 * @param containerDef
	 * @param paramName
	 * @return the parameter definition with the <code>paramName</code> from <code>containerDef</code>
	 */
	private static GConfigParameter getParameterDefByName(GParamConfContainerDef containerDef, String paramName)
	{
		GConfigParameter parameterDef = null;
		EList<GConfigParameter> parametersDefs = containerDef.gGetParameters();
		for (GConfigParameter paramDef: parametersDefs)
		{
			if (paramDef.gGetShortName().equalsIgnoreCase(paramName))
			{
				parameterDef = paramDef;
				break;
			}
		}
		return parameterDef;
	}
}
