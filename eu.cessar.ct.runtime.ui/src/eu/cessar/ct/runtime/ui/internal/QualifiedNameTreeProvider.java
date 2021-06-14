/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * May 21, 2015 11:38:13 AM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import eu.cessar.ct.cid.extension.IDatatype;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Class filters the content of a tree viewer on a given element type, listing the qualified names
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Mon Jun 22 13:10:00 2015 %
 *
 *         %version: 3 %
 */
@Requirement(
	reqID = "54756")
public class QualifiedNameTreeProvider implements ITreeContentProvider
{
	private IProject project;
	private EClass eclass;
	private IDatatype<GIdentifiable> type;

	/**
	 * @param selectedEClass
	 * @param selectedProject
	 */
	public QualifiedNameTreeProvider(EClass selectedEClass, IProject selectedProject)
	{
		project = selectedProject;
		eclass = selectedEClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose()
	{
		// not implemented
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// not implemented since it is not applicable - input can not be changed while wizard is opened
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement)
	{
		List<String> qNamesList = new ArrayList<String>();
		if (eclass != null)
		{
			List<EObject> filteredList = ModelUtils.getInstancesOfType(project, eclass, true);
			for (EObject eObject: filteredList)
			{
				if (eObject instanceof GIdentifiable)
				{
					String absoluteQualifiedName = type.convertToString((GIdentifiable) eObject);
					qNamesList.add(absoluteQualifiedName);
				}
			}
		}
		return qNamesList.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement)
	{
		return Collections.EMPTY_LIST.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element)
	{
		return Collections.EMPTY_LIST.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element)
	{
		// implementation does not yet refer hierarchies of elements because it is not applicable
		return false;
	}

	/**
	 * @param datatype
	 */
	public void setDatatype(IDatatype<GIdentifiable> datatype)
	{
		type = datatype;
	}
}
