/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl7321<br/>
 * Feb 24, 2014 2:33:33 PM
 *
 * </copyright>
 */
package eu.cessar.ct.ecuc.workspace.internal.consistencycheck.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.internal.Messages;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.consistencycheck.impl.ProjectCheckInconsistency;
import eu.cessar.ct.workspace.internal.consistencycheck.impl.ProjectConsistencyCheckResult;
import eu.cessar.req.Requirement;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GecucparameterdefPackage;

/**
 * Checks if there are duplicate module definitions in a project and returns a result with additional details.
 *
 * @author uidl7321
 *
 *         %created_by: uidl7321 %
 *
 *         %date_created: Fri Mar 28 10:50:58 2014 %
 *
 *         %version: 8 %
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public class CheckDuplicateModuleDef implements IProjectConsistencyChecker
{
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.workspace.internal.consistencycheck.IProjectConsistencyChecker#performConsistencyCheck(org.eclipse
	 * .core.resources.IProject)
	 */
	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> resultList = new ArrayList<>();

		List<IProjectCheckInconsistency> projectCheckList = getAllProjectModuleDefinitionsDuplicates(project);

		IConsistencyCheckResult<IProjectCheckInconsistency> checkResult = new ProjectConsistencyCheckResult();
		checkResult.setInconsistencies(projectCheckList);
		checkResult.setStatus(Status.OK_STATUS);
		resultList.add(checkResult);

		return resultList;
	}

	/**
	 * Gets the project all module definitions.
	 *
	 * @param project
	 *        the project
	 * @return the project all module definitions
	 */
	private static List<IProjectCheckInconsistency> getAllProjectModuleDefinitionsDuplicates(IProject project)
	{

		List<IProjectCheckInconsistency> checkList = new ArrayList<>();

		// Get all module definitions in the project.
		Map<String, List<IFile>> map = new LinkedHashMap<>();

		List<EObject> allModuleDefs = ModelUtils.getInstancesOfType(project,
			GecucparameterdefPackage.eINSTANCE.getGModuleDef(), false);

		// Get duplicates and build the project inconsistencies list
		ProjectCheckInconsistency projectCheckInconsistency;

		for (EObject module: allModuleDefs)
		{
			if (module instanceof GModuleDef)
			{
				IFile file = ModelUtils.getDefiningFile(module);
				String qName = MetaModelUtils.getAbsoluteQualifiedName(module);

				List<IFile> files;

				if (!map.containsKey(qName))
				{
					files = new ArrayList<>();
				}

				else
				{
					files = map.get(qName);
				}

				files.add(file);
				map.put(qName, files);
			}
		}

		if (!map.isEmpty())
		{
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext())
			{
				String qName = it.next();
				List<IFile> files = map.get(qName);

				if (files.size() > 1)
				{

					projectCheckInconsistency = new ProjectCheckInconsistency();
					projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.DUPLICATE_MODULE_DEF);
					projectCheckInconsistency.setSeverity(ESeverity.ERROR);
					projectCheckInconsistency.setMessage(NLS.bind(Messages.duplicate_moduleDef_inconsistency, qName));
					projectCheckInconsistency.addFiles(map.get(qName));

					checkList.add(projectCheckInconsistency);
				}
			}

		}

		return checkList;
	}

}
