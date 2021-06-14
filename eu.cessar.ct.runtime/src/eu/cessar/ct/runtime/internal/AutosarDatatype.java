/**
 * <copyright>
 *
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 *
 * File created by uid95513<br/>
 * May 20, 2015 1:12:49 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.internal;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.cid.extension.AbstractDatatype;
import eu.cessar.ct.core.platform.ui.util.SelectionUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Type describing the cid type with the same name. It contains as parameter the GIdentifiable corresponding to the
 * EClass given in the cid file.
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Wed Jun 17 09:44:43 2015 %
 *
 *         %version: 3 %
 */
@Requirement(
	reqID = "54756")
public class AutosarDatatype extends AbstractDatatype<GIdentifiable>
{
	private static final String CONTAINER_TYPE = "containertype"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.cid.extension.IDatatype#getDatatypeClass()
	 */
	@Override
	public Class<GIdentifiable> getDatatypeClass()
	{
		return GIdentifiable.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.cid.extension.IDatatype#convertToString(java.lang.Object)
	 */
	@Override
	public String convertToString(GIdentifiable data)
	{
		if (data == null)
		{
			return ""; //$NON-NLS-1$
		}
		return ModelUtils.getAbsoluteQualifiedName(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.cid.extension.IDatatype#convertFromString(java.lang.String)
	 */
	@Override
	public GIdentifiable convertFromString(String qualifiedName)
	{
		IProject activeProject = SelectionUtils.getActiveProject(false);
		if (activeProject != null)
		{
			GIdentifiable autosarObject = ModelUtils.getEObjectWithQualifiedName(activeProject, qualifiedName, false);
			return autosarObject;
		}
		return null;
	}

	/**
	 * @return the eclass specified in .cid file
	 */
	public String getEclassFromCid()
	{
		String eclass = getParameter(CONTAINER_TYPE, null);
		return eclass;
	}
}
