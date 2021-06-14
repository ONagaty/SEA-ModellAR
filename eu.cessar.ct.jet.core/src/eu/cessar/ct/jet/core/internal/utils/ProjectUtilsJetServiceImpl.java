/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Mar 3, 2011 10:54:55 AM </copyright>
 */
package eu.cessar.ct.jet.core.internal.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.jet.core.JETPackerJob;
import eu.cessar.ct.sdk.utils.ProjectUtils;

/**
 * @author uidt2045
 * 
 */
public class ProjectUtilsJetServiceImpl implements ProjectUtils.JetService
{

	public static final ProjectUtilsJetServiceImpl eINSTANCE = new ProjectUtilsJetServiceImpl();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.utils.ProjectUtils.JetService#compileJet(org.eclipse.core.resources.IProject, org.eclipse.core.resources.IFile)
	 */
	public IStatus compileJet(IProject project, IFile jetFile)
	{
		return compileJets(project, Collections.singletonList(jetFile));
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.utils.ProjectUtils.JetService#compileJets(org.eclipse.core.resources.IProject, java.util.Collection)
	 */
	public IStatus compileJets(IProject project, Collection<IFile> jetFiles)
	{
		JETPackerJob jetPackJob = new JETPackerJob(project,
			JETCoreUtils.getJetComplianceLevel(project));

		Iterator<IFile> iterator = jetFiles.iterator();
		while (iterator.hasNext())
		{
			jetPackJob.addFile(iterator.next());
		}

		jetPackJob.schedule();

		// wait for JET packer job to finish compiling the JET
		try
		{
			jetPackJob.join();
		}
		catch (InterruptedException e)
		{
			return Status.CANCEL_STATUS;
		}
		return jetPackJob.getPackagingResult();
	}

}
