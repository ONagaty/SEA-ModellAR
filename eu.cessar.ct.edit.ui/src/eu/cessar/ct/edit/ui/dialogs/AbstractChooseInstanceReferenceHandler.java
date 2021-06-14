/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jul 12, 2011 2:24:39 PM </copyright>
 */
package eu.cessar.ct.edit.ui.dialogs;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.ITreeContentProvider;

import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import eu.cessar.ct.edit.ui.EditingPreferencesAccessor;
import eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor;
import eu.cessar.ct.edit.ui.instanceref.InstanceRefTreeContentProvider;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 * 
 */
public abstract class AbstractChooseInstanceReferenceHandler implements IChooseIRefHandler
{
	private String filteringString;

	private InstanceReferenceEditor editor;

	private boolean permitIncompleteConfigs;

	private boolean isSystemInstanceRef;

	private IProject project;

	/**
	 * @param editor
	 */
	public AbstractChooseInstanceReferenceHandler(InstanceReferenceEditor editor)
	{
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#init()
	 */
	public void init()
	{
		permitIncompleteConfigs = EditingPreferencesAccessor.getSysInstanceRefPref(getProject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#getCandidates()
	 */
	public List<Object> getCandidates()
	{
		return editor.getCandidates();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#computeCandidates()
	 */
	public void computeCandidates() throws InstanceRefConfigurationException
	{
		editor.computeCandidates();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#showCandidatesForIncompleteConfigs()
	 */
	public boolean areIncompleteConfigsPermitted()
	{
		return permitIncompleteConfigs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see \#setPermitIncompleteConfigs(boolean)
	 */
	public void setPermitIncompleteConfigs(boolean permit)
	{
		permitIncompleteConfigs = permit;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#hasCandidatesForCompleteConfig()
	 */
	public boolean hasCandidatesForCompleteConfig()
	{
		return editor.hasCandidatesForCompleteConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#hasCandidatesForIncompleteConfig()
	 */
	public boolean hasCandidatesForIncompleteConfig()
	{
		return editor.hasCandidatesForIncompleteConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#getCanidatesMap()
	 */
	public Map<GIdentifiable, List<List<GIdentifiable>>> getCanidatesMap()
	{
		return editor.getCandidatesMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#getTreeContentProvider()
	 */
	public ITreeContentProvider getTreeContentProvider()
	{
		return new InstanceRefTreeContentProvider(getCanidatesMap(), this);
	}

	/**
	 * @return context types
	 */
	public List<IContextType> getContextTypes()
	{
		return editor.getContextTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#isSystemInstanceRef()
	 */
	public boolean isSystemInstanceRef()
	{
		return isSystemInstanceRef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#setIsSystemInstanceRef()
	 */
	public void setIsSystemInstanceRef(boolean isSystemInstRef)
	{
		isSystemInstanceRef = isSystemInstRef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#getTargetType()
	 */
	public EClass getTargetType()
	{
		return editor.getTargetType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject(IProject project)
	{
		this.project = project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#setFilterString(java.lang.String)
	 */
	public void setFilterString(String filterString)
	{
		filteringString = filterString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#getFilterString()
	 */
	public String getFilterString()
	{
		return filteringString;
	}

}
