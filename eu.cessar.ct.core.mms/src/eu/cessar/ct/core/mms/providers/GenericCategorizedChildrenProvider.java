package eu.cessar.ct.core.mms.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;

/**
 * Default implementation of {@link IEMFChildrenProvider} interface. This class
 * use the provided editing domain to collect the new children and sibling
 * descriptors and return them as a collection.
 */
@SuppressWarnings("unchecked")
public abstract class GenericCategorizedChildrenProvider extends AdapterImpl implements
	ICategorizedChildrenProvider
{
	/**
	 * @author uidu3379
	 * 
	 */
	private final class ByNameComparator implements Comparator<Object>
	{
		private final static int NO_VALUE = Integer.MAX_VALUE;

		public int compare(Object o1, Object o2)
		{
			int result = NO_VALUE;

			if (!(o1 instanceof CommandParameter))
			{
				result = -1;
			}
			else if (!(o2 instanceof CommandParameter))
			{
				result = 1;
			}

			if (result != NO_VALUE)
			{
				return result;
			}

			EObject eObject1 = ((CommandParameter) o1).getEValue();
			EObject eObject2 = ((CommandParameter) o2).getEValue();
			if ((eObject1 == null) && (eObject2 == null))
			{
				result = 0;
			}
			else if ((eObject1 == null) && (eObject2 != null))
			{
				result = 1;
			}
			else if ((eObject1 != null) && (eObject2 == null))
			{
				result = -1;
			}
			else
			{
				result = eObject1.eClass().getName().compareTo(eObject2.eClass().getName());
			}

			return result;
		}
	}

	/**
	 * Internal comparator implementation used to compare two lists of strings.
	 */
	private static final class StringListComparator implements Comparator<List<String>>
	{
		private final static int NO_VALUE = Integer.MAX_VALUE;

		public int compare(List<String> o1, List<String> o2)
		{
			int result = NO_VALUE;

			// empty strings array shout be at the end
			int o1Size = o1.size();
			int o2Size = o2.size();

			if ((o1Size == 0) && (o2Size == 0))
			{
				result = 0;
			}
			else if (o1Size == 0)
			{
				result = -1;
			}
			else if (o2Size == 0)
			{
				result = 1;
			}

			if (result != NO_VALUE)
			{
				return result;
			}

			if (o1Size == o2Size)
			{
				result = compareIfSameSize(o1, o2);
			}
			else
			{
				result = compareIfDifferentSize(o1, o2);
			}
			return result;
		}

		private int compareIfSameSize(List<String> o1, List<String> o2)
		{
			int result = NO_VALUE;

			int value;
			for (int i = 0; i < o1.size(); i++)
			{
				value = o1.get(i).compareTo(o2.get(i));
				if (value != 0)
				{
					result = value;
					break;
				}
			}
			if (result == NO_VALUE)
			{
				result = 0;
			}
			return result;
		}

		private int compareIfDifferentSize(List<String> o1, List<String> o2)
		{
			int result = NO_VALUE;

			int o1Size = o1.size();
			int o2Size = o2.size();

			if (o1Size < o2Size)
			{
				int value;
				for (int i = 0; i < o1Size; i++)
				{
					value = o1.get(i).compareTo(o2.get(i));
					if (value != 0)
					{
						result = value;
						break;
					}
				}
				if (result == NO_VALUE)
				{
					result = -1;
				}
			}
			else
			{
				int value;
				for (int i = 0; i < o2Size; i++)
				{
					value = o1.get(i).compareTo(o2.get(i));
					if (value != 0)
					{
						result = value;
						break;
					}
				}
				if (result == NO_VALUE)
				{
					result = 1;
				}
			}
			return result;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.providers.ICategorizedChildrenProvider#getCategorizedChildrenDescriptors(java.util.Collection)
	 */
	public Map<List<String>, Collection<?>> getCategorizedChildrenDescriptors(
		Collection<?> childDescriptors)
	{
		Map<List<String>, Collection<?>> result = indexChildDescriptors(childDescriptors);
		return result;
	}

	/**
	 * @param descriptors
	 * @return
	 */
	private Map<List<String>, Collection<?>> indexChildDescriptors(Collection<?> descriptors)
	{
		Map<List<String>, Collection<?>> result = new TreeMap<List<String>, Collection<?>>(
			new StringListComparator());

		if ((descriptors != null) && !descriptors.isEmpty())
		{
			for (Object childDesc: descriptors)
			{
				List<String> path = getPathForChildDescriptor(childDesc);
				if (path != null)
				{
					Collection<Object> tempArray = null;
					if (result.containsKey(path))
					{
						tempArray = (Collection<Object>) result.get(path);
					}
					else
					{
						tempArray = new ArrayList<Object>();
						result.put(path, tempArray);
					}
					tempArray.add(childDesc);
				}
			}

			compactChildrenMap(result);
			sortChildrenByName(result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.adapter.IEMFChildrenProvider#getPathForChildDescriptor(java.lang.Object)
	 */
	public List<String> getPathForChildDescriptor(Object descriptor)
	{
		if (descriptor instanceof CommandParameter)
		{
			EObject value = ((CommandParameter) descriptor).getEValue();
			if (value != null)
			{
				return computeEPackagePath(value.eClass(), 3);
			}
		}
		return null;
	}

	/**
	 * Compute the packages path to specified {@link EClass}, using MM namespace
	 * mapping.
	 * 
	 * @param eClass
	 *        the {@link EClass} instance to which a packages path is required
	 * @param maxLength
	 *        the maximum number of fragments allowed in the path.
	 * @return A list of strings representing the packages path to given
	 *         {@link EClass}.
	 */
	private List<String> computeEPackagePath(EClass eClass, int maxLength)
	{
		List<String> tokens = new ArrayList<String>();
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(eClass);
		String rootPackageURI = mmService.getRootPackageURI();
		if (eClass != null)
		{
			String packNamespaceURI = eClass.getEPackage().getNsURI();
			while (!rootPackageURI.equalsIgnoreCase(packNamespaceURI))
			{
				String name = mmService.getPackageLabel(packNamespaceURI);
				if ((name != null) && (name.length() > 0))
				{
					tokens.add(name);
				}

				int slashIndex = packNamespaceURI.lastIndexOf("/"); //$NON-NLS-1$
				if (slashIndex == -1)
				{
					break;
				}
				packNamespaceURI = packNamespaceURI.substring(0, slashIndex);
			}

			((ArrayList<String>) tokens).trimToSize();
			Collections.reverse(tokens);

			if (tokens.size() > maxLength)
			{
				return tokens.subList(0, maxLength);
			}
		}
		return tokens;
	}

	/**
	 * @param childrenMap
	 */
	private void sortChildrenByName(Map<List<String>, Collection<?>> childrenMap)
	{
		// sort the values of the returned map
		Iterator<List<String>> iterator = childrenMap.keySet().iterator();
		while (iterator.hasNext())
		{
			List<String> key = iterator.next();
			Collection<?> childDescriptors = childrenMap.get(key);
			if (childDescriptors instanceof List)
			{
				Collections.sort((List<?>) childDescriptors, new ByNameComparator());
			}
		}
	}

	private Map<List<String>, Collection<?>> compactChildrenMap(
		Map<List<String>, Collection<?>> childrenMap)
	{
		List<List<String>> tempKeyList = new ArrayList<List<String>>(childrenMap.keySet());
		for (List<String> key: tempKeyList)
		{
			Collection<?> childDescriptors = childrenMap.get(key);
			if (key.size() == 0)
			{
				// empty key is a root key, don't touch it
				continue;
			}

			if ((childDescriptors instanceof List) && (((List<?>) childDescriptors).size() == 1))
			{
				List<String> newKey = new ArrayList<String>(key.subList(0, key.size() - 1));
				Collection<Object> tempArray = null;
				if (childrenMap.containsKey(newKey))
				{
					tempArray = (Collection<Object>) childrenMap.get(newKey);
				}
				else
				{
					tempArray = new ArrayList<Object>();
					childrenMap.put(newKey, tempArray);
				}
				tempArray.addAll(childDescriptors);
				childrenMap.remove(key);
			}
		}
		return null;
	}

}
