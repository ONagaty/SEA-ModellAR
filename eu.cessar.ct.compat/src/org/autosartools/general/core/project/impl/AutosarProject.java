/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 29, 2010 9:46:49 AM </copyright>
 */
package org.autosartools.general.core.project.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.autosartools.general.core.data.ARPackageWrapper;
import org.autosartools.general.core.project.CommonConstants;
import org.autosartools.general.core.project.IAutosarProject;
import org.eclipse.core.resources.IProject;

/**
 * Implementation of an Autosar project.
 * 
 * @author uidl6458
 * @Review uidl7321 - Apr 4, 2012
 * 
 */
public class AutosarProject implements IAutosarProject
{

	private final IProject project;
	private Map<String, Collection<String>> packagesHierarchy;
	private Map<String, ARPackageWrapper> wrappedARPackages;
	private Map<String, Collection<String>> filesForPackages;

	public AutosarProject(IProject project)
	{
		this.project = project;
		packagesHierarchy = new HashMap<String, Collection<String>>();
		wrappedARPackages = new HashMap<String, ARPackageWrapper>();
		filesForPackages = new HashMap<String, Collection<String>>();
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#addPackage(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addPackage(String fileName, String packageName, String parent)
	{
		addPackage(fileName, packageName, parent, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#addPackage(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public void addPackage(String fileName, String packageName, String parent, boolean notify)
	{
		// Index package and the defining file
		Collection<String> files = null;
		String packageFullPath = packageName;

		if (!CommonConstants.ROOT_PACKAGES.equals(parent))
		{
			packageFullPath = parent + CommonConstants.PATH_SEPARATOR + packageName;
		}

		files = filesForPackages.get(packageFullPath);

		if (files == null)
		{
			files = new ArrayList<String>();

			// Wrap the new ARPackage in ARPackageWrapper class
			// Because an ARPackage can be defined across several files
			wrappedARPackages.put(packageFullPath, new ARPackageWrapper(this, packageName, parent));
		}

		if (!files.contains(fileName))
		{
			files.add(fileName);
		}

		filesForPackages.put(packageFullPath, files);

		// Add this new package to the existing package hierarchy
		Collection<String> childPackages = packagesHierarchy.get(parent);
		if (childPackages == null)
		{
			childPackages = new ArrayList<String>();
		}

		if (!childPackages.contains(packageName))
		{
			childPackages.add(packageName);
		}

		packagesHierarchy.put(parent, childPackages);

		// if (notify)
		// {
		// fireModification(this);
		// }
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#removeFile(java.lang.String)
	 */
	public void removeFile(String fileName)
	{
		removeFile(fileName, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#removeFile(java.lang.String, boolean)
	 */
	public void removeFile(String fileName, boolean notify)
	{
		// Loop through the index looking for all packages
		// referenced in the deleted file
		Set<Entry<String, Collection<String>>> entries = new HashSet<Entry<String, Collection<String>>>(
			filesForPackages.entrySet());
		for (Entry<String, Collection<String>> entry: entries)
		{
			String pack = entry.getKey();
			Collection<String> files = entry.getValue();

			if (files.contains(fileName))
			{
				// Remove file from list
				files.remove(fileName);

				if (files.isEmpty())
				{
					// if no file define current package
					// remove package from index and hierarchy
					filesForPackages.remove(pack);
					wrappedARPackages.remove(pack);

					// Loop through the package hierarchy
					// to remove this package occurences
					String packNameToRemove = pack;
					String parent = CommonConstants.ROOT_PACKAGES;
					if (pack.indexOf(CommonConstants.PATH_SEPARATOR) != -1)
					{
						packNameToRemove = pack.substring(pack.lastIndexOf(CommonConstants.PATH_SEPARATOR) + 1);
						parent = pack.substring(0, pack.lastIndexOf(CommonConstants.PATH_SEPARATOR));
					}

					Set<Entry<String, Collection<String>>> packEntries = new HashSet<Entry<String, Collection<String>>>(
						packagesHierarchy.entrySet());
					for (Entry<String, Collection<String>> packEntry: packEntries)
					{
						String pak = packEntry.getKey();
						Collection<String> packs = packEntry.getValue();

						if (pak.equals(parent))
						{
							// Remove package from its parent list
							packs.remove(packNameToRemove);

							// if parent has no children, remove
							// it from the list of parents
							if (packs.isEmpty() && !CommonConstants.ROOT_PACKAGES.equals(pak))
							{
								packagesHierarchy.remove(pak);
							}
							else
							{
								packagesHierarchy.put(pak, packs);
							}
						}
					}
				}
				else
				{
					filesForPackages.put(pack, files);
				}

			}
		}

		// if (notify)
		// {
		// fireModification(this);
		// }
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#removeARPackage(java.lang.String, java.lang.String, boolean)
	 */
	public void removeARPackage(String fullPackagePath, String definitionFile, boolean notify)
	{
		Collection<String> files = getFilesForPackage(fullPackagePath);

		if ((files != null) && files.contains(definitionFile))
		{
			// Remove file from list
			files.remove(definitionFile);

			if (files.isEmpty())
			{
				// if no file define current package
				// remove package from index and hierarchy
				filesForPackages.remove(fullPackagePath);
				wrappedARPackages.remove(fullPackagePath);

				// Loop through the package hierarchy
				// to remove this package occurences
				String packNameToRemove = fullPackagePath;
				String parent = CommonConstants.ROOT_PACKAGES;
				if (fullPackagePath.indexOf(CommonConstants.PATH_SEPARATOR) != -1)
				{
					packNameToRemove = fullPackagePath.substring(fullPackagePath.lastIndexOf(CommonConstants.PATH_SEPARATOR) + 1);
					parent = fullPackagePath.substring(0,
						fullPackagePath.lastIndexOf(CommonConstants.PATH_SEPARATOR));
				}

				Set<Entry<String, Collection<String>>> packEntries = new HashSet<Entry<String, Collection<String>>>(
					packagesHierarchy.entrySet());
				for (Entry<String, Collection<String>> packEntry: packEntries)
				{
					String pak = packEntry.getKey();
					Collection<String> packs = packEntry.getValue();

					if (pak.equals(parent))
					{
						// Remove package from its parent list
						packs.remove(packNameToRemove);

						// if parent has no children, remove
						// it from the list of parents
						if (packs.isEmpty() && !CommonConstants.ROOT_PACKAGES.equals(pak))
						{
							packagesHierarchy.remove(pak);
						}
						else
						{
							packagesHierarchy.put(pak, packs);
						}
					}
				}
			}
			else
			{
				filesForPackages.put(fullPackagePath, files);
			}
		}

		// if (notify)
		// {
		// fireModification(this);
		// }
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#getExistingPackages()
	 */
	public Map<String, Collection<String>> getExistingPackages()
	{
		return packagesHierarchy;
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#getFilesForPackages()
	 */
	public Map<String, Collection<String>> getFilesForPackages()
	{
		return filesForPackages;
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#getFilesForPackage(java.lang.String)
	 */
	public Collection<String> getFilesForPackage(String packageName)
	{
		return filesForPackages.get(packageName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#getARPackageWrapper(java.lang.String)
	 */
	public ARPackageWrapper getARPackageWrapper(String packageName)
	{
		return wrappedARPackages.get(packageName);
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.project.IAutosarProject#getMainARPackageWrappers()
	 */
	public Map<String, ARPackageWrapper> getMainARPackageWrappers()
	{
		HashMap<String, ARPackageWrapper> result = new HashMap<String, ARPackageWrapper>();

		Set<Map.Entry<String, ARPackageWrapper>> entries = wrappedARPackages.entrySet();
		for (Map.Entry<String, ARPackageWrapper> entry: entries)
		{
			String name = entry.getKey();
			ARPackageWrapper wrapper = entry.getValue();

			if (wrapper.isMainARPackage())
			{
				result.put(name, wrapper);
			}
		}

		return result;
	}
}
