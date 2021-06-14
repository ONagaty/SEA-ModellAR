package eu.cessar.ct.core.mms.internal;

import java.util.Map;

/**
 * 
 */
public interface IMMNamespaceMapping
{
	/**
	 * @param namespace
	 * @return
	 */
	public String getPackageName(String namespace);

	/**
	 * @return
	 */
	public String getRootNamespace();

	/**
	 * @return
	 */
	public Map<String, String> getAllPackages();
}
