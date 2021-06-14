/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Mar 3, 2014 1:10:41 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;

import eu.cessar.ct.cid.CIDModels;
import eu.cessar.ct.cid.ICIDGlobalStorage;
import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.runtime.CessarRuntimeUtils;

/**
 * Utilities class used when running plugets from project
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Fri Mar 14 11:55:01 2014 %
 * 
 *         %version: 2 %
 */
public final class RunPlugetUtils
{
	/**
	 * The CID singleton instance
	 */
	public static final RunPlugetUtils INSTANCE = new RunPlugetUtils();
	/**
	 * Artifact type for pluget
	 */
	public static final String PLUGET_AR_TYPE = "pluget"; //$NON-NLS-1$

	private RunPlugetUtils()
	{
		// utility class does not allow instantiation
	}

	public List<IFile> getProjectPlugets(IProject project)
	{

		// get plugets from project
		List<IFile> plugets = new ArrayList<IFile>();
		CessarRuntimeUtils.collectPlugetFiles(project, plugets);
		return plugets;

	}

	/**
	 * Get artifacts of type pluget, global artifacts and for {@link project}.
	 * 
	 * @param project
	 * @return global artifacts for this resource
	 */
	public List<Artifact> getArtifactPlugets(IProject project)
	{

		List<Artifact> artifacts = new ArrayList<Artifact>();
		// get global artifacts
		ICIDGlobalStorage cidGlobalStorage = CIDModels.getCIDGlobalStorage();
		List<Artifact> globalArtifacts = cidGlobalStorage.getArtifacts(PLUGET_AR_TYPE);

		// get project artifacts
		List<Artifact> projectArtifacts = CIDModels.getCIDProjectStorage(project).getArtifacts(PLUGET_AR_TYPE);
		artifacts.addAll(globalArtifacts);
		artifacts.addAll(projectArtifacts);
		return artifacts;
	}

	/**
	 * Return project for the {@link element}
	 * 
	 * @param element
	 *        - element to search project for
	 * @return project for the {@link element}
	 */
	public IProject getProject(Object element)
	{
		IProject project = null;
		if (element instanceof IResource)
		{
			project = ((IResource) element).getProject();
		}
		else if (element instanceof IAdaptable)
		{
			IAdaptable adapted = (IAdaptable) element;
			IResource res = (IResource) adapted.getAdapter(IResource.class);
			if (res != null)
			{
				project = res.getProject();
			}
		}
		return project;
	}

}
