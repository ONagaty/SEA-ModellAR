/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 1, 2010 11:05:23 AM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.core.resources.IFile;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;

/**
 * Static Jar helper methods
 * 
 */
public final class JarUtils
{
	private static final String CLASS_FILE_EXTENSION = ".class"; //$NON-NLS-1$

	private JarUtils()
	{
		// do not instantiate
	}

	/**
	 * @param name
	 * @return
	 */
	private static String getBinaryName(String name)
	{
		if (name == null)
		{
			return null;
		}
		else
		{
			name = name.replace('/', '.').replace('\\', '.');
			if (name.endsWith(CLASS_FILE_EXTENSION))
			{
				name = name.substring(0, name.length() - CLASS_FILE_EXTENSION.length());
			}
			return name;
		}

	}

	/**
	 * Return a list of all classes from a particular jar that extends a
	 * particular class
	 * 
	 * @param <T>
	 * @param jarFile
	 * @param cl
	 * @param superClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Class<T>> getClasses(JarFile jarFile, ClassLoader loader,
		Class<T> superClass) throws Exception
	{
		List<Class<T>> result = new ArrayList<Class<T>>();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements())
		{
			JarEntry element = entries.nextElement();
			if (element.isDirectory())
			{
				// not interested in directories
				continue;
			}
			String name = element.getName();
			if (name.endsWith(CLASS_FILE_EXTENSION))
			{
				// convert from file name to class name
				name = getBinaryName(name);
				Class<?> cls = loader.loadClass(name);
				if (superClass.isAssignableFrom(cls))
				{
					result.add((Class<T>) cls);
				}
			}
		}
		return result;
	}

	/**
	 * Read an entry from a the manifest of a jar file
	 * 
	 * @param jarFile
	 * @param entryName
	 * @return
	 */
	public static String getJarManifestEntry(IFile jarFile, String entryName)
	{
		try
		{
			JarFile jar = new JarFile(jarFile.getLocation().toFile());
			try
			{
				Manifest manifest = jar.getManifest();
				if (manifest != null)
				{
					Attributes attributes = manifest.getMainAttributes();
					if (attributes != null)
					{
						return attributes.getValue(entryName);
					}
				}
			}
			finally
			{
				jar.close();
			}
		}
		catch (IOException e)
		{
			// log and ignore
			CessarPluginActivator.getDefault().logError(e);
		}
		return null;
	}
}
