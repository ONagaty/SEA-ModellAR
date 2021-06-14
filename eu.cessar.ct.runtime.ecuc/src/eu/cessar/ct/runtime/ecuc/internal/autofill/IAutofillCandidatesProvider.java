/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Feb 7, 2014 2:53:50 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.autofill;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType;
import eu.cessar.ct.sdk.autofill.IContainerFragmentFilter;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Provides the candidates for the automatic fill of values.
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Fri Feb 7 16:13:39 2014 %
 *
 *         %version: 1 %
 */
public interface IAutofillCandidatesProvider
{

	/**
	 * @return the mapping between the SEA container wrappers and the active fragments
	 */
	Map<ISEAContainer, GContainer> getCandidates();

	/**
	 * @return the parameter definition, derived from the passed parameter name
	 */
	GReferrable getParameterDefinition();

	/**
	 *
	 * @param project
	 *        the used project
	 * @param moduleConfigQualifiedName
	 *        , module configuration qualified name, could be <code>null</code>
	 * @param containerDefinitionQualifiedName
	 *        , container definition qualified name, must not be <code>null</code>
	 * @param parameterName
	 *        parameter short name
	 * @param automaticFillType
	 *        fill type
	 * @param filter
	 *        fragment filter, can be <code>null</code>
	 * @return the status indicating whether the candidates were successfully computed
	 */
	<T> IStatus computeCandidates(IProject project, String moduleConfigQualifiedName,
		String containerDefinitionQualifiedName, final String parameterName,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter filter);
}
