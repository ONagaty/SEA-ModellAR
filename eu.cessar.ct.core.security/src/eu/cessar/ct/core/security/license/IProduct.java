package eu.cessar.ct.core.security.license;

import java.util.Collection;

public interface IProduct
{
	/**
	 * Return the name of the product
	 * 
	 * @return the name of the product
	 */
	public String getName();

	/**
	 * Return the module associated to the name
	 * 
	 * @param name the name of the module
	 * @return the module associated to the name
	 */
	public IModule getModule(String name);

	/**
	 * Return the list of the modules
	 * 
	 * @return the list of the modules
	 */
	public Collection<IModule> getModulesList();
}
