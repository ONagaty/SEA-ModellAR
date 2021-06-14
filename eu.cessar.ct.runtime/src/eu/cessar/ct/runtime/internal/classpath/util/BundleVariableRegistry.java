/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 26, 2009 11:51:56 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.osgi.storagemanager.StorageManager;

import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * Keeps a memory cache of {@link BundleVariable} instances. <br>
 * </br> At first startup (or after running Cessar with -clean option), for each
 * bundle received, the registry will create an instance of BundleVariable and
 * will update memory cache. Also, with the help of a storage manager will
 * serialize the data on disk.
 * 
 * From second startup and on, will no longer create the bundle variables from
 * scratch , but will make use of the serialize data to update the cache memory. <br>
 * </br> NOTE: the serialized data will be located inside
 * configuration/eu.cessar.ct.runtime folder
 * 
 */
public final class BundleVariableRegistry
{
	private List<BundleVariable> bundleVarCache;
	private StorageManager diskCacheManager;

	/** the singleton */
	public static final BundleVariableRegistry INSTANCE = new BundleVariableRegistry();

	private BundleVariableRegistry()
	{
		bundleVarCache = Collections.synchronizedList(new ArrayList<BundleVariable>());

		URL url = Platform.getConfigurationLocation().getURL();
		String path = url.getPath() + "/" + CessarPluginActivator.PLUGIN_ID;
		diskCacheManager = new StorageManager(new File(path), "none");
	}

	/**
	 * Called by Activator on start() to open the storage manager
	 */
	public void init()
	{
		try
		{
			diskCacheManager.open(true);
		}
		catch (IOException e)
		{ // SUPPRESS CHECKSTYLE OK
			// ignore
		}
	}

	/**
	 * Called by Activator on stop() to close the storage manager
	 */
	public void dispose()
	{
		diskCacheManager.close();
	}

	/**
	 * 
	 * Create and return a list of CPEntries for given <code>bundleID</code> by
	 * avoiding duplicates.<br>
	 * 
	 * @param bundleID
	 * @param existingEntries
	 *        existing entries, read only list
	 * @return
	 */
	public List<IClasspathEntry> createCPEntries(String bundleID,
		List<IClasspathEntry> existingEntries)
	{
		BundleVariable bundleVariable = getBundleVariable(bundleID);

		List<IClasspathEntry> entries = bundleVariable.getClassPathEntries(existingEntries);

		updateDiskCache(bundleVariable);
		return entries;
	}

	/**
	 * Update disk cache if necessary
	 * 
	 * @param bundleVariable
	 */
	private void updateDiskCache(BundleVariable bundleVariable)
	{
		if (bundleVariable.isDirty())
		{
			synchronized (bundleVariable)
			{
				if (bundleVariable.isDirty())
				{
					// save or update storage
					String name = bundleVariable.getSymbolicName();

					try
					{
						String managedFile = getManagedFileName(name);

						File file = diskCacheManager.lookup(managedFile, false);
						File tempFile = diskCacheManager.createTempFile(managedFile);
						FileOutputStream fos = null;
						try
						{
							try
							{
								fos = new FileOutputStream(tempFile);
								ObjectOutputStream out = new ObjectOutputStream(fos);
								// serialize
								out.writeObject(bundleVariable);
								out.flush();
							}
							catch (FileNotFoundException e)
							{
								CessarPluginActivator.getDefault().logError(e);
							}
							finally
							{// close stream before
								if (fos != null)
								{
									fos.close();
								}
							}
							// update manager
							// add the new managed file
							if (file == null)
							{
								diskCacheManager.add(managedFile);
							}
							diskCacheManager.update(new String[] {managedFile},
								new String[] {tempFile.getName()});
						}
						catch (IOException e)
						{
							CessarPluginActivator.getDefault().logError(e);
						}
						// }
					}
					catch (IOException e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}

					// change flag
					bundleVariable.setDirty(false);
				}
			}
		}
	}

	/**
	 * Check inside memory if a bundle variable exists for bundle with given
	 * symbolic name <code>bundleID</code>, if not check on disk and put it in
	 * memory, else create it from scratch and put it in memory
	 * 
	 * @param bundleID
	 * @return corresponding bundle variable
	 */
	private BundleVariable getBundleVariable(String bundleID)
	{
		bundleID = bundleID.intern(); // SUPPRESS CHECKSTYLE OK

		synchronized (bundleID)
		{
			// 1: check inside memory
			// use classic for instead of iterator to be thread safe
			for (int i = 0; i < bundleVarCache.size(); i++)
			{
				BundleVariable bundleVar = bundleVarCache.get(i);
				if (bundleID.equals(bundleVar.getSymbolicName()))
				{
					// found in cache
					// bundleVar.setDirty(true);
					return bundleVar;
				}
			}

			// 2: check on disk
			FileInputStream fin = null;
			try
			{
				String managedFile = getManagedFileName(bundleID);
				File file = diskCacheManager.lookup(managedFile, false);
				if (file != null && file.canRead())
				{
					// found the file on disk
					fin = new FileInputStream(file);
					ObjectInputStream in = new ObjectInputStream(fin);
					try
					{
						// de-serialize
						BundleVariable bundleVar = (BundleVariable) in.readObject();
						if (bundleVar != null && bundleVar.isValid())
						{
							bundleVarCache.add(bundleVar);
							return bundleVar;
						}
					}
					catch (ClassNotFoundException e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}
				}
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e, Thread.currentThread().toString());
			}
			finally
			{
				if (fin != null)
				{
					try
					{
						fin.close();
					}
					catch (IOException e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}
				}
			}
			// 3: not in memory, not on disk->create it
			// and put it in memory; on updateCache put it also on disk
			BundleVariable bundleVariable = new BundleVariable(bundleID);
			bundleVarCache.add(bundleVariable);

			return bundleVariable;
		}
	}

	/**
	 * Return the name of managed file for given <code>bundleID</code>
	 * 
	 * @param bundleID
	 * @return
	 */
	private String getManagedFileName(String bundleID)
	{
		// /replace all "." with "_"
		StringBuffer sb = new StringBuffer(bundleID);
		while (-1 != sb.indexOf("."))
		{
			int i = sb.indexOf(".");
			sb.replace(i, i + 1, "_");
		}

		return sb.toString();
	}
}
