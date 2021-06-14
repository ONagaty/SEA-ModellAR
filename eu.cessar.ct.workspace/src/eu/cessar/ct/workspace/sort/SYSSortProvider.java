/**
 * 
 */
package eu.cessar.ct.workspace.sort;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.workspace.internal.sort.SYSSortProviderImpl;

/**
 * @author uidt2045
 * 
 */
public class SYSSortProvider
{
	/**
	 * @param domain
	 * @param parentObject
	 * @return
	 */
	public static ISortProvider create(TransactionalEditingDomain domain, EObject parentObject)
	{
		return new SYSSortProviderImpl(domain, parentObject);
	}
}
