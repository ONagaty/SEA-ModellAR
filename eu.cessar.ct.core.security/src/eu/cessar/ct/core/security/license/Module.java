package eu.cessar.ct.core.security.license;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

class Module implements IModule
{

	/** Module name */
	private String _name = null;

	/** List of parameters */
	private Collection<String> _parametersList = null;

	/**
	 * Default constructor
	 * 
	 * @param name
	 *        the name of the module
	 */
	Module(String name)
	{

		_name = name;
		_parametersList = new ArrayList<String>();
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
	 * Return the list of the parameters
	 * 
	 * @return the list of the parameters
	 */
	public Collection<String> getParametersList()
	{

		return Collections.unmodifiableCollection(_parametersList);
	}

	/**
	 * Add a parameter without value
	 * 
	 * @param parameter
	 *        the parameter to add
	 */
	public void addParameter(String parameter)
	{

		if (parameter != null)
		{
			if (!_parametersList.contains(parameter))
			{
				_parametersList.add(parameter);
			}
		}
	}

	/**
	 * Remove a parameter
	 * 
	 * @param parameter
	 *        the parameter to remove
	 */
	public void removeParameter(String parameter)
	{

		_parametersList.remove(parameter);
	}

	/**
	 * Remove all the parameters
	 */
	public void removeAllParameters()
	{

		_parametersList.clear();
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
		if (obj instanceof Module)
		{
			Module module = (Module) obj;
			res = (_name == null && module._name == null)
				|| (_name != null && _name.equals(module._name));
			if (res)
			{
				res = (_parametersList == null && module._parametersList == null)
					|| (_parametersList != null && _parametersList.equals(module._parametersList));
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
		buf.append("; parametersList=");
		for (Iterator<String> it = _parametersList.iterator(); it.hasNext();)
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