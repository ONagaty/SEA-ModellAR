/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Jul 4, 2013 5:57:27 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.mdl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

/**
 * Interface for ModelDependencyMatrix.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Mon Dec  9 15:38:23 2013 %
 * 
 *         %version: 3 %
 */
public interface IModelDependencyMatrix
{

	/**
	 * Indicates if the {@code source} may refer, directly or indirectly, via non-containment references, the
	 * {@code dest}.
	 * 
	 * @param source
	 *        the source {@code EClass}
	 * @param dest
	 *        the destination {@code EClass}
	 * @return true if {@code source} may refer {@code dest}, false otherwise
	 */
	public boolean mayRefer(EClass source, EClass dest);

	/**
	 * Indicates if {@code parent} may contain, directly or indirectly, the {@code child}.
	 * 
	 * @param parent
	 *        the parent {@code EClass}
	 * @param child
	 *        the child {@code EClass}
	 * @return true if {@code parent} may contain {@code child}, false otherwise
	 */
	public boolean mayContain(EClass parent, EClass child);



	/**
	 * Get the references of {@code fromClass} to follow in a reachability search from {@code fromClass} to
	 * {@code toClass}. The {@code eligib} parameter indicates whether to follow containment or non-containment
	 * references.
	 * 
	 * @param fromClass
	 *        the starting {@code EClass}
	 * @param toClass
	 *        the destination {@code EClass}
	 * @param eligib
	 *        the eligibility (containment or non-containment)
	 * @return the non-null, possibly empty, list of references
	 */
	public List<EReference> getReferences(EClass fromClass, EClass toClass, MDLEligibility eligib);

	/**
	 * Get all EClasses
	 * 
	 * @param eligib
	 *        the eligibility (containment or non-containment)
	 * @return collection of all {@link EClass}
	 */
	public Collection<EClass> getEAllClasses(MDLEligibility eligib);

}
