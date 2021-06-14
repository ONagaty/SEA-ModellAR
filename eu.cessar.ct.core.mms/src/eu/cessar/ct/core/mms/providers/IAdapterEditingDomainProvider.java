package eu.cessar.ct.core.mms.providers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;

/**
 * Generic interface that should be implemented by classes that provide adapter
 * factories and editing domains.
 */
public interface IAdapterEditingDomainProvider extends IEditingDomainProvider
{
	/**
	 * @return {@link AdapterFactory} for the used {@link EditingDomain},
	 *         depending on each implementation of this method.
	 */
	public AdapterFactory getAdapterFactory();
}
