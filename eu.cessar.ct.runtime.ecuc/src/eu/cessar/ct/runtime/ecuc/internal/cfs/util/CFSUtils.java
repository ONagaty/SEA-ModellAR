/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 2, 2009 10:29:19 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.cfs.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.internal.cfs.ECFSTypeEnum;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * Utility class for the Cessar File System
 */
public class CFSUtils
{

	/** name of glue file store */
	public static final String GLUE_FILE_NAME = "~glue"; //$NON-NLS-1$

	/**
	 * Return the type of element inside the Cessar File System represented by
	 * the given <code>uri</code>.
	 * 
	 * @param uri
	 * @return
	 */
	public static ECFSTypeEnum getCFSType(URI uri)
	{
		// [scheme:]scheme-specific-part[#fragment]
		// [scheme:][//authority][path][?query][#fragment]
		// e.g cfs://PROJECT_NAME/AUTOSAR/ARoot
		Path path = new Path(uri.getSchemeSpecificPart());
		if (path.segmentCount() == 0)
		{
			return ECFSTypeEnum.UNKNOWN;
		}
		if (path.segmentCount() == 1)
		{
			// looks like a root, but check to see if the project name is valid
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				path.lastSegment());
			if (project != null && project.isAccessible())
			{
				return ECFSTypeEnum.ROOT;
			}
			else
			{
				return ECFSTypeEnum.UNKNOWN;
			}
		}
		if (GLUE_FILE_NAME.equals(path.lastSegment()))
		{
			return ECFSTypeEnum.WRAPPER_GLUE;
		}

		// check if uri.Path() represents a qualified name of an EObject inside
		// uri.getAuthority() project
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(uri.getAuthority());

		ECFSTypeEnum wrapperStoreType = getCFSWrapperStoreType(project, uri.getPath());
		if (wrapperStoreType != ECFSTypeEnum.UNKNOWN)
		{
			return wrapperStoreType;
		}
		// TO DO: check if uri represents a java element

		return ECFSTypeEnum.UNKNOWN;
	}

	/**
	 * Looks inside given <code>project</code> for Module Definitions and/or
	 * ARPackages having the given <code>qualifiedName</code> and returns the
	 * corresponding CFS type <br>
	 * </br> NOTE:
	 * <ul>
	 * <li>
	 * If more then 1 Module Definition detected, it will log an error and will
	 * return ECFSTypeEnum.UNKNOWN</li>
	 * <li>
	 * If both a Module Definition and an ARPackage detected, it will log an
	 * error and will return ECFSTypeEnum.WRAPPER_ARPACKAGE</li>
	 * </li>
	 * </ul>
	 * 
	 * @param project
	 * @param qualifiedName
	 * @return
	 */
	public static ECFSTypeEnum getCFSWrapperStoreType(IProject project, String qualifiedName)
	{
		List<EObject> list = EObjectLookupUtils.getEObjectsWithQName(project, qualifiedName);
		int listSize = list.size();

		if (listSize == 0)
		{
			return ECFSTypeEnum.UNKNOWN;
		}
		if (listSize == 1)
		{
			EObject eObject = list.get(0);
			if (eObject instanceof GARPackage)
			{
				return ECFSTypeEnum.WRAPPER_ARPACKAGE;
			}
			if (eObject instanceof GModuleDef)
			{
				return ECFSTypeEnum.WRAPPER_MODULE_DEFINITION;
			}
		}
		else
		{// more then 1 element with same qName, so we have to do some checks
			int moduleCount = 0;
			int arpackageCount = 0;
			String moduleFileNames = ""; //$NON-NLS-1$
			String arpackageFielNames = ""; //$NON-NLS-1$

			for (EObject eObj: list)
			{
				if (eObj instanceof GARPackage)
				{
					arpackageCount++;
					arpackageFielNames += eObj.eResource().getURI().lastSegment() + " "; //$NON-NLS-1$
				}
				else if (eObj instanceof GModuleDef)
				{
					moduleCount++;
					moduleFileNames += eObj.eResource().getURI().lastSegment() + " "; //$NON-NLS-1$
				}
			}

			// if more than 1 module definition found log error
			if (moduleCount > 1)
			{
				CessarPluginActivator.getDefault().logError(Messages.ModuleDefsWithSameQualifiedName,
					moduleCount, qualifiedName, moduleFileNames);
				if (arpackageCount == 0)
				{
					return ECFSTypeEnum.UNKNOWN;
				}
			}

			// both module definition and arpackage found with the same
			// qualified name ->log error and accept arpackage
			if (moduleCount > 0 && arpackageCount > 0)
			{
				CessarPluginActivator.getDefault().logError(Messages.ModuleDef_ARPackageWithSameQualifiedName,
					moduleCount, qualifiedName, moduleFileNames, arpackageFielNames);

				return ECFSTypeEnum.WRAPPER_ARPACKAGE;
			}

			// eventually we end with the situation where we have 2 or more
			// arpackages
			EObject eObject = list.iterator().next();

			if (eObject instanceof GARPackage)
			{
				return ECFSTypeEnum.WRAPPER_ARPACKAGE;
			}
		}

		return ECFSTypeEnum.UNKNOWN;
	}

	/**
	 * Encode given string <code>childName</code> and resolve it against
	 * <code>parent</code> URI
	 * 
	 * @param parent
	 * @param childName
	 * @return
	 */
	public static URI getChildURI(URI parent, String childName)
	{
		try
		{
			String encode = URLEncoder.encode(childName, "UTF-8"); //$NON-NLS-1$

			return new URI(parent.toString() + "/" + encode); //$NON-NLS-1$
		}
		catch (UnsupportedEncodingException e)
		{
			return parent;
		}
		catch (URISyntaxException e)
		{
			return parent;
		}
	}

	/*	try
		{
			String encode = URLEncoder.encode(childName, "UTF-8"); //$NON-NLS-1$
			URI resolve = parent.resolve("/" + encode);

			Path path = new Path(resolve.toString());
			int segmentCount = path.segmentCount();
			return resolve;
		}
		catch (UnsupportedEncodingException e)
		{
			Activator.getDefault().logError(e);
			return parent;
		}
	}*/

}
