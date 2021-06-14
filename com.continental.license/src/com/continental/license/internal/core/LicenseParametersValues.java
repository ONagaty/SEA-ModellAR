package com.continental.license.internal.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class LicenseParametersValues
{
	private static final String SEPARATOR = ",";
	private static final String EQUALS_SIGN = "=";

	private Map<String, String> _map = null;

	/**
	 * Converts LicenseParametersValues to String.
	 * 
	 * @param moduleParams
	 *        the LicenseParametersValues.
	 * @return the String.
	 */
	public static String moduleParamsToString(LicenseParametersValues licParamsValues)
	{

		StringBuffer buffer = new StringBuffer();
		if (licParamsValues != null)
		{
			Map<String, String> map = licParamsValues._map;
			String sep = "";
			for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<String, String> entry = it.next();
				buffer.append(sep);
				buffer.append(entry.getKey()).append(EQUALS_SIGN).append(entry.getValue());
				sep = SEPARATOR;
			}
		}

		return buffer.toString();
	}

	/**
	 * Converts String to LicenseParametersValues.
	 * 
	 * @param str
	 *        the String.
	 * @return the LicenseParametersValues.
	 */
	public static LicenseParametersValues stringToModuleParams(String str)
	{

		StringTokenizer stk = new StringTokenizer(str, SEPARATOR);
		LicenseParametersValues licParamsValues = new LicenseParametersValues();

		while (stk.hasMoreTokens())
		{
			String token = stk.nextToken();
			int pos = token.indexOf(EQUALS_SIGN);
			if (pos != -1)
			{
				String paramKey = token.substring(0, pos);
				String paramValue = token.substring(pos + 1, token.length());
				licParamsValues.put(paramKey, paramValue);
			}
		}

		return licParamsValues;
	}

	/**
	 * Default constructor
	 */
	LicenseParametersValues()
	{
		_map = new HashMap<String, String>();
	}

	/**
	 * Associates the specified value with the specified key.
	 * 
	 * @param key
	 *        the key.
	 * @param value
	 *        the value.
	 */
	public void put(String key, String value)
	{
		_map.put(key, value);
	}

	/**
	 * Returns the value associated to the key.
	 * 
	 * @param key
	 *        the key.
	 * @return the value, or <tt>null</tt>.
	 */
	public String get(String key)
	{
		return _map.get(key);
	}
	
	/**
	 * Returns a set of the mappings contained in the map.
	 * @return a set of the mappings contained in the map.
	 */
	public Set<Map.Entry<String, String>> entrySet()
	{
		return _map.entrySet();
	}
}
