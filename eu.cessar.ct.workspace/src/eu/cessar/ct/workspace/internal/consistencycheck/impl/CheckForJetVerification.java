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
package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredSelection;

import eu.cessar.ct.core.platform.util.JarUtils;
import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.sdk.utils.ProjectUtils;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.Messages;
import eu.cessar.req.Requirement;

/**
 * Checks if there are any unCompiled jets, or any old jar files inside the project.
 *
 * @author uidg3464
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Mon Feb 16 07:57:37 2015 %
 *
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public class CheckForJetVerification implements IProjectConsistencyChecker
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
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> resultList = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		List<IProjectCheckInconsistency> projectCheckList = getAllJetVerificationCheckResults(project);

		IConsistencyCheckResult<IProjectCheckInconsistency> checkResult = new ProjectConsistencyCheckResult();
		checkResult.setInconsistencies(projectCheckList);
		checkResult.setStatus(Status.OK_STATUS);
		resultList.add(checkResult);

		return resultList;
	}

	/**
	 * Gets all the jet files inside the project.
	 *
	 * Check to see if there are unCompiled jetFiles or if there are any old jarFilles
	 *
	 * @param project
	 *        the project
	 * @return a check list after the jets have been checked
	 */
	private List<IProjectCheckInconsistency> getAllJetVerificationCheckResults(IProject project)
	{
		List<IFile> jetFiles = new ArrayList<IFile>();
		List<IFile> jarFiles = new ArrayList<IFile>();
		List<IFile> compiledJets = new ArrayList<IFile>();
		List<IFile> uncompiledJets = new ArrayList<IFile>();
		StructuredSelection strSelection = new StructuredSelection(project);
		try
		{
			JETCoreUtils.collectCompiledJETFiles(strSelection.toArray(), jarFiles);
			JETCoreUtils.collectJETFiles(strSelection.toArray(), jetFiles);
			compiledJets.addAll(jarFiles);
			for (IFile jetFile: jetFiles)
			{ // Check to see the compiled and unCompiled jetFilles
				IFile jarFile = JETCoreUtils.getJarFile(jetFile);
				if (!compiledJets.contains(jarFile))
				{
					if (jarFile.exists() && JETCoreUtils.isJetJarFile(jarFile))
					{
						compiledJets.add(jarFile);
					}
					else
					{
						uncompiledJets.add(jetFile);
					}
				}
			}
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		List<IProjectCheckInconsistency> checkList = new ArrayList<IProjectCheckInconsistency>();
		ProjectCheckInconsistency projectCheckInconsistency = null;

		// For each unCompiled jetFile we have to add a new projectCheckInconsistency
		for (IFile iFile: uncompiledJets)
		{
			projectCheckInconsistency = new ProjectCheckInconsistency();
			projectCheckInconsistency.setSeverity(ESeverity.ERROR);

			// Add the elements of the projectCheckInconsistency
			addInconsistencyForUncompiledJet(checkList, projectCheckInconsistency, iFile);

		}
		// we check to see if there are any old jar files
		checkForOldJars(jetFiles, checkList);
		return checkList;
	}

	/**
	 * Checks all the jet files to see if any of them as an old jar. If so add a new inconsistency
	 *
	 * @param jetFiles
	 * @param checkList
	 */
	private void checkForOldJars(List<IFile> jetFiles, List<IProjectCheckInconsistency> checkList)
	{
		String jarManifestEntry = null;
		String md5 = null;
		// For each jet check the jar file and see if it's older than the jet or not
		for (IFile jetFile: jetFiles)
		{

			IFile jarFile = JETCoreUtils.getJarFile(jetFile);
			if (jarFile.exists())
			{
				// Try to obtain the jarManifestEntry if it exists
				try
				{
					md5 = ProjectUtils.getMD5(jetFile);
					jarManifestEntry = JarUtils.getJarManifestEntry(jarFile, "MD5-Digest"); //$NON-NLS-1$
				}
				catch (CoreException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
				// If the MD5 doesn't exist then check after the Timestamp
				if (null == md5 || null == jarManifestEntry || jarManifestEntry.isEmpty())
				{
					long jarLocalTimeStamp = jarFile.getLocalTimeStamp();
					long jetLocalTimeStamp = jetFile.getLocalTimeStamp();
					if (jarLocalTimeStamp < jetLocalTimeStamp)
					{
						addCheckInconsistencyOldJar(checkList, jarFile);
					}
				}
				else
				{
					if (!md5.equals(jarManifestEntry))
					{
						addCheckInconsistencyOldJar(checkList, jarFile);
					}
				}
			}
		}
	}

	/**
	 * Add an inconsistency for the unCompiled jet
	 *
	 * @param checkList
	 * @param projectCheckInconsistency
	 * @param iFile
	 */
	private void addInconsistencyForUncompiledJet(List<IProjectCheckInconsistency> checkList,
		ProjectCheckInconsistency projectCheckInconsistency, IFile iFile)
	{
		projectCheckInconsistency.addFile(iFile);
		projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.JET_PROBLEMS);
		projectCheckInconsistency.setMessage(iFile.getName() + " " + Messages.error_UnCompilledJet); //$NON-NLS-1$
		checkList.add(projectCheckInconsistency);
	}

	/**
	 * Add a new project inconsistency for the old jarFile
	 *
	 * @param checkList
	 * @param jarFile
	 */
	private void addCheckInconsistencyOldJar(List<IProjectCheckInconsistency> checkList, IFile jarFile)
	{
		boolean isJetInconsistency;
		ProjectCheckInconsistency projectCheckInconsistency;
		projectCheckInconsistency = new ProjectCheckInconsistency();
		projectCheckInconsistency.setSeverity(ESeverity.ERROR);
		isJetInconsistency = true;
		if (isJetInconsistency)
		{
			projectCheckInconsistency.addFile(jarFile);
			projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.JET_PROBLEMS);
			projectCheckInconsistency.setMessage(jarFile.getName() + " " + Messages.error_OldJarFile); //$NON-NLS-1$
			checkList.add(projectCheckInconsistency);
		}
	}
}
