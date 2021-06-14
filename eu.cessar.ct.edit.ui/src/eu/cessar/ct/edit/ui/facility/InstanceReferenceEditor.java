/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jul 12, 2011 2:15:39 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 * 
 */
public interface InstanceReferenceEditor extends IModelFragmentReferenceEditor
{

	public boolean hasCandidatesForCompleteConfig();

	public boolean hasCandidatesForIncompleteConfig();

	public void computeCandidates() throws InstanceRefConfigurationException;

	public Map<GIdentifiable, List<List<GIdentifiable>>> getCandidatesMap();

	public List<IContextType> getContextTypes();

	public EClass getTargetType();
}
