/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 10, 2010 4:02:53 PM </copyright>
 */
package eu.cessar.ct.edit.ui.dialogs;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import eu.cessar.ct.edit.ui.facility.parts.editor.SystemIRefEditorPart;
import eu.cessar.ct.edit.ui.instanceref.GenericIRefSelectionDialog;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 * 
 */
public interface IChooseIRefHandler
{
	/**
	 * Called by the {@link GenericIRefSelectionDialog} to get the window title
	 * 
	 * @return a String representing the short name of the edited reference
	 */
	String getDialogTitle();

	/**
	 * Ask the handler to compute the valid candidates for the configuration of the instance reference.
	 * 
	 * @throws InstanceRefConfigurationException
	 */
	void computeCandidates() throws InstanceRefConfigurationException;

	/**
	 * Get the list of valid targets for the reference value. These candidates will represent valid selections inside
	 * the table displayed by the {@link GenericIRefSelectionDialog}. <br>
	 * NOTE: <code>computeCandidates()</code> must be invoked before retrieving the target candidates
	 * 
	 * @return
	 */
	List<Object> getCandidates();

	/**
	 * Get the target candidates and the associated context candidates
	 * 
	 * @return a map having as key a valid target and as value a list of valid contexts
	 */
	Map<GIdentifiable, List<List<GIdentifiable>>> getCanidatesMap();

	/**
	 * @return
	 */
	List<IContextType> getContextTypes();

	/**
	 * Allows the handler to initialize internal data (e.g read user preferences)
	 */
	void init();

	/**
	 * Return <code>true</code> if incomplete configurations of the system instance references are permitted,
	 * <code>false</code> otherwise.
	 * 
	 * @return
	 */
	boolean areIncompleteConfigsPermitted();

	/**
	 * Sets the flag internally inside the handler. Called when the user changes the flag inside the
	 * {@link GenericIRefSelectionDialog}.
	 * 
	 * @param permit
	 */
	void setPermitIncompleteConfigs(boolean permit);

	/**
	 * Return <code>true</code> if there are candidates for a complete configuration, <code>false</code> otherwise
	 * 
	 * @return
	 */
	boolean hasCandidatesForCompleteConfig();

	/**
	 * Return <code>true</code> if there are candidates for an incomplete configuration of the instance reference,
	 * <code>false</code> otherwise. NOTE: for ECUC instance reference <code>false</code> is returned
	 * 
	 * @return
	 */
	boolean hasCandidatesForIncompleteConfig();

	/**
	 * Get the target of the reference
	 * 
	 * @return
	 */
	GIdentifiable getTarget();

	/**
	 * Get the EClass corresponding to the target
	 * 
	 * @return
	 */
	EClass getTargetType();

	/**
	 * Get the context of the reference
	 * 
	 * @return
	 */
	List<GIdentifiable> getContext();

	/**
	 * Called by {@link GenericIRefSelectionDialog} when "OK" button is pressed.
	 * 
	 * @param newTarget
	 *        the new target to be set
	 * @param newContext
	 *        the new context to be set
	 */
	void setReference(GIdentifiable newTarget, List<GIdentifiable> newContext);

	/**
	 * The implementation of this interface must provide a tree content provider which will be used for the table inside
	 * the {@link InstanceReferenceSelectionDialog}.
	 */
	ITreeContentProvider getTreeContentProvider();

	/**
	 * The implementation of this interface must provide a tree table label provider which will be used for the table
	 * inside the {@link InstanceReferenceSelectionDialog}. provider.
	 */
	ITableLabelProvider getTableLabelProvider();

	/**
	 * 
	 * @return
	 */
	boolean isSystemInstanceRef();

	/**
	 * 
	 * @param isSysInstRef
	 */
	void setIsSystemInstanceRef(boolean isSysInstRef);

	/**
	 * Obtain the corresponding project
	 * 
	 * @return
	 */
	IProject getProject();

	/**
	 * Sets the project. Called by {@link SystemIRefEditorPart}
	 * 
	 * @param project
	 */
	void setProject(IProject project);

	/**
	 * Set filtering text
	 * 
	 * @param filterString
	 */
	void setFilterString(String filterString);

	/**
	 * Retrieve the filtering text
	 * 
	 * @return
	 */
	String getFilterString();

}
