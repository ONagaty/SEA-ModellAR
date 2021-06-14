/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Jun 2, 2013 3:40:16 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.sea.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.runtime.ecuc.internal.sea.util.UnmodifiableSEAEList;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAObject;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.SEAWriteOperationException;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Sea internal utility
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Wed Apr 29 17:08:02 2015 %
 *
 *         %version: 3 %
 */
public final class SeaUtils
{
	static
	{
		// just to reach 100% code coverage
		new SeaUtils();
	}

	private SeaUtils()
	{
		// hide
	}

	/**
	 * @param size
	 *        size of the list
	 * @param data
	 *        the underlying storage of the list
	 * @return an unmodifiable view of the list
	 */
	public static <E> ISEAList<E> unmodifiableSEAEList(int size, List<E> data)
	{
		return new UnmodifiableSEAEList<E>(size, data.toArray());
	}

	/**
	 * @return an empty, unmodifiable list
	 */
	public static final <E> ISEAList<E> emptyList()
	{
		List<E> emptyList = Collections.emptyList();
		return unmodifiableSEAEList(0, emptyList);
	}

	/**
	 * @param parent
	 * @return the qualified name of the <code>parent</code>'s wrapped element
	 */
	public static String getQualifiedName(ISEAContainerParent parent)
	{
		EObject fragment = null;
		if (parent instanceof ISEAConfig)
		{

			List<GModuleConfiguration> configurations = ((ISEAConfig) parent).arGetConfigurations();
			fragment = configurations.get(0);
		}
		else
		{
			List<GContainer> containers = ((ISEAContainer) parent).arGetContainers();
			fragment = containers.get(0);
		}

		if (fragment == null)
		{
			return null;
		}
		else
		{
			return ModelUtils.getAbsoluteQualifiedName(fragment);
		}
	}

	/**
	 * @param containerParent
	 *        {@link ISEAContainerParent} to be checked whether split
	 * @return whether <code>containerParent</code> resides in more than one file
	 */
	public static boolean isSplit(ISEAContainerParent containerParent)
	{
		boolean isSplit = false;
		if (containerParent instanceof ISEAConfig)
		{
			List<GModuleConfiguration> configurations = ((ISEAConfig) containerParent).arGetConfigurations();
			isSplit = configurations.size() > 1;
		}
		else
		{
			List<GContainer> containers = ((ISEAContainer) containerParent).arGetContainers();
			isSplit = containers.size() > 1;
		}

		return isSplit;
	}

	/**
	 * @param containerParent
	 *        the {@link ISEAContainerParent} for which to return the containing {@link ISEAConfig}
	 * @return the SEA configuration wrapper for the given SEA container parent wrapper, or the
	 *         <code>containerParent</code> itself if it is a SEA configuration wrapper; could be <code>null</code>
	 * */
	public static ISEAConfig getConfiguration(ISEAContainerParent containerParent)
	{
		if (containerParent instanceof ISEAConfig)
		{
			return (ISEAConfig) containerParent;
		}

		ISEAConfig seaConfig = null;

		ISEAObject parent = containerParent.getParent();
		while (parent != null)
		{
			if (parent instanceof ISEAConfig)
			{
				seaConfig = (ISEAConfig) parent;
				break;
			}
			else if (parent instanceof ISEAContainer)
			{
				parent = ((ISEAContainer) parent).getParent();
			}
			else
			{
				break;
			}
		}

		return seaConfig;
	}

	/**
	 * Checks whether the wrapped element is split and if so, throws an unchecked exception. <br>
	 * The method should be called internally, as a safety measure, before altering the parent in case a certain write
	 * operation is not supported.
	 *
	 * @param parent
	 *        element to be checked whether split
	 */
	public static void checkSplitStatus(ISEAContainerParent parent)
	{
		if (isSplit(parent))
		{
			String qualifiedName = SeaUtils.getQualifiedName(parent);
			throw new SEAWriteOperationException(NLS.bind(
				"Parent {0} is split! The operation is not currently supported!", qualifiedName)); //$NON-NLS-1$
		}
	}

	/**
	 * Checks if the given container represents an instance of the definition with the provided shortName.
	 *
	 * @param container
	 *        container to be checked
	 * @param defName
	 *        definition shortName
	 * @return whether the definition of the container has <code>defName</code> as shortName
	 */
	public static boolean isInstanceOfDefinition(ISEAContainer container, String defName)
	{
		return defName.equals(container.arGetDefinition().gGetShortName());
	}

	/**
	 * @param seaContainer
	 * @return whether all the fragments of the wrapped container are proxy
	 */
	public static boolean isProxy(ISEAContainer seaContainer)
	{
		boolean isProxy = false;

		List<GContainer> containers = seaContainer.arGetContainers();
		for (GContainer container: containers)
		{
			isProxy = container.eIsProxy();
			// found a fragment that is not proxy
			if (!isProxy)
			{
				break;
			}
		}

		return isProxy;
	}

	/**
	 * @param container
	 * @param referenceName
	 * @param nullForProxy
	 *        if the referred container is proxy, the flag dictates if <code>null</code> will be returned (
	 *        <code>nullForProxy</code>=true) or the referred container (<code>nullForProxy</code>=false)
	 * @return the container that is referred by <code>container</code> via <code>referenceName</code>, or
	 *         <code>null</code>
	 */
	public static ISEAContainer getReference(ISEAContainer container, String referenceName, boolean nullForProxy)
	{
		ISEAContainer referredContainer = null;

		ISEAContainer value = container.getReference(referenceName);
		if (value != null)
		{
			if (!isProxy(value))
			{
				referredContainer = value;
			}
			else
			{
				if (!nullForProxy)
				{
					referredContainer = value;
				}
			}
		}

		return referredContainer;
	}

	/**
	 * @param container
	 *        container to be deleted
	 * @return whether deletion was successful
	 */
	public static boolean deleteContainer(ISEAContainer container)
	{
		ISEAContainerParent parent = container.getParent();
		if (parent != null)
		{
			return parent.getContainers(container.arGetDefinition().gGetShortName()).remove(container);
		}
		// nothing to delete
		return false;
	}

}
