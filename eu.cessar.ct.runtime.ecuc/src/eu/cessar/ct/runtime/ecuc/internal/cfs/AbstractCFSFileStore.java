/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 5, 2009 4:44:32 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.cfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.filesystem.provider.FileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;

/**
 * Base implementation for Cessar File Store
 */
public abstract class AbstractCFSFileStore extends FileStore
{

	/** store name */
	private String name;
	private URI uri;
	private File diskFile;
	private IProject project;
	private IEcucModel ecucModel;

	/**
	 * @param name
	 */
	public AbstractCFSFileStore(String name, URI uri)
	{
		this.name = name;
		this.uri = uri;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize()
	{
		Path path = new Path(uri.toString());

		String projectName = path.segment(0);

		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.isAccessible())
		{
			return;
		}
		ecucModel = IEcucCore.INSTANCE.getEcucModel(project);

		URL url = null;
		try
		{
			url = FileLocator.resolve(project.getLocationURI().toURL());
		}
		catch (MalformedURLException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		if (url != null)
		{
			diskFile = new File(url.getPath() + uri.getPath());
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#openInputStream(int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public InputStream openInputStream(int options, IProgressMonitor monitor) throws CoreException
	{
		try
		{
			return new FileInputStream(diskFile);
		}
		catch (FileNotFoundException e)
		{
			throw new CoreException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, "Error", e));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#toURI()
	 */
	@Override
	public URI toURI()
	{
		return uri;
	}

/*
 * (non-Javadoc)
 * @see org.eclipse.core.filesystem.provider.FileStore#getName()
 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @return
	 */
	protected IEcucModel getEcucModel()
	{
		return ecucModel;
	}

	/**
	 * 
	 * @return
	 */
	protected IProject getProject()
	{
		return project;
	}

}
