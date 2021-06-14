/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 29, 2010 9:37:49 AM </copyright>
 */
package eu.cessar.ct.sdk.ant;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.EProjectVariant;
import gautosar.gecucdescription.GModuleConfiguration;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;

/**
 * Abstract implementation of the get variant task.
 * 
 */
public abstract class AbstractGetVariantTask extends AbstractCessarTask
{
	// ====== task's attributes

	private String property;

	// =======

	protected GModuleConfiguration modConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.AbstractTask#doExecute()
	 */
	@Override
	protected void doExecute() throws BuildException
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(iProject);
		IEcucMMService ecucMMService = mmService.getEcucMMService();
		String variant = ecucMMService.getImplementationConfigVariantAsString(modConfig);

		if (ecucMMService.isSetImplementationConfigVariant(modConfig) && variant != null)
		{
			// set the property with module config's variant
			getProject().setProperty(property, variant);
		}
		else
		{
			// set property with project's variant
			EProjectVariant projectVariant = CESSARPreferencesAccessor.getProjectVariant(iProject);
			if (projectVariant != null)
			{
				getProject().setProperty(property, projectVariant.name());
			}
		}
	}

	@Override
	protected void checkArgs()
	{
		super.checkArgs();

		if (property == null || property.equals("")) //$NON-NLS-1$
		{
			throw new BuildException("property attribute is mandatory!"); //$NON-NLS-1$
		}
	}

	@Override
	protected void processArgs()
	{
		super.processArgs();
		doRetrieveModuleConfig();
	}

	/**
	 * 
	 */
	protected abstract void doRetrieveModuleConfig();

	protected GModuleConfiguration getModuleConfigFromFile(final String ecuc_file_name)
	{
		// ecuc_file_name is set
		final List<IFile> files = new ArrayList<IFile>();

		// locate the file(s) with given name inside current project
		try
		{
			iProject.accept(new IResourceProxyVisitor()
			{
				public boolean visit(final IResourceProxy proxy) throws CoreException
				{
					if (proxy.getType() == IResource.FILE && !proxy.isDerived())
					{
						if (proxy.getName().equals(ecuc_file_name))
						{
							files.add((IFile) proxy.requestResource());
						}
					}
					return true;
				}
			}, 0);

		}
		catch (CoreException exception)
		{
			throw new BuildException(exception.getMessage());
		}

		if (files.size() == 1)
		{
			EObject root = EcorePlatformUtil.loadModelRoot(files.get(0));
			if (root != null)
			{
				List<Resource> list = new ArrayList<Resource>();
				list.add(root.eResource());
				List<GModuleConfiguration> res = EObjectUtil.getAllInstancesOf(list, GModuleConfiguration.class, false);

				if (res.size() == 1)
				{
					return res.get(0);
				}
				else
				{
					// 0 or more elements with the same qualified name found
					throw new BuildException(NLS.bind("{1} module configuration(s) have been found inside {0} file.", //$NON-NLS-1$
						new Object[] {ecuc_file_name, res.size()}));
				}
			}
		}
		else
		{
			// 0 or more then 1 file found
			throw new BuildException(NLS.bind("{1} file(s) having {0} name  have been found.", //$NON-NLS-1$
				new Object[] {ecuc_file_name, files.size()}));
		}
		return null;
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(final String property)
	{
		this.property = property;
	}

}