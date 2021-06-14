package com.continental.license.internal.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

class Product implements IProduct
{

	/** Product name */
	private String _name = null;

	/** Module list */
	private Collection<IModule> _modulesList = null;

	/**
	 * Default constructor
	 * 
	 * @param name
	 *        the name of the product
	 */
	Product(String name)
	{

		_name = name;
		_modulesList = new ArrayList<IModule>();
	}

	/**
	 * Return the name of the product
	 * 
	 * @return the name of the product
	 */
	public String getName()
	{

		return _name;
	}

	/**
	 * Return the module associated to the name.
	 * 
	 * @param name
	 *        the name of the module
	 * @return the module associated to the name.
	 */
	public IModule getModule(String name)
	{

		Iterator<IModule> it = _modulesList.iterator();
		IModule module = null;
		for (; it.hasNext() && module == null;)
		{
			module = it.next();
			if (module != null && !module.getName().equals(name))
			{
				module = null;
			}
		}
		return module;
	}

	/**
	 * Return the list of the modules
	 * 
	 * @return the list of the modules
	 */
	public Collection<IModule> getModulesList()
	{

		return Collections.unmodifiableCollection(_modulesList);
	}

	/**
	 * Add a module
	 * 
	 * @param module
	 *        the module to add
	 */
	public void addModule(Module module)
	{

		if (module != null)
		{
			if (!_modulesList.contains(module))
			{
				_modulesList.add(module);
			}
		}
	}

	/**
	 * Remove a module
	 * 
	 * @param module
	 *        the module to remove
	 */
	public void removeModule(Module module)
	{

		if (module != null)
		{
			String name = module.getName();
			for (Iterator<IModule> it = _modulesList.iterator(); it.hasNext();)
			{
				IModule iModule = it.next();
				if (iModule.getName().equals(name))
				{
					it.remove();
					break;
				}
			}
		}
	}

	/**
	 * Remove all the modules
	 */
	public void removeAllModules()
	{

		_modulesList.clear();
	}

	@Override
	public int hashCode()
	{
		return _name == null ? super.hashCode() : _name.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean res = false;
		if (obj instanceof Product)
		{
			Product product = (Product) obj;
			res = (_name == null && product._name == null)
				|| (_name != null && _name.equals(product._name));
			if (res)
			{
				res = (_modulesList == null && product._modulesList == null)
					|| (_modulesList != null && _modulesList.equals(product._modulesList));
			}
		}
		return res;
	}

	@Override
	public String toString()
	{
		
		StringBuffer buf = new StringBuffer();
		buf.append(getClass());
		buf.append("[");
		buf.append("name=").append(_name);
		buf.append("; moduleList=");
		for (Iterator<IModule> it = _modulesList.iterator(); it.hasNext();)
		{
			buf.append(it.next());
			if (it.hasNext()) {
				buf.append(", ");
			}
		}
		buf.append("]");
		return buf.toString();
	}
}
