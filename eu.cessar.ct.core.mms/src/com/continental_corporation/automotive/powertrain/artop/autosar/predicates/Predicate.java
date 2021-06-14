package com.continental_corporation.automotive.powertrain.artop.autosar.predicates;

import org.eclipse.emf.ecore.EObject;

/**
 * A Predicate is the object equivalent of an if statement. Predicates are used by the QueryPredicate in order to
 * filter/select elements in AUTOSAR models.
 */
public interface Predicate
{
	/**
	 * Evaluates the object.
	 *
	 * @param object
	 *        object to evaluate
	 * @return true if the object is equal to the predicates implementation; false if not
	 */
	boolean evaluate(EObject object);
}
