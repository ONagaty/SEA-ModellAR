/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 28, 2010 5:55:03 PM </copyright>
 */
package org.autosartools.general.core.project;

import java.util.Collection;
import java.util.Map;

import org.autosartools.general.core.data.ARPackageWrapper;
import org.eclipse.core.resources.IProject;

/**
 * Interface for an Autosar project.
 * 
 * @author uidl6458
 * @Review uidl7321 - Apr 4, 2012
 * 
 */
public interface IAutosarProject
{

	/**
	 * Returns the corresponding Eclipse project.
	 * 
	 * @return the IProject
	 */
	public IProject getProject();

	/**
	 * Return a Map with an ARPackage path as key and the files' paths defining
	 * it.
	 * 
	 * @return Map < String, Collection < String >>
	 */
	public Map<String, Collection<String>> getFilesForPackages();

	/**
	 * Adds a package with the given name, to the corresponding file and parent.
	 * 
	 * @param fileName
	 *        corresponding file name
	 * @param packageName
	 *        corresponding package name
	 * @param parent
	 *        corresponding parent
	 */
	public void addPackage(String fileName, String packageName, String parent);

	/**
	 * Adds a package with the given name, to the corresponding file and parent.
	 * 
	 * @param fileName
	 *        corresponding file name
	 * @param packageName
	 *        corresponding package name
	 * @param parentPackage
	 *        corresponding parent
	 * @param notify
	 */
	public void addPackage(String file, String pack, String parentPackage, boolean b);

	/**
	 * Return the package wrapper, for the corresponding package name.
	 * 
	 * @param parentName
	 *        package name
	 * @return the package wrapper
	 */
	public ARPackageWrapper getARPackageWrapper(String parentName);

	/**
	 * Returns the mapping between the full path of a package wrapper and the
	 * package wrapped itself.
	 * 
	 * @return the mapping, or an empty one
	 */
	public Map<String, ARPackageWrapper> getMainARPackageWrappers();

	/**
	 * Returns the files' names containing the package with the given path.
	 * 
	 * @param fullPath
	 *        the package's full path
	 * @return collection of containing files
	 */
	public Collection<String> getFilesForPackage(String fullPath);

	/**
	 * Removes the package with the given path, from the resource with the given
	 * path.
	 * 
	 * @param fullPackagePath
	 *        package's path
	 * @param resourcePath
	 *        resource's path
	 * @param b
	 */
	public void removeARPackage(String fullPackagePath, String resourcePath, boolean b);

	/**
	 * Removes the file with the given path.
	 * 
	 * @param resourcePath
	 *        resource's path
	 * 
	 */
	public void removeFile(String resourcePath);

	/**
	 * Removes the file with the given name.
	 * 
	 * @param fileName
	 *        file's name
	 * @param b
	 */
	public void removeFile(String fileName, boolean notify);

	/**
	 * Return a mapping between a parent package and its children.
	 * 
	 * @return the mapping between packages' names
	 */
	public Map<String, Collection<String>> getExistingPackages();

}
