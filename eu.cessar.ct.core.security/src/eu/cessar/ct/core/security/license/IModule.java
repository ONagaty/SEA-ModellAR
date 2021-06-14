package eu.cessar.ct.core.security.license;

import java.util.Collection;

public interface IModule
{
	/**
	 * Return the name of the product
	 * 
	 * @return the name of the product
	 */
	public String getName();

	/**
	 * Return the list of the parameters
	 * 
	 * @return the list of the parameters
	 */
	public Collection<String> getParametersList();
}
