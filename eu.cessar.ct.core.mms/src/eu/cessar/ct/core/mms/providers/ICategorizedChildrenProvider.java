package eu.cessar.ct.core.mms.providers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This interface must be implemented when is needed to make customizations on
 * the way the given children descriptors are organized. The result returned by
 * the methods can be a list or any data structure (map, tree)
 */
public interface ICategorizedChildrenProvider
{
	/**
	 * The method that split specified children descriptors on categories. The
	 * returned result is a mapping between each category and the children
	 * descriptors belonging to that category.<br>
	 * A category is represented by a list of strings, similar with a path. The
	 * category pointed by the empty strings list is considered root category.
	 * 
	 * <pre>
	 * ----------------------------------------------------------------------------------
	 * category                     | children descriptors
	 * ----------------------------------------------------------------------------------
	 * [] (root category)           | [ARPackage_descriptor, AnotherChild_descriptor]
	 * [System]                     | [System_descriptor, ECUInstance_descriptor]
	 * [System, Flex]               | [FlexCluster_descriptor, FlexArray_descriptor]
	 * [System, Communication, Lin] | [LinCluster_descriptor]
	 * [System, Communication, Can] | [CanCluster_descriptor]
	 * ----------------------------------------------------------------------------------
	 * </pre>
	 * 
	 * @param childDescriptors
	 *        the children descriptors to be splited on categories
	 * @return A map containing categorized children descriptors.
	 */
	Map<List<String>, Collection<?>> getCategorizedChildrenDescriptors(
		Collection<?> childDescriptors);
}
